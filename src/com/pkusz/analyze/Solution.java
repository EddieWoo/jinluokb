package com.pkusz.analyze;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
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
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import com.example.insurance.InsuranceWeb;
import com.example.jinluokb.MainActivity;
import com.example.jinluokb.R;
import com.pkusz.constants.WebInfoConfig;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class Solution extends Activity {
	   private ReceiveThread mReceiveThread = null;
	    private connectThread mConnectThread=null;
	    private boolean stop = true;
	    private String strReceive="";
	    private Socket clientSocket = null;
	    private OutputStream outStream = null;
	    

//	    Button btnConnectToServer;
	private Handler mHandler = null;
	private Handler xmHandler = null;
	private Handler PicHandler = null;
	private TextView zhengzhuang;
	String solution;
	String sexstr="��Ů",patientsex="��";
	String agestr1,agestr2,marriagestr,zhenzstr,
	jiancestr,shiliaostr=" ",shanshistr,duxinshustr,xueweianmostr,duxinshudescription;//�ֱ����Ա�����1������2��������֢״�����������Ŀ��ʳ�ƣ���ʳ����,������
	TextView sex,ages,marriage;
	Button nextzhenz,shiliaobut,shanshibut,duxinshubut,chakanxiangmu,homebut;
    int zhenzindex=1;
    int len;
    int jiancelen=10;
    String[] jiancestrs={"��ť","��ť","��ť","��ť","��ť","��ť","��ť","��ť","��ť","��ť"};
    String[] solutions;
    String[] solutiondetail={"~/"," ~/","~/ ","~/ ","~/ ","~/ "," ~/"," ~/","~/ ","~/ ","~/ ","~/ ","~/ ","~/ "};
    String[] result={" "," "," "};
   static String lastupdate="19000101";//�����Ŀ��һ�θ��µ�����
   int width,height;
    LinearLayout rly;
    byte[] buffer=new byte[]{0};
    byte[] Picbuffer=new byte[]{0};
	public static HashMap<Integer,String> hash=new HashMap<Integer,String>();
	 String temp="";
	 String explain=" ",title=" ";
	 
	 boolean did=false;
	 int indexx;//��Ŀ���
	 String mingcheng,jieshi;//��Ŀ���ƺͽ���
	private SharedPreferences shared;
	private String forresult=" ";
	int pictotallen=2000;
	String picurl=" ";
     int temp1=0;
     int size=0;
     boolean first=true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.solution);
	    rly = (LinearLayout) findViewById(R.id.xiangmu);  
	    shared = getSharedPreferences("userinfo",Context.MODE_PRIVATE);
	    
		 DisplayMetrics dm = new DisplayMetrics();
		 getWindowManager().getDefaultDisplay().getMetrics(dm);
		 width = dm.widthPixels;
		 height = dm.heightPixels;
//		 btnConnectToServer=(Button)findViewById(R.id.connect);

		zhengzhuang=(TextView)findViewById(R.id.zhengzhuang);
//		sex=(TextView)findViewById(R.id.sex);
//		ages=(TextView)findViewById(R.id.ages);
//		marriage=(TextView)findViewById(R.id.marriage);
		nextzhenz=(Button)findViewById(R.id.nextzhenzhuang);
		shiliaobut=(Button)findViewById(R.id.shiliao);
		shanshibut=(Button)findViewById(R.id.shanshi);
		duxinshubut=(Button)findViewById(R.id.duxinshu);
		chakanxiangmu=(Button)findViewById(R.id.chakanxiangmu);
		homebut=(Button)findViewById(R.id.homebutton);
		Bundle myBundle=this.getIntent().getExtras(); 
		solution=myBundle.getString("solution");
		forresult=myBundle.getString("forresult");
		patientsex=myBundle.getString("patientsex");
		
		len=Integer.parseInt(solution.substring(0,1));
	    solutions=solution.split("%");
	    solutions=removeUseless(solutions);
	    
			System.out.println("Ԥ��֢״��������"+len);
			
//			for(int i=0;i<solutions.length;i++){
//				System.out.println("��   "+i+"  ����������� " +solutions[i]);
//			}
			
			
//			for(int i=0;i<solutions.length;i++){
//				System.out.println(solutions[i]+i+i);
//			}
			
		sexstr=solutions[1].split("��")[1];
