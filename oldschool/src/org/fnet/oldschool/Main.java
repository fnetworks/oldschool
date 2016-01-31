package org.fnet.oldschool;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.fnet.oldschool.entities.Block;
import org.fnet.oldschool.entities.Overlay;
import org.fnet.oldschool.entities.Player;
import org.fnet.oldschool.map.Generator;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class Main {

	private static boolean overlayShown;
	private static boolean shouldRun = true;

	public static void closeOverlay() {
		overlayShown = false;
	}

	public static void close() {
		shouldRun = false;
	}

	public static void extractNatives() throws IOException, URISyntaxException {
		File f = new File(".");
		f.mkdirs();
		File x = new File(f, "natives");
		if (x.exists() && x.isDirectory() && x.listFiles().length == 0)
			FileUtils.copyDirectoryToDirectory(new File(Main.class.getResource("/res/natives/").toURI()), f);
		System.setProperty("org.lwjgl.librarypath", x.getAbsolutePath());
	}

	public static void main(String[] args) {
		try {
			extractNatives();
			Manager.init();
			Player p = new Player(Manager.getPng("sprite"));
			ArrayList<Block> blocks = new ArrayList<>();
			Generator.stringsToSegments(Generator.generateMap(), blocks);
			Manager.getOgg("music").playAsMusic(1, 1, true);
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

				if (overlayShown) {
					Overlay.draw();
				}

				Display.sync(30);
				Display.update();
			}
			Manager.clean();
			AL.destroy();
			Display.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
