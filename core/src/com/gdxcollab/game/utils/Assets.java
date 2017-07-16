package com.gdxcollab.game.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.MusicLoader;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.assets.loaders.TextureAtlasLoader;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.utils.Disposable;

public final class Assets implements Disposable, AssetErrorListener {

	private static final String TAG = Assets.class.getName();

	public static final Assets instance = new Assets();

	private AssetManager assetManager;
	private InternalFileHandleResolver filePathResolver;

	public AssetTextures atlas;
	public AssetFonts font;
	public AssetSounds sound;
	public AssetMusic music;
	
	// Singleton pattern (eager initialization): prevent instantiation from other classes
	private Assets() {
		filePathResolver = new InternalFileHandleResolver();
		assetManager = new AssetManager();
		assetManager.setErrorListener(this);
	}

	// Load assets to begin the LoadingScreen
	public void initLoading() {
		loadTextureAsset(Constants.LOADING_BACKGROUND);
		loadTextureAsset(Constants.LOADING_LOGO);
		loadTextureAsset(Constants.LOADING_BAR);
		loadTextureAsset(Constants.LOADING_BAR_BASE);
		assetManager.finishLoading();
	}

	// This method should be called in LoadingScreen for asynchronous loading of assets
	public void loadAssets() {
		assetManager.load(Constants.TEXTURE_ATLAS, TextureAtlas.class);
		// Load sounds
		assetManager.load(Constants.SOUND_JUMP, Sound.class);
		assetManager.load(Constants.SOUND_PICKUP_COIN, Sound.class);
		assetManager.load(Constants.SOUND_PICKUP_ITEM, Sound.class);
		assetManager.load(Constants.SOUND_LIFE_LOST, Sound.class);
		// Load music
		assetManager.load(Constants.MUSIC_BACKGROUND, Music.class);
	}

	// This method should be called in LoadingScreen after all the assets have been loaded
	public void storeAssets() {
		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames())
			Gdx.app.debug(TAG, "asset: " + a);

		TextureAtlas textureAtlas = assetManager.get(Constants.TEXTURE_ATLAS);

		// Enable texture filtering for pixel smoothing
		for (Texture t : textureAtlas.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}

