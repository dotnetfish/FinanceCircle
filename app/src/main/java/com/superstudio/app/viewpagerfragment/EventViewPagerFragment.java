package com.superstudio.app.viewpagerfragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.superstudio.app.R;
import com.superstudio.app.adapter.ViewPageFragmentAdapter;
import com.superstudio.app.base.BaseViewPagerFragment;
import com.superstudio.app.bean.EventList;
import com.superstudio.app.fragment.EventFragment;
import com.superstudio.app.ui.SimpleBackActivity;

/**
 * 活动viewpager页面
 *
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2014年12月24日 下午4:46:04
 */
public class EventViewPagerFragment extends BaseViewPagerFragment {

    private int position = 0;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        position = bundle.getInt(SimpleBackActivity.BUNDLE_KEY_ARGS, 0);
    }

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
        String[] title = getResources().getStringArray(R.array.events);
        if (position == 0) {
            adapter.addTab(title[0], "new_event", EventFragment.class, getBundle(EventList.EVENT_LIST_TYPE_NEW_EVENT));
            adapter.addTab(title[1], "my_event", EventFragment.class, getBundle(EventList.EVENT_LIST_TYPE_MY_EVENT));
            mTabStrip.setVisibility(View.VISIBLE);
        } else {
            adapter.addTab(title[1], "my_event", EventFragment.class, getBundle(EventList.EVENT_LIST_TYPE_MY_EVENT));
            mTabStrip.setVisibility(View.GONE);
        }
        mViewPager.setCurrentItem(position, true);
    }

    private Bundle getBundle(int event_type) {

        Bundle bundle = new Bundle();
        bundle.putInt(EventFragment.BUNDLE_KEY_EVENT_TYPE, event_type);
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
}
