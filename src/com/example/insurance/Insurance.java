package com.example.insurance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.example.jinluokb.R;
import com.pkusz.constants.WebInfoConfig;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

public class Insurance extends Activity{
	private GridView insuranceview;
	private List<Map<String, Object>> data_list;
	private SimpleAdapter sim_adapter;
	private int[] icon = { R.drawable.pingan1,R.drawable.xinhua1, R.drawable.yangguang1,R.drawable.renshou1, R.drawable.taikang1};
	private String[] iconName = { "平安保险","新华保险", "阳光保险", "人寿保险",  "泰康保险"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.insurance);
		insuranceview = (GridView) findViewById(R.id.insurancegrid);
		
		data_list = new ArrayList<Map<String, Object>>();
		getData();
		String [] from ={"image","text"};
		int [] to = {R.id.image,R.id.text};
		sim_adapter = new SimpleAdapter(this, data_list, R.layout.item1, from, to);
		//配置适配器
		insuranceview.setAdapter(sim_adapter);
		insuranceview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parentViews, View clickView, int position,
					long id) {

				if(position==0){
					 Intent intent = new Intent(Insurance.this, InsuranceWeb.class);
					 Bundle bundle=new Bundle();
	                 bundle.putString("url", WebInfoConfig.pingan);
	                 intent.putExtras(bundle);
					 startActivity(intent);
			    }
				if(position==1){
					 Intent intent = new Intent(Insurance.this, InsuranceWeb.class);
					 Bundle bundle=new Bundle();
	                 bundle.putString("url", WebInfoConfig.xinghua);
	                 intent.putExtras(bundle);
					 startActivity(intent);
				}
				if(position==2){
					 Intent intent = new Intent(Insurance.this, InsuranceWeb.class);
					 Bundle bundle=new Bundle();
	                 bundle.putString("url", WebInfoConfig.yangguang);
	                 intent.putExtras(bundle);
					 startActivity(intent);
				}
				if(position==3){
					 Intent intent = new Intent(Insurance.this, InsuranceWeb.class);
					 Bundle bundle=new Bundle();
	                 bundle.putString("url", WebInfoConfig.renshou);
	                 intent.putExtras(bundle);
					 startActivity(intent);
				}
				if(position==4){
					 Intent intent = new Intent(Insurance.this, InsuranceWeb.class);
					 Bundle bundle=new Bundle();
	                 bundle.putString("url", WebInfoConfig.taikang);
	                 intent.putExtras(bundle);
					 startActivity(intent);
				}
			}
		});
	}

	public List<Map<String, Object>> getData(){		
		//cion和iconName的长度是相同的，这里任选其一都可以
		for(int i=0;i<icon.length;i++){
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("image", icon[i]);
			map.put("text", iconName[i]);
			data_list.add(map);
		}
			
		return data_list;
	}
	
}
