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

import woodyx.basicapi.screen.XScreen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.tengames.wheeloffortune.main.Assets;
import com.tengames.wheeloffortune.main.WheelOfFortune;
import com.tengames.wheeloffortune.objects.Player;
import com.tengames.wheeloffortune.setting.HighScoreManager;
import com.tengames.wheeloffortune.setting.SoundManager;

public class StandbyScreen extends XScreen implements Screen {
	private WheelOfFortune game;
	
	private Stage stage;
	
	private Label lbWelcome;
	private Label lbQuiz;
	private Label lbAlert;
	
	private Label lbCongras;
	private Label lbGoodBye;
	
	private String playerName; // save player's name
	
	public StandbyScreen(WheelOfFortune game) {
		super(WheelOfFortune.DEFAULT_WIDTH, WheelOfFortune.DEFAULT_HEIGHT);
		this.game = game;
		
		initialize();
	}

	@Override
	public void initialize() {
		stage = new Stage(WheelOfFortune.DEFAULT_WIDTH, WheelOfFortune.DEFAULT_HEIGHT, true);
		if (!InputScreen.isFinishedGame) {
			initializePlayers();
			initializeText();
			// play music background
			SoundManager.playMusic(Assets.muStandby, true);
		} else {
//			 initialize finished
			initializeFinish();
			// play music background
			SoundManager.playMusic(Assets.muGame, true);
		}
	}
	
	private void initializeText() {
		lbWelcome = new Label("", Assets.lbStyleWelCome);
		lbWelcome.setAlignment(Align.center);
		String textRound = "" + InputScreen.roundNumber;
		String firstText = WheelOfFortune.S_FIRST_PERSON;
		if (InputScreen.roundNumber == 4) {
			textRound = "VÒNG ĐẶC BIỆT";
			firstText = "NGƯỜI CHƠI: ";
		}
		lbWelcome.setText(WheelOfFortune.S_WELCOME_ROUND + textRound + "\n\n" + firstText + playerName);
		lbWelcome.setPosition(400, 425);
		
		lbQuiz = new Label("", Assets.lbStyleQuiz);
		lbQuiz.setAlignment(Align.center);
		lbQuiz.setText("\n" + WheelOfFortune.S_QUIZ + InputScreen.roundNumber + "\n\n" 
		+ WheelOfFortune.S_TOPIC + WheelOfFortune.conectDataBases.get(InputScreen.roundNumber-1).getTopic() + "\n\n"
		+ WheelOfFortune.S_NUMBER_TEXT + WheelOfFortune.conectDataBases.get(InputScreen.roundNumber-1).getNumberChar() + " chữ cái: \n\n" 
		+ WheelOfFortune.conectDataBases.get(InputScreen.roundNumber-1).getHint());
		lbQuiz.setPosition(400, 300);
		
		lbAlert = new Label("", Assets.lbStyle);
		lbAlert.setAlignment(Align.center);
		lbAlert.setText(WheelOfFortune.S_TOUCH);
		lbAlert.setPosition(400, 80);
		
		stage.addActor(lbWelcome);
		stage.addActor(lbQuiz);
		stage.addActor(lbAlert);
	}
	
	private void initializeFinish() {
		// take final result
		int tempScore = InputScreen.players.get(InputScreen.winner).getScoreRound();
		InputScreen.players.get(InputScreen.winner).increaseScoreGame(tempScore); // increase score of game
		tempScore = InputScreen.players.get(InputScreen.winner).getScoreGame();
		
		// initialize text
		lbCongras = new Label("", Assets.lbStyleQuiz);
		lbCongras.setAlignment(Align.center);
		String infor = "Bạn: " + InputScreen.players.get(InputScreen.winner).getName() + "\n\n"
						+ InputScreen.players.get(InputScreen.winner).getAge() + " tuổi" + "\n\n"
						+ "Đến từ: " + InputScreen.players.get(InputScreen.winner).getAddress() + "\n\n";
		String winText = WheelOfFortune.S_PASS_SPECIAL;
		if (!InputScreen.isPassSpecialRound) winText = WheelOfFortune.S_FAIL_SPECIAL;
		lbCongras.setText(WheelOfFortune.S_CONGRAS_FINISH + infor + winText + tempScore + WheelOfFortune.S_GOODBYE);
		lbCongras.setPosition(400, 320);
		
		lbGoodBye = new Label("", Assets.lbStyle);
		lbGoodBye.setAlignment(Align.center);
		lbGoodBye.setText(WheelOfFortune.S_TOUCH2);
		lbGoodBye.setPosition(400, 50);
		
		stage.addActor(lbCongras);
		stage.addActor(lbGoodBye);
		
		// initialize player
		InputScreen.players.get(InputScreen.winner).updateState(Player.STATE_FLYUP);
		stage.addActor(InputScreen.players.get(InputScreen.winner));
		
	}
	
