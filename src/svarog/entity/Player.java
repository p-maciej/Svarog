package svarog.entity;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LAST;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;

import svarog.audio.Audio;
import svarog.audio.Sound;
import svarog.gui.Arena;
import svarog.gui.Button;
import svarog.gui.GuiRenderer;
import svarog.gui.GuiWindow;
import svarog.gui.GuiRenderer.stickTo;
import svarog.gui.PagedGuiWindow;
import svarog.gui.PagedGuiWindow.Type;
import svarog.gui.TextureObject;
import svarog.gui.Tile;
import svarog.gui.TradeWindow;
import svarog.gui.font.Font;
import svarog.gui.font.TextBlock;
import svarog.interactions.Quest;
import svarog.interactions.Task;
import svarog.interactions.Task.doState;
import svarog.io.Window;
import svarog.language.LanguageLoader;
import svarog.objects.Item;
import svarog.render.Animation;
import svarog.render.Camera;
import svarog.render.Texture;
import svarog.render.Transform;
import svarog.save.ItemParameters;
import svarog.save.PlayerParameters;
import svarog.save.Save;
import svarog.world.World;
import svarog.world.WorldRenderer;

public class Player extends Entity {
	
	private int isFightWin = 0; //0==no fight, 1 == win, 2 == lost
	
	private boolean setCamWithoutAnimation;
	private Vector2f movement;

	private float speed = 0.2f;

	private int[] lastKeysPressed = new int[4];
	private int lastPressedKey;
	private String texturesPath;
	private String fileName;
	
	//Confirm in trade window
	private static GuiWindow confirmWindow;
	private static Button applyButton;
	
	private boolean movementLock;

	private Sound walk;
	
	//HP, Level, NPC, MinAttack, MaxAttack//
	private HP hp = new HP(100);
	private XP xp = new XP(0);
	private int maxAttack;
	private int minAttack;
	
	//Money, Inventory //
	private int money = 0;
	private Inventory inventory;
	
	//Trade window
	private int isTradeOn=0;
	private TradeWindow trade;

	private List<Quest> quests = new ArrayList<>();
	
	private boolean autoMovement = false;
	
	private Vector2f lastPosition = new Vector2f();
	private int textureType = 1;

	public Player(int id, String texturePath, String filename, Sound walkSound, Transform transform, boolean fullBoundingBox) {
		super(id, new Texture("textures/animations/" + texturePath + "idle/down/" + filename + ".png"), transform, fullBoundingBox);
		
		this.setWalkSound(walkSound);
		
		//ADDING FIRST QUEST
		List<Task>tasks001 = new ArrayList<>();
		tasks001.add(new Task(0, 1, 2, doState.talk));
		this.getQuests().add(new Quest(-100, "Pogadaj z Rozanna", "Musisz sie udac gdzies tam aby pogadac z Rozanna.",tasks001 ));
		
		//END OF FIRST QUEST
		
		this.texturesPath = texturePath;
		this.fileName = filename;
		
		setCamWithoutAnimation = true;
		lastPressedKey = GLFW_KEY_LAST;
		
		this.setMovementLock(false);
		super.setIsStatic(false); // Non-static - default setting for player 
	}
	
	public Player(Sound walkSound, PlayerParameters playerParam, Font font) {
		super(0,
				new Texture("textures/animations/" + "player/mavak/" + "idle/down/" + "mavak" + ".png"),
				new Transform().setPosition(playerParam.getPositionX(), playerParam.getPositionY()), false);
		
		this.setWalkSound(walkSound);
		
		//ADDING QUEST
		this.quests = playerParam.getQuests();
		this.setInventory(new Inventory(playerParam.getItems()));
		this.setName(playerParam.getPlayerName());
		this.getHP().SetMaxHP(playerParam.getMaxHP());
		this.setHpXpAttack(playerParam.getHP(), playerParam.getXp(), playerParam.getMinAttack(), playerParam.getMaxAttack());
		this.setMoney(playerParam.getMoney());
		this.texturesPath = playerParam.getTexturesPath();
		this.fileName = playerParam.getFileName();
		
		setCamWithoutAnimation = true;
		lastPressedKey = GLFW_KEY_LAST;
		
		this.setMovementLock(false);
		super.setIsStatic(false); // Non-static - default setting for player
		
		confirmWindow = new GuiWindow(LanguageLoader.getLanguageLoader().getValue("youDontHaveEnoughMoney"), font, new TextureObject(new Texture("images/window3.png")), false);
		applyButton = new Button(new Texture("images/buttonYes.png"),new Texture("images/buttonYes_hover.png"), new Vector2f(0, -15));

		confirmWindow.addTextureObject(applyButton);
	}
	
