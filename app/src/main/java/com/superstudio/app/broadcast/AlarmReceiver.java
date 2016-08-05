package com.superstudio.app.broadcast;

import com.superstudio.app.service.NoticeUtils;
import com.superstudio.app.util.TLog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		TLog.log("onReceive ->com.superstudio.app收到定时获取消息");
		NoticeUtils.requestNotice(context);
	}
}
