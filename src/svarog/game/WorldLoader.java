package svarog.game;

import svarog.entity.Player;
import svarog.io.Window;
import svarog.render.Camera;
import svarog.save.Save;
import svarog.world.World;

public abstract class WorldLoader {	
	
	private static int nextFrameLoadWorld = 1;
	private static boolean worldLoaded = false;
	private static int loadedWorldId = 0;

	public static World getWorld(int id, Player player, Camera camera, Window window) { // You need to add new if statement if you want doors to shift to particular world
		loadedWorldId = id;
		
		if(id == 1)
			return Village.getWorld(player, camera, window);
		else if(id == 2)
			return AbandonedCastle.getWorld(player, camera, window);
		else if(id == 3)
			return Crypt.getWorld(player, camera, window);
		else if(id == 4)
			return IceCave.getWorld(player, camera, window);
		else if(id == 5)
			return Swamp.getWorld(player, camera, window);
		else if(id == 6)
			return RitualHill.getWorld(player, camera, window);
		else  
			return null;	
	}
	
	public static int getNextFrameLoadWorld() {
		return nextFrameLoadWorld;
	}

	public static void setNextFrameLoadWorld(int nextFrameLoadWorld) {
		WorldLoader.nextFrameLoadWorld = nextFrameLoadWorld;
	}

	public static boolean isWorldLoaded() {
		return worldLoaded;
	}

	public static void setWorldLoaded(boolean worldLoaded) {
		WorldLoader.worldLoaded = worldLoaded;
	}

	public static int getLoadedWorldId() {
		return loadedWorldId;
	}
}
