package com.glcxw.wechat.service;

import com.glcxw.wechat.model.WxMsgRecord;

/**
 * @author aoki
 */
public interface WxMsgRecordService {

    /**
     * 插入数据
     * @param wxMsgRecord
     * @return
     */
    int insert(WxMsgRecord wxMsgRecord);

    /**
     * 查询记录
     * @param msgId
     * @return
     */
    WxMsgRecord findOrderByQrNo(String msgId);

    /**
     * 更新数据
     * @param wxMsgRecord
     * @return
     */
    int update(WxMsgRecord wxMsgRecord);
}
