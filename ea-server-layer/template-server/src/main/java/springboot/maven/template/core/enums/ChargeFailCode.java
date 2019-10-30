package springboot.maven.template.core.enums;

/**
 * 充电业务失败码
 * @author aoki
 */
public enum ChargeFailCode {
    /**
     * 状态
     */
    Success(0,"操作成功"),
    InvalidOrder(1,"无效的订单"),
    TeLdBusyServer(97,"特来电充电服务繁忙，请稍后重试"),
    ERROR(99,"操作失败");

    private final int status;
    private final String desc;

    ChargeFailCode(int status, String desc) {
        this.status = status;
        this.desc = desc;
    }

    public int getStatus() {
        return status;
    }

    public String getDesc() {
        return desc;
    }
}
