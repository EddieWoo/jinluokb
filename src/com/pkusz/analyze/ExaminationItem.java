package com.pkusz.analyze;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import com.example.insurance.InsuranceWeb;
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
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class ExaminationItem extends Activity {
	
	  private ReceiveThread mReceiveThread = null;
	  private connectThread mConnectThread=null;
	  private boolean stop = true;
	  private Socket clientSocket = null;
	  private OutputStream outStream = null;
	private LinearLayout rly;
	private Handler xmHandler;
	private String strReceive="";
	 static String lastupdate="19000101";//检测项目上一次更新的日期
	   int width,height;
	    byte[] buffer=new byte[]{0};
	    byte[] Picbuffer=new byte[]{0};
		public static HashMap<Integer,String> hash=Solution.hash;
		 String temp="";
		 String explain=" ",title=" ";
		 
		 boolean did=false;
		 int indexx;//项目编号
		 String mingcheng,jieshi;//项目名称和解释
		private SharedPreferences shared;
		private String forresult=" ";
		int pictotallen=2000;
		String picurl=" ";
	     int temp1=0;
	     int size=0;
		private String jiancestr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.examinationitem);
		rly = (LinearLayout) findViewById(R.id.xiangmu);  
		Bundle bundle=this.getIntent().getExtras();
		jiancestr=bundle.getString("jiancestr");
		
		  DisplayMetrics dm = new DisplayMetrics();
		  getWindowManager().getDefaultDisplay().getMetrics(dm);
		 width = dm.widthPixels;
		 height = dm.heightPixels;
		 
		
		String[] jiancexm1=jiancestr.split("/");
		String[] jiancexmreal=GetName(removefirst(jiancexm1));
		jiancexmreal=removenull(jiancexmreal);
		rly.removeAllViews();
		layoutbutton(jiancexmreal,removefirst(jiancexm1));
		
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

        		System.out.println("实际得到的数据开头是!!!!!!!!!!!"+strReceive.substring(0, 8));
				System.out.println("实际得到的数据是长度是!!!!!!!!!!!"+strReceive.length());
				
        		
        		if(strReceive.length()<15){//返回更新日期
        			if(lastupdate.equalsIgnoreCase(strReceive) & !hash.isEmpty()){
//        				不用重新获取数据，直接在hash中查找		
        				System.out.println("不用重新获取项目名称");
        			}else{
        			System.out.println("获取的日期是"+strReceive);
        			lastupdate=strReceive;
        			strReceive="";
        			buffer=new byte[]{0};
        			SendData("2END");

        			}
        		}else{   //返回实际数据,并更新数据
//        			try {
//						strReceive=	new String(buffer, "GB2312").trim();
//					} catch (UnsupportedEncodingException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//        			int tt=(buffer[1]& 0xFF)<<24|(buffer[2]& 0xFF)<<16|(buffer[3]& 0xFF)<<8|(buffer[4]& 0xFF);	
//        			System.out.println("预估得到的长度是！！！！！！！！！！"+tt);
        			if(strReceive.length()>75000){
        				
        			Process(strReceive);
//        			buffer=new byte[]{0};
        			strReceive="";
        			}
        		}
        	}
        };
        
