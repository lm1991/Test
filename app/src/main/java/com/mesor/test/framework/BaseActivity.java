package com.mesor.test.framework;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.mesor.test.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Observable;

import butterknife.ButterKnife;

/**
 * Activity、FragmentActivity基类<br>
 * 
 * @Package com.jiaoyang.video.framework
 * @ClassName BaseActivity
 * @author TryLoveCatch
 * @date 2014年5月21日 下午10:00:58
 */
public abstract class BaseActivity extends AppCompatActivity implements IUI {
	// ================一些常量=====================
	// ================界面相关=====================
	// ================逻辑相关=====================
	/**
	 * 销毁时通知DataTask cancel的观察者
	 */
	protected Observable lifeObservable = new Observable();
	private boolean forbidStartActivityAnimation = false;

	private boolean forbidFinishActivityGesture = true;// 默认不要左划finish
	protected SystemBarTintManager tintManager;

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// super.onSaveInstanceState(outState);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		// if (hasFocus) {
		// getWindow().getDecorView().setSystemUiVisibility(
		// View.SYSTEM_UI_FLAG_LAYOUT_STABLE
		// | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
		// | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
		// | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
		// | View.SYSTEM_UI_FLAG_FULLSCREEN
		// | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
		// }
	}

	// @Override
	// public void onTrimMemory(int level) {
	// switch (level) {
	// case TRIM_MEMORY_COMPLETE:
	// case TRIM_MEMORY_MODERATE:
	// android.os.Process.killProcess(android.os.Process.myPid());
	// System.exit(1);
	// break;
	// }
	// }

	public boolean setMiuiStatusBarDarkMode(boolean darkmode) {
		Class<? extends Window> clazz = getWindow().getClass();
		try {
			int darkModeFlag = 0;
			Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
			Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
			darkModeFlag = field.getInt(layoutParams);
			Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
			extraFlagField.invoke(getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean setMeizuStatusBarDarkIcon(boolean dark) {
		boolean result = false;
		try {
			WindowManager.LayoutParams lp = getWindow().getAttributes();
			Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
			Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
			darkFlag.setAccessible(true);
			meizuFlags.setAccessible(true);
			int bit = darkFlag.getInt(null);
			int value = meizuFlags.getInt(lp);
			if (dark) {
				value |= bit;
			} else {
				value &= ~bit;
			}
			meizuFlags.setInt(lp, value);
			getWindow().setAttributes(lp);
			result = true;
		} catch (Exception e) {
		}
		return result;
	}

	// ================生命周期相关=====================
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// if (status) {
		// if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
		// setTranslucentStatus(true);
		// }
		// // 创建状态栏的管理实例
		// SystemBarTintManager tintManager = new SystemBarTintManager(this);
		// // 激活状态栏设置
		// tintManager.setStatusBarTintEnabled(true);
		// // 激活导航栏设置
		// tintManager.setNavigationBarTintEnabled(true);
		// // 设置一个颜色给系统栏
		// tintManager.setTintColor(Color.parseColor("#00000000"));
		// // 设置一个样式背景给导航栏
		// // tintManager.setNavigationBarTintResource(R.drawable.my_tint);
		// // 设置一个状态栏资源
		// // tintManager.setStatusBarTintDrawable(MyDrawable);
		// }
		// setMiuiStatusBarDarkMode(true);
		// setMeizuStatusBarDarkIcon(true);
		super.onCreate(savedInstanceState);
		ButterKnife.bind(this);
//		UtilManager.getInstance().mUtilActivity.pushActivityToStack(this);
	}

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	/**
	 * 做了4件事:<br>
	 * 1、setContentView<br>
	 * 2、初始化Views<br>
	 * 3、调用initViewProperty<br>
	 * 4、调用initData
	 * 
	 * @Title: onCreate
	 * @param savedInstanceState
	 * @param layoutResId
	 * @return void
	 * @date Apr 18, 2014 11:23:00 AM
	 */
	protected void onCreate(Bundle savedInstanceState, int layoutResId, boolean status) {
		super.onCreate(savedInstanceState);
		if (status) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
				setTranslucentStatus(true);
			}
			// 创建状态栏的管理实例
			tintManager = new SystemBarTintManager(this);
			// 激活状态栏设置
			tintManager.setStatusBarTintEnabled(true);
			// 激活导航栏设置
			tintManager.setNavigationBarTintEnabled(true);
			// 设置一个颜色给系统栏
//			tintManager.setTintColor(getResources().getColor(R.color.title_parent));
			// 设置一个样式背景给导航栏
			// tintManager.setNavigationBarTintResource(R.drawable.my_tint);
			// 设置一个状态栏资源
			// tintManager.setStatusBarTintDrawable(MyDrawable);
		}
		getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
		setContentView(layoutResId);
//		UtilManager.getInstance().mUtilActivity.pushActivityToStack(this);
		ButterKnife.bind(this);

		// ((Application) getApplication()).getNetCheckReceiver().getFilter()
		// .addListener(this);
		initViewProperty();
		initData();
	}

	protected void onCreate(Bundle savedInstanceState, int layoutResId) {
		onCreate(savedInstanceState, layoutResId, false);
	}

	@Override
	protected void onDestroy() {
		lifeObservable.notifyObservers();
		/** 从栈中移除该窗口引用 */
//		UtilManager.getInstance().mUtilActivity.removeActivityFromStack(this);
		// ((Application) getApplication()).getNetCheckReceiver().getFilter()
		// .removeListener(this);
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	// ================事件接口================
	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		if (!this.forbidStartActivityAnimation) {
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
			return;
		}
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		if (!this.forbidStartActivityAnimation) {
			overridePendingTransition(R.anim.right_in, R.anim.left_out);
			return;
		}
	}

	@Override
	public void finish() {
//		hideProgress();
		super.finish();
		if (!this.forbidStartActivityAnimation) {
			overridePendingTransition(R.anim.left_in, R.anim.right_out);
		}
	}

	int startX = 0, startY = 0;

	/*
	 * (non-Javadoc) 手势finish
	 * 
	 * @see android.app.Activity#dispatchTouchEvent(android.view.MotionEvent)
	 */
	// @Override
	public boolean dispatchTouchEvent(MotionEvent event) {

		if (this.forbidFinishActivityGesture) {
			return super.dispatchTouchEvent(event);
		}

		float x = 0, y = 0;
		try {
			x = event.getX();
			y = event.getY();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}

		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			startX = (int) x;
			startY = (int) y;
		}

		if (event.getAction() == MotionEvent.ACTION_UP) {
			if (event.getX() - startX > 100 && Math.abs(event.getY() - startY) < 200) {
				finish();
				startX = 0;
				startY = 0;
				return true;
			}
			startX = 0;
			startY = 0;
		}

		return super.dispatchTouchEvent(event);
	}

	@Override
	public void onBackPressed() {
		FragmentManager fm = getSupportFragmentManager();
		BaseFragment outer = (BaseFragment) fm.findFragmentById(R.id.content_frame);
		if (outer != null) {
			if (outer.onBackPressed()) {
				return;
			}
		}
		super.onBackPressed();
	}

	// @Override
	// public void onNetChanged(boolean isAvailable) {
	// }

	// ================对外方法=====================
	/**
	 * 设置禁止启动Activity动画
	 * 
	 * @Title: setForbidStartActivityAnimation
	 * @param forbidStartActivityAnimation
	 * @return void
	 * @date May 14, 2014 11:14:18 AM
	 */
	public void setForbidStartActivityAnimation(boolean forbidStartActivityAnimation) {
		this.forbidStartActivityAnimation = forbidStartActivityAnimation;
	}

	/**
	 * 设置禁止finish activity手势，用于存在viewpager等手势冲突的activity
	 * 
	 * @Title: setForbidFinishActivityGesture
	 * @param forbidFinishActivityGesture
	 * @return void
	 * @date May 14, 2014 11:48:27 AM
	 */
	public void setForbidFinishActivityGesture(boolean forbidFinishActivityGesture) {
		this.forbidFinishActivityGesture = forbidFinishActivityGesture;
	}

	protected void setContentFragment(Class<? extends BaseFragment> fragmentClass) {
		Bundle arguments = null;
		if (getIntent() != null) {
			arguments = getIntent().getExtras();
		}
		setContentFragment(fragmentClass, arguments);
	}

	protected void setContentFragment(Class<? extends BaseFragment> fragmentClass, Bundle arguments) {
		Fragment fragment = Fragment.instantiate(this, fragmentClass.getName(), arguments);

		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(R.id.content_frame, fragment);
		t.commit();
	}

	protected void setContentFragment(Class<? extends BaseFragment> fragmentClass, Bundle arguments, int contentId) {
		Fragment fragment = Fragment.instantiate(this, fragmentClass.getName(), arguments);

		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(contentId, fragment);
		t.commit();
	}

	protected void setContentFragment(String fragmentClassName, Bundle arguments) {
		Fragment fragment = Fragment.instantiate(this, fragmentClassName, arguments);

		FragmentTransaction t = getSupportFragmentManager().beginTransaction();
		t.replace(R.id.content_frame, fragment);
		t.commit();
	}

