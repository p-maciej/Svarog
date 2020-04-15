package svarog.game;

import svarog.entity.Player;
import svarog.io.Window;
import svarog.render.Camera;
import svarog.world.World;

public abstract class WorldLoader {	
	
	public static Thread worldLoader; 
	
	public static World getWorld(int id, Player player, Camera camera, Window window) { // You need to add new if statement if you want doors to shift to particular world
		if(id == 1)
			return StartWorld.getWorld(player, camera, window);
		else if(id == 2)
			return SecondTestWorld.getWorld(player, camera, window);
		else 
			return null;
	}
}
