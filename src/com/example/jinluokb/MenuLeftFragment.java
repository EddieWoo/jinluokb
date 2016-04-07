package com.example.jinluokb;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

public class MenuLeftFragment extends Fragment
{
	@Override
	 public void onAttach(Activity activity) {
	 // TODO Auto-generated method stub
	 super.onAttach(activity);
	
	 }
	
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
	}
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View rootview=inflater.inflate(R.layout.layout_menu, container, false);
		return rootview;
		
	}

	
     
	
	
}
