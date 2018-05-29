package com.tuacy.convenientinputedit.utls;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 打开或关闭软键盘
 */
public class KeyBoardUtils {

	/**
	 * 打卡软键盘
	 *
	 * @param editText 输入框
	 * @param context  上下文
	 */
	public static void openKeyBoard(EditText editText, Context context) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.showSoftInput(editText, InputMethodManager.RESULT_SHOWN);
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
		}
	}

	/**
	 * 关闭软键盘
	 *
	 * @param editText 输入框
	 * @param context  上下文
	 */
	public static void closeKeyBoard(EditText editText, Context context) {
		InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		}
	}

	/**
	 * 获取软件盘的高度
	 */
	public static int getKeyBoardHeight(Activity activity) {
		Rect r = new Rect();
		/**
		 * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
		 * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
		 */
		activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
		//获取屏幕的高度
		int screenHeight = activity.getWindow().getDecorView().getRootView().getHeight();
		//计算软件盘的高度
		int softInputHeight = screenHeight - r.bottom;

		/**
		 * 某些Android版本下，没有显示软键盘时减出来的高度总是144，而不是零，
		 * 这是因为高度是包括了虚拟按键栏的(例如华为系列)，所以在API Level高于20时，
		 * 我们需要减去底部虚拟按键栏的高度（如果有的话）
		 */
		//		if (Build.VERSION.SDK_INT >= 20) {
		//			 When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
		softInputHeight = softInputHeight - getSoftButtonsBarHeight(activity);
		//		}

		return softInputHeight < 0 ? 0 : softInputHeight;
	}

	public static int getSoftButtonsBarHeight(Activity activity) {
		DisplayMetrics metrics = new DisplayMetrics();
		//这个方法获取可能不是真实屏幕的高度
		activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int usableHeight = metrics.heightPixels;
		//获取当前屏幕的真实高度
		activity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);
		int realHeight = metrics.heightPixels;
		if (realHeight > usableHeight) {
			return realHeight - usableHeight;
		} else {
			return 0;
		}
	}
}
