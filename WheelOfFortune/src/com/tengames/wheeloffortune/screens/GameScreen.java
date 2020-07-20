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

import woodyx.basicapi.physics.BoxUtility;
import woodyx.basicapi.screen.XScreen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.physics.box2d.World;
import com.tengames.wheeloffortune.main.Assets;
import com.tengames.wheeloffortune.main.WheelOfFortune;
import com.tengames.wheeloffortune.setting.SoundManager;
import com.tengames.wheeloffortune.worlds.WorldListener;
import com.tengames.wheeloffortune.worlds.WorldRenderer;
import com.tengames.wheeloffortune.worlds.WorldUpdate;

public class GameScreen extends XScreen implements Screen, WorldListener {
	private WheelOfFortune game;

	private WorldUpdate worldUpdate;
	private WorldRenderer worldRender;
	
	private World world;
	
	public GameScreen(WheelOfFortune game) {
		super(WheelOfFortune.DEFAULT_WIDTH, WheelOfFortune.DEFAULT_HEIGHT);
		this.game = game;
		
		// play music background
		SoundManager.playMusic(Assets.muGame, true);
		
		initialize();
	}
	
	private void renderBackGround() {
		batch.draw(Assets.txBackground_game, 0, 0, WheelOfFortune.DEFAULT_WIDTH, WheelOfFortune.DEFAULT_HEIGHT);
	}
	
	@Override
	public void initialize() {
		world = new World(WheelOfFortune.GRAVITY, true);
		
		worldUpdate = new WorldUpdate(camera, world, this);
		
		worldRender = new WorldRenderer(worldUpdate, this.batch);

	}

	@Override
	public void update(float deltaTime) {
		// world step
		world.step(deltaTime, BoxUtility.VELOCITY_ITER, BoxUtility.POSITION_ITER);
		// update
		worldUpdate.update(deltaTime);
	}

	@Override
	public void draw() {
		bgDrawable(true);
		renderBackGround();
		bgDrawable(false);
		// render world
		renderObjects();
	}
	
	private void renderObjects() {
		worldRender.draw();
	}
	
	// clear force
	private void clearWorld() {
		world.clearForces();
	}

	@Override
	public void render(float deltaTime) {
		update(deltaTime);
		clearScreen();
		clearWorld();
		draw();
	}

	@Override
	public void resize(int width, int height) {}

	@Override
	public void show() {}

	@Override
	public void hide() {
		// pause
		SoundManager.pauseMusic(Assets.muGame);
	}

	@Override
	public void pause() {
		// pause
		SoundManager.pauseMusic(Assets.muGame);
	}

	@Override
	public void resume() {}

	@Override
	public void dispose() {
		SoundManager.pauseMusic(Assets.muGame);
		world.dispose();
	}

	// interface
	@Override
	public void setStandbyScreen() {
		game.setScreen(new StandbyScreen(game));
	}

	@Override
	public void setMenuScreen() {
		game.setScreen(new MainMenuScreen(game));
	}

	@Override
	public void toChangePlayer(String name) {
		game.androidInterface.toChangePlayer(name);
	}

	@Override
	public void toGift() {
		game.androidInterface.toGift();
	}

	@Override
	public void toLucky() {
		game.androidInterface.toLucky();
	}

	@Override
	public void toAlert() {
		game.androidInterface.toAlert();
	}

	@Override
	public void toLostPlay(String name) {
		game.androidInterface.toLostPlay(name);
	}

	@Override
	public void toMistake() {
		game.androidInterface.toMistake();
	}

	@Override
	public void toFail() {
		game.androidInterface.toFail();
	}

	@Override
	public void toHint() {
		game.androidInterface.toHint();
	}

	@Override
	public void soRound1() {
		SoundManager.playSound(Assets.soRound1);
	}

	@Override
	public void soRound2() {
		SoundManager.playSound(Assets.soRound2);
	}

	@Override
	public void soRound3() {
		SoundManager.playSound(Assets.soRound3);
	}

	@Override
	public void soRoundS() {
		SoundManager.playSound(Assets.soRoundS);
	}

	@Override
	public void soSelecText() {
		SoundManager.playSound(Assets.soSelectText);
	}

	@Override
	public void soConeRound() {
		SoundManager.playSound(Assets.soConeRound);
	}

	@Override
	public void soConeStop() {
		SoundManager.playSound(Assets.soConeStop);
	}

	@Override
	public void soWin() {
		SoundManager.playSound(Assets.soWin);
	}

	@Override
	public void soLoose() {
		SoundManager.playSound(Assets.soLoose);
	}

	@Override
	public void soTrue() {
		SoundManager.playSound(Assets.soTrue);
	}

	@Override
	public void soWrong() {
		SoundManager.playSound(Assets.soWrong);
	}

	@Override
	public void soFinishRound() {
		SoundManager.playSound(Assets.soFinishRound);
	}
	
	@Override
	public void muPauseMuGame() {
		SoundManager.pauseMusic(Assets.muGame);
	}

	@Override
	public void muResumeMuGame() {
		SoundManager.playMusic(Assets.muGame, true);
	}

	@Override
	public void saveHscore(int addScore) {
		game.androidInterface.saveHscore(addScore);
	}

	@Override
	public void showIntertitial() {
		game.androidInterface.showIntertitial();
	}

	@Override
	public void traceScene(String level) {
		game.androidInterface.traceScene(level);
	}

}
