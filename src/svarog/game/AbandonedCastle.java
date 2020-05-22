package svarog.game;


import java.util.ArrayList;
import java.util.List;

import svarog.entity.Enemy;
import svarog.entity.Entity;
import svarog.entity.EntityItem;
import svarog.entity.NPC;
import svarog.entity.Player;
import svarog.io.Window;
import svarog.render.Camera;
import svarog.render.Texture;
import svarog.save.EntityHolder;
import svarog.save.Save;
import svarog.world.Door;
import svarog.world.Tile;
import svarog.world.World;
import svarog.world.World.EntityRespawn;

abstract class AbandonedCastle {
	public static World getWorld(Player player, Camera camera, Window window) {
		World world = new World(2, 90, 60);
		world.loadMap("abandonedCastle.png", 32);
		world.setSolidTilesFromMap("abandonedCastle_mask.png");

		List<EntityHolder> temp = Save.ReadWorldEntities("world02");
		List<Entity> entityLocal = new ArrayList<Entity>();
		
		for(EntityHolder i: temp) {
			if(i.getType().equals("npc")) {
				entityLocal.add((Entity) new NPC(i));
			}else if(i.getType().equals("enemy")) {
				entityLocal.add((Entity) new Enemy(i));
			}else if(i.getType().equals("entityItem")){
				entityLocal.add((Entity) new EntityItem(i));
			}else {
				System.out.println("That kind of entity doesnt exist...");
			}
		}
		
		//Save.ReadEntityRespown(world.getId(), entityLocal, world);
		for(EntityRespawn i: Save.ReadEntityRespown(world.getId(), entityLocal, world)) {
			world.addEntitiesToRespawn(i);
		}
		
		//Adding to World
		for(Entity i: entityLocal) {
			world.addEntity((svarog.entity.Entity) i);
		}
		
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
