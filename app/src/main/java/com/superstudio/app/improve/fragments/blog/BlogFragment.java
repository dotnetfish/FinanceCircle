package com.superstudio.app.improve.fragments.blog;


import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import com.loopj.android.http.TextHttpResponseHandler;
import com.superstudio.app.AppContext;
import com.superstudio.app.R;
import com.superstudio.app.api.remote.OSChinaApi;
import com.superstudio.app.cache.CacheManager;
import com.superstudio.app.improve.adapter.base.BaseListAdapter;
import com.superstudio.app.improve.adapter.general.BlogAdapter;
import com.superstudio.app.improve.adapter.general.NewsAdapter;
import com.superstudio.app.improve.app.AppOperator;
import com.superstudio.app.improve.bean.Blog;
import com.superstudio.app.improve.bean.News;
import com.superstudio.app.improve.bean.base.PageBean;
import com.superstudio.app.improve.bean.base.ResultBean;
import com.superstudio.app.improve.detail.activities.BlogDetailActivity;
import com.superstudio.app.improve.fragments.base.BaseGeneralListFragment;
import com.superstudio.app.improve.fragments.news.FinanceNewsResponse;
import com.superstudio.app.improve.fragments.news.ResponseConverter;
import com.superstudio.app.ui.empty.EmptyLayout;

import java.lang.reflect.Type;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * 博客界面
 */
public class BlogFragment extends BaseGeneralListFragment<Blog> {

    public static final String BUNDLE_BLOG_TYPE = "BUNDLE_BLOG_TYPE";

    public static final String HISTORY_BLOG = "history_blog";
    private boolean isFirst = true;

    @Override
    public void onRefreshing() {
        isFirst = true;
        super.onRefreshing();
    }

    @Override
    protected void requestData() {
        super.requestData();

        if (mIsRefresh) {
            OSChinaApi.getBlogList(OSChinaApi.CATALOG_BLOG_HEAT, "0", mHandler);
        } else {
            OSChinaApi.getBlogList(OSChinaApi.CATALOG_BLOG_NORMAL, mBean == null ? null : mBean.getNextPageToken(), mHandler);
        }
    }

    @Override
    protected BaseListAdapter<Blog> getListAdapter() {
        return new BlogAdapter(this);
    }

    @Override
    protected Type getType() {
      /*  return new TypeToken<ResultBean<PageBean<News>>>() {
        }.getType();*/
        return new TypeToken<FinanceNewsResponse>() {
        }.getType();

    }
    @Override
    public TextHttpResponseHandler getHandler(){
        return  new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("error",""+responseString);
                onRequestError(statusCode);
                onRequestFinish();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Log.e("response",responseString);
                    FinanceNewsResponse myBean = AppContext.createGson().fromJson(responseString, getType());
                    //Log.i("bean",myBean.getMsg());
                    ResultBean<PageBean<Blog>> resultBean= ResponseConverter.toBlogResultBean(myBean);
                    if (resultBean != null && resultBean.isSuccess() && resultBean.getResult() != null) {
                        onRequestSuccess(resultBean.getCode());
                        setListData(resultBean);
                    } else {
                        setFooterType(TYPE_NO_MORE);
                        //mRefreshLayout.setNoMoreData();
                    }
                    onRequestFinish();
                } catch (Exception e) {
                    e.printStackTrace();
                    onFailure(statusCode, headers, responseString, e);
                }
            }
        };

    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Blog blog = mAdapter.getItem(position);
        if (blog != null) {
            BlogDetailActivity.show(getActivity(), blog.getId());
            TextView title = (TextView) view.findViewById(R.id.tv_item_blog_title);
            TextView content = (TextView) view.findViewById(R.id.tv_item_blog_body);

            updateTextColor(title, content);
            saveToReadedList(BlogFragment.HISTORY_BLOG, blog.getId() + "");

        }
    }


    @Override
    protected void setListData(ResultBean<PageBean<Blog>> resultBean) {
        //is refresh
        mBean.setNextPageToken(resultBean.getResult().getNextPageToken());
        if (mIsRefresh) {
            List<Blog> blogs = resultBean.getResult().getItems();
            Blog blog = new Blog();
         blog.setViewType(Blog.VIEW_TYPE_TITLE_HEAT);

            blogs.add(0, blog);
            mBean.setItems(blogs);
            mAdapter.clear();
            mAdapter.addItem(mBean.getItems());
            mRefreshLayout.setCanLoadMore();
            mIsRefresh = false;
            OSChinaApi.getBlogList(OSChinaApi.CATALOG_BLOG_NORMAL, "0", mHandler);
        } else {
            List<Blog> blogs = resultBean.getResult().getItems();
            if (isFirst) {
                Blog news = new Blog();
                news.setViewType(Blog.VIEW_TYPE_TITLE_NORMAL);
                blogs.add(0, news);
                isFirst = false;
                AppOperator.runOnThread(new Runnable() {
                    @Override
                    public void run() {
                        CacheManager.saveObject(getActivity(), mBean, CACHE_NAME);
                    }
                });
            }

            mRefreshLayout.setCanLoadMore();
            mBean.setPrevPageToken(resultBean.getResult().getPrevPageToken());
            mAdapter.addItem(blogs);
        }


        if (resultBean.getResult().getItems().size() < 20) {
            setFooterType(TYPE_NO_MORE);
            mRefreshLayout.setNoMoreData();
        }
        if (mAdapter.getDatas().size() > 0) {
            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
            mRefreshLayout.setVisibility(View.VISIBLE);
        } else {
            mErrorLayout.setErrorType(EmptyLayout.NODATA);
        }

    }

}
