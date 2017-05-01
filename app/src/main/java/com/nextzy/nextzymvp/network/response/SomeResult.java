package com.nextzy.nextzymvp.network.response;

import com.nextzy.nextzymvp.network.response.BaseResult;

/**
 * Created by Akexorcist on 3/26/2017 AD.
 */

public class SomeResult extends BaseResult {
    private String name;

    public SomeResult() {
    }

    public SomeResult(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isDataAvailable() {
        return name != null;
    }
}
