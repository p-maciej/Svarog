package svarog.gui;

import java.util.Arrays;
import java.util.List;

import svarog.gui.font.Font;
import svarog.objects.Item;
import svarog.objects.ItemProperties.ItemType;
import svarog.render.Texture;

public class Trade extends GuiWindow {
	private static Texture tileTexture;
	private static Texture tileTextureHover;
	private static Font font;
	private static TextureObject backgroundTexture;
	
	private TileSheet[] products;
	
	private int sum;
	
	public Trade(String title) {
		super(title, font, backgroundTexture);
		products = new TileSheet[2];
		
		products[0] = new TileSheet();
		products[1] = new TileSheet();
		
		this.sum = 0;
		
		createTileSheetProducts(tileTexture, tileTextureHover);
	}
	
	private void createTileSheetProducts(Texture tileTexture, Texture tileTextureHover) {
		List<ItemType> puttables = Arrays.asList(ItemType.values());

		Group tileGroup = new Group();
		
		int tileId = 100;
		
		for(int i = 0; i < 5; i++) {
			for(int j = 0; j < 4; j++) {
				Tile tile = new Tile(tileId++, tileTexture, tileTextureHover, (byte)0, j*50, -i*50);
				tile.setPuttableItemTypes(puttables);
				
				tileGroup.addGuiObject(tile);
			}
		}

		tileGroup.move(-230, 85);
		
		products[0].addTileGroup(tileGroup);
		
		
		Group tileGroup2 = new Group();
		tileId = 200;
		
		for(int j = 0; j < 5; j++) {
			Tile tile = new Tile(tileId++, tileTexture, tileTextureHover, (byte)0, j*50, 0);
			tile.setPuttableItemTypes(puttables);
				
			tileGroup2.addGuiObject(tile);
		}
		
		tileGroup2.move(0, 85);
		products[1].addTileGroup(tileGroup2);
	}
	
	public void addProduct(int tileId, Item item) {
		try {
			products[0].getTile(tileId).putItem(item);
		} catch (Exception e) {}
	}
	
	
	public int getExpanse() {
		return sum;
	}
	
	TileSheet[] getProducts() {
		return products;
	}
	
	public static void setTileTexture(Texture tileTexture) {
		Trade.tileTexture = tileTexture;
	}
	
	public static void setTileTextureHover(Texture tileTextureHover) {
		Trade.tileTextureHover = tileTextureHover;
	}
	
	public static void setFont(Font font) {
		Trade.font = font;
	}

	public static void setBackgroundTexture(TextureObject backgroundTexture) {
		Trade.backgroundTexture = backgroundTexture;
	}
}
