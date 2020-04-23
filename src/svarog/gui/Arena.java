package svarog.gui;

import java.util.ArrayList;
import java.util.List;

import svarog.entity.Entity;
import svarog.entity.Player;
import svarog.gui.font.TextBlock;

public class Arena {
	private TextureObject playerTexture;
	private TextureObject enemyTexture;
	
	private List<TextBlock> log;
	
	public Arena(Player player, Entity enemy) {
		log = new ArrayList<TextBlock>();
		this.setPlayer(player);
		this.setEnemy(enemy);
	}
	
	public void setPlayer(Player player) {
		this.playerTexture = new TextureObject(player.getPlayerTexture(Entity.Direction.up));
	}
	
	public void setEnemy(Entity enemy) {
		this.enemyTexture = new TextureObject(enemy.getTexture());
	}
	
	public void setLog(List<TextBlock> log) {
		this.log = log;
	}
	
	public void addLog(TextBlock log) {
		this.log.add(log);
	}
	
	TextureObject getPlayer() {
		return playerTexture;
	}

	TextureObject getEnemy() {
		return enemyTexture;
	}
	
	List<TextBlock> getLog() {
		return log;
	}
}
