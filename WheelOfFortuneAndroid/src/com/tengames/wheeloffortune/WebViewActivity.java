/*
The MIT License

Copyright (c) 2013 kong <tengames.inc@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.tengames.wheeloffortune;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;

@SuppressLint({ "NewApi", "SetJavaScriptEnabled" })
public class WebViewActivity extends Activity {
	/* URL saved to be loaded after facebook login */
	private static final String target_url_prefix = "4.ten-games.appspot.com";
	
	private Context mContext = null;
	private WebView mWebview = null;
	private WebView mWebviewPop = null;
	private FrameLayout mContainer = null;
	private ImageView imageSplash = null;
	private String link = null;

	/* For upload file */
	private ValueCallback<Uri> mUploadMessage;
	private final static int FILECHOOSER_RESULTCODE = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
	    // Make this activity, full screen
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
	    
	    // request features
	    getWindow().requestFeature(Window.FEATURE_NO_TITLE);
	    getWindow().requestFeature(Window.FEATURE_PROGRESS);
		
		setContentView(R.layout.activity_web);

		Bundle extras = getIntent().getExtras();
		if (extras != null) {
			link = extras.getString("link");
		}

		mContainer = (FrameLayout) findViewById(R.id.webview_frame);
		mContext = this.getApplicationContext();
		
		// create main webview
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.setAcceptCookie(true);
		mWebview = (WebView) findViewById(R.id.webview);
		WebSettings webSettings = mWebview.getSettings();
		webSettings.setJavaScriptEnabled(true);
		webSettings.setAppCacheEnabled(true);
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setSupportMultipleWindows(true);
		webSettings.setDatabaseEnabled(true);
		webSettings.setDomStorageEnabled(true);
		webSettings.setAllowFileAccess(true);
		
		mWebview.setWebViewClient(new UriWebViewClient());
		mWebview.setWebChromeClient(new UriChromeClient());
		mWebview.loadUrl(link);

		// get image splash
		imageSplash = (ImageView) findViewById(R.id.imgSplash);
	}

	// URI chrome client
	private class UriChromeClient extends WebChromeClient {

		@Override
		public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
			// create popup webview
			mWebviewPop = new WebView(mContext);
			mWebviewPop.setVerticalScrollBarEnabled(false);
			mWebviewPop.setHorizontalScrollBarEnabled(false);
			mWebviewPop.setWebViewClient(new UriWebViewClient());
			mWebviewPop.getSettings().setJavaScriptEnabled(true);
			mWebviewPop.getSettings().setSavePassword(false); // important !!!

			mWebviewPop.setLayoutParams(new FrameLayout.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT));
			mContainer.addView(mWebviewPop);
			
			WebView.WebViewTransport transport = (WebView.WebViewTransport) resultMsg.obj;
			transport.setWebView(mWebviewPop);
			resultMsg.sendToTarget();

			return true;
		}
		
		/**
		 * Show progress
		 */
		@Override
		public void onProgressChanged(WebView view, int newProgress) {
			super.onProgressChanged(view, newProgress);
			
			// show splash screen
			if (newProgress == 100) {
				imageSplash.setVisibility(View.GONE);
			}
		}

		/**
		 * For Upload File (except android 4.4.2, 5.0)
		 * @param uploadMsg
		 */
		// The undocumented magic method override
		// Eclipse will swear at you if you try to put @Override here
		// For Android 3.0+
		@SuppressWarnings("unused")
		public void openFileChooser(final ValueCallback<Uri> uploadMsg) {

			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("image/*");
			WebViewActivity.this.startActivityForResult(
					Intent.createChooser(i, "File Chooser"),
					FILECHOOSER_RESULTCODE);

		}

		// For Android 3.0+
		@SuppressWarnings("unused")
		public void openFileChooser(final ValueCallback<Uri> uploadMsg, final String acceptType) {
			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("*/*");
			WebViewActivity.this.startActivityForResult(
					Intent.createChooser(i, "File Browser"),
					FILECHOOSER_RESULTCODE);
		}

		// For Android 4.1
		@SuppressWarnings("unused")
		public void openFileChooser(final ValueCallback<Uri> uploadMsg, final String acceptType, final String capture) {
			mUploadMessage = uploadMsg;
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.addCategory(Intent.CATEGORY_OPENABLE);
			i.setType("image/*");
			WebViewActivity.this.startActivityForResult(
					Intent.createChooser(i, "File Chooser"),
					WebViewActivity.FILECHOOSER_RESULTCODE);

		}

	}

	private class UriWebViewClient extends WebViewClient {
		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			
			// get path
			String path = Uri.parse(url).getPath();
			
			// if path content facebook link
			if (path.equals("/v2.3/dialog/oauth")) {
				SharedPreferences SP = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
				boolean bFirst = SP.getBoolean("fb", true);
				
				if (!bFirst) {
					if (mWebviewPop != null) {
						mWebviewPop.setVisibility(View.GONE);
						mContainer.removeView(mWebviewPop);
						mWebviewPop = null;
					}
				} else {
					// save value
				    SharedPreferences.Editor editor = SP.edit();
				    editor.putBoolean("fb", false);
				    editor.commit(); // Very important
				}
			}
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// get host
			String host = Uri.parse(url).getHost();

			if (host.equals(target_url_prefix)) {
				return false;
			}

			if (host.equals("m.facebook.com") || host.equals("www.facebook.com")) {
				return false;
			}
			
			// Otherwise, the link is not for a page on my site, so launch
			// another Activity that handles URLs
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(intent);
			
			return true;
		}

		@Override
		public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
			 super.onReceivedSslError(view, handler, error);
		}
	}

	// flipscreen not loading again
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public void onBackPressed() {
		if (mWebviewPop == null) {
			finish(); // finish itself
		}
		
		else { // if mWebviewPop is appear
			mWebviewPop.setVisibility(View.GONE);
			mContainer.removeView(mWebviewPop);
			mWebviewPop = null;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == FILECHOOSER_RESULTCODE) {
			if (null == mUploadMessage) return;
			
			Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
			mUploadMessage.onReceiveValue(result);
			mUploadMessage = null;
		}
	}
}
