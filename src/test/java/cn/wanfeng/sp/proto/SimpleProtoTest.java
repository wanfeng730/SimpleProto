package cn.wanfeng.sp.proto;

import cn.wanfeng.sp.proto.record.ProtoRecord;
import cn.wanfeng.sp.proto.record.ProtoRecordContainer;
import cn.wanfeng.sp.proto.record.ProtoRecordFactory;
import cn.wanfeng.sp.proto.serial.DeserializeMethodContainer;
import cn.wanfeng.sp.proto.serial.DeserializeUtils;
import cn.wanfeng.sp.proto.serial.SerializeMethodContainer;
import cn.wanfeng.sp.proto.type.ProtoType;
import cn.wanfeng.sp.proto.type.TypeConstants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Date;

/**
 * @date: 2024-02-06 22:49
 * @author: luozh
 */
public class SimpleProtoTest {

    private static final String LONG_TEXT = "Available space is less and varies due to many factors. A standard configuration uses approximately 12GB to 17GB of space, including iOS 17 with its latest features and Apple apps that can be deleted. Apple apps that can be deleted use about 4.5GB of space, and you can download them back from the App Store. Storage capacity subject to change based on software version, settings, and iPhone model.\n" +
            "Size and weight vary by configuration and manufacturing process.\n" +
            "iPhone 15 and iPhone 15 Plus are splash, water, and dust resistant and were tested under controlled laboratory conditions with a rating of IP68 under IEC standard 60529 (maximum depth of 6 meters up to 30 minutes). Splash, water, and dust resistance are not permanent conditions. Resistance might decrease as a result of normal wear. Do not attempt to charge a wet iPhone; refer to the user guide for cleaning and drying instructions. Liquid damage not covered under warranty.\n" +
            "Apple Cash services are provided by Green Dot Bank, Member FDIC. Learn more about the Terms and Conditions. Only available in the U.S. on eligible devices. To send and receive money with an Apple Cash account, you must be 18 and a U.S. resident. If you’re under 18, your family organizer can set up Apple Cash for you as part of their Apple Cash Family account, but you may not be able to access features that require a supported payment card. Security checks may require more time to make funds available. Apple Cash Family accounts can send up to $2000 per transaction and receive up to $2000 within a seven‑day period. To access and use all Apple Cash features, you must have an eligible device with Wallet that supports and has the latest version of iOS.\n" +
            "Available only in select cities and transit systems. Requires eligible device and OS version. See here for details.\n" +
            "Merchant offers may change at any time.\n" +
            "Apple Card Family Participants and Co-Owners do not need to have a familial relationship but must be part of the same Apple Family Sharing group.\n" +
            "Service is included for free for two years with the activation of any iPhone 15 model. Connection and response times vary based on location, site conditions, and other factors. See support.apple.com/kb/HT213885 for more information.\n" +
            "iPhone 15 and iPhone 15 Pro can detect a severe car crash and call for help. Requires a cellular connection or Wi‑Fi calling.\n" +
            "Data plan required. 5G, Gigabit LTE, VoLTE, and Wi-Fi calling are available in select markets and through select carriers. Speeds are based on theoretical throughput and vary based on site conditions and carrier. For details on 5G and LTE support, contact your carrier and see apple.com/iphone/cellular.\n" +
            "Ultra Wideband availability varies by region.\n" +
            "FaceTime calling requires a FaceTime‑enabled device for the caller and recipient and a Wi‑Fi connection. Availability over a cellular network depends on carrier policies; data charges may apply.";

    @Test
    public void test_DeserializeUtils() {
        byte data = -1;   // 1111 1111
        int i = DeserializeUtils.oneByte2Int(data);
        Assertions.assertEquals(255, i);
    }

    @Test
    public void test_DeserializeMethodContainer() {
        byte[] smallIntBytes = {126};
        Byte b = DeserializeMethodContainer.deserializeBytes2SmallInt(smallIntBytes);
        Assertions.assertTrue(byteArrayEqual(smallIntBytes, new byte[]{b}));

        byte[] intBytes = new byte[]{0, -23, 45, 98};
        Integer intValue = DeserializeMethodContainer.deserializeBytes2Int(intBytes);
        byte[] intBytes2 = SerializeMethodContainer.serializeInt2Bytes(intValue);
        Assertions.assertTrue(byteArrayEqual(intBytes, intBytes2));

        byte[] longBytes = new byte[]{-1, 0, 43, 98, 9, -41, -5, 0};
        Long longValue = DeserializeMethodContainer.deserializeBytes2Long(longBytes);
        byte[] long2Bytes = SerializeMethodContainer.serializeLong2Bytes(longValue);
        Assertions.assertTrue(byteArrayEqual(longBytes, long2Bytes));

        double d1 = -23424.986521;
        byte[] double2Bytes = SerializeMethodContainer.serializeDouble2Bytes(d1);
        Double d2 = DeserializeMethodContainer.deserializeBytes2Double(double2Bytes);
        Assertions.assertEquals(d1, d2);

        long millis = System.currentTimeMillis();
        Date date1 = new Date(millis);
        byte[] dateBytes = SerializeMethodContainer.serializeDate2Bytes(date1);
        Date date = DeserializeMethodContainer.deserializeBytes2Date(dateBytes);
        Assertions.assertEquals(date1, date);

        Boolean b1 = Boolean.TRUE;
        byte[] booleanBytes = SerializeMethodContainer.serializeBoolean2Bytes(b1);
        Boolean b2 = DeserializeMethodContainer.deserializeBytes2Boolean(booleanBytes);
        Assertions.assertEquals(b1, b2);

        String s1 = "aaaAAA晚风";
        byte[] stringBytes = SerializeMethodContainer.serializeString2Bytes(s1);
        String s2 = DeserializeMethodContainer.deserializeBytes2String(stringBytes);
        Assertions.assertEquals(s1, s2);


        System.out.println();
    }

