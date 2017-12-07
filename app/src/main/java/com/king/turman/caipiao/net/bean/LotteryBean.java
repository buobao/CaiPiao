package com.king.turman.caipiao.net.bean;

import java.util.List;

/**
 * Created by diaoqf on 2017/12/7.
 */

public class LotteryBean {

    private String date;
    private String no;
    private List<String> numbers;
    private String allMoney;
    private String fisrtNum;
    private String secondNum;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public List<String> getNumbers() {
        return numbers;
    }

    public void setNumbers(List<String> numbers) {
        this.numbers = numbers;
    }

    public String getAllMoney() {
        return allMoney;
    }

    public void setAllMoney(String allMoney) {
        this.allMoney = allMoney;
    }

    public String getFisrtNum() {
        return fisrtNum;
    }

    public void setFisrtNum(String fisrtNum) {
        this.fisrtNum = fisrtNum;
    }

    public String getSecondNum() {
        return secondNum;
    }

    public void setSecondNum(String secondNum) {
        this.secondNum = secondNum;
    }
}
