package com.pkusz.bluetooth;

import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.example.jinluokb.NewTest;
import com.example.jinluokb.R;
import com.pkusz.analyze.ShowList;
import com.pkusz.constants.DBInfoConfig;
import com.pkusz.dao.DBOpenHelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;
import android.view.View;
import android.view.SurfaceView;
import android.view.Window;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;


public class RTMonitorActivity extends Activity{
	
	
	private Button tb1;
	private Button previous;
	private ImageView image1;
	private ProgressBar rectangleProgressBar;
	protected static final int STOP = 0x10000;  
    protected static final int NEXT = 0x10001;  
    private int iCount = 1;  
    int oldvalue=0;
    int newvalue=0;
	 
	int[] src={R.drawable.xw1,R.drawable.xw2,R.drawable.xw3,R.drawable.xw4,R.drawable.xw5,R.drawable.xw6,R.drawable.xw7,R.drawable.xw8
			,R.drawable.xw9,R.drawable.xw10,R.drawable.xw11,R.drawable.xw12,R.drawable.xw13,R.drawable.xw14,R.drawable.xw15,R.drawable.xw16
			,R.drawable.xw17,R.drawable.xw18,R.drawable.xw19,R.drawable.xw20,R.drawable.xw21,R.drawable.xw22,R.drawable.xw23
			,R.drawable.xw24};
//	int tt;

	List<Integer> candidates=new ArrayList<Integer>();
	List<Integer> tobesend=new ArrayList<Integer>();
	private SoundPool soundPool;
	
	private String dataStr = new String();
	private boolean enRead = false;
	public TextView tvDeviceName;
	public static TextView tvDeviceStatus;
	private SurfaceView sfvECG;
	
	private BTReadThread mReadThread = new BTReadThread(50);
	private Handler msgHandler;
	private DrawECGWaveForm mECGWF;
	private String revTmpStr = new String();
	public List<Float> ECGDataList = new ArrayList<Float>();
	public boolean ECGDLIsAvailable = true;
	private float ECGData = 0;
///////////////////图表所用变量
	private Timer timer = new Timer();
	private TimerTask task;
	private Handler handler;
	private String title = "Signal Strength";
	private XYSeries series;
	private XYMultipleSeriesDataset mDataset;
	private GraphicalView chart;
	private XYMultipleSeriesRenderer renderer;
	private Context context;
	private int addX = -1, addY;
	private int times=24;

	int[] xv = new int[120];
	int[] yv = new int[120];
	
	int nullnumber=0;//仪器的开路值
    int picindex=1;
    Bundle bundle;
    String category,date;
    SQLiteDatabase database=null;
    boolean wasread=false;//代表当前读数状态的前一个状态
    int sourceid;
	SharedPreferences preferences=null;
	private boolean sound; 
///////////////更改指示图片
	private Handler mHandler = new Handler()  
    {  
        public void handleMessage(android.os.Message msg)  
        {  
           if(msg.what==1){//向前翻页
        	  
         	   if(picindex<0){
            	   picindex=0;
               }
        	   image1.setImageResource(src[picindex]);
        	   picindex--;
           }
            if(msg.what==2){//向后翻页
            	 
            	if(picindex>23){
             	   picindex=23;
                }            	
            	image1.setImageResource(src[picindex]);
            	picindex++;
            	
           } 
           
            
        }  
    }; 
////////////////////////
    private Handler progressbarhandler = new Handler(){  
        public void handleMessage(Message msg){  
            switch (msg.what) {  
            case STOP:  
                rectangleProgressBar.setVisibility(View.GONE);                 
                break;  
            case NEXT:  
            	if(iCount<24 || iCount>=00){
                rectangleProgressBar.setProgress(iCount); 
                iCount++;
            	}else{
            		iCount=1;
            	}
              
                break;  
            }  
        }  
    };

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.rtmonitor);
		previous=(Button) findViewById(R.id.button2);
		tb1=(Button) findViewById(R.id.button1);
		image1=(ImageView) findViewById(R.id.imageView1);
		
	 	 rectangleProgressBar = (ProgressBar)findViewById(R.id.rectangleProgressBar);  
		 rectangleProgressBar.setIndeterminate(false); 
		 rectangleProgressBar.setVisibility(View.VISIBLE);             
         rectangleProgressBar.setMax(24);  
         rectangleProgressBar.setProgress(0);  
		
     	soundPool= new SoundPool(10,AudioManager.STREAM_SYSTEM,5);
   	    sourceid = soundPool.load(this, R.raw.bee, 0);
		
   	 preferences=getSharedPreferences("userinfo",0);
	 sound=preferences.getBoolean("sound", true);
	 
	    bundle = this.getIntent().getExtras(); 
	    category=bundle.getString("category");
	    date=bundle.getString("date");
	    System.out.println("接收到的日期是       "+date);
		
	  
		
		tb1.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message ms=new Message();
				ms.what=2;
				mHandler.sendMessage(ms);

			}
		});
		previous.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Message ms=new Message();
				ms.what=1;
				mHandler.sendMessage(ms);

			}
		});
		
		
		// Init Resources
		tvDeviceName = (TextView)findViewById(R.id.tvDeviceName);
		tvDeviceStatus = (TextView)findViewById(R.id.tvDeviceStatus);
