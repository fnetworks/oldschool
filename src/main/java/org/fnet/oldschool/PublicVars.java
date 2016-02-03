package org.fnet.oldschool;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class PublicVars {

	public static final File TEMP = new File(System.getProperty("java.io.tmpdir"),
			"oldschool_tmp");
	public static final int VERSION_RAW = 03;
	public static final String VERSION = "v0.3 (ALPHA)";
	public static final String[] natives = { "/res/natives/jinput-dx8_64.dll", "/res/natives/jinput-dx8.dll",
			"/res/natives/jinput-raw_64.dll", "/res/natives/jinput-raw.dll", "/res/natives/libjinput-linux.so",
			"/res/natives/libjinput-linux64.so", "/res/natives/libjinput-osx.dylib", "/res/natives/liblwjgl.dylib",
			"/res/natives/liblwjgl.so", "/res/natives/liblwjgl64.so", "/res/natives/libopenal.so",
			"/res/natives/libopenal64.so", "/res/natives/lwjgl.dll", "/res/natives/lwjgl64.dll",
			"/res/natives/openal.dylib", "/res/natives/OpenAL32.dll", "/res/natives/OpenAL64.dll" };

	public static final int DISPLAY_WIDTH = 740, DISPLAY_HEIGHT = 480;

	public static final String UPDATE_PROTOCOL = "https", UPDATE_HOST = "raw.githubusercontent.com",
			UPDATE_FILE_LOCATION = "/wweh/oldschool/master/update/";

	public static final InputStream genUpdateStream(String version) throws IOException {
		return new URL(UPDATE_PROTOCOL, UPDATE_HOST, UPDATE_FILE_LOCATION + version + ".deploy").openStream();
	}

	public static final String[] SEGMENTBUNDLE_UPDOWNSTAIRS = { "3", "stairsup", "stairsdown" };
	public static final String[] SEGMENTBUNDLE_START = { "X", "high", "flat" };
	public static final String[] SEGMENTBUNDLE_END = { "X", "flat", "high" };
	public static final String[] SEGMENTBUNDLE_NORMAL = { "10", "flatdoubleblock", "flat", "flatblock" };

	public static final int MAP_LENGTH = 10;

}
