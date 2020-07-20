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

import woodyx.basicapi.accessor.SpriteAccessor;
import woodyx.basicapi.sprite.ObjectSprite;
import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.TweenPaths;
import aurelienribon.tweenengine.equations.Quad;
import aurelienribon.tweenengine.equations.Quart;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Class frameword, tạo các ô chữ cho game
 *
 * @author kong
 *
 */
public class FrameWord extends ObjectSprite {
	
	// values of frameWord
	public static final byte VALUE_A = 0;
	public static final byte VALUE_B = 1;
	public static final byte VALUE_C = 2;
	public static final byte VALUE_D = 3;
	public static final byte VALUE_E = 4;
	public static final byte VALUE_G = 5;
	public static final byte VALUE_H = 6;
	public static final byte VALUE_I = 7;
	public static final byte VALUE_K = 8;
	public static final byte VALUE_L = 9;
	public static final byte VALUE_M = 10;
	public static final byte VALUE_N = 11;
	public static final byte VALUE_O = 12;
	public static final byte VALUE_P = 13;
	public static final byte VALUE_Q = 14;
	public static final byte VALUE_R = 15;
	public static final byte VALUE_S = 16;
	public static final byte VALUE_T = 17;
	public static final byte VALUE_U = 18;
	public static final byte VALUE_V = 19;
	public static final byte VALUE_X = 20;
	public static final byte VALUE_Y = 21;
	public static final byte VALUE_NULL = 22;
	
	private float stateTime;
	private byte value;
	
	private boolean canTouch;
	private boolean isTouched;
	private boolean isFlipped;
	
	private TweenManager tweenManager;
	
	private Sprite spValue;
	
	/**
	 * class FrameWord
	 * @param x: vị trí ban đầu của frameword x
	 * @param y: vị trí ban đầu của framwword y
	 * @param xTarget: vị trí đến x
	 * @param yTarget: vị trí đến y
	 * @param width: chiều rộng
	 * @param height: chiểu cao
	 * @param varChar: giá trị của frameword
	 */
	public FrameWord(float x, float y, float xTarget, float yTarget, float width, float height, char varChar) {
		super(x, y, width, height);
		
		spValue = new Sprite();
		spValue.setPosition(x, y);
		spValue.setSize(width, height);
		
		stateTime = 0;
		value = VALUE_NULL;
		
		canTouch = false;
		isFlipped = false;
		isTouched = false;
		
		setValue(varChar);
		moveToLeft(xTarget, yTarget);
	}
	
	private void moveToLeft(float x, float y) {
		tweenManager = new TweenManager();
		
		Tween.to(this, SpriteAccessor.CPOS_XY, 2f)
		.waypoint(x, y)
		.target(x, y)
		.path(TweenPaths.catmullRom)
		.ease(Quad.INOUT)
		.start(tweenManager);
		
		Tween.to(spValue, SpriteAccessor.CPOS_XY, 2f)
		.waypoint(x, y)
		.target(x, y)
		.path(TweenPaths.catmullRom)
		.ease(Quad.INOUT)
		.start(tweenManager);
	}

	private void flipToFront() {
		tweenManager = new TweenManager();

		Timeline.createSequence()
		.beginParallel()
		.push(Tween.to(this, SpriteAccessor.POS_XY, 0.3f).target(230, 130).ease(Quart.INOUT))
		.push(Tween.to(this, SpriteAccessor.OPACITY, 0.3f).target(0).ease(Quart.IN))
		.push(Tween.to(this, SpriteAccessor.SCALE_XY, 0.3f).target(20, 20).ease(Quart.IN)).end()
		.start(tweenManager);
	}
	
	public void update(float deltaTime) {
		tweenManager.update(deltaTime);
		stateTime += deltaTime;
	}
	
	public void updateState(boolean canFlip) {
		if (canFlip) {
			isFlipped = true;
			flipToFront();
		}
	}
	
	public void setTextureValue(TextureRegion texture) {
		spValue.setRegion(texture);
	}
	
	public Sprite getSpriteValue() {
		return spValue;
	}
	
	public byte getValue() {
		return value;
	}
	
	public boolean getCanTouch() {
		return canTouch;
	}
	
	public void setCanTouch(boolean canTouch) {
		this.canTouch = canTouch;
	}
	
	public boolean getIsTouched() {
		return isTouched;
	}
	
	public void setIsTouched(boolean isTouched) {
		this.isTouched = isTouched;
	}
	
	public boolean getIsFlipped() {
		return isFlipped;
	}
	
	public float getStateTime() {
		return stateTime;
	}
	
	public void checkClicked(float ScreenX, float ScreenY) {
		if (canTouch) {
			if (ScreenX >= this.getX() && ScreenX <= this.getX() + this.getWidth() && ScreenY >= this.getY() && ScreenY <= this.getY() + this.getHeight()) {
				updateState(true);
				isTouched = true;
			}
		}
	}
	
	private void setValue(char c) {
		switch (c) {
		case 'a':
		case 'A':
			value = VALUE_A;
			break;
			
		case 'b':
		case 'B':
			value = VALUE_B;
			break;
			
		case 'c':
		case 'C':
			value = VALUE_C;
			break;
			
		case 'd':
		case 'D':
			value = VALUE_D;
			break;
			
		case 'e':
		case 'E':
			value = VALUE_E;
			break;
			
		case 'g':
		case 'G':
			value = VALUE_G;
			break;
			
		case 'h':
		case 'H':
			value = VALUE_H;
			break;
			
		case 'i':
		case 'I':
			value = VALUE_I;
			break;
			
		case 'k':
		case 'K':
			value = VALUE_K;
			break;
			
		case 'l':
		case 'L':
			value = VALUE_L;
			break;
			
		case 'm':
		case 'M':
			value = VALUE_M;
			break;
			
		case 'n':
		case 'N':
			value = VALUE_N;
			break;
			
		case 'o':
		case 'O':
			value = VALUE_O;
			break;
			
		case 'p':
		case 'P':
			value = VALUE_P;
			break;
			
		case 'q':
		case 'Q':
			value = VALUE_Q;
			break;
			
		case 'r':
		case 'R':
			value = VALUE_R;
			break;
			
		case 's':
		case 'S':
			value = VALUE_S;
			break;
			
		case 't':
		case 'T':
			value = VALUE_T;
			break;
			
		case 'u':
		case 'U':
			value = VALUE_U;
			break;
			
		case 'v':
		case 'V':
			value = VALUE_V;
			break;
			
		case 'x':
		case 'X':
			value = VALUE_X;
			break;
			
		case 'y':
		case 'Y':
			value = VALUE_Y;
			break;
		}
	}
	
	public void dispose() {
		tweenManager.killAll();
	}
}
