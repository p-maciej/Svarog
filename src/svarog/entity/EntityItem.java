package svarog.entity;

import java.util.ArrayList;
import java.util.List;

import svarog.objects.Item;
import svarog.render.Animation;
import svarog.render.Texture;
import svarog.render.Transform;
import svarog.world.WorldRenderer;

public class EntityItem extends Entity {
	
	private List<Item> loot;
	
	public EntityItem(int id, Animation animation, Transform transform, boolean fullBoundingBox) {	
		super(id, animation, transform, fullBoundingBox);
		loot = new ArrayList<Item>();
		super.setClickable(true);
		super.setOverable(true);
	}
	
	public EntityItem(int id, Texture texture, Transform transform, boolean fullBoundingBox) {
		super(id, texture, transform, fullBoundingBox);
		loot = new ArrayList<Item>();
		super.setClickable(true);
		super.setOverable(true);
	}
	
	public EntityItem(int id, Transform transform, String name) {
		super(id, transform, name);
		loot = new ArrayList<Item>();
		super.setClickable(true);
		super.setOverable(true);
	}
	
	public void addLoot(Item item) {
		loot.add(item);
	}
	
	public List<Item> getLoot() {
		return loot;
	}

	@Override
	public boolean isClicked() {
		return WorldRenderer.getClickedEntityId() == super.getObjectId() ? true : false;
	}

}
