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

import java.util.ArrayList;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings.Secure;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.tengames.wheeloffortune.main.AndroidInterface;
import com.tengames.wheeloffortune.main.ConnectDatabase;
import com.tengames.wheeloffortune.main.WheelOfFortune;

public class MainActivity extends AndroidApplication implements AndroidInterface {
	private static final String ADMOBID_INTERSTITIAD = "xxx-xxx-xxx";
	private static final String PROPERTY_ID = "xx-xxx-xx";

	private static InterstitialAd mInterstitialAd = null;

	/**
	 * Enum used to identify the tracker that needs to be used for tracking.
	 *
	 * A single tracker is usually enough for most purposes. In case you do need
	 * multiple trackers, storing them all in Application object helps ensure
	 * that they are created only once per application instance.
	 */
	private enum TrackerName {
		APP_TRACKER, // Tracker used only in this app.
		GLOBAL_TRACKER, // Tracker used by all the apps from a company. eg:
						// roll-up tracking.
		ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a
							// company.
	}

	private static HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();

	private ProgressDialog dgProgress = null;
	
	// initialize string constant
	private static final String STRING_GIFT = " Bạn Nhận Được\n Một Món Quà!";
	private static final String STRING_LUCKY = " Mời Bạn Mở\n Một Ô Chữ!";
	private static final String STRING_PLAYER = " Mời Người Chơi ";
	private static final String STRING_ALERT = " Không Hợp Lệ!\n Mời Quay Lại.";
	private static final String STRING_LOSTPLAY = "\n Mất Quyền Chơi!";
	private static final String STRING_MISTAKE = " Trả Lời Sai!\n Mời Thử Lại.";
	private static final String STRING_FAIL = " Rất Tiếc!\n Bạn Đã Thua";
	private static final String STRING_HINT = "NHẮC NHỞ: Hãy Chạm Vào Avatar Khi Muốn Trả Lời Nhanh Ô Chữ!";
	
	// initialize coreGame
	private WheelOfFortune coreGame;
	
	private Context context;
	
	private DBAdapter mDB;
	
	// take database
	private ArrayList<DatabaseConnect> gameData;
	
	private String playerName;
	
	private SharedPreferences preData;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        context = getApplicationContext();
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
				| WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        
        // create database
		mDB = new DBAdapter(this);
		mDB.createDataBase();
		
		// summon game
		coreGame = new WheelOfFortune();
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        config.useGL20 = true;
        
        coreGame.registerAndroidInterface(this);
        
        initialize(coreGame, config);
        
		// initialize preferences
		preData = PreferenceManager.getDefaultSharedPreferences(this);

		// Create the interstitial.
		mInterstitialAd = new InterstitialAd(this);
		mInterstitialAd.setAdUnitId(ADMOBID_INTERSTITIAD);

		// Create ad request.
		AdRequest adRequest = new AdRequest.Builder().build();
		// Begin loading your interstitial.
		mInterstitialAd.loadAd(adRequest);

