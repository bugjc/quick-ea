package com.glcxw.wechat.service.impl;

import com.glcxw.wechat.dao.WxMsgRecordMapper;
import com.glcxw.wechat.model.WxMsgRecord;
import com.glcxw.wechat.service.WxMsgRecordService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * @author aoki
 */
@Service
public class WxMsgRecordServiceImpl implements WxMsgRecordService {

    @Resource
    private WxMsgRecordMapper wxMsgRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int insert(WxMsgRecord wxMsgRecord) {
        return wxMsgRecordMapper.insert(wxMsgRecord);
    }

    @Override
    public WxMsgRecord findOrderByQrNo(String msgId) {
        return wxMsgRecordMapper.selOrderByQrNo(msgId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public int update(WxMsgRecord wxMsgRecord) {
        return wxMsgRecordMapper.update(wxMsgRecord);
    }
}