//		agestr1=solutions[1].split("��")[2];
//		agestr2=solutions[1].split("��")[3];
//		marriagestr=solutions[1].split("��")[4];
		zhenzstr=solutions[1].split("��")[5];
		jiancestr=solutions[1].split("��")[6];	
		shiliaostr=solutions[1].split("��")[8];
	    shanshistr=solutions[1].split("��")[9];
	    xueweianmostr=solutions[1].split("��")[11];
	    duxinshustr=solutions[1].split("��")[12];
	    if(solutions[1].split("��").length>14){
	    	duxinshudescription=solutions[1].split("��")[13];
	    	Log.d("sssss", duxinshudescription);
	    	}
	    
		String[] jiancexm=jiancestr.split("/");
		
		
	    jiancelen=jiancestr.length();
	    
		if(sexstr.equalsIgnoreCase("��Ů") || sexstr.equalsIgnoreCase(patientsex)){
		zhengzhuang.setText("֢״: "+zhenzstr);
		}
		
        
		mHandler=new Handler(){
        	@Override
        	public void handleMessage(Message msg){
        		zhenzindex++;
        		if(zhenzindex>solutions.length-1){
        			zhenzindex=1;
        		}
//        		System.out.println("��ǰ���������������"+solutions[zhenzindex].split("��").length);
//        		System.out.println("��ǰ���������                      "+solutions[zhenzindex]);
        		System.out.println("patientsexpatientsex"+patientsex);
        		System.out.println("sexstrsexstrsexstr"+sexstr);
        		
        		
        		solutiondetail=solutions[zhenzindex].split("��");
        		
        		if(solutions[zhenzindex].split("��").length>13){
        			sexstr=solutiondetail[1];
        			agestr1=solutiondetail[2];
        			agestr2=solutiondetail[3];
        			marriagestr=solutiondetail[4];
        			zhenzstr=solutiondetail[5];
        			jiancestr=solutiondetail[6];
        			shiliaostr=solutiondetail[8];
        		    shanshistr=solutiondetail[9];
        		    xueweianmostr=solutiondetail[11];
        		    duxinshustr=solutiondetail[12];
        		    duxinshudescription=solutiondetail[13];
        		}
        		    
        		if(zhenzstr.startsWith("����")){
        				 if(solutions.length==1){
        	        			if(sexstr.equalsIgnoreCase("��Ů") || sexstr.equalsIgnoreCase(patientsex)){
        					 zhengzhuang.setText("֢״: "+zhenzstr);  
        					 }
        				 }else{
        					 Message ms1g=new Message();
        					  ms1g.what=0;
        						mHandler.sendMessage(ms1g);
        				 }
        		}else if(zhenzstr.contains("/")){
        			    Message ms1g=new Message();
					    ms1g.what=0;
						mHandler.sendMessage(ms1g);
        		}else{
        			if(sexstr.equalsIgnoreCase("��Ů") || sexstr.equalsIgnoreCase(patientsex)){
        			zhengzhuang.setText("֢״: "+zhenzstr);  } 
        		}
        		
//        			String[] jiancexm1=jiancestr.split("/");
//        			String[] jiancexmreal=GetName(removefirst(jiancexm1));
//        			rly.removeAllViews();
//        			layoutbutton(jiancexmreal,removefirst(jiancexm1));
        			
        	}
        }; 
        
        xmHandler=new Handler(){
        	@Override
        	public void handleMessage(Message msg){
        		strReceive = strReceive + msg.obj.toString();

//                buffer=byteMerger(buffer,(byte[])msg.obj);
//                try {
//					strReceive=	new String(buffer, "GB2312").trim();
//
//                } catch (UnsupportedEncodingException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				} 

//        		System.out.println("ʵ�ʵõ������ݿ�ͷ��!!!!!!!!!!!"+strReceive.substring(0, 8));
				System.out.println("ʵ�ʵõ��������ǳ�����!!!!!!!!!!!"+strReceive.length());
				
        		
        		if(strReceive.length()<15){//���ظ�������
        			if(lastupdate.equalsIgnoreCase(strReceive) & !hash.isEmpty()){
//        				�������»�ȡ���ݣ�ֱ����hash�в���		
        				System.out.println("�������»�ȡ��Ŀ����");
        			}else{
        			System.out.println("��ȡ��������"+strReceive);
        			lastupdate=strReceive;
        			strReceive="";
//        			buffer=new byte[]{0};
        			
        			 if(isNetworkConnected(Solution.this)) {
        				 SendData("2END");
        		         }
        			
        			}
        		}else{   //����ʵ������,����������
//        			try {
//						strReceive=	new String(buffer, "GB2312").trim();
//					} catch (UnsupportedEncodingException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//        			int tt=(buffer[0]& 0xFF)<<24|(buffer[1]& 0xFF)<<16|(buffer[2]& 0xFF)<<8|(buffer[3]& 0xFF);	
//        			System.out.println("Ԥ���õ��ĳ����ǣ�������������������"+tt);
        			if(strReceive.length()>75000){
        			Process(strReceive);
//        			Log.d("sssss", ""+strReceive.getBytes().length);
//        			buffer=new byte[]{0};
        			
        			try {
						saveToSDCard("hash.txt",strReceive);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
        			
        			strReceive="";
        			}
        		}
        	}
        };
        
        
        
        nextzhenz.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Message msg=new Message();
				msg.what=0;
				mHandler.sendMessage(msg);
			}
        	
        });
        
        shiliaobut.setOnClickListener(new OnClickListener(){//��ʳ����
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Solution.this, Shiliao.class);			
				Bundle data=new Bundle();
				data.putString("shiliaostr", shiliaostr);
				intent.putExtras(data);
				startActivity(intent);
			}
        });
        
        shanshibut.setOnClickListener(new OnClickListener(){//��ΪѨλ��Ħ
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Solution.this, Shanshi.class);			
				Bundle data=new Bundle();
//				xueweianmostr
				data.putString("duxinshustr", xueweianmostr);
				intent.putExtras(data);
				startActivity(intent);
			}
        	
        });
        
        duxinshubut.setOnClickListener(new OnClickListener(){//������
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Solution.this, Duxinshu.class);			
				Bundle data=new Bundle();
				data.putString("duxinshustr", duxinshustr);
				data.putString("duxinshudescription", duxinshudescription);
				intent.putExtras(data);
				startActivity(intent);
			}
        	
        });

        chakanxiangmu.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(1==1){//����������Ŀ
//					 dialog1();
					
//				 mConnectThread=new connectThread();
//	             mConnectThread.start();
	             
	                Intent intent = new Intent(Solution.this, ExaminationItem.class);			
					Bundle data=new Bundle();
					data.putString("jiancestr", jiancestr);
					intent.putExtras(data);
					startActivity(intent);
					
	             }else{
	            	 dialog2();
	             }
			}
        	
        });
        
        homebut.setOnClickListener(new OnClickListener(){//������ҳ��
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Solution.this, MainActivity.class);			
				startActivity(intent);
			}
        });
        
