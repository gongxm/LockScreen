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
 * �汾��1.0
 * 
 * ���ߣ�gongxm
 * 
 * ʱ�䣺2014��11��24�� ����9:55:15
 * 
 * ������
 * 
 * ��Ȩ���У���һ�Ƽ�
 * 
 * ������ һ������ =============================
 */

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	// ����
	public void lock(View view) {
		// startActivity(new Intent(this, LockActivity.class));
		// 1����ȡ�豸��˽������
		DevicePolicyManager manager = (DevicePolicyManager) this
				.getSystemService(DEVICE_POLICY_SERVICE);
		// 2�������жϣ��Ƿ񼤻����豸����ԱȨ��
		ComponentName who = new ComponentName(this, ScreenReceiver.class);
		if (manager.isAdminActive(who)) {// 2-1�������豸����ԱȨ�ޣ���ôֱ������
			Toast.makeText(this, "�ѿ���һ����������!", 0).show();
		} else {// 2-2 û�м����ô�ȼ��������
			active();
		}
	}

	// ������ݷ�ʽ
	public void makeIcon(View view) {
		makeIcon();
	}

	// һ��ж��Ӧ��
	public void uninstall(View view) {
		uninstall();
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

	/**
	 * ������ݷ�ʽ
	 */
	private void makeIcon() {
		Intent intent = new Intent();
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		// 1��ָ����ݷ�ʽ����
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
				getResources().getString(R.string.app_name));

		// 2��ָ����ݷ�ʽ�������ͼ
		Intent value = new Intent();
		value.setAction("com.gongxm.lockscreen");
		value.addCategory(Intent.CATEGORY_DEFAULT);
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, value);

		// 3����ݷ�ʽ��ͼ��:��ָ���Ļ�Ĭ��Ϊ��ǰӦ�õ�ͼ��
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON,
				BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));

		// 4��ָ�����ɴ���������ͼ��
		intent.putExtra("duplicate", false);
		sendBroadcast(intent);// 5��������Ӧ�÷��͹㲥
		Toast.makeText(this, "������ݷ�ʽ��ɣ�", 0).show();
	}

	/**
	 * һ��ж��Ӧ��
	 * 
	 * @param view
	 */
	private void uninstall() {
		// 1�����ô����Ƚ���Ӧ�õ��豸����ԱȨ�޸�ȥ��
		DevicePolicyManager manager = (DevicePolicyManager) this
				.getSystemService(DEVICE_POLICY_SERVICE);
		ComponentName who = new ComponentName(this, ScreenReceiver.class);
		manager.removeActiveAdmin(who);
		// 2��ж��Ӧ��
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
