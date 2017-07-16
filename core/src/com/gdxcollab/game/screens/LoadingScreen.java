package com.gdxcollab.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.gdxcollab.game.GameMain;
import com.gdxcollab.game.utils.Assets;
import com.gdxcollab.game.utils.Constants;
import com.hisame.game.screens.transitions.ScreenTransition;
import com.hisame.game.screens.transitions.ScreenTransitionSlide;

public class LoadingScreen extends AbstractGameScreen {

	private static final String TAG = LoadingScreen.class.getSimpleName();

	private Texture background, logo, progressBarImg, progressBarBaseImg;

	private Vector2 logoPos, pbPos;

	private boolean loading = false;
	private boolean loadBar = false;

	public LoadingScreen(final AbstractGame game) {
		super(game);
	}

	@Override
	public void render(float deltaTime) {
		if(Assets.instance.loadCompleted() && loading) {
			Assets.instance.storeAssets();
			loading = false;
			Gdx.app.debug(TAG, "Done loading!");
			ScreenTransition transition = 
					ScreenTransitionSlide.init(0.75f, ScreenTransitionSlide.DOWN, false, Interpolation.pow2InInverse);
			game.setScreen(((GameMain)game).getScreenType(GameMain.ScreenType.Menu), transition);
		}
		
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		
		// Render background image
		game.batch.begin();
		game.batch.draw(background, 0, 0);
		//_game.batch.draw(logo, logoPos.x, logoPos.y);
		game.batch.draw(progressBarBaseImg, pbPos.x, pbPos.y);
		game.batch.draw(progressBarImg, pbPos.x, pbPos.y, 
				progressBarImg.getWidth() * ((loadBar)?Assets.instance.getLoadingProgress() : 0), progressBarImg.getHeight());
		game.batch.end();
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void show() {
		background = Assets.instance.getTextureAsset(Constants.LOADING_BACKGROUND);
		logo = Assets.instance.getTextureAsset(Constants.LOADING_LOGO);
		progressBarImg = Assets.instance.getTextureAsset(Constants.LOADING_BAR);
		progressBarBaseImg = Assets.instance.getTextureAsset(Constants.LOADING_BAR_BASE);
		// Logo position
		logoPos = new Vector2();
		logoPos.set((Gdx.graphics.getWidth() - logo.getWidth())>>1, Gdx.graphics.getHeight()>>1);
		// ProgressBar position
		pbPos = new Vector2();
		pbPos.set(logoPos.x, logoPos.y - (logo.getHeight()));
	}

	@Override
	public void hide() {
		loadBar = false;
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {
		Assets.instance.loadAssets();
		loading = true;
		loadBar = true;
	}

	@Override
	public InputProcessor getInputProcessor() {
		return null;
	}

	@Override
	public void dispose() {
		
	}

}
