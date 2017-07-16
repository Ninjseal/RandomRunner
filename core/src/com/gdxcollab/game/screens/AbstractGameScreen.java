package com.gdxcollab.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;

public abstract class AbstractGameScreen implements Screen {

	protected final AbstractGame game;

	public AbstractGameScreen(final AbstractGame game) {
		this.game = game;
	}

	public abstract void render(float deltaTime);

	public abstract void resize(int width, int height);

	public abstract void show();

	public abstract void hide();

	public abstract void pause();

	public abstract void resume();

	public abstract InputProcessor getInputProcessor();

	public abstract void dispose();

}
