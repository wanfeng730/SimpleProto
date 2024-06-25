package cn.wanfeng.sp.proto.record;

import cn.wanfeng.sp.constants.SpFailedReason;
import cn.wanfeng.sp.exception.SpException;
import cn.wanfeng.sp.proto.constant.ProtoConstants;
import cn.wanfeng.sp.proto.serial.DeserializeMethodContainer;
import cn.wanfeng.sp.proto.serial.DeserializeUtils;
import cn.wanfeng.sp.proto.serial.SerializeMethodContainer;
import cn.wanfeng.sp.proto.serial.SerializeUtils;
import cn.wanfeng.sp.proto.type.ProtoType;
import cn.wanfeng.sp.proto.type.ProtoTypeConstants;
import cn.wanfeng.sp.proto.type.ProtoTypeUtils;
import cn.wanfeng.sp.proto.type.TypeMapContainer;
import cn.wanfeng.sp.proto.value.ProtoValueConstants;
import cn.wanfeng.sp.util.ByteArrayUtils;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @date: 2024-02-15 22:22
 * @author: luozh
 * @since: 1.0
 */
public class ProtoRecordFactory {

    private static final byte FLAG_MAT = 63;

    /**
     * flag -> Method(deserialize the byte array to java value)
     */
    private static final Map<Byte, Method> DESERIAL_VALUE_METHOD_MAP = DeserializeMethodContainer.toFlagMethodMap();
    private static final Map<Byte, Method> SERIAL_VALUE_METHOD_MAP = SerializeMethodContainer.toFlagMethodMap();

    /**
     * convert all byte array to ProtoRecord list
     *
     * @param data byte array
     * @return ProtoRecord list
     */
    // LzhTODO: text类型测试
    public static ProtoRecordContainer readBytesToRecordList(byte[] data) {
        ProtoRecordContainer container = ProtoRecordContainer.emptyContainer();
        boolean success = true;
        while (success) {
            RecordReadResult readResult = readBytesToRecord(data);
            data = readResult.getNextData();
            success = readResult.isSuccess();
            if (success) {
                container.addRecord(readResult.getRecord());
            }
        }
        return container;
    }

