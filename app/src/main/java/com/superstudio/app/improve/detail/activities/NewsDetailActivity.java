package com.superstudio.app.improve.detail.activities;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.TextHttpResponseHandler;

import com.superstudio.app.AppContext;
import com.superstudio.app.R;
import com.superstudio.app.api.remote.OSChinaApi;
import com.superstudio.app.improve.bean.NewsDetail;
import com.superstudio.app.improve.bean.base.ResultBean;
import com.superstudio.app.improve.bean.simple.Comment;
import com.superstudio.app.improve.detail.contract.NewsDetailContract;
import com.superstudio.app.improve.detail.fragments.DetailFragment;
import com.superstudio.app.improve.detail.fragments.NewsDetailFragment;
import com.superstudio.app.improve.fragments.news.FinanceNewsDetail;
import com.superstudio.app.ui.empty.EmptyLayout;
import com.superstudio.app.util.HTMLUtil;
import com.superstudio.app.util.StringUtils;
import com.superstudio.app.util.URLsUtils;

import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;

/**
 * Created by fei on 2016/6/13.
 * desc:   news detail  module
 */
public class NewsDetailActivity extends DetailActivity<NewsDetail, NewsDetailContract.View> implements NewsDetailContract.Operator {

    /**
     * show news detail
     *
     * @param context context
     * @param id      id
     */
    public static void show(Context context, long id) {
        Intent intent = new Intent(context, NewsDetailActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    int getType() {
        return 6;
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_blog_detail;
    }

    @Override
    void requestData() {
        OSChinaApi.getNewsDetail(getDataId(), OSChinaApi.CATALOG_NEWS_DETAIL, getRequestHandler());
    }

    @Override
    Class<? extends DetailFragment> getDataViewFragment() {
        return NewsDetailFragment.class;
    }

    @Override
    Type getDataType() {
        return new TypeToken<FinanceNewsDetail>() {
        }.getType();
    }

    @Override
    public void toFavorite() {
        int uid = requestCheck();
        if (uid == 0)
            return;
        showWaitDialog(R.string.progress_submit);
        final NewsDetail newsDetail = getData();
        OSChinaApi.getFavReverse(getDataId(), getType(), new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                hideWaitDialog();
                if (newsDetail.isFavorite())
                    AppContext.showToastShort(R.string.del_favorite_faile);
                else
                    AppContext.showToastShort(R.string.add_favorite_faile);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Type type = new TypeToken<ResultBean<NewsDetail>>() {
                    }.getType();

                    ResultBean<NewsDetail> resultBean = AppContext.createGson().fromJson(responseString, type);
                    if (resultBean != null && resultBean.isSuccess()) {
                        newsDetail.setFavorite(!newsDetail.isFavorite());
                        mView.toFavoriteOk(newsDetail);
                        if (newsDetail.isFavorite())
                            AppContext.showToastShort(R.string.add_favorite_success);
                        else
                            AppContext.showToastShort(R.string.del_favorite_success);
                    }
                    hideWaitDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                    onFailure(statusCode, headers, responseString, e);
                }
            }
        });
    }

    @Override
    public void toShare() {
        if (getDataId() != 0 && getData() != null) {
            String content;

            String url = String.format(URLsUtils.URL_MOBILE + "news/%s", getDataId());
            final NewsDetail newsDetail = getData();
            if (newsDetail.getBody().length() > 55) {
                content = HTMLUtil.delHTMLTag(newsDetail.getBody().trim());
                if (content.length() > 55)
                    content = StringUtils.getSubString(0, 55, content);
            } else {
                content = HTMLUtil.delHTMLTag(newsDetail.getBody().trim());
            }
            String title = newsDetail.getTitle();

            if (TextUtils.isEmpty(url) || TextUtils.isEmpty(content) || TextUtils.isEmpty(title)) {
                AppContext.showToast("内容加载失败...");
                return;
            }
            toShare(title, content, url);
        } else {
            AppContext.showToast("内容加载失败...");
        }
    }

@Override
protected AsyncHttpResponseHandler getRequestHandler(){
    return new TextHttpResponseHandler() {
        @Override
        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
            throwable.printStackTrace();
            if (isDestroy())
                return;
            showError(EmptyLayout.NETWORK_ERROR);
        }

        @Override
        public void onSuccess(int statusCode, Header[] headers, String responseString) {
            if (isDestroy())
                return;
            if (!handleData(responseString))
                showError(EmptyLayout.NODATA);
        }
    };


}
    boolean handleData(String responseString) {
        ResultBean<NewsDetail> result=new ResultBean<>();
        try {
            Type type = getDataType();
            Log.e("detial ",""+responseString);
          FinanceNewsDetail detail = AppContext.createGson().fromJson(responseString, type);
            NewsDetail newsDetail=new NewsDetail();
            newsDetail.setHref("");
            newsDetail.setTitle(detail.getTitle().replace("丰登街","赢领集市"));
            newsDetail.setViewCount(Integer.valueOf(detail.getCommentnum()));
            newsDetail.setPubDate(detail.getDate());
           // newsDetail.setId("");
            newsDetail.setBody(detail.getContent().replace("丰登街","赢领集市"));
            newsDetail.setAuthor(detail.getCatname());
            result.setResult(newsDetail);
            result.setCode(1);
            result.setMessage("");
            result.setTime(detail.getDate());
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if (result.isSuccess()) {
            mData = result.getResult();
            handleView();
            return true;
        }

        return false;

    }

    @Override
    public void toSendComment(long id, long commentId, long commentAuthorId, String comment) {
        int uid = requestCheck();
        if (uid == 0)
            return;

        if (TextUtils.isEmpty(comment)) {
            AppContext.showToastShort(R.string.tip_comment_content_empty);
            return;
        }
        OSChinaApi.pubNewsComment(id, commentId, commentAuthorId, comment, new TextHttpResponseHandler() {

            @Override
            public void onStart() {
                super.onStart();
                showWaitDialog(R.string.progress_submit);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                AppContext.showToast("评论失败!");
                hideWaitDialog();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                try {
                    Type type = new TypeToken<ResultBean<Comment>>() {
                    }.getType();

                    ResultBean<Comment> resultBean = AppContext.createGson().fromJson(responseString, type);
                    if (resultBean.isSuccess()) {
                        Comment respComment = resultBean.getResult();
                        if (respComment != null) {
                            NewsDetailContract.View view = mView;
                            if (view != null) {
                                view.toSendCommentOk(respComment);
                            }
                        }
                    }
                    hideWaitDialog();
                } catch (Exception e) {
                    e.printStackTrace();
                    onFailure(statusCode, headers, responseString, e);
                }
                hideWaitDialog();
            }
        });

    }
}