	private void initializePlayers() {
		for (int i = 0; i < InputScreen.players.size(); i++) {
			// reset score
			int tempScore = InputScreen.players.get(i).getScoreRound();
			InputScreen.players.get(i).increaseScoreGame(tempScore); // increase score of game
			InputScreen.players.get(i).decreaseScoreRound(tempScore); // reset score
			InputScreen.players.get(i).resetMistake(); // reset mistake
		}
		
		if (InputScreen.roundNumber == 4) {
			// search winner
			int maxScore = InputScreen.players.get(0).getScoreGame();
			for (int i = 0; i < InputScreen.players.size(); i++) {
				if (maxScore < InputScreen.players.get(i).getScoreGame()) {
					maxScore = InputScreen.players.get(i).getScoreGame();
				}
			}
			
			for (int i = 0; i < InputScreen.players.size(); i++) {
				if (InputScreen.players.get(i).getScoreGame() == maxScore) {
					InputScreen.winner = (byte)i;
				}
			}
			// set round is special round
			InputScreen.isSpecialRound = true;
		}
		
		switch (InputScreen.roundNumber) {
		case 1:
			stage.addActor(InputScreen.players.get(0));
			playerName = InputScreen.players.get(0).getName();
			
			break;
			
		case 2:
			stage.addActor(InputScreen.players.get(1));
			playerName = InputScreen.players.get(1).getName();
			
			break;
			
		case 3:
			stage.addActor(InputScreen.players.get(2));
			playerName = InputScreen.players.get(2).getName();
			
			break;
			
		case 4:
			stage.addActor(InputScreen.players.get(InputScreen.winner));
			playerName = InputScreen.players.get(InputScreen.winner).getName();
			
			break;
			
			default:
				break;
		}
		
	}

	@Override
	public void update(float deltaTime) {
		if (!InputScreen.isFinishedGame) {
			updatePlayer(deltaTime);
			
			// if touch screen
			if (Gdx.input.justTouched()) {
				// play sound
				SoundManager.playSound(Assets.soButtonClick);
				
				game.setScreen(new GameScreen(game));
			}
		} else {
//			 update finished game
			updateFinish(deltaTime);
			
			// if touch screen
			if (Gdx.input.justTouched()) {
				// save score
				HighScoreManager.load();
				HighScoreManager.addPlayer(InputScreen.players.get(InputScreen.winner));
				HighScoreManager.save();
				
				// play sound
				SoundManager.playSound(Assets.soButtonClick);

				game.setScreen(new MainMenuScreen(game));
			}
		}

	}
	
	private void updatePlayer(float deltaTime) {
		switch (InputScreen.roundNumber) {
		case 1:
			InputScreen.players.get(0).dispose();
			InputScreen.players.get(0).updateState(Player.STATE_FLYUP);
			InputScreen.players.get(0).update(deltaTime);
			break;
			
		case 2:
			InputScreen.players.get(1).dispose();
			InputScreen.players.get(1).updateState(Player.STATE_FLYUP);
			InputScreen.players.get(1).update(deltaTime);
			break;
			
		case 3:
			InputScreen.players.get(2).dispose();
			InputScreen.players.get(2).updateState(Player.STATE_FLYUP);
			InputScreen.players.get(2).update(deltaTime);
			break;
			
		case 4:
			InputScreen.players.get(InputScreen.winner).dispose();
			InputScreen.players.get(InputScreen.winner).updateState(Player.STATE_FLYUP);
			InputScreen.players.get(InputScreen.winner).update(deltaTime);
			break;
			
			default:
				break;
			
		}
	}
	
	private void updateFinish(float deltaTime) {
		InputScreen.players.get(InputScreen.winner).update(deltaTime);
		InputScreen.players.get(InputScreen.winner).setTextScore(InputScreen.players.get(InputScreen.winner).getScoreGame());
		// update effect
		Assets.efStar.setPosition(InputScreen.players.get(InputScreen.winner).getX() + InputScreen.players.get(InputScreen.winner).getWidth()/2, InputScreen.players.get(InputScreen.winner).getY() + InputScreen.players.get(InputScreen.winner).getHeight()/2);
		Assets.efStar.start();
	}
	
	@Override
	public void draw() {
		bgDrawable(true);
		renderBackGround();
		bgDrawable(false);
		renderStage();
		objDrawable(true);
		renderEffect();
		objDrawable(false);
	}
	
	private void renderStage() {
		stage.draw();
	}
	
	private void renderBackGround() {
		batch.draw(Assets.txBackground_standby, 0, 0);
	}
	
	private void renderEffect() {
		if (InputScreen.isFinishedGame) {
			Assets.efStar.draw(batch, Gdx.graphics.getDeltaTime());
		}
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
