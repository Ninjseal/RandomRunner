package com.gdxcollab.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.Disposable;
import com.gdxcollab.game.utils.Assets;
import com.gdxcollab.game.utils.Constants;
import com.gdxcollab.game.utils.GamePreferences;

public class WorldRenderer implements Disposable {

	private static final String TAG = WorldRenderer.class.getName();
	
	private static final boolean DEBUG_DRAW_BOX2D_WORLD = false;
	
	private OrthographicCamera camera;
	private OrthographicCamera hudCamera;
	private SpriteBatch batch;
	private WorldController worldController;
	private Box2DDebugRenderer b2debugRenderer;
	
	public WorldRenderer(WorldController worldController) {
		this.worldController = worldController;
		init();
	}
	
	private void init() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
		camera.position.set(0, 0, 0);
		camera.update();
		hudCamera = new OrthographicCamera(Constants.HUD_WIDTH, Constants.HUD_HEIGHT);
		hudCamera.position.set(0, 0, 0);
		hudCamera.setToOrtho(true); // Flip y-axis
		hudCamera.update();
		b2debugRenderer = new Box2DDebugRenderer();
	}
	
	public void render() {
		renderWorld(batch);
		renderHud(batch);
	}
	
	public void resize(int width, int height) {
		camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / height) * width;
		camera.update();
		hudCamera.viewportHeight = Constants.HUD_HEIGHT;
		hudCamera.viewportWidth = (Constants.HUD_HEIGHT / (float)height) * (float)width;
		hudCamera.position.set(hudCamera.viewportWidth / 2, hudCamera.viewportHeight / 2, 0);
		hudCamera.update();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
	}

	private void renderWorld(SpriteBatch batch) {
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		// TODO:..
		batch.end();
	}
	
	private void renderHud(SpriteBatch batch) {
		batch.setProjectionMatrix(hudCamera.combined);
		batch.begin();
		
		// Draw collected gold coins icon + text (anchored to top left edge) 
		//renderHudScore(batch);
		// Draw collected feather icon (anchored to top left edge)
		//renderHudPowerup(batch);
		// Draw extra lives icon + text (anchored to top right edge)
		//renderHudLives(batch);
		// Draw FPS text (anchored to bottom right edge)
		if(GamePreferences.instance.showFpsCounter)
			renderFpsCounter(batch);
		if(worldController.isPaused())
			renderPauseMenu(batch);
		
		batch.end();
	}
	
	private void renderFpsCounter(SpriteBatch batch) {
		float x = hudCamera.viewportWidth - 55;
		float y = hudCamera.viewportHeight - 15;
		int fps = Gdx.graphics.getFramesPerSecond();
		BitmapFont fpsFont = Assets.instance.font.defaultNormal;
		if(fps >= 45) {
			// 45 or more FPS show up in green
			fpsFont.setColor(0, 1, 0, 1);
		} else if(fps >= 30) {
			// 30 or more FPS show up in yellow
			fpsFont.setColor(1, 1, 0, 1);
		} else {
			// Less than 30 FPS show up in red
			fpsFont.setColor(1, 0, 0, 1);
		}
		fpsFont.draw(batch, "FPS: " + fps, x, y);
		fpsFont.setColor(1, 1, 1, 1); // White
	}
	
	private void renderPauseMenu(SpriteBatch batch) {
		float x = hudCamera.viewportWidth / 2 - 60;
		float y = hudCamera.viewportHeight / 2;
		BitmapFont fontPause = Assets.instance.font.defaultBig;
		fontPause.setColor(1, 0.75f, 0.25f, 1);
		fontPause.draw(batch, "PAUSED", x, y);
		fontPause.setColor(1, 1, 1, 1);
	}
	
}
