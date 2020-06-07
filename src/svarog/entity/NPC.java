package svarog.entity;

import java.util.ArrayList;
import java.util.List;

import svarog.interactions.Interactions;
import svarog.language.LanguageLoader;
import svarog.render.Texture;
import svarog.render.Transform;
import svarog.save.EntityHolder;
import svarog.save.ItemParameters;
import svarog.save.NpcParameters;
import svarog.save.Save;
import svarog.world.WorldRenderer;

public class NPC extends Entity{

	private int globalNpcID = -1;
	private Interactions interactions = new Interactions();
	
	private static WorldObject questTexture;

	private List<ItemParameters> items = new ArrayList<ItemParameters>();

	public NPC(int id, Texture texture, Transform transform, boolean fullBoundingBox) {
		super(id, texture, transform, fullBoundingBox);
		super.setOverable(true);
		super.setClickable(true);
		
		super.setIsStatic(true); // static - default setting for NPC  
	}
	
	public NPC(int id, Texture texture, Transform transform, boolean fullBoundingBox, List<ItemParameters> items) {
		super(id, texture, transform, fullBoundingBox);
		super.setOverable(true);
		super.setClickable(true); // we should add explicit constructor for this functionality < -----------------------------------------
		super.setIsStatic(true); // static - default setting for NPC 
		
		this.setItems(items);
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
		
		if(!npcParameters.getItems().isEmpty()) {
			this.setItems(npcParameters.getItems());
		}
		
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
		
		if(!npcParams.getItems().isEmpty()) {
			this.setItems(npcParams.getItems());
		}
	}
	
	public NPC(int id, Transform transform, String name) {
		super(id, transform, name);
		super.setOverable(true);
		super.setClickable(true); 
		super.setIsStatic(true); 
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

	public List<ItemParameters> getItems() {
		return items;
	}

	public void setItems(List<ItemParameters> items) {
		this.items = items;
	}
}