//        �ӱ��ض�ȡ�����Ŀ��hash
        try {
			File file = new File(Environment.getExternalStorageDirectory()+"/jinluo/",
					"hash.txt");
			BufferedReader br = new BufferedReader(new FileReader(file));
			String readline = "";
			StringBuffer sb = new StringBuffer();
			while ((readline = br.readLine()) != null) {
				sb.append(readline);
			}
			br.close();
			 Message msg = new Message();
             msg.obj = sb.toString();
//������Ϣ
             xmHandler.sendMessage(msg);
                
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        if(isNetworkConnected(Solution.this)) {
		 mConnectThread=new connectThread();
         mConnectThread.start();
         }else{
        	 Toast.makeText(getApplicationContext(), "���ȿ�������", 1000).show();
         }
        
        
	}
 
// ȥ��solution���е�  #��	
	private String[] removeUseless(String[] solutions2) {
		// TODO Auto-generated method stub
		int t=0;
		for(int i=0;i<solutions2.length;i++){
			if(solutions2[i].equalsIgnoreCase("#")){
			t++;}
		}
		String[] result=new String[solutions2.length-t];
		int m=0;
		for(int i=0;i<solutions2.length;i++){
			if(!solutions2[i].equalsIgnoreCase("#")){
			result[m]=solutions2[i];
			m++;
			}
		}
		return result;
	}


	protected String[] GetName(String[] strs) {
		// TODO Auto-generated method stub
		
		int len=strs.length;
		String[] result=new String[len]; 
		if(hash.isEmpty()){
			return strs;
		}else{

			for(int i=0;i<len;i++){
				result[i]=hash.get(Integer.parseInt(strs[i]));
			}
			return result;
		}
	}


	protected void Process(String strReceive2) {
		
		//���յ������Ŀ�����ݣ�Ȼ�����������ݿ�
       if(!did){
    	   
//    	   try {
//   			saveToSDCard("jinluokb.txt",strReceive2);
//   		} catch (Exception e) {
//   			// TODO Auto-generated catch block
//   			e.printStackTrace();
//   		}

		// TODO Auto-generated method stub
        did=true;
        System.out.println("��ʼ��������");
		hash=new HashMap<Integer,String>();
		StringTokenizer tokens = new StringTokenizer(strReceive2,"@");
	    	tokens.nextElement();
           String indexx = null;
           int i;
	    while(tokens.hasMoreElements()){
	    	if(tokens.hasMoreElements()){
	    	 indexx=(String) tokens.nextElement();
	    	}
	    	
	    	if(tokens.hasMoreElements()){	
	    		mingcheng=(String) tokens.nextElement();
	    	 }
	    	if(tokens.hasMoreElements()){
	    		jieshi=(String) tokens.nextElement();
	    		}
//System.out.println("1111  "+indexx);
//System.out.println("2222  "+mingcheng);
//System.out.println("3333  "+jieshi);

	     if(isNumeric(indexx)){
	    	 i=Integer.parseInt(indexx);
	         hash.put(i, mingcheng+";"+jieshi);
//	         System.out.println("~~~~~~~���������~~~~~~~"+i);
	     }else if(isNumeric(mingcheng)){
	    	 if(tokens.hasMoreElements()){
	    		 i=Integer.parseInt(mingcheng);
	    		 hash.put(i, jieshi+";"+(String)tokens.nextElement());
//	    		 System.out.println("~~~~~~~����������~~~~~~~"+i);
	    	 }
	    	
	     }else if(isNumeric(jieshi)){
	    	 if(tokens.hasMoreElements()){
	    		 i=Integer.parseInt(jieshi);
	    		 String mc=(String)tokens.nextElement();
	    		 if(tokens.hasMoreElements()){
	    		 hash.put(i, mc+";"+(String)tokens.nextElement());
//	    		 System.out.println("~~~~~~~������������~~~~~~~"+i);
	    		 }
	    	 }
	    	
	     }
      }
       }
//       mHandler.sendEmptyMessage(0);
	}
		

	public void layoutbutton(final String[] paras,String[] paras1){
//	��һ��������realname
		    RelativeLayout layout = new RelativeLayout(this);  			
	        Button Btn[] = new Button[paras.length];
	        int j = -1;
	        for  (int i=0; i<paras.length; i++) {      
	              Btn[i]=new Button(this);
	              Btn[i].setId(2000+i); 
	              Btn[i].setText(paras[i]);
	              RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams ((width-30)/2,130);  //���ð�ť�Ŀ�Ⱥ͸߶�
	              if (i%2 == 0) {
	               j++;
	              }
	              btParams.leftMargin = 10+ ((width-30)/2+10)*(i%2);   //�����궨λ        
	              btParams.topMargin = 30 + 130*j;   //�����궨λ       
	              layout.addView(Btn[i],btParams);   
	              //����ť����layout���
	        }
	        
	        for (int k = 0; k <= Btn.length-1; k++) { 
	        	   //���ﲻ��ҪfindId����Ϊ������ʱ���Ѿ�ȷ���ĸ���ť��Ӧ�ĸ�Id
	        	   Btn[k].setTag(k);                //Ϊ��ť����һ����ǣ���ȷ���ǰ�������һ����ť
//	        	   System.out.println("~~~~~~~~~~"+paras[k]);
	        		   
	        	   Btn[k].setOnClickListener(new Button.OnClickListener() {        		   
	        	    @Override
	        	        public void onClick(View v) {
	        	    	 int i = (Integer) v.getTag();
	        	    	 if(paras[i]!=null && isNumeric(paras[i])){
	        	    		 title=" ";
		        	    	 explain="��������Ŀ����ȡ����";
		        	    	new  AlertDialog.Builder(Solution.this)    
		        	    	                .setTitle(title )  
		        	    	                .setMessage(explain)  
		        	    	                .setPositiveButton("ȷ��" ,  null )  
		        	    	                .show();
	        	    	 }else if(paras[i]!=null){
	        	    	 title=paras[i].split(";")[0];
	        	    	 explain=paras[i].split(";")[1];
	        	    	new  AlertDialog.Builder(Solution.this)    
	        	    	                .setTitle(title )  
	        	    	                .setMessage(explain)  
	        	    	                .setPositiveButton("ȷ��" ,  null )  
	        	    	                .show();     }             
	        	       }
	        	     });
	        	   }

	        rly.addView(layout); 
//	        rly.removeView(layout);
	        
	}
	
    public String[] removefirst(String[] paras){
    	int n=paras.length;
        String[] result=new String[n-1];
        for(int i=0;i<n-1;i++){
        	result[i]=paras[i+1];
        }
    	return result;
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
            	clientSocket.connect(new InetSocketAddress("110.86.22.138", 6662), 10000);  
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
            SendData("1END");
        }
    }
    
    
    private class ReceiveThread extends Thread
    {
        private InputStream inStream = null;

        private byte[] buf=new byte[1024*5];
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
//                  while((i = this.inStream.read(this.buf)) !=1 );
                    i=this.inStream.read(this.buf);
                } catch (IOException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }

