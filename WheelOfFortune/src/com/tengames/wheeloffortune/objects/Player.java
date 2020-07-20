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
package com.tengames.wheeloffortune.objects;

import woodyx.basicapi.accessor.DialogAccessor;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.TweenPaths;
import aurelienribon.tweenengine.equations.Quad;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tengames.wheeloffortune.main.Assets;

/**
 * Class cung cấp thông tin của người chơi (tên, tuổi, điểm, . . .), đồng thời cung cấp các phương thức tạo hiệu ứng cho game
 *
 * @author kong
 *
 */
public class Player extends Dialog {
	
	// states of Player
	public static final byte STATE_FLYUP = 1;
	public static final byte STATE_FLYDOWN = 2;
	
	private Label lbScore;
	private Label lbMistake;
	
	private String sName;
	private String sAge;
	private String sAddress;
	
	private byte countMistake;
	private short scoreRound;
	private short scoreGame;
	private byte gift;
	
	private boolean isPlay;
	private boolean canPlay;
	private boolean isDisplayAlert;
	
	private byte state;
	
	private TweenManager tweenManager;
	
	/**
	 * class Player
	 * @param windowStyle: style của window
	 * @param x: vị trí ban đầu x
	 * @param y: vị trí ban đầu y
	 * @param region: image đại diện
	 * @param moveType: kiểu di chuyển của Player
	 */
	public Player(WindowStyle windowStyle, float x, float y, TextureRegion region, int moveType) {
		super("", windowStyle);
		
		tweenManager = new TweenManager();
		
		this.setPosition(x, y);
		this.setSize(135, 159);
		this.setBackground(new TextureRegionDrawable(region));

		this.scoreRound = 0;
		this.countMistake = 0;
		this.isPlay = false;
		this.canPlay = true;
		this.isDisplayAlert = true;
		this.gift = 0;
		
		initializeLabels();
		// move
		moveInputScreen(moveType);
	}
	
	private void initializeLabels() {
		lbScore = new Label("", Assets.lbStyle);
		lbScore.setAlignment(Align.center);
		lbScore.setPosition(67, 15);
		lbScore.setText("" + scoreRound);
		
		lbMistake = new Label("", Assets.lbStyleWhite);
		lbMistake.setAlignment(Align.center);
		lbMistake.setPosition(67, 40);
		lbMistake.setText("Lỗi : " + countMistake);
		
		this.addActor(lbMistake);
		this.addActor(lbScore);
	}

	public void update(float deltaTime) {
		tweenManager.update(deltaTime);
		
		lbMistake.setText("Lỗi : " + countMistake);
		lbScore.setText("" + scoreRound);
	}
	
	public void updateState(byte state) {
		switch (state) {
			
		case STATE_FLYUP:
			this.state = STATE_FLYUP;
			flyUp();
			break;
			
		case STATE_FLYDOWN:
			this.state = STATE_FLYDOWN;
			flyDown();
			break;
			
			default:
				break;
			
		}
	}
	
	public void moveInputScreen(int type) {
		switch (type) {
		case 1:
			Tween.to(this, DialogAccessor.CPOS_XY, 2f)
			.target(200, 200)
			.waypoint(200, 200)
			.path(TweenPaths.catmullRom)
			.ease(Quad.OUT)
			.start(tweenManager);
			break;
			
		case 2:
			Tween.to(this, DialogAccessor.CPOS_XY, 2f)
			.target(400, 200)
			.waypoint(400, 200)
			.path(TweenPaths.catmullRom)
			.ease(Quad.OUT)
			.start(tweenManager);
			break;
			
		case 3:
			Tween.to(this, DialogAccessor.CPOS_XY, 2f)
			.target(600, 200)
			.waypoint(600, 200)
			.path(TweenPaths.catmullRom)
			.ease(Quad.OUT)
			.start(tweenManager);
			break;
			
			default:
				break;
		}
	}
	
	private void flyUp() {
		Tween.to(this, DialogAccessor.CPOS_XY, 2f)
		.target(100, 100)
		.waypoint(100, 100)
		.path(TweenPaths.catmullRom)
		.ease(Quad.OUT)
		.start(tweenManager);
	}
	
	private void flyDown() {
		Tween.to(this, DialogAccessor.CPOS_XY, 2f)
		.target(-200, 100)
		.waypoint(-200, 100)
		.path(TweenPaths.catmullRom)
		.ease(Quad.OUT)
		.start(tweenManager);
	}
	
	public byte getState() {
		return state;
	}
	
	public String getName() {
		return sName;
	}
	
	public String getAge() {
		return sAge;
	}
	
	public String getAddress() {
		return sAddress;
	}
	
	public void setName(String name) {
		this.sName = name;
	}
	
	public void setAge(String age) {
		this.sAge = age;
	}
	
	public void setAddress(String address) {
		this.sAddress = address;
	}
	
	public void increaseScoreRound(int number) {
		this.scoreRound += number;
	}
	
	public void decreaseScoreRound(int number) {
		this.scoreRound -= number;
	}
	
	public short getScoreRound() {
		return scoreRound;
	}
	
	public void increaseScoreGame(int number) {
		this.scoreGame += number;
	}
	
	public short getScoreGame() {
		return scoreGame;
	}
	
	public void setGift(byte gift) {
		this.gift = gift;
	}
	
	public byte getGift() {
		return gift;
	}
	
	public boolean getIsPlay() {
		return isPlay;
	}
	
	public void setIsPlay(boolean flag) {
		this.isPlay = flag;
	}
	
	public boolean getCanPlay() {
		return canPlay;
	}
	
	public void setCanPlay(boolean canPlay) {
		this.canPlay = canPlay;
	}
	
	public void setIsDisplayAlert() {
		this.isDisplayAlert = false;
	}
	
	public boolean getIsDisplayAlert() {
		return isDisplayAlert;
	}
	
	public void setMistake() {
		this.countMistake++;
	}
	
	public byte getMistake() {
		return countMistake;
	}
	
	public void resetMistake() {
		this.countMistake = 0;
		this.canPlay = true;
	}
	
	public void setTextScore(int scoreGame) {
		lbScore.setText("" + scoreGame);
	}
	
	public Label getLbMistake() {
		return lbMistake;
	}
	
	public Label getLbScore() {
		return lbScore;
	}
	
	public void dispose() {
		tweenManager.killAll();
	}
}
