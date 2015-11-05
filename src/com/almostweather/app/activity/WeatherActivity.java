package com.almostweather.app.activity;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.almostweather.app.R;
import com.almostweather.app.util.HttpCallbackListener;
import com.almostweather.app.util.HttpUtil;
import com.almostweather.app.util.Utility;

public class WeatherActivity extends Activity {
	private LinearLayout weatherInfoLayout ;
	private TextView cityNameText;   //显示城市名
	private TextView publishText;    //发布时间
	private TextView weatherDespText;  //天气描述信息
	private TextView temp1Text;        //显示最低温度
	private TextView temp2Text;        //显示最高温度
	private TextView currentDateText;  //显示当前日期
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.weather_layout);
		//初始化各控件
		weatherInfoLayout = (LinearLayout)findViewById(R.id.weather_info_layout);
		publishText = (TextView)findViewById(R.id.publish_text);
		cityNameText = (TextView)findViewById(R.id.city_name);
		weatherDespText = (TextView)findViewById(R.id.weather_desp);
		temp1Text = (TextView)findViewById(R.id.temp1);
		temp2Text = (TextView)findViewById(R.id.temp2);
		currentDateText = (TextView)findViewById(R.id.current_date);
		String countyCode = getIntent().getStringExtra("county_code");
		if(!TextUtils.isEmpty(countyCode)){  //判断是否有县级代号，有就查询天气
			publishText.setText("同步中...");
			weatherInfoLayout.setVisibility(View.INVISIBLE);
			cityNameText.setVisibility(View.INVISIBLE);
			queryWeatherCode(countyCode);
		}else{
			showWeather(); //没有县级代号直接显示本地信息
		}
		
	}
	//查询县级代号所对应的天气代号
	private void queryWeatherCode(String countyCode){
		String address = "http://www.weather.com.cn/data/list3/city"+countyCode+".xml";
		queryFromServer(address,"countyCode");
	}
	//查询天气代号所对应的天气
	private void queryWeatherInfo(String weatherCode){
		String address = "http://www.weather.com.cn/data/cityinfo/"+weatherCode+".html";
		queryFromServer(address,"weatherCode");
	}
	//根据传入的地址和类型去向服务器查询天气代号或者天气信息
	private void queryFromServer(final String address,final String type){
		HttpUtil.sendHttpRequest(address, new HttpCallbackListener(){

			@Override
			public void onFinish(String response) {
				// TODO Auto-generated method stub
				if("countyCode".equals(type)){
					if(!TextUtils.isEmpty(response)){
						//从服务器返回的数据中解析出天气的代号
						String[] array = response.split("\\|");
						if(array!=null&&array.length==2){
							String weatherCode = array[1];
							queryWeatherInfo(weatherCode);
						}
					}
				}else if("weatherCode".equals(type)){
					//处理数据库返回的天气信息
					Utility.handleWeatherResponse(WeatherActivity.this, response);
					runOnUiThread(new Runnable(){

						@Override
						public void run() {
							// TODO Auto-generated method stub
							showWeather();
						}
					});
				}
			}

			@Override
			public void onError(Exception e) {
				// TODO Auto-generated method stub
				runOnUiThread(new Runnable(){

					@Override
					public void run() {
						// TODO Auto-generated method stub
						publishText.setText("同步失败");
					}
					
				});
			}
			
		});
	}
	//从sharedPreferences文件中读取存储天气的信息，并显示在界面上
	private void showWeather(){
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
		cityNameText.setText(prefs.getString("city_name", ""));
		temp1Text.setText(prefs.getString("temp1", ""));
		temp2Text.setText(prefs.getString("temp2", ""));
		weatherDespText.setText(prefs.getString("weather_desp", ""));
		publishText.setText("今天"+prefs.getString("publish_time", "")+"发布");
		currentDateText.setText(prefs.getString("current_date", ""));
		weatherInfoLayout.setVisibility(View.VISIBLE);
		cityNameText.setVisibility(View.VISIBLE);
		
	}
}
