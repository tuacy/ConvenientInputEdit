package com.tuacy.convenientinputedit.quickinput;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.tuacy.convenientinputedit.R;

import java.util.List;

class QuickInputSelectPopupWindow extends PopupWindow {

	public interface OnItemSelectListener {

		void onItemSelect(String item);
	}

	private QuickInputAdapter mAdapter;

	QuickInputSelectPopupWindow(Context context, final OnItemSelectListener listener) {
		super(context);
		View contentView = LayoutInflater.from(context).inflate(R.layout.pop_quick_input_select, null);
		setContentView(contentView);
		setWidth(WindowManager.LayoutParams.MATCH_PARENT);
		setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
		setBackgroundDrawable(new ColorDrawable(0));
		RecyclerView recyclerView = contentView.findViewById(R.id.recycler_pop_quick_input);
		recyclerView.setLayoutManager(new LinearLayoutManager(context));
		mAdapter = new QuickInputAdapter();
		mAdapter.setOnItemSelectListener(new QuickInputAdapter.OnItemSelectListener() {
			@Override
			public void onItemSelect(String item) {
				if (listener != null) {
					listener.onItemSelect(item);
				}
			}
		});
		recyclerView.setAdapter(mAdapter);
	}

	public void setDataList(List<String> dataList) {
		mAdapter.setDataList(dataList);
	}
}
