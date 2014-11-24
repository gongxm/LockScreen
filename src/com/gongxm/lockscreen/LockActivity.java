package com.gongxm.lockscreen;

import com.gongxm.lockscreen.receiver.ScreenReceiver;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;

public class LockActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		lockScreen();
		exitApp();
	}
	
	private void exitApp() {
		android.os.Process.killProcess(android.os.Process.myPid());
	}

	/**
	 * ����
	 */
	private void lockScreen() {
		// 1����ȡ�豸��˽������
		DevicePolicyManager manager = (DevicePolicyManager) this
				.getSystemService(DEVICE_POLICY_SERVICE);
		// 2�������жϣ��Ƿ񼤻����豸����ԱȨ��
		ComponentName who = new ComponentName(this, ScreenReceiver.class);
		if (manager.isAdminActive(who)) {// 2-1�������豸����ԱȨ�ޣ���ôֱ������
			manager.lockNow();
		} else {// 2-2 û�м����ô�ȼ��������
			active();
		}
	}

	/**
	 * �������ԱȨ��
	 */
	private void active() {
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		ComponentName who = new ComponentName(this, ScreenReceiver.class);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, who);// Ҫ�����Ӧ�õ�ȫ·����
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getResources().getString(R.string.description)); // ����ʱ����ʾ��������Ϣ
		startActivity(intent);
	}
	
}
