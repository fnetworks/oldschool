package org.fnet.oldschool.map;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.fnet.oldschool.Manager;
import org.fnet.oldschool.entities.Block;
import org.lwjgl.util.vector.Vector3f;
import org.newdawn.slick.util.ResourceLoader;

public class Segment {

	private final ArrayList<Vector3f> blocks = new ArrayList<>();

	private static final HashMap<String, String> maps = new HashMap<>();
	
	public Segment(String name) {
		if (!maps.containsKey(name)) {
			StringBuilder sb = new StringBuilder();
			Scanner in = new Scanner(ResourceLoader.getResourceAsStream("res/mapsegments/" + name + ".sgm"));
			while (in.hasNextLine()) {
				sb.append(in.nextLine().replace(" ", ""));
			}
			in.close();
			maps.put(name, sb.toString());
		}
		String[] blocks = maps.get(name).split(",");
		for (String s : blocks) {
			int x = Integer.parseInt(s.split("/")[0]);
			int y = Integer.parseInt(s.split("/")[1]);
			int z = s.split("/").length != 2 ? 1 : 0;
			
			this.blocks.add(new Vector3f(x, y, z));
		}
	}

	public int addToList(List<Block> list, int x_offset, int y_offset) throws IOException {
		int highpoint = 0;
		for (Vector3f p : blocks) {
			if (p.getZ() == 0) {
				list.add(new Block(Manager.getPng("block"), x_offset + (int) p.getX() * 64, y_offset - (int) p.getY() * 64));
			} else {
				highpoint++;
				x_offset++;
			}
			highpoint = (int) Math.max(p.getX() * 64, highpoint);
		}
		return highpoint;
	}

}
