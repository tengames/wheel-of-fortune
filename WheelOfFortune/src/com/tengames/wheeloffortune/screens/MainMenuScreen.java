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
package com.tengames.wheeloffortune.screens;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import woodyx.basicapi.screen.XScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tengames.wheeloffortune.json.JSONObject;
import com.tengames.wheeloffortune.main.Assets;
import com.tengames.wheeloffortune.main.GlobalVariables;
import com.tengames.wheeloffortune.main.WheelOfFortune;
import com.tengames.wheeloffortune.objects.AlertDialog;
import com.tengames.wheeloffortune.objects.DynamicButton;
import com.tengames.wheeloffortune.setting.Setting;
import com.tengames.wheeloffortune.setting.SoundManager;

@SuppressWarnings("deprecation")
public class MainMenuScreen extends XScreen implements Screen {
	// rate for touch
	public static final float rateX = WheelOfFortune.DEFAULT_WIDTH / Gdx.graphics.getWidth();
	public static final float rateY = WheelOfFortune.DEFAULT_HEIGHT / Gdx.graphics.getHeight();
	
	private WheelOfFortune game;
	
	private Stage stage;

	// dialogs
	private Dialog dgOption;
	private WindowStyle wStyle;
	private Button btExitDialog;
	private CheckBox optionSound;
	private CheckBox optionMusic;
	private CheckBoxStyle styleCheck;
	
	// buttons
	private DynamicButton buttonPlay; // button for new game
	private DynamicButton buttonOption; // button for optional
	private DynamicButton buttonHighScore; // button for highScore
	private DynamicButton buttonExit; // button for exit game
	private DynamicButton buttonInfor; // button for information
	private DynamicButton buttonHelp; // button for help
	
	private AlertDialog dialogConfirmExit;	// alert for confirm exit game
	
	private boolean flagPlay;
	
	private boolean blackOn; // use for switch light on or off

	public MainMenuScreen(final WheelOfFortune game) {
		super(WheelOfFortune.DEFAULT_WIDTH, WheelOfFortune.DEFAULT_HEIGHT);
		this.game = game;
		
		// load setting
		Setting.load();
		
		initialize();
		
		stage = new Stage(WheelOfFortune.DEFAULT_WIDTH, WheelOfFortune.DEFAULT_HEIGHT, true) {
			private static final int TIME_INTERVAL = 2000; // # milliseconds, desired time passed between two back presses.
			private long mBackPressed;
			
			@Override
			public boolean keyUp(int keyCode) {
				if (keyCode == Keys.BACK) {
					// play sound
					SoundManager.playSound(Assets.soButtonClick);
					
					if (mBackPressed + TIME_INTERVAL > System.currentTimeMillis()) {
						// exit game
						System.exit(0);
					} else {
						game.androidInterface.showToast("Tap back button in order to exit !");
					}

					mBackPressed = System.currentTimeMillis();
				}
				return super.keyUp(keyCode);
			}
			
		};
		Gdx.input.setInputProcessor(stage);
		
		stage.addActor(buttonPlay);
		stage.addActor(buttonOption);
		stage.addActor(buttonHighScore);
		stage.addActor(buttonExit);
		stage.addActor(buttonInfor);
		stage.addActor(buttonHelp);
		stage.addActor(dgOption);
		
		// play music background
		SoundManager.playSound(Assets.soMcVoice);
		SoundManager.playMusic(Assets.muMenu, true);
		
	}
	

	@Override
	public void initialize() {
		initializeDataBase();
		initializeParameters();
		initializeButtons();
		initializeDialog();
	}
	
	private void initializeDataBase() {
		game.androidInterface.getDataBase();
	}
	
	private void initializeParameters() {
		// load save data
		blackOn = false;
		flagPlay = false;
	}
	
