package com.example.jinluokb;


import java.util.HashMap;
import java.util.Random;

import com.pkusz.constants.WebInfoConfig;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class Signup extends Activity implements OnClickListener, Callback {
	// 填写从短信SDK应用后台注册得到的APPKEY
	private static String APPKEY = "a7114c13a500";

	// 填写从短信SDK应用后台注册得到的APPSECRET
	private static String APPSECRET = "f2c5448ced22970f2066472b9958a39f";

	String name,sex,age,location,psd,psd2;
	private EditText editText1,editText2,editText3,editText4,editText5,editText6;
	
	
	// 短信注册，随机产生头像
	private static final String[] AVATARS = {
		"http://tupian.qqjay.com/u/2011/0729/e755c434c91fed9f6f73152731788cb3.jpg",
		"http://99touxiang.com/public/upload/nvsheng/125/27-011820_433.jpg",
		"http://img1.touxiang.cn/uploads/allimg/111029/2330264224-36.png",
		"http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339485237265.jpg",
		"http://diy.qqjay.com/u/files/2012/0523/f466c38e1c6c99ee2d6cd7746207a97a.jpg",
		"http://img1.touxiang.cn/uploads/20121224/24-054837_708.jpg",
		"http://img1.touxiang.cn/uploads/20121212/12-060125_658.jpg",
		"http://img1.touxiang.cn/uploads/20130608/08-054059_703.jpg",
		"http://diy.qqjay.com/u2/2013/0422/fadc08459b1ef5fc1ea6b5b8d22e44b4.jpg",
		"http://img1.2345.com/duoteimg/qqTxImg/2012/04/09/13339510584349.jpg",
		"http://img1.touxiang.cn/uploads/20130515/15-080722_514.jpg",
		"http://diy.qqjay.com/u2/2013/0401/4355c29b30d295b26da6f242a65bcaad.jpg"
	};

	private boolean ready;
	private Dialog pd;
	private TextView tvNum;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.signup);
		   WebView browser=(WebView)findViewById(R.id.webview);
	        browser.loadUrl(WebInfoConfig.zhuce);  
	          
	        //设置可自由缩放网页  
	        browser.getSettings().setSupportZoom(true);  
	        browser.getSettings().setBuiltInZoomControls(true);  
	          
	        // 如果页面中链接，如果希望点击链接继续在当前browser中响应，  
	        // 而不是新开Android的系统browser中响应该链接，必须覆盖webview的WebViewClient对象  
	        browser.setWebViewClient(new WebViewClient() {  
	            public boolean shouldOverrideUrlLoading(WebView view, String url)  
	            {   
	                  //  重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边  
	                     view.loadUrl(url);  
	                        return true;  
	            }         
	             }); 
	        
//		Button btnRegist = (Button) findViewById(R.id.btn_bind_phone);
//		View btnContact = findViewById(R.id.rl_contact);
//		tvNum = (TextView) findViewById(R.id.tv_num);
//		editText1 =(EditText)findViewById(R.id.name);       
//        editText2 =(EditText)findViewById(R.id.sex);          
//        editText3 =(EditText)findViewById(R.id.age);     
//        editText4 =(EditText)findViewById(R.id.location); 
//        editText5 =(EditText)findViewById(R.id.psd);  
//        editText6 =(EditText)findViewById(R.id.psd2);  
        
//		tvNum.setVisibility(View.GONE);
//		btnRegist.setOnClickListener(this);
//		btnContact.setOnClickListener(this);

//    	loadSharePrefrence();
//		showAppkeyDialog();
//		initSDK();
//		setSharePrefrence();
	}

//	private void showAppkeyDialog() {
//		final Dialog dialog = new Dialog(this, R.style.CommonDialog);
//		dialog.setContentView(R.layout.smssdk_set_appkey_dialog);
//		final EditText etAppKey = (EditText) dialog.findViewById(R.id.et_appkey);
//		etAppKey.setText(APPKEY);
//		final EditText etAppSecret = (EditText) dialog.findViewById(R.id.et_appsecret);
//		etAppSecret.setText(APPSECRET);
//		OnClickListener ocl = new OnClickListener() {
//			public void onClick(View v) {
//				if (v.getId() == R.id.btn_dialog_ok) {
//					APPKEY = etAppKey.getText().toString().trim();
//					APPSECRET = etAppSecret.getText().toString().trim();
//					if (TextUtils.isEmpty(APPKEY) || TextUtils.isEmpty(APPSECRET)) {
//						Toast.makeText(v.getContext(), R.string.smssdk_appkey_dialog_title,
//								Toast.LENGTH_SHORT).show();
//					} else {
//						dialog.dismiss();
//						setSharePrefrence();
//						initSDK();
//					}
//				} else {
//					dialog.dismiss();
//					finish();
//				}
//			}
//		};
//		dialog.findViewById(R.id.btn_dialog_ok).setOnClickListener(ocl);
//		dialog.findViewById(R.id.btn_dialog_cancel).setOnClickListener(ocl);
//		dialog.setCancelable(false);
//		dialog.show();
//	}
	
	
	
