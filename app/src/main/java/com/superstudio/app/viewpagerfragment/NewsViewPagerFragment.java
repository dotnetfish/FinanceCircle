package com.superstudio.app.viewpagerfragment;

import com.superstudio.app.R;
import com.superstudio.app.adapter.ViewPageFragmentAdapter;
import com.superstudio.app.base.BaseListFragment;
import com.superstudio.app.base.BaseViewPagerFragment;
import com.superstudio.app.bean.BlogList;
import com.superstudio.app.bean.NewsList;
import com.superstudio.app.fragment.BlogFragment;
import com.superstudio.app.fragment.NewsFragment;
import com.superstudio.app.interf.OnTabReselectListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * 资讯viewpager页面
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年9月25日 下午2:21:52
 * 
 */
public class NewsViewPagerFragment extends BaseViewPagerFragment implements
        OnTabReselectListener {

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        String[] title = getResources().getStringArray(
                R.array.news_viewpage_arrays);
        adapter.addTab(title[0], "news", NewsFragment.class,
                getBundle(NewsList.CATALOG_ALL));
        adapter.addTab(title[1], "news_week", NewsFragment.class,
                getBundle(NewsList.CATALOG_WEEK));
        adapter.addTab(title[2], "latest_blog", BlogFragment.class,
                getBundle(BlogList.CATALOG_LATEST));
        adapter.addTab(title[3], "recommend_blog", BlogFragment.class,
                getBundle(BlogList.CATALOG_RECOMMEND));
    }

    private Bundle getBundle(int newType) {
        Bundle bundle = new Bundle();
        bundle.putInt(BaseListFragment.BUNDLE_KEY_CATALOG, newType);
        return bundle;
    }

    @Override
    protected void setScreenPageLimit() {
        mViewPager.setOffscreenPageLimit(3);
    }

    /**
     * 基类会根据不同的catalog展示相应的数据
     * 
     * @param catalog
     *            要显示的数据类别
     * @return
     */
    private Bundle getBundle(String catalog) {
        Bundle bundle = new Bundle();
        bundle.putString(BlogFragment.BUNDLE_BLOG_TYPE, catalog);
        return bundle;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void initView(View view) {

    }

    @Override
    public void initData() {

    }

    @Override
    public void onTabReselect() {
        try {
            int currentIndex = mViewPager.getCurrentItem();
            Fragment currentFragment = getChildFragmentManager().getFragments()
                    .get(currentIndex);
            if (currentFragment != null
                    && currentFragment instanceof OnTabReselectListener) {
                OnTabReselectListener listener = (OnTabReselectListener) currentFragment;
                listener.onTabReselect();
            }
        } catch (NullPointerException e) {
        }
    }
}
