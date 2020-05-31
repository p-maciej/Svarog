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

abstract class IceCave {
	public static World getWorld(Player player, Camera camera, Window window) {
		World world = new World(4, 130, 130);
		world.loadMap("iceCave.png", 32);
		world.setSolidTilesFromMap("iceCave_mask.png");

		List<EntityHolder> temp = Save.ReadWorldEntities("world04");
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
		
		

		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 65, 129);
		world.addDoor(new Door(3, 65, 129, 50, 21));
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 66, 129);
		world.addDoor(new Door(3, 66, 129, 50, 21));
	
		Save.UpdateInteractions(world.getNPCs());

		return world;
	}
}