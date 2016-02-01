package org.fnet.oldschool.entities;

import java.io.IOException;

import org.fnet.oldschool.Main;
import org.fnet.oldschool.Manager;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.opengl.Texture;

public class Overlay {

	public static void draw() throws IOException {
		Texture overlay = Manager.getPng("overlay");
		Texture close = Manager.getPng("close");
		Texture exit = Manager.getPng("exit");
		Manager.draw(overlay, 0, 0, Manager.DISPLAY_WIDTH, Manager.DISPLAY_HEIGHT);
		Manager.draw(close, Manager.DISPLAY_WIDTH - 35, 5, 30, 30);
		Manager.draw(exit, Manager.DISPLAY_WIDTH - 70, 5, 30, 30);
		if (Mouse.isButtonDown(0)) {
			if (Manager.mouseIsInRangeOf(Manager.DISPLAY_WIDTH - 35, 5, 30, 30)) {
				Main.closeOverlay();
			} else if (Manager.mouseIsInRangeOf(Manager.DISPLAY_WIDTH - 70, 5, 30, 30)) {
				Main.close();
			}
		}
	}

}
