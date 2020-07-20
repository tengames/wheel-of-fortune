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

import com.badlogic.gdx.graphics.Texture;

/**
 * Class cung cấp các phương thức cho người chơi đoán chữ
 *
 * @author kong
 *
 */
public class Draft extends ObjectSprite {
	
	// values of draft
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

	// state of draft
	public static final byte STATE_FLYUP = 24;
	public static final byte STATE_FLYDOWN = 25;

	private byte value;
	
	private char textValue;

	private byte state;
	
	private boolean canTouch;

	private TweenManager tweenManager;

	/**
	 * class Draft
	 * @param txDraft: image 
	 * @param x: vị trí ban đầu x
	 * @param y: vị trí ban đầu y
	 */
	public Draft(Texture txDraft, float x, float y) {
		super(x, y, txDraft.getWidth(), txDraft.getHeight());
		
		tweenManager = new TweenManager();
		
		this.setRegion(txDraft);
		state = STATE_FLYDOWN;
		canTouch = false;
		appear(x, y);
	}

	public boolean getCanTouch() {
		return canTouch;
	}
	
	public void setCanTouch(boolean canTouch) {
		this.canTouch = canTouch;
	}
	
	public byte getState() {
		return state;
	}

	public void update(float deltaTime) {
		tweenManager.update(deltaTime);
	}

	public void updateState(byte state) {
		switch (state) {
		case STATE_FLYUP:
			this.state = STATE_FLYUP;
			canTouch = true;
			flyUpDown(450, 150);
			break;

		case STATE_FLYDOWN:
			this.state = STATE_FLYDOWN;
			canTouch = false;
			flyUpDown(430, -95);
			break;
			
			default:
				break;
		}
	}

	// draft appearing
	private void appear(float x, float y) {
		tweenManager = new TweenManager();

		Timeline.createSequence()
				.push(Tween.set(this, SpriteAccessor.POS_XY).target(x, y))
				.push(Tween.set(this, SpriteAccessor.SCALE_XY).target(10, 10))
				.push(Tween.set(this, SpriteAccessor.ROTATION).target(0))
				.push(Tween.set(this, SpriteAccessor.OPACITY).target(0))
				.pushPause(0.0f)
				.beginParallel()
				.push(Tween.to(this, SpriteAccessor.OPACITY, 1.0f).target(1).ease(Quart.INOUT))
				.push(Tween.to(this, SpriteAccessor.SCALE_XY, 1.0f).target(1, 1).ease(Quart.INOUT))
				.end()
				.pushPause(0.5f)
				.push(Tween.to(this, SpriteAccessor.CPOS_XY, 2f).waypoint(430, -95).target(430, -95).path(TweenPaths.catmullRom).ease(Quad.INOUT))
				.start(tweenManager);
	}

	private void flyUpDown(float x, float y) {
		tweenManager = new TweenManager();

		Tween.to(this, SpriteAccessor.CPOS_XY, 2f).waypoint(x, y).target(x, y).path(TweenPaths.catmullRom).ease(Quad.INOUT)
		.start(tweenManager);
	}
	
	public char getTextValue() {
		return textValue;
	}
	
	public byte getValue() {
		return value;
	}

