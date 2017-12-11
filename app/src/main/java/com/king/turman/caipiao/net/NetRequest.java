package com.king.turman.caipiao.net;

import com.king.turman.caipiao.net.bean.LotteryBean;
import com.king.turman.caipiao.net.utils.HtmlUtil;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
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

    public static Observable<List<LotteryBean>> getLottery(String path) {
        return getCommonService().getLottery(path)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<String, Observable<List<LotteryBean>>>() {
                    @Override
                    public Observable<List<LotteryBean>> apply(@NonNull String s) throws Exception {
                        return Observable.just(HtmlUtil.getLotteryListFromString(s));
                    }
                });

    }
}
