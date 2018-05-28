package com.tuacy.convenientinputedit;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.tuacy.convenientinputedit.quickinput.QuickInputEditText;
import com.tuacy.convenientinputedit.utls.KeyBoardUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

	private Activity           mActivity;
	private Context            mContext;
	private Handler            mHandler;
	private QuickInputEditText mEditText;
	private QuickInputEditText mEditText1;

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
		mEditText1 = findViewById(R.id.edit_shortcut_input1);
		mEditText1.attachActivity(this);
		mEditText = findViewById(R.id.edit_shortcut_input);
		mEditText.attachActivity(this);
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
						mEditText.setPopupHeight(dpToPx(mContext, 32), KeyBoardUtils.getKeyBoardHeight(mActivity));
						mEditText.setShortcutList(obtainShortcutList());
						mEditText1.setPopupHeight(dpToPx(mContext, 32), KeyBoardUtils.getKeyBoardHeight(mActivity));
						mEditText1.setShortcutList(obtainShortcutList());
						KeyBoardUtils.closeKeyBoard(mEditText, mEditText.getContext());
					}
				}, 300);
			}
		}, 300);

	}

	private List<String> obtainShortcutList() {
		List<String> list = new ArrayList<>();
		list.add("快捷输入0");
		list.add("快捷输入1");
		list.add("快捷输入2");
		list.add("快捷输入3");
		list.add("快捷输入4");
		list.add("快捷输入5");
		list.add("快捷输入6");
		list.add("快捷输入7");
		return list;
	}


	public static int dpToPx(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}


}
