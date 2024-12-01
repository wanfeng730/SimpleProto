package cn.wanfeng.proto.record;

import cn.wanfeng.proto.constant.ProtoConstants;
import cn.wanfeng.proto.constants.SpExceptionMessage;
import cn.wanfeng.sp.exception.SpException;
import cn.wanfeng.proto.serial.DeserializeMethodContainer;
import cn.wanfeng.proto.serial.DeserializeUtils;
import cn.wanfeng.proto.serial.SerializeMethodContainer;
import cn.wanfeng.proto.serial.SerializeUtils;
import cn.wanfeng.proto.type.ProtoType;
import cn.wanfeng.proto.type.ProtoTypeConstants;
import cn.wanfeng.proto.type.ProtoTypeUtils;
import cn.wanfeng.proto.type.TypeMapContainer;
import cn.wanfeng.sp.util.ByteArrayUtils;
import cn.wanfeng.proto.value.ProtoValueConstants;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

/**
 * @date: 2024-02-15 22:22
 * @author: luozh
 * @since: 1.0
 */
public class ProtoRecordFactory {

    private static final int INDEX_NO_BYTE_START = 0;

    private static final int INDEX_NO_BYTE_COUNT = 2;

    private static final int TYPE_BYTE_START = 2;

    private static final int TEXT_TYPE_BYTE_COUNT = 3;
    private static final int STRING_TYPE_BYTE_COUNT = 2;
    private static final int COMMON_TYPE_BYTE_COUNT = 1;


    /**
     * flag -> Method(deserialize the byte array to java value)
     */
    private static final Map<Byte, Method> DESERIAL_VALUE_METHOD_MAP = DeserializeMethodContainer.toFlagMethodMap();
    private static final Map<Byte, Method> SERIAL_VALUE_METHOD_MAP = SerializeMethodContainer.toFlagMethodMap();
    private static final Map<Class<?>, ProtoType> CLASS_PROTO_TYPE_MAP = ProtoType.toClassEnumMap();
    private static final Map<Class<?>, Integer> CLASS_TYPE_LEN_MAP = ProtoType.toClassLengthMap();

