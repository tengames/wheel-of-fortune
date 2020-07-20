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

import java.util.ArrayList;

import woodyx.basicapi.screen.XScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.tengames.wheeloffortune.main.Assets;
import com.tengames.wheeloffortune.main.WheelOfFortune;
import com.tengames.wheeloffortune.objects.InputDialog;
import com.tengames.wheeloffortune.objects.Player;
import com.tengames.wheeloffortune.setting.SoundManager;

public class InputScreen extends XScreen implements Screen {
	
	// static variables
	public static ArrayList<Player> players;
	
	public static byte winner;
	public static byte countPlayer;
	public static byte roundNumber;
	public static boolean isSpecialRound;
	public static boolean isFinishedGame;
	public static boolean isPassSpecialRound;
	
	private WheelOfFortune game;
	private Stage stage;
	
	private Label lbText1;
	private Label lbText2;
	
	private boolean isFinishedInput;
	private boolean canAddStage;
	
	private ArrayList<InputDialog> inputDialogs;
	
	public InputScreen(WheelOfFortune game) {
		super(WheelOfFortune.DEFAULT_WIDTH, WheelOfFortune.DEFAULT_HEIGHT);
		this.game = game;
		
		this.isFinishedInput = false;
		this.canAddStage = false;
		
		// initialize parameter
		countPlayer = 0;
		roundNumber = 0;
		isSpecialRound = false;
		isFinishedGame = false;
		isPassSpecialRound = false;
		
		// play music background
		SoundManager.playMusic(Assets.muStandby, true);
		
		initialize();
	}

	@Override
	public void initialize() {
		stage = new Stage(WheelOfFortune.DEFAULT_WIDTH, WheelOfFortune.DEFAULT_HEIGHT, true);
		Gdx.input.setInputProcessor(stage);
		initializeTexts();
		initializePlayers();
		initializeDialogs();
	}
	
	private void initializeTexts() {
		lbText1 = new Label("", Assets.lbStyle);
		lbText1.setAlignment(Align.center);
		lbText1.setText(WheelOfFortune.S_WELCOME);
		lbText1.setPosition(400, 350);
		
		lbText2 = new Label("", Assets.lbStyle);
		lbText2.setAlignment(Align.center);
		lbText2.setText(WheelOfFortune.S_TOUCH);
		lbText2.setPosition(400, 80);
		
	}
	
	private void initializeDialogs() {
		inputDialogs = new ArrayList<InputDialog>();
		InputDialog inputDialog = new InputDialog(players.get(0), countPlayer, Assets.dgStyle, Assets.trDialogGame, 1210, 65);
		stage.addActor(inputDialog);
		inputDialogs.add(inputDialog);
	}
	
	private void initializePlayers() {
		players = new ArrayList<Player>();
		
		Player player1 = new Player(Assets.dgStyle, -200, 200, Assets.trBgAvatar1, 1);
		Player player2 = new Player(Assets.dgStyle, 400, 600, Assets.trBgAvatar2, 2);
		Player player3 = new Player(Assets.dgStyle, 1000, 200, Assets.trBgAvatar3, 3);
		
		players.add(0, player1);
		players.add(1, player2);
		players.add(2, player3);
	}

	@Override
	public void update(float deltaTime) {
		updateDialog(deltaTime);
		updatePlayers(deltaTime);
	}
	
	private void updateDialog(float deltaTime) {
		// update dialog
		for (int i = 0; i < inputDialogs.size(); i++) {
			inputDialogs.get(i).update(deltaTime);
			
			// exit input screen
			if (inputDialogs.get(i).isExitPressed()) {
				// play sound
				SoundManager.playSound(Assets.soButtonClick);
				game.setScreen(new MainMenuScreen(game));
			}
			
			// update
			if (countPlayer < 2) {
				if (inputDialogs.get(i).getX() <= -400) {
					countPlayer++;
					inputDialogs.remove(i);
					InputDialog inputDialog = new InputDialog(players.get(countPlayer), countPlayer, Assets.dgStyle, Assets.trDialogGame, 1210, 65);
					stage.addActor(inputDialog);
					inputDialogs.add(inputDialog);
				}
			}
			
			// condition for player's appear
			if (inputDialogs.get(i).getX() <= -400) {
				if (countPlayer == 2) {
					isFinishedInput = true;
					canAddStage = true;
				}
			}

		}
	}
	
	private void updatePlayers(float deltaTime) {
		// add players, text to the stage
		if (canAddStage) {
			// add players
			for (int i = 0; i < players.size(); i++) {
				stage.addActor(players.get(i));
			}
			
			// add text
			stage.addActor(lbText1);
			stage.addActor(lbText2);
			
			canAddStage = false;
		}
		
		// update players
		if (isFinishedInput) {
			for (int i = 0; i < players.size(); i++) {
				// update
				players.get(i).update(deltaTime);
			}
			// can go to the game screen
			if (Gdx.input.justTouched()) {
				// play sound
				SoundManager.playSound(Assets.soButtonClick);
				
				// increase roundNumber
				roundNumber++;
				
				// set standby screen
				game.setScreen(new StandbyScreen(game));
			}
		}
	}
	
	@Override
	public void draw() {
		bgDrawable(true);
		renderBackGround();
		bgDrawable(false);
		renderStage();
	}
	
	private void renderStage() {
		stage.draw();
	}
	
	private void renderBackGround() {
		batch.draw(Assets.txBackground_highscore, 0, 0, 800, 480);
	}
	
	@Override
	public void render(float deltaTime) {
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
		SoundManager.pauseMusic(Assets.muStandby);
	}

	@Override
	public void pause() {
		SoundManager.pauseMusic(Assets.muStandby);
	}

	@Override
	public void resume() {}

	@Override
	public void dispose() {
		SoundManager.pauseMusic(Assets.muStandby);
		stage.dispose();
	}

}
