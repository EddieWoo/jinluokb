package com.pkusz.dao;

import java.util.List;

import com.example.jinluokb.R;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FragmentAdapter  extends BaseAdapter{

	public LayoutInflater inflater;
	public List<TestItem> Items;
	
	//构造方法
		public FragmentAdapter(LayoutInflater inflater,List<TestItem> Items) {
			super();
			this.inflater = inflater;
			this.Items = Items;
		}
		
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Items.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return Items.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TestItem wantShowVod = Items.get(position);
		convertView = inflater.inflate(R.layout.adapter_info_item ,parent,false);
		
		
		TextView item_name=(TextView)convertView.findViewById(R.id.item_name);
		TextView item_date=(TextView)convertView.findViewById(R.id.item_date);
		TextView item_sex=(TextView)convertView.findViewById(R.id.item_sex);
		TextView item_age=(TextView)convertView.findViewById(R.id.item_age);
		
		item_name.setText(" "+wantShowVod.getName());
		item_date.setText(" "+wantShowVod.getDate());
		item_sex.setText(" "+wantShowVod.getSex());
		item_age.setText(" "+wantShowVod.getAge());

		convertView.setTag(wantShowVod);
		return convertView;
	}

}
