package com.pkusz.analyze;

import com.example.jinluokb.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
//�����õ�activity
public class Organs extends Activity {

	private TextView tv121,tv122,tv123,tv131,tv132,tv133,tv221,tv222,tv223,tv231,tv232,tv233;
	private TextView tv321,tv322,tv323,tv331,tv332,tv333,tv421,tv422,tv423,tv431,tv432,tv433;
	
	private Button nextbut;
	String modernevaluation,solution;
	
        	public String YinXu = "����";
        	public String YinBuZu = "������";
            public String ZhengChang = "����";
            public String YangBuZu = "������";
            public String YangXu = "����";

            public String Sheng = "ʢ";
            public String Kang = "��";

            public String YKYX = "��������";
            public String YSYS = "��������";
            public String YYLX = "��";
            public String QXLX = "��Ѫ����";

            public String RERAO = "����";
            public String RE = "��";
            public String REZAO = "����";
            public String REJIE = "�Ƚ�";

            public String HANRAO = "����";
            public String HAN = "��";
            public String HANNING = "����";
            public String HANJIE = "����";

            public String XUEBUZU = "Ѫ����";
            public String XUEXU = "Ѫ��";
            public String QIBUZU = "������";
            public String QIXU = "����";
            public String YINKANG = "����";
            public String YINSHENG = "��ʢ";
            public String YANGKANG = "����";
            public String YANGSHENG = "��ʢ";
            
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
		
//��
		tv121.setText(list1[2]);
		tv131.setText(list2[2]);
//��
		tv122.setText(list1[0]);
		tv132.setText(list2[0]);
//��
		tv123.setText(list1[7]);
		tv133.setText(list2[7]);
//Ƣ
		tv221.setText(list1[6]);
		tv231.setText(list2[6]);
//��	
		tv222.setText(list1[8]);
		tv232.setText(list2[8]);
//��	
		tv223.setText(list1[10]);
		tv233.setText(list2[10]);
//�İ�    
		tv321.setText(list1[1]);
		tv331.setText(list2[1]);
//����
		tv322.setText(list1[4]);
		tv332.setText(list2[4]);
//θ
		tv323.setText(list1[11]);
		tv333.setText(list2[11]);
//С��	
		tv421.setText(list1[3]);
		tv431.setText(list2[3]);
//��
		tv422.setText(list1[5]);
		tv432.setText(list2[5]);
//����	
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
		case "����":
			list1[i]="����";
			list2[i]="����";			
			break;
		case "������":
			list1[i]="������";
			list2[i]="����";
			break;
		case "����":
			list1[i]="����";
			list2[i]="����";
			break;
		case "������":
			list1[i]="����";
			list2[i]="������";
			break;
		case "����":
			list1[i]="����";
			list2[i]="����";
			break;
		case "��":
			list1[i]="����";
			list2[i]="����";
			break;
		case "��Ѫ����":
			list1[i]="����";
			list2[i]="Ѫ��";
			break;
		case "����":
			list1[i]="����";
			list2[i]="����";
			break;
		case "��":
			list1[i]="��";
			list2[i]="����";
			break;
		case "����":
			list1[i]="����";
			list2[i]="����";
			break;
		case "�Ƚ�":
			list1[i]="�Ƚ�";
			list2[i]="����";
			break;
		case "����":
			list1[i]="����";
			list2[i]="����";
			break;
		case "��":
			list1[i]="����";
			list2[i]="��";
			break;
		case "����":
			list1[i]="����";
			list2[i]="����";
			break;
		case "����":
			list1[i]="����";
			list2[i]="����";
			break;
		case "Ѫ����":
			list1[i]="Ѫ����";
			list2[i]="����";
			break;
		case "Ѫ��":
			list1[i]="Ѫ��";
			list2[i]="����";
			break;
		case "������":
			list1[i]="����";
			list2[i]="������";
			break;
		case "����":
			list1[i]="����";
			list2[i]="����";
			break;
		case "����":
			list1[i]="����";
			list2[i]="����";
			break;
		case "��ʢ":
			list1[i]="��ʢ";
			list2[i]="����";
			break;
		case "����":
			list1[i]="����";
			list2[i]="����";
			break;
		case "��ʢ":
			list1[i]="����";
			list2[i]="��ʢ";
			break;
		}
	}

}
