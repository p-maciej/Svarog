package svarog.gui;

import java.util.Collections;

import svarog.gui.GroupProperties.groupType;
import svarog.gui.font.Font;
import svarog.objects.Item;

public class ItemWindow extends GuiWindow {

	private static Font font;
	private static TextureObject backgroundTexture;
	
	private static TileSheet tileSheet;
	
	public ItemWindow(String title) {
		super(title, font, backgroundTexture);
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

	public static void setTileSheet(TileSheet tileSheet) {
		ItemWindow.tileSheet = tileSheet;
	}
	
	public void addRewardItem(Item item) {
		boolean finished = false;
		
		Collections.sort(tileSheet.getTileGroupsList());
		
		for(Group group : tileSheet.getTileGroupsList()) {
			Collections.sort(group.getObjects());
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
