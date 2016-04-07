package com.pkusz.homecenter;



import com.example.jinluokb.R;
import com.pkusz.circleimage.CircleImageView;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


public class HomeCenter extends Activity {

	private Button modify;
	
	private static String path="/sdcard/myHead/";//sd·��
	private CircleImageView ivHead;
	private TextView name1,sex1,age1,location1;
	
	SharedPreferences preferences;
	Editor editor;
 	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.homecenter);
		name1=(TextView) findViewById(R.id.name);
		age1=(TextView) findViewById(R.id.age);
		sex1=(TextView) findViewById(R.id.sex);
		location1=(TextView) findViewById(R.id.location);
		ivHead=(CircleImageView) findViewById(R.id.homepic);
		initView();
		modify=(Button) findViewById(R.id.mofify);
		
		
		preferences=getSharedPreferences("userinfo",Context.MODE_PRIVATE);
		
	    modify.setOnClickListener(new Button.OnClickListener(){//��������    
            public void onClick(View v) {    
                 Intent intent=new Intent(HomeCenter.this,ModifyStatus.class);
                 startActivity(intent);
            }    
  
        });    
	    
	    String name=preferences.getString("name", "����ǽ");
		String sex=preferences.getString("sex", "��");
		String age=preferences.getString("age", "24");
		String location=preferences.getString("location", "����");
		name1.setText(name);
		age1.setText(age);
		sex1.setText(sex);
		location1.setText(location);
	    
	}
	private void initView() {
		// TODO Auto-generated method stub
		Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");//��Sd����ͷ��ת����Bitmap
		if(bt!=null){
			@SuppressWarnings("deprecation")
			Drawable drawable = new BitmapDrawable(bt);//ת����drawable
			ivHead.setImageDrawable(drawable);
		}else{
			/**
			 *	���SD����û������Ҫ�ӷ�����ȡͷ��ȡ������ͷ���ٱ�����SDد
			 * 
			 */
		}
		
	}
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		String name=preferences.getString("name", "����ǽ");
		String sex=preferences.getString("sex", "��");
		String age=preferences.getString("age", "24");
		String location=preferences.getString("location", "����");
		name1.setText(name);
		age1.setText(age);
		sex1.setText(sex);
		location1.setText(location);
	}

	
	
}
