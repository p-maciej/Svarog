package svarog.game;

import svarog.entity.Enemy;
import svarog.entity.NPC;
import svarog.entity.Player;
import svarog.io.Window;
import svarog.render.Camera;
import svarog.render.Texture;
import svarog.render.Transform;
import svarog.world.Door;
import svarog.world.Tile;
import svarog.world.World;

abstract class StartWorld implements Runnable {
	
	public static World getWorld(Player player, Camera camera, Window window) {
		World world = new World(1, 120, 90);

		world.loadMap("start_map.png", 32);
    	
		world.setSolidTilesFromMap("start_map.png");
		
		NPC ent1 = new NPC(1, new Texture("textures/player.png"), new Transform().setPosition(42, 26), true);
		ent1.setName("Maciej");
		NPC ent2 = new NPC(2, new Texture("textures/player.png"), new Transform().setPosition(46, 27), true);
		ent2.setName("Dawid");
		Enemy ArchEnemy = new Enemy(3, new Texture("textures/avatar.png"), new Transform().setPosition(46, 29), true, 1, 50, 20, 150);
		ArchEnemy.setName("Ten Z³y");
		NPC npc01 = new NPC(4, new Texture("textures/npc01.png"), new Transform().setPosition(46, 25), true);
		npc01.setName("Sklepikarz");
		
		world.addEntity(ent1);
		world.addEntity(ent2);
		world.addEntity(npc01);
		world.addEntity(ArchEnemy);
		
		world.addEntity(player); //We always should add player at the end, otherwise he will be rendered under entities ;)
		

		
		//Show door texture
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 119, 16);
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 119, 15);
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 119, 14);
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 119, 13);
		
		//adding door object
		world.addDoor(new Door(2, 119, 16, 1, 20));
		world.addDoor(new Door(2, 119, 15, 1, 20));
		world.addDoor(new Door(2, 119, 14, 1, 20));
		world.addDoor(new Door(2, 119, 13, 1, 20));
		//world.addDoor(new Door(60, 24, 1, 20));

		return world;
	}
}
