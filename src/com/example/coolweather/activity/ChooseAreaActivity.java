package com.example.coolweather.activity;


import java.util.ArrayList;
import java.util.List;

import com.example.coolweather.R;
import com.example.coolweather.db.CoolWeatherDB;
import com.example.coolweather.model.City;
import com.example.coolweather.model.Province;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.HttpUtil.HttpCallbackListener;

import android.R.anim;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ChooseAreaActivity extends Activity{
	Button b1;
	private ProgressDialog progressDialog;
	ListView lv;
	TextView tv;
	List<Province> provinceList=new ArrayList();
	List<City> cityList=new ArrayList();
	List<String> dataList=new ArrayList();
	private ArrayAdapter<String> adapter;
	private CoolWeatherDB coolWeatherDB;
	final int LOAD=2;
	final int NOLOAD=1;
	private int LEVEL=0;
	private static final String adress="http://v.juhe.cn/weather/citys?key=6ec85ced632f37411a1ec72fee95b6f3";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.choose_area);
		Log.d("MainActivity", "1");
		coolWeatherDB=coolWeatherDB.getInstance(this);
		adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, dataList);
		//coolWeatherDB.queryProvinces("北京");
		tv=(TextView) findViewById(R.id.textView1);
		lv=(ListView) findViewById(R.id.listView1);
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, final int arg2,
					long arg3) {
				// TODO 自动生成的方法存根
				tv.setText(dataList.get(arg2));
				if(LEVEL==LOAD){
					showProggressDialoh();
	
				  querycitys1(arg2);
				}
				else{
					Intent  intent =new Intent (ChooseAreaActivity.this,WeatherActivity.class);
					intent.putExtra("cityname", dataList.get(arg2));
					startActivity(intent);
					finish();
				}
				
			}
		});
		queryProvinces();
	}	
	private void queryProvinces(){
		tv.setText("中国");
		LEVEL=LOAD;
		provinceList=coolWeatherDB.queryprovince();
		if(provinceList.size()>0){
			dataList.clear();
			for(Province province:provinceList){
			 dataList.add(province.getProvinceName());	
			 Log.d("MainActivity", province.getProvinceName());
			}
			adapter.notifyDataSetChanged();
			lv.setSelection(0);
			
		}
		else{
			queryFromServices();
		}
	}
	
	private void querycitys(){
		
		//cityList=coolWeatherDB.querycity();
		if(cityList.size()>0){
			dataList.clear();
			for(City city:cityList){
				dataList.add(city.getCityName());			
			}
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO 自动生成的方法存根
					closeProgressDialog();
					adapter.notifyDataSetChanged();
					lv.setSelection(0);
				}
			});
			LEVEL=NOLOAD;
			Log.d("MainActivity", ""+cityList.size());
		}
	}
	private void querycitys1(int arg2){
		//LEVEL=NOLOAD;
			if((cityList=coolWeatherDB.querycity(dataList.get(arg2))).size()>0)
		{
			
			dataList.clear();
			for(City city:cityList){
				dataList.add(city.getCityName());			
			}
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					// TODO 自动生成的方法存根
					closeProgressDialog();
					adapter.notifyDataSetChanged();
					lv.setSelection(0);
				}
			});
			LEVEL=NOLOAD;
		}
		else{
			if(LEVEL==LOAD)
			queryfromServiceCity(arg2);

		}
	}
	private void queryFromServices(){
			HttpUtil.sendHttpRequest(adress, new HttpCallbackListener() {
				
				@Override
				public void onFinish(String response) {
					// TODO 自动生成的方法存根
					provinceList=HttpUtil.loadJsondata(response);
					coolWeatherDB.saveProvince1(provinceList);
					provinceList=coolWeatherDB.queryprovince();
					if(provinceList.size()>0){
						dataList.clear();
						for(Province province:provinceList){
						 dataList.add(province.getProvinceName());	
						// Log.d("MainActivity", province.getProvinceName());
						}	
						runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								// TODO 自动生成的方法存根
								
								adapter.notifyDataSetChanged();
								lv.setSelection(0);
							}
						});
					}
					
				}
				
				@Override
				public void onError(Exception e) {
					// TODO 自动生成的方法存根
			
				}
			});

		
		
	}
	private void queryfromServiceCity(final int arg2){
	
		HttpUtil.sendHttpRequest(adress, new HttpCallbackListener() {
			
			@Override
			public void onFinish(String response) {
				// TODO 自动生成的方法存根
					cityList=HttpUtil.loadJsondata1(response,dataList.get(arg2));
					Log.d("MainActivity", ""+cityList.size());
				    coolWeatherDB.saveCity1(cityList);
				    cityList=coolWeatherDB.querycity(dataList.get(arg2));
				    querycitys();
				
	
			}
			
			@Override
			public void onError(Exception e) {
				// TODO 自动生成的方法存根
				
			}
		});
		
	}
	
	private void showProggressDialoh(){
		if(progressDialog==null){
			progressDialog=new ProgressDialog(this);
			progressDialog.setMessage("正在加载...");
			progressDialog.setCanceledOnTouchOutside(false);
		}
		progressDialog.show();
	}
	private void closeProgressDialog(){
		if(progressDialog!=null){
			progressDialog.dismiss();
		}
	}

}
