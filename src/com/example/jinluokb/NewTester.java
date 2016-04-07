package com.example.jinluokb;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.pkusz.constants.DBInfoConfig;
import com.pkusz.dao.DBOpenHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class NewTester extends Activity {

	private Button sure;
	private Button categoriesB;
	private EditText name,sex,age,marriage,tel;
	String names=" ",sexs=" ",ages=" ",marriages=" ",tels=" ",categorystr;
	
	RadioGroup      marriaged,male;  
    RadioButton     marriedbutton, singlebutton, malebutton, femalebutton;  
    
	String data;
	SQLiteDatabase database=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.newtester);
		
		sure=(Button)findViewById(R.id.sure1);
		categoriesB=(Button)findViewById(R.id.category);
		name=(EditText)findViewById(R.id.name);
//		sex=(EditText)findViewById(R.id.sex);
		age=(EditText)findViewById(R.id.age);
//		marriage=(EditText)findViewById(R.id.marriage);
		tel=(EditText)findViewById(R.id.tel);
		
		 marriaged = (RadioGroup) findViewById(R.id.RadioGroup02);  
		 male=(RadioGroup) findViewById(R.id.RadioGroup01);  
		 marriedbutton=(RadioButton)findViewById(R.id.married);
         singlebutton=(RadioButton)findViewById(R.id.single);
         malebutton=(RadioButton)findViewById(R.id.male);
         femalebutton=(RadioButton)findViewById(R.id.female);
		
         marriaged.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {  
             @Override  
             public void onCheckedChanged(RadioGroup group, int checkedId)  
             {  
                 // TODO Auto-generated method stub  
                 if (checkedId == marriedbutton.getId())  
                 {  //已婚
                    marriages="已婚";  
                 }  
                 else  
                 {  
                	 marriages="未婚";  
                 }  
             }  
         }); 
		
         male.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {  
             @Override  
             public void onCheckedChanged(RadioGroup group, int checkedId)  
             {  
                 // TODO Auto-generated method stub  
                 if (checkedId == malebutton.getId())  
                 {  //性别 男
                    sexs="男";  
                 }  
                 else  
                 {  
                	 sexs="女";  
                 }  
             }  
         });
         
		View.OnClickListener mylistener = new View.OnClickListener() {  
	            
	            @Override  
	            public void onClick(View v) {  
	                switch (v.getId()) {  
	                case R.id.sure1:  
	                	
	                	names=name.getText().toString();	       
//	                	sexs=sex.getText().toString();
	                	ages=age.getText().toString();
//	                	marriages=marriage.getText().toString();
	                	tels=tel.getText().toString();
	                	categorystr=categoriesB.getText().toString();
	                	
	                	 Date d = new Date();  
	                     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
	                     String dateNowStr = sdf.format(d);
	                     
	                	
	                	
	                	if(categorystr.equalsIgnoreCase("请选择类别") ){
	                		Toast.makeText(getApplicationContext(), "请选择类别！", 2000).show();	   
	                	}
	                	if(names.equals(" ")|ages.equals(" ")|tels.equals(" ")|sexs.equals(" ")|marriages.equals(" ")){
	                		Toast.makeText(getApplicationContext(), "请将信息输入完整！", 2000).show();	  
	                	}else{
	                	 try{
	                	  
	                    DBOpenHelper helper=new DBOpenHelper(NewTester.this,DBInfoConfig.DB_NAME);	
	                    database=helper.getReadableDatabase();	                    
	                    Object[] params = {names,ages,sexs,marriages,tels,categorystr,dateNowStr};
	                    
	                       if(categorystr.equalsIgnoreCase("朋友")){
	                    	   
	                         database.execSQL(DBInfoConfig.INSERT_INTO_FRIEND, params);
	                         
	                       }
	                     else if(categorystr.equalsIgnoreCase("同事")){
	                    	 database.execSQL(DBInfoConfig.INSERT_INTO_COLLEAGUE, params);
	                         
	                       }
	                     else if(categorystr.equalsIgnoreCase("客户")){
	                    	 
	                    	 database.execSQL(DBInfoConfig.INSERT_INTO_CLIENT, params);
	                    	    
//	                    	 database.execSQL(DBInfoConfig.INSERT_INTO_CLIENT_DATE, dateparams);
	                    	
	                    	 
	                    	 }                     
	                       
	                     else  {
	                    	 database.execSQL(DBInfoConfig.INSERT_INTO_FAMILY, params);
	                       
	                     }
	                  }
	              	  catch (Exception e) {  
	              	             e.printStackTrace();  
	              	         } finally {  
	              		             if(database != null) {  
	              		                 database.close();  
	              		             }  
	              		         }  

	                    
	                    Intent intent=new Intent();
	                    Bundle bundle=new Bundle();
	                    bundle.putString("category", categorystr);
	                    bundle.putString("date",dateNowStr);
	                    bundle.putString("patientsex",sexs);
	                    System.out.println("存入的日期是        "+dateNowStr);
	                    intent.putExtras(bundle);
	                    
	                    intent.setClass(NewTester.this, NewTest.class);
                    	startActivity(intent);
                    	}
	                	
	                    break;  
	                case R.id.category:
					    AlertDialog.Builder builder = new AlertDialog.Builder(NewTester.this);
		                builder.setIcon(R.drawable.ic_launcher);
		                builder.setTitle("请选择一个类别");
		                //    指定下拉列表的显示数据
		                final String[] categories = {"同事", "朋友", "家人", "客户"};
		                //    设置一个下拉的列表选择项
		                builder.setItems(categories, new DialogInterface.OnClickListener()
		                {
		                    @Override
		                    public void onClick(DialogInterface dialog, int which)
		                    {		                       
		                    	categoriesB.setText(categories[which]);
		                    }
		                });
		                builder.show();
	                default:  
	                    break;  
	                }     
	            }  
	        };  
	        sure.setOnClickListener(mylistener); 
	        categoriesB.setOnClickListener(mylistener); 
	}

}
