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
    
    String marriage="δ��",age="25",male="��",
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
    String chinesemed="%����%����%����%����%����%����%����%����%����%����%����%����";//��ҽ����
//    String[] result={"����","����","����","����","����","����","����","����","����","����","����","����"};
    String[] solutionlength={"1","1","1","1"};//���������صĽ���������Ⱥ��ļ��ֽڳ���
    int solutionlen=2000;
    int filelen=500;
	String modernevaluation=" % % % % % % % % % % %";//�ִ�����
	String solution="1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1";//�������
    String solutionname; //�������������
    Bundle bundle;
    boolean didnotify=false;
    
	 String strOperationStep;
	 String yue="0";
	 CharSequence contentTitle=" ";
	 CharSequence contentText=" ";
	 String[] dangers={"����","����","����","��","�Ƚ�","����","��","����","��Ѫ����","��"};
	 String[] dangerspart2={"����","��ʢ","����","��ʢ","����","����","��Ѫ����","��"};
	 String[] dangers2={"��������֤","��������֤","��Ƣ����֤","Ƣ������֤","Ƣ������֤", "����̵��֤","Ƣθʪ��֤","�ķ�����֤","�ĸ�Ѫ��֤",
			 "��������֤","��������֤", "��Ƣ����֤","�λ𷸷�֤","��θ����֤","�ε�ʪ��֤","��������֤","��������֤","��Ƣ������֤",
			 "��θ��ʢ֤","С��������������","С����Ѫ������"};
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
         
         System.out.println("������"+data);
         System.out.println("������"+age);
         System.out.println("�Ա���"+male);
         System.out.println("�Ƿ���"+marriage);
         System.out.println("������      "+date);
         
         
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
 		
		//��Ϣ֪ͨ��
        //����NotificationManager
        String ns = Context.NOTIFICATION_SERVICE;
        mNotificationManager = (NotificationManager) getSystemService(ns);
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("���翵��������")//����֪ͨ������  
        .setContentText("��֢״���Ǻϣ�����֤ʵ��ϵͳ����ǰԤ�⹦�ܣ��������⡣ףԸ������!") 
        .setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) //����֪ͨ�������ͼ  
    //  .setNumber(number) //����֪ͨ���ϵ�����  
        .setTicker("���翵��������") //֪ͨ�״γ�����֪ͨ��������������Ч����  
        .setWhen(System.currentTimeMillis())//֪ͨ������ʱ�䣬����֪ͨ��Ϣ����ʾ��һ����ϵͳ��ȡ����ʱ��  
        .setPriority(Notification.PRIORITY_DEFAULT) //���ø�֪ͨ���ȼ�  
    //  .setAutoCancel(true)//���������־���û��������Ϳ�����֪ͨ���Զ�ȡ��    
        .setOngoing(false)//ture��������Ϊһ�����ڽ��е�֪ͨ������ͨ����������ʾһ����̨����,�û���������(�粥������)����ĳ�ַ�ʽ���ڵȴ�,���ռ���豸(��һ���ļ�����,ͬ������,������������)  
        .setDefaults(Notification.DEFAULT_VIBRATE)//��֪ͨ������������ƺ���Ч������򵥡���һ�µķ�ʽ��ʹ�õ�ǰ���û�Ĭ�����ã�ʹ��defaults���ԣ��������  
        //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND ������� // requires VIBRATE permission  
        .setSmallIcon(R.drawable.yingyang);//����֪ͨСICON  
            
        
 		
        btnConnectToServer.setOnClickListener(new View.OnClickListener() {
            private String name;
			private String password;

		   @Override
           public void onClick(View view)
            {
				fromNet=true;
            	name=shared.getString("oa_name", "1");
            	password=shared.getString("oa_pass", "");
            	if(name.equals("1")){//����Ƿ��½
            		Intent intent = new Intent(view.getContext(), LoginActivity.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("age", age);
                    bundle.putString("data", data);
                    bundle.putString("marriage", marriage);
                    bundle.putString("sex", male);
                    bundle.putString("date", date);
                    intent.putExtras(bundle);
					startActivity(intent);
            	}else{//����Ƿ�������
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
        		//��
        				tv121.setText(list1[2]);
        				tv131.setText(list2[2]);
        		//��
        				tv122.setText(list1[0]);
        				tv132.setText(list2[0]);
        		//��
        				tv123.setText(list1[7]);
        				tv133.setText(list2[7]);
        		//Ƣ
        				tv221.setText(list1[6]);
        				tv231.setText(list2[6]);
        		//��	
        				tv222.setText(list1[8]);
        				tv232.setText(list2[8]);
        		//��	
        				tv223.setText(list1[10]);
        				tv233.setText(list2[10]);
        		//�İ�    
        				tv321.setText(list1[1]);
        				tv331.setText(list2[1]);
        		//����
        				tv322.setText(list1[4]);
        				tv332.setText(list2[4]);
        		//θ
        				tv323.setText(list1[11]);
        				tv333.setText(list2[11]);
        		//С��	
        				tv421.setText(list1[3]);
        				tv431.setText(list2[3]);
        		//��
        				tv422.setText(list1[5]);
        				tv432.setText(list2[5]);
        		//����	
        				tv423.setText(list1[9]);
        				tv433.setText(list2[9]);
        				
//        		        �鿴�Ƿ���Ҫ����֪ͨ��֢״�̶����أ�����
        		        if( CheckNotify()){
        		        	if(!didnotify){
        		            //��mNotificationManager��notify����֪ͨ�û����ɱ�������Ϣ֪ͨ
        		            mNotificationManager.notify(1, mBuilder.build());
        		            dialog1();
        		            didnotify=true;
        		            }
        		        }
//        		        ֢״�̶�һ�㣬��������
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
        			Toast.makeText(getApplicationContext(), "�û����������", 3000).show();
        			break;	
        		case"2":
        			yue=yueestr.substring(1).split("END")[0];
        			System.out.println("�յ�������ǣ�  "+yue);
        			if(Integer.parseInt(yue)>0){
     		             mConnectThread=new connectThread();
     		             mConnectThread.start();
     	                 btnorgans.setEnabled(true);
     	                }else{
     	                	Toast.makeText(getApplicationContext(), "�������㣬���ֵ��", 4000).show();
     	                }
        			break;
        		case"3":
        			Toast.makeText(getApplicationContext(), "�û����������", 3000).show();
        			break;
        		}
        		}else{
        			Toast.makeText(getApplicationContext(), "�޷���ѯ�����Ժ�����", 3000).show();
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
                
//              System.out.println("���յ���������"+strReceive);
                System.out.println("���յ������ݳ����ǣ�����������"+strReceive.length());
                

// �õ��������               
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
                	
//              System.out.println("���ݵ�ĩβ��"+strReceive.substring(strReceive.length()-100, strReceive.length()));
            	String[] parts=strReceive.split("@");
            	
//            	lblReceive.setText(parts[1]);               

            	if(parts.length>3){
            		chinesemed=parts[1];//��ҽ����          
            		modernevaluation=parts[2];//�ִ�����        		
            		solution=parts[3];//�������
//                  String solutionname=parts[4]; //�������������
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
            		organHandler.sendEmptyMessage(0);//������������������	
            		
            	}else{
            		Toast.makeText(getApplicationContext(), "��ȡ�����ݲ�����", Toast.LENGTH_LONG).show();
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
                    case "00"://��֤��¼            
                    	Toast.makeText(getApplicationContext(), "û�п������ӣ�", 3000).show();
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
                    	Toast.makeText(getApplicationContext(), "�������", 3000).show();
                    	break;
                    case "12":
                    	Toast.makeText(getApplicationContext(), "�˺�δע��", 3000).show();
                    	break;
                    case "13":
                    	Toast.makeText(getApplicationContext(), "����", 3000).show();
                    	break;
                    case "14"://�����ռ�������
                    	 boolean Isman=false;
                    	if(male.trim().equalsIgnoreCase("��")){
                    		Isman=true;
                    	}
                       
                        int nPatientID=0x09;
                        int Request_Type=0x01;
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");//�������ڸ�ʽ
                        String strMeasure_DateTime=df.format(new Date());
                        
                        int nAge=Integer.parseInt(age.trim());
                        
                        boolean IsMarried=true;
                        System.out.println("�ϴ���������"+nAge);
                        System.out.println("�ϴ����Ա��Ƿ�������"+Isman);
                        System.out.println("�ϴ��Ļ��"+IsMarried);
                            strSend="%"+(Isman?"true":"false")+"%"+Integer.toString(nPatientID)+"%"+Integer.toString(Request_Type)+"%"+strMeasure_DateTime+"%"+Integer.toString(nAge)+"%"
                                    +(IsMarried?"true":"false")+"%";
                            String[] datas=data.split("%");
//                            System.out.println("ʵ�ʴ����������@@@@@@@@@@@"+data);
                            if(datas.length==24){
//                            for(int i=0;i<24;i++)
//                            {
                                strSend+=data;
//                            }
                         SendData("2"+strSend+"xxx%END");
                         forresult="2"+strSend+"xxx%END";
                         }else{
                        	 Toast.makeText(getApplicationContext(), "���������ݳ��Ȳ���", Toast.LENGTH_LONG).show();
                        	 Disconnect();
                         }
//                        String tmp="2%True%1%0%2011-02-10 17:00:43%23%True%90%85%78%105%90%80%70%60%85%86%87%88%89%98%99%100%101%102%135%147%78%88%80%80%ssf%%END";
//                        SendData(tmp);
                        break;
                        
                    case "2%"://��ȡ���������ص���һ����������ݴ�С
                    	solutionlength=strReceive.split("%");
                    	solutionlen=Integer.parseInt(solutionlength[1]);
                    	filelen=Integer.parseInt(solutionlength[2]);
                    	picurl=solutionlength[3];
                    	System.out.println("��������ĳ�����"+solutionlen);
                    	System.out.println("�ļ��ĳ�����"+filelen);
                    	System.out.println("ͼƬ��Ϣ��                    "+picurl);
                    	
//                    	lblReceive.setText(strReceive);
//                      stop=true;
                        strSend="3TransferFileEND";
                        SendData(strSend);
                        break;
                    default:
                    	Toast.makeText(getApplicationContext(), "������û����Ӧ��", 2000).show();

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
//                txtSend.setText("");//�������
//                displayToast("���ͳɹ���");
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
				System.out.println(" �ļ�������         "+  date2);
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
		//������Ϣ
		                mHandler.sendMessage(msg);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
				btnorgans.setEnabled(true);
             }else{
            	 Toast.makeText(getApplicationContext(), "���Ȼ�ȡ����", 3000).show();
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
                Toast.makeText(getApplicationContext(), "�޷����ӷ�����", 3000).show();
                return;
            }catch(SocketException e){
            	e.printStackTrace();
                Toast.makeText(getApplicationContext(), "�޷����ӷ�����", 3000).show();
                return;
            }
            catch (IOException e)
            {
                e.printStackTrace();
                return;
            }
            stop = false;
            mReceiveThread = new ReceiveThread(clientSocket);
//�����߳�
            mReceiveThread.start();
            //�������ӷ�����
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
                this.inStream = s.getInputStream();//���������
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
//��ȡ�������ݣ�������
                    int i = 0;
//                  while((i = this.inStream.read(this.buf)) !=-1 );
                    i=this.inStream.read(this.buf);
                } catch (IOException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }

//�ַ�����ת��
                try {
                    this.str = new String(this.buf, "GB2312").trim();
                } catch (UnsupportedEncodingException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }
//              System.out.println("���ܵ���������"+this.str);
                Message msg = new Message();
                msg.obj = this.str;
//������Ϣ
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
    System.out.println("���͵�������"+msgSend);
    try {
        msgSendBuffer = msgSend.getBytes("GB2312");//�ַ�����ת��
    } catch (UnsupportedEncodingException e1)
    {
        e1.printStackTrace();
    }
    try
    {
        outStream = clientSocket.getOutputStream();//���Socket�������
    } catch (IOException e)
    {
        e.printStackTrace();
        return false;
    }

    try
    {
        outStream.write(msgSendBuffer);//��������
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
            Toast.makeText(getApplicationContext(), "�����Ѿ��ر�", 1000);
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
	case "����":
		list1[i]="����";
		list2[i]="����";			
		break;
	case "������":
		list1[i]="������";
		list2[i]="����";
		break;
	case "����":
		list1[i]="����";
		list2[i]="����";
		break;
	case "������":
		list1[i]="����";
		list2[i]="������";
		break;
	case "����":
		list1[i]="����";
		list2[i]="����";
		break;
	case "��":
		list1[i]="����";
		list2[i]="����";
		break;
	case "��Ѫ����":
		list1[i]="����";
		list2[i]="Ѫ��";
		break;
	case "����":
		list1[i]="����";
		list2[i]="����";
		break;
	case "��":
		list1[i]="��";
		list2[i]="����";
		break;
	case "����":
		list1[i]="����";
		list2[i]="����";
		break;
	case "�Ƚ�":
		list1[i]="�Ƚ�";
		list2[i]="����";
		break;
	case "����":
		list1[i]="����";
		list2[i]="����";
		break;
	case "��":
		list1[i]="����";
		list2[i]="��";
		break;
	case "����":
		list1[i]="����";
		list2[i]="����";
		break;
	case "����":
		list1[i]="����";
		list2[i]="����";
		break;
	case "Ѫ����":
		list1[i]="Ѫ����";
		list2[i]="����";
		break;
	case "Ѫ��":
		list1[i]="Ѫ��";
		list2[i]="����";
		break;
	case "������":
		list1[i]="����";
		list2[i]="������";
		break;
	case "����":
		list1[i]="����";
		list2[i]="����";
		break;
	case "����":
		list1[i]="����";
		list2[i]="����";
		break;
	case "��ʢ":
		list1[i]="��ʢ";
		list2[i]="����";
		break;
	case "����":
		list1[i]="����";
		list2[i]="����";
		break;
	case "��ʢ":
		list1[i]="����";
		list2[i]="��ʢ";
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
            Toast.makeText(getApplicationContext(), "�޷����ӷ�����", 3000).show();
            return;
        }catch(SocketException e){
        	e.printStackTrace();
            Toast.makeText(getApplicationContext(), "�޷����ӷ�����", 3000).show();
            return;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        yueestop = false;
        yueeReceiveThread = new yueeReceiveThread(yueeclientSocket);
//�����߳�
        yueeReceiveThread.start();
        //�������ӷ�����
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
            this.inStream = s.getInputStream();//���������
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
//��ȡ�������ݣ�������
                int i = 0;
//              while((i = this.inStream.read(this.buf)) !=1 );
                i=this.inStream.read(this.buf);
            } catch (IOException e) {
//TODO Auto-generated catch block
                e.printStackTrace();
            }

//�ַ�����ת��
            try {
                this.str = new String(this.buf, "GB2312").trim();
            } catch (UnsupportedEncodingException e) {
//TODO Auto-generated catch block
                e.printStackTrace();
            }
//          System.out.println("���ܵ���������"+this.str);
            Message msg = new Message();
            msg.obj = this.str;
//������Ϣ
            yueeHandler.sendMessage(msg);

        }
    }

}

