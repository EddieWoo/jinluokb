package com.pkusz.analyze;

import java.util.ArrayList;
import java.util.List;

import com.example.jinluokb.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

public class Symptoms extends Activity {
	   
    //声明组件  
    private CheckBox cb1,cb2,cb3,cb4;  
      
    //声明一个集合  
    private List<CheckBox> checkBoxs=new ArrayList<CheckBox>();  
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.symptoms);
		
		//获取组件  
        cb1 = (CheckBox) findViewById(R.id.checkBox1);  
        cb2 = (CheckBox) findViewById(R.id.checkBox2);  
        cb3 = (CheckBox) findViewById(R.id.checkBox3);  
        cb4 = (CheckBox) findViewById(R.id.checkBox4);  
          
        //默认选项  
//        cb2.setChecked(true);  
//        cb4.setChecked(true);  
          
        //注册事件  
        cb1.setOnCheckedChangeListener(listener);  
        cb2.setOnCheckedChangeListener(listener);  
        cb3.setOnCheckedChangeListener(listener);  
        cb4.setOnCheckedChangeListener(listener);  
        //把四个组件添加到集合中去  
        checkBoxs.add(cb1);  
        checkBoxs.add(cb2);  
        checkBoxs.add(cb3);  
        checkBoxs.add(cb4); 
	}
	CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {  
		  
        @Override  
        public void onCheckedChanged(CompoundButton buttonView,  
                boolean isChecked) {  
            // TODO Auto-generated method stub  
            CheckBox box = (CheckBox) buttonView;  
  
            Toast.makeText(getApplicationContext(),  
                    "获取的值:" + isChecked + "xxxxx" + box.getText(),  
                    Toast.LENGTH_LONG).show();  
  
        }  
    };  
    
    public void getValues(View v) {  
    	  
        String content = "";  
  
        for (CheckBox cbx : checkBoxs) {  
            if (cbx.isChecked()) {  
                content += cbx.getText() + "\n";  
            }  
        }  
  
        if ("".equals(content)) {  
            content = "您还没有选择尼";  
        }  
        new AlertDialog.Builder(this).setMessage(content).setTitle("选中的内容如下")  
                .setPositiveButton("关闭", null).show();  
  
    } 
}