    /**
     * convert byte array to a ProtoRecord
     *
     * @param data byte array
     * @return next byte array to write, ProtoRecord, success?
     */
    private static RecordReadResult readBytesToRecord(byte[] data) {
        if (data == null || data.length < 2) {
            return RecordReadResult.buildFailedResult();
        }

        ProtoRecordBuilder builder = ProtoRecord.newBuilder();

        //value[0]: indexNo
        int indexNo = DeserializeUtils.oneByte2Int(data[0]);
        builder = builder.indexNo(indexNo);

        //value[1]: type
        int valueLen;
        // type: smallint, int, long, double, boolean, date
        // last 6 bits of value[1]
        byte flag = ProtoTypeUtils.getTypeFlagFromData1(data[1]);

        // LzhTODO: 代码优化
        if (ProtoTypeConstants.TEXT_FLAG == flag) {
            // type: string
            if (ProtoTypeUtils.isEmptyValue(data[1])) {
                builder = builder.type(ProtoType.TEXT).len(0).value(ProtoValueConstants.EMPTY_TEXT);
                byte[] nextData = ByteArrayUtils.consumeByteArrayHead(data, 4);
                return RecordReadResult.buildSuccessResult(nextData, builder.build());
            }
            //value[1] + value[2] + value[3] = len
            //get last 6 bit to a positive number
            int len1 = ProtoTypeUtils.getFlagByteLengthFromData1(data[1]);
            int len2 = DeserializeUtils.oneByte2Int(data[2]);
            int len3 = DeserializeUtils.oneByte2Int(data[3]);
            valueLen = len1 * 256 * 256 + len2 * 256 + len3;
            // value[4]... : value
            if (data.length - 4 < valueLen) {
                // LzhTODO: 异常原因统一格式化
                throw new SpException("the length of value(text) in value is not enough");
            }

            Object value;
            try {
                byte[] valueBytes = ByteArrayUtils.subByteArray(data, 4, valueLen);
                value = DESERIAL_VALUE_METHOD_MAP.get(ProtoTypeConstants.TEXT_FLAG).invoke(builder, (Object) valueBytes);
            } catch (Exception e) {
                throw new SpException(e);
            }

            builder = builder.type(ProtoType.TEXT).valueLen(valueLen).len(valueLen + 4).value(value);
            byte[] nextData = ByteArrayUtils.consumeByteArrayHead(data, 4 + valueLen);
            return RecordReadResult.buildSuccessResult(nextData, builder.build());

        } else if (ProtoTypeConstants.STRING_FLAG == flag) {
            // type: string
            if (ProtoTypeUtils.isEmptyValue(data[1])) {
                builder = builder.type(ProtoType.STRING).len(0).value(ProtoValueConstants.EMPTY_STRING);
                byte[] nextData = ByteArrayUtils.consumeByteArrayHead(data, 3);
                return RecordReadResult.buildSuccessResult(nextData, builder.build());
            }
            //value[1] + value[2] = len
            //get last 6 bit to a positive number
            int len1 = ProtoTypeUtils.getFlagByteLengthFromData1(data[1]);
            int len2 = DeserializeUtils.oneByte2Int(data[2]);
            valueLen = len1 * 256 + len2;
            // value[3]... : value
            if (data.length - 3 < valueLen) {
                // LzhTODO: 异常原因统一格式化
                throw new SpException("the length of value(string) in value is not enough");
            }

            Object value;
            try {
                byte[] valueBytes = ByteArrayUtils.subByteArray(data, 3, valueLen);
                value = DESERIAL_VALUE_METHOD_MAP.get(ProtoTypeConstants.STRING_FLAG).invoke(builder, (Object) valueBytes);
            } catch (Exception e) {
                throw new SpException(e);
            }

            builder = builder.type(ProtoType.STRING).valueLen(valueLen).len(valueLen + 3).value(value);
            byte[] nextData = ByteArrayUtils.consumeByteArrayHead(data, 3 + valueLen);
            return RecordReadResult.buildSuccessResult(nextData, builder.build());

        } else if (TypeMapContainer.FLAG_CLASS_MAP.containsKey(flag)) {
            ProtoType type = TypeMapContainer.FLAG_ENUM_MAP.get(flag);
            if (ProtoTypeUtils.isEmptyValue(data[1])) {
                builder = builder.type(type).len(0).value(null);
                byte[] nextData = ByteArrayUtils.consumeByteArrayHead(data, 2);
                return RecordReadResult.buildSuccessResult(nextData, builder.build());
            }

            valueLen = TypeMapContainer.FLAG_LENGTH_MAP.get(flag);
            // value[2]... : value
            if (data.length - 2 < valueLen) {
                // LzhTODO: 异常原因统一格式化
                throw new SpException("the length of value in value is not enough");
            }
            Object value;
            try {
                byte[] valueBytes = ByteArrayUtils.subByteArray(data, 2, valueLen);
                value = DESERIAL_VALUE_METHOD_MAP.get(flag).invoke(valueBytes, new Object[]{valueBytes});
            } catch (Exception e) {
                throw new SpException(e);
            }

            builder = builder.type(type).valueLen(valueLen).len(valueLen + 2).value(value);
            byte[] nextData = ByteArrayUtils.consumeByteArrayHead(data, 2 + valueLen);
            return RecordReadResult.buildSuccessResult(nextData, builder.build());
        } else {
            throw new SpException("the type of record can not resolve!");
        }
    }

