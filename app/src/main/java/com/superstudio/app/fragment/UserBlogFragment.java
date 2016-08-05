package com.superstudio.app.fragment;

import android.view.View;
import android.widget.AdapterView;

import com.superstudio.app.adapter.BlogAdapter;
import com.superstudio.app.api.remote.OSChinaApi;
import com.superstudio.app.base.BaseListFragment;
import com.superstudio.app.bean.Blog;
import com.superstudio.app.bean.BlogList;
import com.superstudio.app.util.UIHelper;
import com.superstudio.app.util.XmlUtils;

import java.io.InputStream;
import java.io.Serializable;

/**
 * 用户的博客列表(用用户的id来获取)
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年10月29日 下午5:09:13
 */
public class UserBlogFragment extends BaseListFragment<Blog> {

    protected static final String TAG = UserBlogFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "user_bloglist_";

    @Override
    protected BlogAdapter getListAdapter() {
        return new BlogAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mCatalog;
    }

    @Override
    protected BlogList parseList(InputStream is) throws Exception {
        return XmlUtils.toBean(BlogList.class, is);
    }

    @Override
    protected BlogList readList(Serializable seri) {
        return ((BlogList) seri);
    }

    @Override
    protected void sendRequestData() {
        OSChinaApi.getUserBlogList(mCatalog, "", mCatalog, mCurrentPage, mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Blog blog = mAdapter.getItem(position);
        if (blog != null)
            UIHelper.showUrlRedirect(view.getContext(), blog.getUrl());
    }
}
