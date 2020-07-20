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

public interface WorldListener {
	
	public void setStandbyScreen();
	
	public void setMenuScreen();
	
	public void toChangePlayer(String name);
	
	public void toGift();
	
	public void toLucky();
	
	public void toAlert();
	
	public void toLostPlay(String name);
	
	public void toMistake();
	
	public void toFail();
	
	public void toHint();
	
	public void soRound1();
	
	public void soRound2();
	
	public void soRound3();
	
	public void soRoundS();
	
	public void soSelecText();
	
	public void soConeRound();
	
	public void soConeStop();
	
	public void soWin();
	
	public void soLoose();
	
	public void soTrue();
	
	public void soWrong();
	
	public void soFinishRound();
	
	public void muPauseMuGame();
	
	public void muResumeMuGame();
	
	public void saveHscore(int addScore);
	
	public void showIntertitial();
	
	public void traceScene(String level);
	
}
