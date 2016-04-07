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
///////////////////ͼ�����ñ���
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
	
	int nullnumber=0;//�����Ŀ�·ֵ
    int picindex=1;
    Bundle bundle;
    String category,date;
    SQLiteDatabase database=null;
    boolean wasread=false;//����ǰ����״̬��ǰһ��״̬
    int sourceid;
	SharedPreferences preferences=null;
	private boolean sound; 
///////////////����ָʾͼƬ
	private Handler mHandler = new Handler()  
    {  
        public void handleMessage(android.os.Message msg)  
        {  
           if(msg.what==1){//��ǰ��ҳ
        	  
         	   if(picindex<0){
            	   picindex=0;
               }
        	   image1.setImageResource(src[picindex]);
        	   picindex--;
           }
            if(msg.what==2){//���ҳ
            	 
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
	    System.out.println("���յ���������       "+date);
		
	  
		
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
		
		tvDeviceStatus.setText("׼��");
		
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
		
// ��ȡ���ݣ����ƾɵ�ͼ��     Setting Timer to Draw and Save data
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
		
/////////////////��ͼ��
		context = getApplicationContext();

		// ������main�����ϵĲ��֣�������ͼ���������������
		LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout11);

		// ������������������ϵ����е㣬��һ����ļ��ϣ�������Щ�㻭������
		series = new XYSeries(title);

		// ����һ�����ݼ���ʵ����������ݼ�������������ͼ��
		mDataset = new XYMultipleSeriesDataset();

		// ���㼯��ӵ�������ݼ���
		mDataset.addSeries(series);

		// ���¶������ߵ���ʽ�����Եȵȵ����ã�renderer�൱��һ��������ͼ������Ⱦ�ľ��
		int color = Color.YELLOW;
		PointStyle style = PointStyle.CIRCLE;
		renderer = buildRenderer(color, style, true);

		// ���ú�ͼ�����ʽ
		setChartSettings(renderer, "X", "Y", 0, 120, 120, 300, Color.BLACK,
				Color.BLACK);

		// ����ͼ��
		chart = ChartFactory.getLineChartView(context, mDataset, renderer);

		// ��ͼ����ӵ�������ȥ
		layout.addView(chart, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));

		// �����Handlerʵ������������Timerʵ������ɶ�ʱ����ͼ��Ĺ���
		handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				// ˢ��ͼ��
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
//				                    ÿ�ζ����Ŀ�ֵ��һ����ʱ��230����ʱ����180���⵼��û�нӴ�Ƥ��Ҳ����
					    
//					    System.out.println("�µ�nullnumber   "+nullnumber);
					    }else if(Math.abs(Integer.parseInt((String)msg.obj)-nullnumber)>10 ){//�Ϳ�ֵ��һ������ʼ��ȡ����
					    	while(oldvalue==0){
					    		 oldvalue=Integer.parseInt((String)msg.obj);
//					    		 System.out.println("�µ�oldvalue   "+oldvalue);
					    		}
//					    	else{
					    			if(Math.abs(oldvalue-Integer.parseInt((String)msg.obj))<5 & !wasread  )
					    			{   
					    				//���������仯����Ӱ��
					    				tvDeviceStatus.setText("���ڽ�������");
					    				updateChart(Integer.parseInt((String)msg.obj),1);
//					    				System.out.println("��¼��ֵ��   "+Integer.parseInt((String)msg.obj));
					    				oldvalue=Integer.parseInt((String)msg.obj);
//					    				System.out.println("�µ�oldvalue��"+oldvalue);
					    			}else if(Math.abs(oldvalue-Integer.parseInt((String)msg.obj))>5 & !wasread){ 
					    				//��ȡ��ֵͻȻ�仯���򲻶�ȡ����
					    				tvDeviceStatus.setText("׼��");
//					    				nullnumber=0;
					    				oldvalue=0;
					    				wasread=true;
					    				series.clear();
					    	        	mDataset.clear();
//					    	                              ��;�Ͽ��������Ѩλ֮ǰ���������	
					    	        	updateChart(0,0);
//					    				System.out.println("����oldvalueΪ  0 ");
					    			}else if(Math.abs(oldvalue-Integer.parseInt((String)msg.obj))>5 & wasread){
//					    			
					    				wasread=false;
					    				tvDeviceStatus.setText("���¿�ʼ����");
					    				updateChart(Integer.parseInt((String)msg.obj),1);
//					    				System.out.println("��¼��ֵ��   "+Integer.parseInt((String)msg.obj));
					    				oldvalue=Integer.parseInt((String)msg.obj);
					    			}
//					    		}
						}else{
							tvDeviceStatus.setText("׼����ʼ");
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
//								msg.obj = new String("��ǰ������"+ttt +"");	
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

		// ����ͼ�������߱������ʽ��������ɫ����Ĵ�С�Լ��ߵĴ�ϸ��
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
		// �йض�ͼ�����Ⱦ�ɲο�api�ĵ�
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
		if(notify==1){  //1Ϊ�����������ݣ�0��Ϊ���²�����ǰѨλ

	        addY=yy;
	       
	        int t=candidates.size();
	        int result=0;
	        
	        if(t<120){
	        	candidates.add(yy);
	        	
	        	// ���ú���һ����Ҫ���ӵĽڵ�
	    		// �Ƴ����ݼ��оɵĵ㼯
	    		mDataset.removeSeries(series);
	    		//�жϵ�ǰ�㼯�е����ж��ٵ㣬��Ϊ��Ļ�ܹ�ֻ������100�������Ե���������100ʱ��������Զ��100
	    		int length = series.getItemCount();
	    		if (length > 120) {
	    			length = 120;
	    		}
	    		// ���ɵĵ㼯��x��y����ֵȡ��������backup�У����ҽ�x��ֵ��1�������������ƽ�Ƶ�Ч��
	    		for (int i = 0; i < length; i++) {
	    			xv[i] = (int) series.getX(i) + 1;
	    			yv[i] = (int) series.getY(i);
	    		}
	    		// �㼯����գ�Ϊ�������µĵ㼯��׼��
	    		series.clear();

	    		// ���²����ĵ����ȼ��뵽�㼯�У�Ȼ����ѭ�����н�����任���һϵ�е㶼���¼��뵽�㼯��
	    		// �����������һ�°�˳��ߵ�������ʲôЧ������������ѭ���壬������²����ĵ�
	    		series.add(addX, addY);
	    		for (int k = 0; k < length; k++) {
	    			series.add(xv[k], yv[k]);
	    		}
	    		// �����ݼ�������µĵ㼯
	    		mDataset.addSeries(series);

	    		// ��ͼ���£�û����һ�������߲�����ֶ�̬
	    		// ����ڷ�UI���߳��У���Ҫ����postInvalidate()������ο�api
	    		chart.invalidate();
	    		
	        }else{
	        	
	        	series.clear();
	        	mDataset.clear();
	        	
	        	Message progressbarmsg=new Message();//���Ľ�����
	        	progressbarmsg.what=0x10001;
	        	progressbarhandler.sendMessage(progressbarmsg);
	        	
//	        	������һ��Ѩλ��������
                 if(sound){
                	 if(times>0){
                	 soundPool.play(sourceid, 0.5f, 0.5f, 0, 1, 1);
                	 times--;
                	 }
                 }
	        	
	        	 result=average(candidates);
	        	 tobesend.add(result);
	        	 System.out.println("��ƽ���õ��Ľ����"+ result);
	        	 candidates.clear();
	        	 StringBuilder sb=new StringBuilder();
	        	 
	        	 
	        	 Message msg=new Message();//����ͼƬָʾ
	        	 msg.what=2;
	        	 mHandler.sendMessage(msg);
	        	 
	        	 if(tobesend.size()==24){
	        		 for(int i=0;i<24;i++){
	        			 System.out.println("�����͵�ֵ�ǣ�������������"+tobesend.get(i));
	        			 sb.append(tobesend.get(i)+"%");
	        		 }
	        		 String data1=sb.toString();
	        		 
	        		  DBOpenHelper helper=new DBOpenHelper(RTMonitorActivity.this,DBInfoConfig.DB_NAME);	
	        	      database=helper.getReadableDatabase();
	        	      String[] dataparams={data1,date};
	        	      
	        	      if(category.equalsIgnoreCase("����")){
	        	    	  database.execSQL(DBInfoConfig.UPDATE_DATA_FAMILY, dataparams);
	        	      }else if(category.equalsIgnoreCase("����")){
	        	    	  database.execSQL(DBInfoConfig.UPDATE_DATA_FRIEND, dataparams);
	        	      }else if(category.equalsIgnoreCase("ͬ��")){
	        	    	  database.execSQL(DBInfoConfig.UPDATE_DATA_COLLEAGUE, dataparams);
	        	      }else {//�ͻ�
	        	    	  database.execSQL(DBInfoConfig.UPDATE_DATA_CLIENT, dataparams);
	        	      }
	        	      
	        		 Intent intent=new Intent(RTMonitorActivity.this,ShowList.class);
	        		 startActivity(intent);
	        		 
	        	 }
//	           nullnumber=0;
//	           oldvalue=0;
	        }
	                                           
			
//		�����Ѩλ֮ǰ�洢������
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
