package com.bugjc.ea.qrcode.dao.provider;

import org.apache.ibatis.jdbc.SQL;

/**
 * @author aoki
 * @date ${date}
 */
public class OrderProvider {

    public String insert(){
        return new SQL(){
            {
                INSERT_INTO("tbs_order");
                VALUES("user_id", "#{userId}");
                VALUES("req_type", "#{reqType}");
                VALUES("orig_req_type", "#{origReqType}");
                VALUES("txn_no", "#{txnNo}");
                VALUES("qr_no", "#{qrNo}");
                VALUES("currency_code", "#{currencyCode}");
                VALUES("txn_amt", "#{txnAmt}");
                VALUES("mer_id", "#{merId}");
                VALUES("mer_cat_code", "#{merCatCode}");
                VALUES("mer_name", "#{merName}");
                VALUES("term_id", "#{termId}");
                VALUES("voucher_num", "#{voucherNum}");
                VALUES("settle_key", "#{settleKey}");
                VALUES("settle_data", "#{settleData}");
                VALUES("com_info", "#{comInfo}");
                VALUES("req_reserved", "#{reqReserved}");
                VALUES("coupon_info", "#{couponInfo}");
                VALUES("orig_txn_amt", "#{origTxnAmt}");
                VALUES("cdhd_curr_cd", "#{cdhdCurrCd}");
                VALUES("cdhd_conv_rt", "#{cdhdConvRt}");
                VALUES("cdhd_amt", "#{cdhdAmt}");
                VALUES("settle_curr_code", "#{settleCurrCode}");
                VALUES("settle_conv_rate", "#{settleConvRate}");
                VALUES("settle_amt", "#{settleAmt}");
                VALUES("conv_date", "#{convDate}");
                VALUES("emv_qr_code", "#{emvQrCode}");
                VALUES("order_no", "#{orderNo}");
                VALUES("auth_id_resp_cd", "#{authIdRespCd}");
                VALUES("acp_addn_data", "#{acpAddnData}");
                VALUES("create_time", "#{createTime}");
            }
        }.toString();
    }

    public String selOrderByQrNo(){
        return new SQL(){{
            SELECT_DISTINCT("*");
            FROM("tbs_order");
            WHERE("qr_no=#{qrNo}");
        }}.toString();
    }

}
