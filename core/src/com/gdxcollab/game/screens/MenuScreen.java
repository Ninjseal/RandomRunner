package com.gdxcollab.game.screens;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.alpha;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.moveBy;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.touchable;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.gdxcollab.game.GameMain;
import com.gdxcollab.game.utils.Assets;
import com.gdxcollab.game.utils.AudioManager;
import com.gdxcollab.game.utils.Constants;
import com.gdxcollab.game.utils.GamePreferences;
import com.hisame.game.screens.transitions.ScreenTransition;
import com.hisame.game.screens.transitions.ScreenTransitionFade;

public class MenuScreen extends AbstractGameScreen {

	private static final String TAG = MenuScreen.class.getName();

	private enum MenuType {
		PLAY, SHOP, OPTIONS, MAIN;
	}

	private Stage stage;
	private Skin skin;
	private Skin skinLibgdx;

	// Menu
	private Image imgBackground;
	private Image imgLogo;
	private Image imgInfo;
	private TextButton btnMenuPlay;
	private TextButton btnMenuShop;
	private TextButton btnMenuOptions;
	private TextButton btnMenuExit;
	private MenuType currentMenu = MenuType.MAIN;

	// Play
	private Window winPlay;
	private List playerProfiles;
	private TextButton player1, player2, player3;
	private TextButton btnWinPlayBack;

	// Shop
	private Window winShop;
	private TextButton btnWinShopBack;
	private Array<Image> imgSkins;

	// Options
	private Window winOptions;
	private TextButton btnWinOptSave;
	private TextButton btnWinOptCancel;
	private CheckBox chkSound;
	private Slider sldSound;
	private CheckBox chkMusic;
	private Slider sldMusic;
	private CheckBox chkShowFpsCounter;

	// Debug
	private final float DEBUG_REBUILD_INTERVAL = 5.0f;
	private boolean debugEnabled = false;
	private float debugRebuildStage;

	public MenuScreen(final AbstractGame game) {
		super(game);
		stage = new Stage();

	}

	@Override
	public void render(float deltaTime) {
		Gdx.gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		if (debugEnabled) {
			debugRebuildStage -= deltaTime;
			if (debugRebuildStage <= 0) {
				debugRebuildStage = DEBUG_REBUILD_INTERVAL;
				rebuildStage();
			}
		}
		stage.act(deltaTime);
		stage.draw();
		// stage.setDebugAll(true);
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void show() {
		stage = new Stage(new StretchViewport(Constants.HUD_WIDTH, Constants.HUD_HEIGHT)); // Physical
																							// Screen
																							// Size
		rebuildStage();
	}

	@Override
	public void hide() {
		stage.dispose();
		// skin.dispose();
		skinLibgdx.dispose();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public InputProcessor getInputProcessor() {
		return stage;
	}

	private void rebuildStage() {
		skinLibgdx = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI),
				new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));

		// Build all layers
		Table layerBackground = buildBackgroundLayer();
		Table layerControls = buildControlsLayer();
		Table layerOptionsWindow = buildOptionsWindowLayer();
		Table layerShopWindow = buildShopWindowLayer();

