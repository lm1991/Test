package com.mesor.test.framework;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.mesor.test.R;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Fragment基类<br>
 *
 * @Package com.jiaoyang.video.framework
 * @ClassName BaseFragment
 * @author TryLoveCatch
 * @date 2014年5月21日 下午10:24:10
 */
@SuppressLint("NewApi")
public abstract class BaseFragment extends Fragment implements IUI {

	private View rootView;

	public SystemBarTintManager tintManager;

	@Override
	public void onStart() {
		super.onStart();
//		if (MyApplication.getAccount() != null)
//			if (MyApplication.getCurrentChild() == null) {
//				startActivity(new Intent(getActivity(), SplashActivity.class));
//				getActivity().finish();
//				return;
//			}
	}

	/**
	 * * 做了4件事:<br>
	 * 1、生成rootView<br>
	 * 2、初始化Views<br>
	 * 3、调用initViewProperty<br>
	 * 4、调用initData
	 *
	 * @Title: onCreateView
	 * @param inflater
	 * @param container
	 * @param savedInstanceState
	 * @param layoutResId
	 * @return View
	 * @date Apr 18, 2014 11:24:57 AM
	 */
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, int layoutResId,
							 int color) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		// 创建状态栏的管理实例
		tintManager = new SystemBarTintManager(getActivity());
		// 激活状态栏设置
		tintManager.setStatusBarTintEnabled(true);
		// 激活导航栏设置
		tintManager.setNavigationBarTintEnabled(true);
		// 设置一个颜色给系统栏
		tintManager.setTintColor(color);
		// 设置一个样式背景给导航栏
		// tintManager.setNavigationBarTintResource(R.drawable.my_tint);
		// 设置一个状态栏资源
		// tintManager.setStatusBarTintDrawable(MyDrawable);
		super.onCreateView(inflater, container, savedInstanceState);

		rootView = inflater.inflate(layoutResId, container, false);
		// rootView.setBackgroundResource(R.color.bg_f5);

		ButterKnife.bind(this, rootView);
		initViewProperty();
		initData();

		return rootView;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState, int layoutResId) {
		return onCreateView(inflater, container, savedInstanceState, layoutResId,0xff000000);
	}

	// public View onCreateView(LayoutInflater inflater, ViewGroup container,
	// Bundle savedInstanceState, int layoutResId) {
	// return onCreateView(inflater, container, savedInstanceState, layoutResId,
	// true);
	// }

	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getActivity().getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}

	public AppCompatActivity getAppCompatActivity() {
		return (AppCompatActivity) super.getActivity();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}

	public void replaceFragment(Class<?> fregmentClass, Bundle arguments) {

		Fragment fragment = Fragment.instantiate(getActivity(), fregmentClass.getName(), arguments);
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.content_frame, fragment);
		transaction.commit();
	}

	protected void openFragment(Fragment fromFragment, Class<?> fregmentClass, Bundle arguments) {
		Log.d("open fragment. class={}", fregmentClass.getName());

		Fragment fragment = Fragment.instantiate(getActivity(), fregmentClass.getName(), arguments);
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out);
		transaction.hide(fromFragment);
		transaction.add(R.id.content_frame, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	protected void replaceFragment(Fragment fromFragment, Class<?> fregmentClass, int contentId, Bundle arguments) {
		Log.d("open fragment. class={}", fregmentClass.getName());

		Fragment fragment = Fragment.instantiate(getActivity(), fregmentClass.getName(), arguments);
		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out);
		transaction.replace(contentId, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	protected void openFragment(Fragment fromFragment, Class<?> fregmentClass, int contentId, Bundle arguments) {
		Log.d("open fragment. class={}", fregmentClass.getName());

		Fragment fragment = Fragment.instantiate(getActivity(), fregmentClass.getName(), arguments);
		FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(R.anim.right_in, R.anim.left_out, R.anim.left_in, R.anim.right_out);
		transaction.hide(fromFragment);
		transaction.add(contentId, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
	}

	protected void setTitle(int resId) {
		getActivity().setTitle(resId);
	}

	protected void setTitle(CharSequence title) {
		getActivity().setTitle(title);
	}

	public void showToast(String text) {
		try {
//			ToastUtil.showShort(getActivity(), text);
		} catch (Exception e) {
		}
	}

	public void showToast(int resId) {
		try {
//			ToastUtil.showShort(getActivity(), getString(resId));
		} catch (Exception e) {
		}
	}

	/**
	 * 接收返回键按下事件
	 *
	 * @Title: onBackKeyDown
	 * @return boolean false:back键事件未处理，向下传递。 true：消费掉该事件。
	 * @date 2014-3-10 上午11:15:33
	 */
	public boolean onBackPressed() {
		return false;
	}

	/**
	 * 设置禁止finish activity手势，用于存在viewpager等手势冲突的activity
	 *
	 * @Title: setForbidFinishActivityGesture
	 * @param paramBoolean
	 * @return void
	 * @date 2014-5-20 下午4:44:14
	 */
	protected void setForbidFinishActivityGesture(boolean paramBoolean) {
		if (!(getActivity() instanceof BaseActivity))
			return;
		((BaseActivity) getActivity()).setForbidFinishActivityGesture(paramBoolean);
	}

	/**
	 * 设置禁止启动Activity动画
	 *
	 * @Title: setForbidStartActivityAnimation
	 * @param paramBoolean
	 * @return void
	 * @date 2014-5-20 下午4:44:26
	 */
	public void setForbidStartActivityAnimation(boolean paramBoolean) {
		if (!(getActivity() instanceof BaseActivity))
			return;
		((BaseActivity) getActivity()).setForbidStartActivityAnimation(paramBoolean);
	}

	// 自己增加
	protected final static int TYPE_NO_NET = 1;
	protected final static int TYPE_NO_DATA = 2;
	protected final static int TYPE_ERROR = 3;
	protected final static int TYPE_LOADING = 4;

	public boolean isListValid(List<?> plist) {
		return plist != null && plist.size() > 0;
	}

	private ProgressDialog mProgress;

//	public void showProgress(int resId) {
//		showProgress(getString(resId));
//	}

//	public void showProgress(String tMsg) {
//		if (mProgress != null && mProgress.isShowing())
//			return;
//		try {
//			if (mProgress == null) {
//				mProgress = DialogUtil.createProgressDialog(getActivity(), null, tMsg, null, false);
//			}
//			mProgress.setMessage(tMsg);
//			mProgress.show();
//		} catch (Exception e) {
//		}
//	}

	public boolean hideProgress() {
		try {
			if (mProgress != null && mProgress.isShowing()) {
				mProgress.dismiss();
				return true;
			}
		} catch (Exception e) {
		}
		return false;
	}

//	public void showProgress(String tMsg, boolean cancelable) {
//		if (mProgress != null && mProgress.isShowing())
//			return;
//		try {
//			if (mProgress == null) {
//				mProgress = DialogUtil.createProgressDialog(getActivity(), null, tMsg, null, cancelable);
//			}
//			mProgress.setMessage(tMsg);
//			mProgress.show();
//		} catch (Exception e) {
//		}
//	}

//	public void showProgress(String tMsg, OnDismissListener listener) {
//		if (mProgress != null && mProgress.isShowing())
//			return;
//		try {
//			if (mProgress == null) {
//				mProgress = DialogUtil.createProgressDialog(getActivity(), null, tMsg, null, true);
//			}
//			mProgress.setMessage(tMsg);
//			if (listener != null)
//				mProgress.setOnDismissListener(listener);
//			mProgress.show();
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
//			usualDialog = new Dialog(getActivity());
//			View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_usual,
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
