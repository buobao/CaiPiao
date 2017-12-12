package com.king.turman.caipiao.ui;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Interpolator;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.king.turman.caipiao.R;
import com.king.turman.caipiao.net.NetRequest;
import com.king.turman.caipiao.net.bean.LotteryBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class BlueBallActivity extends BaseActivity {

    private List<LotteryBean> datas;
    private ArrayList<Integer> nums;
    private Map<String, ArrayList<Double>> singleNumCounts;
    private WebView mWebView;
    private Spinner spinner;
    private int currentShowCount = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        datas = new ArrayList<>();
        nums = new ArrayList<>();
        singleNumCounts = new HashMap<>();
        for (int i=1;i<=16;i++) {
            singleNumCounts.put(i+"",new ArrayList<>());
        }

        spinner = (Spinner) findViewById(R.id.select_num);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int num = (position + 1) *100;
                if (num != currentShowCount) {
                    currentShowCount = num;
                    getData(currentShowCount);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mWebView = (WebView) findViewById(R.id.web_view);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new Object(){

            @JavascriptInterface
            public String numberCounts(){
                String str = "";
                for (Integer num : nums) {
                    str = str + num + ";";
                }
                str = str.substring(0,str.length()-1);
                return str;
            }

            @JavascriptInterface
            public String numberSize(){
                return currentShowCount + "";
            }

            @JavascriptInterface
            public String getSingleArr(String num) {
                ArrayList<Double> numArr = singleNumCounts.get(num+"");
                String resultStr = "";
                for (Double aDouble : numArr) {
                    resultStr = resultStr + aDouble +";";
                }
                return resultStr;
            }

        }, "caipiao");

        getData(currentShowCount);
    }

    private void getData(int currentShowCount) {
        datas.clear();
        nums.clear();
        for (String s : singleNumCounts.keySet()) {
            singleNumCounts.get(s).clear();
        }
        int pageCount = currentShowCount/20;

        ArrayList<Observable<List<LotteryBean>>> observables = new ArrayList<>();
        for (int i = 0; i < pageCount; i++) {
            observables.add(NetRequest.getLottery("list_"+(i+1)+".html")
                    .compose(this.bindToLifecycle()));
        }

        Observable.concat(observables)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> {
                    datas.addAll(s);

                    if (datas.size() >= currentShowCount) {
                        Map<String,Integer> numsMap = new HashMap<>();
                        Map<String,Integer> tempMap = new HashMap<>();
                        for (int i = 0; i < datas.size(); i++) {
                            int blueBallIndex = datas.get(i).getNumbers().size()-1;
                            String numKey = datas.get(i).getNumbers().get(blueBallIndex);
                            if (numsMap.containsKey(numKey)) {
                                numsMap.put(numKey, numsMap.get(numKey) + 1);
                            } else {
                                numsMap.put(datas.get(i).getNumbers().get(blueBallIndex),1);
                            }

                            if (tempMap.containsKey(numKey)) {
                                tempMap.put(numKey, tempMap.get(numKey) + 1);
                            } else {
                                tempMap.put(datas.get(i).getNumbers().get(blueBallIndex),1);
                            }

                            if ((i+1) % 20 == 0 && i > 0) {
                                for (int ii=1;ii<=16;ii++) {
                                    boolean flag = false;
                                    for (String s1 : tempMap.keySet()) {
                                        if (Integer.parseInt(s1) == ii) {
                                            singleNumCounts.get(ii+"").add(tempMap.get(s1)/20.0);
                                            flag = true;
                                            break;
                                        }
                                    }
                                    if (!flag) {
                                        singleNumCounts.get(ii+"").add(0.0);
                                    }
                                }
                                tempMap.clear();
                            }
                        }

                        for (int i=0;i<16;i++) {
                            int count = 0;
                            for (String ss : numsMap.keySet()) {
                                if (((i+1) == Integer.parseInt(ss))) {
                                    count = numsMap.get(ss);
                                    break;
                                }
                            }
                            nums.add(count);
                        }

                        mWebView.loadUrl("file:///android_asset/html/blue_ball_bar_chart.html");
                    }
                },throwable -> throwable.printStackTrace());
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_blue_ball;
    }
}
