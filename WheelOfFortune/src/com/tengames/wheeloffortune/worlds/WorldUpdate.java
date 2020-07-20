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

import java.util.ArrayList;
import java.util.LinkedList;

import woodyx.basicapi.physics.BoxUtility;
import woodyx.basicapi.physics.ObjectModel;
import woodyx.basicapi.physics.ObjectsJoint;
import woodyx.basicapi.sprite.ObjectSprite;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.tengames.wheeloffortune.main.Assets;
import com.tengames.wheeloffortune.main.WheelOfFortune;
import com.tengames.wheeloffortune.objects.AlertDialog;
import com.tengames.wheeloffortune.objects.Cone;
import com.tengames.wheeloffortune.objects.Draft;
import com.tengames.wheeloffortune.objects.FrameWord;
import com.tengames.wheeloffortune.objects.Player;
import com.tengames.wheeloffortune.objects.QuizDialog;
import com.tengames.wheeloffortune.objects.TextEffect;
import com.tengames.wheeloffortune.screens.InputScreen;
import com.tengames.wheeloffortune.screens.MainMenuScreen;

/**
 * class update các trạng thái, vị trí của các object
 * @author HeoRungDiNang
 *
 */
public class WorldUpdate implements InputProcessor {
	// initialize world listener
	public static final byte STATE_RUNNING_ROUND = 1;
	public static final byte STATE_FINISHED_ROUND = 2;

	// interface
	public WorldListener worldListener;

	protected Stage stage;

	private byte state;

	private OrthographicCamera camera;

	private World world;

	// initialize sprite
	protected ArrayList<FrameWord> frameWords;
	protected Cone spCone;
	protected ObjectSprite spNeedle;
	protected Draft spDraft;
	protected TextEffect spTextEffect;

	// initialize objects
	private ObjectModel modelGround;
	private ObjectModel modelCone;
	private ObjectModel modelConeAxis;
	private ObjectModel modelNeedle;
	private ObjectModel modelNeedleAxis;

	// initialize joints
	private ObjectsJoint jCone;
	private ObjectsJoint jNeedle;

	// initialize dialogs
	private QuizDialog dgQuiz;
	private QuizDialog dgPlayerInfor;
	private AlertDialog dgChoice;
	private AlertDialog dgResultChoice;
	private AlertDialog dgConfirmExit;

	// quick answer dialog
	private Dialog dgQuickAnsw;
	private Label lbQuickAnsw;
	private TextField tfQuickAnsw;
	private Button btQuickAnsw;
	private Button btQuickAnswExit;

	private Label lbResult;
	private Label lbCongras;
	private Label lbAlert;
	private Label lbFinal;
	private Label lbHint;

	// initialize flags, variables
	protected int answerTime; // time for answer
	protected boolean isCountDown; // count down time for answer
	protected boolean blackOn; // switch black
	private boolean canCreateDialog;
	private boolean checkValue; // check if word is chosen
	private boolean canFlip; // check if frame is flip
	private boolean isInput; // input processor
	private boolean isStage; // stage input
	private boolean canPlaySound; // play finish round sound
	private byte countWord;
	private byte currentPlayer;
	private byte currentConeValue;
	private byte controlPlus; // check if can increase score
	private byte stateDialog;
	private byte winPlayer;
	private byte countFlipFinish; // count flip frame finish
	private int maxScore;
	private int countFail; // count mistake in final round
	private int randFrame1, randFrame2, randFrame3;
	private float countTime; // count down time for finish
	private float countTimeFinish; // count time flip
	private float secondCount; // count second
	private float xTouch; // xTouch 
	private float yTouch; // yTouch
	
	// initialize check Value
	private ArrayList<Byte> checkValues;

	public WorldUpdate(OrthographicCamera camera, World world,
			WorldListener worldListener) {
		state = STATE_RUNNING_ROUND;
		this.camera = camera;
		this.world = world;

		// register
		registerWorldListener(worldListener);

		// trace
		worldListener.traceScene(InputScreen.roundNumber + "");
		
		initialize();
	}

	private void initialize() {
		stage = new Stage(WheelOfFortune.DEFAULT_WIDTH,
				WheelOfFortune.DEFAULT_HEIGHT, true);
		if (!InputScreen.isSpecialRound) {
			Gdx.input.setInputProcessor(this);
		} else {
			Gdx.input.setInputProcessor(stage);
		}
		initializeSound();
		initializeFrameWords(WheelOfFortune.conectDataBases.get(InputScreen.roundNumber - 1).getAccent());
		initializeFlags();
		initializeLabels();
		initializePlayers();
		if (!InputScreen.isSpecialRound) {
			initializeQuizDialog();
			initializeDraft();
			initializeCone();
			createJoint();
		} else {
			initializeDialogFinish();
		}
	}

	private void initializeFlags() {
		InputScreen.isFinishedGame = false;

		controlPlus = 1;
		countFail = 3;
		countTime = 0;
		countFlipFinish = 0;
		countTimeFinish = 0;
		answerTime = 30;
		secondCount = 0;

		randFrame1 = MathUtils.random(frameWords.size() - 1);
		randFrame2 = MathUtils.random(frameWords.size() - 1);
		randFrame3 = MathUtils.random(frameWords.size() - 1);
		
		canPlaySound = true;
		canCreateDialog = false;
		blackOn = false;
		isInput = false;
		canFlip = false;
		isCountDown = false;

		checkValues = new ArrayList<Byte>();

	}

	private void initializeSound() {
		switch (InputScreen.roundNumber) {
		case 1:
			worldListener.soRound1();
			// make hint
			worldListener.toHint();
			break;

		case 2:
			worldListener.soRound2();
			// make hint
			worldListener.toHint();
			break;

		case 3:
			worldListener.soRound3();
			// make hint
			worldListener.toHint();
			break;

		case 4:
			worldListener.soRoundS();
			break;

		}
	}

