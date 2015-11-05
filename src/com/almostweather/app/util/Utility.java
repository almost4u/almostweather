package com.almostweather.app.util;

import android.text.TextUtils;

import com.almostweather.app.db.AlmostWeatherDB;
import com.almostweather.app.model.City;
import com.almostweather.app.model.County;
import com.almostweather.app.model.Province;

public class Utility {
	//�����ʹ������������ص�ʡ������
	public synchronized static boolean handleProvincesResponse(AlmostWeatherDB almostWeatherDB,String response){
		if(!TextUtils.isEmpty(response)){
			String[] allProvinces = response.split(",");
			if(allProvinces!=null && allProvinces.length>0){
				for(String p:allProvinces){
					String[] array = p.split("\\|");
					Province province = new Province();
					province.setProvinceCode(array[0]);
					province.setProvinceName(array[1]);
					almostWeatherDB.saveProvince(province);
				}
				return true;
			}
		}
		return false;
	}
	//�����ʹ������������ص��м�����
	public static boolean handleCityResponse(AlmostWeatherDB almostweatherDB,String response,int provinceId){
		if(!TextUtils.isEmpty(response)){
			String[] allCities = response.split(",");
			if(allCities!=null && allCities.length>0){
				for(String c:allCities){
					String[] array = c.split("\\|");
					City city = new City();
					city.setCityCode(array[0]);
					city.setCityName(array[1]);
					city.setProvinceId(provinceId);
					almostweatherDB.saveCity(city);
				}
				return true;
			}
		}
		return false;
	}
	//�����ʹ������������ص��ؼ�����
	public static boolean handleCountiesResponse(AlmostWeatherDB almostWeatherDB,String response,int cityId){
		if(!TextUtils.isEmpty(response)){
			String[] allCounties = response.split(",");
			if(allCounties!=null&&allCounties.length>0){
				for(String c:allCounties){
					String[] array = c.split("\\|");
					County county = new County();
					county.setCountyCode(array[0]);
					county.setCountyName(array[1]);
					county.setCityId(cityId);
					almostWeatherDB.saveCounty(county);
				}
				return true;
			}
		}
		return false;
	}
}