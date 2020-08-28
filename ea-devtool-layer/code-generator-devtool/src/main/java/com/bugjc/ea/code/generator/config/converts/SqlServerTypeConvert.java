/*
 * Copyright (c) 2011-2020, baomidou (jobob@qq.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * https://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.bugjc.ea.code.generator.config.converts;

import com.bugjc.ea.code.generator.config.GlobalConfig;
import com.bugjc.ea.code.generator.config.ITypeConvert;
import com.bugjc.ea.code.generator.config.rules.IColumnType;
import com.bugjc.ea.code.generator.config.rules.DbColumnType;

/**
 * SQLServer 字段类型转换
 *
 * @author hubin, hanchunlin
 * @since 2017-01-20
 */
public class SqlServerTypeConvert implements ITypeConvert {
    public static final SqlServerTypeConvert INSTANCE = new SqlServerTypeConvert();

    /**
     * @inheritDoc
     */
    @Override
    public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
        return TypeConverts.use(fieldType)
            .test(TypeConverts.containsAny("char", "xml", "text").then(DbColumnType.STRING))
            .test(TypeConverts.contains("bigint").then(DbColumnType.LONG))
            .test(TypeConverts.contains("int").then(DbColumnType.INTEGER))
            .test(TypeConverts.containsAny("date", "time").then(DbColumnType.STRING))
            .test(TypeConverts.contains("bit").then(DbColumnType.BOOLEAN))
            .test(TypeConverts.containsAny("decimal", "numeric").then(DbColumnType.DOUBLE))
            .test(TypeConverts.contains("money").then(DbColumnType.BIG_DECIMAL))
            .test(TypeConverts.containsAny("binary", "image").then(DbColumnType.BYTE_ARRAY))
            .test(TypeConverts.containsAny("float", "real").then(DbColumnType.FLOAT))
            .or(DbColumnType.STRING);
    }

}
