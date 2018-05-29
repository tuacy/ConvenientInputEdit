package com.tuacy.convenientinputedit;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.tuacy.convenientinputedit.base.MobileBaseActivity;
import com.tuacy.convenientinputedit.quickinput.QuickInputEditText;
import com.tuacy.convenientinputedit.utls.KeyBoardUtils;
import com.tuacy.convenientinputedit.utls.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends MobileBaseActivity {

	private QuickInputEditText mEditEvaluate;
	private QuickInputEditText mEditTextFeel;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mActivity = this;
		mContext = this;
		initView();
		initEvent();
		initData();
	}

	private void initView() {
		mEditEvaluate = findViewById(R.id.edit_shortcut_evaluate);
		mEditEvaluate.attachActivity(this);
		mEditEvaluate.setShortcutList(obtainShortcutEvaluate());
		mEditTextFeel = findViewById(R.id.edit_shortcut_feel);
		mEditTextFeel.attachActivity(this);
		mEditTextFeel.setShortcutList(obtainShortcutFeel());
	}

	private void initEvent() {

	}

	private void initData() {
		initQuickInputEditText();

	}

	/**
	 * 这里先去获取下键盘的高度，存储到SharedPreferences文件里面去（这里没有考虑键盘高度变化的情况）
	 */
	private void initQuickInputEditText() {
		int preferencesKeyboardHeight = PreferencesUtils.getInt(mContext, Contexts.PREFERENCE_KEYBOARD_HEIGHT, -1);
		if (preferencesKeyboardHeight == -1) {
			//人为的弹出键盘，计算键盘高度，隐藏键盘
			getMainHandler().postDelayed(new Runnable() {
				@Override
				public void run() {
					KeyBoardUtils.openKeyBoard(mEditTextFeel, mEditTextFeel.getContext());
					getMainHandler().postDelayed(new Runnable() {
						@Override
						public void run() {
							int keyboardHeight = KeyBoardUtils.getKeyBoardHeight(mActivity);
							PreferencesUtils.putInt(mContext, Contexts.PREFERENCE_KEYBOARD_HEIGHT, keyboardHeight);
							initQuickInputPopHeight(keyboardHeight);
							KeyBoardUtils.closeKeyBoard(mEditTextFeel, mEditTextFeel.getContext());
						}
					}, 200);
				}
			}, 200);
		} else {
			initQuickInputPopHeight(preferencesKeyboardHeight);
		}
	}

	private void initQuickInputPopHeight(int keyboardHeight) {
		if (keyboardHeight <= 0) {
			keyboardHeight = dpToPx(mContext, 250);
		}
		mEditTextFeel.setPopupHeight(dpToPx(mContext, 32), keyboardHeight);
		mEditEvaluate.setPopupHeight(dpToPx(mContext, 32), keyboardHeight);
	}

	private List<String> obtainShortcutEvaluate() {
		List<String> list = new ArrayList<>();
		list.add("哎呀，这个产品真好！");
		list.add("一般搬！");
		list.add("勉强能接受！");
		list.add("有点糟糕！");
		list.add("这产品不行呀！");
		list.add("这都是生产的啥！");
		list.add("后悔买这个产品了！");
		list.add("这产品一点用处都没得！");
		return list;
	}

	private List<String> obtainShortcutFeel() {
		List<String> list = new ArrayList<>();
		list.add("第一感觉非常好！");
		list.add("不错，不错！");
		list.add("阳光！");
		list.add("不好！");
		list.add("有点糟糕！");
		list.add("糟糕！");
		list.add("非常的糟糕！");
		return list;
	}


	public static int dpToPx(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}


}