		atlas = new AssetTextures(textureAtlas);
		font = new AssetFonts();
		sound = new AssetSounds(assetManager);
		music = new AssetMusic(assetManager);
	}

	public void init(AssetManager assetManager) {
		this.assetManager = assetManager;
		// Set asset manager error handler
		assetManager.setErrorListener(this);
		initLoading(); // -> Call the LoadingScreen()

		// Maybe crop code from here and let the LoadingScreen do the work
		assetManager.load(Constants.TEXTURE_ATLAS, TextureAtlas.class);
		// Load sounds
		assetManager.load(Constants.SOUND_JUMP, Sound.class);
		assetManager.load(Constants.SOUND_PICKUP_COIN, Sound.class);
		assetManager.load(Constants.SOUND_PICKUP_ITEM, Sound.class);
		assetManager.load(Constants.SOUND_LIFE_LOST, Sound.class);
		// Load music
		assetManager.load(Constants.MUSIC_BACKGROUND, Music.class);
		assetManager.finishLoading();

		Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
		for (String a : assetManager.getAssetNames())
			Gdx.app.debug(TAG, "asset: " + a);

		TextureAtlas textureAtlas = assetManager.get(Constants.TEXTURE_ATLAS);

		// Enable texture filtering for pixel smoothing
		for (Texture t : textureAtlas.getTextures()) {
			t.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}

		atlas = new AssetTextures(textureAtlas);
		font = new AssetFonts();
		sound = new AssetSounds(assetManager);
		music = new AssetMusic(assetManager);
	}

	@Override
	public void error(AssetDescriptor asset, Throwable throwable) {
		Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", (Exception) throwable);
	}

	// Determine whether the asset is loaded or not
	public boolean isAssetLoaded(String fileName) {
		return assetManager.isLoaded(fileName);
	}

	// Are the assets done loading
	public boolean loadCompleted() {
		return assetManager.update();
	}

	// Blocks until all queued assets are loaded
	public void finishLoading() {
		assetManager.finishLoading();
	}

	// Get the progress in percent of completion
	public float getLoadingProgress() {
		return assetManager.getProgress();
	}

	// Manually unload assets using their paths
	public void unloadAsset(String fileName) {
		if (assetManager.isLoaded(fileName)) {
			assetManager.unload(fileName);
		} else {
			Gdx.app.debug(TAG, "Couldn't unload asset: " + fileName + "... Nothing to unload!");
		}
	}

	// This method handles the manual loading of sound assets
	public void loadSoundAsset(String soundFilePath) {
		if (soundFilePath == null || soundFilePath.isEmpty())
			return;

		// if it's already loaded return
		if (assetManager.isLoaded(soundFilePath)) {
			return;
		}

		// load asset
		if (filePathResolver.resolve(soundFilePath).exists()) {
			assetManager.setLoader(Sound.class, new SoundLoader(filePathResolver));
			assetManager.load(soundFilePath, Sound.class);

			Gdx.app.debug(TAG, "Sound loaded!: " + soundFilePath);
		} else {
			Gdx.app.debug(TAG, "Sound doesn't exist!: " + soundFilePath);
		}
	}

	// Getter method for sound assets
	public Sound getSoundAsset(String soundFilePath) {
		Sound sound = null;

		if (assetManager.isLoaded(soundFilePath)) {
			sound = assetManager.get(soundFilePath, Sound.class);
		} else {
			Gdx.app.debug(TAG, "Sound is not loaded: " + soundFilePath);
		}

		return sound;
	}

	// This method handles the manual loading of music assets
	public void loadMusicAsset(String musicFilePath) {
		if (musicFilePath == null || musicFilePath.isEmpty()) {
			return;
		}

		// if it's already loaded return
		if (assetManager.isLoaded(musicFilePath)) {
			return;
		}

		// load asset
		if (filePathResolver.resolve(musicFilePath).exists()) {
			assetManager.setLoader(Music.class, new MusicLoader(filePathResolver));
			assetManager.load(musicFilePath, Music.class);

			Gdx.app.debug(TAG, "Music loaded!: " + musicFilePath);
		} else {
			Gdx.app.debug(TAG, "Music doesn't exist!: " + musicFilePath);
		}
	}

	// Getter method for music assets
	public Music getMusicAsset(String musicFilePath) {
		Music music = null;

		if (assetManager.isLoaded(musicFilePath)) {
			music = assetManager.get(musicFilePath, Music.class);
		} else {
			Gdx.app.debug(TAG, "Music is not loaded: " + musicFilePath);
		}

		return music;
	}

	// This method handles the manual loading of texture assets
	public void loadTextureAsset(String textureFilePath) {
		if (textureFilePath == null || textureFilePath.isEmpty())
			return;

		// if it's already loaded return
		if (assetManager.isLoaded(textureFilePath))
			return;

		// load asset
		if (filePathResolver.resolve(textureFilePath).exists()) {
			assetManager.setLoader(Texture.class, new TextureLoader(filePathResolver));
			assetManager.load(textureFilePath, Texture.class);

			Gdx.app.debug(TAG, "Loaded texture" + textureFilePath + " successfully!");
		} else {
			Gdx.app.debug(TAG, "Texture doesn't exist!: " + textureFilePath);
		}
	}

	// Getter method for texture assets
	public Texture getTextureAsset(String textureFilePath) {
		Texture texture = null;

		if (assetManager.isLoaded(textureFilePath)) {
			texture = assetManager.get(textureFilePath, Texture.class);
		} else {
			Gdx.app.debug(TAG, "Texture is not loaded: " + textureFilePath);
		}

		return texture;
	}

	// This method handles the manual loading of TextureAtlas assets
	public void loadTextureAtlasAsset(String textureFilePath) {
		if (textureFilePath == null || textureFilePath.isEmpty())
			return;

		// if it's already loaded return
		if (assetManager.isLoaded(textureFilePath))
			return;

		// load asset
		if (filePathResolver.resolve(textureFilePath).exists()) {
			assetManager.setLoader(TextureAtlas.class, new TextureAtlasLoader(filePathResolver));
			assetManager.load(textureFilePath, TextureAtlas.class);

			Gdx.app.debug(TAG, "Loaded TextureAtlas " + textureFilePath + " successfully!");
		} else {
			Gdx.app.debug(TAG, "TextureAtlas doesn't exist!: " + textureFilePath);
		}
	}

	// Getter method for texture assets
	public TextureAtlas getTextureAtlasAsset(String textureFilePath) {
		TextureAtlas texture = null;

		if (assetManager.isLoaded(textureFilePath)) {
			texture = assetManager.get(textureFilePath, TextureAtlas.class);
		} else {
			Gdx.app.debug(TAG, "TextureAtlas is not loaded: " + textureFilePath);
		}

		return texture;
	}

	@Override
	public void dispose() {
		if (assetManager != null)
			assetManager.dispose();
		Gdx.app.debug(TAG, "Disposed assetManager");
		font.defaultSmall.dispose();
		font.defaultNormal.dispose();
		font.defaultBig.dispose();
	}

	public class AssetTextures {
		public final AtlasRegion player;
		
		public AssetTextures(TextureAtlas atlas) {
			player = atlas.findRegion("erwin");
			// TODO.....
		}
	}
	
	public class AssetFonts {
		public final BitmapFont defaultSmall;
		public final BitmapFont defaultNormal;
		public final BitmapFont defaultBig;
		
		public AssetFonts() {
			// Create three fonts using Libgdx's 15px bitmap font
			defaultSmall = new BitmapFont(Gdx.files.internal("fonts/arial-15.fnt"), true);
			defaultNormal = new BitmapFont(Gdx.files.internal("fonts/arial-15.fnt"), true);
			defaultBig = new BitmapFont(Gdx.files.internal("fonts/arial-15.fnt"), true);
			// Set font sizes
			defaultSmall.getData().setScale(0.75f);
			defaultNormal.getData().setScale(1.0f);
			defaultBig.getData().setScale(2.0f);
			// Enable linear texture filtering for smooth fonts
			defaultSmall.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultNormal.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
			defaultBig.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		}
	}
	
	public class AssetSounds {
		public final Sound jump;
		public final Sound pickupCoin;
		public final Sound pickupItem;
		public final Sound lifeLost;
		
		public AssetSounds(AssetManager am) {
			jump = am.get(Constants.SOUND_JUMP, Sound.class);
			pickupCoin = am.get(Constants.SOUND_PICKUP_COIN, Sound.class);
			pickupItem = am.get(Constants.SOUND_PICKUP_ITEM, Sound.class);
			lifeLost = am.get(Constants.SOUND_LIFE_LOST, Sound.class);
		}
	}
	
	public class AssetMusic {
		public final Music bgm;
		
		public AssetMusic(AssetManager am) {
			bgm = am.get(Constants.MUSIC_BACKGROUND, Music.class);
		}
	}
	
}
