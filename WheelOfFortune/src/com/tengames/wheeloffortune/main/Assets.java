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
package com.tengames.wheeloffortune.main;

import aurelienribon.bodyeditor.BodyEditorLoader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

/**
 * Class dùng để load tài nguyên (image, sound, music, . . . )
 *
 * @author kong
 *
 */
public class Assets {
	// loading manager
	public static AssetManager assetManager = new AssetManager();
	
	public static BitmapFont fNormalFont;
	
	public static Texture txBackground_menu;
	public static Texture txBackground_game;
	public static Texture txBackground_highscore;
	public static Texture txBackground_standby;
	public static Texture txBackground_help;
	public static Texture txQuizPaper;
	public static TextureRegion trAvatarPaper;
	
	public static Texture txArrow;
	public static Texture txHelp1;
	public static Texture txHelp2;
	public static Texture txHelp3;
	public static Texture txHelp4;
	public static Texture txHelp5;
	
	public static Texture txText;
	public static TextureRegion tr100;
	public static TextureRegion tr200;
	public static TextureRegion tr300;
	public static TextureRegion tr400;
	public static TextureRegion tr500;
	public static TextureRegion tr600;
	public static TextureRegion tr700;
	public static TextureRegion tr800;
	public static TextureRegion tr900;
	public static TextureRegion trDouble;
	public static TextureRegion trDevide;
	public static TextureRegion trLucky;
	public static TextureRegion trGift;
	public static TextureRegion trZero;
	
	public static Texture txCone;
	public static Texture txNeedle;
	
	public static Texture txDraft;
	
	public static Texture txFrame;
	public static TextureRegion trFrame;
	public static Animation aniFrame;
	
	public static Texture txButton;
	public static TextureRegion trButtonPlay_P;
	public static TextureRegion trButtonPlay_R;
	public static TextureRegion trButtonOption_P;
	public static TextureRegion trButtonOption_R;
	public static TextureRegion trButtonHighScore_P;
	public static TextureRegion trButtonHighScore_R;
	public static TextureRegion trButtonExit_P;
	public static TextureRegion trButtonExit_R;
	public static TextureRegion trButtonMenu_P;
	public static TextureRegion trButtonMenu_R;
	public static TextureRegion trButtonAgree_R;
	public static TextureRegion trButtonAgree_P;
	public static TextureRegion trButtonYes_R;
	public static TextureRegion trButtonYes_P;
	public static TextureRegion trButtonNo_R;
	public static TextureRegion trButtonNo_P;
	public static TextureRegion trButtonInfor_R;
	public static TextureRegion trButtonInfor_P;
	public static TextureRegion trButtonHelp_R;
	public static TextureRegion trButtonHelp_P;
	public static TextureRegion trAssetNull;
	public static TextureRegion trCursor;
	public static TextureRegion trTickRegion;
	public static TextureRegion trBlackRegion;
	
	public static Texture txBackground_avatar;
	public static TextureRegion trBgAvatar1;
	public static TextureRegion trBgAvatar2;
	public static TextureRegion trBgAvatar3;
	
	public static Texture txAlphabet;
	public static TextureRegion trA;
	public static TextureRegion trB;
	public static TextureRegion trC;
	public static TextureRegion trD;
	public static TextureRegion trE;
	public static TextureRegion trG;
	public static TextureRegion trH;
	public static TextureRegion trI;
	public static TextureRegion trK;
	public static TextureRegion trL;
	public static TextureRegion trM;
	public static TextureRegion trN;
	public static TextureRegion trO;
	public static TextureRegion trP;
	public static TextureRegion trQ;
	public static TextureRegion trR;
	public static TextureRegion trS;
	public static TextureRegion trT;
	public static TextureRegion trV;
	public static TextureRegion trU;
	public static TextureRegion trX;
	public static TextureRegion trY;
	
	public static Texture txDialog;
	public static TextureRegion trDialogOption;
	public static TextureRegion trDialogGame;
	public static TextureRegion trDialogAlert;
	public static TextureRegion trDialogQuickAnsw;
	
	public static BodyEditorLoader beLoader;
	
	public static WindowStyle dgStyle;
	public static LabelStyle lbStyle;
	public static TextFieldStyle tfStyle;
	
