package me.adamix.pf;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import me.adamix.pf.pathfinder.OLDPathFinder;
import me.adamix.pf.pathfinder.PathFinder;
import me.adamix.pf.pathfinder.SearchTile;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Game extends ApplicationAdapter {
	public static SpriteBatch batch;
	public static int tileSize = 20;
	private static List<Texture> textures = new ArrayList<>();
	private static Map<Vector2, Tile> tiles = new ConcurrentHashMap<>();
	private int tileCount = 0;
	public static BitmapFont font;
	public Vector2 startTile;
	public Vector2 endTile;
	private final int visualPathDelay = 0;

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
		font = new BitmapFont(false);
		tileCount = Gdx.graphics.getWidth() / tileSize;
		loadTextures();
		generateTiles();
	}

	public void handleInput() {
		if (Gdx.input.isButtonPressed(Input.Buttons.RIGHT)) {
			Tile tile = getTileFromScreen(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			if (tile == null) {
				return;
			}
			startTile = tile.getPos();
			tile.setId(1);
		}

		if (Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
			Tile tile = getTileFromScreen(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			if (tile == null) {
				return;
			}
			endTile = tile.getPos();
			tile.setId(2);
		}
		if (Gdx.input.isButtonPressed(Input.Buttons.MIDDLE)) {
			Tile tile = getTileFromScreen(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			if (tile == null) {
				return;
			}
			tile.setId(3);
		}
		if (Gdx.input.isKeyPressed(Input.Keys.X)) {
			Tile tile = getTileFromScreen(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
			if (tile == null) {
				return;
			}
			tile.setId(0);
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
			for (Tile tile : tiles.values()) {
				if (tile.getId() == 4) {
					tile.setId(0);
				}
			}

			if (startTile == null || endTile == null) {
				return;
			}
			Thread thread = new Thread(() -> {
				List<Vector2> path = PathFinder.startSearch(startTile, endTile);
				if (path == null) {
					System.out.println("NO PATH WAS FOUND!");
					return;
				}
				for (Vector2 pos : path) {
					getTileFromGrid(pos).setId(4);
					try {
						Thread.sleep(visualPathDelay);
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
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

		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		for (Texture texture : textures) {
			texture.dispose();
		}
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
