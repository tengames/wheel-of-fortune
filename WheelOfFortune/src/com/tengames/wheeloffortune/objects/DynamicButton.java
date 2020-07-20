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

import woodyx.basicapi.accessor.ButtonAccessor;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import aurelienribon.tweenengine.TweenPaths;
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Quad;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tengames.wheeloffortune.main.WheelOfFortune;

/**
 * Class cung cấp các phương thức cho phép button có khả năng di chuyển, tạo hiệu ứng cho game
 *
 * @author kong
 *
 */
public class DynamicButton extends Button {
	
	// state of button
	public static final byte STATE_DEFAULT = 1;
	public static final byte STATE_CLICKED = 2;
	
	private TweenManager tweenBeforClick;
	private TweenManager tweenAfterClick;
	
	private byte state;
	
	/**
	 * My Button Class
	 * @param x: vị trí ban đầu x
	 * @param y: vị trí ban đầu y
	 * @param yStop: vị trí đến y
	 * @param width: chiều rộng
	 * @param height: chiều cao
	 * @param down: image khi ấn nút
	 * @param up: image lúc bình thường
	 * @param type: kiểu di chuyển
	 */
	public DynamicButton (float x, float y, float yStop, float width, float height, TextureRegion down, TextureRegion up, int type) {
		super(new TextureRegionDrawable(up), new TextureRegionDrawable(down));
		
		tweenBeforClick = new TweenManager();
		tweenAfterClick = new TweenManager();
		
		state = STATE_DEFAULT;

		this.setPosition(x, y);
		this.setSize(width, height);
		
		switch (type) {
		case 1:
			Tween.to(this, ButtonAccessor.CPOS_XY, 2f)
			.target(x, yStop)
			.waypoint(x, yStop)
			.path(TweenPaths.catmullRom)
			.ease(Quad.OUT)
			.start(tweenBeforClick);
			
			Tween.to(this, ButtonAccessor.CPOS_XY, 2f)
			.target(WheelOfFortune.DEFAULT_WIDTH + this.getWidth(), yStop)
			.waypoint(WheelOfFortune.DEFAULT_WIDTH + this.getWidth(), yStop)
			.path(TweenPaths.catmullRom)
			.ease(Back.INOUT)
			.start(tweenAfterClick);
			
			break;
			
		default:
			break;
		}
	}
	
	public boolean getChecked() {
		if (state == STATE_CLICKED && this.getX() >= WheelOfFortune.DEFAULT_WIDTH + this.getWidth() / 2) {
			return true;
		}
		return false;
	}
	
	public void update(float deltaTime, int type) {
		switch (type) {
		case STATE_DEFAULT:
			tweenBeforClick.update(deltaTime);
			break;
			
		case STATE_CLICKED:
			state = STATE_CLICKED;
			tweenAfterClick.update(deltaTime);
			break;
		}
	}
	
	public void dispose(int type) {
		switch (type) {
		case STATE_DEFAULT:
			tweenBeforClick.killAll();
			break;
			
		case STATE_CLICKED:
			tweenAfterClick.killAll();
			break;
		}
	}

}
