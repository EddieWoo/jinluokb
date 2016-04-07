package com.pkusz.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pkusz.dao.DBOpenHelper;
import com.pkusz.constants.DBInfoConfig;

public class OperateDAO {
	
	 public DBOpenHelper helper;// 创建DBOpenHelper对象
	    public SQLiteDatabase db;// 创建SQLiteDatabase对象
	    public SQLiteDatabase db1;
		public OperateDAO(Context context) {
			helper = new DBOpenHelper(context, DBInfoConfig.DB_NAME);// 初始化DBOpenHelper对象
		}
		
		
		public SQLiteDatabase getReadableDB(){
			return helper.getReadableDatabase();
		}

		public List<TestItem> getTestItems(int count) {//搜索客户列表
		  //TODO Auto-generated method stub
			List<TestItem> lists=new ArrayList<TestItem>();
			
			db = helper.getReadableDatabase();
			String sql = DBInfoConfig.SELECT_CLIENT_ITEMS;
			Cursor cursor = db.rawQuery(sql, null);
			
			while(cursor.moveToNext() && count>0){
				TestItem vodItem = new TestItem();

				vodItem.setName(cursor.getString(cursor.getColumnIndex("cl_name")));
				System.out.println("得到的姓名是"+cursor.getString(cursor.getColumnIndex("cl_name")));
				
				vodItem.setAge(cursor.getString(cursor.getColumnIndex("cl_age")));
				vodItem.setSex(cursor.getString(cursor.getColumnIndex("cl_sex")));
				vodItem.setMarriage(cursor.getString(cursor.getColumnIndex("cl_marriage")));
				vodItem.setTel(cursor.getString(cursor.getColumnIndex("cl_tel")));
				vodItem.setAddr(cursor.getString(cursor.getColumnIndex("cl_addr")));
				vodItem.setDate(cursor.getString(cursor.getColumnIndex("date")));
				System.out.println("得到的日期是"+cursor.getString(cursor.getColumnIndex("date")));
				vodItem.setData(cursor.getString(cursor.getColumnIndex("data")));

				lists.add(vodItem);			
				count = count-1;
				
			}
			if(cursor != null){
				cursor.close();
			}
			if(db !=null && db.isOpen()){
				db.close();
			}
			return lists;
			
		}
		public List<TestItem> getTestItemsfamily(int count) {
			  //TODO Auto-generated method stub
				List<TestItem> lists=new ArrayList<TestItem>();
				
				db = helper.getReadableDatabase();
				String sql = DBInfoConfig.SELECT_FAMILY_ITEMS;
				Cursor cursor = db.rawQuery(sql, null);
				
				while(cursor.moveToNext() && count>0){
					TestItem vodItem = new TestItem();
					
					vodItem.setName(cursor.getString(cursor.getColumnIndex("fa_name")));
					System.out.println("得到的姓名是"+cursor.getString(cursor.getColumnIndex("fa_name")));
					
					vodItem.setAge(cursor.getString(cursor.getColumnIndex("fa_age")));
					vodItem.setSex(cursor.getString(cursor.getColumnIndex("fa_sex")));
					vodItem.setMarriage(cursor.getString(cursor.getColumnIndex("fa_marriage")));
					vodItem.setTel(cursor.getString(cursor.getColumnIndex("fa_tel")));
					vodItem.setAddr(cursor.getString(cursor.getColumnIndex("fa_addr")));
					vodItem.setDate(cursor.getString(cursor.getColumnIndex("date")));
					System.out.println("得到的日期是"+cursor.getString(cursor.getColumnIndex("date")));
					vodItem.setData(cursor.getString(cursor.getColumnIndex("data")));

					lists.add(vodItem);			
					count = count-1;
					
				}
				if(cursor != null){
					cursor.close();
				}
				if(db !=null && db.isOpen()){
					db.close();
				}
				return lists;
				
			}
		
