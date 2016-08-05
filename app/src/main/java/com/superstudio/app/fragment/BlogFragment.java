package com.superstudio.app.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.superstudio.app.adapter.BlogAdapter;
import com.superstudio.app.api.remote.OSChinaApi;
import com.superstudio.app.base.BaseListFragment;
import com.superstudio.app.bean.Blog;
import com.superstudio.app.bean.BlogList;
import com.superstudio.app.interf.OnTabReselectListener;
import com.superstudio.app.util.UIHelper;
import com.superstudio.app.util.XmlUtils;

import java.io.InputStream;
import java.io.Serializable;

/**
 * 博客区中单一模块的展示
 *
 * @author kymjs(kymjs123@gmail.com)
 */
public class BlogFragment extends BaseListFragment<Blog> implements
        OnTabReselectListener {

    public static final String BUNDLE_BLOG_TYPE = "BUNDLE_BLOG_TYPE";

    protected static final String TAG = BlogFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "bloglist_";

    private String blogType;

    @Override
    protected BlogAdapter getListAdapter() {
        return new BlogAdapter();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            blogType = args.getString(BUNDLE_BLOG_TYPE);
        }
    }

    /**
     * 获取当前展示页面的缓存数据
     */
    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + blogType;
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

        OSChinaApi.getBlogList(blogType, mCurrentPage, mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        Blog blog = mAdapter.getItem(position);
        if (blog != null) {
            UIHelper.showBlogDetail(getActivity(), blog.getId());
            // 保存到已读列表
            saveToReadedList(view, BlogList.PREF_READED_BLOG_LIST, blog.getId()
                    + "");
        }
    }

    @Override
    public void onTabReselect() {
        onRefresh();
    }

    @Override
    protected long getAutoRefreshTime() {
        // TODO Auto-generated method stub
        // 最新博客
        if (blogType.equals(BlogList.CATALOG_LATEST)) {
            return 2 * 60 * 60;
        }
        return super.getAutoRefreshTime();
    }
}
