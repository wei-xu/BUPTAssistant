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
	
	//����һ��������String����������flag�����Ķ�Ӧ��������Ӧ�������ӳɹ��򷵻�Study���棬�������������Ϣ
	private void AddHomeworkNotify(String result){
		if(result.equals("1")){
			Toast.makeText(getApplicationContext(), "��ҵ��ӳɹ�",Toast.LENGTH_LONG).show();
			Intent intent = new Intent();
			intent.setClass(AddHomework.this, HomePage.class);
			intent.putExtra("com.bupt.buptassistant.Username", username);
			startActivity(intent);
			finish();
		}
		else if(result.equals("2")){
			Toast.makeText(getApplicationContext(), "�벻Ҫ�ظ��ύ��ҵ",Toast.LENGTH_LONG).show();
		}
		else if(result.equals("3")){
			Toast.makeText(getApplicationContext(), "�������������Ժ�����",Toast.LENGTH_LONG).show();
		}
	}
	
	//��ȡ�û�����ֵ���ж��Ƿ�Ϸ�������boolean
		private boolean validateAddHomework(){
			classname = etclassname.getText().toString();
			teacher = etteacher.getText().toString();
			deadline = etdeadline.getText().toString();
			details = etdetails.getText().toString();
			if(classname.equals("")){
				Toast.makeText(getApplicationContext(),"��д����Ŀ", Toast.LENGTH_SHORT).show();
				return false;
			}
			if(teacher.equals("")){
				Toast.makeText(getApplicationContext(),"��ȷ���ον�ʦ", Toast.LENGTH_SHORT).show();
				return false;
			}
			if(deadline.equals("")){
				Toast.makeText(getApplicationContext(),"��ȷ��deadline", Toast.LENGTH_SHORT).show();
				return false;
			}
			return true;
		}

	private String submit(String classname,String teacher,String deadline,String details){
		System.out.println("submit����������");
		String result = "";
		List<NameValuePair> params =  new  ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("classname",classname));
		params.add(new BasicNameValuePair("teacher",teacher));
		params.add(new BasicNameValuePair("deadline",deadline));
		params.add(new BasicNameValuePair("details",details));
		
		String url = HttpUtil.BASE_URL + "/test/AddHomework";
		result = HttpUtil.updateStringForPost(url, params);
		System.out.println("submit�����ɹ���������");
		return result;
	}
	
	class GetMsgHandler extends Handler{
		@SuppressLint("HandlerLeak")//ɶ��˼
		
		@Override
		public void handleMessage(Message msg){

			System.out.println("handleMessage����");
			String s = (String)msg.obj;
			AddHomework.this.AddHomeworkNotify(s);
			System.out.println("registerNotify�Ѿ�ִ��");
		}
	}
	
	class NetworkThread extends Thread{
		public void run(){
			Looper.prepare();
			if(validateAddHomework()){
				
				System.out.println("���߳�validate�ɹ�");
				String s = submit(classname, teacher, deadline, details);
				System.out.println("�յ���s�ǣ� "+ s);
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
