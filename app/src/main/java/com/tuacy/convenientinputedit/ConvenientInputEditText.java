package com.tuacy.convenientinputedit;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;

public class ConvenientInputEditText extends AppCompatEditText {

	private ConvenientInputPopupWindow mConvenientInputPopupWindow;
	private View                       mDecorView;
	private View                       mContentView;
	private int                        mScrollDistance;
	private Rect                       mRect;
	private EditText                   mEditText;

	public ConvenientInputEditText(Context context) {
		super(context);
		init(context);
	}

	public ConvenientInputEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public ConvenientInputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	private void init(Context context) {
		mEditText = this;
		mConvenientInputPopupWindow = new ConvenientInputPopupWindow(context, new ConvenientInputPopupWindow.KeyboardAction() {
			@Override
			public void onKeyboardRequest() {
				KeyBoardUtils.openKeyBoard(mEditText, getContext());
			}
		});
		mConvenientInputPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

			@Override
			public void onDismiss() {
				if (mScrollDistance > 0) {
					if (null != mContentView) {
						mContentView.scrollBy(0, -mScrollDistance);
					}
					mScrollDistance = 0;
				}
			}
		});
	}

	private void showConvenientPopup() {
		if (null != mConvenientInputPopupWindow) {
			if (!mConvenientInputPopupWindow.isShowing()) {
				mConvenientInputPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
				mConvenientInputPopupWindow.showAtLocation(mDecorView, Gravity.BOTTOM, 0, 0);
				mConvenientInputPopupWindow.update();
				if (null != mDecorView && null != mContentView) {
					int[] pos = new int[2];
					// 获取当前EditText在屏幕中的坐标
					getLocationOnScreen(pos);
					float height = dpToPx(getContext(), 240);
					// * 包括标题栏，但不包括状态栏。
					if (mRect == null) {
						mRect = new Rect();
					}
					mDecorView.getWindowVisibleDisplayFrame(mRect);// 获得view空间，也就是除掉标题栏
					// outRect.top表示状态栏（通知栏)
					DisplayMetrics metrics = new DisplayMetrics();
					WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
					if (windowManager != null) {
						Display display = windowManager.getDefaultDisplay();
						display.getMetrics(metrics);
						int screen = metrics.heightPixels - getStatusBarHeight();
						//计算偏移的距离
						mScrollDistance = (int) ((pos[1] + getMeasuredHeight() - mRect.top) - (screen - height));
						if (mScrollDistance > 0) {
							mContentView.scrollBy(0, mScrollDistance);
						}
					}
				}
			}
		}
	}

	private void hideConvenientPopup() {
		if (null != mConvenientInputPopupWindow) {
			if (mConvenientInputPopupWindow.isShowing()) {
				mConvenientInputPopupWindow.dismiss();
			}
		}
	}

	@Override
	public boolean performClick() {
		return super.performClick();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			performClick();
		}
		super.onTouchEvent(event);
		requestFocus();
		requestFocusFromTouch();
		if (event.getAction() == MotionEvent.ACTION_UP) {
			KeyBoardUtils.closeKeyBoard(this, this.getContext());
			showConvenientPopup();
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (null != mConvenientInputPopupWindow) {
				if (mConvenientInputPopupWindow.isShowing()) {
					mConvenientInputPopupWindow.dismiss();
					return true;
				}
			}
		}
		return super.onKeyDown(keyCode, event);

	}

	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		mDecorView = ((Activity) getContext()).getWindow().getDecorView();
		mContentView = ((Activity) getContext()).getWindow().findViewById(Window.ID_ANDROID_CONTENT);
		KeyBoardUtils.closeKeyBoard(this, this.getContext());
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		hideConvenientPopup();
	}

	/**
	 * 密度转换为像素值
	 */
	public static int dpToPx(Context context, float dp) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dp * scale + 0.5f);
	}

	private int getStatusBarHeight() {
		int statusBarHeight1 = -1;
		//获取status_bar_height资源的ID
		int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			//根据资源ID获取响应的尺寸值
			statusBarHeight1 = getResources().getDimensionPixelSize(resourceId);
		}

		return statusBarHeight1;
	}

}
