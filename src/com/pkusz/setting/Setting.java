package com.pkusz.setting;


import java.io.File;

import com.example.jinluokb.NewTest;
import com.example.jinluokb.R;
import com.pkusz.analyze.Solution;
import com.pkusz.dao.OperateDAO;
import com.pkusz.util.*;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

public class Setting extends Activity implements CompoundButton.OnCheckedChangeListener {

	private Switch slidebutton;
	private Switch notification;
	private Switch sound;
	private RelativeLayout cleanarchive;
	private RelativeLayout cleancache;
	private RelativeLayout about,cleanhash;
	private OperateDAO odao;
	SharedPreferences preferences=null;
	SharedPreferences.Editor editor;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting);
		slidebutton=(Switch)findViewById(R.id.bluetooth);
		notification=(Switch)findViewById(R.id.notifycation);
		sound=(Switch)findViewById(R.id.sound);
		notification.setOnCheckedChangeListener(this);
		sound.setOnCheckedChangeListener(this);
		slidebutton.setOnCheckedChangeListener(this);
		preferences=getSharedPreferences("userinfo",0);
		
		boolean is=preferences.getBoolean("notify", true);
		notification.setChecked(is);
		boolean is1=preferences.getBoolean("sound", true);
		sound.setChecked(is1);


		cleanarchive=(RelativeLayout)findViewById(R.id.cleanarchive);
		cleancache=(RelativeLayout)findViewById(R.id.clearcache);
		about=(RelativeLayout)findViewById(R.id.about);
		 cleanhash=(RelativeLayout)findViewById(R.id.cleanhash);
		 odao = new OperateDAO(Setting.this);
		
		cleanarchive.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				odao.cleandata();
				Toast.makeText(getApplicationContext(), "删除数据成功", 3000).show();
			}
		});
		
		cleancache.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				File file = new File(Environment.getExternalStorageDirectory()+"/jinluo/");  
	            DeleteFile(file); 
			}
		});
		cleanhash.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Solution.hash.clear();
				Toast.makeText(getApplicationContext(), "清除检测项目", 3000).show();
			}
		});
        about.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(Setting.this,About.class);
				startActivity(intent);
				
			}
		});
	}
	
	public void DeleteFile(File file) {  
        if (file.exists() == false) {  
        	Toast.makeText(getApplicationContext(), "文件不存在", Toast.LENGTH_SHORT).show();
            return;  
        } else {  
            if (file.isFile()) {  
                file.delete();  
                return;  
            }  
            if (file.isDirectory()) {  
                File[] childFile = file.listFiles();  
                if (childFile == null || childFile.length == 0) {  
//                    file.delete();  
                    return;  
                }  
                for (File f : childFile) {  
                    DeleteFile(f);  
                }  
//                file.delete();  
            }  
            Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT).show();
        }  
    }  

	@Override
	    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
	        switch (compoundButton.getId()){
	            case R.id.bluetooth:
	                if(compoundButton.isChecked()) {
	                	Intent intent=new Intent(Setting.this,NewTest.class);
					startActivity(intent);}
	                break;
	            case R.id.notifycation:
	                if(compoundButton.isChecked()) {
	                	preferences=getSharedPreferences("userinfo",0);
	                    editor= preferences.edit();
				   	    editor.putBoolean("notify", true);
				  	    editor.commit();
	                }else{
	                	preferences=getSharedPreferences("userinfo",0);
	                    editor= preferences.edit();
	                	editor.putBoolean("notify", false);
				  	    editor.commit();
	                }
	                break;
	            case R.id.sound:
	                if(compoundButton.isChecked()) {
	                	preferences=getSharedPreferences("userinfo",0);
	                    editor= preferences.edit();
				   	    editor.putBoolean("sound", true);
				  	    editor.commit();
	                }else{
	                	preferences=getSharedPreferences("userinfo",0);
	                    editor= preferences.edit();
	                	editor.putBoolean("sound", false);
				  	    editor.commit();
	                }
	                break;

	        }
	    }

}
