package com.jiayujie.webview;

import com.jiayujie.android_customeselector.R;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;

public class WebViewActivity extends Activity {

	private WebView  webView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO 自动生成的方法存根
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webview_layout);
		String url = getIntent().getStringExtra("url");
		
		webView = (WebView)this.findViewById(R.id.content_webview);
		webView.setWebViewClient(new MyWebClient());
		
		WebSettings settings = webView.getSettings();
		settings.setJavaScriptEnabled(true);
		settings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		
		
		webView.loadUrl(url);
		
	}
	
	@Override
	public void onBackPressed() {
		// TODO 自动生成的方法存根
		if (webView.canGoBack()) {
			webView.goBack();
		}else {
			finish();
		}
	}
}
