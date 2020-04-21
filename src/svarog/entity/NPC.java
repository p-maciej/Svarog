package svarog.entity;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import svarog.objects.Item;
import svarog.objects.ItemInfo;
import svarog.render.Texture;
import svarog.render.Transform;
import svarog.world.WorldRenderer;

public class NPC extends Entity {

	//Animacja potem siê ogarnie
	//private String texturesPath;
	//private String fileName;
	
	private List<Item> items = new ArrayList<Item>();

	public NPC(int id, Texture texture, Transform transform, boolean fullBoundingBox) {
		super(id, texture, transform, fullBoundingBox);
		super.setOverable(true);
		super.setClickable(true);
		//this.texturesPath = texturePath;
		//this.fileName = filename;
		
		super.setIsStatic(true); // static - default setting for NPC  
	}
	
	public NPC(int id, Texture texture, Transform transform, boolean fullBoundingBox, List<Item> items) {
		super(id, texture, transform, fullBoundingBox);
		super.setOverable(true);
		super.setClickable(true); // we should add explicit constructor for this functionality < -----------------------------------------
		//this.texturesPath = texturePath;
		//this.fileName = filename;
		
		super.setIsStatic(true); // static - default setting for NPC 
		
		this.items = items;
	}
	
	public void AddItem(Item item) {
		items.add(item);
	}
	
	public void AddItem(Texture texture, Vector2f position, ItemInfo itemInfo, int globalID, int localID, int hpRegeneration, int attackBonus, int lvlRequired, String name, String description, int itemType) {
		items.add(new Item(texture, position, itemInfo, globalID, localID, hpRegeneration, attackBonus, lvlRequired, name, description, itemType));
	}

	@Override
	public boolean isClicked() {
		return WorldRenderer.getClickedEntityId() == super.getId() ? true : false;
	}
}

