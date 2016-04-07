package com.example.jinluokb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.DrawerLayout.DrawerListener;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.example.insurance.Insurance;
import com.example.insurance.InsuranceWeb;
import com.nineoldandroids.view.ViewHelper;
import com.pkusz.analyze.Analyze;
import com.pkusz.analyze.Archive;
import com.pkusz.analyze.ShowList;
import com.pkusz.circleimage.CircleImageView;
import com.pkusz.constants.WebInfoConfig;
import com.pkusz.dao.OperateDAO;
import com.pkusz.dao.TestItem;
import com.pkusz.homecenter.HomeCenter;
import com.pkusz.setting.Setting;
import com.pkusz.stepcounter.StepCounterActivity;

/**
 * http://blog.csdn.net/lmj623565791/article/details/41531475
 *
 */
public class MainActivity extends FragmentActivity 
{

	private DrawerLayout mDrawerLayout;
    private MenuRightFragment menurightfagment;
//    private MenuLeftFragment munuleftfragment;
	private GridView gview;
	private List<Map<String, Object>> data_list;
	private SimpleAdapter sim_adapter;
	private CircleImageView civ;
	// 图片封装为一个数组
	private int[] icon = { R.drawable.home_icon1,R.drawable.home_icon2, R.drawable.home_icon3,R.drawable.home_icon4, R.drawable.home_icon5, R.drawable.home_icon6 };
	private String[] iconName = { "新建档案","档案管理", "检测记录", "购买评测",  "商城", "个人中心" };
	
//	"时钟", "游戏", "短信", "铃声","设置", "语音",
	private CircleImageView ivHead;//头像显示
	private Button btnTakephoto;//拍照
	private Button btnPhotos;//相册
	private Bitmap head;//头像Bitmap
	private String name;
	private TextView name1;
	private static String path="/sdcard/myHead/";//sd路径
	SharedPreferences preferences=null;
	ArrayList<String> notifyname=new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
       //绑定相应的fragment
		android.support.v4.app.FragmentManager fm = getSupportFragmentManager();  
		menurightfagment = (MenuRightFragment) fm.findFragmentById(R.id.id_right_menu);  
		name1=(TextView) findViewById(R.id.name);
//		munuleftfragment = (MenuLeftFragment) fm.findFragmentById(R.id.id_left_menu);
		
//        if(munuleftfragment == null )  
//        {  
//        	munuleftfragment = new MenuLeftFragment();  
//          fm.beginTransaction().add(R.id.id_left_menu,munuleftfragment).commit();  
//        }  
        if(menurightfagment == null )  
        {  
        	menurightfagment = new MenuRightFragment();  
            fm.beginTransaction().add(R.id.id_right_menu,menurightfagment).commit();  
        }  
        
        //gridview的操作
        gview = (GridView) findViewById(R.id.gview);
        civ=(CircleImageView) findViewById(R.id.civ);
		//新建List
		data_list = new ArrayList<Map<String, Object>>();
		//获取数据
		getData();
		//新建适配器
		String [] from ={"image","text"};
		int [] to = {R.id.image,R.id.text};
		sim_adapter = new SimpleAdapter(this, data_list, R.layout.item, from, to);
		//配置适配器
		gview.setAdapter(sim_adapter);
		
		initView();
		initEvents();

		 preferences=getSharedPreferences("userinfo",0);
		 name=preferences.getString("name", " ");
		 name1.setText(name);
		
