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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.tengames.wheeloffortune.main.Assets;
import com.tengames.wheeloffortune.main.WheelOfFortune;
import com.tengames.wheeloffortune.objects.DynamicButton;
import com.tengames.wheeloffortune.setting.SoundManager;

public class HelpScreen extends XScreen implements Screen {
	private WheelOfFortune game;
	
	private Stage stage;
	
	private DynamicButton btMenu;
	
	private boolean flagMainMenu;
	
	public HelpScreen (WheelOfFortune game) {
		super(WheelOfFortune.DEFAULT_WIDTH, WheelOfFortune.DEFAULT_HEIGHT);
		this.game = game;
		
		initialize();
	}
	
	@Override
	public void initialize() {
		stage = new Stage(WheelOfFortune.DEFAULT_WIDTH, WheelOfFortune.DEFAULT_HEIGHT, true);
		
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
		
		// initialize scroll pane
		Table table = getTable(Assets.txHelp1, Assets.txHelp2, Assets.txHelp3, Assets.txHelp4, Assets.txHelp5);
		ScrollPane scroll = new ScrollPane(table);
		scroll.setPosition(0, 0);
		scroll.setBounds(0, 0, 800, 360);
		scroll.setScrollingDisabled(false, true);
		scroll.setSmoothScrolling(true);
		
		// add actor
		stage.addActor(scroll);
		stage.addActor(btMenu);
		
		// play music background
		SoundManager.playMusic(Assets.muGame, true);
		
		Gdx.input.setInputProcessor(stage);
	}

	private Table getTable(Texture ... textures) {
		int n = textures.length;
		Table table = new Table();
		
		for (int i = 0; i < n; i++) {
			Image img = new Image(textures[i]);
			table.add(img).padLeft(100).padRight(100);
		}
		
		return table;
	}
	
	@Override
	public void update(float deltaTime) {
		updateButton(deltaTime);
		updateStage(deltaTime);
	}
	
	private void updateStage(float deltaTime) {
		stage.act(deltaTime);
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
		renderBackGround();
		renderStage();
		renderObjects();
	}
	
	private void renderBackGround() {
		bgDrawable(true);
		batch.draw(Assets.txBackground_help, 0, 0, 800, 480);
		bgDrawable(false);
	}
	
	private void renderObjects() {
		objDrawable(true);
		batch.draw(Assets.txArrow, 712, 140, 77, 81);
		batch.draw(Assets.txArrow, 90, 140, -77, 81);
		objDrawable(false);
	}
	
	private void renderStage() {
		stage.draw();
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
