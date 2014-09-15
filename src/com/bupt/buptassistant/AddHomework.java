package com.bupt.buptassistant;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bupt.util.HttpUtil;

public class AddHomework extends Activity {
	private Handler handler;
	private String classname, teacher, deadline, details, username;
	private EditText etclassname, etteacher, etdeadline, etdetails;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_homework);
		Intent intent = getIntent();
		username = intent.getStringExtra("com.bupt.buptassistant.Username");
		etclassname = (EditText)findViewById(R.id.classname);
		etteacher = (EditText)findViewById(R.id.teacher);
		etdeadline = (EditText)findViewById(R.id.deadline);
		etdetails = (EditText)findViewById(R.id.details);
		Button doneAdding = (Button)findViewById(R.id.doneAdding);
		doneAdding.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				NetworkThread nt = new NetworkThread();
				nt.start();
				handler = new GetMsgHandler();
			}
		});
	}
	
	//传入一个服务器String参数，返回flag翻译后的对应服务器响应，如果添加成功则返回Study界面，否则输出错误信息
	private void AddHomeworkNotify(String result){
		if(result.equals("1")){
			Toast.makeText(getApplicationContext(), "作业添加成功",Toast.LENGTH_LONG).show();
			Intent intent = new Intent();
			intent.setClass(AddHomework.this, HomePage.class);
			intent.putExtra("com.bupt.buptassistant.Username", username);
			startActivity(intent);
			finish();
		}
		else if(result.equals("2")){
			Toast.makeText(getApplicationContext(), "请不要重复提交作业",Toast.LENGTH_LONG).show();
		}
		else if(result.equals("3")){
			Toast.makeText(getApplicationContext(), "服务器错误，请稍后再试",Toast.LENGTH_LONG).show();
		}
	}
	
	//获取用户输入值并判断是否合法，返回boolean
		private boolean validateAddHomework(){
			classname = etclassname.getText().toString();
			teacher = etteacher.getText().toString();
			deadline = etdeadline.getText().toString();
			details = etdetails.getText().toString();
			if(classname.equals("")){
				Toast.makeText(getApplicationContext(),"请写明科目", Toast.LENGTH_SHORT).show();
				return false;
			}
			if(teacher.equals("")){
				Toast.makeText(getApplicationContext(),"请确定任课教师", Toast.LENGTH_SHORT).show();
				return false;
			}
			if(deadline.equals("")){
				Toast.makeText(getApplicationContext(),"请确定deadline", Toast.LENGTH_SHORT).show();
				return false;
			}
			return true;
		}

	private String submit(String classname,String teacher,String deadline,String details){
		System.out.println("submit函数被调用");
		String result = "";
		List<NameValuePair> params =  new  ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("classname",classname));
		params.add(new BasicNameValuePair("teacher",teacher));
		params.add(new BasicNameValuePair("deadline",deadline));
		params.add(new BasicNameValuePair("details",details));
		
		String url = HttpUtil.BASE_URL + "/test/AddHomework";
		result = HttpUtil.updateStringForPost(url, params);
		System.out.println("submit函数成功建立连接");
		return result;
	}
	
	class GetMsgHandler extends Handler{
		@SuppressLint("HandlerLeak")//啥意思
		
		@Override
		public void handleMessage(Message msg){

			System.out.println("handleMessage调用");
			String s = (String)msg.obj;
			AddHomework.this.AddHomeworkNotify(s);
			System.out.println("registerNotify已经执行");
		}
	}
	
	class NetworkThread extends Thread{
		public void run(){
			Looper.prepare();
			if(validateAddHomework()){
				
				System.out.println("新线程validate成功");
				String s = submit(classname, teacher, deadline, details);
				System.out.println("收到的s是： "+ s);
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
		getMenuInflater().inflate(R.menu.add_homework, menu);
		return true;
	}

}