		//主界面下端gridview的点击事件
		gview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parentViews, View clickView, int position,
					long id) {
				//为Intent消息对象建立 默认的Bundle数据包
//				Bundle bundle = new Bundle();
				//position从0开始计起，8为“中学gridview”下的第一个Item"语文"，其List_id为8，即position和对应分类的cid的偏移量
//				bundle.putInt("list_id", position+8);
//				bundle.putString("list_name", highSchoolTitles[position]);
//				Intent intent=new Intent(MainActivity.this, HomeCenter.class);
//				intent.putExtras(bundle);
//			    startActivity(intent);
				if(position==0){
//					 AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//		                builder.setIcon(R.drawable.ic_launcher);
//		                builder.setTitle("请选择一个类别");
//		                //    指定下拉列表的显示数据
//		                final String[] categories = {"同事", "朋友", "家人", "客户"};
//		                //    设置一个下拉的列表选择项
//		                builder.setItems(categories, new DialogInterface.OnClickListener()
//		                {
//		                    @Override
//		                    public void onClick(DialogInterface dialog, int which)
//		                    {		                       
		                    	
		                    	Intent intent = new Intent();
//		                    	Bundle bundle = new Bundle();
//		                    	bundle.putInt("category", which);
		                    	intent.setClass(MainActivity.this, NewTester.class);
//		                    	intent.putExtras(bundle);
		                    	startActivity(intent);
//		                    }
//		                });
//		                builder.show();
			    }
				if(position==1){
					Intent intent=new Intent(MainActivity.this, Archive.class);
				    startActivity(intent);
				}
				if(position==2){
					Intent intent=new Intent(MainActivity.this, ShowList.class);
				    startActivity(intent);
				}
				
				if(position==3){
					
					Intent intent=new Intent(MainActivity.this, InsuranceWeb.class);
					Bundle bundle=new Bundle();
					bundle.putString("url", WebInfoConfig.pingce);
					intent.putExtras(bundle);
				    startActivity(intent);
				}
				if(position==4){
					Intent intent=new Intent(MainActivity.this, Insurance.class);
				    startActivity(intent);
				}
				if(position==5){
					Intent intent=new Intent(MainActivity.this, InsuranceWeb.class);
					Bundle bundle=new Bundle();
					bundle.putString("url", WebInfoConfig.infocnter);
					intent.putExtras(bundle);
				    startActivity(intent);
				}
				
				
			}
		});
		
//		civ.setOnClickListener((OnClickListener) this);
		civ.setOnClickListener(new OnClickListener() {//按键单击事件

			 @Override
		 public void onClick(View v) {
				 // TODO Auto-generated method stub
				 new AlertDialog.Builder(MainActivity.this).setTitle("修改头像")//设置对话框标题
				 .setMessage("请选择图片来源")//设置显示的内容
				 .setPositiveButton("拍照",new DialogInterface.OnClickListener() {//添加确定按钮
			       @Override
					 public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
						 // TODO Auto-generated method stub
			    	   Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
						intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
										"head.jpg")));
						startActivityForResult(intent2, 2);//采用ForResult打开
			    	   
					 }
				 }).setNegativeButton("相册",new DialogInterface.OnClickListener() {//添加返回按钮
                    @Override
					 public void onClick(DialogInterface dialog, int which) {//响应事件
						 // TODO Auto-generated method stub
                    	Intent intent1 = new Intent(Intent.ACTION_PICK, null);
						intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
						startActivityForResult(intent1, 1);
					 }
				 }).show();//在按键响应事件中显示此对话框

			 }

		 });
//		
		if(!isNetworkConnected(MainActivity.this)){
			Toast.makeText(getApplicationContext(), "没有网络，请先开启网络！", 5000).show();
		}
		
		preferences=getSharedPreferences("userinfo",0);
		if(preferences.getBoolean("notify", true)){
		 try {
			notifyname=CheckName();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 
		 if(notifyname.size()>0){
			 StringBuilder nn=new StringBuilder();
			 for(int i=0;i<notifyname.size();i++){
				 nn.append(notifyname.get(i)+"、");
			 }
//			 nn.append(notifyname.get(notifyname.size()-1));
			 dialog1(nn.toString());
		 }
		}
		
		
	}

