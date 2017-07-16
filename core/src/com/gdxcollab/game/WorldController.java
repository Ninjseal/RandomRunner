package com.gdxcollab.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.Disposable;
import com.gdxcollab.game.GameMain.ScreenType;
import com.gdxcollab.game.objects.Player;
import com.gdxcollab.game.screens.AbstractGame;
import com.gdxcollab.game.utils.CameraHelper;
import com.gdxcollab.game.utils.Level;
import com.hisame.game.screens.transitions.ScreenTransition;
import com.hisame.game.screens.transitions.ScreenTransitionSlide;

public class WorldController extends InputAdapter implements Disposable {

	private static final String TAG = WorldController.class.getName();
	
	private final AbstractGame game;
	private Player player;
	private Level level;

	public CameraHelper cameraHelper;
	private boolean paused;

	public WorldController(final AbstractGame game) {
		this.game = game;
		init();
	}

	private void init() {
		cameraHelper = new CameraHelper();
		player = new Player();
		level = new Level();
		cameraHelper.setTarget(player);
		paused = false;
	}

	public void update(float deltaTime) {
		handleDebugInput(deltaTime);
		handleGameInput(deltaTime);
		//player.update(deltaTime);
		//level.update(deltaTime);
		cameraHelper.update(deltaTime);
	}

	private void handleDebugInput(float deltaTime) {
		if (!cameraHelper.hasTarget(player)) {
			// Camera Controls (move)
			float camMoveSpeed = 5 * deltaTime;
			float camMoveSpeedAccelerationFactor = 5;
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
				camMoveSpeed *= camMoveSpeedAccelerationFactor;
			if (Gdx.input.isKeyPressed(Keys.LEFT))
				moveCamera(-camMoveSpeed, 0);
			if (Gdx.input.isKeyPressed(Keys.RIGHT))
				moveCamera(camMoveSpeed, 0);
			if (Gdx.input.isKeyPressed(Keys.UP))
				moveCamera(0, camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.DOWN))
				moveCamera(0, -camMoveSpeed);
			if (Gdx.input.isKeyPressed(Keys.BACKSPACE))
				cameraHelper.setPosition(0, 0);
		}

		// Camera Controls (zoom)
		float camZoomSpeed = 1 * deltaTime;
		float camZoomSpeedAccelerationFactor = 5;
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT))
			camZoomSpeed *= camZoomSpeedAccelerationFactor;
		if (Gdx.input.isKeyPressed(Keys.COMMA))
			cameraHelper.addZoom(camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.PERIOD))
			cameraHelper.addZoom(-camZoomSpeed);
		if (Gdx.input.isKeyPressed(Keys.SLASH))
			cameraHelper.setZoom(1);
	}

	private void handleGameInput(float deltaTime) {
		if (cameraHelper.hasTarget(player)) {
			// Player Movement
			if (Gdx.input.isKeyPressed(Keys.A)) {
				
			} else if (Gdx.input.isKeyPressed(Keys.D)) {
				
			} else {
				// Sample code for auto-forward movement...
				
			}

			if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
				
			}
		}
	}

	private void moveCamera(float x, float y) {
		x += cameraHelper.getPosition().x;
		y += cameraHelper.getPosition().y;
		cameraHelper.setPosition(x, y);
	}

	@Override
	public boolean keyUp(int keycode) {
		// Reset game world
		if (keycode == Keys.R) {
			init();
			Gdx.app.debug(TAG, "Game world resetted");
		}
		// Toggle camera follow
		else if (keycode == Keys.ENTER) {
			cameraHelper.setTarget(cameraHelper.hasTarget() ? null : player);
			Gdx.app.debug(TAG, "Camera follow enabled: " + cameraHelper.hasTarget());
		}
		// Back to Menu
		else if (keycode == Keys.ESCAPE) {
			backToMenu();
		}
		// Pause or resume the game
		else if(keycode == Keys.P) {
			paused = !paused;
			Gdx.app.debug(TAG, "Pause pressed!");
		}
		return false;
	}
	
	private void backToMenu() {
		// Switch to menu screen
		ScreenTransition transition = ScreenTransitionSlide.init(0.75f, ScreenTransitionSlide.DOWN, false,
				Interpolation.bounceOut);
		game.setScreen(((GameMain) game).getScreenType(GameMain.ScreenType.Menu), transition);
	}
	
	public boolean isPaused() {
		return paused;
	}

	@Override
	public void dispose() {
		
	}

}
