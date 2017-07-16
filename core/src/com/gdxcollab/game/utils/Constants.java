package com.gdxcollab.game.utils;

public final class Constants {

	private Constants() {
	}

	// Pixel per meter scale for BOX2D
	public static final float PPM = 32.0f;

	// Visible game world is 5 meters wide
	public static final float VIEWPORT_WIDTH = 30.0f;

	// Visible game world is 5 meters tall
	public static final float VIEWPORT_HEIGHT = 25.0f;

	// HUD width in actual screen size(placeholder)
	public static final float HUD_WIDTH = 800.0f;

	// HUD height in actual screen size(placeholder)
	public static final float HUD_HEIGHT = 480.0f;

	// Location of preferences file
	public static final String PREFERENCES = "game_prefs";

	// LoadingScreen assets
	public static final String LOADING_BACKGROUND = "loading_screen/background.png";
	public static final String LOADING_LOGO = "loading_screen/logo.png";
	public static final String LOADING_BAR = "loading_screen/progress_bar.png";
	public static final String LOADING_BAR_BASE = "loading_screen/progress_bar_base.png";

	// Location of description file for skins
	public static final String SKIN_LIBGDX_UI = "ui/uiskin.json";
	public static final String SKIN_GAME_UI = "ui/randomrunner_ui";

	// Location of description file for texture atlas
	public static final String TEXTURE_ATLAS = "images/atlas.pack";
	public static final String TEXTURE_ATLAS_UI = "ui/ui.pack";
	public static final String TEXTURE_ATLAS_LIBGDX_UI = "ui/uiskin.atlas";

	// Location of sound files
	public static final String SOUND_JUMP = "sounds/jump.wav";
	public static final String SOUND_PICKUP_ITEM = "sounds/pickup_coin.wav";
	public static final String SOUND_PICKUP_COIN = "sounds/pickup_feather.wav";
	public static final String SOUND_LIFE_LOST = "sounds/live_lost.wav";

	// Location of music files
	public static final String MUSIC_BACKGROUND = "music/keith303_-_brand_new_highscore.mp3";

}
