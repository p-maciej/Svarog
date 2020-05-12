package svarog.game;

import svarog.entity.Player;
import svarog.io.Window;
import svarog.render.Camera;
import svarog.render.Texture;
import svarog.world.Door;
import svarog.world.Tile;
import svarog.world.World;

abstract class IceCave {
	public static World getWorld(Player player, Camera camera, Window window) {
		World world = new World(4, 130, 130);
		world.loadMap("iceCave.png", 32);
		world.setSolidTilesFromMap("iceCave_mask.png");
		

		world.addEntity(player);
		//player.setPosition(1, 53);
		
		

		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 65, 129);
		world.addDoor(new Door(3, 65, 129, 50, 21));
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 66, 129);
		world.addDoor(new Door(3, 66, 129, 50, 21));
	

		return world;
	}
}