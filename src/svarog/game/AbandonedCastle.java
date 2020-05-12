package svarog.game;


import svarog.entity.Player;
import svarog.io.Window;
import svarog.render.Camera;
import svarog.render.Texture;
import svarog.world.Door;
import svarog.world.Tile;
import svarog.world.World;

abstract class AbandonedCastle {
	public static World getWorld(Player player, Camera camera, Window window) {
		World world = new World(2, 90, 60);
		world.loadMap("abandonedCastle.png", 32);
		world.setSolidTilesFromMap("abandonedCastle_mask.png");

		world.addEntity(player);
		//player.setPosition(2, 45);
		
		// to start_map
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 0, 45);
		world.addDoor(new Door(1, 0, 45, 118, 16));
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 0, 44);
		world.addDoor(new Door(1, 0, 44, 118, 15));
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 0, 43);
		world.addDoor(new Door(1, 0, 43, 118, 14));

		
		
		
		// to second_mapB
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 86, 40);
		world.addDoor(new Door(3, 86, 40, 1, 52));
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 86, 41);
		world.addDoor(new Door(3, 86, 41, 1, 53));
		
		return world;
	}
}
