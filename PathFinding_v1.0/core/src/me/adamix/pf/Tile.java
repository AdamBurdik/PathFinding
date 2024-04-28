package me.adamix.pf;

import com.badlogic.gdx.math.Vector2;

public class Tile {

	private Vector2 pos;
	private int tileId;

	public Tile(Vector2 pos, int tileId) {
		this.pos = pos;
		this.tileId = tileId;
	}

	public void render() {
		Game.batch.draw(Game.getTexture(tileId), pos.x * Game.tileSize, pos.y * Game.tileSize, Game.tileSize, Game.tileSize);
	}

	public void setId(int tileId) {
		this.tileId = tileId;
	}

	public int getId() {
		return this.tileId;
	}

	public int getX() {
		return (int) this.pos.x;
	}

	public int getY() {
		return (int) this.pos.y;
	}

	public Vector2 getPos() {
		return this.pos;
	}

	public boolean isWall() {
		return tileId == 3;
	}

}
