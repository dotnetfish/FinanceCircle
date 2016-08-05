package com.superstudio.app.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;

import com.superstudio.app.adapter.SearchAdapter;
import com.superstudio.app.api.remote.OSChinaApi;
import com.superstudio.app.base.BaseListFragment;
import com.superstudio.app.bean.SearchList;
import com.superstudio.app.bean.SearchResult;
import com.superstudio.app.ui.empty.EmptyLayout;
import com.superstudio.app.util.UIHelper;
import com.superstudio.app.util.XmlUtils;

import java.io.InputStream;
import java.io.Serializable;

public class SearchFragment extends BaseListFragment<SearchResult> {
    protected static final String TAG = SearchFragment.class.getSimpleName();
    private static final String CACHE_KEY_PREFIX = "search_list_";
    private String mCatalog;
    private String mSearch;
    private boolean mRquestDataIfCreated = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mCatalog = args.getString(BUNDLE_KEY_CATALOG);
        }
        int mode = WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN
                | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN;
        getActivity().getWindow().setSoftInputMode(mode);
    }

    public void search(String search) {
        mSearch = search;
        if (mErrorLayout != null) {
            mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
            mState = STATE_REFRESH;
            requestData(true);
        } else {
            mRquestDataIfCreated = true;
        }
    }

    @Override
    protected boolean requestDataIfViewCreated() {
        return mRquestDataIfCreated;
    }

    @Override
    protected SearchAdapter getListAdapter() {
        return new SearchAdapter();
    }

    @Override
    protected String getCacheKeyPrefix() {
        return CACHE_KEY_PREFIX + mCatalog + mSearch;
    }

    @Override
    protected SearchList parseList(InputStream is) throws Exception {
        SearchList list = XmlUtils.toBean(SearchList.class, is);
        return list;
    }

    @Override
    protected SearchList readList(Serializable seri) {
        return ((SearchList) seri);
    }

    @Override
    protected void sendRequestData() {
        OSChinaApi.getSearchList(mCatalog, mSearch, mCurrentPage, mHandler);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        SearchResult res = mAdapter.getItem(position);
        if (res != null) {
            if (res.getType().equalsIgnoreCase(SearchList.CATALOG_SOFTWARE)) {
                UIHelper.showSoftwareDetailById(getActivity(), res.getId());
            } else {
                UIHelper.showUrlRedirect(getActivity(), res.getUrl());
            }
        }
    }
}
