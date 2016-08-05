package com.superstudio.app.team.viewpagefragment;

import java.io.ByteArrayInputStream;

import com.superstudio.app.AppContext;
import com.superstudio.app.R;
import com.superstudio.app.adapter.ViewPageFragmentAdapter;
import com.superstudio.app.api.remote.OSChinaTeamApi;
import com.superstudio.app.base.BaseActivity;
import com.superstudio.app.base.BaseViewPagerFragment;
import com.superstudio.app.team.bean.Team;
import com.superstudio.app.team.bean.TeamGit;
import com.superstudio.app.team.bean.TeamIssueCatalog;
import com.superstudio.app.team.bean.TeamIssueCatalogList;
import com.superstudio.app.team.bean.TeamProject;
import com.superstudio.app.team.fragment.TeamIssueFragment;
import com.superstudio.app.team.fragment.TeamProjectSelectPopupWindow;
import com.superstudio.app.team.fragment.TeamProjectSelectPopupWindow.TeamProjectPopupWindowCallBack;
import com.superstudio.app.team.ui.TeamMainActivity;
import com.superstudio.app.ui.empty.EmptyLayout;
import com.superstudio.app.util.StringUtils;
import com.superstudio.app.util.UIHelper;
import com.superstudio.app.util.XmlUtils;

import cz.msebera.android.httpclient.Header;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Team 任务列表viewpager
 * 
 * @author FireAnt（http://my.oschina.net/LittleDY）
 * @version 创建时间：2015年1月14日 下午2:18:25
 * 
 */

public class TeamIssueViewPageFragment extends BaseViewPagerFragment {

    private Team mTeam;

    private TeamIssueCatalogList mCatalogList;

    private int mTeamId;

    private TeamProject mTeamProject;

    // 默认进来显示的是所有任务列表
    private int mProjectId = -1;

    private TeamProjectSelectPopupWindow mProjectsDialog;

    private AsyncHttpResponseHandler handler = new AsyncHttpResponseHandler() {

	@Override
	public void onCancel() {
	    super.onCancel();
	}

	@Override
	public void onFinish() {
	    super.onFinish();
	    mErrorLayout.setErrorType(EmptyLayout.HIDE_LAYOUT);
	}

	@Override
	public void onStart() {
	    super.onStart();
	    mErrorLayout.setErrorType(EmptyLayout.NETWORK_LOADING);
	}

	@Override
	public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
	    TeamIssueCatalogList catalogList = XmlUtils.toBean(
		    TeamIssueCatalogList.class, new ByteArrayInputStream(arg2));
	    if (catalogList != null) {
		mCatalogList = catalogList;
		mTabsAdapter.removeAll();
		addCatalogList();
	    } else {
		onFailure(arg0, arg1, arg2, null);
	    }
	}

	@Override
	public void onFailure(int arg0, Header[] arg1, byte[] arg2,
		Throwable arg3) {
	    mErrorLayout.setErrorType(EmptyLayout.NETWORK_ERROR);
	}
    };

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	inflater.inflate(R.menu.team_issue_menu, menu);
	super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	switch (item.getItemId()) {
	case R.id.team_issue_change_state:
	    showProjectsSelectDialog();
	    break;
	case R.id.team_new_issue:
	    UIHelper.showCreateNewIssue(getActivity(), mTeam, mTeamProject, null);
	    break;
	default:
	    break;
	}
	return super.onOptionsItemSelected(item);
    }
    
    private TeamProjectPopupWindowCallBack mCallBack = new TeamProjectPopupWindowCallBack() {

	@Override
	public void callBack(TeamProject teamProject) {
	    if (teamProject.getGit().getId() == mProjectId) {
		return;
	    }
	    mProjectId = teamProject.getGit().getId();
	    mTeamProject = teamProject;
	    sendRequestCatalogList();
	}
    };

    private void showProjectsSelectDialog() {
	if (mProjectsDialog == null) {
	    mProjectsDialog = new TeamProjectSelectPopupWindow(getActivity(),
		    mTeam, mCallBack);
	}
	mProjectsDialog.showAsDropDown(((BaseActivity) getActivity())
		.getActionBar().getCustomView());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	Bundle bundle = getArguments();
	if (bundle != null) {
	    Team team = (Team) bundle
		    .getSerializable(TeamMainActivity.BUNDLE_KEY_TEAM);
	    if (team != null) {
		mTeam = team;
		mTeamId = StringUtils.toInt(mTeam.getId());
	    }
	}
	mTeamProject = getDefaultProject();
	setHasOptionsMenu(true);
    }

    @Override
    protected void onSetupTabAdapter(ViewPageFragmentAdapter adapter) {
	sendRequestCatalogList();
    }

    private void sendRequestCatalogList() {
	int uid = AppContext.getInstance().getLoginUid();
	OSChinaTeamApi.getTeamCatalogIssueList(uid, mTeamId, mProjectId, "",
		handler);
    }

    private void addCatalogList() {
	if (mCatalogList != null && !mCatalogList.getList().isEmpty()
		&& mTabsAdapter != null) {
	    for (TeamIssueCatalog catalog : mCatalogList.getList()) {
		Bundle bundle = getBundle(mTeam, mTeamProject, catalog);
		mTabsAdapter.addTab(catalog.getTitle(), catalog.getTitle(),
			TeamIssueFragment.class, bundle);
	    }
	    mTabsAdapter.notifyDataSetChanged();
	}
    }

    private Bundle getBundle(Team team, TeamProject teamProject, TeamIssueCatalog issueCatalog) {
	Bundle bundle = new Bundle();
	bundle.putSerializable(TeamMainActivity.BUNDLE_KEY_TEAM, team);
	bundle.putSerializable(TeamMainActivity.BUNDLE_KEY_PROJECT, teamProject);
	bundle.putSerializable(TeamMainActivity.BUNDLE_KEY_ISSUE_CATALOG, issueCatalog);
	return bundle;
    }

    private TeamProject getDefaultProject() {
	TeamProject project = new TeamProject();
	project.setSource("Team@OSC");
	TeamGit git = new TeamGit();
	git.setId(-1);
	git.setName("所有任务");
	project.setGit(git);
	return project;
    }
}
