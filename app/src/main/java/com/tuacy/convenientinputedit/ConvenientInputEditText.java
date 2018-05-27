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

	private Context                    mContext;
	private ConvenientInputPopupWindow mConvenientInputPopupWindow;
	private int                        mScrollDistance;
	private Rect                       mRect;
	private EditText                   mEditText;
	private int                        mSoftKeyboardHeight;

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
		mContext = context;
		mSoftKeyboardHeight = 0;
		mEditText = this;
		mConvenientInputPopupWindow = new ConvenientInputPopupWindow(context, new ConvenientInputPopupWindow.KeyboardAction() {
			@Override
			public void onKeyboardRequest() {
				hideConvenientPopup();
				KeyBoardUtils.openKeyBoard(mEditText, getContext());
			}
		});
		mConvenientInputPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

			@Override
			public void onDismiss() {
				if (mScrollDistance > 0) {
					View contentView = ((Activity) mContext).getWindow().findViewById(Window.ID_ANDROID_CONTENT);
					if (null != contentView) {
						contentView.scrollBy(0, -mScrollDistance);
					}
					mScrollDistance = 0;
				}
			}
		});
	}

	private boolean showConvenientPopup() {
		if (mConvenientInputPopupWindow == null || mSoftKeyboardHeight == 0) {
			return false;
		}
		View decorView = ((Activity) mContext).getWindow().getDecorView();
		View contentView = ((Activity) mContext).getWindow().findViewById(Window.ID_ANDROID_CONTENT);
		if (!mConvenientInputPopupWindow.isShowing()) {
			mConvenientInputPopupWindow.setHeight(mSoftKeyboardHeight);
			mConvenientInputPopupWindow.showAtLocation(decorView, Gravity.BOTTOM, 0, 0);
			mConvenientInputPopupWindow.update();
			if (null != decorView && null != contentView) {
				int[] pos = new int[2];
				// 获取当前EditText在屏幕中的坐标
				getLocationOnScreen(pos);
				// * 包括标题栏，但不包括状态栏。
				if (mRect == null) {
					mRect = new Rect();
				}
				decorView.getWindowVisibleDisplayFrame(mRect);// 获得view空间，也就是除掉标题栏
				// outRect.top表示状态栏（通知栏)
				DisplayMetrics metrics = new DisplayMetrics();
				WindowManager windowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
				if (windowManager != null) {
					Display display = windowManager.getDefaultDisplay();
					display.getMetrics(metrics);
					int screen = metrics.heightPixels - getStatusBarHeight();
					//计算偏移的距离
					mScrollDistance = (pos[1] + getMeasuredHeight() - mRect.top) - (screen - mSoftKeyboardHeight);
					if (mScrollDistance > 0) {
						contentView.scrollBy(0, mScrollDistance);
					}
				}
			} else {
				return false;
			}
		}
		return true;
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
			KeyBoardUtils.closeKeyBoard(mEditText, mContext);
			mEditText.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (!showConvenientPopup()) {
						KeyBoardUtils.openKeyBoard(mEditText, mContext);
					}
				}
			}, 300);

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
		KeyBoardUtils.closeKeyBoard(this, this.getContext());
	}

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		hideConvenientPopup();
	}

	public void setSoftKeyboardHeight(int height) {
		mSoftKeyboardHeight = height;
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
