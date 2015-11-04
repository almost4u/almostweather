package com.almostweather.app.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.almostweather.app.model.City;
import com.almostweather.app.model.County;
import com.almostweather.app.model.Province;

public class AlmostWeatherDB {
	/*
	 * ���ݿ���
	 * */
	public static final String DB_NAME = "almost_weather";
	/*
	 * ���ݿ�汾
	 */
	public static final int VERSION = 1;
	
	private static AlmostWeatherDB almostWeatherDB;
	
	private SQLiteDatabase db;
	//�����캯��˽�л�
	private AlmostWeatherDB(Context context){
		AlmostWeatherOpenHelper dbHelper = new AlmostWeatherOpenHelper(context,DB_NAME,null,VERSION);
		db = dbHelper.getWritableDatabase();
	}
	//��ȡalmostweatherDBʵ��
	public synchronized static AlmostWeatherDB getInstance(Context context){
		if(almostWeatherDB == null){
			almostWeatherDB = new AlmostWeatherDB(context);
		}
		return almostWeatherDB;
	}
	//��provinceʵ���洢�����ݿ�
	public void saveProvince(Province p){
		if(p!=null){
			ContentValues v = new ContentValues();
			v.put("province_name", p.getProvinceName());
			v.put("province_code", p.getProvinceCode());
			db.insert("Province", null, v);
		}
	}
	//�����ݿ��ȡȫ�����е�ʡ����Ϣ
	public List<Province> loadProvinces(){
		List<Province> list = new ArrayList<Province>();
		Cursor cursor = db.query("Province", null, null, null, null, null, null);
		if(cursor.moveToNext()){
			do{
				Province p = new Province();
				p.setId(cursor.getInt(cursor.getColumnIndex("id")));
				p.setProvinceName(cursor.getString(cursor.getColumnIndex("province_name")));
				p.setProvinceCode(cursor.getString(cursor.getColumnIndex("province_code")));
				list.add(p);
			}while(cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}
	//��cityʵ���洢�����ݿ�
	public void saveCity(City c){
		if(c!=null){
			ContentValues v = new ContentValues();
			v.put("city_name", c.getCityName());
			v.put("city_code", c.getCityCode());
			v.put("province_id", c.getProvinceId());
			db.insert("City", null, v);
		
		}
	}
	//�����ݿ��ȡʡ�����еĳ�����Ϣ
	public List<City> loadCities(int provinceId){
		List<City> list = new ArrayList<City>();
		Cursor cursor = db.query("City", null, "province_id = ?", new String[]{String.valueOf(provinceId)}, null, null, null);
		if(cursor.moveToNext()){
			do{
				City c = new City();
				c.setId(cursor.getInt(cursor.getColumnIndex("id")));
				c.setCityName(cursor.getString(cursor.getColumnIndex("city_name")));
				c.setCityCode(cursor.getString(cursor.getColumnIndex("city_code")));
				c.setProvinceId(provinceId);
				list.add(c);
			}while(cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}
	//��Countyʵ���洢�����ݿ�
	public void saveCounty(County c){
		if(c!=null){
			ContentValues v = new ContentValues();
			v.put("county_name", c.getCountyCode());
			v.put("county_code", c.getCountyCode());
			v.put("city_id", c.getCityId());
			db.insert("County", null, v);
			
		}
	}
	//�����ݿ��ȡĳ�����������ص���Ϣ
	public List<County> loadCounty(int cityId){
		List<County> list = new ArrayList<County>();
		Cursor cursor = db.query("County", null, "city_id = ?", new String[]{String.valueOf(cityId)}, null, null, null);
		if(cursor.moveToNext()){
			do{
				County c = new County();
				c.setCityId(cursor.getInt(cursor.getColumnIndex("id")));
				c.setCountyName(cursor.getString(cursor.getColumnIndex("county_name")));
				c.setCountyCode(cursor.getString(cursor.getColumnIndex("county_code")));
				c.setCityId(cityId);
			}while(cursor.moveToNext());
		}
		if(cursor!=null){
			cursor.close();
		}
		return list;
	}
}
