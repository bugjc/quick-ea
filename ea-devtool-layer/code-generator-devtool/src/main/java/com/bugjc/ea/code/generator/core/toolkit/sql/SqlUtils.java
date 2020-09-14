
package com.bugjc.ea.code.generator.core.toolkit.sql;

import com.bugjc.ea.code.generator.core.enums.SqlLike;
import com.bugjc.ea.code.generator.core.toolkit.StringPool;

/**
 * SqlUtils工具类
 *
 * @author Caratacus
 * @since 2016-11-13
 */
public class SqlUtils {

    /**
     * 用%连接like
     *
     * @param str 原字符串
     * @return like 的值
     */
    public static String concatLike(Object str, SqlLike type) {
        switch (type) {
            case LEFT:
                return StringPool.PERCENT + str;
            case RIGHT:
                return str + StringPool.PERCENT;
            default:
                return StringPool.PERCENT + str + StringPool.PERCENT;
        }
    }
}
