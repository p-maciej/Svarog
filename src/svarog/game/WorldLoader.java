package svarog.game;

import svarog.entity.Player;
import svarog.io.Window;
import svarog.render.Camera;
import svarog.world.World;

public abstract class WorldLoader {	
	
	public static int nextFrameLoadWorld;
	//public static Thread worldLoader;

	public static World getWorld(int id, Player player, Camera camera, Window window) { // You need to add new if statement if you want doors to shift to particular world
		if(id == 1)
			return StartWorld.getWorld(player, camera, window);
		else if(id == 2)
			return SecondWorldA.getWorld(player, camera, window);
		else if(id == 3)
			return SecondWorldB.getWorld(player, camera, window);
		else if(id == 4)
			return ThirdWorld.getWorld(player, camera, window);
		else  
			return null;
	}
	
	public static int getNextFrameLoadWorld() {
		return nextFrameLoadWorld;
	}

	public static void setNextFrameLoadWorld(int nextFrameLoadWorld) {
		WorldLoader.nextFrameLoadWorld = nextFrameLoadWorld;
	}
}
