package com.bupt.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpUtil {

	//����URL
	public static final String BASE_URL = "http://10.0.2.2:8080";
	
	//���Get������󣬰�����HttpGet����
	public static HttpGet getHttpGet(String url){
		HttpGet request = new HttpGet(url);
		return request;
	}
	
	public static HttpPost getHttpPost(String url){
		HttpPost request = new HttpPost(url);
		return request;
	}
	
	//����������get��Ӧ����response
	public static HttpResponse getHttpResponse(HttpGet request) throws ClientProtocolException, IOException{
		HttpResponse response = new DefaultHttpClient().execute(request);
		return response;
	}
	//��ô�������أ�
	public static HttpResponse getHttpResponse(HttpPost post) throws ClientProtocolException, IOException{
		HttpResponse response = new DefaultHttpClient().execute(post);
		return response;
	}
	
	//����get����
	public static String queryStringForGet(String url){
		String result = "0";
		HttpGet request = HttpUtil.getHttpGet(url);
		//InputStream inputStream = null;
		try{
			//���response����
			HttpResponse response = HttpUtil.getHttpResponse(request);
			//httpEntity = httpResponse.getEntity();
			//inputStream = httpEntity.getContent();
			//BufferedReader reader = new BufferedReader(new InputStreamReader());
			//String result = "";
			//String line = "";
			//while((line = reader.readline())!=null){
			//result += line;}
			//�ж��Ƿ�����ɹ�
			//
			if (response.getStatusLine().getStatusCode() == 200){
				//�����Ӧ
				//
				//
				result = EntityUtils.toString(response.getEntity()).trim();
				return result;
				
			}
		}catch (ClientProtocolException e){
			e.printStackTrace();
			result = "�����쳣��";
		}catch (IOException e){
			e.printStackTrace();
			result = "�����쳣!";
		}
		return result;
	}
	
	public static ArrayList<HashMap<String,String>> queryArrayForGet(String url){
		ArrayList<HashMap<String, String>> result = null;
		HttpGet request = HttpUtil.getHttpGet(url);
		try{
			HttpResponse response = HttpUtil.getHttpResponse(request);
			if(response.getStatusLine().getStatusCode()==200){
				result = (ArrayList<HashMap<String, String>>) response.getEntity();
			}
		}catch(ClientProtocolException e){
			e.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}
		
		return result;
		
	}
	
	//post������ֵresultΪ���������ص�flag
	public static String updateStringForPost(String url, List<NameValuePair> params){
		String result = "0";
		System.out.println("sending url" + url);
		HttpPost post = HttpUtil.getHttpPost(url);
		
		try{
			post.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
			HttpResponse response = HttpUtil.getHttpResponse(post);
			System.out.println("returnstatuscode is: " + response.getStatusLine().getStatusCode());
			if (response.getStatusLine().getStatusCode() == 200){
				result = EntityUtils.toString(response.getEntity()).trim();
				System.out.println("statauscode is 200");
				System.out.println("result is: " + result);
				return result;
				}
			}catch(ClientProtocolException e){
				e.printStackTrace();
		}
		catch(UnsupportedEncodingException e){
			e.printStackTrace();
		}
		catch (IOException e){
			e.printStackTrace();
		}
		System.out.println("result is: " + result);
		return result;
	}
}
