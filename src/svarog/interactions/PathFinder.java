package svarog.interactions;

import java.util.ArrayList;
import java.util.List;

import svarog.collision.AABB;
import svarog.entity.Player;
import svarog.world.WorldRenderer;

public class PathFinder {
	private AABB[][]coliders;
	private Node[][]nodes;
	
	private int player_x=0;
	private int player_y=0;
	
	private List<moveInstruction> moveList = new ArrayList<>();
	private int actualMove=0;
	private int isMoveEnded = 1;
	private int isWorking =0;
	
	public PathFinder() {
		test();
	}
	
	public void test() {
		moveList.add(new moveInstruction(65, 5));//left
		moveList.add(new moveInstruction(65, 2));
		moveList.add(new moveInstruction(68, 5));//right
		moveList.add(new moveInstruction(83, 3));//down
		moveList.add(new moveInstruction(87, 3));//up
		moveList.add(new moveInstruction(83, 3));//down
		moveList.add(new moveInstruction(87, 3));//up
		moveList.add(new moveInstruction(0, 0));//stop - end of moving
	}
	
	public void stupidMover(WorldRenderer worldRenderer, Player player) {
		reset();
		moveList.clear();
		isWorking = 1;
		
		System.out.println((worldRenderer.getMouseOverX()-player.getPositionX()) + "  "+ (player.getPositionY()-worldRenderer.getMouseOverY()));
		if(worldRenderer.getMouseOverX()>player.getPositionX()) {
			moveList.add(new moveInstruction(68, worldRenderer.getMouseOverX()-player.getPositionX()));
			System.out.println("right");
		}else {
			moveList.add(new moveInstruction(65, player.getPositionX()-worldRenderer.getMouseOverX()));
			System.out.println("left");
		}
		if(worldRenderer.getMouseOverY()>player.getPositionY()) {
			moveList.add(new moveInstruction(83, worldRenderer.getMouseOverY()-player.getPositionY()));
			System.out.println("down");
		}else {
			moveList.add(new moveInstruction(87, player.getPositionY()-worldRenderer.getMouseOverY()));
			System.out.println("up");
		}
		moveList.add(new moveInstruction(0, 0));
		movePlayer(player);
	}
	
	public void movePlayer(Player player) {
		
		//LEFT
		if(moveList.get(actualMove).getDirection()==65 && isMoveEnded !=0) {
			player_x = player.getPositionX() - moveList.get(actualMove).getNumberOfTiles();
			isMoveEnded--;
			player.setMovement(player.movePlayer(65, true));
		}
		//System.out.println (player.getPositionX() + "   "+ player_x);
		if(moveList.get(actualMove).getDirection()==65) {
			if(player.getPositionX()<=player_x) {
				isMoveEnded++;
				actualMove++;
			}
		}
		
		//RIGHT
		if(moveList.get(actualMove).getDirection()==68 && isMoveEnded !=0) {
			player_x = player.getPositionX() + moveList.get(actualMove).getNumberOfTiles();
			isMoveEnded--;
			player.setMovement(player.movePlayer(68, true));
		}
		if(moveList.get(actualMove).getDirection()==68) {
			if(player.getPositionX()>=player_x) {
				isMoveEnded++;
				actualMove++;
			}
		}
		
		//DOWN
		if(moveList.get(actualMove).getDirection()==83 && isMoveEnded !=0) {
			player_y = player.getPositionY() + moveList.get(actualMove).getNumberOfTiles();
			isMoveEnded--;
			player.setMovement(player.movePlayer(83, true));
		}
		if(moveList.get(actualMove).getDirection()==83) {
			if(player.getPositionY()>=player_y) {
				isMoveEnded++;
				actualMove++;
			}
		}
		
		//UP
		if(moveList.get(actualMove).getDirection()==87 && isMoveEnded !=0) {
			player_y = player.getPositionY() - moveList.get(actualMove).getNumberOfTiles();
			isMoveEnded--;
			player.setMovement(player.movePlayer(87, true));
		}
		if(moveList.get(actualMove).getDirection()==87) {
			if(player.getPositionY()<=player_y) {
				isMoveEnded++;
				actualMove++;
			}
		}
		
		//STOP
		if(moveList.get(actualMove).getDirection()==0 || player.isEntityColliding()) {
			player.setMovement(player.movePlayer(0, true));
			isWorking = 0;
			actualMove=0;
			isMoveEnded=1;
		}
	}
	
	public void reset() {
		isWorking = 0;
		actualMove=0;
		isMoveEnded=1;
	}

	public int getIsWorking() {
		return isWorking;
	}

	public void setIsWorking(int isWorking) {
		this.isWorking = isWorking;
	}
	
}

