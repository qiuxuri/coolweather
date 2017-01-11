package com.example.coolweather.db;

import com.example.coolweather.activity.ChooseAreaActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class CoolWeatherOpenHelper extends SQLiteOpenHelper{
	private Context mcontext;
	public static final String CREATE_PROVINCE="create table Province("+
            "id integer primary key autoincrement, "
			+"province_name text, "
            +"province_code text)";
	public static final String CREATR_CITY="create table City("+
            "id integer primary key autoincrement, "
			+"city_name text, "
            +"city_code text, "
			+"province_id integer)";
	public static final String CREATE_COUNTY="create table County("+
			"id integer primary key autoincrement, "+
			"county_name text, "+
			"county_code text, "+
			"city_id integer)";
	

	public CoolWeatherOpenHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO �Զ����ɵĹ��캯�����
		mcontext=context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO �Զ����ɵķ������
		db.execSQL(CREATE_PROVINCE);
		db.execSQL(CREATE_COUNTY);
		db.execSQL(CREATR_CITY);
		Toast.makeText(mcontext, "sucess", 1000).show();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO �Զ����ɵķ������
		
	}

}
