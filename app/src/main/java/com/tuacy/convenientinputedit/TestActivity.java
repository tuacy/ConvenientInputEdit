package com.tuacy.convenientinputedit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TestActivity extends AppCompatActivity {

	public static void startUp(Context context) {
		context.startActivity(new Intent(context, TestActivity.class));
	}

	private EditText        mEditText;
	private Button          mButtonChange;
	private TextView        mTextView;
	private EmotionKeyboard mEmotionKeyboard;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		initView();
		initEvent();
		initData();
	}

	public void onWindowFocusChanged(boolean hasFocus) {

	}

	private void initView() {
		mEditText = findViewById(R.id.edit_input);
		mButtonChange = findViewById(R.id.button_change);
		mTextView = findViewById(R.id.text_emotion);

	}

	private void initEvent() {

	}

	private void initData() {
		mEmotionKeyboard = EmotionKeyboard.with(this).setEmotionView(mTextView)//绑定表情面板
										  .bindToContent(findViewById(R.id.layout_content))//绑定内容view
										  .bindToEditText(mEditText)//判断绑定那种EditView
										  .bindToEmotionButton(mButtonChange)//绑定表情按钮
										  .build();

	}

	@Override
	public void onBackPressed() {
		/**
		 * 判断是否拦截返回键操作
		 */
		if (!mEmotionKeyboard.interceptBackPress()) {
			super.onBackPressed();
		}
	}


}
