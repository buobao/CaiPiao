package com.king.turman.caipiao.net;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by diaoqf on 2017/12/7.
 */

public interface NetApi {
    @GET("zhcw/html/ssq/{path}")
    Observable<String> getLottery(@Path("path") String path);
}
