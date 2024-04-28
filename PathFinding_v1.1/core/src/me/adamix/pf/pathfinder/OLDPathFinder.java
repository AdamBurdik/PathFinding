package me.adamix.pf.pathfinder;

import com.badlogic.gdx.math.Vector2;
import me.adamix.pf.Game;
import me.adamix.pf.Tile;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class OLDPathFinder {

	public final Map<Vector2, SearchTile> searchedTiles = new ConcurrentHashMap<>();
	public final Map<Vector2, SearchTile> exploredTiles = new ConcurrentHashMap<>();
	private Vector2 endTile;
	private Vector2 startTile;
	private final int maxDistance = 10000;
	private boolean isFound = false;
	public final List<Vector2> path = new ArrayList<>();
	private final boolean diagonal = false;
	private final int searchingStepDelay = 0;
	private final int pathStepDelay = 3;
	private final boolean visualSearch = false;
	private final boolean visualPath = true;

	public void nextStep(int distance) {
		if (distance < 1) {
			return;
		}
		SearchTile lowestFCostTile = getLowestFCostTile();
		if (lowestFCostTile == null) {
			return;
		}
		searchedTiles.remove(lowestFCostTile.getPos());
		exploredTiles.put(lowestFCostTile.getPos(), lowestFCostTile);
		if (visualSearch) {
			Tile gameTile = Game.getTileFromGrid(lowestFCostTile.getPos());
			gameTile.setId(5);
		}
		try {
			searchTile(lowestFCostTile, distance - 1);
		} catch (StackOverflowError e) {
			System.out.println("search is too long");
		}
	}

	// Retrace path from end to start
	private void retracePath(Vector2 pos) {

		if (path.contains(pos)) {
			return;
		}

		SearchTile searchTile = getExploredTile(pos);
		if (searchTile == null) {
			return;
		}

		path.add(searchTile.getPos());

		if (pos == startTile) {
			return;
		}

		retracePath(searchTile.getParentTilePos());
	}

	private SearchTile getExploredTile(Vector2 pos) {
		return exploredTiles.get(pos);
	}

	private void onFound() {
		isFound = true;
		searchedTiles.clear();
		retracePath(endTile);
		path.remove(0);
		exploredTiles.clear();
		for (Vector2 pos : path) {
			if (visualPath) {
				Game.getTileFromGrid(pos).setId(4);
			}
			try {
				Thread.sleep(pathStepDelay);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private void test(Vector2 startTilePos, Vector2 endTilePos) {

		List<SearchTile> searchTiles = new ArrayList<>();
		List<SearchTile> exploredTiles = new ArrayList<>();

		searchTiles.add(new SearchTile(startTilePos, 0, 0, null));

		while (!isFound) {

			// [LF] = Get lowest F cost tile

			// Search surrounding tiles of [LF] and add them to list

		}

	}

	// Update all surrounding tiles
	private void searchTile(SearchTile tile, int distance) {
		try {
			Thread.sleep(searchingStepDelay);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		if (isFound) {
			return;
		}

		if (distance < 1) {
			return;
		}

		List<Tile> surroundingTiles = getSurroundingTiles(tile.getPos());
		for (Tile surroundingTile : surroundingTiles) {
			if (surroundingTile == null) {
				continue;
			}

			if (surroundingTile.isWall()) {
				continue;
			}

			if (surroundingTile.getPos() == endTile) {
				exploredTiles.put(endTile, new SearchTile(endTile, 0, 0, tile.getPos()));
				onFound();
				return;
			}

			if (!searchedTiles.containsKey(surroundingTile.getPos()) && !exploredTiles.containsKey(surroundingTile.getPos())) {
				SearchTile searchTile = new SearchTile(
						surroundingTile.getPos(),
						tile.getGCost() + calculateDistance(tile.getPos(), surroundingTile.getPos()),
						calculateDistance(surroundingTile.getPos(),endTile),
						tile.getPos());
				searchedTiles.put(searchTile.getPos(), searchTile);
				if (visualSearch) {
					Game.getTileFromGrid(searchTile.getPos()).setId(6);
				}
			} else {
				tile.update(tile.getGCost() + calculateDistance(tile.getPos(), surroundingTile.getPos()), tile.getPos());
			}
		}

		nextStep(distance);
	}

	private SearchTile getLowestFCostTile() {
		SearchTile lowestFCostTile = null;
		for (SearchTile searchTile : searchedTiles.values()) {
			if (searchTile.getPos() == startTile) {
				continue;
			}

			if (lowestFCostTile == null) {
				lowestFCostTile = searchTile;
				continue;
			}

			if (searchTile.getFCost() < lowestFCostTile.getFCost()) {
				lowestFCostTile = searchTile;
			}
		}

		return lowestFCostTile;
	}

//	RANDOM
//	private SearchTile getLowestFCostTile() {
//		int lowestFCost = 999999;
//		List<SearchTile> lowestFCostTiles = new ArrayList<>();
//		for (SearchTile searchTile : searchedTiles.values()) {
//			if (searchTile.getPos() == startTile) {
//				continue;
//			}
//
//			if (searchTile.getFCost() <= lowestFCost) {
//				lowestFCostTiles.add(searchTile);
//				lowestFCost = searchTile.getFCost();
//			}
//		}
//
//		if (lowestFCostTiles.isEmpty()) {
//			System.out.println("ERROR");
//			return null;
//		}
//
//		int randomIdx = ThreadLocalRandom.current().nextInt(0, lowestFCostTiles.size());
//
//		return lowestFCostTiles.get(randomIdx);
//	}

	public List<Tile> startSearch(Tile startTile, Tile endTile) {
		isFound = false;
		searchedTiles.clear();
		exploredTiles.clear();
		path.clear();

		List<Tile> path = new ArrayList<>();

		if (calculateDistance(startTile.getPos(), endTile.getPos()) == 0) {
			return path;
		}

		this.endTile = endTile.getPos();
		this.startTile = startTile.getPos();

		SearchTile startSearchTile = new SearchTile(
				startTile.getPos(),
				0,
				calculateDistance(startTile.getPos(), endTile.getPos()),
				startTile.getPos());
		startSearchTile.setStart();
		searchedTiles.put(startSearchTile.getPos(), startSearchTile);
		searchTile(startSearchTile, maxDistance);

		return path;
	}

	private List<Vector2> getSurroundingPos(Vector2 pos) {
		List<Vector2> surroundingPos = new ArrayList<>();

		surroundingPos.add(new Vector2(pos.x + 1, pos.y));
		surroundingPos.add(new Vector2(pos.x - 1, pos.y));
		surroundingPos.add(new Vector2(pos.x, pos.y + 1));
		surroundingPos.add(new Vector2(pos.x, pos.y - 1));
		if (diagonal) {
			surroundingPos.add(new Vector2(pos.x + 1, pos.y - 1));
			surroundingPos.add(new Vector2(pos.x - 1, pos.y + 1));
			surroundingPos.add(new Vector2(pos.x + 1, pos.y + 1));
			surroundingPos.add(new Vector2(pos.x - 1, pos.y - 1));
		}

		return surroundingPos;

	}

	private List<Tile> getSurroundingTiles(Vector2 pos) {
		List<Tile> surroundingTiles = new ArrayList<>();

		surroundingTiles.add(Game.getTileFromGrid((int) (pos.x + 1), (int) pos.y));
		surroundingTiles.add(Game.getTileFromGrid((int) (pos.x - 1), (int) pos.y));
		surroundingTiles.add(Game.getTileFromGrid((int) pos.x, (int) (pos.y + 1)));
		surroundingTiles.add(Game.getTileFromGrid((int) pos.x, (int) (pos.y - 1)));
		if (diagonal) {
			surroundingTiles.add(Game.getTileFromGrid((int) (pos.x + 1), (int) pos.y + 1));
			surroundingTiles.add(Game.getTileFromGrid((int) (pos.x - 1), (int) pos.y - 1));
			surroundingTiles.add(Game.getTileFromGrid((int) pos.x + 1, (int) (pos.y - 1)));
			surroundingTiles.add(Game.getTileFromGrid((int) pos.x - 1, (int) (pos.y + 1)));
		}

		return surroundingTiles;
	}

	public int calculateDistance(Vector2 vector1, Vector2 vector2) {

		float distanceX = Math.abs(vector1.x - vector2.x);
		float distanceY = Math.abs(vector1.y - vector2.y);
		float diagonalDistance = Math.min(distanceX, distanceY);
		float straightDistance = Math.abs(distanceX - distanceY);

		return (int) ((diagonalDistance * Math.sqrt(2) + straightDistance) * 10);
	}

}