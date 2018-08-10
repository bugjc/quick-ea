package com.bugjc.ea.qrcode.core.task;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.bugjc.ea.qrcode.config.QrCodePropConfig;
import com.bugjc.ea.qrcode.core.component.C2bProcessControlComponent;
import com.bugjc.ea.qrcode.core.dto.UnionPayResultCode;
import com.bugjc.ea.qrcode.core.sdk.qrcode.AcpService;
import com.bugjc.ea.qrcode.core.util.NettyTimerTaskUtil;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 附加请求通知
 * @author aoki
 */
@Slf4j
@Data
@Scope("prototype")
@Component("orderPayTask")
public class ReqReservedNotifyTask implements TimerTask {

	private Integer maxRetryCount = 9;
	private Integer retryCount = 0;
	private Map<String,String> contentData;
	@Resource
	private QrCodePropConfig qrCodePropConfig;
	@Resource
	private C2bProcessControlComponent c2bProcessControlComponent;

	@Override
	public void run(Timeout timeout) throws Exception {
		//重试总次数
		if (retryCount <= maxRetryCount){
			if (this.execMethod(qrCodePropConfig)){
				return;
			}
			//等待下一次重试
			this.setRetryCount(++retryCount);
			NettyTimerTaskUtil.addTask(this,retryCount * 2, TimeUnit.SECONDS);
		}
	}

	/**
	 * @param qrCodePropConfig
	 * @return
	 */
	private boolean execMethod(QrCodePropConfig qrCodePropConfig){

		if (getContentData() == null || getContentData().isEmpty()){
			log.error("参数不能为空");
			return false;
		}

		//构建请求报文
		Map<String, String> contentData = new HashMap<String, String>();
		contentData.put("version", "1.0.0");
		contentData.put("reqType", "0240000903");
		contentData.put("issCode", "90880019");

		contentData.put("qrNo", getContentData().get("qrNo"));
		contentData.put("respCode", "00");
		contentData.put("respMsg", "成功[0000000]");

		contentData.put("upReserved", "");
		contentData.put("voucherNum", getContentData().get("voucherNum"));

		Map<String, String> reqData = AcpService.sign(contentData, CharsetUtil.UTF_8);
		String requestUrl = qrCodePropConfig.getQrcB2cIssBackTransUrl();
		try {
			AcpService.post(reqData,requestUrl,CharsetUtil.UTF_8,qrCodePropConfig.isIfValidateRemoteCert());
			log.info("附加请求通知成功");
			return true;
		} catch (Exception e) {
			log.error(e.getMessage(),e);
			log.info("附加请求通知失败");
			return false;
		}
	}


}