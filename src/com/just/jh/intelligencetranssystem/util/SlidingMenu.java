package com.just.jh.intelligencetranssystem.util;

import com.just.jh.intelligencetranssystem.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.gesture.Gesture;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

public class SlidingMenu extends HorizontalScrollView {

	private LinearLayout wrapper;
	private ViewGroup viewContent;
	private ViewGroup viewMenu;

	private int screenWidth;
	private int menuRightPadding;
	private int menuWidth;

	private boolean isOnce = true;
	private boolean isOpen = true;

	private Context context;
	private boolean canScroll;
	private GestureDetector gestureDetector;

	public SlidingMenu(Context context) {
		this(context, null);
	}

	public SlidingMenu(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SlidingMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		gestureDetector = new GestureDetector(context,
				new HorizontalScrollDetecor());
		canScroll = true;

		TypedArray attributes = context.getTheme().obtainStyledAttributes(
				attrs, R.styleable.SlidingMenu, defStyle, 0);
		int count = attributes.getIndexCount();
		for (int i = 0; i < count; i++) {
			int attr = attributes.getIndex(i);
			switch (attr) {
			case R.styleable.SlidingMenu_rightPadding:
				menuRightPadding = attributes.getDimensionPixelSize(attr,
						(int) TypedValue.applyDimension(
								TypedValue.COMPLEX_UNIT_DIP, 50, context
										.getResources().getDisplayMetrics()));
				break;

			default:
				break;
			}
		}

		attributes.recycle();

		screenWidth = getScreenWidth(context);

	}

	/**
	 * 计算的是该view及其子view的宽和高
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		if (isOnce) {
			wrapper = (LinearLayout) getChildAt(0);
			viewMenu = (ViewGroup) wrapper.getChildAt(0);
			viewContent = (ViewGroup) wrapper.getChildAt(1);

			menuWidth = viewMenu.getLayoutParams().width;
			menuWidth = screenWidth - menuRightPadding;
			viewMenu.getLayoutParams().width = screenWidth - menuRightPadding;
			viewContent.getLayoutParams().width = screenWidth;

			isOnce = false;
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

	}

	/**
	 * 决定该view的子view的摆放位置 通过设置偏移量，将menu隐藏
	 */
	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		super.onLayout(changed, l, t, r, b);
		if (changed) {
			this.scrollTo(menuWidth, 0);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		switch (ev.getAction()) {
		case MotionEvent.ACTION_UP:
			/**
			 * getScrollX: return the scrolled left position of the view
			 */
			int scrollX = getScrollX();

			if (scrollX >= menuWidth / 2) {
				this.smoothScrollTo(menuWidth, 0);
			} else {
				this.smoothScrollTo(0, 0);
			}
			break;
		default:
			break;
		}

		return super.onTouchEvent(ev);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (MotionEvent.ACTION_UP == ev.getAction()) {
			canScroll = true;
		}
		return super.onInterceptTouchEvent(ev)
				&& gestureDetector.onTouchEvent(ev);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);

		/**
		 * 调用属性动画，设置TranslationX; 参数l:即getScrollX()，变化范围是从mMenuWidth~0;
		 */

		viewMenu.setTranslationX(l);

		// float scale = l * 1.0f / menuWidth;// 1~0  
		// viewMenu.setTranslationX(scale * menuWidth);

	}

	public void toggle() {
		if (isOpen) {
			this.smoothScrollTo(menuWidth, 0);
			isOpen = false;
		} else {
			this.smoothScrollTo(0, 0);
			isOpen = true;
		}
	}

	/**
	 * 获得屏幕宽度
	 */
	public int getScreenWidth(Context context) {
		WindowManager windowManager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics = new DisplayMetrics();
		windowManager.getDefaultDisplay().getMetrics(metrics);
		return metrics.widthPixels;

	}

	public class HorizontalScrollDetecor extends SimpleOnGestureListener {

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {

			if (canScroll) {
				if (Math.abs(distanceX) >= Math.abs(distanceY)) {
					canScroll = true;
				} else {
					canScroll = false;
				}
			}
			return canScroll;
		}
	}

}