	public Player(int id, String texturePath, String filename, Sound walkSound, Transform transform, boolean fullBoundingBox, Inventory inventory, Font font) {
		super(id, new Texture("textures/animations/" + texturePath + "idle/down/" + filename + ".png"), transform, fullBoundingBox);
		setInventory(inventory);
		this.setWalkSound(walkSound);
		
		//ADDING FIRST QUEST
		List<Task>tasks001 = new ArrayList<>();
		tasks001.add(new Task(0, 1, 2, doState.talk));
		this.getQuests().add(new Quest(-100, "Pogadaj z Rozanna", "Musisz sie udac gdzies tam aby pogadac z Rozanna.",tasks001 ));
		
		//END OF FIRST QUEST
		
		this.texturesPath = texturePath;
		this.fileName = filename;
		
		setCamWithoutAnimation = true;
		lastPressedKey = GLFW_KEY_LAST;
		
		this.setMovementLock(false);
		super.setIsStatic(false); // Non-static - default setting for player 

		confirmWindow = new GuiWindow(LanguageLoader.getLanguageLoader().getValue("youDontHaveEnoughMoney"), font, new TextureObject(new Texture("images/window3.png")), false);
		applyButton = new Button(new Texture("images/buttonYes.png"),new Texture("images/buttonYes_hover.png"), new Vector2f(0, -15));

		confirmWindow.addTextureObject(applyButton);
	}
	
	public PagedGuiWindow getQuestsPagedOnGUI(Font font, Font font2, LanguageLoader language) {
		/// Windows on GUI /////////////////////////
		PagedGuiWindow quests1 = new PagedGuiWindow("Questy", font, new TextureObject(new Texture("images/window1.png")));
		quests1.setStickTo(stickTo.TopRight);
		quests1.move(-520, -275);
		
		for(Quest ques: this.getQuests()) {
			if(ques.isEndedQuest() != true) {
				quests1.addTextBlock(new TextBlock(280, new Vector2f(), font, language.getValue(ques.getTitle())), Type.headline);
				String tempowy = language.getValue(ques.getDescription());
				for(Task tasks01: ques.getTasks()) {
					tempowy+= TextBlock.lineBreak;
					tempowy+= tasks01.progress();
				}
				quests1.addTextBlock(new TextBlock(265, new Vector2f(), font2, tempowy), Type.content);
			}
		}

		quests1.setPageContent();
		
		////////////////////////////////////////////
		return quests1;
	}
	
	public void tradeWithPlayer(WorldRenderer currentWorld, GuiRenderer guiRenderer, int NPCid) {
		isTradeOn=1;
		trade = new TradeWindow("trade");
		
		List<ItemParameters> items = new ArrayList<>();
		items = currentWorld.getWorld().getNpcByNpcId(NPCid).getItems();
		for(ItemParameters itemPa: items) {
			trade.addProduct(itemPa.getItemTileID(), new Item(Save.getItemById(itemPa.getItemGlobalID())));
		}
		trade.setPosition(-100, 0);
		guiRenderer.addWindow(trade);
	}
	
	private boolean isPlayerMoved() {
		return !(lastPosition.x == super.getTransform().getPosition().x && lastPosition.y == super.getTransform().getPosition().y);
	}
	