    /**
     * convert all byte array to ProtoRecord list
     *
     * @param data byte array
     * @return ProtoRecord list
     */
    public static ProtoRecordContainer readBytesToRecordList(byte[] data) {
        ProtoRecordContainer container = ProtoRecordContainer.emptyContainer();
        boolean success = true;
        while (success) {
            RecordReadResult readResult = readBytesToRecord(data);
            data = readResult.getNextData();
            success = readResult.isSuccess();
            if (success) {
                container.putRecord(readResult.getRecord());
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

        // value[0][1]: indexNo
        byte indexData0 = data[INDEX_NO_BYTE_START];
        byte indexData1 = data[INDEX_NO_BYTE_START + 1];
        byte[] indexNoBytes = new byte[]{indexData0, indexData1};
        int indexNo = DeserializeUtils.twoByte2Int(indexNoBytes);
        builder = builder.indexNo(indexNo);

        // value[2]: type
        int valueLen;
        // type: smallint, int, long, double, boolean, date
        // last 6 bits of value[2]
        byte typeData0 = data[TYPE_BYTE_START];
        byte flag = ProtoTypeUtils.getTypeFlagFromData1(typeData0);
        int indexNoAndTypeLen;
        int totalLen;

        // LzhTODO: 代码优化
        if (ProtoTypeConstants.TEXT_FLAG == flag) {
            indexNoAndTypeLen = INDEX_NO_BYTE_COUNT + TEXT_TYPE_BYTE_COUNT;
            // type: string
            if (ProtoTypeUtils.isEmptyValue(typeData0)) {
                builder = builder.type(ProtoType.TEXT).len(0).value(ProtoValueConstants.EMPTY_TEXT);
                byte[] nextData = ByteArrayUtils.consumeByteArrayHead(data, indexNoAndTypeLen);
                return RecordReadResult.buildSuccessResult(nextData, builder.build());
            }
            byte typeData1 = data[TYPE_BYTE_START + 1];
            byte typeData2 = data[TYPE_BYTE_START + 2];
            // value[2] + value[3] + value[4] = len
            //get last 6 bit to a positive number
            int len1 = ProtoTypeUtils.getFlagByteLengthFromData1(typeData0);
            int len2 = DeserializeUtils.oneByte2Int(typeData1);
            int len3 = DeserializeUtils.oneByte2Int(typeData2);
            valueLen = len1 * 256 * 256 + len2 * 256 + len3;
            // value[5]... : value
            if (data.length - (INDEX_NO_BYTE_COUNT + TEXT_TYPE_BYTE_COUNT) < valueLen) {
                // LzhTODO: 异常原因统一格式化
                throw new SpException("the length of value(text) in value is not enough");
            }

            Object value;
            try {
                byte[] valueBytes = ByteArrayUtils.subByteArray(data, indexNoAndTypeLen, valueLen);
                value = DESERIAL_VALUE_METHOD_MAP.get(ProtoTypeConstants.TEXT_FLAG).invoke(builder, (Object) valueBytes);
            } catch (Exception e) {
                throw new SpException(e);
            }

            totalLen = indexNoAndTypeLen + valueLen;

            builder = builder.type(ProtoType.TEXT).valueLen(valueLen).len(totalLen).value(value);
            byte[] nextData = ByteArrayUtils.consumeByteArrayHead(data, totalLen);
            return RecordReadResult.buildSuccessResult(nextData, builder.build());

        } else if (ProtoTypeConstants.STRING_FLAG == flag) {
            indexNoAndTypeLen = INDEX_NO_BYTE_COUNT + STRING_TYPE_BYTE_COUNT;
            // type: string
            if (ProtoTypeUtils.isEmptyValue(typeData0)) {
                builder = builder.type(ProtoType.STRING).len(0).value(ProtoValueConstants.EMPTY_STRING);
                byte[] nextData = ByteArrayUtils.consumeByteArrayHead(data, indexNoAndTypeLen);
                return RecordReadResult.buildSuccessResult(nextData, builder.build());
            }
            //value[1] + value[2] = len
            //get last 6 bit to a positive number
            byte typeData1 = data[TYPE_BYTE_START + 1];
            int len1 = ProtoTypeUtils.getFlagByteLengthFromData1(typeData0);
            int len2 = DeserializeUtils.oneByte2Int(typeData1);
            valueLen = len1 * 256 + len2;
            // value[3]... : value
            if (data.length - indexNoAndTypeLen < valueLen) {
                // LzhTODO: 异常原因统一格式化
                throw new SpException("the length of value(string) in value is not enough");
            }

            Object value;
            try {
                byte[] valueBytes = ByteArrayUtils.subByteArray(data, indexNoAndTypeLen, valueLen);
                value = DESERIAL_VALUE_METHOD_MAP.get(ProtoTypeConstants.STRING_FLAG).invoke(builder, (Object) valueBytes);
            } catch (Exception e) {
                throw new SpException(e);
            }

            totalLen = indexNoAndTypeLen + valueLen;
            builder = builder.type(ProtoType.STRING).valueLen(valueLen).len(totalLen).value(value);
            byte[] nextData = ByteArrayUtils.consumeByteArrayHead(data, totalLen);
            return RecordReadResult.buildSuccessResult(nextData, builder.build());

        } else if (TypeMapContainer.FLAG_CLASS_MAP.containsKey(flag)) {
            ProtoType type = TypeMapContainer.FLAG_ENUM_MAP.get(flag);
            indexNoAndTypeLen = INDEX_NO_BYTE_COUNT + COMMON_TYPE_BYTE_COUNT;
            if (ProtoTypeUtils.isEmptyValue(typeData0)) {
                builder = builder.type(type).len(indexNoAndTypeLen).value(null);
                byte[] nextData = ByteArrayUtils.consumeByteArrayHead(data, indexNoAndTypeLen);
                return RecordReadResult.buildSuccessResult(nextData, builder.build());
            }

            valueLen = TypeMapContainer.FLAG_LENGTH_MAP.get(flag);
            // value[2]... : value
            if (data.length - indexNoAndTypeLen < valueLen) {
                // LzhTODO: 异常原因统一格式化
                throw new SpException("the length of value in value is not enough");
            }
            Object value;
            try {
                byte[] valueBytes = ByteArrayUtils.subByteArray(data, indexNoAndTypeLen, valueLen);
                value = DESERIAL_VALUE_METHOD_MAP.get(flag).invoke(valueBytes, new Object[]{valueBytes});
            } catch (Exception e) {
                throw new SpException(e);
            }

            totalLen = indexNoAndTypeLen + valueLen;
            builder = builder.type(type).valueLen(valueLen).len(totalLen).value(value);
            byte[] nextData = ByteArrayUtils.consumeByteArrayHead(data, totalLen);
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
        for (Map.Entry<Integer, ProtoRecord> entry : container.getIndexNoRecordMap().entrySet()) {
            ProtoRecord record = entry.getValue();
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
        byte[] indexNoBytes = SerializeUtils.int2TwoBytes(record.getIndexNo());
        System.arraycopy(indexNoBytes, 0, data, INDEX_NO_BYTE_START, INDEX_NO_BYTE_COUNT);

        int indexNoAndTypeLen;
        if (record.isTextType()) {
            indexNoAndTypeLen = INDEX_NO_BYTE_COUNT + TEXT_TYPE_BYTE_COUNT;
            // string len to 3 byte
            byte[] typeData = SerializeUtils.textLen2Bytes(record.getValueLen());
            // set text flag
            typeData[0] = (byte) (typeData[0] | ProtoTypeConstants.TEXT_FLAG);
            System.arraycopy(typeData, 0, data, TYPE_BYTE_START, TEXT_TYPE_BYTE_COUNT);
            // if value is null
            if (record.isEmptyValue()) {
                data[TYPE_BYTE_START] = ProtoTypeUtils.type0SetEmpty(data[TYPE_BYTE_START]);
                return ByteArrayUtils.subByteArray(data, 0, indexNoAndTypeLen);
            }
            // LzhTODO: 改成Text类可行？
            byte[] valueBytes = SerializeUtils.string2Bytes((String) record.getValue());
            System.arraycopy(valueBytes, 0, data, indexNoAndTypeLen, record.getValueLen());
        } else if (record.isStringType()) {
            indexNoAndTypeLen = INDEX_NO_BYTE_COUNT + STRING_TYPE_BYTE_COUNT;
            // string len to 2 byte
            byte[] typeData = SerializeUtils.stringLen2Bytes(record.getValueLen());
            // add string flag
            typeData[0] = (byte) (typeData[0] | ProtoTypeConstants.STRING_FLAG);
            System.arraycopy(typeData, 0, data, TYPE_BYTE_START, STRING_TYPE_BYTE_COUNT);
            // if value is null
            if (record.isEmptyValue()) {
                data[TYPE_BYTE_START] = ProtoTypeUtils.type0SetEmpty(data[TYPE_BYTE_START]);
                return ByteArrayUtils.subByteArray(data, 0, indexNoAndTypeLen);
            }

            byte[] valueBytes = SerializeUtils.string2Bytes((String) record.getValue());
            System.arraycopy(valueBytes, 0, data, indexNoAndTypeLen, record.getValueLen());
        } else {
            indexNoAndTypeLen = INDEX_NO_BYTE_COUNT + COMMON_TYPE_BYTE_COUNT;

            byte flag = record.getType().toFlag();
            data[TYPE_BYTE_START] = flag;
            // if value is null
            if (record.isEmptyValue()) {
                data[TYPE_BYTE_START] = ProtoTypeUtils.type0SetEmpty(data[TYPE_BYTE_START]);
                return ByteArrayUtils.subByteArray(data, 0, indexNoAndTypeLen);
            }
            try {
                byte[] valueData = (byte[]) SERIAL_VALUE_METHOD_MAP.get(flag).invoke(record, record.getValue());
                System.arraycopy(valueData, 0, data, indexNoAndTypeLen, record.getValueLen());
            } catch (Exception e) {
                throw new SpException(e);
            }
        }

        return data;
    }

    /**
     * value不为null调用该方法构建ProtoRecord
     * @param index indexNo
     * @param value 值
     * @return ProtoRecord
     */
    public static ProtoRecord buildProtoRecordByIndexAndValue(int index, Object value){
        return buildProtoRecordByIndexAndValue(index, value.getClass(), value);
    }

    /**
     * value为null调用该方法构建ProtoRecord，手动提供Class
     * @param index indexNo
     * @param valueClass 值的类型
     * @param value 值
     * @return ProtoRecord
     */
    public static ProtoRecord buildProtoRecordByIndexAndValue(int index, Class<?> valueClass, Object value) {
        assertClassMappingProtoType(valueClass, value);
        ProtoRecordBuilder builder = ProtoRecord.newBuilder();
        builder.indexNo(index);
        builder.value(value);
        if(value == null){
            ProtoType protoType = CLASS_PROTO_TYPE_MAP.get(valueClass);
            int typeByteLen = valueClass == String.class ? 2 : 1;
            builder.type(protoType);
            builder.valueLen(0);
            builder.len(INDEX_NO_BYTE_COUNT + typeByteLen);
            return builder.build();
        }
        if (value instanceof String) {
            builder.type(ProtoType.STRING);
            int valueLen = ((String) value).getBytes(ProtoConstants.UTF8_CHARSET).length;
            if (valueLen > ProtoTypeConstants.TEXT_MAX_LENGTH) {
                throw new SpException(SpExceptionMessage.stringValueLengthTooLong(valueLen));
            } else if (valueLen > ProtoTypeConstants.STRING_MAX_LENGTH) {
                builder.valueLen(valueLen);
                builder.len(valueLen + INDEX_NO_BYTE_COUNT + TEXT_TYPE_BYTE_COUNT);
            } else {
                builder.valueLen(valueLen);
                builder.len(valueLen + INDEX_NO_BYTE_COUNT + STRING_TYPE_BYTE_COUNT);
            }
            return builder.build();
        }
        ProtoType type = TypeMapContainer.CLASS_ENUM_MAP.get(value.getClass());
        builder.type(type);
        Integer valueLen = TypeMapContainer.CLASS_LENGTH_MAP.get(value.getClass());
        builder.valueLen(valueLen);
        builder.len(valueLen + INDEX_NO_BYTE_COUNT + COMMON_TYPE_BYTE_COUNT);
        return builder.build();
    }


    /**
     * 确认类型能被proto序列化
     * 1. 若value有值，取value.getClass()
     * 2. 若value为空，取反射获取的clazz
     * @param clazz 字段声明的类
     * @param value 值
     */
    private static void assertClassMappingProtoType(Class<?> clazz, Object value){
        Class<?> assertClass = Objects.nonNull(value) ? value.getClass() : clazz;
        if(!CLASS_PROTO_TYPE_MAP.containsKey(assertClass)){
            throw new SpException("类型[%s]不支持SimpleProto序列化", assertClass.getName());
        }
    }
}