	public static LabelStyle lbStyleWhite;
	public static LabelStyle lbStylePlayer;
	public static LabelStyle lbStyleQuiz;
	public static LabelStyle lbStyleWelCome;
	public static LabelStyle lbStyleAlert;
	
	public static BitmapFont fAlertFont;
	public static BitmapFont fTimeFont;
	public static BitmapFont fPlayerFont;
	public static BitmapFont fBlue;
	public static BitmapFont fRed;
	
	public static ParticleEffect efStar;
	
	public static Sound soButtonClick;
	public static Sound soTickClick;
	public static Sound soConeRound;
	public static Sound soConeStop;
	public static Sound soLoose;
	public static Sound soWin;
	public static Sound soMcVoice;
	public static Sound soRound1;
	public static Sound soRound2;
	public static Sound soRound3;
	public static Sound soRoundS;
	public static Sound soSelectText;
	public static Sound soTrue;
	public static Sound soWrong;
	public static Sound soFinishRound;
	
	public static Music muGame;
	public static Music muMenu;
	public static Music muStandby;
	
	public static void loadLoading() {
		assetManager.load("data/drawable/loading.atlas", TextureAtlas.class);
		assetManager.finishLoading();
	}
	
	public static void load() {
		loadImage();
		loadSound();
	}
	
	public static void loadDone() {
		loadImageDone();
		loadSoundDone();
		loadModelsDone();
	}
	
	public static void dispose() {
		assetManager.dispose();
	}
	
	private static void loadModelsDone() {
		beLoader = new BodyEditorLoader(Gdx.files.internal("data/models/cone.json"));
	}
	
	private static void loadImage() {
		assetManager.load("data/drawable/background_menu.png", Texture.class);
		assetManager.load("data/drawable/button.png", Texture.class);
		assetManager.load("data/drawable/background_game.png", Texture.class);
		assetManager.load("data/drawable/background_highscore.png", Texture.class);
		assetManager.load("data/drawable/background_standby.png", Texture.class);
		assetManager.load("data/drawable/background_help.png", Texture.class);
		assetManager.load("data/drawable/quiz_paper.png", Texture.class);
		assetManager.load("data/drawable/text.png", Texture.class);
		assetManager.load("data/drawable/background_avatar.png", Texture.class);
		assetManager.load("data/drawable/alphabet.png", Texture.class);
		assetManager.load("data/models/cone.png", Texture.class);
		assetManager.load("data/models/needle.png", Texture.class);
		assetManager.load("data/drawable/dialog.png", Texture.class);
		assetManager.load("data/drawable/draft.png", Texture.class);
		assetManager.load("data/drawable/frame.png", Texture.class);
		
		assetManager.load("data/drawable/arrow.png", Texture.class);
		assetManager.load("data/drawable/help1.jpg", Texture.class);
		assetManager.load("data/drawable/help2.jpg", Texture.class);
		assetManager.load("data/drawable/help3.jpg", Texture.class);
		assetManager.load("data/drawable/help4.jpg", Texture.class);
		assetManager.load("data/drawable/help5.jpg", Texture.class);
	}
	
	private static void loadSound() {
		assetManager.load("data/raw/buttonclick.mp3", Sound.class);
		assetManager.load("data/raw/tickclick.mp3", Sound.class);
		assetManager.load("data/raw/coneround.mp3", Sound.class);
		assetManager.load("data/raw/conestop.mp3", Sound.class);
		assetManager.load("data/raw/loose.mp3", Sound.class);
		assetManager.load("data/raw/win.mp3", Sound.class);
		assetManager.load("data/raw/mcvoice.mp3", Sound.class);
		assetManager.load("data/raw/round1.mp3", Sound.class);
		assetManager.load("data/raw/round2.mp3", Sound.class);
		assetManager.load("data/raw/round3.mp3", Sound.class);
		assetManager.load("data/raw/rounds.mp3", Sound.class);
		assetManager.load("data/raw/selecttext.mp3", Sound.class);
		assetManager.load("data/raw/true.mp3", Sound.class);
		assetManager.load("data/raw/wrong.mp3", Sound.class);
		assetManager.load("data/raw/finishround.mp3", Sound.class);
		
		assetManager.load("data/raw/musicgame.mp3", Music.class);
		assetManager.load("data/raw/musicmenu.mp3", Music.class);
		assetManager.load("data/raw/musicstandby.mp3", Music.class);
	}
	