	public Vector2f movePlayer(int direction, boolean auto) {
		movement = new Vector2f();
		
		if(auto)
			autoMovement = true;
		
		if(direction == 65) {
			movement.add(-1*speed, 0);
			
			if(super.currentDirection == Direction.left && (super.isColliding[0] || super.isColliding[1])) {
				if(textureType != 1)
					setTexture(Direction.left);
			}
			else  {
				if(super.currentDirection != Direction.left || isPlayerMoved()) {
					if(textureType != 2 || super.currentDirection != Direction.left)
						setAnimation(Direction.left);
				}
			}
			
		} else if(direction == 68) {
			movement.add(1*speed, 0);
			
			if(super.currentDirection == Direction.right && (super.isColliding[0] || super.isColliding[1])) {
				if(textureType != 1)
					setTexture(Direction.right);
			}
			else {
				if(super.currentDirection != Direction.right || isPlayerMoved())
					if(textureType != 2 || super.currentDirection != Direction.right)
						setAnimation(Direction.right);
			}
		} else if(direction == 87) {
			movement.add(0, 1*speed);
			
			if(super.currentDirection == Direction.up && (super.isColliding[0] || super.isColliding[1])) {
				if(textureType != 1)
					setTexture(Direction.up);
			}
			else {
				if(super.currentDirection != Direction.up || isPlayerMoved())
					if(textureType != 2 || super.currentDirection != Direction.up)
						setAnimation(Direction.up);
			}
		} else if(direction == 83) {
			movement.add(0, -1*speed);
			
			if(super.currentDirection == Direction.down && (super.isColliding[0] || super.isColliding[1])) {
				if(textureType != 1)
					setTexture(Direction.down);
			}
			else {
				if(super.currentDirection != Direction.down || isPlayerMoved()) {
					if(textureType != 2 || super.currentDirection != Direction.down)
						setAnimation(Direction.down);
				}
			}
		} else if(direction == 0) {
			if(super.currentDirection == Direction.left) {
				setTexture(Direction.left);
			}
			if(super.currentDirection == Direction.right) {
				setTexture(Direction.right);
			}
			if(super.currentDirection == Direction.up) {
				setTexture(Direction.up);
			}
			if(super.currentDirection == Direction.down) {
				setTexture(Direction.down);
			}
		}
		
		lastPressedKey = direction;
		
		this.lastPosition = new Vector2f(super.getTransform().getPosition().x, super.getTransform().getPosition().y);
		
		return movement;
	}
	
	@Override
	public void update(float delta, Window window, Camera camera, WorldRenderer world, Audio audioPlayer, GuiRenderer guiRenderer) {
		
		if(isTradeOn==1) {
			if(trade.getBuyButton().isClicked()) {
				if(trade.getExpanse()>0) {
					if(trade.getExpanse()<=this.money) {
						trade.buyItems(this, guiRenderer);
						this.setMoney(getMoney()-trade.getExpanse());
						guiRenderer.getStatsContainer().updatePlayerInventory(guiRenderer, this);
					}
					else {
						guiRenderer.addWindow(confirmWindow);
					}
				}
			}
			if(applyButton.isClicked()) {

					guiRenderer.removeWindow(confirmWindow.getId());

			}
			if(trade.isClosed()) {
				isTradeOn=0;
			}
		}
		
		if(movementLock == false) {		
			///////////// WASD Player movement ////////////////////
			
			int direction = 0;
			int keysPressed[] = new int[4];
			
			if(window.getInput().isKeyDown(GLFW_KEY_A)) {
				keysPressed[0] = GLFW_KEY_A;
			}
			else {
				keysPressed[0] = -1;
			}
			
			if(window.getInput().isKeyDown(GLFW_KEY_D)) {
				keysPressed[1] = GLFW_KEY_D;
			}
			else {
				keysPressed[1] = -1;
			}
			
			if(window.getInput().isKeyDown(GLFW_KEY_W)) {
				keysPressed[2] = GLFW_KEY_W;
			}
			else {
				keysPressed[2] = -1;
			}
			
			if(window.getInput().isKeyDown(GLFW_KEY_S)) {
				keysPressed[3] = GLFW_KEY_S;
			}
			else {
				keysPressed[3] = -1;
			}
			
			direction = getNewPressedKey(lastKeysPressed, keysPressed);
			setLastKeysPressed(keysPressed);
			
			if(keysPressed[0]+keysPressed[1]+keysPressed[2]+keysPressed[3] != -4) {
				autoMovement = false;
			}
			
			if(!autoMovement)
				movement = movePlayer(direction, false);
			
			
			if((movement.x == 0 && movement.y == 0) || (super.isColliding[0] || super.isColliding[1])) {
				if(audioPlayer.isPlaying(walk))
					audioPlayer.stop(walk);
			} else {
				if(!audioPlayer.isPlaying(walk))
					audioPlayer.play(walk);
			}
			
			move(movement);
			
			if(setCamWithoutAnimation) {
				camera.setPosition(transform.getPosition().mul(-WorldRenderer.getScale(), new Vector3f()));
				setCamWithoutAnimation = false;
			}
			else {
				camera.getPosition().lerp(transform.getPosition().mul(-WorldRenderer.getScale(), new Vector3f()), 0.6f); // Camera movement
			}
			
			super.update(delta, window, camera, world, audioPlayer, guiRenderer);
			/////////////////////////////////////////////////////////
		}
	}
	
