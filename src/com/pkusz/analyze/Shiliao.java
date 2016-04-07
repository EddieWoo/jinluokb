package com.pkusz.analyze;

import com.example.insurance.InsuranceWeb;
import com.example.jinluokb.R;
import com.pkusz.constants.WebInfoConfig;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Shiliao extends Activity {//膳食调整

	String shiliaostr;
	TextView shiliao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shiliao);
		shiliao=(TextView)findViewById(R.id.shiliao);
		shiliao.setMovementMethod(ScrollingMovementMethod.getInstance());
		Bundle myBundle=this.getIntent().getExtras(); 
		shiliaostr=myBundle.getString("shiliaostr");
		shiliao.setText(shiliaostr);
		
		Button mall=(Button)findViewById(R.id.mall);
		mall.setOnClickListener(new OnClickListener(){//膳食调整
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Shiliao.this, InsuranceWeb.class);			
				Bundle bundle=new Bundle();
                bundle.putString("url", WebInfoConfig.homepage);
                intent.putExtras(bundle);
				 startActivity(intent);
			}
        	
        });
	}

}
