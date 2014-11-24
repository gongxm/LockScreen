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
	 * 锁屏
	 */
	private void lockScreen() {
		// 1、获取设备隐私管理器
		DevicePolicyManager manager = (DevicePolicyManager) this
				.getSystemService(DEVICE_POLICY_SERVICE);
		// 2、进行判断，是否激活了设备管理员权限
		ComponentName who = new ComponentName(this, ScreenReceiver.class);
		if (manager.isAdminActive(who)) {// 2-1激活了设备管理员权限，那么直接锁屏
			manager.lockNow();
		} else {// 2-2 没有激活，那么先激活，再锁屏
			active();
		}
	}

	/**
	 * 激活管理员权限
	 */
	private void active() {
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		ComponentName who = new ComponentName(this, ScreenReceiver.class);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, who);// 要激活的应用的全路径名
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getResources().getString(R.string.description)); // 激活时，提示的描述信息
		startActivity(intent);
	}
	
}
