package com.king.turman.caipiao.ui;

import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.king.turman.caipiao.R;
import com.king.turman.caipiao.net.NetRequest;
import com.king.turman.caipiao.net.bean.LotteryBean;
import com.king.turman.caipiao.net.utils.HtmlUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlueBallActivity extends BaseActivity {

    private List<LotteryBean> datas;
    private ArrayList<Integer> nums;
    private WebView mWebView;
    private int currentShowCount = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        datas = new ArrayList<>();

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

        }, "caipiao");

        getData(currentShowCount);
    }

    private void getData(int currentShowCount) {
        NetRequest.getLottery("list_1.html")
                .compose(this.bindToLifecycle())
                .subscribe(s -> {
                    datas.addAll(HtmlUtil.getLotteryListFromString(s));
                    Map<String,Integer> numsMap = new HashMap<>();
                    for (int i = 0; i < datas.size(); i++) {
                        int blueBallIndex = datas.get(i).getNumbers().size()-1;
                        String numKey = datas.get(i).getNumbers().get(blueBallIndex);
                        if (numsMap.containsKey(numKey)) {
                            numsMap.put(numKey, numsMap.get(numKey) + 1);
                        } else {
                            numsMap.put(datas.get(i).getNumbers().get(blueBallIndex),1);
                        }
                    }

                    nums = new ArrayList<>();
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
                },throwable -> {
                    throwable.printStackTrace();
                });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_blue_ball;
    }
}
