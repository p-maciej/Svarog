package svarog.game;

import svarog.entity.Player;
import svarog.io.Window;
import svarog.render.Camera;
import svarog.render.Texture;
import svarog.world.Door;
import svarog.world.World;

abstract class StartWorld {
	public static World getWorld(Player player, Camera camera, Window window) {
		World world = new World(1, 120, 80);
		world.calculateView(window);
		
		world.fillWorld(new Texture("grass_map_1.png"));
		
		camera.setProjection(window.getWidth(), window.getHeight(), window, world.getScale(), world.getWidth(), world.getHeight());

		world.loadMap("start_map.png", 32);
		world.getTile(60, 28).setTexture(new Texture("door.png"), (byte)2);
		world.getTile(60, 27).setTexture(new Texture("door.png"), (byte)2);
		world.getTile(60, 26).setTexture(new Texture("door.png"), (byte)2);
		world.getTile(60, 25).setTexture(new Texture("door.png"), (byte)2);
		world.getTile(60, 24).setTexture(new Texture("door.png"), (byte)2);
		
		world.addEntity(player);
		
		world.setSolidTilesFromMap("start_map.png");
		
		world.setBoundingBoxes();
		
		
		world.addDoor(new Door(60, 28, 1, 20));
		world.addDoor(new Door(60, 27, 1, 20));
		world.addDoor(new Door(60, 26, 1, 20));
		world.addDoor(new Door(60, 25, 1, 20));
		//world.addDoor(new Door(60, 24, 1, 20));
		
		
		return world;
	}
}
