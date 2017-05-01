package com.nextzy.nextzymvp;

import android.util.Log;

import com.nextzy.nextzymvp.network.response.SomeResult;

import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

;

/**
 * Created by Akexorcist on 3/27/2017 AD.
 */

public class MockNetworkCacheManager {

    // TODO ทำให้มันไม่ต้องสร้างเองเยอะนัก ถ้าออโต้ได้ยิ่งดี โยน Key สำหรับดึง Cache มาได้ยิ่งดี แล้วโยน Observable ของ Service ที่ต้องการมาให้แทน
    private static MockNetworkCacheManager manager;

    public static MockNetworkCacheManager getInstance(ActivityContractor activityContractor) {
        if (manager == null || !manager.isActivityContractorAvailable()) {
            manager = new MockNetworkCacheManager(activityContractor);
        }
        return manager;
    }

    public static MockNetworkCacheManager getInstance(DatabaseContractor databaseContractor) {
        if (manager == null || !manager.isActivityContractorAvailable()) {
            manager = new MockNetworkCacheManager(databaseContractor);
        }
        return manager;
    }

    public static MockNetworkCacheManager getInstance(ActivityContractor activityContractor, DatabaseContractor databaseContractor) {
        if (manager == null || (!manager.isActivityContractorAvailable() && !manager.isDatabaseContractorAvailable())) {
            manager = new MockNetworkCacheManager(activityContractor, databaseContractor);
        }
        return manager;
    }

    private ActivityContractor activityContractor;
    private DatabaseContractor databaseContractor;

    private MockNetworkCacheManager(ActivityContractor activityContractor) {
        this.activityContractor = activityContractor;
    }

    private MockNetworkCacheManager(DatabaseContractor databaseContractor) {
        this.databaseContractor = databaseContractor;
    }

    private MockNetworkCacheManager(ActivityContractor activityContractor, DatabaseContractor databaseContractor) {
        this.activityContractor = activityContractor;
        this.databaseContractor = databaseContractor;
    }

    private boolean isActivityContractorAvailable() {
        return activityContractor != null;
    }

    private boolean isDatabaseContractorAvailable() {
        return databaseContractor != null;
    }

    public Single<SomeResult> getSomeResultService() {
        return getSomeResult(true);
    }

    public Single<SomeResult> getSomeResultFromCache() {
        return getSomeResult(false);
    }

    private Single<SomeResult> getSomeResult(boolean isCacheFirst) {
        Observable<SomeResult> memory = Observable.just(new SomeResult());
        Observable<SomeResult> disk = Observable.just(getDataFromDisk(isCacheFirst));
        Observable<SomeResult> network = Observable.just(new SomeResult("Network"))
                .map(mapToDatabase);
        return Observable
                .concat(memory, disk, network)
                .filter(new Predicate<SomeResult>() {
                    @Override
                    public boolean test(@NonNull SomeResult someResult) throws Exception {
                        return someResult.isDataAvailable();
                    }
                })
                .first(new SomeResult());
    }


    private SomeResult getDataFromDisk(boolean isCacheFirst) {
        if (isCacheFirst && isCached()) {
            Log.e("Check", "Read from disk");
            SomeResult result = new SomeResult("disk");
            Log.e("Check", "Remove data from disk");
            isCached = false;
            return result;
        }
        if (isCacheFirst && !isCached()) {
            Log.e("Check", "No data in disk");
        }
        return new SomeResult();
    }

    private boolean isCached = true;

    private boolean isCached() {
        return isCached;
    }

    private Function<SomeResult, SomeResult> mapToDatabase = new Function<SomeResult, SomeResult>() {
        @Override
        public SomeResult apply(@NonNull SomeResult someResult) throws Exception {
            if (activityContractor != null && activityContractor.isInactive()) {
                Log.e("Check", "Save to disk");
            }
            if (databaseContractor != null) {
                databaseContractor.save(someResult);
            }
            return someResult;
        }
    };
}
