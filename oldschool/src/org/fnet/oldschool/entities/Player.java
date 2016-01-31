package org.fnet.oldschool.entities;

import java.io.IOException;

import org.fnet.oldschool.Logics;
import org.fnet.oldschool.Manager;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;

public class Player {

	private final Texture texture;
	private static final float GRAVITY = 0.04F, JUMP_POWER = 0.47f;
	private float x, y, dx, dy;

	public Player(final Texture texture) throws IOException {
		this.texture = texture;
		this.respawn();
	}
	
	public void respawn() throws IOException {
		this.setX(Manager.DISPLAY_HEIGHT / 2);
		this.setY(0);
		this.setDx(0);
		this.setDy(0);
		Manager.resetAllX();
		Manager.getOgg("start").playAsSoundEffect(1, 1, false);
	}

	public float getX() {
		return this.x;
	}

	public float getY() {
		return this.y;
	}

	public void setX(float x) {
		this.x = x;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Texture getTexture() {
		return this.texture;
	}

	public void draw() {
		Manager.draw(this.getTexture(), this.getX(), this.getY(), this.getTexture().getTextureWidth(),
				this.getTexture().getTextureHeight());
	}

	public void update() throws IOException {
		
		float blockdx = 0;
		
		if (this.y > Manager.DISPLAY_HEIGHT) {
			this.respawn();
		}
		if (!Manager.hitsGround(this)) {
			this.dy += GRAVITY;
		} else {
			this.dy = 0;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
			this.jump();
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) && !Manager.touchesBlockRight(this)) {
			blockdx = -0.2f;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) && !Manager.touchesBlockLeft(this)) {
			blockdx = 0.2f;
		} else {
			if (Manager.hitsGround(this))
				this.dx = 0;
		}
		
		Manager.moveAllX(blockdx * Logics.delta());

		this.x += this.dx * Logics.delta();
		this.y += this.dy * Logics.delta();
	}

	private void jump() throws IOException {
		if (Manager.hitsGround(this)) {
			this.dy -= JUMP_POWER;
			Manager.getOgg("jump").playAsSoundEffect(1, 90, false, this.getX() - 740 / 2, this.getY(), 1);
		}
	}

	public float getDx() {
		return dx;
	}

	public void setDx(float dx) {
		this.dx = dx;
	}

	public float getDy() {
		return dy;
	}

	public void setDy(float dy) {
		this.dy = dy;
	}

}
