package org.fnet.oldschool.entities;

import org.fnet.oldschool.Manager;
import org.newdawn.slick.opengl.Texture;

public class Block {

	private final Texture texture;
	private float x, y;
	public final float firstX;

	public Block(Texture texture, float x, float y) {
		this.texture = texture;
		this.x = x;
		this.y = y;
		this.firstX = x;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public Texture getTexture() {
		return texture;
	}

	public void draw() {
		Manager.draw(this.getTexture(), this.getX(), this.getY(), this.getTexture().getTextureWidth() / 2,
				this.getTexture().getTextureHeight() / 2);
	}

}
