package org.fnet.oldschool.logging;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.newdawn.slick.util.Log;

public class LogSystem implements org.newdawn.slick.util.LogSystem {

	private static Logger slick;
	
	public static void init() {
		slick = LogManager.getLogger("SLICK");
		Log.setLogSystem(new LogSystem());
		getLogger(LogSystem.class).info("");
		getLogger(LogSystem.class).info("BEGIN OF NEW LOG ON " + new SimpleDateFormat("dd.MM.yyyy / HH:mm:ss").format(new Date()));
	}
	
	public static Logger getLogger(Class<?> cls) {
		return LogManager.getLogger(cls);
	}
	
	@Override
	public void error(String message, Throwable e) {
		slick.error(message, e);
	}

	@Override
	public void error(Throwable e) {
		slick.catching(e);
	}

	@Override
	public void error(String message) {
		slick.error(message);
	}

	@Override
	public void warn(String message) {
		slick.warn(message);
	}

	@Override
	public void warn(String message, Throwable e) {
		slick.warn(message, e);
	}

	@Override
	public void info(String message) {
		slick.info(message);
	}

	@Override
	public void debug(String message) {
		slick.debug(message);
	}

	public static void shutdown() {
		getLogger(LogSystem.class).info("LOG END!");
		getLogger(LogSystem.class).info("");
	}

}
