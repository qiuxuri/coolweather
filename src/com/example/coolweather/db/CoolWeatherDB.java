package com.example.coolweather.db;

import java.util.ArrayList;
import java.util.List;

import com.example.coolweather.model.City;
import com.example.coolweather.model.Province;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

public class CoolWeatherDB {
	public static final String DB_NAME="cool_weather";
	public static final int VERSION=1;
	private static CoolWeatherDB coolweatherDB;
	private SQLiteDatabase db;
	public CoolWeatherDB(Context context){
		CoolWeatherOpenHelper dbHelper=new CoolWeatherOpenHelper(context, DB_NAME, null, VERSION);
		db=dbHelper.getWritableDatabase();
	}
	public synchronized static CoolWeatherDB getInstance(Context context){
		if(coolweatherDB==null){
			coolweatherDB=new CoolWeatherDB(context);
		}
		return coolweatherDB;
	}
	public void saveProvince1(List<Province> province){
		if(province.size()>0){
			for(int i=0;i<province.size()-1;i++){
				
				if(queryifhaveProvinces(province.get(i).getProvinceName())){
					saveProvince(province.get(i));
			
				}

			}
		}
	}
	public void saveCity1(List<City> city){
		if(city.size()>0){
			for(int i=0;i<city.size()-1;i++){
				//Log.d("MainActivity", "123");
				if(queryifhaveCitys(city.get(i).getCityName())){
				
					saveCity(city.get(i));
					//Log.d("MainActivity", city.get(i).toString());
				}

			}
		}
	}
	public void saveProvince(Province province){
		if(province !=null){
			ContentValues values=new ContentValues();
			values.put("province_name", province.getProvinceName());
			values.put("province_code", province.getProvinceCode());
			db.insert("Province", null, values);
		}
	}
	public void saveCity(City city){
		if(city !=null){
			ContentValues values=new ContentValues();
			values.put("city_name", city.getCityName());
			values.put("city_code", city.getCityCode());
			values.put("province_id", city.getProvinceId());
			db.insert("City", null, values);
		}
	}
	public boolean  queryifhaveProvinces(String province){
		//Cursor cursor=db.query("Province", null, null, null, null, null, null);
		Cursor cursor=db.query("Province", null, "province_name=?", new String[]{province}, null, null, null);
		
	if(cursor.getCount()==0){
			cursor.close();
			return true;//没有数据，可以添加
	}
		else 
		{
			cursor.close();
			return false;//有数据了，不能添加
		}


	}
	public boolean queryifhaveCitys(String city){
		Cursor cursor=db.query("City", null, "city_name=?", new String[]{city}, null, null, null);
		if(cursor.getCount()==0){
			cursor.close();
			return true;
		}
		else {
			cursor.close();
			return false;
		}
	}
	public boolean  queryifhaveCity(String city){
		//Cursor cursor=db.query("Province", null, null, null, null, null, null);
		Cursor cursor=db.query("City", null, "city_name=?", new String[]{city}, null, null, null);
		
	if(cursor.getCount()==0){
			cursor.close();
			return true;//没有数据，可以添加
	}
		else 
		{
			cursor.close();
			return false;//有数据了，不能添加
		}


	}
/*	public boolean  queryifhavecitys(String citys){
		//Cursor cursor=db.query("Province", null, null, null, null, null, null);
		Cursor cursor=db.query("Province", null, "province_name=?", new String[]{province}, null, null, null);
		
	if(cursor.getCount()==0){
			cursor.close();
			return true;//没有数据，可以添加
	}
		else 
		{
			cursor.close();
			return false;//有数据了，不能添加
		}


	}
	*/
	public List<Province> queryprovince(){
		
		Cursor cursor=db.query("Province", null, null, null, null, null, null);
		List<Province> provinces=new ArrayList();
		if(cursor.moveToFirst()){
			do{
				Province p=new Province();
				p.setProvinceName(cursor.getString(cursor.
						getColumnIndex("province_name")));
				p.setProvinceCode(cursor.getString(cursor.
						getColumnIndex("province_code")));
				String province_id=cursor.getString(cursor.
						getColumnIndex("id"));
				provinces.add(p);
				
			//	Log.d("MainActivity", p.tostring());
			}while (cursor.moveToNext());
		}
		cursor.close();
		return  provinces;
	}
	public List<City> querycity(String provincename){
		String id="";
		Cursor cursor1=db.query("Province", null, "province_name=?", new String[]{provincename}, null, null, null);
		if(cursor1.moveToFirst()){
			do{
				
				 id=cursor1.getString(cursor1.getColumnIndex("province_code"));
				 Log.d("MainActivity", "id:"+id);
			}while (cursor1.moveToNext());
		}
		cursor1.close();
		Cursor cursor=db.query("City", null, "province_id=?", new String[]{id}, null, null, null);
		List<City> citys=new ArrayList();
		if(cursor.moveToFirst()){
			do{
				City city=new City();
				city.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				city.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				city.setProvinceId(cursor.getInt(cursor.getColumnIndex("province_id")));
				citys.add(city);
				
			}while(cursor.moveToNext());
		}
		cursor.close();
		return citys;
	}
	public void InsertProvience(Province province,String str_province){
		if(queryifhaveProvinces(str_province)){
			saveProvince(province);
		}	
	}
}
