package com.example.jinluokb;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class Agreement extends Activity {
	
   TextView yifang;
   TextView xieyi;
   private SharedPreferences shared;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.agreement);
		yifang=(TextView)findViewById(R.id.yifang);
		xieyi=(TextView)findViewById(R.id.xieyi);
		xieyi.setMovementMethod(ScrollingMovementMethod.getInstance());
		shared=getSharedPreferences("userinfo",0);
		String names=shared.getString("name", " ");
		String sex=shared.getString("sex", " ");
		String age=shared.getString("age", "  ");
		String marriage=shared.getString("bornlocation", "  ");//birthlocation改为是否结婚
		String tel=shared.getString("theid", "  ");//身份证改为电话号码
		yifang.setText("乙方姓名："+ names + "\n性别："+sex+"\n年龄:"+age+" \n婚姻:"+marriage+"\n手机号码："+tel+"\n");
	
	}

}
