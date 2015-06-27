package com.animationtwitter;

import java.util.ArrayList;
import android.app.Activity;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;

public class MainActivity extends Activity implements OnScrollListener {

	/*
	 * first of all THRESHOLD_SCROLL variable should be converted in to pixels for all size of android devices see onCreate method. It will
	 * check and start when user at least scroll by at least 5 density pixels. This value will not varies and remain constant.
	 */
	private static int THRESHOLD_SCROLL = 5;

	
	
	private ListView mListView;
	private FrameLayout frameBottom;
	protected ArrayList<String> arrayList;


	/*
	 * this variable save the last top position of listview while scrolling.
	 */
	private int oldTop = -1;

	
	
	private int scrollY;

	
	
	/*
	 * this variable save the first visible item on the listview.
	 */
	private int oldFirstVisibleItem = -1;

	
	
	/*
	 * this variable check the current scroll difference is equal to the THRESHOLD_SCROLL.
	 */
	private int lastScollDiff;

	
	
	
	
	/*
	 * variable should be converted in to pixels for all size of android devices. This will set the height of the bottom of the buttons. And
	 * will appear and disappear on all android devices and not stuck in between the scrolling.
	 */
	private int viewHeight = 50;

	
	
	
	/* this will save the last position of scrollY of listview. This value varies on scroll. */
	private int oldScrollY = 5;

	
	
	/*
	 * this variable should detect whether listview is scrolling up or down.
	 */
	private boolean isScrollUp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		viewHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, viewHeight, getResources().getDisplayMetrics());

		THRESHOLD_SCROLL = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, THRESHOLD_SCROLL, getResources()
				.getDisplayMetrics());

		inItView();

	}

	/*
	 * initialize view of widgets
	 */
	@SuppressWarnings("rawtypes")
	private void inItView() {

		mListView = (ListView) findViewById(R.id.list_view);
		frameBottom = (FrameLayout) findViewById(R.id.frame_bottom);
		arrayList = new ArrayList<String>();

		for (int i = 0; i <= 200; i++) {
			arrayList.add(i, "List :: " + i);
		}

		ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayList);
		mListView.setAdapter(adapter);
		mListView.setOnScrollListener(this);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (scrollState == SCROLL_STATE_IDLE) {

			if (isScrollUp) {
				if (scrollY < viewHeight / 2)
					scrollY = 0;
				else
					scrollY = viewHeight;
			} else {
				if (scrollY > viewHeight / 2)
					scrollY = viewHeight;
				else
					scrollY = 0;
			}

			frameBottom.setTranslationY(scrollY);// this will set the animation of view while appearing and disappearing on scroll state
													// idle too.
		}
	}

	@Override
	public void onScroll(AbsListView view, int first_visible_child, int visibleItemCount, int totalItemCount) {

		if (first_visible_child == oldFirstVisibleItem) {
			int top = view.getChildAt(0).getTop();// check the top position of listview
			lastScollDiff = top - oldTop;

			if (lastScollDiff > 0) {
				if (scrollY > 0)
					scrollY -= Math.abs(lastScollDiff);

				if (scrollY < 0)
					scrollY = 0; // it means animation will appear up

				isScrollUp = true; // this will show scrolling up and animation will visible
			} else {
				if (scrollY < viewHeight)
					scrollY += Math.abs(lastScollDiff);

				if (scrollY > viewHeight)
					scrollY = viewHeight;// this will animation appear down smoothly when scrolly is greater than 0

				isScrollUp = false;// this will show scrolling down and animation will hide
			}
			oldTop = top;
			oldFirstVisibleItem = first_visible_child;
		} else {
			if (view.getChildCount() > 0) {
				oldFirstVisibleItem = first_visible_child;
				oldTop = view.getChildAt(0).getTop();
			}
		}

		if (Math.abs(scrollY - oldScrollY) >= THRESHOLD_SCROLL) {
			frameBottom.setTranslationY(scrollY);
			oldScrollY = scrollY;
		}
	}
}