//	protected void showToast(String text) {
//		ToastUtil.showShort(this, text);
//	}
//
//	protected void showToast(int resId) {
//		ToastUtil.showShort(this, getString(resId));
//	}

//	ProgressDialog pd;
//
//	protected void showProgress(String text) {
//		if (pd != null && pd.isShowing())
//			return;
//		try {
//			pd = new ProgressDialog(this, R.style.ProgressDialog);
//			pd.setCanceledOnTouchOutside(false);
//			pd.setMessage(text);
//			pd.show();
//		} catch (Exception e) {
//		}
//	}
//
//	protected void showProgress(String text, OnDismissListener listener) {
//		if (pd != null && pd.isShowing())
//			return;
//		try {
//			pd = new ProgressDialog(this, R.style.ProgressDialog);
//			pd.setCanceledOnTouchOutside(true);
//			pd.setOnDismissListener(listener);
//			pd.setMessage(text);
//			pd.show();
//		} catch (Exception e) {
//		}
//	}
//
//	protected void showProgress(int resId) {
//		if (pd != null && pd.isShowing())
//			return;
//		try {
//			pd = new ProgressDialog(this, R.style.ProgressDialog);
//			pd.setCanceledOnTouchOutside(false);
//			pd.setMessage(getString(resId));
//			pd.show();
//		} catch (Exception e) {
//		}
//	}
//
//	public void hideProgress() {
//		try {
//			if (pd != null && pd.isShowing())
//				pd.dismiss();
//		} catch (Exception e) {
//		}
//	}

	private Dialog usualDialog;
	private TextView dialogContentTV;

	/**
	 * fragment通用提示框
	 * @param title 标题文字
	 * @param content  内容文字
	 * @param cancel  取消按钮文字， 传入null时为默认“取消”
	 * @param confirm 确认按钮文字， 传入null时为默认“确定”
	 * @param confirmListener  确认按钮点击事件
	 */
