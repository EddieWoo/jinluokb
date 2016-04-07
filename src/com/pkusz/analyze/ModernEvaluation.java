package com.pkusz.analyze;

import java.util.Random;

import com.example.jinluokb.MainActivity;
import com.example.jinluokb.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ModernEvaluation extends Activity{

	String modernevaluation,solution;
	private TextView tv,tv1,tv2;
	private Button solutions,next,homebut;
	String[] titles={"1����������״̬����","2���³´�л״̬����","3������״̬����","4���񾭼������ϵͳ״̬����","5������ƽ������״̬����"};
	String[] evaluations={"i","i","i","i","i","i","i","i","i","i","i"};
	static int i=1;//�ִ����۵��±�
	private String picurl="1,1,1,1,1,1"; 
	private String forresult;
	private String patientsex;


private Handler mHandler = new Handler()  
{  
public void handleMessage(android.os.Message msg)  
{  
	if(evaluations.length==11){
    if(i<5){
	tv.setText(titles[i]);
    tv1.setText("����ֵ�ǣ�"+ evaluations[2*i+1]);
    tv2.setText(evaluations[2*i+2]);
    i++;
    }
    else{
    	i=0;
    	tv.setText(titles[i]);
        tv1.setText("����ֵ�ǣ�"+ evaluations[1]);
        tv2.setText(evaluations[2]);
        i++;
    }
   }  
} 

};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mordernevaluation);
		
		
		Bundle myBundle=this.getIntent().getExtras(); 
		modernevaluation=myBundle.getString("modern");
		solution=myBundle.getString("solution");
		forresult=myBundle.getString("forresult");
		patientsex=myBundle.getString("patientsex");
		
		tv=(TextView)findViewById(R.id.title);
		tv1=(TextView)findViewById(R.id.data);
		tv2=(TextView)findViewById(R.id.description);
		solutions=(Button)findViewById(R.id.button1);
		next=(Button)findViewById(R.id.button2);
		homebut=(Button)findViewById(R.id.homebutton);

		evaluations=modernevaluation.split("%");
		
		for(int i=0;i<evaluations.length;i++){		
			System.out.println(evaluations.length+evaluations.length+evaluations.length);
			System.out.println("�����������"+i+i+i+i+i+i+i+i+i+evaluations[i]);
		}
		
		
		tv1.setText("����ֵ�ǣ�"+ evaluations[1]+"\n\n\n");
		tv2.setText(evaluations[2]);
		
		
		solutions.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
//                if(isNetworkConnected(ModernEvaluation.this)){
				Intent intent = new Intent(ModernEvaluation.this, Solution.class);			
				Bundle data=new Bundle();
				
				data.putString("solution", solution);
				data.putString("forresult", forresult);
				data.putString("patientsex", patientsex);
				intent.putExtras(data);
				startActivity(intent);
//				}else{
//					Toast.makeText(getApplicationContext(), "û�����磬���ȿ������磡", 5000).show();
//				}
			}
		});
		
        next.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 mHandler.sendEmptyMessage(0);
			}
		});
        
        homebut.setOnClickListener(new OnClickListener(){//������ҳ��
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ModernEvaluation.this, MainActivity.class);			
				startActivity(intent);
			}
			});
		
	}
	
	public boolean isNetworkConnected(Context context) {  
	     if (context != null) {  
	         ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
	                 .getSystemService(Context.CONNECTIVITY_SERVICE);  
	         NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
	         if (mNetworkInfo != null) {  
	             return mNetworkInfo.isAvailable();  
	         }  
	     }  
	     return false;  
	 }

}
