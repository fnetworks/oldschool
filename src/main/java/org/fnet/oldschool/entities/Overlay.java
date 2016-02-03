package org.fnet.oldschool.entities;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

import org.fnet.oldschool.Logics;
import org.fnet.oldschool.Main;
import org.fnet.oldschool.Manager;
import org.fnet.oldschool.PublicVars;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.ResourceLoader;

public class Overlay {

	private static boolean soundEnabled = Boolean.parseBoolean(System.getProperty("os.music", String.valueOf(true))),
			mouseClicked = false;
	public static int score = 0, highscore = score, round = 0;
	public static long lasttime = 0, besttime = 0;

	public static void drawDefault() throws IOException {
		font.drawString(10, 10, "SCORE     : " + score);
		font.drawString(10, 40, "HIGHSCORE : " + highscore);
		font.drawString(200, 10, "TIME : " + (Logics.getTime() / 1000 - lasttime) + 's');
		font.drawString(200, 40, "BEST : " + besttime + 's');
		font.drawString(350, 10, "ROUND : " + round);
	}

	private static TrueTypeFont font;

	public static void newRound() {
		final long current = Logics.getTime() / 1000;
		if (round == 1) {
			besttime = current - lasttime;
		} else {
			besttime = Math.min(besttime, current - lasttime);
		}
		lasttime = current;
		round++;
	}

	public static void init() throws FontFormatException, IOException {
		font = new TrueTypeFont(
				Font.createFont(Font.TRUETYPE_FONT, ResourceLoader.getResourceAsStream("res/fonts/data-latin.ttf"))
						.deriveFont(24f),
				false);
	}

	public static void drawPauseMenu() throws IOException {
		Texture overlay = Manager.getPng("overlay");
		Texture close = Manager.getPng("close");
		Texture exit = Manager.getPng("exit");
		Texture sound_mute = Manager.getPng("sound_mute");
		Texture sound_unmute = Manager.getPng("sound_unmute");
		Manager.draw(overlay, 0, 0, PublicVars.DISPLAY_WIDTH, PublicVars.DISPLAY_HEIGHT);
		Manager.draw(close, PublicVars.DISPLAY_WIDTH - 35, 5, 30, 30);
		Manager.draw(exit, PublicVars.DISPLAY_WIDTH - 70, 5, 30, 30);
		if (!soundEnabled) {
			Manager.draw(sound_mute, PublicVars.DISPLAY_WIDTH - 105, 5, 30, 30);
		} else {
			Manager.draw(sound_unmute, PublicVars.DISPLAY_WIDTH - 105, 5, 30, 30);
		}
		if (Mouse.isButtonDown(0)) {
			if (Manager.mouseIsInRangeOf(PublicVars.DISPLAY_WIDTH - 35, 5, 30, 30)) {
				Main.closeOverlay();
			} else if (Manager.mouseIsInRangeOf(PublicVars.DISPLAY_WIDTH - 70, 5, 30, 30)) {
				Main.close();
			} else if (Manager.mouseIsInRangeOf(PublicVars.DISPLAY_WIDTH - 105, 5, 30, 30)) {
				if (!mouseClicked) {
					mouseClicked = true;
					if (soundEnabled) {
						Manager.getOgg("music").stop();
						soundEnabled = false;
					} else {
						Manager.getOgg("music").playAsMusic(1, 1, true);
						soundEnabled = true;
					}
				}
			}
		} else {
			mouseClicked = false;
		}
	}

}
