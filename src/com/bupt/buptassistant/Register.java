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
				System.out.println("�ɹ����");
				NetworkThread nt = new NetworkThread();
				System.out.println("�ɹ��������߳�");
				nt.start();
				System.out.println("�ɹ��������߳�");
				handler = new GetMsgHandler();
				System.out.println("�ɹ������µ�handler");
			}
		});
		
	}
	//��ȡ�û�����ֵ���ж��Ƿ�Ϸ�������boolean
	private boolean validateRegister(){
		account = etNewAccount.getText().toString();
		nickname = etNewNickname.getText().toString();
		password = etNewPassword.getText().toString();
		confirmpassword = etConfirmNewPassword.getText().toString();
		school = etSchool.getText().toString();
		classnumber = etClassNumber.getText().toString();
		if(account.equals("")){
			Toast.makeText(getApplicationContext(),"�鷳�����û�������", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(password.equals("")){
			Toast.makeText(getApplicationContext(),"����д����", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(confirmpassword.equals(password) == false){
			Toast.makeText(getApplicationContext(),"����������������벻�԰�", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(classnumber.equals("")){
			Toast.makeText(getApplicationContext(),"�����°��", Toast.LENGTH_SHORT).show();
			return false;
		}
		return true;
	}
	//����һ��������String����������flag�����Ķ�Ӧ��������Ӧ�����ע��ɹ��򷵻ص�½���棬���ɹ������������Ϣ�����޸ĳ�void��
	private boolean registerNotify(String result){
		if(result.equals("1")){
			Toast.makeText(getApplicationContext(), "GO~",Toast.LENGTH_LONG).show();
			Intent intent = new Intent();
			intent.setClass(Register.this, Login.class);
			startActivity(intent);
			return true;
		}
		else if(result.equals("2")){
			Toast.makeText(getApplicationContext(), "�û����Ѿ�����",Toast.LENGTH_LONG).show();
			return false;
		}
		else if(result.equals("3")){
			Toast.makeText(getApplicationContext(), "�뽫��Ϣ��д����",Toast.LENGTH_LONG).show();
			return false;
		}
		else if(result.equals("4")){
			Toast.makeText(getApplicationContext(), "���������Ӵ���",Toast.LENGTH_LONG).show();
			return false;
		}
		return false;
	}
	//����ע����Ϣ������ע��http���ӣ������ܷ�������Ӧflag
	private String register(String account,String nickname,String password,String school,String classnumber){
		System.out.println("register����������");
		String result = "";
		List<NameValuePair> params =  new  ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("username",account));///////#####
		params.add(new BasicNameValuePair("nickname",nickname));
		params.add(new BasicNameValuePair("password",password));
		params.add(new BasicNameValuePair("school",school));
		params.add(new BasicNameValuePair("classnumber",classnumber));
		
		String url = HttpUtil.BASE_URL+"/test/UserRegister";
		result = HttpUtil.updateStringForPost(url, params);//��������
		System.out.println("register�ɹ���������");
		return result;
	}
	
	class GetMsgHandler extends Handler{
		@SuppressLint("HandlerLeak")//ɶ��˼
		
		@Override
		public void handleMessage(Message msg){

			System.out.println("handleMessage����");
			String s = (String)msg.obj;
			Register.this.registerNotify(s);
			System.out.println("registerNotify�Ѿ�ִ��");
		}
	}
	
	class NetworkThread extends Thread{
		public void run(){
			Looper.prepare();
			if(validateRegister()){
				
				System.out.println("���߳�validate�ɹ�");
				String s = register(account, nickname, password, school, classnumber);
				System.out.println("�յ���s�ǣ� "+ s);
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
