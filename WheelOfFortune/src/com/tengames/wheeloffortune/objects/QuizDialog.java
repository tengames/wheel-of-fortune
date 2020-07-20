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

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tengames.wheeloffortune.main.Assets;

/**
 * Class cho phép người chơi xem lại thông tin về câu hỏi trong khi chơi game và xem thông tin của những người chơi khác
 *
 * @author kong
 *
 */
public class QuizDialog extends Dialog {
	
	private TweenManager tweenManager;
	
	private Label lbQuiz;
	private Label lbTQuiz;
	
	private Player player1;
	private Player player2;
	private Player player3;
	
	private boolean isInside;
	
	/**
	 * class QuizDialog
	 * @param windowStyle: style của window
	 * @param region: image đại diện
	 * @param x: vị trí x
	 * @param y: vị trí y
	 * @param txQuiz: câu hỏi của vòng chơi
	 */
	public QuizDialog(WindowStyle windowStyle, Texture region, float x, float y, String txQuiz) {
		super("", windowStyle);
		
		this.tweenManager = new TweenManager();
		
		this.isInside = false;
		this.setBackground(new TextureRegionDrawable(new TextureRegion(region)));
		this.setSize(800, 240);
		this.setPosition(x, y);
		
		lbTQuiz = new Label("", Assets.lbStyle);
		lbTQuiz.setAlignment(Align.center);
		lbTQuiz.setText("CÂU HỎI");
		lbTQuiz.setPosition(400, 200);
		
		this.addActor(lbTQuiz);
		
		lbQuiz = new Label("", Assets.lbStyleQuiz);
		lbQuiz.setAlignment(Align.center);
		lbQuiz.setText(txQuiz);
		lbQuiz.setPosition(400, 145);
		
		this.addActor(lbQuiz);
	}
	
	/**
	 * class QuizDialog
	 * @param windowStyle
	 * @param region
	 * @param x
	 * @param y
	 */
	public QuizDialog(WindowStyle windowStyle, TextureRegion region, float x, float y, Player player1, Player player2, Player player3) {
		super ("", windowStyle);
		this.tweenManager = new TweenManager();
		
		this.isInside = false;
		this.setBackground(new TextureRegionDrawable(new TextureRegion(region)));
		this.setSize(800, 240);
		this.setPosition(x, y);
		
		this.player1 = new Player(windowStyle, 150, 50, Assets.trBgAvatar1, 4);
		this.player1.setTitle(player1.getName());
		this.player2 = new Player(windowStyle, 320, 50, Assets.trBgAvatar2, 4);
		this.player2.setTitle(player2.getName());
		this.player3 = new Player(windowStyle, 495, 50, Assets.trBgAvatar3, 4);
		this.player3.setTitle(player3.getName());
		
		this.addActor(this.player1);
		this.addActor(this.player2);
		this.addActor(this.player3);
	}
	
	public void updateInfor(Player player1, Player player2, Player player3) {
		this.player1.getLbMistake().setText("Lỗi : " + player1.getMistake());
		this.player2.getLbMistake().setText("Lỗi : " + player2.getMistake());
		this.player3.getLbMistake().setText("Lỗi : " + player3.getMistake());
		
		this.player1.getLbScore().setText("" + player1.getScoreRound());
		this.player2.getLbScore().setText("" + player2.getScoreRound());
		this.player3.getLbScore().setText("" + player3.getScoreRound());
	}
	
	public void update(float deltaTime) {
		tweenManager.update(deltaTime);
	}
	
	public void checkClicked(float screenX, float screenY, int type) {
		if (this.getX() < screenX && this.getX() + this.getWidth() > screenX 
		 && this.getY() < screenY && this.getY() + this.getHeight() > screenY) {
			isInside = !isInside;
		}
		
		if (isInside) {
			switch (type) {
			case 1:
				flyIn();
				break;
				
			case 2:
				flyApp();
				break;
			}

		} else {
			switch (type) {
			case 1:
				flyOut();
				break;
				
			case 2:
				flyHide();
				break;
			}
		}
	}
	
	private void flyIn() {
		Tween.to(this, DialogAccessor.CPOS_XY, 2f)
		.target(400, 240)
		.waypoint(400, 240)
		.path(TweenPaths.catmullRom)
		.ease(Quad.OUT)
		.start(tweenManager);
	}
	
	private void flyOut() {
		Tween.to(this, DialogAccessor.CPOS_XY, 2f)
		.target(1110, 240)
		.waypoint(1110, 240)
		.path(TweenPaths.catmullRom)
		.ease(Quad.OUT)
		.start(tweenManager);
	}
	
	private void flyApp() {
		Tween.to(this, DialogAccessor.CPOS_XY, 2f)
		.target(400, 350)
		.waypoint(400, 350)
		.path(TweenPaths.catmullRom)
		.ease(Quad.OUT)
		.start(tweenManager);
	}
	
	private void flyHide() {
		Tween.to(this, DialogAccessor.CPOS_XY, 2f)
		.target(-305, 350)
		.waypoint(-305, 350)
		.path(TweenPaths.catmullRom)
		.ease(Quad.OUT)
		.start(tweenManager);
	}
	
	public boolean getIsInside() {
		return isInside;
	}
	
	public void dispose() {
		tweenManager.killAll();
	}

}
