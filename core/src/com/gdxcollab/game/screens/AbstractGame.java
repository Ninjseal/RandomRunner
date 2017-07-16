package com.gdxcollab.game.screens;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.gdxcollab.game.screens.transitions.ScreenTransition;

public abstract class AbstractGame implements ApplicationListener {

	private boolean init;
	private AbstractGameScreen currScreen;
	private AbstractGameScreen nextScreen;
	private FrameBuffer currFbo;
	private FrameBuffer nextFbo;
	SpriteBatch batch;
	private float t;
	private ScreenTransition screenTransition;
	
	public void setScreen(AbstractGameScreen screen) {
		setScreen(screen, null);
	}
	
	public void setScreen(AbstractGameScreen screen, ScreenTransition screenTransition) {
		int w = Gdx.graphics.getWidth();
		int h = Gdx.graphics.getHeight();
		if(!init) {
			currFbo = new FrameBuffer(Format.RGB888, w, h, false);
			nextFbo = new FrameBuffer(Format.RGB888, w, h, false);
			batch = new SpriteBatch();
			init = true;
		}
		// Start new transition
		nextScreen = screen;
		nextScreen.show(); // Activate next screen
		nextScreen.resize(w, h);
		nextScreen.render(0); // Let screen update() once
		if(currScreen != null) currScreen.pause();
		nextScreen.pause();
		Gdx.input.setInputProcessor(null); // Disable input
		this.screenTransition = screenTransition;
		t = 0;
	}
	
	@Override
	public void render() {
		// Get delta time and ensure an upper limit of one 60th second
		float deltaTime = Math.min(Gdx.graphics.getDeltaTime(), 1.0f/60.0f);
		if(nextScreen == null) {
			// No ongoing transition
			if(currScreen != null) currScreen.render(deltaTime);
		} else {
			// Ongoing transition
			float duration = 0;
			if(screenTransition != null)
				duration = screenTransition.getDuration();
			// Update progress of ongoing transition
			t = Math.min(t + deltaTime, duration);
			if(screenTransition == null || t >= duration) {
				// No transition effect set of transition has just finished
				if(currScreen != null) currScreen.hide();
				nextScreen.resume();
				// Enable input for next screen
				Gdx.input.setInputProcessor(nextScreen.getInputProcessor());
				// Switch screens
				currScreen = nextScreen;
				nextScreen = null;
				screenTransition = null;
			} else {
				// Render screens to FBOs
				currFbo.begin();
				if(currScreen != null) currScreen.render(deltaTime);
				currFbo.end();
				nextFbo.begin();
				nextScreen.render(deltaTime);
				nextFbo.end();
				// Render transition effect to screen
				float alpha = t / duration;
				screenTransition.render(batch, currFbo.getColorBufferTexture(), nextFbo.getColorBufferTexture(), alpha);
				
			}
		}
	}
	
	@Override
	public void resize(int width, int height) {
		if(currScreen != null) currScreen.resize(width, height);
		if(nextScreen != null) nextScreen.resize(width, height);
	}
	
	@Override
	public void pause() {
		if(currScreen != null) currScreen.pause();
	}
	
	@Override
	public void resume() {
		if(currScreen != null) currScreen.resume();
	}
	
	@Override
	public void dispose() {
		if(currScreen != null) currScreen.hide();
		if(nextScreen != null) nextScreen.hide();
		if(init) {
			currFbo.dispose();
			currScreen = null;
			nextFbo.dispose();
			nextScreen = null;
			batch.dispose();
			init = false;
		}
	}
	
}
