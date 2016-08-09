package com.superstudio.app.improve.fragments.news;

import java.util.ArrayList;

/**
 * Created by T440P on 2016-8-4.
 */
public class FinanceNewsResponse {
    private ArrayList<FinanceNew> list;

    private  ArrayList<FinanceCategory> cate;

    public ArrayList<FinanceNew> getList() {
        return list;
    }

    private ArrayList<FinanceSlider> top;

    public void setList(ArrayList<FinanceNew> list) {
        this.list = list;
    }

    public ArrayList<FinanceSlider> getTop() {
        return top;
    }

    public void setTop(ArrayList<FinanceSlider> top) {
        this.top = top;
    }

    public ArrayList<FinanceCategory> getCate() {
        return cate;
    }

    public void setCate(ArrayList<FinanceCategory> cate) {
        this.cate = cate;
    }
   /* private int state;

    private  String msg;

    private ArrayList<FinanceNew> data;

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<FinanceNew> getData() {
        return data;
    }

    public void setData(ArrayList<FinanceNew> data) {
        this.data = data;
    }*/
}
