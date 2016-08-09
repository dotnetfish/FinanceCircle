package com.superstudio.app.improve.fragments.news;

import com.superstudio.app.bean.Banner;
import com.superstudio.app.improve.bean.Blog;
import com.superstudio.app.improve.bean.News;
import com.superstudio.app.improve.bean.base.PageBean;
import com.superstudio.app.improve.bean.base.ResultBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by T440P on 2016-8-5.
 */
public class ResponseConverter {
    public  static ResultBean<PageBean<News>> toResultBean(FinanceNewsResponse response){
        ResultBean<PageBean<News>> resultBean=new ResultBean<PageBean<News>>();
        resultBean.setCode(1);
        resultBean.setMessage("");
        PageBean<News> list=new PageBean<>();
        ArrayList<News> newsList=new ArrayList<>();
        News targetNews=null;
        for (FinanceNew news:response.getList()
                ) {
           targetNews=new News();
            targetNews.setAuthor(news.getCatname());
            targetNews.setBody(news.getSummary().replace("丰登街","赢领集市"));
            targetNews.setId(Long.valueOf(news.getAid()));
            Calendar c1 = Calendar.getInstance();
            c1.setTime(new Date());
            //使用pattern
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:m:s");
            targetNews.setPubDate(format.format(c1.getTime()));
            targetNews.setTitle(news.getTitle().replace("丰登街","赢领集市"));
            targetNews.setViewCount(10);
            targetNews.setHref("");
            //targetNews.set

            newsList.add(targetNews);
        }
        if(null!=targetNews){
            list.setNextPageToken(String.valueOf(targetNews.getId()));
            list.setPrevPageToken("0");
        }
        list.setItems(newsList);
        resultBean.setResult(list);
        // resultBean.setCode(response.getState());

        return resultBean;
    }

    public static ResultBean<PageBean<Blog>> toBlogResultBean(FinanceNewsResponse response) {
        ResultBean<PageBean<Blog>> resultBean=new ResultBean<PageBean<Blog>>();
        resultBean.setCode(1);
        resultBean.setMessage("");
        PageBean<Blog> list=new PageBean<>();
        ArrayList<Blog> newsList=new ArrayList<>();
        Blog targetNews=null;
        for (FinanceNew news:response.getList()
                ) {
            targetNews=new Blog();
            targetNews.setAuthor(news.getCatname()+"");
            targetNews.setBody(news.getSummary().replace("丰登街","赢领集市"));
            targetNews.setId(Long.valueOf(news.getAid()));
            Calendar c1 = Calendar.getInstance();
            c1.setTime(new Date());
            //使用pattern
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:m:s");
             targetNews.setPubDate(format.format(c1.getTime()));
            targetNews.setTitle(news.getTitle().replace("丰登街","赢领集市"));
            targetNews.setViewCount(10);
            targetNews.setHref("");
            //targetNews.set

            newsList.add(targetNews);
        }
        if(null!=targetNews){
            list.setNextPageToken(String.valueOf(targetNews.getId()));
            list.setPrevPageToken("0");
        }
        list.setItems(newsList);
        resultBean.setResult(list);
        // resultBean.setCode(response.getState());

        return resultBean;
    }

    public static ResultBean<PageBean<Banner>> toBannerBean(FinanceNewsResponse response) {
        ResultBean<PageBean<Banner>> resultBean=new ResultBean<PageBean<Banner>>();
        resultBean.setCode(1);
        resultBean.setMessage("");
        PageBean<Banner> list=new PageBean<>();
        ArrayList<Banner> newsList=new ArrayList<>();
        Banner targetNews=null;
        for (FinanceSlider news:response.getTop()
                ) {
            targetNews=new Banner();

            targetNews.setDetail(news.getSummary().replace("丰登街","赢领集市"));
            targetNews.setId(Long.valueOf(news.getId()));
            Calendar c1 = Calendar.getInstance();
            c1.setTime(new Date());
            //使用pattern
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:m:s");
            targetNews.setPubDate(format.format(c1.getTime()));
            targetNews.setName(news.getTitle().replace("丰登街","赢领集市"));
            targetNews.setImg(news.getImage_url());
           //targetNews.setHref("");
            //targetNews.set

            newsList.add(targetNews);
        }
        if(null!=targetNews){
            list.setNextPageToken(String.valueOf(targetNews.getId()));
            list.setPrevPageToken("0");
        }
        list.setItems(newsList);
        resultBean.setResult(list);
        // resultBean.setCode(response.getState());

        return resultBean;
    }
}