//		sfvECG = (SurfaceView)findViewById(R.id.sfvECG);
		mECGWF = new DrawECGWaveForm(sfvECG);
		
		tvDeviceStatus.setText("准备");
		
		try{
			if(NewTest.mBTSocket.getInputStream()!=null)
			{
				enRead = true;
				mReadThread.start();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		Looper lp = Looper.myLooper();
		msgHandler = new MsgHandler(lp);
		
// 获取数据，绘制旧的图表     Setting Timer to Draw and Save data
//		Timer mDSTimer = new Timer();
//		TimerTask mDSTask = new TimerTask(){
//			public void run(){
//				Message msg = Message.obtain();
//				msg.what = 1;
//				msgHandler.sendMessage(msg);
//			}
//		};
		
		// Set Timer
//		mDSTimer.schedule(mDSTask,1000,100);
		
/////////////////新图表
		context = getApplicationContext();

		// 这里获得main界面上的布局，下面会把图表画在这个布局里面
		LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout11);

		// 这个类用来放置曲线上的所有点，是一个点的集合，根据这些点画出曲线
		series = new XYSeries(title);

		// 创建一个数据集的实例，这个数据集将被用来创建图表
		mDataset = new XYMultipleSeriesDataset();

		// 将点集添加到这个数据集中
		mDataset.addSeries(series);

		// 以下都是曲线的样式和属性等等的设置，renderer相当于一个用来给图表做渲染的句柄
		int color = Color.YELLOW;
		PointStyle style = PointStyle.CIRCLE;
		renderer = buildRenderer(color, style, true);

		// 设置好图表的样式
		setChartSettings(renderer, "X", "Y", 0, 120, 120, 300, Color.BLACK,
				Color.BLACK);

		// 生成图表
		chart = ChartFactory.getLineChartView(context, mDataset, renderer);

		// 将图表添加到布局中去
		layout.addView(chart, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));

		// 这里的Handler实例将配合下面的Timer实例，完成定时更新图表的功能
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// 刷新图表
				addY = (int) (Math.random() * 300);
//				updateChart(tt);
				super.handleMessage(msg);
			}
		};

		task = new TimerTask() {
			@Override
			public void run() {
				Message message = new Message();
				message.what = 0;
				handler.sendMessage(message);
			}
		};

//		timer.schedule(task, 100, 200);

	
	}
	
	
//	@Override
//	public void onDestroy()  
//    {   
//		timer.cancel();
//		//Close Socket
//		try {
//			enRead = false;
//			NewTest.mBTSocket.close();
//			mReadThread.destroy();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//        super.onDestroy();  
//    }
	
