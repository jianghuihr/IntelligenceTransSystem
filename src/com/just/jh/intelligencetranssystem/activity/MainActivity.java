package com.just.jh.intelligencetranssystem.activity;

import java.util.ArrayList;
import java.util.List;

import com.just.jh.intelligencetranssystem.R;
import com.just.jh.intelligencetranssystem.util.SlidingMenu;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends FragmentActivity {

	private SlidingMenu leftMenu;
	private ImageView ivTransSettings;
	private Button btnCityLocation;
	private ViewPager vpTransFunctions;
	private FragmentPagerAdapter vpAdapter;

	private Fragment fragmentRoute;
	private Fragment fragmentSite;
	private Fragment fragmentNearby;
	private Fragment fragmentTransChange;

	private List<Fragment> container;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		initViews();

		setAdapters();

		setListeners();

		Resources resources = getResources();
		String city_location = String.format(
				resources.getString(R.string.btn_city_location), "苏州");
		btnCityLocation.setText(city_location);

	}

	public void initViews() {
		leftMenu = (SlidingMenu) findViewById(R.id.slidemenu_trans);
		ivTransSettings = (ImageView) findViewById(R.id.iv_trans_settings);
		btnCityLocation = (Button) findViewById(R.id.btn_city_location);
		vpTransFunctions = (ViewPager) findViewById(R.id.vp_trans_functions);

		fragmentRoute = new RouteFragment();
		fragmentSite = new SiteFragment();
		fragmentNearby = new NearbyFragment();
		fragmentTransChange = new TransChangeFragment();

		container = new ArrayList<Fragment>();
		container.add(fragmentRoute);
		container.add(fragmentSite);
		container.add(fragmentNearby);
		container.add(fragmentTransChange);

	}

	public void setAdapters() {
		vpAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {

			@Override
			public int getCount() {
				return container.size();
			}

			@Override
			public Fragment getItem(int arg0) {
				return container.get(arg0);
			}
		};

		vpTransFunctions.setAdapter(vpAdapter);

	}

	public void setListeners() {
		ivTransSettings.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				leftMenu.toggle();
			}
		});
		
	}
	

	
}
