package com.pkusz.homecenter;

import com.example.jinluokb.R;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
//字段分别是name、sex、age、location、bornlocation、birthday、theid
public class ModifyStatus extends Activity {
 
	String name,sex,age,location,bornlocation,birthday,theid;
	private EditText editText1,editText2,editText3,editText4,editText5,editText6,editText7;
	
	SharedPreferences preferences=null;
	SharedPreferences.Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.modifystatus);
		editText1 =(EditText)findViewById(R.id.name);       
        editText2 =(EditText)findViewById(R.id.sex);          
        editText3 =(EditText)findViewById(R.id.age);     
        editText4 =(EditText)findViewById(R.id.location); 
        editText5 =(EditText)findViewById(R.id.bornlocation);  
        editText6 =(EditText)findViewById(R.id.birthday);  
        editText7 =(EditText)findViewById(R.id.theid);  
        
        preferences=getSharedPreferences("userinfo",0);
        editor= preferences.edit();
        
        editText1.setText(preferences.getString("name", " "));
        editText2.setText(preferences.getString("sex", " "));
        editText3.setText(preferences.getString("age", "  "));
        editText4.setText(preferences.getString("laocation", " "));
        editText5.setText(preferences.getString("bornlocation", " "));
        editText6.setText(preferences.getString("birthday", " "));
        editText7.setText(preferences.getString("theid", " "));
        
    	Button sure=(Button) findViewById(R.id.sure);
       
    	
    	
        View.OnClickListener mylistener = new View.OnClickListener() {  
            
            @Override  
            public void onClick(View v) {  
                switch (v.getId()) {  
                case R.id.sure:  
                     //保存数据，上传服务器
                	name=editText1.getText().toString();
                	sex=editText2.getText().toString();
                	age=editText3.getText().toString();
                	location=editText4.getText().toString();
                	bornlocation=editText5.getText().toString();
                	birthday=editText6.getText().toString();
                	theid=editText7.getText().toString();
                	
                	
                	editor.putString("name", name);
                    editor.putString("age", age);
                    editor.putString("sex", sex);
                 	editor.putString("location", location);
                    editor.putString("birthday", birthday); 
                    editor.putString("bornlocation", bornlocation);
                    editor.putString("theid", theid);  
                	editor.commit();
                	Toast.makeText(getApplicationContext(), "已经保存成功",Toast.LENGTH_SHORT).show();
                    break;  
                default:  
                    break;  
                }     
            }  
        };  
        sure.setOnClickListener(mylistener);  
	}
}