		public List<TestItem> getTestItemscolleague(int count) {
			  //TODO Auto-generated method stub
				List<TestItem> lists=new ArrayList<TestItem>();
				
				db = helper.getReadableDatabase();
				String sql = DBInfoConfig.SELECT_COLLEAGUE_ITEMS;
				Cursor cursor = db.rawQuery(sql, null);
				
				while(cursor.moveToNext() && count>0){
					TestItem vodItem = new TestItem();
					
					vodItem.setName(cursor.getString(cursor.getColumnIndex("co_name")));
					System.out.println("得到的姓名是"+cursor.getString(cursor.getColumnIndex("co_name")));
					
					vodItem.setAge(cursor.getString(cursor.getColumnIndex("co_age")));
					vodItem.setSex(cursor.getString(cursor.getColumnIndex("co_sex")));
					vodItem.setMarriage(cursor.getString(cursor.getColumnIndex("co_marriage")));
					vodItem.setTel(cursor.getString(cursor.getColumnIndex("co_tel")));
					vodItem.setAddr(cursor.getString(cursor.getColumnIndex("co_addr")));
					vodItem.setDate(cursor.getString(cursor.getColumnIndex("date")));
					System.out.println("得到的日期是"+cursor.getString(cursor.getColumnIndex("date")));
					vodItem.setData(cursor.getString(cursor.getColumnIndex("data")));

					lists.add(vodItem);			
					count = count-1;
					
				}
				if(cursor != null){
					cursor.close();
				}
				if(db !=null && db.isOpen()){
					db.close();
				}
				return lists;
				
			}
		
		public List<TestItem> getTestItemsfriend(int count) {
			  //TODO Auto-generated method stub
				List<TestItem> lists=new ArrayList<TestItem>();
				
				db = helper.getReadableDatabase();
				String sql = DBInfoConfig.SELECT_FRIEND_ITEMS;
				Cursor cursor = db.rawQuery(sql, null);
				
				while(cursor.moveToNext() && count>0){
					TestItem vodItem = new TestItem();
					
					vodItem.setName(cursor.getString(cursor.getColumnIndex("fr_name")));
					System.out.println("得到的姓名是"+cursor.getString(cursor.getColumnIndex("fr_name")));
					
					vodItem.setAge(cursor.getString(cursor.getColumnIndex("fr_age")));
					vodItem.setSex(cursor.getString(cursor.getColumnIndex("fr_sex")));
					vodItem.setMarriage(cursor.getString(cursor.getColumnIndex("fr_marriage")));
					vodItem.setTel(cursor.getString(cursor.getColumnIndex("fr_tel")));
					vodItem.setAddr(cursor.getString(cursor.getColumnIndex("fr_addr")));
					vodItem.setDate(cursor.getString(cursor.getColumnIndex("date")));
					System.out.println("得到的日期是"+cursor.getString(cursor.getColumnIndex("date")));
					vodItem.setData(cursor.getString(cursor.getColumnIndex("data")));

					lists.add(vodItem);			
					count = count-1;

				}
				if(cursor != null){
					cursor.close();
				}
				if(db !=null && db.isOpen()){
					db.close();
				}
				return lists;
				
			}
		
		public List<TestItem> getDistinctItemsfriend(int count) {
			  //TODO Auto-generated method stub
				List<TestItem> lists=new ArrayList<TestItem>();
				
				db = helper.getReadableDatabase();
				String sql = DBInfoConfig.SELECT_FRIEND_ITEMS_UNIQUE;
				Cursor cursor = db.rawQuery(sql, null);
				
				while(cursor.moveToNext() && count>0){
					TestItem vodItem = new TestItem();
					
					vodItem.setName(cursor.getString(cursor.getColumnIndex("fr_name")));
					System.out.println("得到的姓名是"+cursor.getString(cursor.getColumnIndex("fr_name")));
					
					vodItem.setAge(cursor.getString(cursor.getColumnIndex("fr_age")));
					vodItem.setSex(cursor.getString(cursor.getColumnIndex("fr_sex")));
					vodItem.setMarriage(cursor.getString(cursor.getColumnIndex("fr_marriage")));
					vodItem.setTel(cursor.getString(cursor.getColumnIndex("fr_tel")));
					vodItem.setAddr(cursor.getString(cursor.getColumnIndex("fr_addr")));
					vodItem.setDate(cursor.getString(cursor.getColumnIndex("date")));
					System.out.println("得到的日期是"+cursor.getString(cursor.getColumnIndex("date")));
					vodItem.setData(cursor.getString(cursor.getColumnIndex("data")));

					lists.add(vodItem);			
					count = count-1;

				}
				if(cursor != null){
					cursor.close();
				}
				if(db !=null && db.isOpen()){
					db.close();
				}
				return lists;
				
			}
		
