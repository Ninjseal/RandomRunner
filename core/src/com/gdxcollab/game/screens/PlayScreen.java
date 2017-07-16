package com.gdxcollab.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.gdxcollab.game.WorldController;
import com.gdxcollab.game.WorldRenderer;

public class PlayScreen extends AbstractGameScreen {

	private static final String TAG = PlayScreen.class.getName();

	private WorldController worldController;
	private WorldRenderer worldRenderer;

	private boolean paused;

	public PlayScreen(final AbstractGame game) {
		super(game);
	}

	@Override
	public void render(float deltaTime) {
		// Do not update game world when paused
		if (!paused && !worldController.isPaused()) {
			// Update game world by the time that has passed since last rendered
			// frame
			worldController.update(deltaTime);
		}
		// Sets the clear screen color to: Cornflower Blue
		Gdx.gl.glClearColor(100 / 255.0f, 149 / 255.0f, 237 / 255.0f, 255 / 255.0f);
		// Clears the screen
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		// Render game world to screen
		worldRenderer.render();
	}

	@Override
	public void resize(int width, int height) {
		worldRenderer.resize(width, height);
	}

	@Override
	public void show() {
		worldController = new WorldController(game);
		worldRenderer = new WorldRenderer(worldController);
	}

	@Override
	public void hide() {
		worldController.dispose();
		worldRenderer.dispose();
	}

	@Override
	public void pause() {
		paused = true;
	}

	@Override
	public void resume() {
		paused = false;
	}

	@Override
	public InputProcessor getInputProcessor() {
		return worldController;
	}

	@Override
	public void dispose() {
		// TODO
	}

}