	private static void loadImageDone() {
		txArrow = assetManager.get("data/drawable/arrow.png", Texture.class);
		txHelp1 = assetManager.get("data/drawable/help1.jpg", Texture.class);
		txHelp2 = assetManager.get("data/drawable/help2.jpg", Texture.class);
		txHelp3 = assetManager.get("data/drawable/help3.jpg", Texture.class);
		txHelp4 = assetManager.get("data/drawable/help4.jpg", Texture.class);
		txHelp5 = assetManager.get("data/drawable/help5.jpg", Texture.class);
		
		txBackground_menu = assetManager.get("data/drawable/background_menu.png");
		
		txButton = assetManager.get("data/drawable/button.png");
		trButtonPlay_R = new TextureRegion(txButton, 0, 0, 256, 96);
		trButtonPlay_P = new TextureRegion(txButton, 256, 0, 256, 96);
		trButtonOption_R = new TextureRegion(txButton, 0, 96, 320, 96);
		trButtonOption_P = new TextureRegion(txButton, 320, 96, 320, 96);
		trButtonHighScore_R = new TextureRegion(txButton, 0, 288, 224, 96);
		trButtonHighScore_P = new TextureRegion(txButton, 224, 288, 224, 96);
		trButtonExit_R = new TextureRegion(txButton, 0, 384, 192, 96);
		trButtonExit_P = new TextureRegion(txButton, 192, 384, 192, 96);
		trTickRegion = new TextureRegion(txButton, 640, 224, 64, 64);
		trBlackRegion = new TextureRegion(txButton, 736, 256, 18, 17);
		trButtonMenu_R = new TextureRegion(txButton, 512, 0, 256, 96);
		trButtonMenu_P = new TextureRegion(txButton, 448, 288, 256, 96);
		trButtonAgree_R = new TextureRegion(txButton, 0, 192, 256, 96);
		trButtonAgree_P = new TextureRegion(txButton, 256, 192, 256, 96);
		trButtonYes_R = new TextureRegion(txButton, 224, 480, 128, 64);
		trButtonYes_P = new TextureRegion(txButton, 352, 480, 128, 64);
		trButtonNo_R = new TextureRegion(txButton, 480, 480, 128, 64);
		trButtonNo_P = new TextureRegion(txButton, 608, 480, 128, 64);
		trButtonInfor_R = new TextureRegion(txButton, 480, 544, 128, 64);
		trButtonInfor_P = new TextureRegion(txButton, 608, 544, 128, 64);
		trButtonHelp_R = new TextureRegion(txButton, 480, 608, 128, 64);
		trButtonHelp_P = new TextureRegion(txButton, 608, 608, 128, 64);
		
		trAssetNull = new TextureRegion(txButton, 0, 0, 5, 5);
		
		txBackground_game = assetManager.get("data/drawable/background_game.png");
		txBackground_highscore = assetManager.get("data/drawable/background_highscore.png");
		txBackground_standby = assetManager.get("data/drawable/background_standby.png");
		txBackground_help = assetManager.get("data/drawable/background_help.png");
		txQuizPaper = assetManager.get("data/drawable/quiz_paper.png");
		trAvatarPaper = new TextureRegion(txQuizPaper);
		trAvatarPaper.flip(true, false);
		
		txText = assetManager.get("data/drawable/text.png");
		tr100 = new TextureRegion(txText, 0, 0, 320, 128);
		tr200 = new TextureRegion(txText, 0, 128, 320, 128);
		tr300 = new TextureRegion(txText, 0, 256, 320, 128);
		tr400 = new TextureRegion(txText, 0, 384, 320, 128);
		tr500 = new TextureRegion(txText, 0, 512, 320, 128);
		tr600 = new TextureRegion(txText, 0, 640, 320, 128);
		tr700 = new TextureRegion(txText, 0, 768, 320, 128);
		tr800 = new TextureRegion(txText, 320, 0, 320, 128);
		tr900 = new TextureRegion(txText, 320, 128, 320, 128);
		trDevide = new TextureRegion(txText, 320, 256, 320, 128);
		trGift = new TextureRegion(txText, 320, 384, 320, 128);
		trDouble = new TextureRegion(txText, 320, 512, 320, 128);
		trLucky = new TextureRegion(txText, 320, 640, 320, 128);
		trZero = new TextureRegion(txText, 320, 768, 320, 128);
		
		txBackground_avatar = assetManager.get("data/drawable/background_avatar.png");
		trBgAvatar1 = new TextureRegion(txBackground_avatar, 0, 0, 135, 159);
		trBgAvatar2 = new TextureRegion(txBackground_avatar, 135, 0, 135, 159);
		trBgAvatar3 = new TextureRegion(txBackground_avatar, 270, 0, 135, 159);
		
		txAlphabet = assetManager.get("data/drawable/alphabet.png");
		trA = new TextureRegion(txAlphabet, 0, 0, 64, 64);
		trB = new TextureRegion(txAlphabet, 64, 0, 64, 64);
		trC = new TextureRegion(txAlphabet, 128, 0, 64, 64);
		trD = new TextureRegion(txAlphabet, 192, 0, 64, 64);
		trE = new TextureRegion(txAlphabet, 256, 0, 64, 64);
		trG = new TextureRegion(txAlphabet, 320, 0, 64, 64);
		trH = new TextureRegion(txAlphabet, 384, 0, 64, 64);
		trI = new TextureRegion(txAlphabet, 448, 0, 64, 64);	
		trK = new TextureRegion(txAlphabet, 512, 0, 64, 64);
		trL = new TextureRegion(txAlphabet, 576, 0, 64, 64);
		trM = new TextureRegion(txAlphabet, 640, 0, 64, 64);
		trN = new TextureRegion(txAlphabet, 0, 64, 64, 64);
		trO = new TextureRegion(txAlphabet, 64, 64, 64, 64);
		trP = new TextureRegion(txAlphabet, 128, 64, 64, 64);
		trQ = new TextureRegion(txAlphabet, 192, 64, 64, 64);
		trR = new TextureRegion(txAlphabet, 256, 64, 64, 64);
		trS = new TextureRegion(txAlphabet, 320, 64, 64, 64);
		trT = new TextureRegion(txAlphabet, 384, 64, 64, 64);
		trV = new TextureRegion(txAlphabet, 448, 64, 64, 64);	
		trU = new TextureRegion(txAlphabet, 512, 64, 64, 64);
		trX = new TextureRegion(txAlphabet, 576, 64, 64, 64);
		trY = new TextureRegion(txAlphabet, 640, 64, 64, 64);
		
		txCone = assetManager.get("data/models/cone.png");
		txNeedle = assetManager.get("data/models/needle.png");
		
		txDialog = assetManager.get("data/drawable/dialog.png");
		trDialogOption = new TextureRegion(txDialog, 0, 0, 512, 448);
		trDialogGame = new TextureRegion(txDialog, 512, 0, 512, 448);
		trDialogAlert = new TextureRegion(txDialog, 288, 448, 512, 320);
		trDialogQuickAnsw = new TextureRegion(txDialog, 0, 767, 512, 432);
		
		trCursor = new TextureRegion(txDialog, 415, 49, 2, 35);
		
		txDraft = assetManager.get("data/drawable/draft.png");
		
		txFrame = assetManager.get("data/drawable/frame.png");
		trFrame = new TextureRegion(txFrame, 0, 0, txFrame.getWidth(), txFrame.getHeight());
		aniFrame = new Animation(0.2f, frameSplit(trFrame, 1, 9));
		
		fNormalFont = new BitmapFont(Gdx.files.internal("data/fonts/font.fnt"), Gdx.files.internal("data/fonts/font.png"), false);
		fAlertFont = new BitmapFont(Gdx.files.internal("data/fonts/font.fnt"), Gdx.files.internal("data/fonts/font.png"), false);
		fTimeFont = new BitmapFont(Gdx.files.internal("data/fonts/timefont.fnt"), Gdx.files.internal("data/fonts/timefont.png"), false);
		fPlayerFont = new BitmapFont(Gdx.files.internal("data/fonts/timefont.fnt"), Gdx.files.internal("data/fonts/timefont.png"), false);
		fBlue = new BitmapFont(Gdx.files.internal("data/fonts/fontblue.fnt"), Gdx.files.internal("data/fonts/fontblue.png"), false);
		fRed = new BitmapFont(Gdx.files.internal("data/fonts/fontred.fnt"), Gdx.files.internal("data/fonts/fontred.png"), false);
		
		efStar = loadParticleEffect("data/effect", "star.p");
		
		dgStyle = new WindowStyle();
		dgStyle.titleFont = fNormalFont;
		dgStyle.titleFontColor = Color.BLACK;
		
		lbStyle = new LabelStyle();
		lbStyle.font = fNormalFont;
		lbStyle.fontColor = Color.RED;
		
		lbStyleWelCome = new LabelStyle();
		lbStyleWelCome.font = fNormalFont;
		lbStyleWelCome.fontColor = Color.BLUE;
		
		lbStyleQuiz = new LabelStyle();
		lbStyleQuiz.font = fNormalFont;
		lbStyleQuiz.fontColor = Color.BLACK;
		
		lbStyleWhite = new LabelStyle();
		lbStyleWhite.font = fNormalFont;
		lbStyleWhite.fontColor = Color.WHITE;
		
		lbStyleAlert = new LabelStyle();
		lbStyleAlert.font = fBlue;
		
		lbStylePlayer = new LabelStyle();
		lbStylePlayer.font = fRed;
		
		tfStyle = new TextFieldStyle();
		tfStyle.font = fNormalFont;
		tfStyle.cursor = new TextureRegionDrawable(trCursor);
		tfStyle.fontColor = Color.BLACK;
	}
	
