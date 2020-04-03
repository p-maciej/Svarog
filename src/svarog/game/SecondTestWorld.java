package svarog.game;

import java.awt.image.BufferedImage;

import org.joml.Vector2f;

import svarog.entity.Enemy;
import svarog.entity.Entity;
import svarog.entity.Player;
import svarog.io.Window;
import svarog.render.Camera;
import svarog.render.Texture;
import svarog.render.Transform;
import svarog.world.Door;
import svarog.world.World;

abstract class SecondTestWorld {
	public static World getWorld(Player player, Camera camera, Window window) {
		World world = new World(2, 42, 30);
		world.calculateView(window);
		
		world.fillWorld(new Texture("textures/grass_map_1.png"));
		
		world.getTile(0, 20).setTexture(new Texture("textures/door.png"), (byte)1);
		Vector2f offset = new Vector2f(350, 70);
		world.setWorldOffset(offset);
		camera.setProjection(window.getWidth(), window.getHeight(), window, world.getScale(), world.getWidth(), world.getHeight(), world.getWorldOffset());

		
		BufferedImage home = Texture.getImageBuffer("textures/home1_map_1.png");
		for(int i = 0; i < 8; i++)
			for(int j = 0; j < 4; j++)
				world.getTile(7+i, 15+j).setTexture(new Texture(home, i, j, 32), (byte)(j < 3 ? 2 : 1));
		
		Enemy ArchEnemy = new Enemy(1, "", "avatar", new Transform().setPosition(10, 11), true, 10, 100, 200, 50);
		
		world.addEntity(ArchEnemy);
		
		world.addEntity(new Entity(2, new Texture("textures/player.png"), new Transform().setPosition(10, 10), true).setIsStatic(false));
		world.addEntity(new Entity(3, new Texture("textures/player.png"), new Transform().setPosition(18, 17), false));		
		
		for(int i = 0; i < 100; i++) {
			//System.out.println(Dziekan.GetRandomAttack());
		}
		world.addEntity(player);
		
		world.addDoor(new Door(1, 0, 20, 59, 26));
		
		world.setBoundingBoxes();
		
		return world;
	}
}
