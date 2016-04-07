package com.example.insurance;

import com.example.jinluokb.R;
import com.pkusz.constants.WebInfoConfig;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

public class InsuranceWeb extends Activity{

	private WebView mWebView;
	private Bundle bundle;
	private String url1;
	private Button butt,pre,next;
	private static final int ID_NOTIFICATION = 1;  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.insuranceweb);
		bundle = this.getIntent().getExtras();    
        url1=bundle.getString("url");
		
		mWebView = (WebView)findViewById(R.id.webview);
		butt=(Button)findViewById(R.id.button);
		pre=(Button)findViewById(R.id.pre);
		next=(Button)findViewById(R.id.next);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setSupportZoom(true);
		mWebView.getSettings().setBuiltInZoomControls(true);
		mWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		mWebView.getSettings().setDefaultFontSize(18);
		mWebView.loadUrl(url1);

		
		
		butt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				InsuranceWeb.this.finish();
			}
		});
		pre.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mWebView.goBack();   //后退  
			}
		});
		next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mWebView.reload();  //刷新
			}
		});
		
		mWebView.setWebViewClient(new WebViewClient() {
			// Load opened URL in the application instead of standard browser
			// application
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
		});

		mWebView.setWebChromeClient(new WebChromeClient() {
			// Set progress bar during loading
			public void onProgressChanged(WebView view, int progress) {
				InsuranceWeb.this.setProgress(progress * 100);
			}
		});
		
		
		mWebView.setOnKeyListener(new View.OnKeyListener() {    
            @Override    
            public boolean onKey(View v, int keyCode, KeyEvent event) {    
                if (event.getAction() == KeyEvent.ACTION_DOWN) {    
                    if (keyCode == KeyEvent.KEYCODE_BACK && mWebView.canGoBack()) {  //表示按返回键  时的操作  
                        mWebView.goBack();   //后退    
                        //webview.goForward();//前进  
                        return true;    //已处理    
                    }    
                }    
            return false;    
            }    
        });  
		
		mWebView.setWebViewClient(new WebViewClient()
		{    
		    @Override  
		    public void onPageFinished(WebView webView, String url){   
		    	webView.loadUrl("javascript:document.getElementById('mobile').value = '"+"userName"+"';" +
		    	 		"document.getElementById('psw').value='"+"password"+"';");   
//		        webView.loadUrl("javascript:document.getElementsByName('login')[0].click()");  
		    }  
		});  
		
	}
	

}
