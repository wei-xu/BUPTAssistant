package com.bupt.buptassistant;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.bupt.JsonUtil.JsonUtil;
import com.bupt.util.HttpUtil;

public class Study extends Activity {
	private ArrayList<HashMap<String,String>> list = null;
	private Handler handler;
	private ListView listView;
	private Button add;
	private String username;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_study);
		Intent intent = getIntent();
		username = intent.getStringExtra("com.bupt.buptassistant.Username");
		
//		SimpleAdapter listAdapter = new SimpleAdapter(this, list, R.layout.homework_item,
//				new String[]{"classname","deadline"}, new int[]{R.id.classname,R.id.deadline});
//		listView = (ListView)findViewById(R.id.listView);
//		listView.setAdapter(listAdapter);
		NetworkThread nt = new NetworkThread();
		nt.start();
		handler = new GetMsgHandler();
		
//		listView.setOnItemClickListener(new OnItemClickListener() {
//			
//			@Override
//			public void onItemClick(AdapterView<?>parent, View view, int position, long id){
//				String classname = (String)list.get(position).get("classname");
//				String deadline = (String)list.get(position).get("deadline");
//						
//			}
//		});
		
		add = (Button)findViewById(R.id.add);
		ButtonListener addListener = new ButtonListener();
		add.setOnClickListener(addListener);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.study, menu);
		return true;
	}
	
	//发送用户选课设置（GET方法）
		private String query(String allString){
			//
			String queryString = "username="+ username;
			String url = HttpUtil.BASE_URL+"/test/MyHomework"+"?"+queryString;
			//返回查询结果（服务器端的flag）
			System.out.println("sending url: "+url);
			String jsonString = HttpUtil.queryStringForGet(url);//此函数建立链接
			System.out.println("连接建立成功");
			return jsonString;
		}
		
	class GetMsgHandler extends Handler{
		@Override
		public void handleMessage(Message msg){
			String s = (String)msg.obj;
			ArrayList<HashMap<String, String>> list = JsonUtil.jsonToArray(s);
			System.out.println("jsonString@handleMessage="+s);
//			for(int i = 0; i < list.size();i++)
//				System.out.println(list.get(i));
			SimpleAdapter listAdapter = new SimpleAdapter(Study.this, list, R.layout.homework_item,
					new String[]{"classname","deadline"}, new int[]{R.id.classname,R.id.deadline});
			listView = (ListView)findViewById(R.id.listView);
			listView.setAdapter(listAdapter);
		}
	}
		
		
	class NetworkThread extends Thread{
		public void run(){
			Looper.prepare();
			System.out.println("under loop method");
			String s = query(username);//返回一个json String
			System.out.println("jsonString@query="+s);
			Message msg = handler.obtainMessage();
			msg.obj = s;
			handler.sendMessage(msg);
			Looper.loop();
		}
		
	}
		
	class ButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			Intent addHomeworkIntent = new Intent();
			addHomeworkIntent.setClass(Study.this, AddHomework.class);
			addHomeworkIntent.putExtra("com.bupt.buptassistant.Username",username);
			startActivity(addHomeworkIntent);
		}
		
	}

}