//	@Override  
//    public boolean onKeyDown(int keyCode, KeyEvent event) {  
//        WebView browser=(WebView)findViewById(R.id.web);  
//        // Check if the key event was the Back button and if there's history  
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && browser.canGoBack()) {  
//            browser.goBack();  
//            return true;  
//        }  
//      //  return true;  
//        // If it wasn't the Back key or there's no web page history, bubble up to the default  
//        // system behavior (probably exit the activity)  
//        return super.onKeyDown(keyCode, event);  
//    } 
	
	
	
	
	


	private void loadSharePrefrence() {
		SharedPreferences p = getSharedPreferences("userinfo", 0);
		APPKEY = p.getString("APPKEY", APPKEY);
		APPSECRET = p.getString("APPSECRET", APPSECRET);
	}

	private void setSharePrefrence() {
		SharedPreferences p = getSharedPreferences("userinfo", 0);
		Editor edit = p.edit();
		edit.putString("APPKEY", APPKEY);
		edit.putString("APPSECRET", APPSECRET);
		name=editText1.getText().toString();
		sex=editText2.getText().toString();
    	age=editText3.getText().toString();
    	location=editText4.getText().toString();
    	psd=editText5.getText().toString();
    	psd2=editText6.getText().toString();
    	edit.putString("name", name);
    	edit.putString("sex", sex);
    	edit.putString("age", age);
    	edit.putString("location", location);
    	edit.putString("psd", psd);
    	edit.putString("psd2", psd2);
    	
		edit.commit();
	}

	protected void onDestroy() {
		if (ready) {
			// 销毁回调监听接口
		}
		super.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (ready) {
			// 获取新好友个数
			showDialog();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	public void onClick(View v) {
		switch (v.getId()) {
//		case R.id.btn_bind_phone:
//			
//			setSharePrefrence();
//			// 打开注册页面
//			RegisterPage registerPage = new RegisterPage();
//			registerPage.setRegisterCallback(new EventHandler() {
//				public void afterEvent(int event, int result, Object data) {
//					// 解析注册结果
//					if (result == SMSSDK.RESULT_COMPLETE) {
//						@SuppressWarnings("unchecked")
//						HashMap<String,Object> phoneMap = (HashMap<String, Object>) data;
//						String country = (String) phoneMap.get("country");
//						String phone = (String) phoneMap.get("phone");
//						// 提交用户信息
//						registerUser(country, phone);
//					}
//				}
//			});
//			registerPage.show(this);
//			break;
//		case R.id.rl_contact:
//			tvNum.setVisibility(View.GONE);
//			// 打开通信录好友列表页面
//			ContactsPage contactsPage = new ContactsPage();
//			contactsPage.show(this);
//			break;
		}
	}

	public boolean handleMessage(Message msg) {
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}

		int event = msg.arg1;
		int result = msg.arg2;
		Object data = msg.obj;
//		else if (event == SMSSDK.EVENT_GET_NEW_FRIENDS_COUNT){
//			if (result == SMSSDK.RESULT_COMPLETE) {
//				refreshViewCount(data);
//			} else {
//				((Throwable) data).printStackTrace();
//			}
//		}
		return false;
	}
	// 更新，新好友个数
	private void refreshViewCount(Object data){
		int newFriendsCount = 0;
		try {
			newFriendsCount = Integer.parseInt(String.valueOf(data));
		} catch (Throwable t) {
			newFriendsCount = 0;
		}
		if(newFriendsCount > 0){
			tvNum.setVisibility(View.VISIBLE);
			tvNum.setText(String.valueOf(newFriendsCount));
		}else{
			tvNum.setVisibility(View.GONE);
		}
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
	}
	// 弹出加载框
	private void showDialog(){
		if (pd != null && pd.isShowing()) {
			pd.dismiss();
		}
	}
	// 提交用户信息

}