	private void initializeLabels() {

		lbResult = new Label("", Assets.lbStyleQuiz);
		lbResult.setPosition(400, 410);
		lbResult.setAlignment(Align.center);
		lbResult.setText(WheelOfFortune.S_RESULT
				+ WheelOfFortune.conectDataBases.get(
						InputScreen.roundNumber - 1).getAnswer());
		lbResult.setVisible(false);
		stage.addActor(lbResult);

		lbCongras = new Label("", Assets.lbStyle);
		lbCongras.setPosition(400, 320);
		lbCongras.setAlignment(Align.center);
		lbCongras.setVisible(false);
		stage.addActor(lbCongras);

		lbAlert = new Label("", Assets.lbStyle);
		lbAlert.setPosition(400, 30);
		lbAlert.setAlignment(Align.center);
		lbAlert.setText(WheelOfFortune.S_TOUCH2);
		lbAlert.setVisible(false);
		stage.addActor(lbAlert);

		lbFinal = new Label("", Assets.lbStyle);
		lbFinal.setPosition(400, 320);
		lbFinal.setAlignment(Align.center);
		lbFinal.setVisible(false);
		stage.addActor(lbFinal);

		lbHint = new Label("", Assets.lbStyleWhite);
		lbHint.setPosition(400, 410);
		lbHint.setAlignment(Align.center);
		lbHint.setText(WheelOfFortune.S_HINT);
		lbHint.setVisible(false);
		stage.addActor(lbHint);
	}

	private void initializeQuizDialog() {
		String text = WheelOfFortune.S_NUMBER_TEXT
				+ WheelOfFortune.conectDataBases.get(
						InputScreen.roundNumber - 1).getNumberChar()
				+ " chữ cái: \n\n"
				+ WheelOfFortune.conectDataBases.get(
						InputScreen.roundNumber - 1).getHint();
		
		dgQuiz = new QuizDialog(Assets.dgStyle, Assets.txQuizPaper, 730, 120, text);
		stage.addActor(dgQuiz);
		
		dgPlayerInfor = new QuizDialog(Assets.dgStyle, Assets.trAvatarPaper, -720, 200, InputScreen.players.get(0), InputScreen.players.get(1), InputScreen.players.get(2));
		stage.addActor(dgPlayerInfor);
	}

	private void initializeCone() {
		// initialize ground
		Vector2 polygonBox = new Vector2(WheelOfFortune.DEFAULT_WIDTH, 10);
		modelGround = new ObjectModel(world, ObjectModel.STATIC,
				ObjectModel.POLYGON, polygonBox, new Vector2(), 0, new Vector2(
						0, -100), 0, 10, 0.5f, 0.1f, "ground");

		// initialize cone model
		modelCone = new ObjectModel(world, ObjectModel.DYNAMIC,
				Assets.beLoader, "cone", new Vector2(100, 180), 0, 2f, 0.2f, 0,
				Assets.txCone.getWidth(), "cone");
		modelConeAxis = new ObjectModel(world, ObjectModel.STATIC,
				ObjectModel.POLYGON, new Vector2(10, 10), new Vector2(), 0,
				new Vector2(0, 480), 0, 1f, 0, 0, "axisCone");

		modelNeedle = new ObjectModel(world, ObjectModel.DYNAMIC,
				Assets.beLoader, "needle", new Vector2(100, 100), 0, 3, 0, 0,
				Assets.txNeedle.getWidth(), "noTouch");
		modelNeedleAxis = new ObjectModel(world, ObjectModel.STATIC,
				ObjectModel.POLYGON, new Vector2(10, 10), new Vector2(), 0,
				new Vector2(0, 0), 0, 1, 0, 0, "axisNeedle");

		// initialize cone sprite
		spCone = new Cone(Assets.txCone, 100, 180);
		spNeedle = new ObjectSprite(Assets.txNeedle, 100, 100);

	}

	private void createJoint() {
		// create joint cone
		jCone = new ObjectsJoint(modelConeAxis, modelCone,
				ObjectsJoint.REVOLUTE, true);
		jCone.setAnchorA(400, 0);
		jCone.setAnchorB(Assets.txCone.getWidth() / 2,
				Assets.txCone.getHeight() / 2);
		jCone.setMotor(400, 0);
		jCone.createJoint(world);

		// create joint needle
		jNeedle = new ObjectsJoint(modelNeedleAxis, modelNeedle,
				ObjectsJoint.REVOLUTE, true);
		jNeedle.setAnchorA(170, 210);
		jNeedle.setAnchorB(23, 17);
		jNeedle.setAngleLimit(-60, 20);
		jNeedle.setMotor(10, 360);
		jNeedle.createJoint(world);

	}

	private void initializeDraft() {
		// initialize draft
		spDraft = new Draft(Assets.txDraft, 250, 0);
	}

	private void initializePlayers() {
		if (InputScreen.isSpecialRound) {
			InputScreen.players.get(InputScreen.winner).setIsPlay(true);
			InputScreen.players.get(InputScreen.winner).updateState(
					Player.STATE_FLYUP);
			stage.addActor(InputScreen.players.get(InputScreen.winner));
		} else {
			for (int i = 0; i < InputScreen.players.size(); i++) {
				InputScreen.players.get(i).updateState(Player.STATE_FLYDOWN);
				InputScreen.players.get(i).setIsPlay(false);
				stage.addActor(InputScreen.players.get(i));
			}

			InputScreen.players.get(InputScreen.roundNumber - 1).setIsPlay(true);
			InputScreen.players.get(InputScreen.roundNumber - 1).updateState(Player.STATE_FLYUP);
		}
	}

