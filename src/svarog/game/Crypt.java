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

abstract class Crypt {
	public static World getWorld(Player player, Camera camera, Window window) {
		World world = new World(3, 60, 60);
		world.loadMap("crypt.png", 32);
		world.setSolidTilesFromMap("crypt_mask.png");

		List<EntityHolder> temp = Save.ReadWorldEntities("world03");
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
		
		Save.UpdateInteractions(world.getNPCs());
		
		return world;
	}
}