    @Test
    public void test() {
        ProtoRecordContainer bigContainer = buildTestContainer();
        byte[] data3 = ProtoRecordFactory.writeRecordListToBytes(bigContainer);
        System.out.println(bytesToHexString(data3));
        ProtoRecordContainer bigContainer2 = ProtoRecordFactory.readBytesToRecordList(data3);
        System.out.println();
    }


    @Test
    public void test_print() {
        byte b = -65;
        System.out.println(byteToBineryString(b));

    }

    private static ProtoRecordContainer buildTestContainer() {
        ProtoRecordContainer container = ProtoRecordContainer.emptyContainer();
        ProtoRecord record1 = ProtoRecord.newBuilder().indexNo(5).type(ProtoType.INT).valueLen(TypeConstants.INT_LENGTH).len(TypeConstants.INT_LENGTH + 2).value(-3452553).build();
        ProtoRecord record2 = ProtoRecord.newBuilder().indexNo(15).type(ProtoType.LONG).valueLen(TypeConstants.LONG_LENGTH).len(TypeConstants.LONG_LENGTH + 2).value(-323234452553L).build();
        ProtoRecord record3 = ProtoRecord.newBuilder().indexNo(50).type(ProtoType.DOUBLE).valueLen(TypeConstants.DOUBLE_LENGTH).len(TypeConstants.DOUBLE_LENGTH + 2).value(9845.2553).build();
        ProtoRecord record4 = ProtoRecord.newBuilder().indexNo(90).type(ProtoType.DATE).valueLen(TypeConstants.DATE_LENGTH).len(TypeConstants.DATE_LENGTH + 2).value(new Date()).build();
        ProtoRecord record5 = ProtoRecord.newBuilder().indexNo(175).type(ProtoType.BOOLEAN).valueLen(TypeConstants.BOOLEAN_LENGTH).len(TypeConstants.BOOLEAN_LENGTH + 2).value(Boolean.FALSE).build();
        //ProtoRecord record6 = ProtoRecord.newBuilder().indexNo(255).type(ProtoType.STRING).valueLen(8745).len(8745 + 3).value(LONG_TEXT + LONG_TEXT + LONG_TEXT).build();
        container.addRecords(Arrays.asList(record1, record2, record3, record4, record5));
        return container;
    }

    private static ProtoRecordContainer buildNullValueTestContainer() {
        ProtoRecordContainer container = ProtoRecordContainer.emptyContainer();
        ProtoRecord record1 = ProtoRecord.newBuilder().indexNo(5).type(ProtoType.INT).valueLen(0).len(2).value(null).build();
        ProtoRecord record2 = ProtoRecord.newBuilder().indexNo(15).type(ProtoType.LONG).valueLen(0).len(2).value(null).build();
        ProtoRecord record3 = ProtoRecord.newBuilder().indexNo(50).type(ProtoType.DOUBLE).valueLen(0).len(2).value(null).build();
        ProtoRecord record4 = ProtoRecord.newBuilder().indexNo(90).type(ProtoType.DATE).valueLen(0).len(2).value(null).build();
        ProtoRecord record5 = ProtoRecord.newBuilder().indexNo(175).type(ProtoType.BOOLEAN).valueLen(0).len(2).value(null).build();
        ProtoRecord record6 = ProtoRecord.newBuilder().indexNo(255).type(ProtoType.STRING).valueLen(0).len(3).value(null).build();
        container.addRecords(Arrays.asList(record1, record2, record3, record4, record5, record6));
        return container;
    }


    private static String byteToBineryString(byte nByte) {
        StringBuilder nStr = new StringBuilder();
        for (int i = 7; i >= 0; i--) {
            int j = (int) nByte & (int) (Math.pow(2, i));
            if (j > 0) {
                nStr.append("1");
            } else {
                nStr.append("0");
            }
        }
        return nStr.toString();
    }

    public static String bytesToHexString(byte[] inByte) {

        StringBuilder sb = new StringBuilder();
        String hexString;

        for (int i = 0; i < inByte.length; i++) {

            //toHexString方法用于将16进制参数转换成无符号整数值的字符串
            String hex = Integer.toHexString(inByte[i]);

            if (hex.length() == 1) {
                sb.append("0");//当16进制为个位数时，在前面补0
            }
            sb.append(hex);//将16进制加入字符串
            sb.append(" ");//16进制字符串后补空格区分开

        }

        hexString = sb.toString();
        hexString = hexString.toUpperCase();//将16进制字符串中的字母大写

        return hexString;

    }

    private static boolean byteArrayEqual(byte[] arr1, byte[] arr2) {
        if (arr1 == null && arr2 == null) {
            return true;
        } else if (arr1 == null || arr2 == null) {
            return false;
        }

        if (arr1.length != arr2.length) {
            return false;
        }
        for (int i = 0; i < arr1.length; i++) {
            if (arr1[i] != arr2[i]) {
                return false;
            }
        }
        return true;
    }


}
