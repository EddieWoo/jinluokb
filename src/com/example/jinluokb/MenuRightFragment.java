package com.example.jinluokb;

import com.pkusz.homecenter.HomeCenter;
import com.pkusz.setting.Setting;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class MenuRightFragment extends Fragment implements OnClickListener
{

	SharedPreferences preferences = null;  
    SharedPreferences.Editor editor = null; 
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View rootview=inflater.inflate(R.layout.menu_layout_right, container, false);
		ImageView image=(ImageView) rootview.findViewById(R.id.image1);
		ImageView image2=(ImageView) rootview.findViewById(R.id.image2);
		ImageView image3=(ImageView) rootview.findViewById(R.id.image3);
		ImageView help=(ImageView) rootview.findViewById(R.id.help);
		image.setOnClickListener(this);
		image2.setOnClickListener(this);
		image3.setOnClickListener(this);
		help.setOnClickListener(this);
		preferences=getActivity().getSharedPreferences("userinfo",0);
		
		return rootview;
		
	}
	




	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
		 if(arg0.getId()==R.id.image1){
		    	Intent intent=new Intent(getActivity(), HomeCenter.class);
		    	startActivity(intent);
			}
	    
	    if(arg0.getId()==R.id.image2){
	    	Intent intent=new Intent(getActivity(), Setting.class);
	    	startActivity(intent);
	    }
	    
	    if(arg0.getId()==R.id.help){
	    	Intent intent=new Intent(getActivity(), Helper.class);
	    	startActivity(intent);
	    }
	    
	    if(arg0.getId()==R.id.image3){
	    	AlertDialog ad = new AlertDialog.Builder(getActivity())
	    	.setMessage("退出登录将要删掉个人信息，确定退出吗？")
	    	.setPositiveButton("确定",new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog,
	    	int which) {
	    	//这里写上按确认键你要做的事情
	    		editor=preferences.edit();
	    		editor.clear();
	    		editor.commit();
	    		System.exit(0);
	    		
	    	}
	    	})
	    	.setNegativeButton("取消",
	    	new DialogInterface.OnClickListener() {
	    	public void onClick(DialogInterface dialog,
	    	int which) {
	    		
	    	}
	    	}).create();
	    	ad.show();
	    }
	    
	}
	
	

}