	private void setAnimation(Direction direction) {
		if(direction == Direction.left) {
			super.setTexture(Direction.left, new Animation(4, 8, this.texturesPath + "walking/left/" + this.fileName));
		} else 	if(direction == Direction.right) {
			super.setTexture(Direction.right, new Animation(4, 8, this.texturesPath + "walking/right/" + this.fileName));
		} else 	if(direction == Direction.up) {
			super.setTexture(Direction.up, new Animation(4, 8, this.texturesPath + "walking/up/" + this.fileName));
		} else 	if(direction == Direction.down) {
			super.setTexture(Direction.down, new Animation(4, 8, this.texturesPath + "walking/down/" + this.fileName));
		}
		
		textureType = 2;
	}
	
	private void setTexture(Direction direction) {
		if(direction == Direction.left) {
			super.setTexture(Direction.left, new Texture("textures/animations/" + this.texturesPath + "idle/left/" + this.fileName + ".png"));
		} else if(direction == Direction.right) {
			super.setTexture(Direction.right, new Texture("textures/animations/" + this.texturesPath + "idle/right/" + this.fileName + ".png"));
		} else if(direction == Direction.up) {
			super.setTexture(Direction.up, new Texture("textures/animations/" + this.texturesPath + "idle/up/" + this.fileName + ".png"));
		} else if(direction == Direction.down) {
			super.setTexture(Direction.down, new Texture("textures/animations/" + this.texturesPath + "idle/down/" + this.fileName + ".png"));
		}
		
		textureType = 1;
	}
	
	public Texture getPlayerTexture(Direction direction) {
		if(direction == Direction.left) {
			return new Texture("textures/animations/" + this.texturesPath + "idle/left/" + this.fileName + ".png");
		} else if(direction == Direction.right) {
			return new Texture("textures/animations/" + this.texturesPath + "idle/right/" + this.fileName + ".png");
		} else if(direction == Direction.up) {
			return new Texture("textures/animations/" + this.texturesPath + "idle/up/" + this.fileName + ".png");
		} else if(direction == Direction.down) {
			return new Texture("textures/animations/" + this.texturesPath + "idle/down/" + this.fileName + ".png");
		}
		
		return null;
	}
	
	private int getNewPressedKey(int[] lastPressedKeys, int[] pressedKeys) {
		int pressed = 0;
		for(int i = 0; i < 4; i++) {
			if(pressedKeys[i] != -1)
				pressed++;
			
			if(lastKeysPressed[i] != pressedKeys[i] && pressedKeys[i] != -1) {
				return pressedKeys[i];
			}
		}
		
		if(pressed == 1) {
			for(int i = 0; i < 4; i++) {
				if(pressedKeys[i] != -1)
					return pressedKeys[i];
			}
		}
		
		if(pressed == 0) {
			return 0;
		}
		
		return lastPressedKey;
	}
	
	public void fightShow(GuiRenderer guiRenderer, Player player, Enemy enemy, World world, Font font) {
		Arena arena = new Arena(player, enemy);
		List<TextBlock> log = new ArrayList<TextBlock>();
		
		for(String word: player.fightLogic((Enemy)enemy, world, guiRenderer)) {
			log.add(new TextBlock(250, new Vector2f(), font, word));
		}
		
		arena.setLog(log);
		this.setMovementLock(true);
		guiRenderer.showArena(arena);
	}
	