		// Enable Advertising Features.
		getTracker(TrackerName.APP_TRACKER).enableAdvertisingIdCollection(true);
    }

	private synchronized Tracker getTracker(TrackerName trackerId) {
		if (!mTrackers.containsKey(trackerId)) {

			GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
			Tracker t = analytics.newTracker(PROPERTY_ID);
					
			mTrackers.put(trackerId, t);

		}
		return mTrackers.get(trackerId);
	}
	
	@Override
	public void getDataBase() {
		// open database
		mDB.open();
		// create random quiz
		mDB.getRandomData();
		gameData = mDB.getGameData();
		
		// reset database
		WheelOfFortune.resetData();
		
		for (int i = 0; i < gameData.size(); i++) {
			ConnectDatabase conectDb = new ConnectDatabase();
			
			conectDb.setAnswer(gameData.get(i).getAnswer());
			String hint = splitHintToView(gameData.get(i).getHint());
			conectDb.setHint(hint);
			conectDb.setKeyWord(gameData.get(i).getKeyWord());
			conectDb.setNumberChar(gameData.get(i).getNumberChar());
			conectDb.setQuizId(gameData.get(i).getQuizId());
			conectDb.setTopic(gameData.get(i).getTopic());
			conectDb.setAccent(mDB.removeAccent(gameData.get(i).getAnswer()));
			
			WheelOfFortune.conectDataBases.add(conectDb);
		}
		
		// remove gameData data
		gameData.clear();
		
		mDB.close();
		
	}
	
	// split hint
	public String splitHintToView(String hint) {
        int i = 1;
        int j;
        StringBuffer strBuff = new StringBuffer(hint);
        int len = hint.length();
        if (len > 50) {
            do {
                int maxLength = 50;
                if (strBuff.charAt(50 * i) == ' ') {
                    strBuff.insert(50 * i, '\n');
                    strBuff.deleteCharAt(49 * i + 1);
                } else {
                    int k = 0;
                    do {
                        j = maxLength * i - k;
                        k++;
                    } while (strBuff.charAt(j) != ' ');
                    strBuff.insert(j, '\n');
                    strBuff.deleteCharAt(j + 1);
                }
                i++;
            } while (50 * i <= len);
        }
        return strBuff.toString();
    }

	// toast change player
	@Override
	public void toChangePlayer(String name) {
		playerName = STRING_PLAYER + name;
		hlChangePlayer.sendEmptyMessage(0);
	}
	
	private Handler hlChangePlayer = new Handler() {
		public void handleMessage(android.os.Message msg) {
			cutomToast(playerName);
		};
	};

	// toast gift
	@Override
	public void toGift() {
		hlGift.sendEmptyMessage(0);
	}
	
	private Handler hlGift = new Handler() {
		public void handleMessage(android.os.Message msg) {
			cutomToast(STRING_GIFT);
		};
	};

	// toast lucky
	@Override
	public void toLucky() {
		hlLucky.sendEmptyMessage(0);
	}
	
	private Handler hlLucky = new Handler() {
		public void handleMessage(android.os.Message msg) {
			cutomToast(STRING_LUCKY);
		};
	};
	
	// toast alert
	@Override
	public void toAlert() {
		hlAlert.sendEmptyMessage(0);
	}

	private Handler hlAlert = new Handler() {
		public void handleMessage(android.os.Message msg) {
			cutomToast(STRING_ALERT);
		};
	};
	
	// toast lost playing
	@Override
	public void toLostPlay(String name) {
		playerName = "Bạn: " + name + STRING_LOSTPLAY;
		hlLostPlay.sendEmptyMessage(0);
	}
	
	private Handler hlLostPlay = new Handler() {
		public void handleMessage(android.os.Message msg) {
			cutomToast(STRING_LOSTPLAY);
		};
	};
	
	@Override
	public void toMistake() {
		hlMistake.sendEmptyMessage(0);
	}
	
	private Handler hlMistake = new Handler() {
		public void handleMessage(android.os.Message msg) {
			cutomToast(STRING_MISTAKE);
		};
	};
	

	@Override
	public void toFail() {
		hlFail.sendEmptyMessage(0);
	}

	private Handler hlFail = new Handler() {
		public void handleMessage(android.os.Message msg) {
			cutomToast(STRING_FAIL);
		};
	};
	
	@Override
	public void toHint() {
		hlHint.sendEmptyMessage(0);
	}
	
	private Handler hlHint = new Handler() {
		public void handleMessage(android.os.Message msg) {
			Toast.makeText(context, STRING_HINT, Toast.LENGTH_LONG).show();
		};
	};
	
	// custom toast
	private void cutomToast(String string) {
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast_layout, (ViewGroup) findViewById(R.id.toast_layout_root));

		TextView text = (TextView) layout.findViewById(R.id.toast_text);
		text.setText(string);

		Toast toast = new Toast(getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();
	}

	@Override
	public void saveHscore(int addScore) {
		int hscore = getHscore();
		hscore += addScore;
		
		SharedPreferences.Editor editor = preData.edit();
		editor.putInt("hscore", hscore);
		editor.commit();
	}

	@Override
	public int getHscore() {
		int hscore = preData.getInt("hscore", 0);
		return hscore;
	}

	@Override
	public void showDialog(final String text) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				AlertDialog.Builder dgPopup = new AlertDialog.Builder(MainActivity.this);
				dgPopup.setTitle("WARNING");
				dgPopup.setMessage(text);
				dgPopup.setCancelable(false);
				// accept
				dgPopup.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
				dgPopup.show();
			}
		});
	}

	@Override
	public void showToast(final String text) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public void showLoading(final boolean show) {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if (show) {
					dgProgress = ProgressDialog.show(MainActivity.this, "Sending request", "Please wait ...");
				} else {
					dgProgress.dismiss();
				}
			}
		});
	}

	@Override
	public void showIntertitial() {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if(mInterstitialAd.isLoaded()){
					mInterstitialAd.show();
					
				}
				// Create ad request.
				AdRequest adRequest = new AdRequest.Builder().build();
				// Begin loading your interstitial.
				mInterstitialAd.loadAd(adRequest);
			}
		});
	}

	@Override
	public void gotoHighscore(final String link) {
		// open webview
		if (isConnected(this)) {
			Intent myIntent = new Intent(this, WebViewActivity.class);
			myIntent.putExtra("link", link); //Optional parameters
			startActivity(myIntent);
		} else {
			showToast("Please check your internet connection and try again !");
		}		
	}

	@Override
	public void gotoStore(String link) {
		// open webview
		if (isConnected(this)) {
			Intent myIntent = new Intent(this, WebViewActivity.class);
			myIntent.putExtra("link", link); //Optional parameters
			startActivity(myIntent);
		} else {
			showToast("Please check your internet connection and try again !");
		}		
	}
	
	/**
	* Device id
	*/
	@Override
	public String getDeviceId() {
		return Secure.getString(getContentResolver(), Secure.ANDROID_ID);		
	}
	
	/**
	* Google Analytic
	*/
	@Override
	public void traceScene(final String level) {
		// Set screen name.
		getTracker(TrackerName.APP_TRACKER).setScreenName(level);

		// Send a screen view.
		getTracker(TrackerName.APP_TRACKER).send(new HitBuilders.ScreenViewBuilder().build());
	}
	
	@Override
	protected void onStart() {
	    super.onStart();
	    GoogleAnalytics.getInstance(this).reportActivityStart(this);
	}

	@Override
	protected void onStop() {
	    super.onStop();
	    GoogleAnalytics.getInstance(this).reportActivityStop(this);
	}
	
	/**
	 * check Internet connection
	 * @param context
	 * @return
	 */
	public boolean isConnected(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		if (activeNetwork != null && activeNetwork.isConnected()) {
			return true;
		}

		return false;
	}

}
