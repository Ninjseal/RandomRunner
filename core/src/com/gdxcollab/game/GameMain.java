package com.gdxcollab.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;

public class GameMain extends ApplicationAdapter {

	private OrthographicCamera camera;

	private World world;
	private Body player, platform;
	private Box2DDebugRenderer b2dr;
	
	public static final float PPM = 32f;

	@Override
	public void create() {
		float h = Gdx.graphics.getWidth();
		float w = Gdx.graphics.getHeight();
		camera = new OrthographicCamera();
		camera.setToOrtho(false, w / 2, h / 2);
		b2dr = new Box2DDebugRenderer();
		world = new World(new Vector2(0, -9.8f), false);
		player = createPlayer();
		platform = createPlatform();
	}

	@Override
	public void render() {

		update(Gdx.graphics.getDeltaTime());

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		b2dr.render(world, camera.combined.cpy().scl(PPM));

	}

	@Override
	public void resize(int width, int height) {
		camera.setToOrtho(false, width, height);
	}

	@Override
	public void dispose() {
		world.dispose();
		b2dr.dispose();
	}

	public void update(float delta) {
		world.step(1 / 60f, 6, 2);
	}

	public Body createPlayer() {
		Body playerBody;
		BodyDef def = new BodyDef();
		def.type = BodyDef.BodyType.DynamicBody;
		def.position.set(200 / PPM, 300 / PPM);
		def.fixedRotation = true;
		playerBody = world.createBody(def);
		Shape shape = new PolygonShape();
		((PolygonShape) shape).setAsBox(25 / PPM, 25 / PPM);
		playerBody.createFixture(shape, 1.0f);
		shape.dispose();
		return playerBody;
	}
	
	public Body createPlatform() {
		Body platformBody;
		BodyDef bDef = new BodyDef();
		bDef.type = BodyDef.BodyType.StaticBody;
		bDef.position.set(170 / PPM, 50 / PPM);
		platformBody = world.createBody(bDef);
		Shape shape = new PolygonShape();
		((PolygonShape)shape).setAsBox(70 / PPM, 50 / PPM);
		platformBody.createFixture(shape, 1.0f);
		shape.dispose();
		return platformBody;
	}
	
}
