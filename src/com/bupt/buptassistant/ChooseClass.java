package com.bupt.buptassistant;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.bupt.util.HttpUtil;

public class ChooseClass extends Activity {
	private Handler handler;
	private Button doneButton;
	//课程box，待改成数组；
	private CheckBox class01, class02,class03, class04, class05, class06;
	private String allClass, username;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chooseclass);
		Intent intent = getIntent();
		username = intent.getStringExtra("com.bupt.buptassistant.Username");
		doneButton = (Button)findViewById(R.id.done_choose);//选到该工程下的其他控件id是不会报错的！！！！！！！！
		class01 = (CheckBox)findViewById(R.id.class01);
		class02 = (CheckBox)findViewById(R.id.class02);
		class03 = (CheckBox)findViewById(R.id.class03);
		class04 = (CheckBox)findViewById(R.id.class04);
		class05 = (CheckBox)findViewById(R.id.class05);
		class06 = (CheckBox)findViewById(R.id.class06);
		
		doneButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				NetworkThread nt = new NetworkThread();
				nt.start();
				handler = new GetMsgHandler();
			}
		});
	}
	
	private boolean notify(String result){
		if(result.equals("1")){
			Toast.makeText(getApplicationContext()
					,"添加成功！", Toast.LENGTH_LONG).show();
			Intent intent = new Intent();
			intent.setClass(ChooseClass.this, HomePage.class);
			intent.putExtra("com.bupt.buptassistant.Username",username);
			startActivity(intent);
			finish();
			return true;
		}
		else {
			Toast.makeText(getApplicationContext(),"数据库访问错误，请稍后再试", Toast.LENGTH_LONG).show();
		}
		return false;
	}
	
	//用于判断用户是否有选择课程，以及收集所有选课信息至allClass字符串，用在doneButton中
	private boolean validate(){
		allClass = "";
		//待修改成数组
		if(class01.isChecked())
			allClass += "1$";
		if(class02.isChecked())
			allClass += "2$";
		if(class03.isChecked())
			allClass += "3$";
		if(class04.isChecked())
			allClass += "4$";
		if(class05.isChecked())
			allClass += "5$";
		if(class06.isChecked())
			allClass += "6$";
		if (allClass.equals("")){
			showDialog("请choose !");
			return false;
		}
		System.out.println("allClass="+allClass);
		return true;
	}
	
	private void showDialog(String msg){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);//啥意思
		builder.setMessage(msg).setCancelable(false).setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	//发送用户选课设置（GET方法）
	private String query(String allString){
		//
		String queryString = "username="+username + "&" + "allClass=" + allString;
		String url = HttpUtil.BASE_URL+"/test/ChooseClass"+"?"+queryString;
		//返回查询结果（服务器端的flag）
		System.out.println("sending url: "+url);
		String aString = HttpUtil.queryStringForGet(url);//此函数建立链接
		System.out.println("aString is: "+aString);
		return aString;
	}
	
	@SuppressLint("HandlerLeak")
	class GetMsgHandler extends Handler{
		@Override
		public void handleMessage(Message msg){
			String s = (String)msg.obj;
			System.out.println("收到flag="+s);
			ChooseClass.this.notify(s);
		}
	}
	
	class NetworkThread extends Thread{
		public void run(){
			Looper.prepare();
			if(validate()){
				String s = query(allClass);
				
				Message msg = handler.obtainMessage();
				msg.obj = s;
				handler.sendMessage(msg);
			}
			Looper.loop();
		}
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

}

