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
package com.tengames.wheeloffortune.worlds;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.tengames.wheeloffortune.main.Assets;
import com.tengames.wheeloffortune.screens.InputScreen;

/**
 * Class dùng để draw các object
 *
 * @author kong
 *
 */
public class WorldRenderer {
	private WorldUpdate worldUpdate;
	private SpriteBatch batch;
	
	public WorldRenderer(WorldUpdate worldUpdate, SpriteBatch batch) {
		
		this.worldUpdate = worldUpdate;
		this.batch = batch;
		
	}
	
	private void renderStage() {
		worldUpdate.stage.draw();
	}

	private void renderCone() {
		worldUpdate.spCone.draw(batch);
		worldUpdate.spNeedle.draw(batch);
	}
	
	private void renderDraft() {
		worldUpdate.spDraft.draw(batch);
	}
	
	private void renderFrameWords() {
		TextureRegion keyFrame = null;
		
		for (int i = 0; i < worldUpdate.frameWords.size(); i++) {
			if (worldUpdate.frameWords.get(i).getIsFlipped()) {
				keyFrame = Assets.aniFrame.getKeyFrame(worldUpdate.frameWords.get(i).getStateTime(), false);
			} else {
				keyFrame = Assets.aniFrame.getKeyFrame(0);
			}
			worldUpdate.frameWords.get(i).getSpriteValue().draw(batch);
			worldUpdate.frameWords.get(i).renderSprite(keyFrame).draw(batch);
		}
	}
	
	private void renderEffect() {
		Assets.efStar.draw(batch, Gdx.graphics.getDeltaTime());
	}
	
	// render text effect
	private void renderTextEffect() {
		if (worldUpdate.spTextEffect != null) {
			worldUpdate.spTextEffect.draw(batch);
		}
	}
	
	private void renderTime() {
		if (worldUpdate.isCountDown) {
			if (worldUpdate.answerTime > 5) {
				Assets.fTimeFont.setColor(Color.WHITE);
			}
			if (worldUpdate.answerTime <= 5 && worldUpdate.answerTime > 1) {
				Assets.fTimeFont.setColor(Color.RED);
			}
			Assets.fTimeFont.draw(batch, "" + worldUpdate.answerTime, 85, 223);
		}
	}
	
	private void renderBlack() {
		if (worldUpdate.blackOn) {
			batch.draw(Assets.trBlackRegion, -1, -1, 801, 481);
		}
	}
	
	public void draw() {
		objDrawable(true);
		if (!InputScreen.isSpecialRound) {
			if (worldUpdate.getState() != WorldUpdate.STATE_FINISHED_ROUND) {
				renderCone();
				renderDraft();
				renderFrameWords();
				renderTime();
			}
		} else {
			renderFrameWords();
		}
		renderBlack();
		objDrawable(false);
		renderStage();
		objDrawable(true);
		if (worldUpdate.getState() != WorldUpdate.STATE_FINISHED_ROUND) {
			renderTextEffect();
		}
		renderEffect();
		objDrawable(false);
		
	}
	
	/**
	 * prepare for drawing objects
	 * @param flag
	 */
	private void objDrawable(boolean flag) {
		if (flag) {
			batch.enableBlending();
			batch.begin();
		} else {
			batch.end();
		}
	}
}
