package com.king.turman.caipiao.net;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by diaoqf on 2017/12/7.
 */

public class NetRequest {
    private static NetApi commonApi;

    //带缓存服务
    public static NetApi getCommonService() {
        if (commonApi == null) {
            commonApi = NetManager.getClient().create(NetApi.class);
        }
        return commonApi;
    }

    public static Observable<String> getLottery(String path) {
        return getCommonService().getLottery(path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }
}