		// Assemble stage for menu screen
		stage.clear();
		Stack stack = new Stack();
		stage.addActor(stack);
		stack.setSize(Constants.HUD_WIDTH, Constants.HUD_HEIGHT);
		stack.add(layerBackground);
		stack.add(layerControls);
		stage.addActor(layerOptionsWindow);
		stage.addActor(layerShopWindow);
	}

	private Table buildBackgroundLayer() {
		Table layer = new Table();
		// + Background
		imgBackground = new Image(Assets.instance.getTextureAsset(Constants.LOADING_BACKGROUND));
		layer.add(imgBackground);
		return layer;
	}

	private Table buildControlsLayer() {
		Table layer = new Table();
		layer.center();
		// + Play Button
		btnMenuPlay = new TextButton("Play", skinLibgdx);
		layer.add(btnMenuPlay);
		btnMenuPlay.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				currentMenu = MenuType.PLAY;
				onPlayClicked();
			}
		});
		layer.row();
		// + Shop Button
		btnMenuShop = new TextButton("Shop", skinLibgdx);
		layer.add(btnMenuShop);
		btnMenuShop.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				currentMenu = MenuType.SHOP;
				onShopClicked();
			}
		});
		layer.row();
		// + Options Button
		btnMenuOptions = new TextButton("Options", skinLibgdx);
		layer.add(btnMenuOptions);
		btnMenuOptions.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				currentMenu = MenuType.OPTIONS;
				onOptionsClicked();
			}
		});
		layer.row();
		// + Exit Button
		btnMenuExit = new TextButton("Exit", skinLibgdx);
		layer.add(btnMenuExit);
		btnMenuExit.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onExitClicked();
			}
		});
		layer.row();

		return layer;
	}

	private Table buildOptionsWindowLayer() {
		winOptions = new Window("Options", skinLibgdx);
		// + Audio Settings: Sound/Music CheckBox and Volume Slider
		winOptions.add(buildOptWinAudioSettings()).row();
		// + Debug: Show FPS Counter
		winOptions.add(buildOptWinDebug()).row();
		// + Separator and Buttons (Save, Cancel)
		winOptions.add(buildOptWinButtons()).pad(10, 0, 10, 0);

		// Make options window slightly transparent
		winOptions.setColor(1, 1, 1, 0.8f);
		// Hide options window by default
		showMenuWindow(winOptions, false, false);
		if (debugEnabled)
			winOptions.debug();
		// Make the window stay in one place
		winOptions.setMovable(false);
		// Let TableLayout recalculate widget sizes and positions
		winOptions.pack();
		// Move options window to bottom right corner
		winOptions.setPosition(Constants.HUD_WIDTH - winOptions.getWidth() - 50, 50);
		return winOptions;
	}

	private Table buildShopWindowLayer() {
		winShop = new Window("Shop", skinLibgdx);
		winShop.add(buildShopWinStatus()).row();
		winShop.add(buildShopWinButtons()).row();

		winShop.setColor(1, 1, 1, 0.8f);
		showMenuWindow(winShop, false, false);
		if (debugEnabled)
			winShop.debug();

		winShop.setMovable(false);

		winShop.pack();
		winShop.setPosition(Constants.HUD_WIDTH / 2 - winShop.getWidth() / 2,
				Constants.HUD_HEIGHT / 2 - winShop.getHeight());
		return winShop;
	}

	private Table buildOptWinAudioSettings() {
		Table tab = new Table();
		// + Title: "Audio"
		tab.pad(10, 10, 0, 10);
		tab.add(new Label("Audio", skinLibgdx, "default-font", Color.ORANGE)).colspan(3);
		tab.row();
		tab.columnDefaults(0).padRight(10);
		tab.columnDefaults(1).padRight(10);
		// + Checkbox, "Sound" label, sound volume slider
		chkSound = new CheckBox("", skinLibgdx);
		tab.add(chkSound);
		tab.add(new Label("Sound", skinLibgdx));
		sldSound = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
		tab.add(sldSound);
		tab.row();
		// + Checkbox, "Music" label, music volume slider
		chkMusic = new CheckBox("", skinLibgdx);
		tab.add(chkMusic);
		tab.add(new Label("Music", skinLibgdx));
		sldMusic = new Slider(0.0f, 1.0f, 0.1f, false, skinLibgdx);
		tab.add(sldMusic);
		tab.row();
		return tab;
	}

	private Table buildOptWinDebug() {
		Table tab = new Table();
		// + Title: "Debug"
		tab.pad(10, 10, 0, 10);
		tab.add(new Label("Debug", skinLibgdx, "default-font", Color.RED)).colspan(3);
		tab.row();
		tab.columnDefaults(0).padRight(10);
		tab.columnDefaults(1).padRight(10);
		// + Checkbox, "Show FPS Counter" label
		chkShowFpsCounter = new CheckBox("", skinLibgdx);
		tab.add(new Label("Show FPS Counter", skinLibgdx));
		tab.add(chkShowFpsCounter);
		tab.row();
		return tab;
	}

	private Table buildOptWinButtons() {
		Table tab = new Table();
		// + Separator
		Label lbl = null;
		lbl = new Label("", skinLibgdx);
		lbl.setColor(0.75f, 0.75f, 0.75f, 1);
		lbl.setStyle(new LabelStyle(lbl.getStyle()));
		lbl.getStyle().background = skinLibgdx.newDrawable("white");
		tab.add(lbl).colspan(2).height(1).width(220).pad(0, 0, 0, 1);
		tab.row();
		lbl = new Label("", skinLibgdx);
		lbl.setColor(0.5f, 0.5f, 0.5f, 1);
		lbl.setStyle(new LabelStyle(lbl.getStyle()));
		lbl.getStyle().background = skinLibgdx.newDrawable("white");
		tab.add(lbl).colspan(2).height(1).width(220).pad(0, 1, 5, 0);
		tab.row();
		// + Save Button with event handler
		btnWinOptSave = new TextButton("Save", skinLibgdx);
		tab.add(btnWinOptSave).padRight(30);
		btnWinOptSave.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onSaveClicked();
			}
		});
		// + Cancel Button with event handler
		btnWinOptCancel = new TextButton("Cancel", skinLibgdx);
		tab.add(btnWinOptCancel);
		btnWinOptCancel.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onCancelClicked();
			}
		});

		return tab;
	}

	private Table buildShopWinStatus() {
		Table tab = new Table();

		return tab;
	}

	private Table buildShopWinButtons() {
		Table tab = new Table();
		// + Separator
		Label lbl = null;
		lbl = new Label("", skinLibgdx);
		lbl.setColor(0.75f, 0.75f, 0.75f, 1);
		lbl.setStyle(new LabelStyle(lbl.getStyle()));
		lbl.getStyle().background = skinLibgdx.newDrawable("white");
		tab.add(lbl).colspan(2).height(1).width(220).pad(0, 0, 0, 1);
		tab.row();
		lbl = new Label("", skinLibgdx);
		lbl.setColor(0.5f, 0.5f, 0.5f, 1);
		lbl.setStyle(new LabelStyle(lbl.getStyle()));
		lbl.getStyle().background = skinLibgdx.newDrawable("white");
		tab.add(lbl).colspan(2).height(1).width(220).pad(0, 1, 5, 0);
		tab.row();
		// + Back Button with event handler
		btnWinShopBack = new TextButton("Back", skinLibgdx);
		tab.add(btnWinShopBack);
		btnWinShopBack.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				onBackClicked();
			}
		});

		return tab;
	}

	private void loadSettings() {
		GamePreferences prefs = GamePreferences.instance;
		prefs.load();
		chkSound.setChecked(prefs.sound);
		sldSound.setValue(prefs.volSound);
		chkMusic.setChecked(prefs.music);
		sldMusic.setValue(prefs.volMusic);
		chkShowFpsCounter.setChecked(prefs.showFpsCounter);
	}

	private void saveSettings() {
		GamePreferences prefs = GamePreferences.instance;
		prefs.sound = chkSound.isChecked();
		prefs.volSound = sldSound.getValue();
		prefs.music = chkMusic.isChecked();
		prefs.volMusic = sldMusic.getValue();
		prefs.showFpsCounter = chkShowFpsCounter.isChecked();
		prefs.save();
	}

	private void onPlayClicked() {
		ScreenTransition transition = ScreenTransitionFade.init(0.75f);
		game.setScreen(((GameMain) game).getScreenType(GameMain.ScreenType.Play), transition);
	}

	private void onShopClicked() {
		showMenuButtons(false);
		showMenuWindow(winShop, true, true);
	}

	private void onOptionsClicked() {
		loadSettings();
		showMenuButtons(false);
		showMenuWindow(winOptions, true, true);
	}

	private void onSaveClicked() {
		saveSettings();
		onCancelClicked();
		AudioManager.instance.onSettingsUpdated();
	}

	private void onCancelClicked() {
		showMenuButtons(true);
		showMenuWindow(winOptions, false, true);
		AudioManager.instance.onSettingsUpdated();
	}

	private void onBackClicked() {
		showMenuButtons(true);
		switch (currentMenu) {
		case PLAY:
			showMenuWindow(winPlay, false, true);
			break;
		case SHOP:
			showMenuWindow(winShop, false, true);
			break;
		case MAIN:
			onExitClicked();
			break;
		default:
			break;
		}
		currentMenu = MenuType.MAIN;
	}

	private void onExitClicked() {
		Gdx.app.exit();
	}

	private void showMenuButtons(boolean visible) {
		float moveDuration = 1.0f;
		Interpolation moveEasing = Interpolation.smooth2;
		float delayShopButton = 0.15f;
		float delayOptionsButton = 0.30f;
		float delayExitButton = 0.45f;
		float moveX = 0 * (visible ? -1 : 1);
		float moveY = Constants.HUD_HEIGHT * (visible ? -1 : 1);
		final Touchable touchEnabled = visible ? Touchable.enabled : Touchable.disabled;
		btnMenuPlay.addAction(moveBy(moveX, moveY, moveDuration, moveEasing));
		btnMenuShop.addAction(sequence(delay(delayShopButton), 
				moveBy(moveX, moveY, moveDuration, moveEasing)));
		btnMenuOptions.addAction(sequence(delay(delayOptionsButton), 
				moveBy(moveX, moveY, moveDuration, moveEasing)));
		btnMenuExit.addAction(sequence(delay(delayExitButton), 
				moveBy(moveX, moveY, moveDuration, moveEasing)));
		
		SequenceAction seq = sequence();
		if(visible)
			seq.addAction(delay(delayOptionsButton + moveDuration));
		seq.addAction(run(new Runnable() {
			public void run() {
				btnMenuPlay.setTouchable(touchEnabled);
				btnMenuShop.setTouchable(touchEnabled);
				btnMenuOptions.setTouchable(touchEnabled);
				btnMenuExit.setTouchable(touchEnabled);
			}
		}));
		stage.addAction(seq);
	}

	private void showMenuWindow(Window window, boolean visible, boolean animated) {
		float alphaTo = visible ? 0.8f : 0.0f;
		float duration = animated ? 1.0f : 0.0f;
		Touchable touchEnabled = visible ? Touchable.enabled : Touchable.disabled;
		window.addAction(sequence(touchable(touchEnabled), alpha(alphaTo, duration)));
	}

	@Override
	public void dispose() {
		
	}

}