    // LzhTODO: text类型测试
    public static byte[] writeRecordListToBytes(ProtoRecordContainer container) {
        if (container.isEmpty()) {
            return null;
        }
        // get total size
        byte[] data = new byte[container.getTotalSize()];
        int cursor = 0;
        for (int i = 0; i < container.getRecordList().size(); i++) {
            ProtoRecord record = container.getRecordList().get(i);
            byte[] recordData = writeRecordToBytes(record);
            int len = recordData.length;
            System.arraycopy(recordData, 0, data, cursor, len);
            cursor += len;
        }

        return data;
    }

    private static byte[] writeRecordToBytes(ProtoRecord record) {
        byte[] data = new byte[record.getLen()];
        // indexNo
        data[0] = (byte) record.getIndexNo();

        if (record.isTextType()) {
            // string len to 3 byte
            byte[] typeData = SerializeUtils.textLen2Bytes(record.getValueLen());
            // set text flag
            typeData[0] = (byte) (typeData[0] | ProtoTypeConstants.TEXT_FLAG);
            System.arraycopy(typeData, 0, data, 1, 3);
            // if value is null
            if (record.isEmptyValue()) {
                data[1] = ProtoTypeUtils.type0SetEmpty(data[1]);
                return ByteArrayUtils.subByteArray(data, 0, 3);
            }
            // LzhTODO: 改成Text类可行？
            byte[] valueBytes = SerializeUtils.string2Bytes((String) record.getValue());
            System.arraycopy(valueBytes, 0, data, 4, record.getValueLen());
        } else if (record.isStringType()) {
            // string len to 2 byte
            byte[] typeData = SerializeUtils.stringLen2Bytes(record.getValueLen());
            // add string flag
            typeData[0] = (byte) (typeData[0] | ProtoTypeConstants.STRING_FLAG);
            System.arraycopy(typeData, 0, data, 1, 2);
            // if value is null
            if (record.isEmptyValue()) {
                data[1] = ProtoTypeUtils.type0SetEmpty(data[1]);
                return ByteArrayUtils.subByteArray(data, 0, 3);
            }

            byte[] valueBytes = SerializeUtils.string2Bytes((String) record.getValue());
            System.arraycopy(valueBytes, 0, data, 3, record.getValueLen());
        } else {
            byte flag = record.getType().toFlag();
            data[1] = flag;
            // if value is null
            if (record.isEmptyValue()) {
                data[1] = ProtoTypeUtils.type0SetEmpty(data[1]);
                return ByteArrayUtils.subByteArray(data, 0, 2);
            }
            try {
                byte[] valueData = (byte[]) SERIAL_VALUE_METHOD_MAP.get(flag).invoke(record, record.getValue());
                System.arraycopy(valueData, 0, data, 2, record.getValueLen());
            } catch (Exception e) {
                throw new SpException(e);
            }
        }

        return data;
    }

    public static ProtoRecord buildProtoRecordByIndexAndValue(int index, Object value) {
        ProtoRecordBuilder builder = ProtoRecord.newBuilder();
        builder.indexNo(index);
        builder.value(value);
        if (value instanceof String) {
            builder.type(ProtoType.STRING);
            int valueLen = ((String) value).getBytes(ProtoConstants.UTF8_CHARSET).length;
            if (valueLen > ProtoTypeConstants.TEXT_MAX_LENGTH) {
                throw new SpException(SpFailedReason.stringValueLengthTooLong(valueLen));
            } else if (valueLen > ProtoTypeConstants.STRING_MAX_LENGTH) {
                // LzhTODO: 升级为text类型

            } else {
                builder.valueLen(valueLen);
                builder.len(valueLen + 3);
            }
            return builder.build();
        }
        ProtoType type = TypeMapContainer.CLASS_ENUM_MAP.get(value.getClass());
        builder.type(type);
        Integer valueLen = TypeMapContainer.CLASS_LENGTH_MAP.get(value.getClass());
        builder.valueLen(valueLen);
        builder.len(valueLen + 2);
        return builder.build();
    }

}
