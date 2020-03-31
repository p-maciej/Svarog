package svarog.entity;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import svarog.objects.Item;
import svarog.objects.ItemInfo;
import svarog.render.Texture;
import svarog.render.Transform;

public class NPC extends Entity {

	//Animacja potem siê ogarnie
	//private String texturesPath;
	//private String fileName;
	
	private List<Item> items = new ArrayList<Item>();

	public NPC(String texturePath, String filename, Transform transform, boolean fullBoundingBox) {
		super(new Texture("textures/" + texturePath + "" + filename + ".png"), transform, fullBoundingBox);
		
		//this.texturesPath = texturePath;
		//this.fileName = filename;
		
		super.setIsStatic(true); // static - default setting for NPC  
	}
	
	public NPC(String texturePath, String filename, Transform transform, boolean fullBoundingBox, List<Item> items) {
		super(new Texture("textures/" + texturePath + "" + filename + ".png"), transform, fullBoundingBox);
		
		//this.texturesPath = texturePath;
		//this.fileName = filename;
		
		super.setIsStatic(true); // static - default setting for NPC 
		
		this.items = items;
	}
	
	public void AddItem(Item item) {
		items.add(item);
	}
	
	public void AddItem(Texture texture, Vector2f position, ItemInfo itemInfo, int hpRegeneration, int attackBonus, int lvlRequired, String name, String description, int itemType) {
		items.add(new Item(texture, position, itemInfo, hpRegeneration, attackBonus, lvlRequired, name, description, itemType));
	}
}

