package com.tuacy.convenientinputedit;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

	private Context mContext;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mContext = this;
		findViewById(R.id.button_1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				TestActivity.startUp(mContext);
			}
		});

		findViewById(R.id.button_2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ConvenientActivity.startUp(mContext);
			}
		});

	}
}
