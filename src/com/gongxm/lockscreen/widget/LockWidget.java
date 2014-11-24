package com.gongxm.lockscreen.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.gongxm.lockscreen.R;

public class LockWidget extends AppWidgetProvider {
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		AppWidgetManager awm = AppWidgetManager.getInstance(context);
		ComponentName provider = new ComponentName(context, LockWidget.class);
		RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
		Intent value = new Intent();//�Զ���Ĺ㲥
		value.setAction("com.gongxm.lockscreen");
		value.addCategory(Intent.CATEGORY_DEFAULT);     //��Ҫ��һ���㲥�����ߴ�������㲥
		value.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent= PendingIntent.getActivity(context, 0, value, 0);
		//����Widget�еİ�ť����¼�
		views.setOnClickPendingIntent(R.id.icon, pendingIntent);
		awm.updateAppWidget(provider, views);
	}
	
}
