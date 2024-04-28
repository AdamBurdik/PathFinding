package me.adamix.pf.pathfinder;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import me.adamix.pf.Game;

public class SearchTile {

	private Vector2 pos;

	private int gCost; // Distance from starting tile
	private int hCost; // distance from end node
	private int fCost; // gCost + hCost
	private boolean isEnd = false;
	private boolean isStart = false;
	private Vector2 parentTilePos;

	public SearchTile(Vector2 pos, int gCost, int hCost, Vector2 parentTilePos) {
		this.pos = pos;
		this.gCost = gCost;
		this.hCost = hCost;
		this.fCost = hCost + gCost;
		this.parentTilePos = parentTilePos;
	}

	public int getGCost() {
		return gCost;
	}

	public int getHCost() {
		return hCost;
	}

	public int getFCost() {
		return fCost;
	}

	public void setStart() {
		this.isStart = true;
	}

	public void setEnd() {
		this.isEnd = true;
	}

	public boolean isStart() {
		return isStart;
	}

	public Vector2 getParentTilePos() {
		return this.parentTilePos;
	}

	public void update(int gCost, Vector2 parentTilePos) {
		this.gCost = Math.min(this.gCost, gCost);
		if (this.gCost + this.hCost < this.fCost) {
			this.parentTilePos = parentTilePos;
			this.fCost = this.gCost + this.hCost;
			System.out.println("UPDATED: " + pos);
		}
	}

	public void render(boolean isExplored) {
		int screenX = (int) (this.pos.x * Game.tileSize);
		int screenY = (int) (this.pos.y * Game.tileSize);
//		Game.font.setColor(Color.BLACK);
//		Game.font.draw(Game.batch, String.valueOf(gCost), screenX + 10, screenY - 10 + Game.tileSize);
//		Game.font.setColor(Color.BLUE);
//		Game.font.draw(Game.batch, String.valueOf(hCost), screenX + 60, screenY - 10 + Game.tileSize);
//		Game.font.setColor(Color.GREEN);
//		Game.font.draw(Game.batch, String.valueOf(fCost), screenX + 30, screenY - 40 + Game.tileSize);
//		Game.font.setColor(Color.BLUE);
//		Game.font.getData().setScale(0.8f);
//		Game.font.draw(Game.batch, String.valueOf(parentTilePos), screenX + 15, screenY - 40 + Game.tileSize);
//		if (isExplored) {
//			Game.font.setColor(Color.RED);
//			Game.font.draw(Game.batch, String.valueOf("true"), screenX + 30, screenY - 60 + Game.tileSize);
//		}

	}

	public Vector2 getPos() {
		return this.pos;
	}

}
