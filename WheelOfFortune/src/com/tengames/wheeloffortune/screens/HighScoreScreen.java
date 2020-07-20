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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.tengames.wheeloffortune.main.Assets;
import com.tengames.wheeloffortune.main.WheelOfFortune;
import com.tengames.wheeloffortune.objects.DynamicButton;
import com.tengames.wheeloffortune.setting.HighScoreManager;
import com.tengames.wheeloffortune.setting.SoundManager;

public class HighScoreScreen extends XScreen implements Screen {
	private static final String tab = "    ---    ";
	
	private WheelOfFortune game;
	
	private Stage stage;
	
	private DynamicButton btMenu;
	
	private Label lbHighScore;
	private ArrayList<Label> lbHighScores;
	
	private boolean flagMainMenu;
	
	public HighScoreScreen(WheelOfFortune game) {
		super(WheelOfFortune.DEFAULT_WIDTH, WheelOfFortune.DEFAULT_HEIGHT);
		this.game = game;
		
		initialize();
	}
	
	@Override
	public void initialize() {
		stage = new Stage(WheelOfFortune.DEFAULT_WIDTH, WheelOfFortune.DEFAULT_HEIGHT, true);
		Gdx.input.setInputProcessor(stage);
		
		// initialize parameters
		flagMainMenu = false;
		
		// initialize button
		btMenu = new DynamicButton(670, 600, 420, 256, 96, Assets.trButtonMenu_P, Assets.trButtonMenu_R, 1);
		btMenu.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// play sound
				SoundManager.playSound(Assets.soButtonClick);
				
				flagMainMenu = true;
			}
		});
		stage.addActor(btMenu);
		
		// initialize labels
		lbHighScore = new Label("", Assets.lbStyle);
		lbHighScore.setPosition(400, 400);
		lbHighScore.setAlignment(Align.center);
		lbHighScore.setText(WheelOfFortune.S_HIGHSCORE);
		stage.addActor(lbHighScore);
		
		// initialize players
		lbHighScores = new ArrayList<Label>(6);
		
		// take data from setting
		
		for (int i = 0; i < HighScoreManager.list.size(); i++) {
			Label lbPlayer = new Label("", Assets.lbStyleQuiz);
			lbPlayer.setAlignment(Align.left);
			lbPlayer.setPosition(120, 320 - 45*i);
			lbPlayer.setText("" + (i+1) + ". " + HighScoreManager.list.get(i).getName() + tab + HighScoreManager.list.get(i).getAge() + " tuổi" + tab + HighScoreManager.list.get(i).getAddress() + tab + HighScoreManager.list.get(i).getScore() + " điểm" );
			lbHighScores.add(lbPlayer);
		}
		
		for (int i = 0; i < lbHighScores.size(); i++) {
			stage.addActor(lbHighScores.get(i));
		}
		
		// play music background
		SoundManager.playMusic(Assets.muGame, true);
	}

	@Override
	public void update(float deltaTime) {
		updateButton(deltaTime);
	}

	private void updateButton(float deltaTime) {
		btMenu.update(deltaTime, DynamicButton.STATE_DEFAULT);
		
		if (flagMainMenu) {
			btMenu.update(deltaTime, DynamicButton.STATE_CLICKED);
		}
		
		if (btMenu.getChecked()) {
			game.setScreen(new MainMenuScreen(game));
		}
	}
	
	@Override
	public void draw() {
		renderBackground();
	}
	
	private void renderStage() {
		stage.draw();
	}
	
	private void renderBackground() {
		bgDrawable(true);
		batch.draw(Assets.txBackground_highscore, 0, 0, WheelOfFortune.DEFAULT_WIDTH, WheelOfFortune.DEFAULT_HEIGHT);
		bgDrawable(false);
	}

	@Override
	public void render(float deltaTime) {
		update(deltaTime);
		clearScreen();
		draw();
		renderStage();
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void show() {}

	@Override
	public void hide() {
		SoundManager.pauseMusic(Assets.muGame);
	}

	@Override
	public void pause() {
		SoundManager.pauseMusic(Assets.muGame);
	}

	@Override
	public void resume() {}

	@Override
	public void dispose() {
		SoundManager.pauseMusic(Assets.muGame);
		stage.dispose();
	}

}