	public byte chooseWord(float screenX, float screenY) {
		// initialize for alphabet position
		value = VALUE_NULL;
		if (canTouch) {
			float xStart = this.getX();
			float yStart = this.getY();
			
			if (screenX <= (xStart + 143) && screenX >= (xStart + 108)
					&& screenY <= (yStart + 188) && screenY >= (yStart + 154)) {
				value = VALUE_A;
				textValue = 'A';
			}
			
			if (screenX <= (xStart + 192) && screenX >= (xStart + 160)
					&& screenY <= (yStart + 196) && screenY >= (yStart + 162)) {
				value = VALUE_B;
				textValue = 'B';
			}
			
			if (screenX <= (xStart + 240) && screenX >= (xStart + 210)
					&& screenY <= (yStart + 205) && screenY >= (yStart + 170)) {
				value = VALUE_C;
				textValue = 'C';
			}
			
			if (screenX <= (xStart + 291) && screenX >= (xStart + 258)
					&& screenY <= (yStart + 214) && screenY >= (yStart + 182)) {
				value = VALUE_D;
				textValue = 'D';
			}
			
			if (screenX <= (xStart + 340) && screenX >= (xStart + 309)
					&& screenY <= (yStart + 224) && screenY >= (yStart + 190)) {
				value = VALUE_E;
				textValue = 'E';
			}
			
			if (screenX <= (xStart + 152) && screenX >= (xStart + 118)
					&& screenY <= (yStart + 139) && screenY >= (yStart + 105)) {
				value = VALUE_G;
				textValue = 'G';
			}
			
			if (screenX <= (xStart + 201) && screenX >= (xStart + 168)
					&& screenY <= (yStart + 148) && screenY >= (yStart + 114)) {
				value = VALUE_H;
				textValue = 'H';
			}
			
			if (screenX <= (xStart + 233) && screenX >= (xStart + 216)
					&& screenY <= (yStart + 155) && screenY >= (yStart + 123)) {
				value = VALUE_I;
				textValue = 'I';
			}
			
			if (screenX <= (xStart + 285) && screenX >= (xStart + 251)
					&& screenY <= (yStart + 164) && screenY >= (yStart + 129)) {
				value = VALUE_K;
				textValue = 'K';
			}
			
			if (screenX <= (xStart + 330) && screenX >= (xStart + 300)
					&& screenY <= (yStart + 171) && screenY >= (yStart + 138)) {
				value = VALUE_L;
				textValue = 'L';
			}
			
			if (screenX <= (xStart + 384) && screenX >= (xStart + 345)
					&& screenY <= (yStart + 180) && screenY >= (yStart + 146)) {
				value = VALUE_M;
				textValue = 'M';
			}
			
			if (screenX <= (xStart + 156) && screenX >= (xStart + 123)
					&& screenY <= (yStart + 93) && screenY >= (yStart + 60)) {
				value = VALUE_N;
				textValue = 'N';
			}
			
			if (screenX <= (xStart + 207) && screenX >= (xStart + 176)
					&& screenY <= (yStart + 102) && screenY >= (yStart + 66)) {
				value = VALUE_O;
				textValue = 'O';
			}
			
			if (screenX <= (xStart + 253) && screenX >= (xStart + 225)
					&& screenY <= (yStart + 110) && screenY >= (yStart + 76)) {
				value = VALUE_P;
				textValue = 'P';
			}
			
			if (screenX <= (xStart + 308) && screenX >= (xStart + 274)
					&& screenY <= (yStart + 117) && screenY >= (yStart + 84)) {
				value = VALUE_Q;
				textValue = 'Q';
			}
			
			if (screenX <= (xStart + 356) && screenX >= (xStart + 324)
					&& screenY <= (yStart + 127) && screenY >= (yStart + 92)) {
				value = VALUE_R;
				textValue = 'R';
			}
			
			if (screenX <= (xStart + 403) && screenX >= (xStart + 374)
					&& screenY <= (yStart + 135) && screenY >= (yStart + 102)) {
				value = VALUE_S;
				textValue = 'S';
			}
			
			if (screenX <= (xStart + 160) && screenX >= (xStart + 131)
					&& screenY <= (yStart + 45) && screenY >= (yStart + 11)) {
				value = VALUE_T;
				textValue = 'T';
			}
			
			if (screenX <= (xStart + 210) && screenX >= (xStart + 180)
					&& screenY <= (yStart + 51) && screenY >= (yStart + 20)) {
				value = VALUE_U;
				textValue = 'U';
			}
			
			if (screenX <= (xStart + 256) && screenX >= (xStart + 226)
					&& screenY <= (yStart + 62) && screenY >= (yStart + 28)) {
				value = VALUE_V;
				textValue = 'V';
			}
			
			if (screenX <= (xStart + 309) && screenX >= (xStart + 276)
					&& screenY <= (yStart + 68) && screenY >= (yStart + 35)) {
				value = VALUE_X;
				textValue = 'X';
			}
			
			if (screenX <= (xStart + 354) && screenX >= (xStart + 325)
					&& screenY <= (yStart + 77) && screenY >= (yStart + 45)) {
				value = VALUE_Y;
				textValue = 'Y';
			}
		}
		return value;
	}

	public void dispose() {
		tweenManager.killAll();
	}

}