	private void initializeDialogFinish() {
		// make label
		lbFinal.setText(WheelOfFortune.S_FINAL_QUIZ+ WheelOfFortune.conectDataBases.get(InputScreen.roundNumber - 1).getHint());
		lbFinal.setVisible(true);

		// make dialog
		dgQuickAnsw = new Dialog("", Assets.dgStyle);
		dgQuickAnsw.setBackground(new TextureRegionDrawable(
				Assets.trDialogQuickAnsw));
		dgQuickAnsw.setPosition(500, 10);
		dgQuickAnsw.setSize(285, 250);

		lbQuickAnsw = new Label("", Assets.lbStyle);
		lbQuickAnsw.setPosition(142, 154);
		lbQuickAnsw.setText("Số Lần Đoán: " + countFail);
		lbQuickAnsw.setAlignment(Align.center);
		dgQuickAnsw.addActor(lbQuickAnsw);

		tfQuickAnsw = new TextField("", Assets.tfStyle);
		tfQuickAnsw.setMessageText("Trả lời . . .");
		tfQuickAnsw.setPosition(50, 110);
		dgQuickAnsw.addActor(tfQuickAnsw);

		btQuickAnswExit = new Button(new TextureRegionDrawable(
				Assets.trAssetNull));
		btQuickAnswExit.setPosition(233, 207);
		btQuickAnswExit.setSize(35, 30);
		dgQuickAnsw.addActor(btQuickAnswExit);

		// add listener
		btQuickAnswExit.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				blackOn = true;
				dgConfirmExit = new AlertDialog(Assets.dgStyle,
						Assets.trDialogAlert, Assets.trButtonYes_R,
						Assets.trButtonYes_P, Assets.trButtonNo_R,
						Assets.trButtonNo_P, Assets.trButtonAgree_R,
						Assets.trButtonAgree_P,
						WheelOfFortune.S_CONFIRM_EXIT_GAME, 1);
				stage.addActor(dgConfirmExit);
			}
		});

		btQuickAnsw = new Button(new TextureRegionDrawable(
				Assets.trButtonAgree_R), new TextureRegionDrawable(
				Assets.trButtonAgree_P));
		btQuickAnsw.setPosition(78, 25);
		btQuickAnsw.setSize(128, 48);
		dgQuickAnsw.addActor(btQuickAnsw);

		// add listener
		btQuickAnsw.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				// set action
				if (tfQuickAnsw.getText().equalsIgnoreCase(
						WheelOfFortune.conectDataBases.get(
								InputScreen.roundNumber - 1).getKeyWord())) {
					// increase score for winner
					InputScreen.players.get(InputScreen.winner)
							.increaseScoreRound(10000);
					// check flag
					InputScreen.isPassSpecialRound = true;
					// flip frames
					for (int i = 0; i < frameWords.size(); i++) {
						frameWords.get(i).updateState(true);
						countFlipFinish++;
					}
					// check finish game
					InputScreen.isFinishedGame = true;
					// display result
					lbFinal.setText(WheelOfFortune.S_RESULT
							+ WheelOfFortune.conectDataBases.get(
									InputScreen.roundNumber - 1).getAnswer());
					// play sound
					worldListener.soWin();
				} else {
					// check fail
					if (countFail <= 1) {
						// toast alert
						worldListener.toFail();
						// fail
						InputScreen.isPassSpecialRound = false;
						// check finish game
						InputScreen.isFinishedGame = true;
						// flip frames
						for (int i = 0; i < frameWords.size(); i++) {
							frameWords.get(i).updateState(true);
							countFlipFinish++;
						}
						// display result
						lbFinal.setText(WheelOfFortune.S_RESULT
								+ WheelOfFortune.conectDataBases.get(
										InputScreen.roundNumber - 1)
										.getAnswer());
						// play sound
						worldListener.soLoose();
					} else {
						// toast alert
						worldListener.toMistake();
					}
					// increase mistake
					InputScreen.players.get(InputScreen.winner).setMistake();
					countFail--;
					lbQuickAnsw.setText("Số Lần Đoán: " + countFail);
					// clear text field
					tfQuickAnsw.setText("");

				}
			}
		});

		stage.addActor(dgQuickAnsw);

	}

	private void initializeFrameWords(String testStr) {
		// generate frameWords
		frameWords = new ArrayList<FrameWord>();

		LinkedList<String> partsOfWord = new LinkedList<String>();
		String testStrNoSpace = testStr.replaceAll("\\s", "").toUpperCase();
		int i = 0;
		int j = 0;

		int lenWithSpace = testStr.length();
		testStr = testStr.trim();
		String[] wordToken = testStr.split("\\s");
		int breakPoint = 0;
		int breakPoint1 = 0;
		int breakPoint2 = 0;

		for (i = 0; i < wordToken.length; i++) {
			partsOfWord.add(wordToken[i]);
		}

		int countSpace = partsOfWord.size() - 1;
		int lenNoSpace = lenWithSpace - countSpace;
		int partsOfWordSize = partsOfWord.size();
		if (partsOfWord.size() == 1) {
		} else if ( partsOfWord.size() > 1 && partsOfWord.size() <= 8) {
			i = 0;
			breakPoint = 0;
			if ((partsOfWord.size() % 2) == 0) {
				while (i <= (partsOfWord.size() / 2)) {
					breakPoint += partsOfWord.getFirst().length();
					partsOfWord.removeFirst();
					i++;
				}
			} else {
				while (i <= (int) (partsOfWord.size() / 2) + 1) {
					breakPoint += partsOfWord.getFirst().length();
					partsOfWord.removeFirst();
					i++;
				}
			}
		} else {
			i = 0;
			j = 0;
			breakPoint1 = 0;
			breakPoint2 = 0;
			while (i <= (partsOfWord.size() / 2)) {
				breakPoint1 += partsOfWord.getFirst().length();
				partsOfWord.removeFirst();
				i++;
			}
			while (j < (partsOfWord.size() - (int) (partsOfWord.size() / 2)) / 2 + 1) {
				breakPoint2 += partsOfWord.getFirst().length();
				partsOfWord.removeFirst();
				j++;
			}
		}

		int rowsOfWord = 0;
		boolean generateFrameWord = false;

		if (partsOfWordSize == 1 || lenNoSpace <= 12)
			rowsOfWord = 1;
		else if (lenNoSpace >= 12 && lenNoSpace <= 24)
			rowsOfWord = 2;
		else
			rowsOfWord = 3;

		while (!generateFrameWord) {

			if (rowsOfWord == 1) {
				for (i = 0; i < lenNoSpace; i++) {
					FrameWord frameWord = new FrameWord(
							900 + i * 100,
							450,
							(50 + WheelOfFortune.DEFAULT_WIDTH - lenNoSpace * 57)
									/ 2 + i * 57, 450, 64, 64,
							testStrNoSpace.charAt(i));

					frameWords.add(frameWord);
				}
				generateFrameWord = true;
			} else if (rowsOfWord == 2) {
				for (i = 0; i < breakPoint; i++) {
					FrameWord frameWord1 = new FrameWord(
							900 + i * 100,
							450,
							(50 + WheelOfFortune.DEFAULT_WIDTH - breakPoint * 57)
									/ 2 + i * 57, 450, 64, 64,
							testStrNoSpace.charAt(i));

					frameWords.add(frameWord1);
				}

				for (j = 0; j < lenNoSpace - breakPoint; j++) {
					FrameWord frameWord2 = new FrameWord(
							900 + j * 100,
							386,
							(50 + WheelOfFortune.DEFAULT_WIDTH - (lenNoSpace - breakPoint) * 57)
									/ 2 + j * 57, 386, 64, 64,
							testStrNoSpace.charAt(j + breakPoint));

					frameWords.add(frameWord2);
				}
				generateFrameWord = true;
			} else {
				for (i = 0; i < breakPoint1; i++) {
					FrameWord frameWord1 = new FrameWord(
							900 + i * 100,
							450,
							(50 + WheelOfFortune.DEFAULT_WIDTH - breakPoint1 * 57)
									/ 2 + i * 57, 450, 64, 64,
							testStrNoSpace.charAt(i));

					frameWords.add(frameWord1);
				}
				for (j = 0; j < breakPoint2; j++) {
					FrameWord frameWord2 = new FrameWord(
							900 + j * 100,
							386,
							(50 + WheelOfFortune.DEFAULT_WIDTH - (breakPoint2) * 57)
									/ 2 + j * 57, 386, 64, 64,
							testStrNoSpace.charAt(j + breakPoint1));

					frameWords.add(frameWord2);
				}
				for (i = 0; i < lenNoSpace - breakPoint1 - breakPoint2; i++) {
					FrameWord frameWord3 = new FrameWord(900 + i * 100, 322,
							(50 + WheelOfFortune.DEFAULT_WIDTH - (lenNoSpace
									- breakPoint1 - breakPoint2) * 57)
									/ 2 + i * 57, 322, 64, 64,
							testStrNoSpace
									.charAt(i + breakPoint1 + breakPoint2));

					frameWords.add(frameWord3);
				}
				generateFrameWord = true;
			}
		} // end generate frame of keyword

		// set texture for frameWords
		for (int k = 0; k < frameWords.size(); k++) {
			TextureRegion textureValue = null;
			textureValue = chooseFrameTexture(frameWords.get(k).getValue());
			frameWords.get(k).setTextureValue(textureValue);
		}
	}

	// get texture for text effect
	private TextureRegion chooseTextTexture(byte value, boolean numberFlag) {
		if (numberFlag) {
			switch (value) {
			case Cone.VALUE_S100:
				return Assets.tr100;

			case Cone.VALUE_S200:
				return Assets.tr200;

			case Cone.VALUE_S300:
				return Assets.tr300;

			case Cone.VALUE_S400:
				return Assets.tr400;

			case Cone.VALUE_S500:
				return Assets.tr500;

			case Cone.VALUE_S600:
				return Assets.tr600;

			case Cone.VALUE_S700:
				return Assets.tr700;

			case Cone.VALUE_S800:
				return Assets.tr800;

			case Cone.VALUE_S900:
				return Assets.tr900;

			case Cone.VALUE_DIVIDED:
				return Assets.trDevide;

			case Cone.VALUE_DOUBLE:
				return Assets.trDouble;

			case Cone.VALUE_LOSTSCORE:
				return Assets.trZero;

			}
		} else {
			switch (value) {
			case Cone.VALUE_LOSTSCORE:
				return Assets.trZero;

			}
		}

		return null;
	}

	// get texture for frameWord
	private TextureRegion chooseFrameTexture(byte value) {
		switch (value) {
		case FrameWord.VALUE_A:
			return Assets.trA;

		case FrameWord.VALUE_B:
			return Assets.trB;

		case FrameWord.VALUE_C:
			return Assets.trC;

		case FrameWord.VALUE_D:
			return Assets.trD;

		case FrameWord.VALUE_E:
			return Assets.trE;

		case FrameWord.VALUE_G:
			return Assets.trG;

		case FrameWord.VALUE_H:
			return Assets.trH;

		case FrameWord.VALUE_I:
			return Assets.trI;

		case FrameWord.VALUE_K:
			return Assets.trK;

		case FrameWord.VALUE_L:
			return Assets.trL;

		case FrameWord.VALUE_M:
			return Assets.trM;

		case FrameWord.VALUE_N:
			return Assets.trN;

		case FrameWord.VALUE_O:
			return Assets.trO;

		case FrameWord.VALUE_P:
			return Assets.trP;

		case FrameWord.VALUE_Q:
			return Assets.trQ;

		case FrameWord.VALUE_R:
			return Assets.trR;

		case FrameWord.VALUE_S:
			return Assets.trS;

		case FrameWord.VALUE_T:
			return Assets.trT;

		case FrameWord.VALUE_U:
			return Assets.trU;

		case FrameWord.VALUE_V:
			return Assets.trV;

		case FrameWord.VALUE_X:
			return Assets.trX;

		case FrameWord.VALUE_Y:
			return Assets.trY;

		}
		return Assets.trA;
	}

	private void checkFinishedRound(float deltaTime) {
		// all frames is flip
		int countFlipped = 0;
		for (int i = 0; i < frameWords.size(); i++) {
			if (frameWords.get(i).getIsFlipped()) {
				countFlipped++;
			}
		}

		// all player are wrong
		int countPlayer = 0;
		for (int i = 0; i < InputScreen.players.size(); i++) {
			if (InputScreen.players.get(i).getMistake() >= 3) {
				countPlayer++;
			}
		}

		// check all
		if (countFlipped >= frameWords.size()) {
			// set input stage
			isStage = true;

			// set can touch for cone
			spCone.setCanTouch(false);

			countTime += deltaTime;
			if (countTime >= 5) {
				state = STATE_FINISHED_ROUND;

				// set can touch for cone
				spCone.setCanTouch(false);

				countTime = 0;
			}
		}

		if (countPlayer >= InputScreen.players.size()) {
			// set input stage
			isStage = true;

			// set can touch for cone
			spCone.setCanTouch(false);

			// flip frameWords
			for (int i = 0; i < frameWords.size(); i++) {
				frameWords.get(i).updateState(true);
			}

			countTime += deltaTime;
			if (countTime >= 5) {
				state = STATE_FINISHED_ROUND;

				// set can touch for cone
				spCone.setCanTouch(false);

				countTime = 0;
			}
		}
	}

	// register listener
	private void registerWorldListener(WorldListener worldListener) {
		this.worldListener = worldListener;
	}

	public void update(float deltaTime) {
		updateSprites(deltaTime);
		if (!InputScreen.isSpecialRound) {
			checkFinishedRound(deltaTime);
			controlInput();
			updatePlayers(deltaTime);
			updateDialogs(deltaTime);
			updateQuizDialog(deltaTime);
		} else {
			updateFinish(deltaTime);
		}
	}

	private void controlInput() {
		if (isInput) {
			Gdx.input.setInputProcessor(this);
			isInput = false;
		}

		if (isStage) {
			Gdx.input.setInputProcessor(stage);
			isStage = false;
		}
	}

	private void updateSprites(float deltaTime) {
		updateFrames(deltaTime);
		if (!InputScreen.isSpecialRound) {
			updateDraft(deltaTime);
			updateTextEffect(deltaTime);
			updateCone();
		}
	}

	// update text effect
	private void updateTextEffect(float deltaTime) {
		if (spTextEffect != null) {
			spTextEffect.update(deltaTime);
		}
	}

	private void updateFrames(float deltaTime) {
		// update frameWords
		for (int i = 0; i < frameWords.size(); i++) {
			frameWords.get(i).update(deltaTime);
		}
		
		// if special round
		if (InputScreen.isSpecialRound) {
			// choose random flip frame
			countTimeFinish += deltaTime;
			if (countTimeFinish >= 2) {
				if (frameWords.size() <= 5) {
					frameWords.get(randFrame1).updateState(true);
				}
				if (frameWords.size() > 5 && frameWords.size() <= 10) {
					frameWords.get(randFrame1).updateState(true);
					frameWords.get(randFrame2).updateState(true);
				}
				if (frameWords.size() > 10) {
					frameWords.get(randFrame1).updateState(true);
					frameWords.get(randFrame2).updateState(true);
					frameWords.get(randFrame3).updateState(true);
				}
				countTimeFinish = 0;
			}
			
		}

		// can touch frameWords
		if (canFlip) {
			for (int i = 0; i < frameWords.size(); i++) {
				frameWords.get(i).setCanTouch(true);
			}
		} else {
			for (int i = 0; i < frameWords.size(); i++) {
				frameWords.get(i).setCanTouch(false);
			}
		}

		for (int i = 0; i < frameWords.size(); i++) {
			if (frameWords.get(i).getIsTouched()) {
				// play sound
				worldListener.soSelecText();
				canFlip = false;
				frameWords.get(i).setIsTouched(false);
				break;
			}
		}

	}

	private void updateDraft(float deltaTime) {
		// update draft
		spDraft.update(deltaTime);
	}

	private void updateCone() {
		// update cone
		spCone.updateFollowModel(modelCone);
		spCone.update();
		if (spCone.getCanTouch()) {
			modelCone.getBody().setUserData("canTouch");
		} else {
			modelCone.getBody().setUserData("noTouch");
		}

		// update needle
		spNeedle.updateFollowModel(modelNeedle);

		// process cone value
		if (spCone.getValue() != Cone.VALUE_NULL) {
			currentConeValue = spCone.getValue();

			// log
			Gdx.app.debug("[DEBUG] CONE VALUE: ", "" + currentConeValue);
			
			// play sound
			worldListener.soConeStop();

			switch (spCone.getValue()) {
			// lost turn
			case Cone.VALUE_LOSTTURN:
				// change player
				if (state != STATE_FINISHED_ROUND) {
					changePlayer();
				}
				spCone.setCanTouch(true);
				break;

			// get gift
			case Cone.VALUE_GIFT:
				getGift();
				break;

			case Cone.VALUE_LUCKY:
				getLucky();
				break;

			case Cone.VALUE_INVALID:
				// toast alert
				worldListener.toAlert();
				// set can touch for cone
				spCone.setCanTouch(true);

				break;

			default:
				// count time
				isCountDown = true;
				spDraft.updateState(Draft.STATE_FLYUP);
				// set can touch for cone
				spCone.setCanTouch(false);

				break;
			}

		}
	}

	private void updateFinish(float deltaTime) {
		// update player
		InputScreen.players.get(InputScreen.winner).update(deltaTime);
		// flip frames
		if (countFlipFinish >= frameWords.size()) {
			countTime += deltaTime;
			if (countTime >= 5) {
				// set standby screen
				InputScreen.isFinishedGame = true;
				worldListener.setStandbyScreen();
			}
		}
		// update finish
		// update exit dialog
		if (dgConfirmExit != null) {
			if (dgConfirmExit.getIsYes()) {
				// show admob
				worldListener.showIntertitial();
				worldListener.setMenuScreen();
			}

			if (dgConfirmExit.getIsNo()) {
				blackOn = false;
				// play music
				worldListener.muResumeMuGame();
				dgConfirmExit.setVisible(false);
				dgConfirmExit = null;
			}
		}
	}

	private void updatePlayers(float deltaTime) {
		if (state == STATE_FINISHED_ROUND) {
			// play sound
			if (canPlaySound) {
				worldListener.soFinishRound();
				
				// show admob
				worldListener.showIntertitial();
				
				canPlaySound = false;
			}
			maxScore = InputScreen.players.get(0).getScoreRound();
			// search maxScore
			for (int i = 0; i < InputScreen.players.size(); i++) {
				if (maxScore < InputScreen.players.get(i).getScoreRound()) {
					maxScore = InputScreen.players.get(i).getScoreRound();
				}

				// move players
				InputScreen.players.get(i).dispose();
				InputScreen.players.get(i).moveInputScreen(i + 1);
			}

			// search winner
			for (int i = 0; i < InputScreen.players.size(); i++) {
				if (InputScreen.players.get(i).getScoreRound() == maxScore) {
					winPlayer = (byte) i;
					// add score
					worldListener.saveHscore(maxScore);
					break;
				}
			}

			// display label
			lbAlert.setVisible(true);
			lbResult.setVisible(true);
			lbCongras.setText(WheelOfFortune.S_CONGRAS
					+ InputScreen.players.get(winPlayer).getName());
			lbCongras.setVisible(true);

			// display effect
			Assets.efStar.setPosition(InputScreen.players.get(winPlayer).getX()
					+ InputScreen.players.get(winPlayer).getWidth() / 2,
					InputScreen.players.get(winPlayer).getY()
							+ InputScreen.players.get(winPlayer).getHeight()
							/ 2);
			Assets.efStar.start();

			// set standby screen
			if (Gdx.input.justTouched()) {
				InputScreen.roundNumber++;
				worldListener.setStandbyScreen();
			}

		}

		for (int i = 0; i < InputScreen.players.size(); i++) {
			InputScreen.players.get(i).update(deltaTime);

			// change player
			if (InputScreen.players.get(i).getIsPlay()) {
				currentPlayer = (byte) i;
				InputScreen.players.get(i).updateState(Player.STATE_FLYUP);
			} else {
				InputScreen.players.get(i).updateState(Player.STATE_FLYDOWN);
			}

		}
		
		// count down time
		if (isCountDown) {
			if (answerTime < 0) {
				if (spDraft.getState() == Draft.STATE_FLYUP) {
					spDraft.updateState(Draft.STATE_FLYDOWN);
				}
				// disappear dialog, black
				if (dgQuickAnsw != null) {
					isInput = true;
					blackOn = false;
					// invisible hint
					lbHint.setVisible(false);
					dgQuickAnsw.setVisible(false);
					dgQuickAnsw = null;
				}
				
				if (dgChoice != null) {
					// set can touch for cone
					spCone.setCanTouch(true);
					// set input
					Gdx.input.setInputProcessor(this);
					dgChoice.setVisible(false);
					dgChoice = null;
				}
				
				InputScreen.players.get(currentPlayer).setMistake();
				// change player
				if (state != STATE_FINISHED_ROUND) {
					changePlayer();
				}
				spCone.setCanTouch(true);
				answerTime = 30;
				secondCount = 0;
				isCountDown = false;
			}
			secondCount += deltaTime;
			if (secondCount >= 1) {
				answerTime--;
				secondCount = 0;
			}
		}

	}

	private void updateQuizDialog(float deltaTime) {
		// update quizDialog, playerInforDialog
		dgQuiz.update(deltaTime);
		dgPlayerInfor.update(deltaTime);
		dgPlayerInfor.updateInfor(InputScreen.players.get(0), InputScreen.players.get(1), InputScreen.players.get(2));
	
		if (state == STATE_FINISHED_ROUND) {
			dgQuiz.setVisible(false);
			dgPlayerInfor.setVisible(false);
		}
	}

	private void updateDialogs(float deltaTime) {
		// update choiceDialog
		if (dgChoice != null) {

			Gdx.input.setInputProcessor(stage);

			if (dgChoice.getIsYes()) {
				// set can touch for cone
				spCone.setCanTouch(true);
				
				// reset count down time
				answerTime = 30;
				secondCount = 0;
				isCountDown = false;

				checkChoice();

			}

			if (dgChoice.getIsNo()) {
				spDraft.updateState(Draft.STATE_FLYUP);
				// set can touch for cone
				spCone.setCanTouch(false);

				dgChoice = null;
				Gdx.input.setInputProcessor(this);
			}

		}

		// update resultChoiceDialog
		if (dgResultChoice != null) {
			if (dgResultChoice.getIsAgree()) {

				// process dialog's state
				switch (stateDialog) {
				case 1:
				case 3:
					// play sound
					worldListener.soWrong();
					controlPlus = 0;
					// increase mistake
					InputScreen.players.get(currentPlayer).setMistake();
					if (InputScreen.players.get(currentPlayer).getMistake() >= 3) {
						// toast
						worldListener.toLostPlay(InputScreen.players.get(
								currentPlayer).getName());
					}
					if (state != STATE_FINISHED_ROUND) {
						changePlayer();
					}
					break;

				case 2:
					// play sound
					worldListener.soTrue();
					controlPlus = 1;
					break;

				default:
					break;
				}

				// flip frames
				for (int i = 0; i < frameWords.size(); i++) {
					if (spDraft.getValue() == frameWords.get(i).getValue()) {
						frameWords.get(i).updateState(true);
					}
				}

				// other cases
				switch (currentConeValue) {
				case Cone.VALUE_S100:
					InputScreen.players.get(currentPlayer).increaseScoreRound(
							controlPlus * countWord * 100);
					break;

				case Cone.VALUE_S200:
					InputScreen.players.get(currentPlayer).increaseScoreRound(
							controlPlus * countWord * 200);
					break;

				case Cone.VALUE_S300:
					InputScreen.players.get(currentPlayer).increaseScoreRound(
							controlPlus * countWord * 300);
					break;

				case Cone.VALUE_S400:
					InputScreen.players.get(currentPlayer).increaseScoreRound(
							controlPlus * countWord * 400);
					break;

				case Cone.VALUE_S500:
					InputScreen.players.get(currentPlayer).increaseScoreRound(
							controlPlus * countWord * 500);
					break;

				case Cone.VALUE_S600:
					InputScreen.players.get(currentPlayer).increaseScoreRound(
							controlPlus * countWord * 600);
					break;

				case Cone.VALUE_S700:
					InputScreen.players.get(currentPlayer).increaseScoreRound(
							controlPlus * countWord * 700);
					break;

				case Cone.VALUE_S800:
					InputScreen.players.get(currentPlayer).increaseScoreRound(
							controlPlus * countWord * 800);
					break;

				case Cone.VALUE_S900:
					InputScreen.players.get(currentPlayer).increaseScoreRound(
							controlPlus * countWord * 900);
					break;

				case Cone.VALUE_LOSTSCORE:
					int tempScore = InputScreen.players.get(currentPlayer)
							.getScoreRound();
					InputScreen.players.get(currentPlayer).decreaseScoreRound(
							tempScore);
					break;

				case Cone.VALUE_DOUBLE:
					int tempScore1 = InputScreen.players.get(currentPlayer)
							.getScoreRound();
					InputScreen.players.get(currentPlayer).increaseScoreRound(
							tempScore1);
					break;

				case Cone.VALUE_DIVIDED:
					int tempScore2 = InputScreen.players.get(currentPlayer)
							.getScoreRound() / 2;
					InputScreen.players.get(currentPlayer).decreaseScoreRound(
							tempScore2);
					break;

				default:
					break;
				}
				// text effect
				if ((currentConeValue != Cone.VALUE_LOSTTURN)
						&& (currentConeValue != Cone.VALUE_NULL)
						&& (currentConeValue != Cone.VALUE_INVALID)
						&& (currentConeValue != Cone.VALUE_GIFT)
						&& (currentConeValue != Cone.VALUE_LUCKY)) {
					TextureRegion textTexture = null;
					if (controlPlus == 1) {
						textTexture = chooseTextTexture(currentConeValue, true);
						spTextEffect = new TextEffect(textTexture, 0, 70);
					}
				}

				// destroy dialog
				dgChoice = null;
				Gdx.input.setInputProcessor(this);
				dgResultChoice = null;
			}
		}

		// update exit dialog
		if (dgConfirmExit != null) {
			if (dgConfirmExit.getIsYes()) {
				worldListener.setMenuScreen();
			}

			if (dgConfirmExit.getIsNo()) {
				blackOn = false;
				dgConfirmExit = null;
				Gdx.input.setInputProcessor(this);
			}
		}
	}

	private void checkChoice() {
		checkValue = false; // check if word is chosen
		countWord = 0;

		for (int i = 0; i < frameWords.size(); i++) {
			if (spDraft.getValue() == frameWords.get(i).getValue()) {
				countWord++;
			}
		}

		if (countWord > 0) {
			for (int i = 0; i < checkValues.size(); i++) {
				if (spDraft.getValue() == checkValues.get(i)) {
					checkValue = true;
					break;
				}
			}
			// add new value to checkValues
			checkValues.add(spDraft.getValue());
		}

		if (canCreateDialog) {
			String text;
			if (countWord > 0) {
				if (checkValue) {
					text = "Chữ " + spDraft.getTextValue() + " đã được chọn!";
					stateDialog = 1;
				} else {
					text = "Có " + countWord + " chữ " + spDraft.getTextValue();
					stateDialog = 2;
				}
			} else {
				text = "Không có chữ " + spDraft.getTextValue() + " nào!";
				stateDialog = 3;
			}
			dgResultChoice = new AlertDialog(Assets.dgStyle,
					Assets.trDialogAlert, Assets.trButtonYes_R,
					Assets.trButtonYes_P, Assets.trButtonNo_R,
					Assets.trButtonNo_P, Assets.trButtonAgree_R,
					Assets.trButtonAgree_P, text, 2);
			stage.addActor(dgResultChoice);
			canCreateDialog = false;
		}

	}

	// if player get a gift
	private void getGift() {
		// display toast
		worldListener.toGift();

		// display text effect
		spTextEffect = new TextEffect(Assets.trGift, 0, 70);

		// set can touch for cone
		spCone.setCanTouch(true);
	}

	// if player get lucky
	private void getLucky() {
		// display toast
		worldListener.toLucky();

		// display text effect
		spTextEffect = new TextEffect(Assets.trLucky, 0, 70);

		canFlip = true;

		// set can touch for cone
		spCone.setCanTouch(true);
	}

	// change player method
	private void changePlayer() {

		int j = 0;
		for (int i = 0; i < InputScreen.players.size(); i++) {
			if (InputScreen.players.get(i).getIsPlay()) {
				j = i;
				InputScreen.players.get(i).setIsPlay(false);
			}
		}

		j++;
		if (j >= InputScreen.players.size()) {
			j = j - InputScreen.players.size();
		}

		// person1
		if (!InputScreen.players.get(j).getCanPlay()) {
			j++;
			if (j >= InputScreen.players.size()) {
				j = j - InputScreen.players.size();
			}
		}

		// person2
		if (!InputScreen.players.get(j).getCanPlay()) {
			j++;
			if (j >= InputScreen.players.size()) {
				j = j - InputScreen.players.size();
			}
		}

		// check players
		int countPlayer = 0;
		for (int i = 0; i < InputScreen.players.size(); i++) {
			if (InputScreen.players.get(i).getMistake() >= 3) {
				countPlayer++;
			}
		}

		// display toast
		if (countPlayer < 3) {
			worldListener.toChangePlayer("" + (j + 1) + ":\n  " + InputScreen.players.get(j).getName());
		}

		InputScreen.players.get(j).setIsPlay(true);

	}

	public byte getState() {
		return state;
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// when click in back key
		if (keycode == Keys.BACK) {
			blackOn = true;
			// pause music
			worldListener.muPauseMuGame();
			dgConfirmExit = new AlertDialog(Assets.dgStyle,
					Assets.trDialogAlert, Assets.trButtonYes_R,
					Assets.trButtonYes_P, Assets.trButtonNo_R,
					Assets.trButtonNo_P, Assets.trButtonAgree_R,
					Assets.trButtonAgree_P, WheelOfFortune.S_CONFIRM_EXIT_GAME,
					1);
			stage.addActor(dgConfirmExit);
			Gdx.input.setInputProcessor(stage);
		}

		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// get touch
		xTouch = screenX * MainMenuScreen.rateX;
		yTouch = screenY * MainMenuScreen.rateY;
		
		// touch models: cone
		BoxUtility.touchObject(screenX, screenY, world, camera, modelGround, "noTouch", 100f);

		// touch quizDialog, playerInforDialog
		dgQuiz.checkClicked(BoxUtility.getTestPoint().x, BoxUtility.getTestPoint().y, 1);
		dgPlayerInfor.checkClicked(BoxUtility.getTestPoint().x, BoxUtility.getTestPoint().y, 2);
		
		// touch draft
		int getChar;
		getChar = spDraft.chooseWord(xTouch, WheelOfFortune.DEFAULT_HEIGHT - yTouch);
		if (getChar != Draft.VALUE_NULL) {
			// draft fly up
			spDraft.updateState(Draft.STATE_FLYDOWN);

			// make a dialog
			String text = WheelOfFortune.S_CHOOSE_WORD + spDraft.getTextValue();
			dgChoice = new AlertDialog(Assets.dgStyle, Assets.trDialogAlert,
					Assets.trButtonYes_R, Assets.trButtonYes_P,
					Assets.trButtonNo_R, Assets.trButtonNo_P,
					Assets.trButtonAgree_R, Assets.trButtonAgree_P, text, 1);
			stage.addActor(dgChoice);

			// can make resultDialog
			canCreateDialog = true;
		}

		// touch frameWords
		for (int i = 0; i < frameWords.size(); i++) {
			frameWords.get(i).checkClicked(xTouch, WheelOfFortune.DEFAULT_HEIGHT - yTouch);
		}

		// run effect
		if (state != STATE_FINISHED_ROUND) {
			Assets.efStar.setPosition(xTouch, WheelOfFortune.DEFAULT_HEIGHT - yTouch);
			Assets.efStar.start();
		}

		// make quick answer dialog
		// make dialog
		if (xTouch >= 35 && xTouch <= 167 && yTouch >= 300 && yTouch <= 460) {
			//
			Gdx.input.setInputProcessor(stage);
			blackOn = true;

			// set hint
			lbHint.setVisible(true);

			dgQuickAnsw = new Dialog("", Assets.dgStyle);
			dgQuickAnsw.setBackground(new TextureRegionDrawable(
					Assets.trDialogQuickAnsw));
			dgQuickAnsw.setPosition(258, 50);
			dgQuickAnsw.setSize(285, 250);

			tfQuickAnsw = new TextField("", Assets.tfStyle);
			tfQuickAnsw.setMessageText("Trả lời . . .");
			tfQuickAnsw.setPosition(50, 110);
			dgQuickAnsw.addActor(tfQuickAnsw);

			btQuickAnsw = new Button(new TextureRegionDrawable(
					Assets.trButtonAgree_R), new TextureRegionDrawable(
					Assets.trButtonAgree_P));
			btQuickAnsw.setPosition(78, 25);
			btQuickAnsw.setSize(128, 48);
			dgQuickAnsw.addActor(btQuickAnsw);

			// add listener
			btQuickAnsw.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					// reset count down time
					answerTime = 30;
					secondCount = 0;
					isCountDown = false;
					
					if (tfQuickAnsw.getText().equalsIgnoreCase(
							WheelOfFortune.conectDataBases.get(
									InputScreen.roundNumber - 1).getKeyWord())) {
						// play sound
						worldListener.soTrue();
						// flip frameWords
						for (int i = 0; i < frameWords.size(); i++) {
							frameWords.get(i).updateState(true);
						}
						// increase score for player
						InputScreen.players.get(currentPlayer).increaseScoreRound(1000);
						if (spDraft.getState() == Draft.STATE_FLYUP) {
							spDraft.updateState(Draft.STATE_FLYDOWN);
						}
					} else {
						// play sound
						worldListener.soWrong();
						// toast
						worldListener.toLostPlay(InputScreen.players.get(currentPlayer).getName());
						// lost play
						InputScreen.players.get(currentPlayer).setCanPlay(false);
						// check finish
						byte countPlayer = 0;
						for (byte i = 0; i < InputScreen.players.size(); i++) {
							if (!InputScreen.players.get(i).getCanPlay()) {
								countPlayer++;
							}
						}
						if (countPlayer == InputScreen.players.size()) {
							state = STATE_FINISHED_ROUND;
						}
						// change player
						if (state != STATE_FINISHED_ROUND) {
							changePlayer();
						}
						// change state of draft
						if (spDraft.getState() == Draft.STATE_FLYUP) {
							spDraft.updateState(Draft.STATE_FLYDOWN);
							spCone.setCanTouch(true);
						}
					}
					isInput = true;
					blackOn = false;
					// invisible hint
					lbHint.setVisible(false);
					dgQuickAnsw.setVisible(false);
					dgQuickAnsw = null;
				}
			});

			btQuickAnswExit = new Button(new TextureRegionDrawable(
					Assets.trAssetNull));
			btQuickAnswExit.setPosition(233, 207);
			btQuickAnswExit.setSize(35, 30);
			dgQuickAnsw.addActor(btQuickAnswExit);

			// add listener
			btQuickAnswExit.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					isInput = true;
					blackOn = false;
					// invisible hint
					lbHint.setVisible(false);
					dgQuickAnsw.setVisible(false);
					dgQuickAnsw = null;
				}
			});

			stage.addActor(dgQuickAnsw);
		}

		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// release objects
		BoxUtility.releaseObject(world);

		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// scale touch
		xTouch = screenX * MainMenuScreen.rateX;
		yTouch = screenY * MainMenuScreen.rateY;
		
		// drag objects
		BoxUtility.dragObject(camera, screenX, screenY);

		// run effect
		if (state != STATE_FINISHED_ROUND) {
			Assets.efStar.setPosition(xTouch, WheelOfFortune.DEFAULT_HEIGHT - yTouch);
			Assets.efStar.start();
		}

		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

}
