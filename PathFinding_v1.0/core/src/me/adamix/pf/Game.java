package me.adamix.pf;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import me.adamix.pf.pathfinder.PathFinder;
import me.adamix.pf.pathfinder.SearchTile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Game extends ApplicationAdapter {
	public static SpriteBatch batch;
	public static int tileSize = 40;
	private static List<Texture> textures = new ArrayList<>();
	private static Map<Vector2, Tile> tiles = new ConcurrentHashMap<>();
	private int tileCount = 0;
	private PathFinder pathFinder;
	public static BitmapFont font;
	public Vector2 startTile;
	public Vector2 endTile;

	private void loadTextures() {
		textures.add(new Texture("tile.png"));
		textures.add(new Texture("startTile.png"));
		textures.add(new Texture("endTile.png"));
		textures.add(new Texture("wallTile.png"));
		textures.add(new Texture("pathTile.png"));
		textures.add(new Texture("exploredTile.png"));
		textures.add(new Texture("searchedTile.png"));
	}

	private void generateTiles() {
		for (int y = 0; y < tileCount; y++) {
			for (int x = 0; x < tileCount; x++) {
				tiles.put(new Vector2(x, y), new Tile(new Vector2(x, y), 0));
			}
		}
	}

	@Override
	public void create () {
		batch = new SpriteBatch();
		pathFinder = new PathFinder();
		font = new BitmapFont(false);
		tileCount = Gdx.graphics.getWidth() / tileSize;
		loadTextures();
		generateTiles();
	}

	public void handleInput() {
		if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
			Tile tile = getTileFromScreen(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			startTile = tile.getPos();
			tile.setId(1);
		}

		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			Tile tile = getTileFromScreen(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			endTile = tile.getPos();
			tile.setId(2);
		}
		if (Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)) {
			Tile tile = getTileFromScreen(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			tile.setId(3);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.X)) {
			Tile tile = getTileFromScreen(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			tile.setId(0);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			if (startTile == null || endTile == null) {
				return;
			}
			Thread thread = new Thread(() -> {
				pathFinder.startSearch(tiles.get(startTile), tiles.get(endTile));
			});
			thread.start();
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
			tiles.clear();
			generateTiles();
		}
	}

	@Override
	public void render () {
		handleInput();
		ScreenUtils.clear(0, 0, 0, 1);
		batch.begin();

		for (Tile tile : tiles.values()) {
			tile.render();
		}

		for (SearchTile searchTile : pathFinder.searchedTiles.values()) {
			searchTile.render(false);
		}

		for (SearchTile searchTile : pathFinder.exploredTiles.values()) {
			searchTile.render(true);
		}

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
	}

	public static Texture getTexture(int tileId) {
		return textures.get(tileId);
	}

	public static Tile getTileFromScreen(int screenX, int screenY) {
		int tileGridX = screenX / tileSize;
		int tileGridY = screenY / tileSize;
		return tiles.get(new Vector2(tileGridX, tileGridY));
	}

	public static Tile getTileFromGrid(int gridX, int gridY) {
		return tiles.get(new Vector2(gridX, gridY));
	}

	public static Tile getTileFromGrid(Vector2 pos) {
		return tiles.get(pos);
	}
}
