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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tengames.wheeloffortune.main.Assets;
import com.tengames.wheeloffortune.main.WheelOfFortune;

/**
 * Class cung cấp dialog cho người chơi nhập thông tin
 *
 * @author kong
 *
 */
public class InputDialog extends Dialog {
	
	// states of dialog
	public static final byte STATE_FLYIN = 0;
	public static final byte STATE_FLYOUT = 1;
	
	private TweenManager tweenManager;
	
	// text fields
	private TextField tfInputName;
	private TextField tfInputAge;
	private TextField tfInputAdress;
	
	// buttons
	private Button btExit;
	private Button btAgree;
	
	// flag
	private boolean isExit;

	private int posPlayer;
	private byte state;
	
	/**
	 * class InputDialog
	 * @param player: người chơi cần nhận thông tin
	 * @param posPlayer: vị trí của người chơi (số thứ tự)
	 * @param windowStyle: style của window (cái này dialog nào cũng cần có)
	 * @param textureRegion: image của dialog
	 * @param x: vị trí x
	 * @param y: vị trí y
	 */
	public InputDialog(Player player, int posPlayer, WindowStyle windowStyle, TextureRegion textureRegion, float x, float y) {
		super("", windowStyle);
		
		tweenManager = new TweenManager();
		
		this.setPosition(x, y);
		this.setSize(400, 350);
		this.setBackground(new TextureRegionDrawable(textureRegion));
		
		this.posPlayer = posPlayer;
		this.isExit = false;
		this.state = STATE_FLYIN;
		
		updateState(STATE_FLYIN);
		
		initializeLabel();
		initializeInputs();
		initializeButtons(player, posPlayer + 1);
		
	}
	
	private void initializeLabel() {
		Label labelPlayer = new Label("", Assets.lbStylePlayer);
		labelPlayer.setText("" + (posPlayer+1));
		labelPlayer.setPosition(200, 265);
		this.addActor(labelPlayer);
	}
	
	private void initializeButtons(final Player player, final int number) {
		// button exit
		btExit = new Button(new TextureRegionDrawable(Assets.trAssetNull));
		btExit.setSize(50, 50);
		btExit.setPosition(320, 280);
		// add listener
		btExit.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// set action
				isExit = true;
			}
		});
		this.addActor(btExit);
		
		// button agree
		btAgree = new Button(new TextureRegionDrawable(Assets.trButtonAgree_R), new TextureRegionDrawable(Assets.trButtonAgree_P));
		btAgree.setPosition(140, 40);
		btAgree.setSize(128, 48);
		// add listener
		btAgree.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// set action
				updateState(STATE_FLYOUT);
				setInforPlayer(player, number);
			}
		});
		this.addActor(btAgree);
	}
	
	private void initializeInputs() {
		// input name
		tfInputName = new TextField("", Assets.tfStyle);
		tfInputName.setMessageText(WheelOfFortune.S_NAME);
		tfInputName.setPosition(135, 196);
		this.addActor(tfInputName);
		
		// input age
		tfInputAge = new TextField("", Assets.tfStyle);
		tfInputAge.setMessageText(WheelOfFortune.S_AGE);
		tfInputAge.setPosition(135, 154);
		this.addActor(tfInputAge);
		
		// input address
		tfInputAdress = new TextField("", Assets.tfStyle);
		tfInputAdress.setMessageText(WheelOfFortune.S_ADDRESS);
		tfInputAdress.setPosition(135, 113);
		this.addActor(tfInputAdress);
		
	}
	
	/**
	 * set infor cho người chơi
	 * @param player
	 * @param number
	 */
	private void setInforPlayer(Player player, int number) {
		if (tfInputName.getText().equals("")) {
			player.setTitle(WheelOfFortune.S_DEF_NAME + number);
			player.setName(WheelOfFortune.S_DEF_NAME + number);
		} else {
			String name = viewShrink(tfInputName.getText());
			player.setTitle(name);
			player.setName(name);
		}
		
		if (tfInputAge.getText().equals("")) {
			player.setAge(WheelOfFortune.S_DEF_VALUE);
		} else {
			player.setAge(tfInputAge.getText());
		}

		if (tfInputAdress.getText().equals("")) {
			player.setAddress(WheelOfFortune.S_DEF_VALUE);
		} else {
			player.setAddress(tfInputAdress.getText());
		}
	}
	
	// split string
	private String viewShrink(String name){
		StringBuffer strBuff = new StringBuffer();
		String[] splitName = name.split("\\s");
		int countTokens = splitName.length;
		if( countTokens > 3 ){
			strBuff.append(splitName[countTokens-2]);
			strBuff.append(" "+splitName[countTokens-1]);
		}else{
			strBuff = new StringBuffer(name);
		}
		return strBuff.toString();
	}
	
	public boolean isExitPressed() {
		if (isExit) return true;
		return false;
	}
	
	public byte getState() {
		return state;
	}
	
	public void update(float deltaTime) {
		tweenManager.update(deltaTime);
	}
	
	public void updateState(byte state) {
		switch (state) {
		case STATE_FLYIN:
			flyIn();
			break;
			
		case STATE_FLYOUT:
			flyOut();
			break;
		}
	}
	
	private void flyIn() {
		Tween.to(this, DialogAccessor.CPOS_XY, 2f)
		.target(400, 230)
		.waypoint(400, 230)
		.path(TweenPaths.catmullRom)
		.ease(Quad.OUT)
		.start(tweenManager);
	}
	
	private void flyOut() {
		// isFinised = true;
		Tween.to(this, DialogAccessor.CPOS_XY, 2f)
		.target(-200, 230)
		.waypoint(-200, 230)
		.path(TweenPaths.catmullRom)
		.ease(Quad.OUT)
		.start(tweenManager);
	}
	
	public void dispose() {
		tweenManager.killAll();
	}
	
}
