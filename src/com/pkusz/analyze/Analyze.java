package com.pkusz.analyze;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

import com.example.insurance.Insurance;
import com.example.insurance.InsuranceWeb;
import com.example.jinluokb.LoginActivity;
import com.example.jinluokb.R;
import com.pkusz.constants.WebInfoConfig;

/**

 *
 * @see SystemUiHider
 */
public class Analyze extends Activity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */

    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
//    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
//    private SystemUiHider mSystemUiHider;
    
    private TextView tv121,tv122,tv123,tv131,tv132,tv133,tv221,tv222,tv223,tv231,tv232,tv233;
	private TextView tv321,tv322,tv323,tv331,tv332,tv333,tv421,tv422,tv423,tv431,tv432,tv433;
	String[] list1=new String[12];
    String[] list2=new String[12];
    
    private View controlsView ;
    private Button btnConnectToServer;
    private Button history;
//    private Button btnDisconnect;
//    private Button btnSend;
//    private Button btnAddData;
//    private EditText txtSend;
//    private EditText txtData;
//    private TextView lblReceive;
//    private Button btnsymptom;
    private Button btnorgans;
    private SharedPreferences shared;
    
    String marriage="未婚",age="25",male="男",
    	   data="50%50%";
    
    private Socket clientSocket = null;
    private OutputStream outStream = null;

    private Handler mHandler = null;
    private Handler organHandler = null;    

    private ReceiveThread mReceiveThread = null;
    private connectThread mConnectThread=null;
    private boolean stop = true;
    private String strReceive="";
    
    private yueeReceiveThread yueeReceiveThread = null;
    private yueeconnectThread yueeConnectThread=null;
    private boolean yueestop = true;
    private String yueestr="";
    private Socket yueeclientSocket = null;
    private OutputStream yueeoutStream = null;
	private Handler yueeHandler= null;
    
    String[] organresult={"","","","","","","","","","","",""};
    String chinesemed="%正常%正常%正常%正常%正常%正常%正常%正常%正常%正常%正常%正常";//中医评价
//    String[] result={"正常","正常","正常","正常","正常","正常","正常","正常","正常","正常","正常","正常"};
    String[] solutionlength={"1","1","1","1"};//服务器返回的解决方案长度和文件字节长度
    int solutionlen=2000;
    int filelen=500;
	String modernevaluation=" % % % % % % % % % % %";//现代评价
	String solution="1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1";//解决方案
    String solutionname; //解决方案的名称
    Bundle bundle;
    boolean didnotify=false;
    
	 String strOperationStep;
	 String yue="0";
	 CharSequence contentTitle=" ";
	 CharSequence contentText=" ";
	 String[] dangers={"寒结","寒凝","寒扰","寒","热结","热燥","热","热扰","气血两虚","亏"};
	 String[] dangerspart2={"阴亢","阴盛","阳亢","阳盛","阴虚","阳虚","气血两虚","亏"};
	 String[] dangers2={"心肾不交证","心肾阳虚证","心脾二虚证","脾肺气虚证","脾肾阳虚证", "胆郁痰扰证","脾胃湿热证","心肺气虚证","心肝血虚证",
			 "肝肾阴虚证","肝肾阳虚证", "肝脾不调证","肝火犯肺证","肝胃不和证","肝胆湿热证","肺肾阴虚证","肺肾阳虚证","肺脾肾阳虚证",
			 "心胃火盛证","小肠烦热上升于心","小肠化血不养心"};
	 HashMap<Integer,String> dangermap=new HashMap<Integer,String>();
	 HashMap<Integer,String> dangermappart2=new HashMap<Integer,String>();

	 HashMap<Integer,String> dangermap2=new HashMap<Integer,String>();
	 NotificationManager mNotificationManager;
	 Notification notification;
	 NotificationCompat.Builder mBuilder;
	 String picurl="1,1,1,1,1,1,1,1";
	 String forresult="";

	private String date="1990";	 
	String date1="1990";
	public boolean fromNet=false;
	protected byte[] buffer=new byte[0];
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.myself);
    	shared = getSharedPreferences("userinfo",Context.MODE_PRIVATE);
    //    controlsView = findViewById(R.id.fullscreen_content_controls);
        btnConnectToServer=(Button)findViewById(R.id.btnConnectToServer);
