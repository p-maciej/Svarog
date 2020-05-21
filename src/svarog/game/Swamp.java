package svarog.game;

import java.util.List;

import svarog.entity.Enemy;
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
public abstract class Swamp {

	public static World getWorld(Player player, Camera camera, Window window) {
		World world = new World(5,100,100);
		world.loadMap("swamp.png", 32);
		world.setSolidTilesFromMap("swamp_mask.png");
		
		List<EntityHolder> temp = Save.ReadWorldEntities("world05");
		
		for(EntityHolder i: temp) {
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
		world.addEntity(player);
		
		
	
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 85, 0);
		world.addDoor(new Door(1, 85, 0, 10, 88));
		world.addTile((new Tile().setTexture(new Texture("textures/door.png"), (byte)1)), 86, 0);
		world.addDoor(new Door(1, 86, 0, 11, 88));
	

		
		return world;
}
}