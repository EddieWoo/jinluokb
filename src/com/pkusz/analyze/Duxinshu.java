package com.pkusz.analyze;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.jinluokb.R;
import com.pkusz.util.DuxinshuAdapter;
import com.pkusz.util.DuxinshuItem;
import com.pkusz.util.PhotoViewer;


public class Duxinshu extends Activity {//读心术

	String imageUrl = "https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png";
	String duxinshustr;
	TextView duxinshu;
	private List<DuxinshuItem> list;
	DuxinshuItem duxinshuitem = null;
	LinkedList<String> namelist =new LinkedList<String>();
	String[] temp;
	String[] urls;
	String descriptionstr,namestr;
	String[] description={" "," "," "," "," "};//图片描述
	String[] name={" "," "," "," "," "};//图片地址
	protected ListView lv;
	String title =" ";
	String summary;

	private Handler mHandler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				DuxinshuAdapter adapter = new DuxinshuAdapter(list);
				lv.setAdapter(adapter);
				lv.setOnItemClickListener(new ItemClickEvent());
				break;

			default:
				break;
			}
		}
		
	};
	private String singlename;
	private String duxinshudescription;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.duxinshu);
        duxinshu=(TextView)findViewById(R.id.duxinshu);
        lv = (ListView) this.findViewById(R.id.lv);
		Bundle myBundle=this.getIntent().getExtras(); 
		duxinshustr=myBundle.getString("duxinshustr");
		duxinshudescription=myBundle.getString("duxinshudescription");
		name=duxinshustr.split("/");
		description=duxinshudescription.split("/");

        
		  list = new ArrayList<DuxinshuItem>();
		  if(name.length>0){
	      for (int i = 0; i < name.length; i++) {
//				String imageUrl1 =imageUrl;
				   if(name[i].trim().endsWith("jpg")){
					   singlename ="http://www.jinluo.net/download/images/"+name[i].trim();
						summary =description[i];
						duxinshuitem = new DuxinshuItem(singlename, title, summary);
						list.add(duxinshuitem);
				   }
				
			}
	      
	      mHandler.obtainMessage(0).sendToTarget();
	}
	}

	private String[] TurnArray(LinkedList<String> urllist2) {
		int n=urllist2.size();
		String[] res=new String[n];
		for(int i=0;i<n;i++){
			res[i]=urllist2.get(i);
		}
		return res;
		// TODO Auto-generated method stub
		
	}
	
	private final class ItemClickEvent implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// TODO Auto-generated method stub
			//通过单击事件，获得单击选项的内容
			DuxinshuItem item = (DuxinshuItem) lv.getItemAtPosition(arg2);
			String urlsss=item.getImageUrl();
			//通过吐丝对象显示出来。
			  Intent intent=new Intent(Duxinshu.this,PhotoViewer.class);
			  Bundle bundle=new Bundle();
              bundle.putString("urlsss", urlsss);
              intent.putExtras(bundle);
//              System.out.println(urlsss);
              startActivity(intent);
		}	
	}

}
