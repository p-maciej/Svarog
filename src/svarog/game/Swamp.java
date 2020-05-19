package svarog.game;

import svarog.entity.Player;
import svarog.io.Window;
import svarog.render.Camera;
import svarog.render.Texture;
import svarog.world.Door;
import svarog.world.Tile;
import svarog.world.World;
public abstract class Swamp {

	public static World getWorld(Player player, Camera camera, Window window) {
		World world = new World(5,100,100);
		world.loadMap("swamp.png", 32);
		world.setSolidTilesFromMap("swamp_mask.png");
		

		world.addEntity(player);
		
		
	
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 85, 0);
		world.addDoor(new Door(1, 85, 0, 10, 88));
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 86, 0);
		world.addDoor(new Door(1, 86, 0, 11, 88));
	

		
		return world;
}
}