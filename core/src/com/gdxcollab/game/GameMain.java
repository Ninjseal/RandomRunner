package com.gdxcollab.game;

import com.gdxcollab.game.screens.AbstractGame;
import com.gdxcollab.game.screens.AbstractGameScreen;
import com.gdxcollab.game.screens.LoadingScreen;
import com.gdxcollab.game.screens.MenuScreen;
import com.gdxcollab.game.screens.PlayScreen;
import com.gdxcollab.game.utils.Assets;
import com.gdxcollab.game.utils.GamePreferences;
import com.hisame.game.screens.transitions.ScreenTransition;
import com.hisame.game.screens.transitions.ScreenTransitionFade;

public class GameMain extends AbstractGame {

	private static final String TAG = GameMain.class.getName();
	
	private PlayScreen playScreen;
	private MenuScreen menuScreen;
	private LoadingScreen loadingScreen;
	
	public static enum ScreenType {
		Loading,
		Menu,
		Play;
	}
	
	public AbstractGameScreen getScreenType(ScreenType screenType){
		switch(screenType){
			case Loading:
				return loadingScreen;
			case Menu:
				return menuScreen;
			case Play:
				return playScreen;
			default:
				return menuScreen;
		}
	}
	
	@Override
	public void create() {
		// Init Assets for LoadingScreen at the beginning
		Assets.instance.initLoading();
		
		// Load preferences for audio settings and start playing music
		GamePreferences.instance.load();
		
		loadingScreen = new LoadingScreen(this);
		menuScreen = new MenuScreen(this);
		playScreen = new PlayScreen(this);
		
		ScreenTransition transition = ScreenTransitionFade.init(1.0f);
		
		setScreen(loadingScreen, transition);
	}

	@Override
	public void dispose() {
		super.dispose();
		Assets.instance.dispose();
	}
}