private boolean yueeSendData(String  msgSend)
{
    byte[] msgSendBuffer = null;
    System.out.println("yueeSendData���͵�������"+msgSend);
    try {
        msgSendBuffer = msgSend.getBytes("GB2312");//�ַ�����ת��
    } catch (UnsupportedEncodingException e1)
    {
        e1.printStackTrace();
    }
    try
    {
        yueeoutStream = yueeclientSocket.getOutputStream();//���Socket�������
    } catch (IOException e)
    {
        e.printStackTrace();
        return false;
    }

    try
    {
        yueeoutStream.write(msgSendBuffer);//��������
        yueeoutStream.flush();
    } catch (IOException e)
    {
        e.printStackTrace();
        return false;
    }

    return true;
}

//����Ƿ���Ҫ����֪ͨ
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
               AlertDialog.Builder builder=new AlertDialog.Builder(this);  //�ȵõ�������
               builder.setTitle("����Ԥ��"); //���ñ���
               builder.setIcon(R.drawable.smallicon);
               builder.setMessage("����Ԥ��������֤ʵ���⽡��״̬����ڷ���ָ���쳣������֮����ѯQQ:3207575496.1587756029.���翵��ף����������Ը������йط���ָ��ָ�������:ȷ���������"); //��������
//               builder.setIcon(R.mipmap.ic_launcher);//����ͼ�꣬ͼƬid����
               builder.setPositiveButton("ȷ������", new DialogInterface.OnClickListener() { //����ȷ����ť
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss(); //�ر�dialog
                     Intent intent = new Intent(Analyze.this, InsuranceWeb.class);
  					 Bundle bundle=new Bundle();
  	                 bundle.putString("url", WebInfoConfig.jiancexiangmu);
  	                 intent.putExtras(bundle);
  					 startActivity(intent);
                   }
               });
               builder.setNegativeButton("�ȿ����", new DialogInterface.OnClickListener() { //����ȡ����ť
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                   }
               });

               //��������������ˣ���������ʾ����
               builder.create().show();
           }
           
           
           private void dialog2(){
               AlertDialog.Builder builder=new AlertDialog.Builder(this);  //�ȵõ�������
               builder.setTitle("���翵��������"); //���ñ���
               builder.setIcon(R.drawable.smallicon);
               builder.setMessage("��֢״���Ǻϣ��漰�����й�����Ӱ�졢���ų������������¼�������¡�����֮����ѯQQ:3207575496.1587756029.���翵��ף��������"); //��������
//               builder.setIcon(R.mipmap.ic_launcher);//����ͼ�꣬ͼƬid����
               builder.setPositiveButton("ȷ������", new DialogInterface.OnClickListener() { //����ȷ����ť
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss(); //�ر�dialog
                     Intent intent = new Intent(Analyze.this, InsuranceWeb.class);
  					 Bundle bundle=new Bundle();
  	                 bundle.putString("url", WebInfoConfig.jiancexiangmu);
  	                 intent.putExtras(bundle);
  					 startActivity(intent);
                   }
               });
               builder.setNegativeButton("�ȿ����", new DialogInterface.OnClickListener() { //����ȡ����ť
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                   }
               });

               //��������������ˣ���������ʾ����
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
