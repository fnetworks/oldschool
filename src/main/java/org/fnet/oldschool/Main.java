package org.fnet.oldschool;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.fnet.oldschool.entities.Block;
import org.fnet.oldschool.entities.Overlay;
import org.fnet.oldschool.entities.Player;
import org.fnet.oldschool.map.Generator;
import org.lwjgl.input.Keyboard;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class Main {

	private static boolean overlayShown = false;
	private static boolean shouldRun = true;
	private static final String[] natives = { "/res/natives/jinput-dx8_64.dll", "/res/natives/jinput-dx8.dll",
			"/res/natives/jinput-raw_64.dll", "/res/natives/jinput-raw.dll", "/res/natives/libjinput-linux.so",
			"/res/natives/libjinput-linux64.so", "/res/natives/libjinput-osx.dylib", "/res/natives/liblwjgl.dylib",
			"/res/natives/liblwjgl.so", "/res/natives/liblwjgl64.so", "/res/natives/libopenal.so",
			"/res/natives/libopenal64.so", "/res/natives/lwjgl.dll", "/res/natives/lwjgl64.dll",
			"/res/natives/openal.dylib", "/res/natives/OpenAL32.dll", "/res/natives/OpenAL64.dll" };

	public static void closeOverlay() {
		overlayShown = false;
	}

	public static void close() {
		shouldRun = false;
	}

	public static void extractNatives() throws FileNotFoundException, IOException, InterruptedException {
		File base = new File(System.getProperty("user.dir"), "natives");
		base.mkdirs();
		for (String s : natives) {
			File f = new File(base, s.split("/")[s.split("/").length - 1]);
			if (!f.exists()) {
				IOUtils.copy(Main.class.getResourceAsStream(s), new FileOutputStream(f));
			}
		}
		System.setProperty("org.lwjgl.librarypath", base.getAbsolutePath());
		Thread.sleep(100);
	}

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				sub();
			}
		});

	}

	private static void sub() {
		try {
			extractNatives();
			Manager.init();
			{
				// Textures Loading
				Manager.getPng("sprite");
				Manager.getPng("block");
				Manager.getPng("exit");
				Manager.getPng("close");
				Manager.getPng("overlay");

				// Sound Loading
				Manager.getOgg("music");
				Manager.getOgg("start");
				Manager.getOgg("jump");
			}
			Manager.setColorToDefault();
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
				Manager.frameCount++;
			}
			Manager.clean();
			AL.destroy();
			Display.destroy();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
