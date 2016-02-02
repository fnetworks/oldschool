package org.fnet.oldschool;

import org.lwjgl.Sys;

public class Logics {

	private static long lastFrame;
	private static int lastDelta;

	public static long getTime() {
		return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}

	public static void update() {
		long time = getTime();
		int delta = (int) (time - lastFrame);
		lastFrame = time;
		lastDelta = delta;
	}

	public static int delta() {
		return lastDelta;
	}

}
