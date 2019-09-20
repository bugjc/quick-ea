package springboot.maven.template.core.enums.business;

/**
 * 订单状态
 * @author yangqing
 */
public enum OrderStatus {
    /**
     * 订单状态
     */
    ScanQrCode(0,"扫码充电"),
    Starting(1,"启动中"),
    Charging(2,"充电中"),
    Stopping(3,"停止中"),
    Complete(4,"已结束"),
    Unknown(5,"未知状态"),
    DisputedTransaction(6,"已结束的争议交易");

    private final int status;
    private final String desc;

    OrderStatus(int status, String desc) {
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
