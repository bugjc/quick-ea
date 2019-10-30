package com.ugiant.job.model.page;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ugiant.job.core.enums.business.JobStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 任务列表 page 对象
 * @author aoki
 * @date 2019/10/16
 * **/
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class JobPage<T> extends Page<T> {
    private static final long serialVersionUID = 5194933845448697148L;

    /**
     * 任务状态
     */
    private Integer status;

    public JobPage(long current, long size) {
        super(current, size);
    }
}
