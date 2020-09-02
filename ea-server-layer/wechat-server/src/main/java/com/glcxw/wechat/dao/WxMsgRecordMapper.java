package com.glcxw.wechat.dao;

import com.glcxw.wechat.dao.provider.WxMsgRecordProvider;
import com.glcxw.wechat.model.WxMsgRecord;
import org.apache.ibatis.annotations.*;

/**
 *
 * @author aoki
 */
@Mapper
public interface WxMsgRecordMapper {

    /**
     * 插入数据
     * @param wxMsgRecord
     * @return
     */
    @InsertProvider(type = WxMsgRecordProvider.class,method = "insert")
    @Options(keyColumn="msgId")
    int insert(WxMsgRecord wxMsgRecord);

    /**
     * 查询记录
     * @param msgId
     * @return
     */
    @SelectProvider(type = WxMsgRecordProvider.class,method = "selByMsgId")
    WxMsgRecord selOrderByQrNo(@Param("msgId") String msgId);

    /**
     * 更新数据
     * @param wxMsgRecord
     * @return
     */
    @UpdateProvider(type = WxMsgRecordProvider.class,method="update")
    int update(WxMsgRecord wxMsgRecord);
}