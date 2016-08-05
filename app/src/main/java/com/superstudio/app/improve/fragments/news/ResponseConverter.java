package com.superstudio.app.improve.fragments.news;

import com.superstudio.app.improve.bean.News;
import com.superstudio.app.improve.bean.base.PageBean;
import com.superstudio.app.improve.bean.base.ResultBean;

import java.util.ArrayList;

/**
 * Created by T440P on 2016-8-5.
 */
public class ResponseConverter {
    public  static ResultBean<PageBean<News>> toResultBean(FinanceNewsResponse response){
        ResultBean<PageBean<News>> resultBean=new ResultBean<PageBean<News>>();
        resultBean.setCode(response.getState());
        resultBean.setMessage(response.getMsg());
        PageBean<News> list=new PageBean<>();
        ArrayList<News> newsList=new ArrayList<>();
        for (FinanceNew news:response.getData()
                ) {
            News targetNews=new News();
            targetNews.setAuthor(news.getFrom());
            targetNews.setBody(news.getTitle());
            targetNews.setId(Long.valueOf(news.getTid()));
            targetNews.setPubDate(news.getTime());
            targetNews.setTitle(news.getTitle());
            targetNews.setViewCount(10);
            targetNews.setHref("");
            //targetNews.set

            newsList.add(targetNews);
        }
        list.setItems(newsList);
        resultBean.setResult(list);
        // resultBean.setCode(response.getState());

        return resultBean;
    }
}
