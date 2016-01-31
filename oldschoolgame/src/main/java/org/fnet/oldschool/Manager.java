package org.fnet.oldschool;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glViewport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.fnet.oldschool.entities.Block;
import org.fnet.oldschool.entities.Player;
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Manager {

	private static final HashMap<String, Texture> textures = new HashMap<>();
	private static final HashMap<String, Audio> sounds = new HashMap<>();
	private static final ArrayList<Block> blocklist = new ArrayList<>();

	public static final int DISPLAY_WIDTH = 740, DISPLAY_HEIGHT = 480;

	public static boolean mouseIsInRangeOf(final float x, final float y, final float width, final float height) {
		return (x < Mouse.getX() && Mouse.getX() < x + width)
				&& (y < DISPLAY_HEIGHT - Mouse.getY() && DISPLAY_HEIGHT - Mouse.getY() < y + height);
	}

	public static void init() throws LWJGLException {
		Display.setDisplayMode(new DisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT));
		Display.create();
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glShadeModel(GL_SMOOTH);
		glOrtho(0, DISPLAY_WIDTH, DISPLAY_HEIGHT, 0, 1, -1);
		glViewport(0, 0, DISPLAY_WIDTH, DISPLAY_HEIGHT);
		glEnable(GL_TEXTURE_2D);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_LIGHTING);
		glDisable(GL_DEPTH_TEST);
		glClearDepth(1);
		glClearColor(63.0f / 255.0f, 165.0f / 255.0f, 255.0f / 255.0f, 1.0f);
		glMatrixMode(GL_MODELVIEW);
		Logics.update();
	}

	public static void processBlock(Block... b) {
		blocklist.addAll(Arrays.asList(b));
	}

	public static void processBlock(List<Block> b) {
		processBlock(b.toArray(new Block[b.size()]));
	}

	public static void update() {
		for (Block b : blocklist) {
			b.draw();
		}
		blocklist.clear();
	}

	public static Texture getPng(String name) throws IOException {
		if (!textures.containsKey(name)) {
			textures.put(name, TextureLoader.getTexture("PNG",
					ResourceLoader.getResourceAsStream("res/textures/" + name + ".png")));
		}
		return textures.get(name);
	}

	public static Audio getOgg(String name) throws IOException {
		if (!sounds.containsKey(name)) {
			sounds.put(name,
					AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream("res/sounds/" + name + ".ogg")));
		}
		return sounds.get(name);
	}

	public static void clean() {
		System.out.print("Cleaning Textures ... ");
		int tc = 0;
		for (Texture t : textures.values()) {
			tc++;
			t.release();
		}
		System.out.print(tc + " OK, finalizing ... ");
		textures.clear();
		System.out.println("OK");

		for (Audio a : sounds.values()) {
			a.stop();
		}
	}

	public static void draw(Texture t, float x, float y, float width, float height) {
		t.bind();
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(x, y);
		glTexCoord2f(1, 0);
		glVertex2f(x + width, y);
		glTexCoord2f(1, 1);
		glVertex2f(x + width, y + height);
		glTexCoord2f(0, 1);
		glVertex2f(x, y + height);
		glEnd();
	}

	public static boolean hitsGround(Player p) {
		for (Block b : blocklist) {
			float playerXMidPoint = p.getX() + p.getTexture().getTextureWidth() / 2;
			if (!(playerXMidPoint > b.getX() && playerXMidPoint < b.getX() + b.getTexture().getTextureWidth() / 2
					|| p.getX() + 20 > b.getX() && p.getX() + 20 < b.getX() + b.getTexture().getTextureWidth() / 2
					|| p.getX() + p.getTexture().getTextureWidth() - 20 > b.getX() && p.getX()
							+ p.getTexture().getTextureWidth() - 20 < b.getX() + b.getTexture().getTextureWidth()))
				continue;
			float playerDeepY = p.getY() + p.getTexture().getTextureHeight();
			if (playerDeepY > b.getY() + 1) {
				p.setY(b.getY() - p.getTexture().getTextureHeight() + 1.6f);
				return true;
			}
		}
		return false;
	}

	public static boolean touchesBlockRight(Player p) {
		for (Block b : blocklist) {
			float playerHighY = p.getY();
			float playerDeepY = p.getY() + p.getTexture().getTextureHeight() - 1.6f;

			float blockHighY = b.getY();
			float blockDeepY = b.getY() + b.getTexture().getTextureHeight();

			if ((blockDeepY > playerDeepY && blockHighY < playerDeepY)
					|| (blockDeepY > playerHighY && blockHighY < playerHighY)) {
				if (p.getX() + p.getTexture().getTextureWidth() >= b.getX()
						&& p.getX() + p.getTexture().getTextureWidth() < b.getX() + 10) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean touchesBlockLeft(Player p) {
		for (Block b : blocklist) {
			float playerHighY = p.getY();
			float playerDeepY = p.getY() + p.getTexture().getTextureHeight() - 1.6f;

			float blockHighY = b.getY();
			float blockDeepY = b.getY() + b.getTexture().getTextureHeight();

			if ((blockDeepY > playerDeepY && blockHighY < playerDeepY)
					|| (blockDeepY > playerHighY && blockHighY < playerHighY)) {
				if (p.getX() <= b.getX() + (b.getTexture().getTextureWidth() / 2)
						&& p.getX() > b.getX() + (b.getTexture().getTextureWidth() / 2) - 10) {
					return true;
				}
			}
		}
		return false;
	}

	public static void moveAllX(float x) {
		for (Block b : blocklist) {
			b.setX(b.getX() + x);
		}
	}

	public static void resetAllX() {
		for (Block b : blocklist) {
			b.setX(b.firstX);
		}
	}

}