	public void addItemToInventoryWithGUIupdate(Item item, GuiRenderer guiRenderer) {
		this.getInventory().getItems().add(item);
		guiRenderer.getTileSheet().putItemFirstEmpty(this.getInventory().getItems().get(this.getInventory().getItems().size()-1), this);
	}
	
	public ArrayList<String> fightLogic(Enemy enemy, World world, GuiRenderer guiRenderer) {
		ArrayList<String> fightString = new ArrayList<>();
		while((enemy).GetEnemyHP()>0) { // This is too "smart". You should make method like "attack" and make all of this statements and returning different results.
			fightString.add(enemy.getName()+ " "+LanguageLoader.getLanguageLoader().getValue("fightSystemHpBefore")+" " + (enemy).GetEnemyHP());
			(enemy).DecreaseEnemyHP(this.getRandomAttack()+getPlayerAttackBonus());
			fightString.add(enemy.getName()+ " "+LanguageLoader.getLanguageLoader().getValue("fightSystemHpAfter")+" " + (enemy).GetEnemyHP());
			if((enemy).GetEnemyHP()<=0) {
				fightString.add((enemy).getName() + " "+LanguageLoader.getLanguageLoader().getValue("fightSystemPlayerWon"));
				//Adding XP and money reward
				this.AddPlayerXP(enemy.GetXpForKilling());
				//System.out.println(this.getXP().GetXP()+ " " + this.getXP().getXpmin() + " " + this.getXP().getXpmax());
				
				this.money += enemy.getReward();
				
				for(int iter = 0; iter<enemy.getItems().size();iter++) {
					if(enemy.getPropability().get(iter)==1) {
						this.addItemToInventoryWithGUIupdate(new Item(enemy.getItems().get(iter)), guiRenderer);
					}else {
						int propability = (int)((enemy.getPropability().get(iter))*Math.random() + 1);
						if(propability==1) {
							this.addItemToInventoryWithGUIupdate(new Item(enemy.getItems().get(iter)), guiRenderer);
						}
					}
				}
				
				//TASK KILL
				for(Quest q1: this.getQuests()) {
					for(Task t1:q1.getTasks()) {
						if(t1.getState() == doState.kill) {
							if(t1.getDoItemID() == enemy.getGlobalID()) {
								t1.increaseHowMuchIsDone();
							}
						}
					}
					if(q1.isEndedQuest() && !q1.isRewardedYet()) {
						System.out.println("Killin great?");
						q1.sendReward(this, guiRenderer, world, 0);
						guiRenderer.getStatsContainer().updatePlayerInventory(guiRenderer, this);
					}
				}
				
				//Last line (everything should be done before it)
				world.removeAndRespawn(enemy);
			}else {
				fightString.add(this.getName()+" "+LanguageLoader.getLanguageLoader().getValue("fightSystemHpBefore")+" " + this.getHP().GetHP());
				int enemyAttcc = (enemy).GetRandomAttack();
				int attack = ((enemyAttcc-this.getPlayerDefense())>0)?(enemyAttcc-this.getPlayerDefense()):0;
				this.DecreasePlayerHP(attack);
				fightString.add(this.getName()+" "+LanguageLoader.getLanguageLoader().getValue("fightSystemHpAfter")+" " + this.getHP().GetHP());
				if(this.getHP().GetHP()<0) {
					fightString.add(LanguageLoader.getLanguageLoader().getValue("fightSystemPlayerDefeat")+" "+
							(enemy).getName() + " "+LanguageLoader.getLanguageLoader().getValue("fightSystemPlayerDefeat2"));
					playerDead();
					break;
				}
			}
		}
		return fightString;
	}
	
	public void playerDead() {
		Save.ReadFrom("MainSave.save");
		setIsFightWin(2);
	}
	
	public int getPlayerDefense() {
		int defense = 0;
		for(Item i: inventory.getItems()) {
			if(i.getItemInfo().getTileID() == 0 || i.getItemInfo().getTileID() == 1 ||i.getItemInfo().getTileID() == 2
					||i.getItemInfo().getTileID() == 3 ||i.getItemInfo().getTileID() == 4) {
				defense+=i.getItemInfo().getDefense();
			}
		}
		return defense;
	}
	
