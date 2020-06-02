package svarog.entity;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import svarog.interactions.Interactions;
import svarog.language.LanguageLoader;
import svarog.objects.Item;
import svarog.objects.ItemInfo;
import static svarog.objects.ItemInfo.ItemType;
import svarog.render.Texture;
import svarog.render.Transform;
import svarog.save.EntityHolder;
import svarog.save.NpcParameters;
import svarog.save.Save;
import svarog.world.WorldRenderer;

public class NPC extends Entity{

	private int globalNpcID = -1;
	private Interactions interactions = new Interactions();
	
	private static WorldObject questTexture;

	private List<Item> items = new ArrayList<Item>();

	public NPC(int id, Texture texture, Transform transform, boolean fullBoundingBox) {
		super(id, texture, transform, fullBoundingBox);
		super.setOverable(true);
		super.setClickable(true);
		
		super.setIsStatic(true); // static - default setting for NPC  
	}
	
	public NPC(int id, Texture texture, Transform transform, boolean fullBoundingBox, List<Item> items) {
		super(id, texture, transform, fullBoundingBox);
		super.setOverable(true);
		super.setClickable(true); // we should add explicit constructor for this functionality < -----------------------------------------
		super.setIsStatic(true); // static - default setting for NPC 
		
		this.items = items;
	}
	
	public NPC(int id, NpcParameters npcParams, Transform transform, boolean isClickable) {
		super(id, npcParams, transform);
		super.setOverable(true);
		super.setClickable(isClickable);
		
		super.setIsStatic(true);
		super.setName(npcParams.getName());
		
		this.globalNpcID = npcParams.getGlobalNpcID();
		if(npcParams.getInteractionsPath()!=null && !(npcParams.getInteractionsPath().isEmpty())) {
			this.setInteractions(new Interactions(npcParams.getInteractionsPath()));
		}
	}
	
	public NPC(int id, NpcParameters npcParams, Transform transform, String name, boolean isClickable) {
		super(id, npcParams, transform);
		super.setOverable(true);
		super.setClickable(isClickable);
		
		super.setIsStatic(true);
		super.setName(name);
		
		this.globalNpcID = npcParams.getGlobalNpcID();
		if(npcParams.getInteractionsPath()!=null && !(npcParams.getInteractionsPath().isEmpty())) {
			this.setInteractions(new Interactions(npcParams.getInteractionsPath()));
		}
	}
	
	public NPC(EntityHolder entityHolder) {
		super(entityHolder.getId(), Save.getNpcsByID(entityHolder.getTypeID()), new Transform().setPosition(entityHolder.getPosX(), entityHolder.getPosY()).setScale(entityHolder.getScaleX(), entityHolder.getScaleY()));
		super.setOverable(true);
		super.setClickable(entityHolder.isClickable());
		
		super.setIsStatic(true);
		
		NpcParameters npcParameters = Save.getNpcsByID(entityHolder.getTypeID());
		
		if(entityHolder.getName() != null && !(entityHolder.getName().equals(""))) {
			super.setName(LanguageLoader.getLanguageLoader().getValue(entityHolder.getName()));
		}else {
			super.setName(LanguageLoader.getLanguageLoader().getValue(npcParameters.getName()));
		}
		
		this.globalNpcID = (Save.getNpcsByID(entityHolder.getTypeID())).getGlobalNpcID();
		
		if((Save.getNpcsByID(entityHolder.getTypeID())).getInteractionsPath()!=null && !((Save.getNpcsByID(entityHolder.getTypeID())).getInteractionsPath().isEmpty())) {
			this.setInteractions(new Interactions((Save.getNpcsByID(entityHolder.getTypeID())).getInteractionsPath()));
		}
	}
	
	public NPC(int id, NpcParameters npcParams, boolean isClickable) {
		super(id, npcParams);
		super.setOverable(true);
		super.setClickable(isClickable); 
		
		super.setIsStatic(true); 
		super.setName(npcParams.getName());
		
		this.globalNpcID = npcParams.getGlobalNpcID();
		if(npcParams.getInteractionsPath()!=null && !(npcParams.getInteractionsPath().isEmpty())) {
			System.out.println("-"+npcParams.getInteractionsPath()+"-");
			this.setInteractions(new Interactions(npcParams.getInteractionsPath()));
		}
	}
	
	public NPC(int id, Transform transform, String name) {
		super(id, transform, name);
		super.setOverable(true);
		super.setClickable(true); 
		super.setIsStatic(true); 
	}
	
	public void AddItem(Item item) {
		items.add(item);
	}
	
	public void AddItem(Texture texture, Vector2f position, ItemInfo itemInfo, int globalID, int localID, int hpRegeneration, int attackBonus, int lvlRequired, String name, String description, ItemType itemType, int prize) {
		items.add(new Item(texture, position, itemInfo, globalID, localID, hpRegeneration, attackBonus, lvlRequired, name, description, itemType, prize));
	}
	
	public Interactions getInteractions() {
		return interactions;
	}

	public void setInteractions(Interactions interactions) {
		this.interactions = interactions;
	}

	@Override
	public boolean isClicked() {
		return WorldRenderer.getClickedEntityId() == super.getObjectId() ? true : false;
	}

	public int getGlobalNpcID() {
		return globalNpcID;
	}

	public void setGlobalNpcID(int globalNpcID) {
		this.globalNpcID = globalNpcID;
	}
	
	public int isQuestWaiting() {
		return this.interactions.getIsUsed();
	}

	public static WorldObject getQuestTexture() {
		return questTexture;
	}

	public static void setQuestTexture(WorldObject questTexture) {
		NPC.questTexture = questTexture;
		NPC.questTexture.getTexture().prepare();
	}
}

