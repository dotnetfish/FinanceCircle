package com.superstudio.app.fragment;

import android.annotation.TargetApi;
import android.os.Build;
import android.view.View;
import android.widget.AdapterView;

import com.superstudio.app.AppContext;
import com.superstudio.app.adapter.EventApplyAdapter;
import com.superstudio.app.api.remote.OSChinaApi;
import com.superstudio.app.base.BaseListFragment;
import com.superstudio.app.bean.Apply;
import com.superstudio.app.bean.EventAppliesList;
import com.superstudio.app.util.UIHelper;
import com.superstudio.app.util.XmlUtils;

import java.io.InputStream;
import java.io.Serializable;

/**
 * 活动出席人员列表
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @created 2014年12月12日 下午7:59:10
 *
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class EventAppliesFragment extends BaseListFragment<Apply> {

	protected static final String TAG = EventAppliesFragment.class.getSimpleName();
	private static final String CACHE_KEY_PREFIX = "event_apply_user_list";

	@Override
	public void initView(View view) {
		super.initView(view);
	}

	@Override
	protected EventApplyAdapter getListAdapter() {
		return new EventApplyAdapter();
	}

	@Override
	protected String getCacheKeyPrefix() {
		return CACHE_KEY_PREFIX + "_" + mCatalog;
	}

	@Override
	protected EventAppliesList parseList(InputStream is) throws Exception {
		return XmlUtils.toBean(EventAppliesList.class, is);
	}

	@Override
	protected EventAppliesList readList(Serializable seri) {
		return ((EventAppliesList) seri);
	}

	@Override
	protected void sendRequestData() {
		OSChinaApi.getEventApplies(mCatalog, mCurrentPage, mHandler);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Apply item = (Apply) mAdapter.getItem(position);
		if (item != null) {
			if (AppContext.getInstance().isLogin()) {
				UIHelper.showMessageDetail(getActivity(), item.getId(), item.getName());
				return;
			}
			UIHelper.showUserCenter(getActivity(), item.getId(),item.getName());
		}
			
	}
}
