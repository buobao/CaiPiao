package com.king.turman.caipiao.net.utils;

import com.king.turman.caipiao.net.bean.LotteryBean;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by diaoqf on 2017/12/7.
 */

public class HtmlUtil {

    public static List<LotteryBean> getLotteryListFromString(String str) {
        Document doc = Jsoup.parse(str);
        Elements lines = doc.getElementsByTag("tr");
        List<LotteryBean> beans = new ArrayList<>();
        LotteryBean bean = null;
        for (int i = 2; i < lines.size() - 1; i++) {
            bean = new LotteryBean();
            Elements tds = lines.get(i).getElementsByTag("td");
            for (int i1 = 0; i1 < tds.size(); i1++) {
                switch (i1) {
                    case 0:
                        bean.setDate(tds.get(i1).text());
                        break;
                    case 1:
                        bean.setNo(tds.get(i1).text());
                        break;
                    case 2:
                        Elements ems = tds.get(i1).getElementsByTag("em");
                        List<String> nums = new ArrayList<>();
                        for (int i2 = 0; i2 < ems.size(); i2++) {
                            nums.add(ems.get(i2).text());
                        }
                        bean.setNumbers(nums);
                        break;
                    case 3:
                        Elements strongs = tds.get(i1).getElementsByTag("strong");
                        bean.setAllMoney(strongs.get(0).text());
                        break;
                    case 4:
                        Elements strongs1 = tds.get(i1).getElementsByTag("strong");
                        bean.setFisrtNum(strongs1.get(0).text());
                        break;
                    case 5:
                        Elements strongs2 = tds.get(i1).getElementsByTag("strong");
                        bean.setFisrtNum(strongs2.get(0).text());
                        break;
                    default:continue;
                }
            }
            beans.add(bean);
        }

        return beans;

    }

}
