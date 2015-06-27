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
	private static int THRESHOLD_SCROLL = 5;

	private ListView mListView;
	private FrameLayout frameBottom;
	protected ArrayList<String> arrayList;

	private int oldTop = -1;
	private int scrollY;
	private int oldFirstVisibleItem = -1;

	private int lastScollDiff;
	private int viewHeight = 50;

	private int oldScrollY = 5;

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

			frameBottom.setTranslationY(scrollY);
		}
	}

	@Override
	public void onScroll(AbsListView view, int first_visible_child, int visibleItemCount, int totalItemCount) {

		if (first_visible_child == oldFirstVisibleItem) {
			int top = view.getChildAt(0).getTop();
			lastScollDiff = top - oldTop;

			if (lastScollDiff > 0) {
				if (scrollY > 0)
					scrollY -= Math.abs(lastScollDiff);

				if (scrollY < 0)
					scrollY = 0;

				isScrollUp = true;
			} else {
				if (scrollY < viewHeight)
					scrollY += Math.abs(lastScollDiff);

				if (scrollY > viewHeight)
					scrollY = viewHeight;

				isScrollUp = false;
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