	private static void loadSoundDone() {
		soButtonClick = assetManager.get("data/raw/buttonclick.mp3", Sound.class);
		soTickClick = assetManager.get("data/raw/tickclick.mp3", Sound.class);
		soConeRound = assetManager.get("data/raw/coneround.mp3", Sound.class);
		soConeStop = assetManager.get("data/raw/conestop.mp3", Sound.class);
		soLoose = assetManager.get("data/raw/loose.mp3", Sound.class);
		soWin = assetManager.get("data/raw/win.mp3", Sound.class);
		soMcVoice = assetManager.get("data/raw/mcvoice.mp3", Sound.class);
		soRound1 = assetManager.get("data/raw/round1.mp3", Sound.class);
		soRound2 = assetManager.get("data/raw/round2.mp3", Sound.class);
		soRound3 = assetManager.get("data/raw/round3.mp3", Sound.class);
		soRoundS = assetManager.get("data/raw/rounds.mp3", Sound.class);
		soSelectText = assetManager.get("data/raw/selecttext.mp3", Sound.class);
		soTrue = assetManager.get("data/raw/true.mp3", Sound.class);
		soWrong = assetManager.get("data/raw/wrong.mp3", Sound.class);
		soFinishRound = assetManager.get("data/raw/finishround.mp3", Sound.class);
		
		muGame = assetManager.get("data/raw/musicgame.mp3", Music.class);
		muMenu = assetManager.get("data/raw/musicmenu.mp3", Music.class);
		muStandby = assetManager.get("data/raw/musicstandby.mp3", Music.class);
	}
	
	/**
	 * Load Particle Effect
	 * @param folder
	 * @param fileName
	 * @return
	 */
	public static ParticleEffect loadParticleEffect (String folder, String fileName) {
		ParticleEffect effect;
		
		effect = new ParticleEffect();
		effect.load(Gdx.files.internal(folder + "/" + fileName), Gdx.files.internal(folder));
		
		return effect;
	}
	/**
	 * Split TextureRegion for Animation
	 * @param textureRegion
	 * @param rows
	 * @param cols
	 * @return TextureRegion[]
	 */
	private static TextureRegion[] frameSplit (TextureRegion textureRegion, int rows, int cols) {
		
		TextureRegion[] result;
		int tileWidth = textureRegion.getRegionWidth() / cols;
		int tileHeight = textureRegion.getRegionHeight() / rows;
		
		TextureRegion[][] temp = textureRegion.split(tileWidth, tileHeight);
		result = new TextureRegion[cols * rows];
		
		int index = 0;
		
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j++) {
				result[index++] = temp[i][j];
			}
		}
		
		return result;
	}
}
