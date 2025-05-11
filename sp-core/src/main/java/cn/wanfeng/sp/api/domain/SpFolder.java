package cn.wanfeng.sp.api.domain;


import cn.wanfeng.sp.api.enums.SystemTag;
import cn.wanfeng.sp.config.custom.SimpleProtoConfig;
import cn.wanfeng.sp.session.SpSession;
import org.apache.commons.lang3.StringUtils;

/**
 * @date: 2024-12-22 13:47
 * @author: luozh.wanfeng
 * @description: 文件夹对象
 * @since: 1.0
 */
public class SpFolder extends SpSysObject implements ISpFolder {

    public SpFolder(SpSession session, String type, String name, ISpSysObject parentSysObject) {
        super(session, type, name, parentSysObject, SystemTag.FOLDER);
    }

    public SpFolder(SpSession session, String type, String name) {
        super(session, type, name);
        setSystemTag(SystemTag.FOLDER);
    }

    public SpFolder(SpSession session, Long id) {
        super(session, id);
    }


    @Override
    public int getChildFolderCount() {
        int count = 0;
        if(StringUtils.isNotBlank(this.path)){
            count = session.databaseStorage().countObjectByLikePathAndTag(SimpleProtoConfig.dataTable, this.path + "/%", SystemTag.FOLDER.getValue());
        }
        return count;
    }

    @Override
    public int getChildFileCount() {
        int count = 0;
        if(StringUtils.isNotBlank(this.path)){
            count = session.databaseStorage().countObjectByLikePathAndTag(SimpleProtoConfig.dataTable, this.path + "/%", SystemTag.FILE.getValue());
        }
        return count;
    }
}