//	获取25天内没有检测的用户名称
	private ArrayList<String> CheckName() throws ParseException {
		// TODO Auto-generated method stub
		ArrayList<String> result=new ArrayList<String>();
		Date date2;
		
		Date d = new Date();  
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
        String dateNowStr = sdf.format(d);
      
		OperateDAO dao = new OperateDAO(MainActivity.this);
		
		List<TestItem> vodItems = dao.getTestItemsfamily(20);
		HashMap<String,String> hash=new HashMap<String,String>();
		for(int i=0;i<vodItems.size();i++){
			if(!hash.containsKey(vodItems.get(i).getName())){
				hash.put(vodItems.get(i).getName(), vodItems.get(i).getDate());
			}else{
				date2=sdf.parse(vodItems.get(i).getDate());
				if(date2.after(d)){
					hash.put(vodItems.get(i).getName(), vodItems.get(i).getDate());
				}
			}
		}
		   Iterator iter = hash.entrySet().iterator();
	       while (iter.hasNext()) {
		  Map.Entry entry = (Map.Entry) iter.next();
		  System.out.println("加入result"+(String)entry.getKey()+(String)entry.getValue());
	      if(CompareDates((String)entry.getValue(),dateNowStr)){
	    	  if(!result.contains((String)entry.getKey())){
	    	  result.add((String)entry.getKey());}
	    	 
	      }
		  }
	       
		
		List<TestItem> vodItems1 = dao.getTestItemsfriend(20);
		for(int i=0;i<vodItems1.size();i++){
			if(!hash.containsKey(vodItems1.get(i).getName())){
				hash.put(vodItems1.get(i).getName(), vodItems1.get(i).getDate());
			}else{
				date2=sdf.parse(vodItems1.get(i).getDate());
				if(date2.after(d)){
					hash.put(vodItems1.get(i).getName(), vodItems1.get(i).getDate());
				}
			}
		}
		   Iterator iter1 = hash.entrySet().iterator();
	       while (iter1.hasNext()) {
		  Map.Entry entry = (Map.Entry) iter1.next();
	      if(CompareDates((String)entry.getValue(),dateNowStr)){
	    	  if(!result.contains((String)entry.getKey())){
		    	  result.add((String)entry.getKey());}
	      }
		  }
	       
	       
		List<TestItem> vodItems2 = dao.getTestItemscolleague(20);
		for(int i=0;i<vodItems2.size();i++){
			if(!hash.containsKey(vodItems2.get(i).getName())){
				hash.put(vodItems2.get(i).getName(), vodItems2.get(i).getDate());
			}else{
				date2=sdf.parse(vodItems2.get(i).getDate());
				if(date2.after(d)){
					hash.put(vodItems2.get(i).getName(), vodItems2.get(i).getDate());
				}
			}
		}
		   Iterator iter2 = hash.entrySet().iterator();
	       while (iter2.hasNext()) {
		  Map.Entry entry = (Map.Entry) iter2.next();
	      if(CompareDates((String)entry.getValue(),dateNowStr)){
	    	  if(!result.contains((String)entry.getKey())){
		    	  result.add((String)entry.getKey());}
	      }
		  }
		
		
		
		List<TestItem> vodItems3 = dao.getTestItems(20);
		for(int i=0;i<vodItems3.size();i++){
			if(!hash.containsKey(vodItems3.get(i).getName())){
				hash.put(vodItems3.get(i).getName(), vodItems3.get(i).getDate());
			}else{
				date2=sdf.parse(vodItems3.get(i).getDate());
				if(date2.after(d)){
					hash.put(vodItems3.get(i).getName(), vodItems3.get(i).getDate());
				}
			}
		}
		   Iterator iter3 = hash.entrySet().iterator();
	       while (iter3.hasNext()) {
		  Map.Entry entry = (Map.Entry) iter3.next();
	      if(CompareDates((String)entry.getValue(),dateNowStr)){
	    	  if(!result.contains((String)entry.getKey())){
		    	  result.add((String)entry.getKey());}
	      }
		  }
		
		return result;
	}

