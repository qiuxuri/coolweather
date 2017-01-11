package com.example.coolweather.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.coolweather.db.CoolWeatherDB;
import com.example.coolweather.model.City;
import com.example.coolweather.model.Province;

import android.os.Message;
import android.util.Log;

public class HttpUtil {

	public static void sendHttpRequest(final String adress,final HttpCallbackListener listener){
		new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO 自动生成的方法存根
				HttpURLConnection connection=null;
				try {
					//HttpURLConnection connection=null;
					URL url=new URL(adress);
					connection=(HttpURLConnection) url.openConnection();
					connection.setRequestMethod("GET");
					connection.setReadTimeout(8000);
					connection.setConnectTimeout(8000);
					connection.setDoInput(true);
					InputStream in=connection.getInputStream();
					BufferedReader reader=new BufferedReader(new InputStreamReader(in));
					StringBuilder response=new StringBuilder();
					String line;
					while((line=reader.readLine())!=null){
						response.append(line);
					}
				
					listener.onFinish(response.toString());
			//	parserJSONwithJsonObject(response.toString());
					
			
				} catch (MalformedURLException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				} catch (IOException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					listener.onError(e);
				
				}
				finally{
					if(connection!=null){
						connection.disconnect();
					}
				}
				
				
			}
		}).start();

	
		
	}
	public interface HttpCallbackListener{
		void onFinish(String response);
		void onError(Exception e);
	}
	public static List<Province> loadJsondata(String data){
		List<Province> provinces=new ArrayList();
		String s=data.substring(50);
		
		JSONArray jsonArray;
		Province province=new Province();
		try {
			jsonArray = new JSONArray(s);
			for(int i=0;i<jsonArray.length();i++){

			//	provinces.clear();
				JSONObject json= jsonArray.getJSONObject(i);

				String code=json.getString("id");
				String provincename=json.getString("province");
			//	Log.d("MainActivity", ""+provincename);
				province=new Province();
				province.setProvinceName(provincename);
				province.setProvinceCode(code);
				provinces.add(province);
			}
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		//Log.d("MainActivity",""+provinces.size() );
		
	//	Log.d("MainActivity", "list is "+provinces.get(1).getProvinceName());
		return provinces;


		
	}
	public static List<City> loadJsondata1(String data,String province_name){
		List<City> citys=new ArrayList();
		String s=data.substring(50);
		boolean flag=false;
		//coolWeatherDB=CoolWeatherDB.getInstance(this);
		
		JSONArray jsonArray;
		Province province=new Province();
		City city;
		int codes=0;
		try {
			jsonArray = new JSONArray(s);
			for(int i=0;i<jsonArray.length();i++){

			//	provinces.clear();
				JSONObject json= jsonArray.getJSONObject(i);
				

				String code=json.getString("id");
				String provincename=json.getString("province");
				String cityname=json.getString("district");
				
				if(provincename.equals(province_name)){
					
					if(flag==false){
					codes=Integer.parseInt(code);
						flag=true;
					}
				
					city=new City();
					city.setProvinceId(codes);
					city.setCityName(cityname);
					city.setCityCode(code);
					
					citys.add(city);
					Log.d("MainActivity", city.toString());
				}
				
				//citys.add(city);
			}
		} catch (JSONException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		//Log.d("MainActivity",""+provinces.size() );
		
	//	Log.d("MainActivity", "list is "+provinces.get(1).getProvinceName());
		return citys;


		
	}
}

