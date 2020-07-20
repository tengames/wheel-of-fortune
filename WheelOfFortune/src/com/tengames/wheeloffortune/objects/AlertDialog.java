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

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tengames.wheeloffortune.main.Assets;

/**
 * Class đưa ra các dialog thông báo cho người chơi
 *
 * @author kong	
 *
 */
public class AlertDialog extends Dialog {
	private Label lbText;
	
	private Button btYes;
	private Button btNo;
	private Button btAgree;
	
	private boolean isYes;
	private boolean isNo;
	private boolean isAgree;
	
	/**
	 * class AlertDialog:
	 * @param windowStyle: style của window
	 * @param background: background của window
	 * @param bYesUp: nút "có" khi bình thường
	 * @param bYesDown: nút "có" khi ấn
	 * @param bNoUp: nút "không" khi bình thường
	 * @param bNoDown: nút "không" khi ấn
	 * @param bAgreeUp: nút "đồng ý" khi bình thường
	 * @param bAgreeDown: nút "đồng ý" khi ấn
	 * @param sAlert: Thông báo của dialog
	 * @param type: kiểu của dialog (cái này nên chuyển thành constructor mới)
	 */
	public AlertDialog(WindowStyle windowStyle, TextureRegion background, TextureRegion bYesUp, TextureRegion bYesDown, TextureRegion bNoUp, TextureRegion bNoDown, TextureRegion bAgreeUp, TextureRegion bAgreeDown, String sAlert, int type) {
		super("", windowStyle);
		
		this.setBackground(new TextureRegionDrawable(background));
		this.setSize(background.getRegionWidth(), background.getRegionHeight());
		this.setPosition(144, 80);
		
		this.isNo = false;
		this.isYes = false;
		this.isAgree = false;
		
		switch (type) {
		case 1:
			btYes = new Button(new TextureRegionDrawable(bYesUp), new TextureRegionDrawable(bYesDown));
			btYes.setPosition(64, 60);
			btYes.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					setVisible(false);
					resetButtons();
					isYes = true;
				}
			});
			this.addActor(btYes);
			
			btNo = new Button(new TextureRegionDrawable(bNoUp), new TextureRegionDrawable(bNoDown));
			btNo.setPosition(320, 60);
			btNo.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					setVisible(false);
					resetButtons();
					isNo = true;
				}
			});
			this.addActor(btNo);
			
			lbText = new Label("", Assets.lbStyleAlert);
			lbText.setText(sAlert);
			lbText.setAlignment(Align.center);
			lbText.setPosition(256, 210);
			this.addActor(lbText);
			
			break;
			
		case 2:
			btAgree =  new Button(new TextureRegionDrawable(bAgreeUp), new TextureRegionDrawable(bAgreeDown));
			btAgree.setSize(192, 72);
			btAgree.setPosition(160, 60);
			btAgree.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					setVisible(false);
					resetButtons();
					isAgree = true;
				}
			});
			this.addActor(btAgree);
			
			lbText = new Label("", Assets.lbStyleAlert);
			lbText.setText(sAlert);
			lbText.setAlignment(Align.center);
			lbText.setPosition(256, 210);
			this.addActor(lbText);
			
			break;
			
		case 3:
			btAgree =  new Button(new TextureRegionDrawable(bAgreeUp), new TextureRegionDrawable(bAgreeDown));
			btAgree.setSize(192, 72);
			btAgree.setPosition(160, 60);
			btAgree.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					setVisible(false);
					resetButtons();
					isAgree = true;
				}
			});
			this.addActor(btAgree);
			
			lbText = new Label("", Assets.lbStyleQuiz);
			lbText.setText(sAlert);
			lbText.setAlignment(Align.center);
			lbText.setPosition(256, 300);
			this.addActor(lbText);
			
			default:
				break;
		}
	}
	
	public void resetButtons() {
		isYes = false;
		isNo = false;
	}
	
	public boolean getIsYes() {
		return isYes;
	}
	
	public boolean getIsNo() {
		return isNo;
	}
	
	public boolean getIsAgree() {
		return isAgree;
	}

}
