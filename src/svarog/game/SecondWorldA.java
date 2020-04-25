package svarog.game;


import svarog.entity.Player;
import svarog.io.Window;
import svarog.render.Camera;
import svarog.render.Texture;
import svarog.world.Door;
import svarog.world.Tile;
import svarog.world.World;

abstract class SecondWorldA {
	public static World getWorld(Player player, Camera camera, Window window) {
		World world = new World(2, 90, 60);
		world.loadMap("second_map.png", 32);
		

		world.addEntity(player);
		player.setPosition(2, 45);
		
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 1, 45);
		world.addDoor(new Door(1, 1, 45, 118, 16));
		
		return world;
	}
}
