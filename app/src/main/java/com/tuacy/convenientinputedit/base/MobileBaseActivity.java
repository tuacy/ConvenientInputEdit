package com.tuacy.convenientinputedit.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public abstract class MobileBaseActivity extends AppCompatActivity {

	protected Activity mActivity;
	protected Context  mContext;
	private   Handler  mMainHandler;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mActivity = this;
		mContext = this;
	}

	protected Handler getMainHandler() {
		if (mMainHandler == null) {
			mMainHandler = new Handler(getMainLooper());
		}
		return mMainHandler;
	}
}