	private void initializeDialog() {
		
		btExitDialog = new Button(new TextureRegionDrawable(new TextureRegion(Assets.trAssetNull)));
		btExitDialog.setSize(64, 64);
		btExitDialog.setPosition(416, 352);
		btExitDialog.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// play sound
				SoundManager.playSound(Assets.soButtonClick);
				
				dgOption.setVisible(false);
				blackOn = false;
				Setting.save();
				Gdx.input.setInputProcessor(stage);
			}
		});
		
		styleCheck = new CheckBoxStyle(new TextureRegionDrawable(new TextureRegion(Assets.trAssetNull)), new TextureRegionDrawable(new TextureRegion(Assets.trTickRegion)), Assets.fNormalFont, Color.BLACK);
	
		optionSound = new CheckBox("", styleCheck);
		optionSound.setPosition(357, 148);
		optionSound.setSize(64, 64);
		optionSound.setChecked(Setting.SOUND_ENABLE);
		optionSound.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// play sound
				SoundManager.playSound(Assets.soTickClick);
				
				if (optionSound.isChecked()) {
					// saved
					Setting.SOUND_ENABLE = true;
				} else {
					Setting.SOUND_ENABLE = false;
				}
			}
		});
		
		optionMusic = new CheckBox("", styleCheck);
		optionMusic.setPosition(357, 217);
		optionMusic.setSize(64, 64);
		optionMusic.setChecked(Setting.MUSIC_ENABLE);
		optionMusic.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// play sound
				SoundManager.playSound(Assets.soTickClick);
				
				if (optionMusic.isChecked()) {
					// saved
					Setting.MUSIC_ENABLE = true;
					// play music background
					SoundManager.playMusic(Assets.muMenu, true);
					SoundManager.playSound(Assets.soMcVoice);
				} else {
					Setting.MUSIC_ENABLE = false;
					// pause 
					SoundManager.pauseMusic(Assets.muMenu);
				}
			}
		});
		
		// make a dialog
		wStyle = new WindowStyle();
		wStyle.titleFont = Assets.fNormalFont;
		wStyle.titleFontColor = Color.WHITE;
		
		dgOption = new Dialog("", wStyle);
		dgOption.setBackground(new TextureRegionDrawable(new TextureRegion(Assets.trDialogOption)));
		dgOption.setSize(512, 448);
		dgOption.setPosition(144, 16);
		dgOption.setVisible(false);
		
		dgOption.addActor(btExitDialog);
		dgOption.addActor(optionSound);
		dgOption.addActor(optionMusic);
	}
	
	private void initializeButtons() {
		buttonPlay = new DynamicButton(670, -100, 330, 192, 72, Assets.trButtonPlay_P, Assets.trButtonPlay_R, 1);
		buttonPlay.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// play sound
				SoundManager.playSound(Assets.soButtonClick);
				
				flagPlay = true;
			}
		});
		
		buttonOption = new DynamicButton(600, -200, 250, 240, 72, Assets.trButtonOption_P, Assets.trButtonOption_R, 1);
		buttonOption.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// play sound
				SoundManager.playSound(Assets.soButtonClick);
				
				blackOn = true;
				dgOption.setVisible(true);
			}
		});
		
		buttonHighScore = new DynamicButton(670, -300, 170, 168, 72, Assets.trButtonHighScore_P, Assets.trButtonHighScore_R, 1);
		buttonHighScore.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// play sound
				SoundManager.playSound(Assets.soButtonClick);
				
				//TODO: highscore
				try {
					sendRequest();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		buttonExit = new DynamicButton(600, -400, 95, 144, 72, Assets.trButtonExit_P, Assets.trButtonExit_R, 1);
		buttonExit.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// play sound
				SoundManager.playSound(Assets.soButtonClick);
				
				dialogConfirmExit = new AlertDialog(Assets.dgStyle, Assets.trDialogAlert, Assets.trButtonYes_R, Assets.trButtonYes_P, Assets.trButtonNo_R, Assets.trButtonNo_P, Assets.trButtonAgree_R, Assets.trButtonAgree_P, WheelOfFortune.S_CONFIRM_EXIT, 1);
				stage.addActor(dialogConfirmExit);
				blackOn = true;
			}
		});
		
		// button store
		buttonInfor = new DynamicButton(100, 600, 440, 128, 64, Assets.trButtonInfor_P, Assets.trButtonInfor_R, 1);
		buttonInfor.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// play sound
				SoundManager.playSound(Assets.soButtonClick);
				
				//TODO: go to store
				game.androidInterface.gotoStore(GlobalVariables.STORE_URL);
			}
		});
		
		buttonHelp = new DynamicButton(250, 700, 440, 128, 64, Assets.trButtonHelp_P, Assets.trButtonHelp_R, 1);
		buttonHelp.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// play sound
				SoundManager.playSound(Assets.soButtonClick);
				
				// set screen
				game.setScreen(new HelpScreen(game));
			}
		});
		
	}
	
	private void setButtonVisialbe(boolean flag) {
		buttonPlay.setVisible(flag);
		buttonOption.setVisible(flag);
		buttonHighScore.setVisible(flag);
		buttonExit.setVisible(flag);
		buttonInfor.setVisible(flag);
		buttonHelp.setVisible(flag);
	}

	@Override
	public void update(float deltaTime) {
		updateButton(deltaTime);
		updateDialog(deltaTime);
	}
	
	private void updateDialog(float deltaTime) {
		if (dialogConfirmExit != null) {
			
			// confirm exit
			if (dialogConfirmExit.getIsYes()) {
				// play sound
				SoundManager.playSound(Assets.soButtonClick);
				// exit game
				System.exit(0);
			}
			
			// cancel
			if (dialogConfirmExit.getIsNo()) {
				// play sound
				SoundManager.playSound(Assets.soButtonClick);
				
				blackOn = false;
				dialogConfirmExit = null;

			}
		}
	}
	
	private void updateButton(float deltaTime) {
		buttonPlay.update(deltaTime, DynamicButton.STATE_DEFAULT);
		buttonOption.update(deltaTime, DynamicButton.STATE_DEFAULT);
		buttonHighScore.update(deltaTime, DynamicButton.STATE_DEFAULT);
		buttonExit.update(deltaTime, DynamicButton.STATE_DEFAULT);
		buttonInfor.update(deltaTime, DynamicButton.STATE_DEFAULT);
		buttonHelp.update(deltaTime, DynamicButton.STATE_DEFAULT);
		
		if (flagPlay) {
			buttonPlay.update(deltaTime, DynamicButton.STATE_CLICKED);
		}
		
		if (buttonPlay.getChecked()) {
			game.setScreen(new InputScreen(game));
		}
		
	}
	
	// HTTP GET request
	@SuppressWarnings({ "resource", "unused" })
	private void sendRequest() throws Exception {
		game.androidInterface.showLoading(true);
 
		HttpClient client = new DefaultHttpClient();
		HttpGet request = new HttpGet(getHighScoreReq(game.androidInterface.getDeviceId(), "4996618386407424", "Wheel%20Of%20Fortune", (int)(game.androidInterface.getHscore()/100), ""));
		
		// add request header
		request.addHeader("User-Agent", "Android");
		HttpResponse response = client.execute(request);
		BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
		StringBuffer result = new StringBuffer();
		String line = "";
		while ((line = rd.readLine()) != null) {
			result.append(line);
		}
 
		game.androidInterface.showLoading(false);
		
		// show result and code
		if (response == null) {
			game.androidInterface.showToast("Network Error !");
		}
		
		if (response.getStatusLine().getStatusCode() == 600) { // error code
			game.androidInterface.showDialog(result.toString());
		} else if (response.getStatusLine().getStatusCode() == 200) { // valid code
			JSONObject json = new JSONObject(result.toString());
			
			if (!json.getString(GlobalVariables.RES_INFORM).equals("")) {
				
			}
			
			if (!json.getString(GlobalVariables.RES_BONUS).equals("")) {

			}

			if (!json.getString(GlobalVariables.RES_MESSAGE).equals("")) {

			}

			if (!json.getString(GlobalVariables.RES_LINK).equals("")) {
				game.androidInterface.gotoHighscore(json.getString(GlobalVariables.RES_LINK));
			}

		}
 
	}
	
	private String getHighScoreReq(String deviceId, String gameId, String gameName, int score, String params) {
		return (GlobalVariables.HSCORE_URL + "?" + GlobalVariables.REQ_DEVICEID + "=" + deviceId + "&" + GlobalVariables.REQ_GAMEID + "=" + gameId + "&" + GlobalVariables.REQ_GAMENAME + "=" + gameName + "&" + GlobalVariables.REQ_DEVICETYPE + "=" + GlobalVariables.DEV_ADR + "&" + GlobalVariables.REQ_SCORE + "=" + score + "&" + GlobalVariables.REQ_PARAMS + "=" + params);
	}

	private void renderStage() {
		stage.draw();
	}
	
	private void renderBlack() {
		if (blackOn) {
			setButtonVisialbe(!blackOn);
			batch.draw(Assets.trBlackRegion, -1, -1, 801, 481);
		} else {
			setButtonVisialbe(!blackOn);
		}
	}
	
	private void renderObjects() {
		renderBlack();
	}
	
	@Override
	public void draw() {
		bgDrawable(true);
		renderBackground();
		bgDrawable(false);
		objDrawable(true);
		renderObjects();
		objDrawable(false);
		renderStage();
	}
	
	private void renderBackground() {
		batch.draw(Assets.txBackground_menu, 0, 0, WheelOfFortune.DEFAULT_WIDTH, WheelOfFortune.DEFAULT_HEIGHT);
	}

	@Override
	public void render(float deltaTime) {
		loopTime(deltaTime, 0.1f);
		update(deltaTime);
		clearScreen();
		draw();
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void show() {}

	@Override
	public void hide() {
		SoundManager.pauseMusic(Assets.muMenu);
	}

	@Override
	public void pause() {
		SoundManager.pauseMusic(Assets.muMenu);
		Setting.save();
	}

	@Override
	public void resume() {}

	@Override
	public void dispose() {
		SoundManager.pauseMusic(Assets.muMenu);
		stage.dispose();
	}

}
