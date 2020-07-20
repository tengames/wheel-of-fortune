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

import woodyx.basicapi.sprite.ObjectSprite;

import com.badlogic.gdx.graphics.Texture;

/**
 * Class cung cấp phương thức cho người chơi quay nón
 *
 * @author kong
 *
 */
public class Cone extends ObjectSprite {
	
	// values of cone
	public static final byte VALUE_S800 = 0;
	public static final byte VALUE_S600 = 1;
	public static final byte VALUE_S500 = 2;
	public static final byte VALUE_S700 = 3;
	public static final byte VALUE_S400 = 4;
	public static final byte VALUE_LOSTTURN = 5;
	public static final byte VALUE_DOUBLE = 6;
	public static final byte VALUE_S300 = 7;
	public static final byte VALUE_S100 = 8;
	public static final byte VALUE_S900 = 9;
	public static final byte VALUE_S200 = 10;
	public static final byte VALUE_LOSTSCORE = 11;
	public static final byte VALUE_LUCKY = 12;
	public static final byte VALUE_DIVIDED = 13;
	public static final byte VALUE_GIFT = 14;
	public static final byte VALUE_INVALID = 15;
	public static final byte VALUE_NULL = 19;

	// state of cone
	public static final byte STATE_VALID = 16;
	public static final byte STATE_STOP = 17;
	public static final byte STATE_INVALID = 18;

	private byte value;
	private byte state;

	private float oldAngle;
	private float angle;
	private float lastAngle;
	private float deltaAngle;
	private float tempAngle;
	private float stopDeltaAngle;

	private boolean canGetData;
	
	private boolean canTouch;

	/**
	 * class Cone
	 * @param texture: image
	 * @param x: vị trí ban đầu x
	 * @param y: vị trí ban đầu y
	 */
	public Cone(Texture texture, float x, float y) {
		super(texture, x, y);
		// initialize parameters
		value = VALUE_NULL;
		state = STATE_STOP;

		oldAngle = 0;
		lastAngle = 0;
		deltaAngle = 0;
		tempAngle = 0;
		stopDeltaAngle = 0;

		canGetData = false;
		canTouch = true;
	}

	public void update() {
		angle = this.getRotation();
		deltaAngle = angle - oldAngle;
		if (deltaAngle == 0) {
			state = STATE_STOP;
			stopDeltaAngle = angle - lastAngle;
			if (stopDeltaAngle == 0) {
				canGetData = false;
			}
			else {
				canGetData = true;
				if (stopDeltaAngle < -0.05)
					value = VALUE_INVALID;
				if (stopDeltaAngle > 0.02 && stopDeltaAngle <= 180)
					value = VALUE_INVALID;
				if (stopDeltaAngle > 180) {
					tempAngle = angle;
					int i = (int) tempAngle / 360;
					tempAngle -= 360 * i;
					if (tempAngle > 17.20 && tempAngle <= 32.21)
						value = VALUE_LOSTSCORE;
					if ((tempAngle > 32.21 && tempAngle <= 47.72)
							|| (tempAngle > 91.82 && tempAngle <= 106.94)
							|| (tempAngle > 197.67 && tempAngle <= 213.26))
						value = VALUE_S500;
					if (tempAngle > 47.72 && tempAngle <= 77.14)
						value = VALUE_LUCKY;
					if (tempAngle > 77.14 && tempAngle <= 91.82
							|| (tempAngle > 288.10 && tempAngle <= 316.90))
						value = VALUE_S300;
					if (tempAngle > 106.94 && tempAngle <= 122.39
							|| (tempAngle > 3 && tempAngle <= 17.20))
						value = VALUE_S200;
					if (tempAngle > 122.39 && tempAngle <= 137.18)
						value = VALUE_DIVIDED;
					if (tempAngle > 137.18 && tempAngle <= 152.14)
						value = VALUE_GIFT;
					if (tempAngle > 152.14 && tempAngle <= 167.74)
						value = VALUE_S800;
					if (tempAngle > 167.74 && tempAngle <= 197.67
							|| (tempAngle > 273.1 && tempAngle <= 288.10))
						value = VALUE_S600;
					if (tempAngle > 213.26 && tempAngle <= 227.25)
						value = VALUE_S700;
					if ((tempAngle > 227.25 && tempAngle <= 242.78)
							|| (tempAngle > 332.20 && tempAngle <= 347.43))
						value = VALUE_S400;
					if (tempAngle > 242.78 && tempAngle <= 258.22)
						value = VALUE_LOSTTURN;
					if (tempAngle > 258.22 && tempAngle <= 273.1)
						value = VALUE_DOUBLE;
					if (tempAngle > 316.90 && tempAngle <= 332.20)
						value = VALUE_S100;
					if ((tempAngle > 347.43 && tempAngle <= 360)
							|| (tempAngle <= 3))
						value = VALUE_S900;
				}
			}
			lastAngle = angle;
		} else {
			state = STATE_VALID;
			if (deltaAngle > 0.1) canTouch = false;
		}

		oldAngle = angle;
		
	}
	
	public boolean getCanTouch() {
		return canTouch;
	}
	
	public void setCanTouch(boolean canTouch) {
		this.canTouch = canTouch;
	}
	
	public byte getValue() {
		if (state == STATE_STOP) {
			if (canGetData) {
				canTouch = false;
				return value;
			}
		}
		return VALUE_NULL;
	}
}
