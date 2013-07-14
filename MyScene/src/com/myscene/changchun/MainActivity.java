package com.myscene.changchun;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MainActivity extends Activity {

	Button pro_create;
	Button pro_search;
	Button pro_localdata;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		pro_create = (Button) findViewById(R.id.pro_create);
		pro_search = (Button) findViewById(R.id.pro_search);
		pro_localdata = (Button)findViewById(R.id.pro_localdata);
		pro_search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ProjectSearchActivity.class);
				startActivity(intent);
			}
		});
		pro_create.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, ProjectCreateActivity.class);
				startActivity(intent);
			}
		});
		
		pro_localdata.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//go to the local data
				Intent intent = new Intent(MainActivity.this,LocalData.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
//		System.out.println(R.string.menu_settings);
		if (item.getTitle().equals("退出") || item.getTitle().equals("Exit")) {
			finish();
		}
		return true;
	}
}
