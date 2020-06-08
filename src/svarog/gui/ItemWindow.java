package svarog.gui;

import svarog.gui.GroupProperties.groupType;
import svarog.gui.font.Font;
import svarog.objects.Item;

public class ItemWindow extends GuiWindow {

	private static Font font;
	private static TextureObject backgroundTexture;
	
	private TileSheet tileSheet;
	
	public ItemWindow(String title, TileSheet sheet) {
		super(title, font, backgroundTexture);
		
		this.setTileSheet(sheet);
	}
	
	public static void setFont(Font font) {
		ItemWindow.font = font;
	}

	public static void setBackgroundTexture(TextureObject backgroundTexture) {
		ItemWindow.backgroundTexture = backgroundTexture;
	}

	public TileSheet getTileSheet() {
		return tileSheet;
	}

	public void setTileSheet(TileSheet tileSheet) {
		this.tileSheet = tileSheet;
	}
	
	public void addRewardItem(Item item) {
		boolean finished = false;
			
		for(Group group : tileSheet.getTileGroupsList()) {
			if(group.getType() == groupType.swap) {
				for(GuiObject tile : group.getObjects()) {
					if(tile instanceof Tile) {
						Tile temp = (Tile)tile;
						if(temp.getPuttableItemTypes().size() > 1) {
							if(temp.getPuttedItem() == null) {
								try {
									temp.putItem(item);
									finished = true;
									break;
								} catch (Exception e) {}
							}		
						}
					}
				}
				if(finished)
					break;
			}
		}
	}
	
	public void clear() {
		for(Group group : tileSheet.getTileGroupsList()) {
			if(group.getType() == groupType.swap) {
				for(GuiObject tile : group.getObjects()) {
					if(tile instanceof Tile) {
						Tile temp = (Tile)tile;
							
						if(temp.getPuttedItem() != null) {
							temp.removePuttedItem();
						}		
					}
				}
			}
		}
	}
}
