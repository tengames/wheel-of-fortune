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
package com.tengames.wheeloffortune.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.tengames.wheeloffortune.main.Assets;
import com.tengames.wheeloffortune.main.WheelOfFortune;

public class LoadingScreen implements Screen {
	  protected WheelOfFortune game;
	  protected Stage stage;
    protected int type;
    
	  private Image logo;
    private Image loadingFrame;
    private Image loadingBarHidden;
    private Image screenBg;
    private Image loadingBg; 
    
    private float startX, endX;
    private float percent;
    
    private Actor loadingBar;
	
	public LoadingScreen(WheelOfFortune game) {
		this.game = game;
		
		Gdx.input.setCatchBackKey(true);
		Assets.loadLoading();
		Assets.load();
	}
	
	private void create_actor(){
		 stage.setViewport(800 , 480, false);      

       // Make the background fill the screen
       screenBg.setSize(800, 480);

       // Place the logo in the middle of the screen and 100 px up
       logo.setX((800 - logo.getWidth()) / 2);
       logo.setY((480 - logo.getHeight()) / 2 + 100);

       // Place the loading frame in the middle of the screen
       loadingFrame.setX((800 - loadingFrame.getWidth()) / 2);
       loadingFrame.setY(150);

       // Place the loading bar at the same spot as the frame, adjusted a few px
       loadingBar.setX(loadingFrame.getX() + 15);
       loadingBar.setY(loadingFrame.getY() + 5);

       // Place the image that will hide the bar on top of the bar, adjusted a few px
       loadingBarHidden.setX(loadingBar.getX() + 35);
       loadingBarHidden.setY(loadingBar.getY() - 3);
       // The start position and how far to move the hidden loading bar
       startX = loadingBarHidden.getX();
       endX = 440;

       // The rest of the hidden bar
       loadingBg.setSize(450, 50);
       loadingBg.setX(loadingBarHidden.getX() + 30);
       loadingBg.setY(loadingBarHidden.getY() + 3);
	}
	
	@Override
	public void render(float delta) {
		  // Clear the screen
      Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);

      if (Assets.assetManager.update() && percent > 0.99f) {
    	  Assets.loadDone();
    	  game.setScreen(new MainMenuScreen(game));
      }

      percent = Interpolation.linear.apply(percent, Assets.assetManager.getProgress(), 0.02f);

      // Update positions (and size) to match the percentage
      loadingBarHidden.setX(startX + endX * percent);
      loadingBg.setX(loadingBarHidden.getX() + 30);
      loadingBg.setWidth(450 - 450 * percent);
      loadingBg.invalidate();

      // Show the loading screen
      stage.act(delta);
      stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		
	}

	@Override
	public void show() {
        stage = new Stage();

        // Get our texture atlas from the manager
        TextureAtlas atlas = Assets.assetManager.get("data/drawable/loading.atlas", TextureAtlas.class);

        // Grab the regions from the atlas and create some images
        logo = new Image(atlas.findRegion("libgdx-logo"));
        loadingFrame = new Image(atlas.findRegion("loading-frame"));
        loadingBarHidden = new Image(atlas.findRegion("loading-bar-hidden"));
        screenBg = new Image(atlas.findRegion("screen-bg"));
        loadingBg = new Image(atlas.findRegion("loading-frame-bg"));

//       Or if you only need a static bar, you can do
         loadingBar = new Image(atlas.findRegion("loading-bar1"));

        // Add all the actors to the stage
        stage.addActor(screenBg);
        stage.addActor(loadingBar);
        stage.addActor(loadingBg);
        stage.addActor(loadingBarHidden);
        stage.addActor(loadingFrame);
        stage.addActor(logo);
        
        create_actor();
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
		
	}

	@Override
	public void resume() {
		
	}

	@Override
	public void dispose() {
	}

}
