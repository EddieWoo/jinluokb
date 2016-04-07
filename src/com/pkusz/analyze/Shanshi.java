package com.pkusz.analyze;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.example.jinluokb.R;
import com.pkusz.util.DuxinshuAdapter;
import com.pkusz.util.DuxinshuItem;
import com.pkusz.util.PhotoViewer;

public class Shanshi extends Activity {//穴位按摩

	String duxinshustr;
	TextView shanshi;
	private ListView lv;
	private String[] temp;
	private List<DuxinshuItem> list;
	DuxinshuItem duxinshuitem = null;
	LinkedList<String> urllist =new LinkedList<String>();
	String[] urls;
	
	
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
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shanshi);
		lv = (ListView) this.findViewById(R.id.lv);
		Bundle myBundle=this.getIntent().getExtras(); 
		duxinshustr=myBundle.getString("duxinshustr");
		
		temp=duxinshustr.split("/");
		System.out.println("图片是"+duxinshustr);
		
		if(temp.length>0){
		for(int i=0;i<temp.length;i++){
			if(temp[i].endsWith("jpg")){
				urllist.add(temp[i]);
			}
			urls=TurnArray(urllist);}
		}
	
		if(temp.length>0){
	  list = new ArrayList<DuxinshuItem>();
      for (int i = 0; i < urls.length; i++) {
			String imageUrl1 ="http://www.jinluo.net/download/images/"+urls[i].trim();
			String title =" ";
			String summary =" ";
			duxinshuitem = new DuxinshuItem(imageUrl1, title, summary);
			System.out.println(imageUrl1);
			list.add(duxinshuitem);
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
			  Intent intent=new Intent(Shanshi.this,PhotoViewer.class);
			  Bundle bundle=new Bundle();
              bundle.putString("urlsss", urlsss);
              intent.putExtras(bundle);
              System.out.println(urlsss);
              startActivity(intent);
		}	
	}
}