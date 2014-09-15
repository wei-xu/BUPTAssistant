package com.bupt.buptassistant;
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

public class Register extends Activity {
	private Handler handler;
	private String account, nickname, password, confirmpassword, school, classnumber;
	private EditText etNewAccount, etNewNickname, etNewPassword, etConfirmNewPassword, etSchool, etClassNumber;
	private Button doneNewAccount;
	//
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		etNewAccount = (EditText)findViewById(R.id.newAccount);
		etNewNickname = (EditText)findViewById(R.id.newNickname);
		etNewPassword = (EditText)findViewById(R.id.newPassword);
		etConfirmNewPassword = (EditText)findViewById(R.id.confirmNewPassword);
		etSchool = (EditText)findViewById(R.id.school);
		etClassNumber = (EditText)findViewById(R.id.classnumber);
		doneNewAccount = (Button)findViewById(R.id.doneNewAccount);
		doneNewAccount.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				System.out.println("成功点击");
				NetworkThread nt = new NetworkThread();
				System.out.println("成功建立新线程");
				nt.start();
				System.out.println("成功启动新线程");
				handler = new GetMsgHandler();
				System.out.println("成功构造新的handler");
			}
		});
		
	}
	//获取用户输入值并判断是否合法，返回boolean
	private boolean validateRegister(){
		account = etNewAccount.getText().toString();
		nickname = etNewNickname.getText().toString();
		password = etNewPassword.getText().toString();
		confirmpassword = etConfirmNewPassword.getText().toString();
		school = etSchool.getText().toString();
		classnumber = etClassNumber.getText().toString();
		if(account.equals("")){
			Toast.makeText(getApplicationContext(),"麻烦填下用户名……", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(password.equals("")){
			Toast.makeText(getApplicationContext(),"请填写密码", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(confirmpassword.equals(password) == false){
			Toast.makeText(getApplicationContext(),"大哥你两次输入密码不对啊", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(classnumber.equals("")){
			Toast.makeText(getApplicationContext(),"请填下班号", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	//传入一个服务器String参数，返回flag翻译后的对应服务器响应，如果注册成功则返回登陆界面，不成功则输出错误信息（待修改成void）
	private boolean registerNotify(String result){
		if(result.equals("1")){
			Toast.makeText(getApplicationContext(), "GO~",Toast.LENGTH_LONG).show();
			Intent intent = new Intent();
			intent.setClass(Register.this, Login.class);
			startActivity(intent);
			return true;
		}
		else if(result.equals("2")){
			Toast.makeText(getApplicationContext(), "用户名已经存在",Toast.LENGTH_LONG).show();
			return false;
		}
		else if(result.equals("3")){
			Toast.makeText(getApplicationContext(), "请将信息填写完整",Toast.LENGTH_LONG).show();
			return false;
		}
		else if(result.equals("4")){
			Toast.makeText(getApplicationContext(), "服务器连接错误",Toast.LENGTH_LONG).show();
			return false;
		}
		return false;
	}
	//建立注册信息，发出注册http连接，并接受服务器响应flag
	private String register(String account,String nickname,String password,String school,String classnumber){
		System.out.println("register函数被调用");
		String result = "";
		List<NameValuePair> params =  new  ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username",account));///////#####
		params.add(new BasicNameValuePair("nickname",nickname));
		params.add(new BasicNameValuePair("password",password));
		params.add(new BasicNameValuePair("school",school));
		params.add(new BasicNameValuePair("classnumber",classnumber));
		
		String url = HttpUtil.BASE_URL+"/test/UserRegister";
		result = HttpUtil.updateStringForPost(url, params);//建立链接
		System.out.println("register成功建立连接");
		return result;
	}
	
	class GetMsgHandler extends Handler{
		@SuppressLint("HandlerLeak")//啥意思
		
		@Override
		public void handleMessage(Message msg){

			System.out.println("handleMessage调用");
			String s = (String)msg.obj;
			Register.this.registerNotify(s);
			System.out.println("registerNotify已经执行");
		}
	}
	
	class NetworkThread extends Thread{
		public void run(){
			Looper.prepare();
			if(validateRegister()){
				
				System.out.println("新线程validate成功");
				String s = register(account, nickname, password, school, classnumber);
				System.out.println("收到的s是： "+ s);
				Message msg = handler.obtainMessage();
				msg.obj = s;
				handler.sendMessage(msg);
			}
			Looper.loop();
		}
	}
	
	//
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

}
