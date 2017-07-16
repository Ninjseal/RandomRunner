package com.gdxcollab.game.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class GameObject {

	protected boolean toDestroy;
	protected boolean destroyed;

	protected Vector2 position;
	protected Vector2 dimension;
	protected Vector2 origin;
	protected Vector2 scale;
	protected Vector2 velocity;
	protected Vector2 terminalVelocity;
	protected Vector2 friction;
	protected Vector2 acceleration;
	protected float rotation;
	protected Rectangle bounds;

	protected float stateTime;
	protected Animation<TextureRegion> animation;

	public GameObject() {
		toDestroy = false;
		destroyed = false;
		position = new Vector2();
		dimension = new Vector2(1, 1);
		origin = new Vector2();
		scale = new Vector2(1, 1);
		rotation = 0;
		velocity = new Vector2();
		terminalVelocity = new Vector2(1, 1);
		friction = new Vector2();
		acceleration = new Vector2();
		bounds = new Rectangle();
	}

	public GameObject(float x, float y) {
		toDestroy = false;
		destroyed = false;
		position = new Vector2();
		dimension = new Vector2(1, 1);
		origin = new Vector2();
		scale = new Vector2(1, 1);
		rotation = 0;
		velocity = new Vector2();
		terminalVelocity = new Vector2(1, 1);
		friction = new Vector2();
		acceleration = new Vector2();
		bounds = new Rectangle();
		setPosition(new Vector2(x, y));
	}

	protected void updateMotionX(float deltaTime) {
		if (velocity.x != 0) {
			// Apply friction
			if (velocity.x > 0) {
				velocity.x = Math.max(velocity.x - friction.x * deltaTime, 0);
			} else {
				velocity.x = Math.min(velocity.x + friction.x * deltaTime, 0);
			}
		}
		// Apply acceleration
		velocity.x += acceleration.x * deltaTime;
		// Make sure the object's velocity does not exceed the positive or
		// negative terminal velocity
		velocity.x = MathUtils.clamp(velocity.x, -terminalVelocity.x, terminalVelocity.x);
	}

	protected void updateMotionY(float deltaTime) {
		if (velocity.y != 0) {
			// Apply friction
			if (velocity.y > 0) {
				velocity.y = Math.max(velocity.y - friction.y * deltaTime, 0);
			} else {
				velocity.y = Math.min(velocity.y + friction.y * deltaTime, 0);
			}
		}
		// Apply acceleration
		velocity.y += acceleration.y * deltaTime;
		// Make sure the object's velocity does not exceed the positive or
		// negative terminal velocity
		velocity.y = MathUtils.clamp(velocity.y, -terminalVelocity.y, terminalVelocity.y);
	}

	public void update(float deltaTime) {
		stateTime += deltaTime;
		updateMotionX(deltaTime);
		updateMotionY(deltaTime);
		// Move to new position
		position.x += velocity.x * deltaTime;
		position.y += velocity.y * deltaTime;
	}

	public abstract void render(SpriteBatch batch);

	public void setAnimation(Animation<TextureRegion> animation) {
		this.animation = animation;
		stateTime = 0;
	}

	public void setScale(Vector2 scale) {
		this.scale.set(scale);
	}

	public void setScaleX(float scaleX) {
		this.scale.x = scaleX;
	}

	public void setScaleY(float scaleY) {
		this.scale.y = scaleY;
	}

	public void setAcceleration(Vector2 acceleration) {
		this.acceleration.set(acceleration);
	}

	public void setAccelerationX(float accelerationX) {
		this.acceleration.x = accelerationX;
	}

	public void setAccelerationY(float accelerationY) {
		this.acceleration.y = accelerationY;
	}

	public void setFriction(Vector2 friction) {
		this.friction.set(friction);
	}

	public void setFrictionX(float frictionX) {
		this.friction.x = frictionX;
	}

	public void setFrictionY(float frictionY) {
		this.friction.y = frictionY;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity.set(velocity);
	}

	public void setVelocityX(float velocityX) {
		this.velocity.x = velocityX;
	}

	public void setVelocityY(float velocityY) {
		this.velocity.y = velocityY;
	}

	public void setTerminalVelocity(Vector2 terminalVelocity) {
		this.terminalVelocity.set(terminalVelocity);
	}

	public void setTerminalVelocityX(float terminalVelocityX) {
		this.terminalVelocity.x = terminalVelocityX;
	}

	public void setTerminalVelocityY(float terminalVelocityY) {
		this.terminalVelocity.y = terminalVelocityY;
	}

	public void setPosition(Vector2 position) {
		this.position.set(position);
	}

	public void setPositionX(float positionX) {
		this.position.x = positionX;
	}

	public void setPositionY(float positionY) {
		this.position.y = positionY;
	}

	public void setRotation(float rotation) {
		this.rotation = rotation;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds.set(bounds);
	}

	public void setBounds(float x, float y, float width, float height) {
		this.bounds.set(x, y, width, height);
	}

	public Vector2 getPosition() {
		return this.position;
	}

	public float getX() {
		return this.position.x;
	}

	public float getY() {
		return this.position.y;
	}

	public Vector2 getVelocity() {
		return this.velocity;
	}

	public Vector2 getAcceleration() {
		return this.acceleration;
	}

	public Vector2 getFriction() {
		return this.friction;
	}

	public Vector2 getTerminalVelocity() {
		return this.terminalVelocity;
	}

	public float getRotation() {
		return this.rotation;
	}

	public Rectangle getBounds() {
		return this.bounds;
	}

	public void setToDestroy() {
		toDestroy = true;
	}

	public boolean isDestroyed() {
		return this.destroyed;
	}

}
