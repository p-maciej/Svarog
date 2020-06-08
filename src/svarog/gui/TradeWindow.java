package svarog.gui;

import java.util.Arrays;
import java.util.List;

import org.joml.Vector2f;

import svarog.entity.Player;
import svarog.gui.font.Font;
import svarog.gui.font.Line;
import svarog.language.LanguageLoader;
import svarog.objects.Item;
import svarog.objects.ItemProperties.ItemType;
import svarog.render.Texture;

public class TradeWindow extends GuiWindow implements GroupProperties {
	private static Texture tileTexture;
	private static Texture tileTextureHover;
	private static Font font;
	private static LanguageLoader language;

	private static TextureObject backgroundTexture;
	
	private TileSheet[] products;
	
	private Button buy;
	
	private int sum;
	private Line amount;
	
	private static Button buyButton;
	
	public TradeWindow(String title) {
		super(title, font, backgroundTexture);
		products = new TileSheet[2];
		
		products[0] = new TileSheet();
		products[1] = new TileSheet();
		
		this.sum = 0;
		
		createTileSheetProducts(tileTexture, tileTextureHover);
		addStaticElements();
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
	
	private void addStaticElements() {
		if(buyButton == null) {
			buyButton = new Button(new Texture("images/button_buy.png"), new Texture("images/button_buy_hover.png"), new Vector2f());
			buyButton.move(220, 120);
		}
		
		this.buy = buyButton;
		
		Line worth = new Line(-10, -125);
		worth.setString(language.getValue("tradeWindowWorth"), font);
		
		amount = new Line(0, 0);
		amount.setString("0", font);
		amount.setPosition(30+amount.getWidth()/2, -125);
		
		super.addTextureObject(amount);
		super.addTextureObject(worth);
		super.addTextureObject(buy);
	}
	
	private void calcWorth() {
		this.sum = 0;
		for(Group group : products[1].getTileGroupsList()) {
			for(GuiObject object : group.getTextureObjectList()) {
				Tile temp = (Tile)object;
				
				if(temp.getPuttedItem() != null)
					this.sum += temp.getPuttedItem().getItemInfo().getPrize();
			}
		}
	}
	
	void update() {
		calcWorth();
		amount.setString(Integer.toString(sum), font);
		amount.setPosition(30+amount.getWidth()/2, -125);
	}
	
	public void addProduct(int tileId, Item item) {
		try {
			products[0].getTile(tileId).putItem(item);
		} catch (Exception e) {}
	}
	
	public int itemsInBuySection() {
		int temp_=0;
		for(Group grp:products[1].getTileGroupsList()) {
			for(GuiObject object:grp.getTextureObjectList()) {
				Tile temp = (Tile)(object);
				
				if(temp.getPuttedItem() != null) {
					temp_++;
				}
			}
		}
		return temp_;
	}
	
	public void buyItems(Player player, GuiRenderer guiRenderer) {
		for(Group grp:products[1].getTileGroupsList()) {
			for(GuiObject object:grp.getTextureObjectList()) {
				Tile temp = (Tile)(object);
				
				if(temp.getPuttedItem() != null) {
					player.addItemToInventoryWithGUIupdate(new Item(temp.getPuttedItem()), guiRenderer);
				}
			}
		}
		for(Group group : products[1].getTileGroupsList()) {
			for(GuiObject object : group.getTextureObjectList()) {
				Tile temp = (Tile)object;
				
				if(temp.getPuttedItem() != null)
					temp.removePuttedItem();
			}
		}
		update();
	}
	
	public int getExpanse() {
		return sum;
	}
	
	TileSheet[] getProducts() {
		return products;
	}
	
	public TileSheet getProductsToBuy() {
		return products[1];
	}
	
	public static void setTileTexture(Texture tileTexture) {
		TradeWindow.tileTexture = tileTexture;
	}
	
	public static void setTileTextureHover(Texture tileTextureHover) {
		TradeWindow.tileTextureHover = tileTextureHover;
	}
	
	public static void setFont(Font font) {
		TradeWindow.font = font;
	}

	public static void setBackgroundTexture(TextureObject backgroundTexture) {
		TradeWindow.backgroundTexture = backgroundTexture;
	}

	public Button getBuyButton() {
		return buy;
	}
	
	public static void setLanguage(LanguageLoader language) {
		TradeWindow.language = language;
	}
}
