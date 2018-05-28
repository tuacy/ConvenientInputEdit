package com.tuacy.convenientinputedit.quickinput;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.tuacy.convenientinputedit.R;

class QuickInputBarPopupWindow extends PopupWindow {

	public interface ActionBarAction {

		void onSwitch();

		void onCancel();
	}

	QuickInputBarPopupWindow(Context context, final ActionBarAction action) {
		super(context);
		View contentView = LayoutInflater.from(context).inflate(R.layout.pop_shortcut_bar, null);
		setContentView(contentView);
		setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		contentView.findViewById(R.id.image_recycler_pop_shortcut_bar_switch).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (action != null) {
					action.onSwitch();
				}
			}
		});
		contentView.findViewById(R.id.text_recycler_pop_shortcut_bar_cancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (action != null) {
					action.onCancel();
				}
			}
		});
		setBackgroundDrawable(new ColorDrawable(0));
		setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}
}
