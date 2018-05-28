package com.tuacy.convenientinputedit.quickinput;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.PopupWindow;

import com.tuacy.convenientinputedit.utls.KeyBoardUtils;

import java.util.List;

public class QuickInputEditText extends AppCompatEditText {

	private Context                     mContext;
	private EditText                    mEditText;
	private QuickInputBarPopupWindow    mQuickInputBarPopup;
	private QuickInputSelectPopupWindow mQuickInputSelectPopup;
	private int                         mQuickInputBarHeight;
	private int                         mQuickInputSelectHeight;
	private Rect                        mRect;
	private int                         mScrollDistance;
	private Activity                    mActivity;

	public QuickInputEditText(Context context) {
		super(context);
		init();
	}

	public QuickInputEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public QuickInputEditText(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		mContext = getContext();
		mEditText = this;
		mQuickInputBarPopup = new QuickInputBarPopupWindow(mContext, mActionBarAction);
		mQuickInputBarPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
			@Override
			public void onDismiss() {
				if (mScrollDistance > 0) {
					checAttach();
					View contentView = mActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
					if (null != contentView) {
						contentView.scrollBy(0, -mScrollDistance);
					}
					mScrollDistance = 0;
				}
			}
		});
		mQuickInputSelectPopup = new QuickInputSelectPopupWindow(mContext, new QuickInputSelectPopupWindow.OnItemSelectListener() {
			@Override
			public void onItemSelect(String item) {
				if (!TextUtils.isEmpty(item)) {
					mEditText.append(item);
					mEditText.setSelection(mEditText.getText().length());
				}
				//				if (mQuickInputBarPopup.isShowing()) {
				//					mQuickInputBarPopup.dismiss();
				//				}
				//				if (mQuickInputSelectPopup.isShowing()) {
				//					mQuickInputSelectPopup.dismiss();
				//				}
				//				KeyBoardUtils.closeKeyBoard(mEditText, mContext);
			}
		});

		setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					if (mQuickInputBarPopup.isShowing()) {
						mQuickInputBarPopup.dismiss();
					}
					if (mQuickInputSelectPopup.isShowing()) {
						mQuickInputSelectPopup.dismiss();
					}
					KeyBoardUtils.closeKeyBoard(mEditText, mContext);
				}
			}
		});

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
		if (mQuickInputBarHeight == 0 || mQuickInputSelectHeight == 0) {
			return super.onTouchEvent(event);
		}
		super.onTouchEvent(event);
		requestFocus();
		requestFocusFromTouch();
		if (event.getAction() == MotionEvent.ACTION_UP) {
			showPopup();
		}
		return true;
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean handle = false;
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (null != mQuickInputBarPopup && mQuickInputBarPopup.isShowing()) {
				mQuickInputBarPopup.dismiss();
				handle = true;
			}
			if (mQuickInputSelectPopup.isShowing()) {
				mQuickInputSelectPopup.dismiss();
				handle = true;
			}
		}
		return handle || super.onKeyDown(keyCode, event);

	}

	@Override
	public boolean dispatchKeyEventPreIme(KeyEvent event) {
		boolean handle = false;
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			handle = interceptBackPress();
		}
		return handle || super.dispatchKeyEvent(event);

	}

	public void attachActivity(Activity activity) {
		mActivity = activity;
		addOnSoftKeyBoardVisibleListener(mActivity);
	}

	public void setPopupHeight(int actionBarHeight, int keyboardHeight) {
		mQuickInputBarHeight = actionBarHeight;
		mQuickInputSelectHeight = keyboardHeight;
	}

	public void setShortcutList(List<String> shortcutList) {
		mQuickInputSelectPopup.setDataList(shortcutList);
	}

	private void showPopup() {
		checAttach();
		KeyBoardUtils.closeKeyBoard(mEditText, mContext);
		View decorView = mActivity.getWindow().getDecorView();
		View contentView = mActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT);
		if (!mQuickInputBarPopup.isShowing()) {
			mQuickInputBarPopup.setHeight(mQuickInputBarHeight);
			mQuickInputBarPopup.showAtLocation(decorView, Gravity.BOTTOM, 0,
											   mQuickInputSelectHeight + KeyBoardUtils.getSoftButtonsBarHeight(mActivity));
			mQuickInputBarPopup.update();
			//处理滑动
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
				mScrollDistance = (pos[1] + getMeasuredHeight() - mRect.top) - (screen - (mQuickInputBarHeight + mQuickInputSelectHeight));
				if (mScrollDistance > 0) {
					contentView.scrollBy(0, mScrollDistance);
				}
			}
		}

		if (!mQuickInputSelectPopup.isShowing()) {
			mQuickInputSelectPopup.setHeight(mQuickInputSelectHeight);
			mQuickInputSelectPopup.showAtLocation(decorView, Gravity.BOTTOM, 0, 0);
			mQuickInputSelectPopup.update();
		}
	}

	/**
	 * 点击返回键时先隐藏表情布局
	 */
	public boolean interceptBackPress() {
		if (mQuickInputBarPopup.isShowing()) {
			if (mQuickInputBarPopup.isShowing()) {
				mQuickInputBarPopup.dismiss();
			}
			if (mQuickInputSelectPopup.isShowing()) {
				mQuickInputSelectPopup.dismiss();
			}
			KeyBoardUtils.closeKeyBoard(mEditText, mContext);
			return true;
		}
		return false;
	}

	private QuickInputBarPopupWindow.ActionBarAction mActionBarAction = new QuickInputBarPopupWindow.ActionBarAction() {
		@Override
		public void onSwitch() {
			View decorView = mActivity.getWindow().getDecorView();
			if (mQuickInputSelectPopup.isShowing()) {
				mQuickInputSelectPopup.dismiss();
				KeyBoardUtils.openKeyBoard(mEditText, mContext);
			} else {
				mQuickInputSelectPopup.setHeight(mQuickInputSelectHeight);
				mQuickInputSelectPopup.showAtLocation(decorView, Gravity.BOTTOM, 0, 0);
				KeyBoardUtils.closeKeyBoard(mEditText, mContext);
			}
		}

		@Override
		public void onCancel() {
			if (mQuickInputBarPopup.isShowing()) {
				mQuickInputBarPopup.dismiss();
			}
			if (mQuickInputSelectPopup.isShowing()) {
				mQuickInputSelectPopup.dismiss();
			}
			KeyBoardUtils.closeKeyBoard(mEditText, mContext);
		}
	};

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

	private void checAttach() {
		if (mActivity == null) {
			throw new NullPointerException("no set activity please call the attachActivity() function first");
		}
	}

	public void addOnSoftKeyBoardVisibleListener(Activity activity) {
		final View decorView = activity.getWindow().getDecorView();
		decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				Rect rect = new Rect();
				decorView.getWindowVisibleDisplayFrame(rect);
				//计算出可见屏幕的高度
				int displayHeight = rect.bottom - rect.top;
				//获得屏幕整体的高度
				int height = decorView.getHeight();
				//获得键盘高度
				boolean visible = (double) displayHeight / height < 0.8;
				//如果键盘消失
				if (!visible && !mQuickInputSelectPopup.isShowing()) {
					if (mQuickInputBarPopup.isShowing()) {
						mQuickInputBarPopup.dismiss();
					}
					if (mQuickInputSelectPopup.isShowing()) {
						mQuickInputSelectPopup.dismiss();
					}
				}
			}
		});
	}
}
