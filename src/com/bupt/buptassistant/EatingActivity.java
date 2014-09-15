package com.bupt.buptassistant;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;


public class EatingActivity extends Activity {
	

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.food);
		
		Button eatingButton1 = (Button)findViewById(R.id.eating_button_north);
		Button eatingButton2 = (Button)findViewById(R.id.eating_button_school);
		Button eatingButton3 = (Button)findViewById(R.id.eating_button_south);
		
		eatingButton1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "开发中", 1000);
			}
		});
		eatingButton2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent _intent=new Intent(EatingActivity.this,InsideSchool.class);
				startActivity(_intent);
			}
		});
		eatingButton3.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "开发中", 1000);
			}
		});
	}
}
