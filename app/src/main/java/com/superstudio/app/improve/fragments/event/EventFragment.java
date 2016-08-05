package com.superstudio.app.improve.fragments.event;

import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.TextHttpResponseHandler;

import com.superstudio.app.AppContext;
import com.superstudio.app.R;
import com.superstudio.app.api.remote.OSChinaApi;
import com.superstudio.app.bean.Banner;
import com.superstudio.app.cache.CacheManager;
import com.superstudio.app.improve.adapter.base.BaseListAdapter;
import com.superstudio.app.improve.adapter.general.EventAdapter;
import com.superstudio.app.improve.app.AppOperator;
import com.superstudio.app.improve.bean.Event;
import com.superstudio.app.improve.bean.base.PageBean;
import com.superstudio.app.improve.bean.base.ResultBean;
import com.superstudio.app.improve.detail.activities.EventDetailActivity;
import com.superstudio.app.improve.fragments.base.BaseGeneralListFragment;
import com.superstudio.app.widget.ViewEventHeader;

import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;

/**
 * 活动界面
 */
public class EventFragment extends BaseGeneralListFragment<Event> {

    private boolean isFirst = true;
    private static final String EVENT_BANNER = "event_banner";
    private ViewEventHeader mHeaderView;
    public static final String HISTORY_EVENT = "history_event";
    private Handler handler = new Handler();

    @Override
    protected void initWidget(View root) {
        super.initWidget(root);
        mHeaderView = new ViewEventHeader(getActivity());

        AppOperator.runOnThread(new Runnable() {
            @SuppressWarnings("unchecked")
            @Override
            public void run() {
                final PageBean<Banner> pageBean = (PageBean<Banner>) CacheManager.readObject(getActivity(), EVENT_BANNER);
                if (pageBean != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
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
    protected void requestData() {
        super.requestData();
        OSChinaApi.getEventList(mIsRefresh ? mBean.getPrevPageToken() : mBean.getNextPageToken(), mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Event event = mAdapter.getItem(position - 1);
        if (event != null) {
            EventDetailActivity.show(getActivity(), event.getId());
            TextView title = (TextView) view.findViewById(R.id.tv_event_title);
            updateTextColor(title, null);
            saveToReadedList(HISTORY_EVENT, event.getId() + "");
        }
    }

    @Override
    protected BaseListAdapter<Event> getListAdapter() {
        return new EventAdapter(this);
    }

    @Override
    protected Type getType() {
        return new TypeToken<ResultBean<PageBean<Event>>>() {
        }.getType();
    }

    @Override
    protected void onRequestFinish() {
        super.onRequestFinish();
        isFirst = false;
    }

    private void getBannerList() {
        OSChinaApi.getBannerList(OSChinaApi.CATALOG_BANNER_EVENT, new TextHttpResponseHandler() {
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
                                CacheManager.saveObject(getActivity(), resultBean.getResult(), EVENT_BANNER);
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
