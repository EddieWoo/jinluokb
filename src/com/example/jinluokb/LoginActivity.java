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
	private EditText accountEdit;// �û���
	private EditText passwordEdit;// ����
	private Button login,mall;// ��½��ť
	private Button signup;//ע�ᰴť
	private CheckBox rememberPass;// ��ס����
	private CheckBox denglu;// �Զ���½
	private SharedPreferences shared;
	private Editor editor;
	String APPKEY="a7114c13a500";
	String APPSECRET="f2c5448ced22970f2066472b9958a39f"; 
	
	 String marriage="δ��",age="25",male="��",
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
		// ��SharedPreferences���ȡ�� ��ס�����״̬
		 bundle = this.getIntent().getExtras();    
         data=bundle.getString("data");
         age=bundle.getString("age");
         male=bundle.getString("sex");    
         date=bundle.getString("date");
         marriage=bundle.getString("marriage");
         
		if (shared.getBoolean("ISCHECK", false)) {
			// ����ס��������Ϊ�����״̬
			rememberPass.setChecked(true);
			// Ȼ��ֵ��ֵ��EditText
			accountEdit.setText(shared.getString("oa_name", ""));
			passwordEdit.setText(shared.getString("oa_pass", ""));
			// ��ȡ�Զ���¼��ť��״̬
			if (shared.getBoolean("AUTO_ISCHECK", false)) {
				// �����Զ���¼����� Ȼ��ʵ����ת
				denglu.setChecked(true);
			}

		}
		
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				LoginMain();
				
				 if ("admin".equals(accountEdit.getText().toString())
							&& "123456".equals(passwordEdit.getText().toString())) {
						// ����Intent���󣬴���ԴActivity��Ŀ��Activity�������
						Intent intent = new Intent(LoginActivity.this, Analyze.class);
						 Bundle bundle=new Bundle();
	                      bundle.putString("age", age);
	                      bundle.putString("data", data);
	                      bundle.putString("marriage", marriage);
	                      bundle.putString("sex", male);
	                      bundle.putString("date", date);
	                      intent.putExtras(bundle);
						// ����Activity
						startActivity(intent);
						LoginActivity.this.finish();
					}else{
						if(!isOpenNetwork()){
							Toast.makeText(getApplicationContext(), "û����������", 3000).show();
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
		
		// �������checkBOx���뵽users��
		rememberPass.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Boolean isChecked1 = rememberPass.isChecked();
				editor.putBoolean("ISCHECK", isChecked1);
				editor.commit();
			}
		});
		
// �����Զ���¼Ĭ��Ϊ�����
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
// �����ס������Զ���¼����ѡ�о�ѡ���¼��ת
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
			            
			            System.out.println("���յ���������"+strReceive);
			           			            
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
			                case "01"://��֤��¼
			                case "00":
			                    try {
			                        strSend=ssf_helper.EnCode_String("ZS%" + Integer.toString(strMemberNo.length())+  "%" + Integer.toString(strMemberPwd.length()) + "%" +Integer.toString(strCode.length())+  "%" +Integer.toString(strVersion.length()) + "%" + strMemberNo + strMemberPwd + strCode + strVersion, "JINLUO.NET");
			                    } catch (UnsupportedEncodingException e) {
			                        e.printStackTrace();
			                    }
			                    SendData("1"+strSend+"END");  
			                    break;
			                case "14":
//                       ����ֵ��      1��11END���û����������
//			                	 2��14END����֤ͨ����
			                	LoginMain();
			                	System.out.println("LoginMainLoginMainLoginMainLoginMainLoginMain");
			                    break;
			                case "11":
			                	Toast.makeText(getApplicationContext(), "�������", 3000).show();
			                	break;
			                case "13":
			                	Toast.makeText(getApplicationContext(), "����", 3000).show();
			                	break;
			                default:
			                	Toast.makeText(getApplicationContext(), "�޷����ӷ�����", 3000).show();
			                	break;
			                    
			            }
			            strReceive="";
			             
			        }                    	
			    };
			  
	}

	
	public void LoginMain() {
		       
		// ����Ϣ���뵽users����
		editor.putString("oa_name", accountEdit.getText().toString());
		editor.putString("oa_pass", passwordEdit.getText().toString());
		editor.commit();
		if (TextUtils.isEmpty(accountEdit.getText().toString())) {
			Toast.makeText(this, "�������û���", Toast.LENGTH_LONG).show();
			return;
		}
		if (TextUtils.isEmpty(passwordEdit.getText().toString())) {
			Toast.makeText(this, "����������", Toast.LENGTH_LONG).show();
			return;
		}
//		if ("admin".equals(accountEdit.getText().toString())
//				&& "123456".equals(passwordEdit.getText().toString())) {
			// ����Intent���󣬴���ԴActivity��Ŀ��Activity�������
			Intent intent = new Intent(LoginActivity.this, Analyze.class);
			 Bundle bundle=new Bundle();
             bundle.putString("age", age);
             bundle.putString("data", data);
             bundle.putString("marriage", marriage);
             bundle.putString("sex", male);
             bundle.putString("date", date);
             intent.putExtras(bundle);
			// ����Activity
			startActivity(intent);
			LoginActivity.this.finish();
//		} else {
//			// ��¼��Ϣ����ͨ��Toast��ʾ��ʾ��Ϣ
//			Toast.makeText(LoginActivity.this, "�û������������", Toast.LENGTH_SHORT)
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

        private byte[] buf=new byte[1024*20];
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
                } catch (UnsupportedEncodingException e) {
// TODO Auto-generated catch block
                    e.printStackTrace();
                }
//                System.out.println("���ܵ���������"+this.str);
                Message msg = new Message();
                msg.obj = this.str;
//������Ϣ
                mHandler.sendMessage(msg);

            }
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
    
    
    private boolean isOpenNetwork() {  
        ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);  
        if(connManager.getActiveNetworkInfo() != null) {  
            return connManager.getActiveNetworkInfo().isAvailable();  
        }  
      
        return false;  
    }  
    
    
	
}
