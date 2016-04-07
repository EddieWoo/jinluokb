package com.example.jinluokb;



import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.insurance.InsuranceWeb;
import com.pkusz.analyze.Analyze;
import com.pkusz.analyze.Shiliao;
import com.pkusz.analyze.ssf_helper;
import com.pkusz.constants.WebInfoConfig;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends Activity {
	private EditText accountEdit;// 用户名
	private EditText passwordEdit;// 密码
	private Button login,mall;// 登陆按钮
	private Button signup;//注册按钮
	private CheckBox rememberPass;// 记住密码
	private CheckBox denglu;// 自动登陆
	private SharedPreferences shared;
	private Editor editor;
	String APPKEY="a7114c13a500";
	String APPSECRET="f2c5448ced22970f2066472b9958a39f"; 
	
	 String marriage="未婚",age="25",male="男",
	 data="50%50%50%50%50%50%50%50%50%50%50%50%50%50%50%50%50%50%50%50%50%50%50%50%";
	
	  private Socket clientSocket = null;
	    private OutputStream outStream = null;

	    private Handler mHandler = null;

	    private ReceiveThread mReceiveThread = null;
	    private connectThread mConnectThread=null;
	    private boolean stop = true;
	    private String strReceive="";
	    Bundle bundle;
		private String date="1990";
	    
	    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login);
		
		
		accountEdit = (EditText) findViewById(R.id.edit_user);
		passwordEdit = (EditText) findViewById(R.id.edit_psd);
		rememberPass = (CheckBox) findViewById(R.id.checkBox1);
		denglu = (CheckBox) findViewById(R.id.checkBox2);
		login = (Button) findViewById(R.id.btn_login);
		mall = (Button) findViewById(R.id.mall);
		signup=(Button) findViewById(R.id.btn_signup);
		shared = getSharedPreferences("userinfo",Context.MODE_PRIVATE);
		editor = shared.edit();
		// 从SharedPreferences里边取出 记住密码的状态
		 bundle = this.getIntent().getExtras();    
         data=bundle.getString("data");
         age=bundle.getString("age");
         male=bundle.getString("sex");    
         date=bundle.getString("date");
         marriage=bundle.getString("marriage");
         
		if (shared.getBoolean("ISCHECK", false)) {
			// 将记住密码设置为被点击状态
			rememberPass.setChecked(true);
			// 然后将值赋值给EditText
			accountEdit.setText(shared.getString("oa_name", ""));
			passwordEdit.setText(shared.getString("oa_pass", ""));
			// 获取自动登录按钮的状态
			if (shared.getBoolean("AUTO_ISCHECK", false)) {
				// 设置自动登录被点击 然后实现跳转
				denglu.setChecked(true);
			}

		}
		
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				LoginMain();
				
				 if ("admin".equals(accountEdit.getText().toString())
							&& "123456".equals(passwordEdit.getText().toString())) {
						// 创建Intent对象，传入源Activity和目的Activity的类对象
						Intent intent = new Intent(LoginActivity.this, Analyze.class);
						 Bundle bundle=new Bundle();
	                      bundle.putString("age", age);
	                      bundle.putString("data", data);
	                      bundle.putString("marriage", marriage);
	                      bundle.putString("sex", male);
	                      bundle.putString("date", date);
	                      intent.putExtras(bundle);
						// 启动Activity
						startActivity(intent);
						LoginActivity.this.finish();
					}else{
						if(!isOpenNetwork()){
							Toast.makeText(getApplicationContext(), "没有网络连接", 3000).show();
						}
//				 mConnectThread=new connectThread();
//			     mConnectThread.start();
						LoginMain();
						
					}
				 }
			
		});
		
		signup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(LoginActivity.this, Signup.class);
			    startActivity(intent);
			}
		});
		
		mall.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent=new Intent(LoginActivity.this, InsuranceWeb.class);
				Bundle bundle=new Bundle();
                bundle.putString("url", WebInfoConfig.pingce);
                intent.putExtras(bundle);
				 startActivity(intent);
			}
		});
		
		// 将点击的checkBOx存入到users中
		rememberPass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Boolean isChecked1 = rememberPass.isChecked();
				editor.putBoolean("ISCHECK", isChecked1);
				editor.commit();
			}
		});
		
// 设置自动登录默认为不点击
				Boolean value1 = shared.getBoolean("AUTO_ISCHECK", false);
				denglu.setChecked(value1);
				denglu.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						Boolean isChecked2 = denglu.isChecked();
						editor.putBoolean("AUTO_ISCHECK", isChecked2);
						editor.commit();
					}
				});
