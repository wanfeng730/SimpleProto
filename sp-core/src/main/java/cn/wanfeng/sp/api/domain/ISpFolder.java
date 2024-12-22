package cn.wanfeng.sp.api.domain;


/**
 * @date: 2024-12-22 13:56
 * @author: luozh.wanfeng
 * @description:
 * @since:
 */
public interface ISpFolder extends ISpSysObject{

    /**
     * 获取包含的文件夹数量
     */
    int getChildFolderCount();

    /**
     * 获取包含的文件数量
     */
    int getChildFileCount();


    // /**
    //  * 获取文件夹大小（统计文件夹下所有文件的大小总和）
    //  * @return 字节数
    //  */
    // long getSize();
    //
    // /**
    //  * 获取文件夹大小并转换单位（统计文件夹下所有文件的大小总和）
    //  * @param unit 单位
    //  * @return 加上单位后的大小
    //  */
    // String getSizeByUnit(ByteUnit unit);
}