//	protected void showDialog(String title, String content, String cancel, String confirm, final View.OnClickListener confirmListener) {
//		if (usualDialog == null) {
//			usualDialog = new Dialog(this);
//			View rootView = LayoutInflater.from(this).inflate(R.layout.dialog_usual,
//					(ViewGroup) usualDialog.getWindow().getDecorView(), false);
//			TextView titleTV = (TextView) rootView.findViewById(R.id.tv_title);
//			titleTV.setText(title);
//			dialogContentTV = (TextView) rootView.findViewById(R.id.tv_content);
//			dialogContentTV.setText(content);
//			Button cancelBtn = (Button) rootView.findViewById(R.id.btn_cancel);
//			cancelBtn.setText(getString(R.string.cancel));
//			if (!TextUtils.isEmpty(cancel))
//				cancelBtn.setText(cancel);
//			cancelBtn.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					usualDialog.dismiss();
//				}
//			});
//			Button okBtn = (Button) rootView.findViewById(R.id.btn_ok);
//			okBtn.setText(getString(R.string.ok));
//			if (!TextUtils.isEmpty(confirm))
//				okBtn.setText(confirm);
//			okBtn.setOnClickListener(new View.OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					usualDialog.dismiss();
//					confirmListener.onClick(v);
//				}
//			});
//			usualDialog.setContentView(rootView, rootView.getLayoutParams());
//		}
//		usualDialog.show();
//	}

}
