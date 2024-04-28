package me.adamix.pf.pathfinder;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import me.adamix.pf.Game;

public class SearchTile {

	private Vector2 pos;

	private int gCost; // Distance from starting tile
	private int hCost; // distance from end node
	private int fCost; // gCost + hCost
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

	public Vector2 getPos() {
		return this.pos;
	}

}