//  比较两个日期查是否超过25天
	private boolean CompareDates(String tempdate, String dateNowStr) {
	// TODO Auto-generated method stub
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try
		{
		Date d1 = df.parse(tempdate);
		Date d2 = df.parse(dateNowStr);
		long diff = d1.getTime() - d2.getTime();
		if(diff<0) diff=-diff;
		long days = diff / (1000 * 60 * 60 * 24);
		if(days>25){
			return true;
		}
		}
		catch (Exception e)
		{
			
		}
	return false;
}

	public boolean isNetworkConnected(Context context) {  
	     if (context != null) {  
	         ConnectivityManager mConnectivityManager = (ConnectivityManager) context  
	                 .getSystemService(Context.CONNECTIVITY_SERVICE);  
	         NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();  
	         if (mNetworkInfo != null) {  
	             return mNetworkInfo.isAvailable();  
	         }  
	     }  
	     return false;  
	 }
	
	public void OpenRightMenu(View view)
	{
		mDrawerLayout.openDrawer(Gravity.RIGHT);
//		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED,Gravity.RIGHT);
	}
	


	private void initEvents()
	{
		mDrawerLayout.setDrawerListener(new DrawerListener()
		{
			@Override
			public void onDrawerStateChanged(int newState)
			{
			}

			@Override
			public void onDrawerSlide(View drawerView, float slideOffset)
			{
				View mContent = mDrawerLayout.getChildAt(0);
				View mMenu = drawerView;
				float scale = 1 - slideOffset;
				float rightScale = 0.8f + scale * 0.2f;

				if (drawerView.getTag().equals("LEFT"))
			{

					/*float leftScale = 1 - 0.3f * scale;

					ViewHelper.setScaleX(mMenu, leftScale);
					ViewHelper.setScaleY(mMenu, leftScale);
					ViewHelper.setAlpha(mMenu, 0.6f + 0.4f * (1 - scale));
					ViewHelper.setTranslationX(mContent,
							mMenu.getMeasuredWidth() * (1 - scale));
					ViewHelper.setPivotX(mContent, 0);
					ViewHelper.setPivotY(mContent,
							mContent.getMeasuredHeight() / 2);
					mContent.invalidate();
					ViewHelper.setScaleX(mContent, rightScale);
					ViewHelper.setScaleY(mContent, rightScale);*/
				} else
				{
					ViewHelper.setTranslationX(mContent,
							-mMenu.getMeasuredWidth() * slideOffset);
					ViewHelper.setPivotX(mContent, mContent.getMeasuredWidth());
					ViewHelper.setPivotY(mContent,
							mContent.getMeasuredHeight() /2);
					mContent.invalidate();
					ViewHelper.setScaleX(mContent, rightScale);
					ViewHelper.setScaleY(mContent, rightScale);
				}

			}

			@Override
			public void onDrawerOpened(View drawerView)
			{
			}

			@Override
			public void onDrawerClosed(View drawerView)
			{
//				mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
			}
		});
	}

	private void initView()
	{
		mDrawerLayout = (DrawerLayout) findViewById(R.id.id_drawerLayout);
//		mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED,Gravity.RIGHT);
		//初始化控仿
//				btnPhotos = (Button) findViewById(R.id.btn_photos);
//				btnTakephoto = (Button) findViewById(R.id.btn_takephoto);
//				btnPhotos.setOnClickListener(this);
//				btnTakephoto.setOnClickListener(this);
				ivHead = (CircleImageView) findViewById(R.id.civ);
				Bitmap bt = BitmapFactory.decodeFile(path + "head.jpg");//从Sd中找头像，转换成Bitmap
				if(bt!=null){
					@SuppressWarnings("deprecation")
					Drawable drawable = new BitmapDrawable(bt);//转换成drawable
					ivHead.setImageDrawable(drawable);
				}else{
					/**
					 *	如果SD里面没有则需要从服务器取头像，取回来的头像再保存在SD丿
					 * 
					 */
				}
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
	
	  
	/*		 
	public void onClick(View v) {
		if(v.getId()==R.id.civ){
			Intent intent=new Intent(MainActivity.this, HomeCenter.class);
		    startActivity(intent);
		}
		switch (v.getId()) {
		case R.id.btn_photos://从相册里面取照片
			Intent intent1 = new Intent(Intent.ACTION_PICK, null);
			intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
			startActivityForResult(intent1, 1);
			break;
		case R.id.btn_takephoto://调用相机拍照
			Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			intent2.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
							"head.jpg")));
			startActivityForResult(intent2, 2);//采用ForResult打开
			break;
		default:
			break;
		}
		
      }	*/


	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1:
			if (resultCode == RESULT_OK) {
				cropPhoto(data.getData());//裁剪图片
			}

			break;
		case 2:
			if (resultCode == RESULT_OK) {
				File temp = new File(Environment.getExternalStorageDirectory()
						+ "/head.jpg");
				cropPhoto(Uri.fromFile(temp));//裁剪图片
			}

			break;
		case 3:
			if (data != null) {
				Bundle extras = data.getExtras();
				head = extras.getParcelable("data");
				if(head!=null){
					/**
					 * 上传服务器代砿
					 */
					setPicToView(head);//保存在SD卡中
					ivHead.setImageBitmap(head);//用ImageView显示出来
				}
			}else return;
			break;
		default:
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	};
	/**
	 * 调用系统的裁剿
	 * @param uri
	 */
	public void cropPhoto(Uri uri) {
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		 // aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX outputY 是裁剪图片宽髿
		intent.putExtra("outputX", 150);
		intent.putExtra("outputY", 150);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 3);
	}
	private void setPicToView(Bitmap mBitmap) {
		 String sdStatus = Environment.getExternalStorageState();  
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 棿Ջsd是否可用  
               return;  
           }  
		FileOutputStream b = null;
		File file = new File(path);
		file.mkdirs();// 创建文件夿
		String fileName =path + "head.jpg";//图片名字
		try {
			b = new FileOutputStream(fileName);
			mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文仿
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				//关闭浿
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
}
	
    private void dialog1(String names){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
        builder.setTitle("金络康宝提醒您"); //设置标题
        builder.setIcon(R.drawable.yingyang);
        builder.setMessage("客户: "+names+"需要重新检测驱体运行状态，以调整膳食方案"); //设置内容
//        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() { //设置确定按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 dialog.dismiss(); //关闭dialog
            }
        });
        

        //参数都设置完成了，创建并显示出来
        builder.create().show();
    }
	
}
