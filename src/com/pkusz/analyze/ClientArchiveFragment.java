package com.pkusz.analyze;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
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

import com.example.jinluokb.NewTest;
import com.example.jinluokb.R;
import com.pkusz.bluetooth.RTMonitorActivity;
import com.pkusz.constants.DBInfoConfig;
import com.pkusz.dao.DBOpenHelper;
import com.pkusz.dao.FragmentAdapter;
import com.pkusz.dao.OperateDAO;
import com.pkusz.dao.TestItem;

public class ClientArchiveFragment extends Fragment {
	SQLiteDatabase database=null;
	private final int NUMBER = DBInfoConfig.NUM_OF_ITEMS;
	private AlertDialog.Builder builder;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.clientfragment, null);
		OperateDAO dao = new OperateDAO(getActivity());
		List<TestItem> vodItems = dao.getDistinctItemsclient(NUMBER);
		DBOpenHelper helper=new DBOpenHelper(getActivity(),DBInfoConfig.DB_NAME);	
        database=helper.getReadableDatabase();	 
        builder=new AlertDialog.Builder(this.getActivity());
        
		if(!vodItems.isEmpty()){//���������ǿ�
			
			ListView vodsList = (ListView)view.findViewById(R.id.clientlist);
			FragmentAdapter vodAdapter = new FragmentAdapter(inflater,vodItems);
			vodsList.setAdapter(vodAdapter);
			vodsList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View view,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
                   TestItem thisitem=(TestItem) view.getTag();
					
					String name=thisitem.getName();
					String sex=thisitem.getSex();
					String age=thisitem.getAge();
					String marriage=thisitem.getMarriage();
//					String addr=thisitem.getAddr();
					String tel=thisitem.getTel();
					String category="�ͻ�";
					
					Date d = new Date();  
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
                    String dateNowStr = sdf.format(d);
					
                    Object[] params = {name,age,sex,marriage,tel,category,dateNowStr};
                    
                    database.execSQL(DBInfoConfig.INSERT_INTO_CLIENT, params);
                    
					 Intent intent = new Intent(view.getContext(), NewTest.class);
					 Bundle bundle=new Bundle();					 
	                 bundle.putString("category", "�ͻ�");
	                 bundle.putString("date",dateNowStr);	                 
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
			               builder.setMessage("ȷ��ɾ�����û���"); //��������
//			               builder.setIcon(R.mipmap.ic_launcher);//����ͼ�꣬ͼƬid����
			               builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() { //����ȷ����ť
			                   private String name;

							@Override 
			                   public void onClick(DialogInterface dialog, int which) {
			                        dialog.dismiss(); //�ر�dialog	
									 name=thisitem.getName();
						        	 Object[] params = {name};
					                 database.execSQL(DBInfoConfig.DELETE_CLIENT, params);
			                   }
			               });
			               builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() { //����ȡ����ť
			                   @Override
			                   public void onClick(DialogInterface dialog, int which) {
			                       dialog.dismiss();
			                   }
			               });
			               //��������������ˣ���������ʾ����
			               builder.create().show();
		                   return true;
			}
			});			
		}else{
			System.out.println("�����ݿ��в�ѯ���Ľ����Ϊ��");
		}
		
		return view;
	}
	
	
	
	
	
}
