package com.nextzy.nextzymvp.network;

import com.nextzy.library.nextwork.engine.NextworkService;
import com.nextzy.nextzymvp.network.MockApi;

import okhttp3.Request;

/**
 * Created by TheKhaeng on 9/15/2016 AD.
 */

public class MockService extends NextworkService<MockApi> {

    public static MockService newInstance(String baseUrl) {
        MockService service = new MockService();
        service.setBaseUrl(baseUrl);
        return service;
    }

    private MockService() {
    }

    @Override
    protected Class<MockApi> getApiClassType() {
        return MockApi.class;
    }

    @Override
    protected Request.Builder getRequestInterceptor(Request.Builder requestBuilder) {
        // Custom request builder
        return requestBuilder;
    }

    @Override
    protected boolean isLogger() {
        return true;
    }
}
