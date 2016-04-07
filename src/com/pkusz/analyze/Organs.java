package com.pkusz.analyze;

import com.example.jinluokb.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
//被弃用的activity
public class Organs extends Activity {

	private TextView tv121,tv122,tv123,tv131,tv132,tv133,tv221,tv222,tv223,tv231,tv232,tv233;
	private TextView tv321,tv322,tv323,tv331,tv332,tv333,tv421,tv422,tv423,tv431,tv432,tv433;
	
	private Button nextbut;
	String modernevaluation,solution;
	
        	public String YinXu = "阴虚";
        	public String YinBuZu = "阴不足";
            public String ZhengChang = "正常";
            public String YangBuZu = "阳不足";
            public String YangXu = "阳虚";

            public String Sheng = "盛";
            public String Kang = "亢";

            public String YKYX = "阳亢阴虚";
            public String YSYS = "阴亢阳虚";
            public String YYLX = "亏";
            public String QXLX = "气血两虚";

            public String RERAO = "热扰";
            public String RE = "热";
            public String REZAO = "热燥";
            public String REJIE = "热结";

            public String HANRAO = "寒扰";
            public String HAN = "寒";
            public String HANNING = "寒凝";
            public String HANJIE = "寒结";

            public String XUEBUZU = "血不足";
            public String XUEXU = "血虚";
            public String QIBUZU = "气不足";
            public String QIXU = "气虚";
            public String YINKANG = "阴亢";
            public String YINSHENG = "阴盛";
            public String YANGKANG = "阳亢";
            public String YANGSHENG = "阳盛";
            
            String[] list1=new String[12];
            String[] list2=new String[12];
            
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.organs);
		
		Bundle myBundle=this.getIntent().getExtras(); 
		String[] result=myBundle.getStringArray("data"); 
		modernevaluation=myBundle.getString("modern");
		solution=myBundle.getString("solution");
		
		tv121=(TextView)findViewById(R.id.tv121);
		tv122=(TextView)findViewById(R.id.tv122);
		tv123=(TextView)findViewById(R.id.tv123);
		tv131=(TextView)findViewById(R.id.tv131);
		tv132=(TextView)findViewById(R.id.tv132);
		tv133=(TextView)findViewById(R.id.tv133);
		tv221=(TextView)findViewById(R.id.tv221);
		tv222=(TextView)findViewById(R.id.tv222);
		tv223=(TextView)findViewById(R.id.tv223);
        tv231=(TextView)findViewById(R.id.tv231);
        tv232=(TextView)findViewById(R.id.tv232);
        tv233=(TextView)findViewById(R.id.tv233);
		tv321=(TextView)findViewById(R.id.tv321);
		tv322=(TextView)findViewById(R.id.tv322);
		tv323=(TextView)findViewById(R.id.tv323);
		tv331=(TextView)findViewById(R.id.tv331);
		tv332=(TextView)findViewById(R.id.tv332);
		tv333=(TextView)findViewById(R.id.tv333);
		tv421=(TextView)findViewById(R.id.tv421);
		tv422=(TextView)findViewById(R.id.tv422);
		tv423=(TextView)findViewById(R.id.tv423);
		tv431=(TextView)findViewById(R.id.tv431);
		tv432=(TextView)findViewById(R.id.tv432);
		tv433=(TextView)findViewById(R.id.tv433);
		
		nextbut=(Button)findViewById(R.id.modern);
		
	      for(int i=0;i<12;i++){
	    	  check(result[i],i);
	    	  System.out.println(result[i]);
	      }
		
//心
		tv121.setText(list1[2]);
		tv131.setText(list2[2]);
//肺
		tv122.setText(list1[0]);
		tv132.setText(list2[0]);
//肝
		tv123.setText(list1[7]);
		tv133.setText(list2[7]);
//脾
		tv221.setText(list1[6]);
		tv231.setText(list2[6]);
//肾	
		tv222.setText(list1[8]);
		tv232.setText(list2[8]);
//胆	
		tv223.setText(list1[10]);
		tv233.setText(list2[10]);
//心包    
		tv321.setText(list1[1]);
		tv331.setText(list2[1]);
//三焦
		tv322.setText(list1[4]);
		tv332.setText(list2[4]);
//胃
		tv323.setText(list1[11]);
		tv333.setText(list2[11]);
//小肠	
		tv421.setText(list1[3]);
		tv431.setText(list2[3]);
//大肠
		tv422.setText(list1[5]);
		tv432.setText(list2[5]);
//膀胱	
		tv423.setText(list1[9]);
		tv433.setText(list2[9]);
	      
		
      nextbut.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
 
				Intent intent = new Intent(Organs.this, ModernEvaluation.class);			
				Bundle data=new Bundle();
				data.putString("modern", modernevaluation);
				data.putString("solution", solution);
				intent.putExtras(data);
				startActivity(intent);
			}
		});
      

      
      
      
		
		
		
	}

	private void check(String string,int i) {
		// TODO Auto-generated method stub
		switch (string){
		case "阴虚":
			list1[i]="阴虚";
			list2[i]="正常";			
			break;
		case "阴不足":
			list1[i]="阴不足";
			list2[i]="正常";
			break;
		case "正常":
			list1[i]="正常";
			list2[i]="正常";
			break;
		case "阳不足":
			list1[i]="正常";
			list2[i]="阳不足";
			break;
		case "阳虚":
			list1[i]="正常";
			list2[i]="阳虚";
			break;
		case "亏":
			list1[i]="阴虚";
			list2[i]="阳虚";
			break;
		case "气血两虚":
			list1[i]="气虚";
			list2[i]="血虚";
			break;
		case "热扰":
			list1[i]="热扰";
			list2[i]="正常";
			break;
		case "热":
			list1[i]="热";
			list2[i]="正常";
			break;
		case "热燥":
			list1[i]="热燥";
			list2[i]="正常";
			break;
		case "热结":
			list1[i]="热结";
			list2[i]="正常";
			break;
		case "寒扰":
			list1[i]="正常";
			list2[i]="寒扰";
			break;
		case "寒":
			list1[i]="正常";
			list2[i]="寒";
			break;
		case "寒凝":
			list1[i]="正常";
			list2[i]="寒凝";
			break;
		case "寒结":
			list1[i]="正常";
			list2[i]="寒结";
			break;
		case "血不足":
			list1[i]="血不足";
			list2[i]="正常";
			break;
		case "血虚":
			list1[i]="血虚";
			list2[i]="正常";
			break;
		case "气不足":
			list1[i]="正常";
			list2[i]="气不足";
			break;
		case "气虚":
			list1[i]="正常";
			list2[i]="气虚";
			break;
		case "阴亢":
			list1[i]="阴亢";
			list2[i]="正常";
			break;
		case "阴盛":
			list1[i]="阴盛";
			list2[i]="正常";
			break;
		case "阳亢":
			list1[i]="正常";
			list2[i]="阳亢";
			break;
		case "阳盛":
			list1[i]="正常";
			list2[i]="阳盛";
			break;
		}
	}

}
