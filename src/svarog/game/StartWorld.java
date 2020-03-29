package svarog.game;

import org.joml.Vector2f;

import svarog.entity.Entity;
import svarog.entity.Player;
import svarog.io.Window;
import svarog.render.Camera;
import svarog.render.Texture;
import svarog.render.Transform;
import svarog.world.Door;
import svarog.world.World;

abstract class StartWorld {
	public static World getWorld(Player player, Camera camera, Window window) {
		World world = new World(1, 120, 90);
		world.calculateView(window);
		
		world.fillWorld(new Texture("textures/grass_map_1.png"));
		Vector2f offset = new Vector2f(350, 70);
		world.setWorldOffset(offset);
		camera.setProjection(window.getWidth(), window.getHeight(), window, world.getScale(), world.getWidth(), world.getHeight(), world.getWorldOffset());

		world.loadMap("start_map.png", 32);

		world.addEntity(player);
		Entity ent1 = new Entity(new Texture("textures/player.png"), new Transform().setPosition(42, 26), true);
		ent1.setName("Maciej");
		Entity ent2 = new Entity(new Texture("textures/player.png"), new Transform().setPosition(46, 27), true);
		ent2.setName("Dawid");
		
		world.addEntity(ent1);
		world.addEntity(ent2);
		world.setSolidTilesFromMap("start_map.png");
		
		world.setBoundingBoxes();
		
		//Show door texture
		world.getTile(60, 28).setTexture(new Texture("textures/door.png"), (byte)1);
		world.getTile(60, 27).setTexture(new Texture("textures/door.png"), (byte)1);
		world.getTile(60, 26).setTexture(new Texture("textures/door.png"), (byte)1);
		world.getTile(60, 25).setTexture(new Texture("textures/door.png"), (byte)1);
		
		//adding door object
		world.addDoor(new Door(2, 60, 28, 1, 20));
		world.addDoor(new Door(2, 60, 27, 1, 20));
		world.addDoor(new Door(2, 60, 26, 1, 20));
		world.addDoor(new Door(2, 60, 25, 1, 20));
		//world.addDoor(new Door(60, 24, 1, 20));
		
		
		return world;
	}
}
