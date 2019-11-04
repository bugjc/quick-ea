package com.bugjc.ea.opensdk.http.api;

/**
 * 任务调度服务接口,接口路径信息，路径、版本
 * @author aoki
 */
public enum JobPathInfo {
    /**
     * 接口信息
     */
    JOB_CREATE_PATH_V1("/job/create","1.0","创建任务接口"),
    JOB_DEL_PATH_V1("/job/del","1.0","删除任务接口"),
    JOB_UPD_PATH_V1("/job/upd","1.0","修改任务接口"),
    JOB_FIND_PATH_V1("/job/find","1.0","获取任务信息接口");

    private String path;
    private String version;
    private String desc;

    JobPathInfo(String path, String version, String desc) {
        this.path = path;
        this.version = version;
        this.desc = desc;
    }

    public String getPath() {
        return path;
    }

    public String getVersion() {
        return version;
    }

    public String getDesc() {
        return desc;
    }
}

