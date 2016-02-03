package org.fnet.oldschool;

import java.util.ArrayList;

import org.fnet.oldschool.entities.Block;
import org.fnet.oldschool.entities.Overlay;
import org.fnet.oldschool.entities.Player;
import org.fnet.oldschool.logging.LogSystem;
import org.fnet.oldschool.map.Generator;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class Main {

	private static boolean overlayShown = false, shouldRun = true;

	public static void closeOverlay() {
		overlayShown = false;
	}

	public static void close() {
		shouldRun = false;
	}


	public static void main(String[] args) {
		try {
			Thread t = new Thread(() -> {
				sub();
			}, "Game Thread");
			t.start();
		} catch (Exception e) {
			System.err.println("Please Help!");
			e.printStackTrace();
		}

	}

	private static void sub() {
		LogSystem.init();
		try {
			PreGame.extractNatives();
			Manager.init();
			{
				// Textures Loading
				Manager.getPng("sprite");
				Manager.getPng("block");
				Manager.getPng("exit");
				Manager.getPng("close");
				Manager.getPng("overlay");
				Manager.getPng("sound_mute");
				Manager.getPng("sound_unmute");

				// Sound Loading
				Manager.getOgg("music");
				Manager.getOgg("start");
				Manager.getOgg("jump");

				LogSystem.getLogger(Manager.class).info("Loading Font ...");
				// Font Loading
				Overlay.init();
				LogSystem.getLogger(Manager.class).info("Font loading OK.");
			}
			PreGame.getUpdate();
			Manager.setColorToDefault();
			Player p = new Player(Manager.getPng("sprite"));
			ArrayList<Block> blocks = new ArrayList<>();
			Generator.stringsToSegments(Generator.generateMap(), blocks);
			Manager.getOgg("music").playAsMusic(1, 1, true);
			Overlay.newRound();
			LogSystem.getLogger(Main.class).info("Loading Complete!");
			while (shouldRun) {
				if (Display.isCloseRequested()) {
					close();
				}
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
				Logics.update();

				Manager.processBlock(blocks);
				p.update();
				p.draw();

				Manager.update();

				if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
					overlayShown = true;
				}

				if (p.getX() > blocks.get(blocks.size() - 1).getX() - 200) {
					blocks.clear();
					Generator.stringsToSegments(Generator.generateMap(), blocks);
					Overlay.score++;
					Overlay.highscore = Math.max(Overlay.highscore, Overlay.score);
					Overlay.newRound();
					p.respawn();
				}

				if (overlayShown) {
					Overlay.drawPauseMenu();
					p.keyInputs = false;
				} else {
					p.keyInputs = true;
				}
				Overlay.drawDefault();

				Display.sync(40);
				Display.update();
				Manager.frameCount++;
			}
			Manager.clean();
			AL.destroy();
			Display.destroy();
		} catch (Throwable t) {
			LogSystem.getLogger(Main.class).fatal("An Error got to the TOP-LEVEL of this program.", t);
		}
		LogSystem.shutdown();
	}

}