	public int getPlayerAttackBonus() {
		int attack = 0;
		for(Item i: inventory.getItems()) {
			if(i.getItemInfo().getTileID() == 0 || i.getItemInfo().getTileID() == 1 ||i.getItemInfo().getTileID() == 2
					||i.getItemInfo().getTileID() == 3 ||i.getItemInfo().getTileID() == 4) {
				attack+=i.getItemInfo().getAttackBonus();
			}
		}
		return attack;
	}
	
	public void addMoney(int value) {
		this.money+=value;
	}
	
	private void setLastKeysPressed(int[] keysPressed) {
		for(int i = 0; i < 4; i++) {
			lastKeysPressed[i] = keysPressed[i];
		}
	}
	
	public void SetPlayerHP(int hp) {
		this.hp.SetHP(hp);
	}
	
	public void SetPlayerXP(int xp) {
		this.xp.setXP(xp);
	}
	
	public void AddPlayerXP(int xp) {
		this.xp.AddXP(xp);
	}
	
	public void DecreasePlayerHP(int damage) {
		this.hp.DecreaseHP(damage);
	}
	
	public void AddPlayerHP(int health) {
		this.hp.AddHP(health);
	}

	public void setSetCamWithoutAnimation(boolean setCamWithoutAnimation) {
		this.setCamWithoutAnimation = setCamWithoutAnimation;
	}
	
	public void setHpXpAttack(int hp, int xp, int minAttack, int maxAttack) {
		this.hp.SetHP(hp);
		this.xp.setXP(xp);
		this.setAttack(minAttack, maxAttack);
	}
	
	public void setAttack(int minAttack, int maxAttack) {
		this.minAttack = minAttack;
		this.maxAttack = maxAttack;
	}
	
	public int getMaxAttack() {
		return maxAttack;
	}

	public void setMaxAttack(int maxAttack) {
		if(minAttack > maxAttack) {
			this.minAttack = maxAttack;
		}
		this.maxAttack = maxAttack;
	}

	public int getRandomAttack() {
		return (int)((maxAttack-minAttack)*Math.random() + minAttack);
	}
	
	public int getMinAttack() {
		return minAttack;
	}
	
	public HP getHP() {
		return hp;
	}
	
	public XP getXP() {
		return xp;
	}
	
	public void FullyRecoverHP() {
		hp.SetHP(hp.getMaxHP());
	}

	public void setMinAttack(int minAttack) {
		if(minAttack > maxAttack) {
			this.maxAttack = minAttack;
		}
		this.minAttack = minAttack;
	}
	
	public int getMoney() {
		return money;
	}
	
	public List<Quest> getQuests() {
		return quests;
	}

	public void addNewQuestNoRepeating(Quest quest) {
		int i=0;
		for(Quest q:quests) {
			if(q.getQuestID()==quest.getQuestID()) {
				i++;
			}
		}
		if(i == 0)
			this.quests.add(quest);
	}
	
	public void setQuests(List<Quest> quests) {
		this.quests = quests;
	}

	//ERROR04 -- mo¿liwa wartoœæ ujemna ale w teorii jest to mo¿liwe sooo to tak na razie zostawiam
	public void setMoney(int money) {
		this.money = money;
	}

	@Override
	public boolean isClicked() {
		return false;
	}

	public boolean isMovementLocked() {
		return movementLock;
	}

	public void setMovementLock(boolean movementLock) {
		this.movementLock = movementLock;
	}

	public void setWalkSound(Sound walk) {
		this.walk = walk;
	}
	
	public Sound getWalkSound() {
		return walk;
	}
	
	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventoryT) {
		this.inventory = inventoryT;
	}

	public String getTexturesPath() {
		return texturesPath;
	}

	public String getFileName() {
		return fileName;
	}
	
	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public void setMovement(Vector2f movement) {
		this.movement = movement;
	}
	
	public boolean isAutoMovement() {
		return autoMovement;
	}

	public int getIsFightWin() {
		return isFightWin;
	}

	public void setIsFightWin(int isFightWin) {
		this.isFightWin = isFightWin;
	}
}
