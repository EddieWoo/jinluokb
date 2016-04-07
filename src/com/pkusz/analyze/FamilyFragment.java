package com.pkusz.analyze;

import java.util.List;

import com.example.jinluokb.R;
import com.pkusz.constants.DBInfoConfig;
import com.pkusz.dao.DBOpenHelper;
import com.pkusz.dao.FragmentAdapter;
import com.pkusz.dao.OperateDAO;
import com.pkusz.dao.TestItem;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/*
 * familyfragment 是得到data数据，传给服务器做分析
 * familyarchivefragment是新建测试*/
public class FamilyFragment extends Fragment {
	
	SQLiteDatabase database=null;
	String data="";
	String age="25";
	String sex="男";
	String marriage="未婚";
	String date="男";
	
	
private final int NUMBER = DBInfoConfig.NUM_OF_ITEMS;
private Dialog _pDialog;
private AlertDialog.Builder builder;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragmentfamily, null);
		OperateDAO dao = new OperateDAO(getActivity());
		List<TestItem> vodItems = dao.getTestItemsfamily(NUMBER);
		builder=new AlertDialog.Builder(this.getActivity());
        
		DBOpenHelper helper=new DBOpenHelper(getActivity(),DBInfoConfig.DB_NAME);	
        database=helper.getReadableDatabase();	
        
        
		if(!vodItems.isEmpty()){//如果结果集非空
			
			ListView vodsList = (ListView)view.findViewById(R.id.familylist);
			FragmentAdapter vodAdapter = new FragmentAdapter(inflater,vodItems);
			vodsList.setAdapter(vodAdapter);
			vodsList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
					System.out.println("data from tag--->"+view.getTag());
					
					TestItem thisitem=(TestItem) view.getTag();
					
					 data=thisitem.getData();
					 if(data==null){
						 data="50%50%50%50%50%50%50%50%50%50%50%50%50%50%50%50%50%50%50%50%50%50%50%50%50%50%";
					 }
					 age=thisitem.getAge();
					 marriage=thisitem.getMarriage();
					 sex=thisitem.getSex();
					 date=thisitem.getDate();
					
					
					Intent intent = new Intent(view.getContext(), Analyze.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("age", age);
                    bundle.putString("data", data);
                    bundle.putString("marriage", marriage);
                    bundle.putString("sex", sex);
                    bundle.putString("date", date);
                    intent.putExtras(bundle);
					startActivity(intent);
				}
			});
			
			 vodsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			        private TestItem thisitem;

					@Override
			        public boolean onItemLongClick(AdapterView<?> arg0, View view,
			                final int arg2, long arg3) {
			        	 
			               thisitem=(TestItem) view.getTag();
			               builder.setMessage("确定删除该条记录吗？"); //设置内容
//			               builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
			               builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
			                   @Override 
			                   public void onClick(DialogInterface dialog, int which) {
			                       dialog.dismiss(); //关闭dialog	
			                        
									 date=thisitem.getDate();
						        	 Object[] params = {date};
					                 database.execSQL(DBInfoConfig.DELETE_DATA_FAMILY, params);
			                   }
			               });
			               builder.setNegativeButton("取消", new DialogInterface.OnClickListener() { //设置取消按钮
			                   @Override
			                   public void onClick(DialogInterface dialog, int which) {
			                       dialog.dismiss();
			                   }
			               });
			               //参数都设置完成了，创建并显示出来
			               builder.create().show();
		                 return true;
			}
			});
			 
		}else{
			System.out.println("从数据库中查询出的结果集为空");
		}
		
		return view;}
}
