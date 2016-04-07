package com.pkusz.constants;


public class DBInfoConfig {

	public static final int DB_VERSION = 1;
	public static final String DB_NAME = "jinbaoluokang.db";
	
//  初始化数据库
//	表中的数据有，姓名，年龄，性别，婚否，电话，地址，测试时间，测量数据(string)
	public static final String CREATE_TABLE_FRIEND = "create table if not exists table_friend(friend_id integer primary key AUTOINCREMENT,fr_name varchar(64),fr_age varchar(8),fr_sex varchar(8),fr_marriage varchar(8),fr_tel varchar(64) ,fr_addr varchar(64),date varchar(64),data varchar(64))";
	public static final String CREATE_TABLE_COLLEAGUE = "create table if not exists table_colleague(colleague_id integer primary key AUTOINCREMENT,co_name varchar(64),co_age varchar(8),co_sex varchar(8),co_marriage varchar(8),co_tel varchar(64),co_addr varchar(64),date varchar(64),data varchar(64))";
	public static final String CREATE_TABLE_FAMILY = "create table if not exists table_family(family_id integer primary key AUTOINCREMENT,fa_name varchar(64),fa_age varchar(8),fa_sex varchar(8),fa_marriage varchar(8),fa_tel varchar(64),fa_addr varchar(64),date varchar(64),data varchar(64))";
	public static final String CREATE_TABLE_CLIENT = "create table if not exists table_client(client_id integer primary key AUTOINCREMENT,cl_name varchar(64),cl_age varchar(8),cl_sex varchar(8),cl_marriage varchar(8),cl_tel varchar(64),cl_addr varchar(64),date varchar(64),data varchar(64))";
    
//基本信息，注意！！！这里addr存入的是类别，即朋友，同事等
	public static final String INSERT_INTO_FRIEND = "insert into table_friend(fr_name ,fr_age ,fr_sex,fr_marriage ,fr_tel,fr_addr,date) values(?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_COLLEAGUE = "insert into table_colleague(co_name,co_age,co_sex,co_marriage,co_tel,co_addr,date) values(?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_FAMILY = "insert into table_family(fa_name,fa_age,fa_sex,fa_marriage,fa_tel,fa_addr,date) values(?,?,?,?,?,?,?)";
	public static final String INSERT_INTO_CLIENT = "insert into table_client(cl_name,cl_age,cl_sex,cl_marriage,cl_tel,cl_addr,date) values(?,?,?,?,?,?,?)";

//更新数据
	public static final String UPDATE_DATA_FRIEND="update table_friend set data = ? where date = ? ";
	public static final String UPDATE_DATA_COLLEAGUE="update table_colleague set data = ? where date = ? ";
	public static final String UPDATE_DATA_FAMILY="update table_family set data = ? where date = ? ";
	public static final String UPDATE_DATA_CLIENT="update table_client set data = ? where date = ? ";
	
//删除数据历史记录
	public static final String DELETE_DATA_FRIEND="delete from table_friend where date = ?";
	public static final String DELETE_DATA_COLLEAGUE="delete from table_colleague where date = ?";
	public static final String DELETE_DATA_FAMILY="delete from table_family where date = ?";
	public static final String DELETE_DATA_CLIENT="delete from table_client where date = ?";
	
	//删除用户
		public static final String DELETE__FRIEND="delete  from table_friend where fr_name = ?";
		public static final String DELETE_COLLEAGUE="delete from table_colleague where co_name = ?";
		public static final String DELETE_FAMILY="delete from table_family where fa_name = ?";
		public static final String DELETE_CLIENT="delete  from table_client where cl_name = ?";
	
//用户档案查询，合并重复的
	public static final String SELECT_CLIENT_ITEMS_UNIQUE = "select  * from table_client group by cl_name ";
	public static final String SELECT_FAMILY_ITEMS_UNIQUE = "select  * from table_family group by fa_name ";
	public static final String SELECT_COLLEAGUE_ITEMS_UNIQUE = "select  * from table_colleague group by co_name ";
	public static final String SELECT_FRIEND_ITEMS_UNIQUE ="select * from table_friend group by fr_name ";

	
//OperateDAO中使用：
	public static final String SELECT_CLIENT_ITEMS = "select * from table_client order by date desc";
	public static final String SELECT_FAMILY_ITEMS = "select * from table_family order by date desc";
	public static final String SELECT_COLLEAGUE_ITEMS = "select * from table_colleague order by date desc";
	public static final String SELECT_FRIEND_ITEMS = "select * from table_friend order by date desc";

	
	public static final int NUM_OF_GET_LATEST_VOD = 20;
	public static final int NUM_OF_ITEMS = 20;
	public static final int NUM_OF_GET_HISTORY_VOD = 20;
	public static final int NUM_OF_GET_LIKES_VOD = 20;
	
	
	//VideoPlayActivity中使用:
	public static final String SELECT_PPLIST_ITEM_BY_LISTID = "select * from pp_list where list_id=?";
	
	//SearchView相关使用：
	public static final String SELECT_VODS_LIKE_STRING = "select * from pp_vod where vod_name like ? or vod_content like ?";
	public static final String SELECT_VODS_BY_VOD_ID = "select * from pp_vod where vod_id=?";
	public static final String SELECT_VODS_BY_LIKES_ID = "select * from play_likes where likes_id=?";

}
