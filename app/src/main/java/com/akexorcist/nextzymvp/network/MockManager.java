package com.akexorcist.nextzymvp.network;

import com.akexorcist.nextzymvp.network.response.SomeResult;
import com.nextzy.library.nextwork.engine.BackgroundThreadTransformer;

import io.reactivex.Single;


/**
 * Created by TheKhaeng on 9/14/2016 AD.
 */

public class MockManager {
    public static final String SERVICE_GET_POSTPAID_INFORMATION = "service_get_postpaid_information";
    public static final String SERVICE_GET_PREPAID_INFORMATION = "service_get_prepaid_information";
    public static final String SERVICE_GET_BILLING_CYCLE = "service_get_billing_cycle";
    public static final String SERVICE_GET_BILL_MEDIA = "service_get_bill_media";
    public static final String SERVICE_GET_POSTPAID_BILLING_ADDRESS = "service_get_postpaid_billing_address";
    public static final String SERVICE_GET_ADDRESS_REMARK = "service_get_address_remark";

    public static final String SERVICE_GET_POSTPAID_PROFILE = "service_get_postpaid_profile";
    public static final String SERVICE_GET_PREPAID_PROFILE = "service_get_prepaid_profile";
    public static final String SERVICE_GET_POSTPAID_CORPORATE_PROFILE = "service_get_postpaid_corporate_profile";
    public static final String SERVICE_GET_PREPAID_CORPORATE_PROFILE = "service_get_prepaid_corporate_profile";
    public static final String SERVICE_GET_FIBRE_PROFILE = "service_get_fibre_profile";

    private static MockManager instance;
    private MockApi api;

    public static MockManager getInstance() {
        if (instance == null) {
            instance = new MockManager();
        }
        return instance;
    }

    private MockManager() {
    }

    public MockApi getMockApi() {
        return api;
    }

    public void setMockApi(MockApi api) {
        this.api = api;
    }

    /*******************/
    /** Create Single **/
    /*******************/

    public Single<SomeResult> getSomeResult(String mobileNumber) {
        return MockService.newInstance(MockUrl.MOCK_NEXTZY)
                .getRxApi(getMockApi())
                .getPostpaidInformation(mobileNumber)
                .compose(new BackgroundThreadTransformer<>());
//                .compose(new SchedulersAndErrorHandling<>(PostpaidInfoResult.class, SERVICE_GET_POSTPAID_INFORMATION))
//                .map(new MyAccountSaveRealmResult<>(SERVICE_GET_POSTPAID_INFORMATION));
    }

}
