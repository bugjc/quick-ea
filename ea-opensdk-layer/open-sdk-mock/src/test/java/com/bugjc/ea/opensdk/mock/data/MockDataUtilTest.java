package com.bugjc.ea.opensdk.mock.data;

import com.bugjc.ea.opensdk.mock.data.entity.Param;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

class MockDataUtilTest {
    
    @Test
    void mock() {
        Param param = MockDataUtil.mock(Param.class);
        System.out.println(new Gson().toJson(param));
    }
}