		public List<TestItem> getDistinctItemsfamily(int count) {
			  //TODO Auto-generated method stub
				List<TestItem> lists=new ArrayList<TestItem>();
				
				db = helper.getReadableDatabase();
				String sql = DBInfoConfig.SELECT_FAMILY_ITEMS_UNIQUE;
				Cursor cursor = db.rawQuery(sql, null);
				
				while(cursor.moveToNext() && count>0){
					TestItem vodItem = new TestItem();
					
					vodItem.setName(cursor.getString(cursor.getColumnIndex("fa_name")));
					System.out.println("得到的姓名是"+cursor.getString(cursor.getColumnIndex("fa_name")));
					vodItem.setAge(cursor.getString(cursor.getColumnIndex("fa_age")));
					vodItem.setSex(cursor.getString(cursor.getColumnIndex("fa_sex")));
					vodItem.setMarriage(cursor.getString(cursor.getColumnIndex("fa_marriage")));
					vodItem.setTel(cursor.getString(cursor.getColumnIndex("fa_tel")));
					vodItem.setAddr(cursor.getString(cursor.getColumnIndex("fa_addr")));
					vodItem.setDate(cursor.getString(cursor.getColumnIndex("date")));
					System.out.println("得到的日期是"+cursor.getString(cursor.getColumnIndex("date")));
					vodItem.setData(cursor.getString(cursor.getColumnIndex("data")));

					lists.add(vodItem);			
					count = count-1;
					
				}
				if(cursor != null){
					cursor.close();
				}
				if(db !=null && db.isOpen()){
					db.close();
				}
				return lists;
				
			}
		
		public List<TestItem> getDistinctItemsclient(int count) {
			  //TODO Auto-generated method stub
				List<TestItem> lists=new ArrayList<TestItem>();
				
				db = helper.getReadableDatabase();
				String sql = DBInfoConfig.SELECT_CLIENT_ITEMS_UNIQUE;
				Cursor cursor = db.rawQuery(sql, null);
				
				while(cursor.moveToNext() && count>0){
					TestItem vodItem = new TestItem();

					vodItem.setName(cursor.getString(cursor.getColumnIndex("cl_name")));
					System.out.println("得到的姓名是"+cursor.getString(cursor.getColumnIndex("cl_name")));
					
					vodItem.setAge(cursor.getString(cursor.getColumnIndex("cl_age")));
					vodItem.setSex(cursor.getString(cursor.getColumnIndex("cl_sex")));
					vodItem.setMarriage(cursor.getString(cursor.getColumnIndex("cl_marriage")));
					vodItem.setTel(cursor.getString(cursor.getColumnIndex("cl_tel")));
					vodItem.setAddr(cursor.getString(cursor.getColumnIndex("cl_addr")));
					vodItem.setDate(cursor.getString(cursor.getColumnIndex("date")));
					System.out.println("得到的日期是"+cursor.getString(cursor.getColumnIndex("date")));
					vodItem.setData(cursor.getString(cursor.getColumnIndex("data")));

					lists.add(vodItem);			
					count = count-1;
					
				}
				if(cursor != null){
					cursor.close();
				}
				if(db !=null && db.isOpen()){
					db.close();
				}
				return lists;
				
			}
		
		public List<TestItem> getDistinctItemscolleague(int count) {
			  //TODO Auto-generated method stub
				List<TestItem> lists=new ArrayList<TestItem>();
				
				db = helper.getReadableDatabase();
				String sql = DBInfoConfig.SELECT_COLLEAGUE_ITEMS_UNIQUE;
				Cursor cursor = db.rawQuery(sql, null);
				
				while(cursor.moveToNext() && count>0){
					TestItem vodItem = new TestItem();
					
					vodItem.setName(cursor.getString(cursor.getColumnIndex("co_name")));
					System.out.println("得到的姓名是"+cursor.getString(cursor.getColumnIndex("co_name")));
					
					vodItem.setAge(cursor.getString(cursor.getColumnIndex("co_age")));
					vodItem.setSex(cursor.getString(cursor.getColumnIndex("co_sex")));
					vodItem.setMarriage(cursor.getString(cursor.getColumnIndex("co_marriage")));
					vodItem.setTel(cursor.getString(cursor.getColumnIndex("co_tel")));
					vodItem.setAddr(cursor.getString(cursor.getColumnIndex("co_addr")));
					vodItem.setDate(cursor.getString(cursor.getColumnIndex("date")));
					System.out.println("得到的日期是"+cursor.getString(cursor.getColumnIndex("date")));
					vodItem.setData(cursor.getString(cursor.getColumnIndex("data")));

					lists.add(vodItem);			
					count = count-1;
					
				}
				if(cursor != null){
					cursor.close();
				}
				if(db !=null && db.isOpen()){
					db.close();
				}
				return lists;
				
			}
		
		public void cleandata(){
			db1=helper.getWritableDatabase();
			db1.execSQL("DELETE FROM table_family;");
			db1.execSQL("DELETE FROM table_colleague;");
			db1.execSQL("DELETE FROM table_friend;");
			db1.execSQL("DELETE FROM table_client;");
		}
}
