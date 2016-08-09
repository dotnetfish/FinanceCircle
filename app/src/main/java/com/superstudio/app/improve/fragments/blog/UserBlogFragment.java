package com.superstudio.app.improve.fragments.blog;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;
import com.superstudio.app.AppContext;
import com.superstudio.app.R;
import com.superstudio.app.improve.adapter.base.BaseListAdapter;
import com.superstudio.app.improve.adapter.general.BlogAdapter;
import com.superstudio.app.api.remote.OSChinaApi;
import com.superstudio.app.improve.adapter.general.NewsAdapter;
import com.superstudio.app.improve.bean.News;
import com.superstudio.app.improve.detail.activities.BlogDetailActivity;
import com.superstudio.app.improve.bean.Blog;
import com.superstudio.app.improve.bean.base.PageBean;
import com.superstudio.app.improve.bean.base.ResultBean;
import com.superstudio.app.improve.fragments.base.BaseListFragment;
import com.superstudio.app.improve.fragments.news.FinanceNewsResponse;
import com.superstudio.app.improve.fragments.news.ResponseConverter;

import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;

/**
 * 博客界面
 */
public class UserBlogFragment extends BaseListFragment<Blog> {

    public static final String HISTORY_BLOG = "history_my_blog";
    public static final String USER_ID = "user_id";
    private int userId;

    @Override
    protected void initBundle(Bundle bundle) {
        super.initBundle(bundle);
        userId = bundle.getInt(USER_ID, 0);
        // mIsRefresh = false;
    }


    @Override
    public TextHttpResponseHandler getHandler(){
        return  new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                Log.e("error",responseString);
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
    protected void requestData() {
        super.requestData();

        OSChinaApi.getUserBlogList(OSChinaApi.CATALOG_BLOG_NORMAL,
                (mIsRefresh ? (mBean != null ? mBean.getPrevPageToken() : null) : (mBean != null ? mBean.getNextPageToken() : null))
                , userId, mHandler);

    }

    @Override
    protected BaseListAdapter<Blog> getListAdapter() {
       BlogAdapter blogAdapter = new BlogAdapter(this);
       blogAdapter.setUserBlog(true);

          return blogAdapter;
    }

    @Override
    protected Type getType() {
//        return new TypeToken<ResultBean<PageBean<Blog>>>() {
//        }.getType();
        return new TypeToken<FinanceNewsResponse>() {
        }.getType();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        Blog blog = mAdapter.getItem(position);
        if (blog != null) {
            BlogDetailActivity.show(getActivity(), blog.getId());
            TextView title = (TextView) view.findViewById(R.id.tv_item_blog_title);
            TextView content = (TextView) view.findViewById(R.id.tv_item_blog_body);

            updateTextColor(title, content);
            saveToReadedList(UserBlogFragment.HISTORY_BLOG, blog.getId() + "");
        }
    }

}
