package com.tuacy.convenientinputedit;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

public class ConvenientActivity extends AppCompatActivity {

	public static void startUp(Context context) {
		context.startActivity(new Intent(context, ConvenientActivity.class));
	}

	private ConvenientInputEditText mEditText;
	private Handler mHandler;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_convenient);
		initView();
		initEvent();
		initData();
	}

	private void initView() {
		mEditText = findViewById(R.id.edit_convenient);
	}

	private void initEvent() {

	}

	private void initData() {
		mHandler = new Handler(Looper.getMainLooper());
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				KeyBoardUtils.openKeyBoard(mEditText, mEditText.getContext());
				mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						Log.d("tuacy", "height = " + getSupportSoftInputHeight());
						KeyBoardUtils.closeKeyBoard(mEditText, mEditText.getContext());
					}
				}, 300);
			}
		}, 300);
	}

	/**
	 * 获取软件盘的高度
	 */
	private int getSupportSoftInputHeight() {
		Rect r = new Rect();
		/**
		 * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
		 * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
		 */
		getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
		//获取屏幕的高度
		int screenHeight = getWindow().getDecorView().getRootView().getHeight();
		//计算软件盘的高度
		int softInputHeight = screenHeight - r.bottom;

		/**
		 * 某些Android版本下，没有显示软键盘时减出来的高度总是144，而不是零，
		 * 这是因为高度是包括了虚拟按键栏的(例如华为系列)，所以在API Level高于20时，
		 * 我们需要减去底部虚拟按键栏的高度（如果有的话）
		 */
		if (Build.VERSION.SDK_INT >= 20) {
			// When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
			softInputHeight = softInputHeight - getSoftButtonsBarHeight();
		}

		return softInputHeight;
	}


	/**
	 * 底部虚拟按键栏的高度
	 */
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	private int getSoftButtonsBarHeight() {
		DisplayMetrics metrics = new DisplayMetrics();
		//这个方法获取可能不是真实屏幕的高度
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int usableHeight = metrics.heightPixels;
		//获取当前屏幕的真实高度
		getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
		int realHeight = metrics.heightPixels;
		if (realHeight > usableHeight) {
			return realHeight - usableHeight;
		} else {
			return 0;
		}
	}
}