//	@Override
//	public void onStop(){		
//		mReadThread.stop();
//		super.onStop();
//	}
//	
//	@Override
//	public void onResume(){
//		super.onResume();
//		mReadThread.resume();
//	}
	
	
	
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
//		timer.cancel();
		//Close Socket
		try {
			enRead = false;
			NewTest.mBTSocket.close();
			mReadThread.destroy();
		} catch (IOException e) {
			e.printStackTrace();
		}
        super.onDestroy();  
	}


	// MsgHandler class to Update UI
	class MsgHandler extends Handler{
		public MsgHandler(Looper lp){
			super(lp);
		}
		
		@Override
		public void handleMessage(Message msg){
			switch(msg.what){
			case 0:
//				tvDeviceStatus.setText((String)msg.obj);
				
				if(NewTest.mBTSocket!=null)
					try{
//					    tvDeviceName.setText(NewTest.mBTSocket.getRemoteDevice().getName());
						if(nullnumber==0){
					    nullnumber=Integer.parseInt((String)msg.obj);
//				                    每次读到的空值不一样有时是230，有时候是180，这导致没有接触皮肤也在跑
					    
//					    System.out.println("新的nullnumber   "+nullnumber);
					    }else if(Math.abs(Integer.parseInt((String)msg.obj)-nullnumber)>10 ){//和空值不一样，开始读取数据
					    	while(oldvalue==0){
					    		 oldvalue=Integer.parseInt((String)msg.obj);
//					    		 System.out.println("新的oldvalue   "+oldvalue);
					    		}
//					    	else{
					    			if(Math.abs(oldvalue-Integer.parseInt((String)msg.obj))<5 & !wasread  )
					    			{   
					    				//数据连续变化，不影响
					    				tvDeviceStatus.setText("正在接受数据");
					    				updateChart(Integer.parseInt((String)msg.obj),1);
//					    				System.out.println("记录的值是   "+Integer.parseInt((String)msg.obj));
					    				oldvalue=Integer.parseInt((String)msg.obj);
//					    				System.out.println("新的oldvalue是"+oldvalue);
					    			}else if(Math.abs(oldvalue-Integer.parseInt((String)msg.obj))>5 & !wasread){ 
					    				//读取的值突然变化，则不读取数据
					    				tvDeviceStatus.setText("准备");
//					    				nullnumber=0;
					    				oldvalue=0;
					    				wasread=true;
					    				series.clear();
					    	        	mDataset.clear();
//					    	                              中途断开，清除该穴位之前保存的数据	
					    	        	updateChart(0,0);
//					    				System.out.println("设置oldvalue为  0 ");
					    			}else if(Math.abs(oldvalue-Integer.parseInt((String)msg.obj))>5 & wasread){
//					    			
					    				wasread=false;
					    				tvDeviceStatus.setText("重新开始读数");
					    				updateChart(Integer.parseInt((String)msg.obj),1);
//					    				System.out.println("记录的值是   "+Integer.parseInt((String)msg.obj));
					    				oldvalue=Integer.parseInt((String)msg.obj);
					    			}
//					    		}
						}else{
							tvDeviceStatus.setText("准备开始");
							wasread=false;
							oldvalue=0;
						}
					}
					catch(Exception e){
						e.printStackTrace();
					}
					break;
			}

		}
	}			
	// Bluetooth Reading data thread
	// Bluetooth Reading data thread
		class BTReadThread extends Thread{
			private int wait = 5;// Time to wait
			public BTReadThread(int wait){
				this.wait = wait;
			}

				
			public void run(){
				while(enRead){
					try{
						if (NewTest.mBTSocket.getInputStream()!=null)
						{
							NewTest.mBTSocket.getOutputStream().write(48);						
							byte[] tmp = new byte[512];
							int len = NewTest.mBTSocket.getInputStream().read(tmp,0,512);
							if (len > 0){
								
								byte[] tmp2 = new byte[len];
								tmp2 = tmp;
								String str = new String(tmp2);
								revTmpStr = revTmpStr + str;
								
								
//								for(int i=0;i<16;i++){
//									   Random ran=new Random();
//									   int ff=ran.nextInt();
//									   if(ff<0) ff=-ff;
									   
									   int tt= tmp[0]&0xFF;
//									   System.out.print(tmp[0]&0xFF);
									   float ff=Float.parseFloat(tt+"");
//									   ECGDataList.add(Float.parseFloat(ff+""));
//									   updateChart(tt);
//									}
								
//								Update UI
								Message msg = Message.obtain();
								msg.what = 0;
								int ttt=tmp[0]&0xFF;
//								msg.obj = new String("当前数据是"+ttt +"");	
								msg.obj=ttt+"";
								msgHandler.sendMessage(msg);
								
							}
						}
//						Thread.sleep(wait);
					}
					catch(Exception e){
						e.printStackTrace();
					}
				}
			}
		}
	
	

	protected XYMultipleSeriesRenderer buildRenderer(int color,
			PointStyle style, boolean fill) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();

		// 设置图表中曲线本身的样式，包括颜色、点的大小以及线的粗细等
		XYSeriesRenderer r = new XYSeriesRenderer();
		r.setColor(color);
		r.setPointStyle(style);
		r.setFillPoints(fill);
		r.setLineWidth(3);
		renderer.addSeriesRenderer(r);

		return renderer;
	}

	protected void setChartSettings(XYMultipleSeriesRenderer renderer,
			String xTitle, String yTitle, double xMin, double xMax,
			double yMin, double yMax, int axesColor, int labelsColor) {
		// 有关对图表的渲染可参看api文档
		renderer.setChartTitle(title);
		renderer.setXTitle(xTitle);
		renderer.setYTitle(yTitle);
		renderer.setXAxisMin(xMin);
		renderer.setXAxisMax(xMax);
		renderer.setYAxisMin(yMin);
		renderer.setYAxisMax(yMax);
		renderer.setAxesColor(axesColor);
		renderer.setLabelsColor(labelsColor);
		renderer.setShowGrid(true);
		renderer.setGridColor(Color.WHITE);
		renderer.setXLabels(20);
		renderer.setYLabels(10);
		renderer.setXTitle("Time");
		renderer.setYTitle("dBm");
		renderer.setYLabelsAlign(Align.RIGHT);
		renderer.setPointSize((float) 2);
		renderer.setShowLegend(false);
		renderer.setZoomEnabled(true);
		renderer.setZoomButtonsVisible(true);
	}

	private void updateChart(int yy,int notify) {
		if(notify==1){  //1为正常更新数据，0则为重新测量当前穴位

	        addY=yy;
	       
	        int t=candidates.size();
	        int result=0;
	        
	        if(t<120){
	        	candidates.add(yy);
	        	
	        	// 设置好下一个需要增加的节点
	    		// 移除数据集中旧的点集
	    		mDataset.removeSeries(series);
	    		//判断当前点集中到底有多少点，因为屏幕总共只能容纳100个，所以当点数超过100时，长度永远是100
	    		int length = series.getItemCount();
	    		if (length > 120) {
	    			length = 120;
	    		}
	    		// 将旧的点集中x和y的数值取出来放入backup中，并且将x的值加1，造成曲线向右平移的效果
	    		for (int i = 0; i < length; i++) {
	    			xv[i] = (int) series.getX(i) + 1;
	    			yv[i] = (int) series.getY(i);
	    		}
	    		// 点集先清空，为了做成新的点集而准备
	    		series.clear();

	    		// 将新产生的点首先加入到点集中，然后在循环体中将坐标变换后的一系列点都重新加入到点集中
	    		// 这里可以试验一下把顺序颠倒过来是什么效果，即先运行循环体，再添加新产生的点
	    		series.add(addX, addY);
	    		for (int k = 0; k < length; k++) {
	    			series.add(xv[k], yv[k]);
	    		}
	    		// 在数据集中添加新的点集
	    		mDataset.addSeries(series);

	    		// 视图更新，没有这一步，曲线不会呈现动态
	    		// 如果在非UI主线程中，需要调用postInvalidate()，具体参考api
	    		chart.invalidate();
	    		
	        }else{
	        	
	        	series.clear();
	        	mDataset.clear();
	        	
	        	Message progressbarmsg=new Message();//更改进度条
	        	progressbarmsg.what=0x10001;
	        	progressbarhandler.sendMessage(progressbarmsg);
	        	
//	        	测量完一个穴位播放声音
                 if(sound){
                	 if(times>0){
                	 soundPool.play(sourceid, 0.5f, 0.5f, 0, 1, 1);
                	 times--;
                	 }
                 }
	        	
	        	 result=average(candidates);
	        	 tobesend.add(result);
	        	 System.out.println("求平均得到的结果是"+ result);
	        	 candidates.clear();
	        	 StringBuilder sb=new StringBuilder();
	        	 
	        	 
	        	 Message msg=new Message();//更改图片指示
	        	 msg.what=2;
	        	 mHandler.sendMessage(msg);
	        	 
	        	 if(tobesend.size()==24){
	        		 for(int i=0;i<24;i++){
	        			 System.out.println("待发送的值是！！！！！！！"+tobesend.get(i));
	        			 sb.append(tobesend.get(i)+"%");
	        		 }
	        		 String data1=sb.toString();
	        		 
	        		  DBOpenHelper helper=new DBOpenHelper(RTMonitorActivity.this,DBInfoConfig.DB_NAME);	
	        	      database=helper.getReadableDatabase();
	        	      String[] dataparams={data1,date};
	        	      
	        	      if(category.equalsIgnoreCase("家人")){
	        	    	  database.execSQL(DBInfoConfig.UPDATE_DATA_FAMILY, dataparams);
	        	      }else if(category.equalsIgnoreCase("朋友")){
	        	    	  database.execSQL(DBInfoConfig.UPDATE_DATA_FRIEND, dataparams);
	        	      }else if(category.equalsIgnoreCase("同事")){
	        	    	  database.execSQL(DBInfoConfig.UPDATE_DATA_COLLEAGUE, dataparams);
	        	      }else {//客户
	        	    	  database.execSQL(DBInfoConfig.UPDATE_DATA_CLIENT, dataparams);
	        	      }
	        	      
	        		 Intent intent=new Intent(RTMonitorActivity.this,ShowList.class);
	        		 startActivity(intent);
	        		 
	        	 }
//	           nullnumber=0;
//	           oldvalue=0;
	        }
	                                           
			
//		清除该穴位之前存储的数据
		}else{
			candidates.clear();
			series.clear();
        	mDataset.clear();
		}
	}

	
	
	private int average(List<Integer> candidates2) {
		// TODO Auto-generated method stub
		int total=0;
		int i=0;
		while(i<candidates2.size()){
			total+=candidates2.get(i);
			i++;
		}
		return total/120;
	}
	
}