//        mConnectThread=new connectThread();
//        mConnectThread.start();
		
	}
	
	private String[] removenull(String[] jiancexmreal) {
		// TODO Auto-generated method stub
		int t=0;
		for(int i=0;i<jiancexmreal.length;i++){
			if(jiancexmreal[i]!=null)  t++;
		}
		String[] result=new String[t];
		int m=0;
		for(int i=0;i<jiancexmreal.length ;i++){
			if(jiancexmreal[i]!=null){
				result[m]=jiancexmreal[i];
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
		
		//接收到检测项目的数据，然后处理，存入数据库
       if(!did){
    	   
//    	   try {
//   			saveToSDCard("jinluokb.txt",strReceive2);
//   		} catch (Exception e) {
//   			// TODO Auto-generated catch block
//   			e.printStackTrace();
//   		}

    	   
  			  
		// TODO Auto-generated method stub
        did=true;
        System.out.println("开始处理数据");
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
//	         System.out.println("~~~~~~~编号是数字~~~~~~~"+i);
	     }else if(isNumeric(mingcheng)){
	    	 if(tokens.hasMoreElements()){
	    		 i=Integer.parseInt(mingcheng);
	    		 hash.put(i, jieshi+";"+(String)tokens.nextElement());
//	    		 System.out.println("~~~~~~~名称是数字~~~~~~~"+i);
	    	 }
	    	
	     }else if(isNumeric(jieshi)){
	    	 if(tokens.hasMoreElements()){
	    		 i=Integer.parseInt(jieshi);
	    		 String mc=(String)tokens.nextElement();
	    		 if(tokens.hasMoreElements()){
	    		 hash.put(i, mc+";"+(String)tokens.nextElement());
//	    		 System.out.println("~~~~~~~名解释是数字~~~~~~~"+i);
	    		 }
	    	 }
	     }
      }
       }
//       mHandler.sendEmptyMessage(0);
	}
		

	public void layoutbutton(final String[] paras,String[] paras1){
//	第一个参数是realname
		    RelativeLayout layout = new RelativeLayout(this);  			
	        Button Btn[] = new Button[paras.length];
	        int j = -1;
	        for  (int i=0; i<paras.length; i++) {      
	              Btn[i]=new Button(this);
	              Btn[i].setId(2000+i); 
	              Btn[i].setText(paras[i]);
	              RelativeLayout.LayoutParams btParams = new RelativeLayout.LayoutParams ((width-30)/2,130);  //设置按钮的宽度和高度
	              if (i%2 == 0) {
	               j++;
	              }
	              btParams.leftMargin = 10+ ((width-30)/2+10)*(i%2);   //横坐标定位        
	              btParams.topMargin = 30 + 130*j;   //纵坐标定位       
	              layout.addView(Btn[i],btParams);   
	              //将按钮放入layout组件
	        }
	        
	        
	       
	        
	        for (int k = 0; k <= Btn.length-1; k++) { 
	        	   //这里不需要findId，因为创建的时候已经确定哪个按钮对应哪个Id
	        	   Btn[k].setTag(k);                //为按钮设置一个标记，来确认是按下了哪一个按钮
//	        	   System.out.println("~~~~~~~~~~"+paras[k]);
	        		   
	        	   Btn[k].setOnClickListener(new Button.OnClickListener() {        		   
	        	    @Override
	        	        public void onClick(View v) {
	        	    	 int i = (Integer) v.getTag();
	        	    	 if(paras[i]!=null && isNumeric(paras[i])){
	        	    		 title=" ";
		        	    	 explain="点击检测项目，获取更新";
		        	    	new  AlertDialog.Builder(ExaminationItem.this)    
		        	    	                .setTitle(title )  
		        	    	                .setMessage(explain)  
		        	    	                .setPositiveButton("确定" ,  null )  
		        	    	                .show();
	        	    	 }else if(paras[i]!=null){
	        	    	 title=paras[i].split(";")[0];
	        	    	 explain=paras[i].split(";")[1];
	        	    	new  AlertDialog.Builder(ExaminationItem.this)    
	        	    	                .setTitle(title )  
	        	    	                .setMessage(explain)  
	        	    	                .setPositiveButton("确定" ,  null )  
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
            	clientSocket.connect(new InetSocketAddress("110.86.22.138", 7772), 10000);  
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
//                  while((i = this.inStream.read(this.buf)) !=1 );
                    i=this.inStream.read(this.buf);
                } catch (IOException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }

//字符编码转换
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
//              System.out.println("接受到的数据是"+this.str);
                Message msg = new Message();
                msg.obj = this.str;
               
//发送消息
//                size=size+str.length();
//                System.out.println("接收到额数据大小是 !!!!!!!!!!! "+size);
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
	
}
public static String stringToHexString(String strPart) {
    String hexString = "";
    for (int i = 0; i < strPart.length(); i++) {
        int ch = (int) strPart.charAt(i);
        String strHex = Integer.toHexString(ch); 
        hexString = hexString + strHex;
    }
    return hexString;
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
                  AlertDialog.Builder builder=new AlertDialog.Builder(this);  //先得到构造器
                  builder.setTitle("购买检测项目服务"); //设置标题
                  builder.setIcon(R.drawable.yingyang);
                  builder.setMessage("您还没有购买查看检测项目的服务，若要使用请先购买"); //设置内容
//                builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可
                  builder.setPositiveButton("确定购买", new DialogInterface.OnClickListener() { //设置确定按钮
             @Override
              public void onClick(DialogInterface dialog, int which) {
                   dialog.dismiss(); //关闭dialog
                   Intent intent = new Intent(ExaminationItem.this, InsuranceWeb.class);
		        	 Bundle bundle=new Bundle();
                  bundle.putString("url", WebInfoConfig.jiancexiangmu);
                intent.putExtras(bundle);
			      startActivity(intent);
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
} 

}
