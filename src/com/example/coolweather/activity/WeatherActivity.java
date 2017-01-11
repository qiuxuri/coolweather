package com.example.coolweather.activity;

import java.net.URLEncoder;

import org.json.JSONException;
import org.json.JSONObject;

import com.example.coolweather.R;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.HttpUtil.HttpCallbackListener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


public class WeatherActivity extends Activity{
	
	private TextView city_text,date_text,weather_text,teamp_text;
	String s="";
	String m="";
	String k="";
	String date="";
	Button b1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weather_layout);
		String s1=getIntent().getStringExtra("cityname");
	//	if()
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);

		city_text=(TextView) findViewById(R.id.textView1);
		date_text=(TextView) findViewById(R.id.textView3);
		weather_text=(TextView) findViewById(R.id.textView4);
	    teamp_text=(TextView) findViewById(R.id.textView6);
	    b1=(Button) findViewById(R.id.button1);
	    b1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO 自动生成的方法存根
				Intent intent=new Intent(WeatherActivity.this,ChooseAreaActivity.class);
				startActivity(intent);
				finish();
			}
		});
		//if()
		if(s1.equals(prefs.getString("city", ""))){
			showWeather();
			Log.d("WeatherActivity","234243");
		//	Log.d("WeatherActivity", prefs.getString("city", "77"));
		}
		else{
			
		
			Log.d("WeatherActivity","asdsad");
		HttpUtil.sendHttpRequest(encodeAdress(s1), new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO 自动生成的方法存根
			//	Log.d("WeatherActivity", response);
				try {
					JSONObject json=new JSONObject(response);
				//	Log.d("WeatherActivity", json.toString());
					JSONObject json1=json.getJSONObject("result");
					JSONObject json2=json1.getJSONObject("today");
					//Log.d("WeatherActivity", json2.toString());
				 s=json2.getString("city");
					 m=json2.getString("weather");
				 k=json2.getString("temperature");
					 date=json2.getString("date_y");
					SharedPreferences.Editor editor=PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
					editor.putString("city", s);
					editor.putString("weather", m);
					editor.putString("date_y", date);
					editor.putString("temperature", k);
					editor.commit();
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO 自动生成的方法存根
			/*				city_text.setText(s);
							weather_text.setText(m);
							teamp_text.setText(k);
							date_text.setText(date);
						*/
							showWeather();
						}
					});
			//		showWeather();
				/*	city_text.setText(s);
					weather_text.setText(m);
					teamp_text.setText(k);
					date_text.setText(date);*/
					//Log.d("WeatherActivity", json1.toString());
				} catch (JSONException e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
				}
			}
			
			@Override
			public void onError(Exception e) {
				// TODO 自动生成的方法存根
				
			}
		});}
	}
	@Override
	public void onBackPressed() {
		// TODO 自动生成的方法存根
		super.onBackPressed();
		
	}
	private String encodeAdress(String cityname){
		
		String code=URLEncoder.encode(cityname);
		String adress="http://v.juhe.cn/weather/index?format=2&cityname="+code+"&key=6ec85ced632f37411a1ec72fee95b6f3";
		return adress;
		
	}
	private void showWeather(){
		SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences(this);
		city_text.setText(prefs.getString("city", ""));
		weather_text.setText(prefs.getString("weather", ""));
		teamp_text.setText(prefs.getString("temperature", ""));
		date_text.setText(prefs.getString("date_y", ""));
	
	}

}


