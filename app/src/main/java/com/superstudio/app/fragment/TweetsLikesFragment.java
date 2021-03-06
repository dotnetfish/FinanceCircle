package com.superstudio.app.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.superstudio.app.AppContext;
import com.superstudio.app.R;
import com.superstudio.app.adapter.TweetLikeAdapter;
import com.superstudio.app.api.remote.OSChinaApi;
import com.superstudio.app.base.BaseListFragment;
import com.superstudio.app.bean.Constants;
import com.superstudio.app.bean.Entity;
import com.superstudio.app.bean.Notice;
import com.superstudio.app.bean.Tweet;
import com.superstudio.app.bean.TweetLike;
import com.superstudio.app.bean.TweetLikeList;
import com.superstudio.app.bean.TweetsList;
import com.superstudio.app.improve.activities.TweetDetailActivity;
import com.superstudio.app.service.NoticeUtils;
import com.superstudio.app.ui.empty.EmptyLayout;
import com.superstudio.app.util.UIHelper;
import com.superstudio.app.util.XmlUtils;
import com.superstudio.app.viewpagerfragment.NoticeViewPagerFragment;

import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

/**
 * 赞过我动弹的列表
 *
 * @date 2014年10月10日
 */
public class TweetsLikesFragment extends BaseListFragment<TweetLike> {

    private static final String CACHE_KEY_PREFIX = "mytweets_like_list_";

    private boolean mIsWatingLogin;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            setupContent();
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mCatalog > 0) {
            IntentFilter filter = new IntentFilter(
                    Constants.INTENT_ACTION_USER_CHANGE);
            filter.addAction(Constants.INTENT_ACTION_LOGOUT);
            getActivity().registerReceiver(mReceiver, filter);
        }
    }

    @Override
    public void onDestroy() {
        if (mCatalog > 0) {
            getActivity().unregisterReceiver(mReceiver);
        }
        super.onDestroy();
    }

    @Override
    protected TweetLikeAdapter getListAdapter() {
        return new TweetLikeAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mCatalog;
    }

    @Override
    protected TweetLikeList parseList(InputStream is) throws Exception {
        TweetLikeList list = XmlUtils.toBean(TweetLikeList.class, is);
        return list;
    }

    @Override
    protected TweetLikeList readList(Serializable seri) {
        return ((TweetLikeList) seri);
    }

    @Override
    protected void sendRequestData() {
        OSChinaApi.getTweetLikeList(mCurrentPage, mHandler);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        TweetLike tweetLike = mAdapter.getItem(position);
        if (tweetLike != null) {
            Tweet tweet = tweetLike.getTweet();
//            UIHelper.showTweetDetail(view.getContext(), null, tweet.getId());
            TweetDetailActivity.show(getActivity(), tweet);
        }
    }

    private void setupContent() {
        if (AppContext.getInstance().isLogin()) {
            mIsWatingLogin = false;
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
            requestData(true);
        } else {
            mCatalog = TweetsList.CATALOG_ME;
            mIsWatingLogin = true;
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            mErrorLayout.setErrorMessage(getString(R.string.unlogin_tip));
        }
    }

    @Override
    protected void requestData(boolean refresh) {
        if (AppContext.getInstance().isLogin()) {
            mCatalog = AppContext.getInstance().getLoginUid();
            mIsWatingLogin = false;
            super.requestData(refresh);
        } else {
            mIsWatingLogin = true;
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
            mErrorLayout.setErrorMessage(getString(R.string.unlogin_tip));
        }
    }

    @Override
    public void initView(View view) {
        super.initView(view);
        mErrorLayout.setOnLayoutClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (AppContext.getInstance().isLogin()) {
                    mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
                    requestData(true);
                } else {
                    UIHelper.showLoginActivity(getActivity());
                }
            }
        });
    }

    @Override
    protected long getAutoRefreshTime() {
        if (mCatalog == TweetsList.CATALOG_LATEST) {
            return 3 * 60;
        }
        return super.getAutoRefreshTime();
    }

    @Override
    protected void onRefreshNetworkSuccess() {
        // TODO Auto-generated method stub

        super.onRefreshNetworkSuccess();
        if (AppContext.getInstance().isLogin()
                && mCatalog == AppContext.getInstance().getLoginUid()
                && 4 == NoticeViewPagerFragment.sCurrentPage) {
            NoticeUtils.clearNotice(Notice.TYPE_NEWLIKE);
            UIHelper.sendBroadcastForNotice(getActivity());
        }
    }

    protected boolean compareTo(List<? extends Entity> data, Entity enity) {
        int s = data.size();

        if (enity != null && enity instanceof TweetLike) {
            TweetLike tweetLike = (TweetLike) enity;
            for (int i = 0; i < s; i++) {
                if (tweetLike.getUser().getId() == ((TweetLike) data.get(i)).getId()) {
                    return true;
                }
            }
        }
        return false;
    }
}