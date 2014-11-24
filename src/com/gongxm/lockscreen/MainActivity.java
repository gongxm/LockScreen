package com.gongxm.lockscreen;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.gongxm.lockscreen.receiver.ScreenReceiver;

/**
 * =============================
 * 
 * 版本：1.0
 * 
 * 作者：gongxm
 * 
 * 时间：2014年11月24日 上午9:55:15
 * 
 * 修正：
 * 
 * 版权所有：静一科技
 * 
 * 描述： 一键锁屏 =============================
 */

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	// 锁屏
	public void lock(View view) {
		// startActivity(new Intent(this, LockActivity.class));
		// 1、获取设备隐私管理器
		DevicePolicyManager manager = (DevicePolicyManager) this
				.getSystemService(DEVICE_POLICY_SERVICE);
		// 2、进行判断，是否激活了设备管理员权限
		ComponentName who = new ComponentName(this, ScreenReceiver.class);
		if (manager.isAdminActive(who)) {// 2-1激活了设备管理员权限，那么直接锁屏
			Toast.makeText(this, "已开启一键锁屏功能!", 0).show();
		} else {// 2-2 没有激活，那么先激活，再锁屏
			active();
		}
	}

	// 创建快捷方式
	public void makeIcon(View view) {
		makeIcon();
	}

	// 一键卸载应用
	public void uninstall(View view) {
		uninstall();
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

	/**
	 * 创建快捷方式
	 */
	private void makeIcon() {
		Intent intent = new Intent();
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		// 1、指定快捷方式名字
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				getResources().getString(R.string.app_name));

		// 2、指定快捷方式激活的意图
		Intent value = new Intent();
		value.setAction("com.gongxm.lockscreen");
		value.addCategory(Intent.CATEGORY_DEFAULT);
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, value);

		// 3、快捷方式的图标:不指定的话默认为当前应用的图标
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON,
				BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));

		// 4、指定不可创建多个快捷图标
		intent.putExtra("duplicate", false);
		sendBroadcast(intent);// 5、向桌面应用发送广播
		Toast.makeText(this, "创建快捷方式完成！", 0).show();
	}

	/**
	 * 一键卸载应用
	 * 
	 * @param view
	 */
	private void uninstall() {
		// 1、利用代码先将该应用的设备管理员权限给去掉
		DevicePolicyManager manager = (DevicePolicyManager) this
				.getSystemService(DEVICE_POLICY_SERVICE);
		ComponentName who = new ComponentName(this, ScreenReceiver.class);
		manager.removeActiveAdmin(who);
		// 2、卸载应用
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_UNINSTALL_PACKAGE);
		intent.addCategory(Intent.CATEGORY_DEFAULT);
		intent.setData(Uri.parse("package:" + getPackageName()));
		startActivity(intent);
	}

	@Override
	public void onBackPressed() {
		finish();
		android.os.Process.killProcess(android.os.Process.myPid());
		super.onBackPressed();
	}

}
