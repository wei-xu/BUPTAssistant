package com.bupt.buptassistant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class HomePage extends Activity {

	private TextView helloUser,chooseClass;
	private ImageView studyPic, foodPic;
	String username;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);
		
		helloUser = (TextView)findViewById(R.id.helloUser);
		studyPic = (ImageView)findViewById(R.id.studyPic);
		foodPic = (ImageView)findViewById(R.id.foodPic);
		chooseClass = (TextView)findViewById(R.id.chooseClass);
		
		Intent intent = getIntent();
		username = intent.getStringExtra("com.bupt.buptassistant.Username");
		helloUser.setText(username + "£¬ÄúºÃ£¡");
		
		ButtonListener buttonListener = new ButtonListener();
		studyPic.setOnClickListener(buttonListener);
		foodPic.setOnClickListener(buttonListener);
		chooseClass.setOnClickListener(buttonListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.home_page, menu);
		return true;
	}
	
	class ButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			if(v.getId()==R.id.studyPic){
				Intent startStudy = new Intent();
				startStudy.setClass(HomePage.this,Study.class);
				startStudy.putExtra("com.bupt.buptassistant.Username", username);
				startActivity(startStudy);
			}
			else if(v.getId()==R.id.foodPic){
				Intent startFood = new Intent();
				startFood.setClass(HomePage.this,EatingActivity.class);
				startActivity(startFood);
			}
			else if(v.getId()==R.id.chooseClass){
				Intent intent = new Intent();
				intent.putExtra("com.bupt.buptassistant.Username", username);
				intent.setClass(HomePage.this, ChooseClass.class);
				startActivity(intent);
			}
		}
		
	}

}
