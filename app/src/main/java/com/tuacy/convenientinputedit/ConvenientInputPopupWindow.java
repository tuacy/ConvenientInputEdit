package com.tuacy.convenientinputedit;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.util.List;

public class ConvenientInputPopupWindow extends PopupWindow {

	private RecyclerView mRecyclerView;
	private List<String> mConvenientList;

	public interface KeyboardAction {

		void onKeyboardRequest();
	}

	public ConvenientInputPopupWindow(Context context, final KeyboardAction action) {
		super(context);
		View contentView = LayoutInflater.from(context).inflate(R.layout.popup_convenient_input, null);
		setContentView(contentView);
		mRecyclerView = contentView.findViewById(R.id.recycler_convenient);
		contentView.findViewById(R.id.image_keyboard).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (action != null) {
					action.onKeyboardRequest();
				}
			}
		});
		setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		setHeight(dpToPx(context, 240));
		setAnimationStyle(R.style.AnimationFade);
		ColorDrawable dw = new ColorDrawable(00000);
		setBackgroundDrawable(dw);
	}

	public void setConvenientList(List<String> dataList) {
		mConvenientList = dataList;
	}

	/**
	 * 密度转换为像素值
	 */
	public static int dpToPx(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}
}
