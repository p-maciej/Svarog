package svarog.game;

import svarog.entity.Enemy;
import svarog.entity.EntityItem;
import svarog.entity.NPC;
import svarog.entity.Player;
import svarog.io.Window;
import svarog.objects.Item;
import svarog.objects.ItemInfo;
import svarog.objects.ItemProperties.ItemType;
import svarog.render.Camera;
import svarog.render.Texture;
import svarog.render.Transform;
import svarog.save.Save;
import svarog.world.Door;
import svarog.world.Tile;
import svarog.world.World;

abstract class Village implements Runnable {
	
	public static World getWorld(Player player, Camera camera, Window window) {
		World world = new World(1, 120, 90);

		world.loadMap("village.png", 32);
		world.setSolidTilesFromMap("village_mask.png");
		
		NPC ent1 = new NPC(1, Save.getNpcsByID(0), true);
		NPC ent2 = new NPC(2, Save.getNpcsByID(0), new Transform().setPosition(46, 27), "Dawid", true);
		
		Enemy ArchEnemy = new Enemy(3, Save.getEnemyById(7), new Transform().setPosition(46, 29), "Bandit");
		ArchEnemy.setRespownInSec(3);
		Enemy enemy1 = new Enemy(4, Save.getEnemyById(3), new Transform().setPosition(49, 15), "Strzyga");
		enemy1.setRespownInSec(3);
		Enemy enemy2 = new Enemy(5, Save.getEnemyById(7), new Transform().setPosition(47, 29), "Andrzej");
		enemy2.setRespownInSec(-1);
		
		NPC npc01 = new NPC(6, Save.getNpcsByID(1), new Transform().setPosition(46, 25), true);
		
		NPC npc02 = new NPC(7, Save.getNpcsByID(2), new Transform().setPosition(48, 25).setScale(1, 1), true);
		
		NPC ninja = new NPC(8, Save.getNpcsByID(3), new Transform().setPosition(29, 28).setScale(1, 2), true);
		
		EntityItem appleEntity = new EntityItem(9, Save.getEntityItemParameters(0), new Transform().setPosition(29, 29));
		
		world.addEntity(appleEntity);
		world.addEntity(ent1);
		world.addEntity(ent2);
		world.addEntity(npc01);
		world.addEntity(npc02);
		world.addEntity(ArchEnemy);
		world.addEntity(enemy1);
		world.addEntity(enemy2);
		world.addEntity(ninja);

		world.addEntity(player); //We always should add player at the end, otherwise he will be rendered under entities ;)
		

		
		//Show door texture
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 119, 16);
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 119, 15);
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 119, 14);
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 119, 13);
		
		//adding door object
		world.addDoor(new Door(2, 119, 16, 2, 45));
		world.addDoor(new Door(2, 119, 15, 2, 45));
		world.addDoor(new Door(2, 119, 14, 2, 45));
		world.addDoor(new Door(2, 119, 13, 2, 45));
		//world.addDoor(new Door(60, 24, 1, 20));

		
		// door to swamp
	
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 12, 89);
		world.addDoor(new Door(5, 12, 89, 85, 1));
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 11, 89);
		world.addDoor(new Door(5, 11, 89, 85, 1));
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 10, 89);
		world.addDoor(new Door(5, 10, 89, 86, 1));
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 9, 89);
		world.addDoor(new Door(5, 9, 89, 86, 1));
		
		
		// door to ritualHill
		
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 0, 40);
		world.addDoor(new Door(6, 0, 40, 48, 40));
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 0, 41);
		world.addDoor(new Door(6, 0, 41, 48, 41));
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 0, 42);
		world.addDoor(new Door(6, 0, 42, 48, 42));

		
		
		return world;
	}
}