// 如果记住密码跟自动登录都被选中就选择登录跳转
				if (rememberPass.isChecked() && denglu.isChecked()) {
					LoginMain();
				}

				 
			    mHandler=new Handler()
			    {
			        @Override
			        public void handleMessage(Message msg)
			        {
			            final String strOperationStep;
			            final String strMemberNo=accountEdit.getText().toString();
//			            		"xmuarkin";
			            final String strMemberPwd=passwordEdit.getText().toString();
//			                     "lxfssf8079";
			            
			            final String strCode="";
			            final String strVersion="YYMEMBER";
			            String strSend="";
			            
			            strReceive = strReceive + msg.obj.toString();
			            
			            System.out.println("接收到的数据是"+strReceive);
			           			            
			            if(strReceive.length()<3)
			            {
			                return;
			            }
			            if(!strReceive.substring(strReceive.length() - 3, strReceive.length()).equals("END"))
			            {
			                return;
			            }
			            strOperationStep=strReceive.substring(0, 2);    
			            
			             switch (strOperationStep)
			            {
			                case "01"://验证登录
			                case "00":
			                    try {
			                        strSend=ssf_helper.EnCode_String("ZS%" + Integer.toString(strMemberNo.length())+  "%" + Integer.toString(strMemberPwd.length()) + "%" +Integer.toString(strCode.length())+  "%" +Integer.toString(strVersion.length()) + "%" + strMemberNo + strMemberPwd + strCode + strVersion, "JINLUO.NET");
			                    } catch (UnsupportedEncodingException e) {
			                        e.printStackTrace();
			                    }
			                    SendData("1"+strSend+"END");  
			                    break;
			                case "14":
//                       返回值是      1、11END：用户名密码错误；
//			                	 2、14END：验证通过；
			                	LoginMain();
			                	System.out.println("LoginMainLoginMainLoginMainLoginMainLoginMain");
			                    break;
			                case "11":
			                	Toast.makeText(getApplicationContext(), "密码错误", 3000).show();
			                	break;
			                case "13":
			                	Toast.makeText(getApplicationContext(), "余额不足", 3000).show();
			                	break;
			                default:
			                	Toast.makeText(getApplicationContext(), "无法连接服务器", 3000).show();
			                	break;
			                    
			            }
			            strReceive="";
			             
			        }                    	
			    };
			  
	}

	
	public void LoginMain() {
		       
		// 将信息存入到users里面
		editor.putString("oa_name", accountEdit.getText().toString());
		editor.putString("oa_pass", passwordEdit.getText().toString());
		editor.commit();
		if (TextUtils.isEmpty(accountEdit.getText().toString())) {
			Toast.makeText(this, "请输入用户名", Toast.LENGTH_LONG).show();
			return;
		}
		if (TextUtils.isEmpty(passwordEdit.getText().toString())) {
			Toast.makeText(this, "请输入密码", Toast.LENGTH_LONG).show();
			return;
		}
//		if ("admin".equals(accountEdit.getText().toString())
//				&& "123456".equals(passwordEdit.getText().toString())) {
			// 创建Intent对象，传入源Activity和目的Activity的类对象
			Intent intent = new Intent(LoginActivity.this, Analyze.class);
			 Bundle bundle=new Bundle();
             bundle.putString("age", age);
             bundle.putString("data", data);
             bundle.putString("marriage", marriage);
             bundle.putString("sex", male);
             bundle.putString("date", date);
             intent.putExtras(bundle);
			// 启动Activity
			startActivity(intent);
			LoginActivity.this.finish();
//		} else {
//			// 登录信息错误，通过Toast显示提示信息
//			Toast.makeText(LoginActivity.this, "用户名或密码错误", Toast.LENGTH_SHORT)
//					.show();
//		}

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

        private byte[] buf=new byte[1024*20];
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
                } catch (UnsupportedEncodingException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }
//                System.out.println("接受到的数据是"+this.str);
                Message msg = new Message();
                msg.obj = this.str;
//发送消息
                mHandler.sendMessage(msg);

            }
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
    
    
    private boolean isOpenNetwork() {  
        ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);  
        if(connManager.getActiveNetworkInfo() != null) {  
            return connManager.getActiveNetworkInfo().isAvailable();  
        }  
      
        return false;  
    }  
    
    
	
}
