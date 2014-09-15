package com.bupt.JsonUtil;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.stream.JsonReader;


public class JsonUtil {
	public static ArrayList<HashMap<String, String>> jsonToArray(String jsonData){
		ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String, String>>();
		System.out.println("这里是jsonToArray函数内部！");
//		jsonData = "[{\"classname\":\"通信原理\",\"deadline\":\"2014-06-05\"},{\"classname\":\"计算机网络\",\"deadline\":\"2014-07-01\"}]";
		try{
			@SuppressWarnings("resource")
			
			JsonReader reader = new JsonReader(new StringReader(jsonData));
			reader.beginArray();
			
			while(reader.hasNext()){
				reader.beginObject();
				String classname = "";
				String deadline = "";
				while(reader.hasNext()){
					HashMap<String, String> map = new HashMap<String, String>();
					while(reader.hasNext()){
						String tagName = reader.nextName();
						if(tagName.equals("classname")){
							classname = reader.nextString();
							System.out.println("classname@tagName@while="+classname);
//							map.put("classname",classname);
						}
						else if(tagName.equals("deadline")){
							deadline = reader.nextString();
							System.out.println("deadline@tagName@while="+deadline);
//							map.put("deadline", deadline);
						}
					}
					map.put("classname", classname);
					map.put("deadline",deadline);
					list.add(map);//紧接着put
				}
				
				reader.endObject();
			}
			System.out.println("list.size="+list.size());
			//遍历list
			for(int i = 0; i < list.size();i++){
				System.out.println("遍历list@jsonToArray="+list.get(i).get("classname"));
				System.out.println("遍历list@jsonToArray="+list.get(i).get("deadline"));
			}
				
			
			reader.endArray();
			return list;
		}catch(Exception e){
			e.printStackTrace();
		}
		return list;
	}
	
//	public static ArrayList<HashMap<String, String>> getList(String jsonString){
//		ArrayList<HashMap<String, String>> list = null;
//		try
//		{
//			JSONArray jsonArray = new JSONArray(jsonString);
//			JSONObject jsonObject;
//			list = new ArrayList<HashMap<String, String>>();
//			for (int i = 0; i < jsonArray.length(); i++){
//				jsonObject = jsonArray.getJSONObject(i);
//				list.add(getMap(jsonObject.toString()));
//			}
//			
//		}catch (Exception e){
//			e.printStackTrace();
//		}
//		return list;
//	}
//	
//	public static HashMap<String, String> getMap(String jsonString)
//	{
//	  JSONObject jsonObject;
//	  try
//	  {
//	   jsonObject = new JSONObject(jsonString);   @SuppressWarnings("unchecked")
//	   Iterator<String> keyIter = jsonObject.keys();
//	   String key;
//	   Object value;
//	   HashMap<String, String> valueMap = new HashMap<String, String>();
//	   while (keyIter.hasNext())
//	   {
//	    key = (String) keyIter.next();
//	    value = jsonObject.get(key);
//	    valueMap.put(key, (String) value);
//	   }
//	   return valueMap;
//	  }
//	  catch (JSONException e)
//	  {
//	   e.printStackTrace();
//	  }
//	  return null;
//	}
	
	// 获取数组型的结构,返回ArrayList<HashMap<String, Object>>,方便listview中填充数据
	/**
	* 参数说明:
	* 1.webContent 获取的网页封装的json格式数据
	* 2.key 以数组形式组成的json的键名称 3.jsonName
	* 封装json数组数据的json名称
	* */

	public ArrayList<HashMap<String, Object>> getJSONArray(String webContent,
	String[] key, String jsonName) {
		ArrayList<HashMap<String, Object>> list;
		JSONArray jsonObject;
		try {
			jsonObject = new JSONObject(webContent).getJSONArray(jsonName);//将网络上的json格式
			list = new ArrayList<HashMap<String, Object>>();
			for (int i = 0; i < jsonObject.length(); i++) {
				JSONObject jsonObject2 = (JSONObject) jsonObject.opt(i);
				HashMap<String, Object> map = new HashMap<String, Object>();
			for (int j = 0; j < key.length; j++) {
				map.put(key[j], jsonObject2.getString(key[j]));
				System.out.println(key[j] + "==="
				+ jsonObject2.getString(key[j]));
			}
			list.add(map);
			}
	
		} catch (JSONException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		list = null;
		}
		return list;
	}
	
}
