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
import aurelienribon.tweenengine.equations.Back;
import aurelienribon.tweenengine.equations.Quart;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Class tạo hiệu ứng chữ khi người chơi quay nón và nhận được kết quả
 *
 * @author kong
 *
 */
public class TextEffect extends ObjectSprite {
	private TweenManager tweenManager;
	
	/**
	 * class TextEffect
	 * @param textureRegion: image của text
	 * @param x: vị trí x
	 * @param y: vị trí y
	 */
	public TextEffect(TextureRegion textureRegion, float x, float y) {
		super(textureRegion, x, y);
		tweenManager = new TweenManager();
		
		Timeline.createSequence()
		.push(Tween.set(this, SpriteAccessor.OPACITY).target(0))
		.push(Tween.set(this, SpriteAccessor.SCALE_XY).target(10f, 10f))
		.beginParallel()
			.push(Tween.to(this, SpriteAccessor.POS_XY, 0.5f).target(x, y))
			.push(Tween.to(this, SpriteAccessor.OPACITY, 0.5f).target(1).ease(Quart.INOUT))
			.push(Tween.to(this, SpriteAccessor.SCALE_XY, 0.5f).target(0.625f, 0.625f).ease(Quart.INOUT))
		.end()
		.pushPause(0.5f)
		.push(Tween.to(this, SpriteAccessor.OPACITY, 0.5f).target(0).ease(Back.OUT))
		.start(tweenManager);
		
	}
	
	public void update(float deltaTime) {
		tweenManager.update(deltaTime);
	}
	
	public void dispose() {
		tweenManager.killAll();
	}

}
