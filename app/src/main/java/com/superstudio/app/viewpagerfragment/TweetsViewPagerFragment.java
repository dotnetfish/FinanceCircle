package com.superstudio.app.viewpagerfragment;

import com.superstudio.app.R;
import com.superstudio.app.adapter.ViewPageFragmentAdapter;
import com.superstudio.app.base.BaseListFragment;
import com.superstudio.app.base.BaseViewPagerFragment;
import com.superstudio.app.bean.TweetsList;
import com.superstudio.app.fragment.TweetsFragment;
import com.superstudio.app.interf.OnTabReselectListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

/**
 * 动弹界面（包括最新动弹、热门动弹、我的动弹）
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年9月25日 下午2:21:52
 * 
 */
public class TweetsViewPagerFragment extends BaseViewPagerFragment implements
        OnTabReselectListener {

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {

        String[] title = getResources().getStringArray(
                R.array.tweets_viewpage_arrays);
        adapter.addTab(title[0], "new_tweets", TweetsFragment.class,
                getBundle(TweetsList.CATALOG_LATEST));
        adapter.addTab(title[1], "hot_tweets", TweetsFragment.class,
                getBundle(TweetsList.CATALOG_HOT));
        adapter.addTab(title[2], "my_tweets", TweetsFragment.class,
                getBundle(TweetsList.CATALOG_ME));
    }

    private Bundle getBundle(int catalog) {
        Bundle bundle = new Bundle();
        bundle.putInt(BaseListFragment.BUNDLE_KEY_CATALOG, catalog);
        return bundle;
    }

    @Override
    public void onClick(View v) {}

    @Override
    public void initView(View view) {}

    @Override
    public void initData() {}

    @Override
    public void onTabReselect() {
        Fragment fragment = mTabsAdapter.getItem(mViewPager.getCurrentItem());
        if (fragment != null && fragment instanceof OnTabReselectListener) {
            ((OnTabReselectListener) fragment).onTabReselect();
        }

        /*
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
        */
    }
}