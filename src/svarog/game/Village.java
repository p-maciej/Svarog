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
import svarog.save.EntityHolder;
import svarog.save.Save;
import svarog.world.Door;
import svarog.world.Tile;
import svarog.world.World;

abstract class Village implements Runnable {
	
	public static World getWorld(Player player, Camera camera, Window window) {
		World world = new World(1, 120, 90);

		world.loadMap("village.png", 32);
		world.setSolidTilesFromMap("village_mask.png");
		
		for(EntityHolder i: Save.getEntityHolder01()) {
			if(i.getType().equals("npc")) {
				world.addEntity(new NPC(i));
			}else if(i.getType().equals("enemy")) {
				world.addEntity(new Enemy(i));
			}else if(i.getType().equals("entityItem")){
				world.addEntity(new EntityItem(i));
			}else {
				System.out.println("WTF???");
			}
		}
		
		world.getEntityById(4).setRespownInSec(3);
		
		

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
