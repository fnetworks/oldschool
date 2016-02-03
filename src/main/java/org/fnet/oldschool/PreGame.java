package org.fnet.oldschool;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.fnet.oldschool.logging.LogSystem;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

public class PreGame {

	public static void getUpdate() throws MalformedURLException, FileNotFoundException, IOException, JDOMException {
		File base = new File(PublicVars.TEMP, "update");
		base.mkdirs();
		File updatexml = new File(base, "version.xml");
		IOUtils.copy(
				new URL("https://raw.githubusercontent.com/wweh/oldschool/master/update/versions.xml").openStream(),
				new FileOutputStream(updatexml));
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(updatexml);
		for (Element e : doc.getRootElement().getChildren("version")) {
			if (e.getAttribute("latest") != null && e.getAttribute("latest").getBooleanValue() == true) {
				String latest = e.getValue();
				String[] latest_split = latest.split("-");
				LogSystem.getLogger(PreGame.class).info("The Latest version is a " + latest_split[1] + " version: "
						+ latest_split[0] + " (CURRENT: " + PublicVars.VERSION + ")");
				int rawversion = Integer.parseInt(latest_split[0].replace(".", "").replace("v", ""));
				if (rawversion > PublicVars.VERSION_RAW) {
					File target = new File(base, latest + ".jar");
					if (!target.exists()) {
						LogSystem.getLogger(PreGame.class).info("Downloading version " + latest_split[0] + ".");
						Thread t = new Thread(() -> {
							try {
								IOUtils.copy(PublicVars.genUpdateStream(latest), new FileOutputStream(target));
							} catch (IOException e1) {
								e1.printStackTrace();
								LogSystem.getLogger(PreGame.class).warn("Failed to download the latest version.", e1);
							} finally {
								LogSystem.getLogger(PreGame.class).info("Finished Downloading.");
							}
						}, "Download Thread");
						t.start();
					}
				}
				break;
			}
		}
	}

	public static void extractNatives() throws FileNotFoundException, IOException, InterruptedException {
		File base = new File(PublicVars.TEMP, "natives");
		LogSystem.getLogger(PreGame.class).info("Game base directory: " + PublicVars.TEMP);
		base.mkdirs();
		for (String s : PublicVars.natives) {
			File f = new File(base, s.split("/")[s.split("/").length - 1]);
			if (!f.exists()) {
				IOUtils.copy(Main.class.getResourceAsStream(s), new FileOutputStream(f));
			}
		}
		System.setProperty("org.lwjgl.librarypath", base.getAbsolutePath());
		Thread.sleep(100);
	}

}
