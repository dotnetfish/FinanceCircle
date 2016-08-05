package com.superstudio.app.improve.fragments.news;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import com.superstudio.app.AppContext;
import com.superstudio.app.R;
import com.superstudio.app.api.remote.OSChinaApi;
import com.superstudio.app.bean.Banner;
import com.superstudio.app.cache.CacheManager;
import com.superstudio.app.improve.adapter.base.BaseListAdapter;
import com.superstudio.app.improve.adapter.general.NewsAdapter;
import com.superstudio.app.improve.app.AppOperator;
import com.superstudio.app.improve.bean.News;
import com.superstudio.app.improve.bean.base.PageBean;
import com.superstudio.app.improve.bean.base.ResultBean;
import com.superstudio.app.improve.fragments.base.BaseGeneralListFragment;
import com.superstudio.app.ui.empty.EmptyLayout;
import com.superstudio.app.util.UIHelper;
import com.superstudio.app.widget.ViewNewsHeader;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


/**
 * 资讯界面
 */
public class NewsFragment extends BaseGeneralListFragment<News> {

    public static final String HISTORY_NEWS = "history_news";

    //private static final String TAG = "NewsFragment";

    public static final String NEWS_SYSTEM_TIME = "news_system_time";

    private boolean isFirst = true;

    private static final String NEWS_BANNER = "news_banner";

    private ViewNewsHeader mHeaderView;
    private Handler handler = new Handler();

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mHeaderView = new ViewNewsHeader(getActivity());
        AppOperator.runOnThread(new Runnable() {
            @SuppressWarnings("unchecked")
            @Override
            public void run() {
                final PageBean<Banner> pageBean = (PageBean<Banner>) CacheManager.readObject(getActivity(), NEWS_BANNER);
                if (pageBean != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ((NewsAdapter) mAdapter).setSystemTime(AppContext.get(NEWS_SYSTEM_TIME, null));
                            mHeaderView.initData(getImgLoader(), pageBean.getItems());
                        }
                    });
                }
            }
        });

        mHeaderView.setRefreshLayout(mRefreshLayout);
        mListView.addHeaderView(mHeaderView);
        getBannerList();
    }

    @Override
    public void onRefreshing() {
        super.onRefreshing();
        if (!isFirst)
            getBannerList();
    }

    @Override
    public  void   initData(){
       // super.initData();
        mAdapter = getListAdapter();
        mListView.setAdapter(mAdapter);

        mHandler = new TextHttpResponseHandler() {
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
                    ResultBean<PageBean<News>> resultBean=ResponseConverter.toResultBean(myBean);
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

        AppOperator.runOnThread(new Runnable() {
            @Override
            public void run() {
                mBean = (PageBean<News>) CacheManager.readObject(getActivity(), CACHE_NAME);
                //if is the first loading
                if (mBean == null) {
                    mBean = new PageBean<>();
                    mBean.setItems(new ArrayList<News>());
                    onRefreshing();
                } else {
                    mRoot.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.addItem(mBean.getItems());
                            mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
                            mRefreshLayout.setVisibility(View.VISIBLE);
                            onRefreshing();
                        }
                    });
                }
            }
        });


    }

    @Override
    protected void requestData() {
        super.requestData();

        OSChinaApi.getNewsList(mIsRefresh ? mBean.getPrevPageToken() : mBean.getNextPageToken(), mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        News news = mAdapter.getItem(position - 1);
        if (news != null) {
            int type = news.getType();
            long newsId = news.getId();
            UIHelper.showDetail(getActivity(), type, newsId, news.getHref());
            TextView title = (TextView) view.findViewById(R.id.tv_title);
            TextView content = (TextView) view.findViewById(R.id.tv_description);
            updateTextColor(title, content);
            saveToReadedList(HISTORY_NEWS, news.getId() + "");
        }
    }

    @Override
    protected BaseListAdapter<News> getListAdapter() {
        return new NewsAdapter(this);
    }

    @Override
    protected Type getType() {
      return new TypeToken<FinanceNewsResponse>() {
       }.getType();

       // return FinanceNewsResponse.class;
    }

    @Override
    protected void onRequestFinish() {
        super.onRequestFinish();
        isFirst = false;
    }


    @Override
    protected void setListData(ResultBean<PageBean<News>> resultBean) {
        ((NewsAdapter) mAdapter).setSystemTime(resultBean.getTime());
        AppContext.set(NEWS_SYSTEM_TIME, resultBean.getTime());
        super.setListData(resultBean);
    }

    private  void loadBannerList(){
       /* AppOperator.runOnThread(new Runnable() {
            @Override
            public void run() {
                CacheManager.saveObject(getActivity(), resultBean.getResult(), NEWS_BANNER);
            }
        });
        mHeaderView.initData(getImgLoader(), resultBean.getResult().getItems());*/
    }
    private void getBannerList() {
        OSChinaApi.getBannerList(OSChinaApi.CATALOG_BANNER_NEWS, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    final ResultBean<PageBean<Banner>> resultBean = AppContext.createGson().fromJson(responseString, new TypeToken<ResultBean<PageBean<Banner>>>() {
                    }.getType());
                    if (resultBean != null && resultBean.isSuccess()) {
                        AppOperator.runOnThread(new Runnable() {
                            @Override
                            public void run() {
                                CacheManager.saveObject(getActivity(), resultBean.getResult(), NEWS_BANNER);
                            }
                        });
                        mHeaderView.initData(getImgLoader(), resultBean.getResult().getItems());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
