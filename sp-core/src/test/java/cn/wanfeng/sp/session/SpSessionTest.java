package cn.wanfeng.sp.session;


import cn.wanfeng.proto.util.LogUtils;
import cn.wanfeng.sp.SimpleprotoApplicationTest;
import cn.wanfeng.sp.base.object.SpSettingsDO;
import cn.wanfeng.sp.config.SimpleProtoConfig;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @date: 2024-11-06 00:21
 * @author: luozh.wanfeng
 * @description:
 * @since:
 */
public class SpSessionTest extends SimpleprotoApplicationTest {

    @Resource
    private SpSession spSession;

    @Test
    public void testInsertSettings() {
        String settingsName = "testSettings_0";
        SpSettingsDO settingsDO = new SpSettingsDO(settingsName, 730L, "730");
        int insertRows = spSession.databaseStorage().insertSettings(SimpleProtoConfig.settingsTable, settingsDO);
        Assertions.assertEquals(1, insertRows);

        settingsDO = spSession.databaseStorage().findSettingsByName(SimpleProtoConfig.settingsTable, settingsName);
        Assertions.assertEquals(settingsName, settingsDO.getName());
        Assertions.assertEquals(730L, settingsDO.getIncreaseLong());
        Assertions.assertEquals("730", settingsDO.getIncreaseString());

        settingsDO.setIncreaseLong(998L);
        settingsDO.setIncreaseString("777");
        int updateRows = spSession.databaseStorage().updateSettings(SimpleProtoConfig.settingsTable, settingsDO);
        Assertions.assertEquals(1, updateRows);

        settingsDO = spSession.databaseStorage().findSettingsByName(SimpleProtoConfig.settingsTable, settingsName);
        Assertions.assertEquals(settingsName, settingsDO.getName());
        Assertions.assertEquals(998L, settingsDO.getIncreaseLong());
        Assertions.assertEquals("777", settingsDO.getIncreaseString());

        LogUtils.info("设置表新建设置测试完成");
    }
}
