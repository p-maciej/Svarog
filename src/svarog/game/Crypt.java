package svarog.game;


import svarog.entity.Player;
import svarog.io.Window;
import svarog.render.Camera;
import svarog.render.Texture;
import svarog.world.Door;
import svarog.world.Tile;
import svarog.world.World;

abstract class Crypt {
	public static World getWorld(Player player, Camera camera, Window window) {
		World world = new World(2, 60, 60);
		world.loadMap("secondB_map.png", 32);
		

		world.addEntity(player);
		//player.setPosition(1, 53);
		
		// to second_mapA
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 0, 52);
		world.addDoor(new Door(2, 0, 52, 85, 40));
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 0, 53);
		world.addDoor(new Door(2, 0, 53, 85, 41));
		
		// to third_map
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 49, 21);
		world.addDoor(new Door(4, 49, 21, 65, 128));
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 49, 22);
		world.addDoor(new Door(4, 49, 22, 65, 128));
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 48, 21);
		world.addDoor(new Door(4, 48, 21, 65, 128));
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 48, 22);
		world.addDoor(new Door(4, 48, 22, 65, 128));
		
		return world;
	}
}