//        btnDisconnect=(Button)findViewById(R.id.btnDisconnect);
//        btnSend=(Button)findViewById(R.id.btnSend);
//        btnAddData=(Button)findViewById(R.id.btnAddData);
//        txtSend=(EditText)findViewById(R.id.txtSend);
//        lblReceive=(TextView)findViewById(R.id.lblReceive);
//        txtData=(EditText)findViewById(R.id.Txtdata);
//        btnsymptom=(Button)findViewById(R.id.symptom);
         btnorgans=(Button)findViewById(R.id.organs);
         history=(Button)findViewById(R.id.history);
//        btnDisconnect.setEnabled(false);
//        btnsymptom.setEnabled(false);
         btnorgans.setEnabled(false);
        
         bundle = this.getIntent().getExtras();    
         data=bundle.getString("data");
         age=bundle.getString("age");
         male=bundle.getString("sex");    
         marriage=bundle.getString("marriage");
         date=bundle.getString("date");
         
         System.out.println("数据是"+data);
         System.out.println("年龄是"+age);
         System.out.println("性别是"+male);
         System.out.println("是否结婚"+marriage);
         System.out.println("日期是      "+date);
         
         
        tv121=(TextView)findViewById(R.id.tv121);
 		tv122=(TextView)findViewById(R.id.tv122);
 		tv123=(TextView)findViewById(R.id.tv123);
 		tv131=(TextView)findViewById(R.id.tv131);
 		tv132=(TextView)findViewById(R.id.tv132);
 		tv133=(TextView)findViewById(R.id.tv133);
 		tv221=(TextView)findViewById(R.id.tv221);
 		tv222=(TextView)findViewById(R.id.tv222);
 		tv223=(TextView)findViewById(R.id.tv223);
        tv231=(TextView)findViewById(R.id.tv231);
        tv232=(TextView)findViewById(R.id.tv232);
        tv233=(TextView)findViewById(R.id.tv233);
 		tv321=(TextView)findViewById(R.id.tv321);
 		tv322=(TextView)findViewById(R.id.tv322);
 		tv323=(TextView)findViewById(R.id.tv323);
 		tv331=(TextView)findViewById(R.id.tv331);
 		tv332=(TextView)findViewById(R.id.tv332);
 		tv333=(TextView)findViewById(R.id.tv333);
 		tv421=(TextView)findViewById(R.id.tv421);
 		tv422=(TextView)findViewById(R.id.tv422);
 		tv423=(TextView)findViewById(R.id.tv423);
 		tv431=(TextView)findViewById(R.id.tv431);
 		tv432=(TextView)findViewById(R.id.tv432);
 		tv433=(TextView)findViewById(R.id.tv433);
 		
 		for(int i=0;i<dangers.length;i++){
 			dangermap.put(i, dangers[i]);
 		}
 		for(int i=0;i<dangerspart2.length;i++){
 			dangermappart2.put(i, dangerspart2[i]);
 		}
 		for(int i=0;i<dangers2.length;i++){
 			dangermap2.put(i, dangers2[i]);
 		}
 		
		//消息通知栏
        //定义NotificationManager
        String ns = Context.NOTIFICATION_SERVICE;
        mNotificationManager = (NotificationManager) getSystemService(ns);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("金络康宝提醒您")//设置通知栏标题  
        .setContentText("如症状不吻合，经验证实，系统有提前预测功能，敬请留意。祝愿您安康!") 
        .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) //设置通知栏点击意图  
    //  .setNumber(number) //设置通知集合的数量  
        .setTicker("金络康宝提醒您") //通知首次出现在通知栏，带上升动画效果的  
        .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间  
        .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级  
    //  .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消    
        .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)  
        .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合  
        //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission  
        .setSmallIcon(R.drawable.yingyang);//设置通知小ICON  
            
        
 		
        btnConnectToServer.setOnClickListener(new View.OnClickListener() {
            private String name;
			private String password;

		   @Override
           public void onClick(View view)
            {
				fromNet=true;
            	name=shared.getString("oa_name", "1");
            	password=shared.getString("oa_pass", "");
            	if(name.equals("1")){//检查是否登陆
            		Intent intent = new Intent(view.getContext(), LoginActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("age", age);
                    bundle.putString("data", data);
                    bundle.putString("marriage", marriage);
                    bundle.putString("sex", male);
                    bundle.putString("date", date);
                    intent.putExtras(bundle);
					startActivity(intent);
            	}else{//检查是否余额充足
//                  yueeConnectThread=new yueeconnectThread();
//	               yueeConnectThread.start();
	                 mConnectThread=new connectThread();
		             mConnectThread.start();
	                 btnorgans.setEnabled(true);
	             }
            }
        });
//        btnAddData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    txtSend.setText("1"+ ssf_helper.EnCode_String(txtData.getText().toString(), "JINLUO.NET")+"END");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
        
        organHandler=new Handler(){
        	@Override
        	public void handleMessage(Message msg){
        		//心
        				tv121.setText(list1[2]);
        				tv131.setText(list2[2]);
        		//肺
        				tv122.setText(list1[0]);
        				tv132.setText(list2[0]);
        		//肝
        				tv123.setText(list1[7]);
        				tv133.setText(list2[7]);
        		//脾
        				tv221.setText(list1[6]);
        				tv231.setText(list2[6]);
        		//肾	
        				tv222.setText(list1[8]);
        				tv232.setText(list2[8]);
        		//胆	
        				tv223.setText(list1[10]);
        				tv233.setText(list2[10]);
        		//心包    
        				tv321.setText(list1[1]);
        				tv331.setText(list2[1]);
        		//三焦
        				tv322.setText(list1[4]);
        				tv332.setText(list2[4]);
        		//胃
        				tv323.setText(list1[11]);
        				tv333.setText(list2[11]);
        		//小肠	
        				tv421.setText(list1[3]);
        				tv431.setText(list2[3]);
        		//大肠
        				tv422.setText(list1[5]);
        				tv432.setText(list2[5]);
        		//膀胱	
        				tv423.setText(list1[9]);
        				tv433.setText(list2[9]);
        				
//        		        查看是否需要发送通知，症状程度严重，提醒
        		        if( CheckNotify()){
        		        	if(!didnotify){
        		            //用mNotificationManager的notify方法通知用户生成标题栏消息通知
        		            mNotificationManager.notify(1, mBuilder.build());
        		            dialog1();
        		            didnotify=true;
        		            }
        		        }
//        		        症状程度一般，发送提醒
        		        if(CheckNotify2()){
        		        	dialog2();
        		        }
        	}
        };
        
        yueeHandler=new Handler(){
        	@Override
        	public void handleMessage(Message msg){
        		
        		yueestr = yueestr + msg.obj.toString();
        		if(yueestr==" "){
        			
        		strOperationStep=yueestr.substring(0, 1); 
        		switch(strOperationStep)
        		{
        		case"1":
        			Toast.makeText(getApplicationContext(), "用户名密码错误", 3000).show();
        			break;	
        		case"2":
        			yue=yueestr.substring(1).split("END")[0];
        			System.out.println("收到的余额是！  "+yue);
        			if(Integer.parseInt(yue)>0){
     		             mConnectThread=new connectThread();
     		             mConnectThread.start();
     	                 btnorgans.setEnabled(true);
     	                }else{
     	                	Toast.makeText(getApplicationContext(), "您的余额不足，请充值！", 4000).show();
     	                }
        			break;
        		case"3":
        			Toast.makeText(getApplicationContext(), "用户名密码错误", 3000).show();
        			break;
        		}
        		}else{
        			Toast.makeText(getApplicationContext(), "无法查询余额，请稍后再试", 3000).show();
        			Analyze.this.finish();
        			Disconnect();
        		}
        	}
        };
     		
        mHandler=new Handler()
        {
            @Override
            public void handleMessage(Message msg)
            {
                final String strOperationStep;
                final String strMemberNo=shared.getString("oa_name", "");
//                		"xmuarkin";
                final String strMemberPwd=shared.getString("oa_pass", "");
//                		"lxfssf8079";
//                		"ssf790401";
                final String strCode="";
                final String strVersion="YYMEMBER";
                String strSend="";
                
                strReceive = strReceive + msg.obj.toString();
                
//              buffer=byteMerger(buffer,(byte[])msg.obj);
//              try {
//					strReceive=	new String(buffer, "GB2312").trim();
//
//              } catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} 
                
//              System.out.println("接收到的数据是"+strReceive);
                System.out.println("接收到的数据长度是！！！！！！"+strReceive.length());
                

// 得到解决方案               
                if(strReceive.length()>solutionlen-800){
                	if(fromNet){
                	date1=date.replace(":", "a");
                	String filename=date1+".txt";
                	
                	File file = new File(Environment.getExternalStorageDirectory()+"/jinluo/"+filename);
//                	if(!file.exists()){
                	 try {
						 saveToSDCard(filename,strReceive);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
//                	}
//                	}
                	stop=true;
                    mReceiveThread.interrupt();
                    strSend="4#COMPLETE#END";
                    SendData(strSend);
                    Disconnect();
                    }
                	
//              System.out.println("数据的末尾是"+strReceive.substring(strReceive.length()-100, strReceive.length()));
            	String[] parts=strReceive.split("@");
            	
//            	lblReceive.setText(parts[1]);               

            	if(parts.length>3){
            		chinesemed=parts[1];//中医评价          
            		modernevaluation=parts[2];//现代评价        		
            		solution=parts[3];//解决方案
//                  String solutionname=parts[4]; //解决方案的名称
            	}

            	System.out.println("111111111111111111111111111111"+chinesemed);
//            	System.out.println("222222222222222222222222222222"+parts[2]);
//            	System.out.println("333333333333333333333333333333"+parts[3]);
            	
            	String[] splits=chinesemed.split("%");
            	if(splits.length>=12){
            		for(int i=0;i<12;i++){
            			organresult[i]=splits[i+1];
            			check(organresult[i],i);
            			
            		}
            		organHandler.sendEmptyMessage(0);//更新五脏六腑的数据	
            		
            	}else{
            		Toast.makeText(getApplicationContext(), "获取的数据不完整", Toast.LENGTH_LONG).show();
            	}
            	
            	}

                
                if(strReceive.length()<3)
                {
                    return;
                }
                else if(!strReceive.substring(strReceive.length() - 3, strReceive.length()).equals("END"))
                {
                    return;
                }
                else if(strReceive.length()<50){
                	
                strOperationStep=strReceive.substring(0, 2);             
                 switch (strOperationStep)
                {
                    case "00"://验证登录            
                    	Toast.makeText(getApplicationContext(), "没有可用连接！", 3000).show();
                    	break;
                    case "01":
                        try {
                            strSend=ssf_helper.EnCode_String("ZS%" + Integer.toString(strMemberNo.length())+  "%" + Integer.toString(strMemberPwd.length()) + "%" +Integer.toString(strCode.length())+  "%" +Integer.toString(strVersion.length()) + "%" + strMemberNo + strMemberPwd + strCode + strVersion, "JINLUO.NET");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        SendData("1"+strSend+"END");
                        break;
                    case "11":
                    	Toast.makeText(getApplicationContext(), "密码错误", 3000).show();
                    	break;
                    case "12":
                    	Toast.makeText(getApplicationContext(), "账号未注册", 3000).show();
                    	break;
                    case "13":
                    	Toast.makeText(getApplicationContext(), "余额不足", 3000).show();
                    	break;
                    case "14"://发送收集的数据
                    	 boolean Isman=false;
                    	if(male.trim().equalsIgnoreCase("男")){
                    		Isman=true;
                    	}
                       
                        int nPatientID=0x09;
                        int Request_Type=0x01;
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//设置日期格式
                        String strMeasure_DateTime=df.format(new Date());
                        
                        int nAge=Integer.parseInt(age.trim());
                        
                        boolean IsMarried=true;
                        System.out.println("上传的年龄是"+nAge);
                        System.out.println("上传的性别是否是男性"+Isman);
                        System.out.println("上传的婚否"+IsMarried);
                            strSend="%"+(Isman?"true":"false")+"%"+Integer.toString(nPatientID)+"%"+Integer.toString(Request_Type)+"%"+strMeasure_DateTime+"%"+Integer.toString(nAge)+"%"
                                    +(IsMarried?"true":"false")+"%";
                            String[] datas=data.split("%");
//                            System.out.println("实际传输的数据是@@@@@@@@@@@"+data);
                            if(datas.length==24){
//                            for(int i=0;i<24;i++)
//                            {
                                strSend+=data;
//                            }
                         SendData("2"+strSend+"xxx%END");
                         forresult="2"+strSend+"xxx%END";
                         }else{
                        	 Toast.makeText(getApplicationContext(), "测量的数据长度不足", Toast.LENGTH_LONG).show();
                        	 Disconnect();
                         }
//                        String tmp="2%True%1%0%2011-02-10 17:00:43%23%True%90%85%78%105%90%80%70%60%85%86%87%88%89%98%99%100%101%102%135%147%78%88%80%80%ssf%%END";
//                        SendData(tmp);
                        break;
                        
                    case "2%"://获取服务器返回的下一步传输的数据大小
                    	solutionlength=strReceive.split("%");
                    	solutionlen=Integer.parseInt(solutionlength[1]);
                    	filelen=Integer.parseInt(solutionlength[2]);
                    	picurl=solutionlength[3];
                    	System.out.println("解决方案的长度是"+solutionlen);
                    	System.out.println("文件的长度是"+filelen);
                    	System.out.println("图片信息是                    "+picurl);
                    	
//                    	lblReceive.setText(strReceive);
//                      stop=true;
                        strSend="3TransferFileEND";
                        SendData(strSend);
                        break;
                    default:
                    	Toast.makeText(getApplicationContext(), "服务器没有响应！", 2000).show();

                }
                strReceive="";
            	buffer=new byte[0];
            }             
            }
        };
        
//        
//        btnSend.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                SendData(txtSend.getText().toString());
//                txtSend.setText("");//清空内容
//                displayToast("发送成功！");
//            }
//        });


//        btnsymptom.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//
//				Intent intent = new Intent(Analyze.this, Symptoms.class);			
//				startActivity(intent);
//			}
//		});

        btnorgans.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				Disconnect();  
				 String over="4#COMPLETE#END";
//               SendData(over);
				Intent intent = new Intent(Analyze.this, ModernEvaluation.class);			
				Bundle data=new Bundle();
//				data.putStringArray("data", organresult);
				data.putString("modern", modernevaluation);
				data.putString("solution", solution);
				data.putString("forresult", forresult);
				data.putString("patientsex", male.trim());
				intent.putExtras(data);
				startActivity(intent);
				
						
			}
		});
        
        history.setOnClickListener(new View.OnClickListener() {
			
        	
			@Override
			public void onClick(View arg0) {
				fromNet=false;
				// TODO Auto-generated method stub
				String date2=date.replace(":", "a");
				System.out.println(" 文件日期是         "+  date2);
				if(!date2.equalsIgnoreCase("1990")){
				try {
					File file = new File(Environment.getExternalStorageDirectory()+"/jinluo/",
							date2+".txt");
					BufferedReader br = new BufferedReader(new FileReader(file));
					String readline = "";
					StringBuffer sb = new StringBuffer();
					while ((readline = br.readLine()) != null) {
						System.out.println("readline:" + readline);
						sb.append(readline);
					}
					br.close();
					 Message msg = new Message();
		                msg.obj = sb.toString();
//		                		.getBytes("GB2312");
		//发送消息
		                mHandler.sendMessage(msg);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				btnorgans.setEnabled(true);
             }else{
            	 Toast.makeText(getApplicationContext(), "请先获取报告", 3000).show();
             }
				
			}
		});

//        btnDisconnect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                stop=true;
//                try
//                {
//                	if(clientSocket!=null){
//                    mReceiveThread.interrupt();
//                    clientSocket.shutdownInput();
//                    clientSocket.getInputStream().close();
//                    clientSocket.shutdownOutput();
//                    clientSocket.getOutputStream().close();
//                    clientSocket.close();}
//                }
//                catch (IOException e)
//                {
//                    e.printStackTrace();
//                }
//            }
//        });
        

        
    }

    private class connectThread extends Thread
    {
    	
        connectThread()
        {
        	
        }
        @Override
        public void run()
        {
            try
            {
            	clientSocket=new Socket();        	
            	clientSocket.connect(new InetSocketAddress("110.86.22.138", 6663), 10000);  
//            	172.16.152.128
            } catch (UnknownHostException e)
            {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "无法连接服务器", 3000).show();
                return;
            }catch(SocketException e){
            	e.printStackTrace();
                Toast.makeText(getApplicationContext(), "无法连接服务器", 3000).show();
                return;
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return;
            }
            stop = false;
            mReceiveThread = new ReceiveThread(clientSocket);
//开启线程
            mReceiveThread.start();
            //申请连接服务器
            strReceive="";
            SendData("0END");
        }
    }
    
    
    private class ReceiveThread extends Thread
    {
        private InputStream inStream = null;

        private byte[] buf=new byte[1024*10];
        private String str = null;

        ReceiveThread(Socket s)
        {
            try
            {
                this.inStream = s.getInputStream();//获得输入流
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        @Override
        public void run()
        {
            while(!stop)
            {

                try {
//读取输入数据（阻塞）
                    int i = 0;
//                  while((i = this.inStream.read(this.buf)) !=-1 );
                    i=this.inStream.read(this.buf);
                } catch (IOException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }

//字符编码转换
                try {
                    this.str = new String(this.buf, "GB2312").trim();
                } catch (UnsupportedEncodingException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }
//              System.out.println("接受到的数据是"+this.str);
                Message msg = new Message();
                msg.obj = this.str;
//发送消息
                mHandler.sendMessage(msg);

            }
        }

    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();

        if(mReceiveThread != null)
        {
            stop = true;
            mReceiveThread.interrupt();

        }
    }

private boolean SendData(String  msgSend)
{
    byte[] msgSendBuffer = null;
    System.out.println("发送的数据是"+msgSend);
    try {
        msgSendBuffer = msgSend.getBytes("GB2312");//字符编码转换
    } catch (UnsupportedEncodingException e1)
    {
        e1.printStackTrace();
    }
    try
    {
        outStream = clientSocket.getOutputStream();//获得Socket的输出流
    } catch (IOException e)
    {
        e.printStackTrace();
        return false;
    }

    try
    {
        outStream.write(msgSendBuffer);//发送数据
        outStream.flush();
    } catch (IOException e)
    {
        e.printStackTrace();
        return false;
    }

    return true;
}

public void Disconnect(){
	stop=true;
    
	if(clientSocket!=null){
    try {
		    clientSocket.close();
//		    mReceiveThread.interrupt();
            clientSocket.shutdownInput();
            clientSocket.getInputStream().close();
            clientSocket.shutdownOutput();
            clientSocket.getOutputStream().close();
            Toast.makeText(getApplicationContext(), "连接已经关闭", 1000);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    
	}
	
    if(yueeReceiveThread != null)
    {
        yueestop = true;
        try {
			yueeclientSocket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        yueeReceiveThread.interrupt();
    }
    
}

private void check(String string,int i) {
	// TODO Auto-generated method stub
	switch (string){
	case "阴虚":
		list1[i]="阴虚";
		list2[i]="正常";			
		break;
	case "阴不足":
		list1[i]="阴不足";
		list2[i]="正常";
		break;
	case "正常":
		list1[i]="正常";
		list2[i]="正常";
		break;
	case "阳不足":
		list1[i]="正常";
		list2[i]="阳不足";
		break;
	case "阳虚":
		list1[i]="正常";
		list2[i]="阳虚";
		break;
	case "亏":
		list1[i]="阴虚";
		list2[i]="阳虚";
		break;
	case "气血两虚":
		list1[i]="气虚";
		list2[i]="血虚";
		break;
	case "热扰":
		list1[i]="热扰";
		list2[i]="正常";
		break;
	case "热":
		list1[i]="热";
		list2[i]="正常";
		break;
	case "热燥":
		list1[i]="热燥";
		list2[i]="正常";
		break;
	case "热结":
		list1[i]="热结";
		list2[i]="正常";
		break;
	case "寒扰":
		list1[i]="正常";
		list2[i]="寒扰";
		break;
	case "寒":
		list1[i]="正常";
		list2[i]="寒";
		break;
	case "寒凝":
		list1[i]="正常";
		list2[i]="寒凝";
		break;
	case "寒结":
		list1[i]="正常";
		list2[i]="寒结";
		break;
	case "血不足":
		list1[i]="血不足";
		list2[i]="正常";
		break;
	case "血虚":
		list1[i]="血虚";
		list2[i]="正常";
		break;
	case "气不足":
		list1[i]="正常";
		list2[i]="气不足";
		break;
	case "气虚":
		list1[i]="正常";
		list2[i]="气虚";
		break;
	case "阴亢":
		list1[i]="阴亢";
		list2[i]="正常";
		break;
	case "阴盛":
		list1[i]="阴盛";
		list2[i]="正常";
		break;
	case "阳亢":
		list1[i]="正常";
		list2[i]="阳亢";
		break;
	case "阳盛":
		list1[i]="正常";
		list2[i]="阳盛";
		break;
	}
}     


private class yueeconnectThread extends Thread
{   	
    @Override
    public void run()
    {
        try
        {
        	yueeclientSocket=new Socket();        	
        	yueeclientSocket.connect(new InetSocketAddress("110.86.22.138", 6666), 10000);  
//        	172.16.152.128
        } catch (UnknownHostException e)
        {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "无法连接服务器", 3000).show();
            return;
        }catch(SocketException e){
        	e.printStackTrace();
            Toast.makeText(getApplicationContext(), "无法连接服务器", 3000).show();
            return;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        yueestop = false;
        yueeReceiveThread = new yueeReceiveThread(yueeclientSocket);
//开启线程
        yueeReceiveThread.start();
        //申请连接服务器
        yueestr="";
        try {
    		final String strMemberNo=shared.getString("oa_name", "");
//    		"xmuarkin";
            final String strMemberPwd=shared.getString("oa_pass", "");
//           lxfssf8079
			yueeSendData("%4%ZS%"+ssf_helper.EnCode_String(strMemberNo,"JINLUO.NET")+"%"+ssf_helper.EnCode_String(strMemberPwd,"JINLUO.NET")+"%END");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

private class yueeReceiveThread extends Thread
{
    private InputStream inStream = null;

    private byte[] buf=new byte[1024*5];
    private String str = null;

    yueeReceiveThread(Socket s)
    {
        try
        {
            this.inStream = s.getInputStream();//获得输入流
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void run()
    {
        while(!yueestop)
        {

            try {
//读取输入数据（阻塞）
                int i = 0;
//              while((i = this.inStream.read(this.buf)) !=1 );
                i=this.inStream.read(this.buf);
            } catch (IOException e) {
//TODO Auto-generated catch block
                e.printStackTrace();
            }

//字符编码转换
            try {
                this.str = new String(this.buf, "GB2312").trim();
            } catch (UnsupportedEncodingException e) {
//TODO Auto-generated catch block
                e.printStackTrace();
            }
//          System.out.println("接受到的数据是"+this.str);
            Message msg = new Message();
            msg.obj = this.str;
//发送消息
            yueeHandler.sendMessage(msg);

        }
    }

}

private boolean yueeSendData(String  msgSend)
{
    byte[] msgSendBuffer = null;
    System.out.println("yueeSendData发送的数据是"+msgSend);
    try {
        msgSendBuffer = msgSend.getBytes("GB2312");//字符编码转换
    } catch (UnsupportedEncodingException e1)
    {
        e1.printStackTrace();
    }
    try
    {
        yueeoutStream = yueeclientSocket.getOutputStream();//获得Socket的输出流
    } catch (IOException e)
    {
        e.printStackTrace();
        return false;
    }

    try
    {
        yueeoutStream.write(msgSendBuffer);//发送数据
        yueeoutStream.flush();
    } catch (IOException e)
    {
        e.printStackTrace();
        return false;
    }

    return true;
}

//检查是否需要发送通知
           public boolean CheckNotify(){
        	   
        	   if(list1!=null){
        		   for(int i=0;i<list1.length;i++){
        			   if(dangermap.containsValue(list1[i])){
        				   return true;
        			   }
        			   if(dangermappart2.containsValue(list1[i])){
        				   return true;
        			   }
        		   }
        	   }
        	   
        	   if(list2!=null){
        		   for(int i=0;i<list2.length;i++){
        			   if(dangermap.containsValue(list2[i])){
        				   return true;
        			   }
        			   if(dangermappart2.containsValue(list1[i])){
        				   return true;
        			   }
        		   }
        	   }
        	   return false;
           }
           
           public boolean CheckNotify2(){
        	   
        	   for(int i=0;i<dangers2.length;i++){
        		   if(chinesemed.contains(dangers2[i]))
        			   return true;
        	   }
        	   if(list1!=null){
        		   for(int i=0;i<list1.length;i++){
        			   if(dangermap2.containsValue(list1[i])){
        				   return true;
        			   }
        		   }
        	   }
        	   
        	   if(list2!=null){
        		   for(int i=0;i<list2.length;i++){
        			   if(dangermap2.containsValue(list2[i])){
        				   return true;
        			   }
        		   }
        	   }
        	   return false;
           }
           
           public PendingIntent getDefalutIntent(int flags){  
        	    PendingIntent pendingIntent= PendingIntent.getActivity(this, 1, new Intent(), flags);  
        	    return pendingIntent;  
        	}  
           private void dialog1(){
               AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
               builder.setTitle("健康预警"); //设置标题
               builder.setIcon(R.drawable.smallicon);
               builder.setMessage("健康预警，经验证实，这健康状态或存在分子指标异常。不明之处咨询QQ:3207575496.1587756029.金络康宝祝您安康！若愿意接受有关分子指标指导，点击:确定购买服务"); //设置内容
//               builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
               builder.setPositiveButton("确定购买", new DialogInterface.OnClickListener() { //设置确定按钮
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss(); //关闭dialog
                     Intent intent = new Intent(Analyze.this, InsuranceWeb.class);
  					 Bundle bundle=new Bundle();
  	                 bundle.putString("url", WebInfoConfig.jiancexiangmu);
  	                 intent.putExtras(bundle);
  					 startActivity(intent);
                   }
               });
               builder.setNegativeButton("先看情况", new DialogInterface.OnClickListener() { //设置取消按钮
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                   }
               });

               //参数都设置完成了，创建并显示出来
               builder.create().show();
           }
           
           
           private void dialog2(){
               AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
               builder.setTitle("金络康宝提醒您"); //设置标题
               builder.setIcon(R.drawable.smallicon);
               builder.setMessage("若症状不吻合，涉及到与有关因素影响、不排除将来发生的事件，请见谅。不明之处咨询QQ:3207575496.1587756029.金络康宝祝您安康！"); //设置内容
//               builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
               builder.setPositiveButton("确定购买", new DialogInterface.OnClickListener() { //设置确定按钮
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss(); //关闭dialog
                     Intent intent = new Intent(Analyze.this, InsuranceWeb.class);
  					 Bundle bundle=new Bundle();
  	                 bundle.putString("url", WebInfoConfig.jiancexiangmu);
  	                 intent.putExtras(bundle);
  					 startActivity(intent);
                   }
               });
               builder.setNegativeButton("先看情况", new DialogInterface.OnClickListener() { //设置取消按钮
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                   }
               });

               //参数都设置完成了，创建并显示出来
               builder.create().show();
           }
           
           public void saveToSDCard(String filename,String content) throws Exception{  
        	  File dir = new File(Environment.getExternalStorageDirectory()+"/jinluo");
        	      if(!dir.exists())
        	      {
        	    	  dir.mkdirs();
        	      }
               File file=new File(Environment.getExternalStorageDirectory()+"/jinluo/", filename);
               
//               if(!file.exists())
//               {
//            	   file.createNewFile();
//               }
               
               OutputStream out=new FileOutputStream(file);  
               out.write(content.getBytes());  
               out.close();  
           } 
           public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){  
        	    byte[] byte_3 = new byte[byte_1.length+byte_2.length];  
        	    System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);  
        	    System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);  
        	    return byte_3;  
        	}  
           

}
