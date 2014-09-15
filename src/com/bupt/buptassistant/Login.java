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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bupt.util.HttpUtil;

public class Login extends Activity {
	private Handler handler;
	private Button loginButton;
	private EditText usernameET, pwdET;
	private TextView register;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		loginButton = (Button)findViewById(R.id.loginButton);
		usernameET = (EditText)findViewById(R.id.username);
		pwdET = (EditText)findViewById(R.id.passwd);
		register = (TextView)findViewById(R.id.register);
		
		loginButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				NetworkThread nt = new NetworkThread();
				nt.start();
				handler = new GetMsgHandler();
			}
		});
		register.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Login.this,Register.class);
				
				startActivity(intent);
			}
		});
	}
	//��ȡ�����
	private String login(){
		String username = usernameET.getText().toString();
		System.out.println("username is:"+username);
		String pwd = pwdET.getText().toString();
		System.out.println("pwd is :"+pwd);
		String result = query(username, pwd);
		return result;
	}
	
	//֪ͨ��֤��������ƥ����ȷ����HomePage
	private boolean notify(String result){
		if(result.equals("1")){
			Toast.makeText(getApplicationContext()
					,"��½�ɹ���", Toast.LENGTH_LONG).show();
			Intent intent = new Intent();
			intent.setClass(Login.this, HomePage.class);
			intent.putExtra("com.bupt.buptassistant.Username",usernameET.getText().toString());
			startActivity(intent);
			return true;
		}
		else if(result.equals("2")){
			Toast.makeText(getApplicationContext(), "�û�������", Toast.LENGTH_LONG).show();
		}
		else if(result.equals("3")) {
			Toast.makeText(getApplicationContext(), "�������", Toast.LENGTH_LONG).show();
		}
		else {
			Toast.makeText(getApplicationContext(),"���ݿ���ʴ���", Toast.LENGTH_LONG).show();
		}
		return false;
	}
	
	//ȷ���û���д���û���������
	private boolean validate(){
		String username = usernameET.getText().toString();
		String pwd = pwdET.getText().toString();
		if(username.equals("")){
			showDialog("����д�û�����");
			return false;
		}
		if (pwd.equals("")){
			showDialog("����д���룡");
			return false;
		}
		return true;
	}
	
	private void showDialog(String msg){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);//ɶ��˼
		builder.setMessage(msg).setCancelable(false).setPositiveButton("ȷ��",
				new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int id) {
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	//��֤�û������루GET������
	private String query(String username, String password){
		//
		String queryString = "username="+username+"&password="+password;
		String url = HttpUtil.BASE_URL+"/test/UserLogin"+"?"+queryString;
		//���ز�ѯ������������˵�flag��
		System.out.println("sending url: "+url);
		String aString = HttpUtil.queryStringForGet(url);//�˺�����������
		System.out.println("aString is: "+aString);
		return aString;
	}
	
	@SuppressLint("HandlerLeak")
	class GetMsgHandler extends Handler{
		@Override
		public void handleMessage(Message msg){
			String s = (String)msg.obj;
			System.out.println("handleMessage s is: "+s);
			Login.this.notify(s);
		}
	}
	
	class NetworkThread extends Thread{
		public void run(){
			Looper.prepare();
			if(validate()){
				String s = login();
				
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