//�ַ�����ת��
                try {
                    this.str = new String(this.buf, "GB2312").trim();
//                      saveToSDCard("jinluokb"+temp1+".txt",str);
//				      temp1++;
                } catch (UnsupportedEncodingException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//              System.out.println("���ܵ���������"+this.str);
                Message msg = new Message();
                msg.obj = this.str;

//������Ϣ
//                size=size+str.length();
//                System.out.println("���յ������ݴ�С�� !!!!!!!!!!! "+size);
                xmHandler.sendMessage(msg);
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

	
}



public static byte[] byteMerger(byte[] byte_1, byte[] byte_2){  
    byte[] byte_3 = new byte[byte_1.length+byte_2.length];  
    System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);  
    System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);  
    return byte_3;  
}  

public static boolean isNumeric(String str) {
      java.util.regex.Pattern pattern = Pattern.compile("[0-9]*");
      java.util.regex.Matcher isNum = pattern.matcher(str.trim());
        if (!isNum.matches()) {
          return false;
      }
     return true;
     }


            private void dialog2(){
                  AlertDialog.Builder builder=new AlertDialog.Builder(this);  //�ȵõ�������
                  builder.setTitle("��������Ŀ����"); //���ñ���
                  builder.setIcon(R.drawable.yingyang);
                  builder.setMessage("����û�й���鿴�����Ŀ�ķ�����Ҫʹ�����ȹ���"); //��������
//                builder.setIcon(R.mipmap.ic_launcher);//����ͼ�꣬ͼƬid����
                  builder.setPositiveButton("ȷ������", new DialogInterface.OnClickListener() { //����ȷ����ť
             @Override
              public void onClick(DialogInterface dialog, int which) {
                   dialog.dismiss(); //�ر�dialog
                   Intent intent = new Intent(Solution.this, InsuranceWeb.class);
		        	 Bundle bundle=new Bundle();
                  bundle.putString("url", WebInfoConfig.jiancexiangmu);
                intent.putExtras(bundle);
			      startActivity(intent);
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
    	
    	  public void saveToSDCard(String filename,String content) throws Exception{  
        	  File dir = new File(Environment.getExternalStorageDirectory()+"/jinluo");
    	      if(!dir.exists())
    	      {
    	    	  dir.mkdirs();
    	      }
           File file=new File(Environment.getExternalStorageDirectory()+"/jinluo/", filename);
           
//           if(!file.exists())
//           {
//        	   file.createNewFile();
//           }
           
           OutputStream out=new FileOutputStream(file);  
           out.write(content.getBytes());  
           out.close();  
       } 
          private void dialog1(){
              AlertDialog.Builder builder=new AlertDialog.Builder(this);  //�ȵõ�������
              builder.setTitle("���翵��������"); //���ñ���
              builder.setIcon(R.drawable.yingyang);
              builder.setMessage("��Ը�����������·��Ӽ���ָ��ָ����������ȷ�Ϲ�������Ŀ����"); //��������
//              builder.setIcon(R.mipmap.ic_launcher);//����ͼ�꣬ͼƬid����
              builder.setPositiveButton("ȷ������", new DialogInterface.OnClickListener() { //����ȷ����ť
                  @Override
                  public void onClick(DialogInterface dialog, int which) {
                      dialog.dismiss(); //�ر�dialog
                    Intent intent = new Intent(Solution.this, InsuranceWeb.class);
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
          
          public static int bytesToInt(byte[] bytes) {  
  		    int number = bytes[3] & 0xFF;  
  		    // "|="��λ��ֵ��  
  		    number |= ((bytes[2] << 8) & 0xFF00);  
  		    number |= ((bytes[1] << 16) & 0xFF0000);  
  		    number |= ((bytes[0] << 24) & 0xFF000000);  
  		    return number;  
  		}  
}
