package com.akexorcist.nextzymvp.network;


import com.akexorcist.nextzymvp.network.response.SomeResult;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by TheKhaeng on 9/14/2016 AD.
 */

public interface MockApi {

    @GET(MockUrl.GET_SOME_RESULT)
    Single<SomeResult> getPostpaidInformation(@Path("mobileNumber") String mobileNumber);

}
