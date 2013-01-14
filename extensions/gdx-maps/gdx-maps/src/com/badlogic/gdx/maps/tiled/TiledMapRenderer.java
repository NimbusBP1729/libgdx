package com.badlogic.gdx.maps.tiled;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapRenderer;

public class TiledMapRenderer implements MapRenderer {

	private TiledMap map;
	
	private SpriteBatch spriteBatch;
	
	private float unitScale = 1;
	
	public TiledMap getMap() {
		return map;
	}
	
	public SpriteBatch getSpriteBatch() {
		return spriteBatch;
	}
	
	public float getUnitScale() {
		return unitScale;
	}
	
	public TiledMapRenderer(TiledMap map) {
		this(map, new SpriteBatch());
	}
	
	public TiledMapRenderer(TiledMap map, float unitScale) {
		this(map, new SpriteBatch(), unitScale);
	}
	
	public TiledMapRenderer(TiledMap map, SpriteBatch spriteBatch) {
		this.map = map;
		this.spriteBatch = spriteBatch;
	}
	
	public TiledMapRenderer(TiledMap map, SpriteBatch spriteBatch, float unitScale) {
		this.map = map;
		this.spriteBatch = spriteBatch;
		this.unitScale = unitScale;
	}
	
	@Override
	public void render(OrthographicCamera camera) {
		
		final float camTop = camera.position.y + camera.viewportHeight / 2 * camera.zoom;
		final float camLeft = camera.position.x - camera.viewportWidth / 2 * camera.zoom;
		final float camRight = camera.position.x + camera.viewportWidth / 2 * camera.zoom;
		final float camBottom = camera.position.y - camera.viewportHeight / 2 * camera.zoom;
		
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		for (MapLayer layer : map.getLayers()) {
			renderLayer(camLeft, camTop, camRight, camBottom, layer);
		}
		spriteBatch.end();
	}

	@Override
	public void render(OrthographicCamera camera, int[] layers) {
		final float camTop = camera.position.y + camera.viewportHeight / 2 * camera.zoom;
		final float camLeft = camera.position.x - camera.viewportWidth / 2 * camera.zoom;
		final float camRight = camera.position.x + camera.viewportWidth / 2 * camera.zoom;
		final float camBottom = camera.position.y - camera.viewportHeight / 2 * camera.zoom;
		
		spriteBatch.setProjectionMatrix(camera.combined);
		spriteBatch.begin();
		MapLayers mapLayers = map.getLayers();
		for (int i = 0; i < layers.length; i++) {
			renderLayer(camLeft, camTop, camRight, camBottom, mapLayers.getLayer(i));
		}
		spriteBatch.end();
	}
	
	protected void renderLayer(float camLeft, float camTop, float camRight, float camBottom, MapLayer layer) {
		if (layer.getVisible()) {
			spriteBatch.setColor(1, 1, 1, layer.getOpacity());
			
			if (layer instanceof TiledMapTileLayer) {
				final TiledMapTileLayer tileLayer = (TiledMapTileLayer) layer;
				
				final int layerWidth = tileLayer.getWidth();
				final int layerHeight = tileLayer.getHeight();
				
				final float layerTileWidth = tileLayer.getTileWidth() * unitScale;
				final float layerTileHeight = tileLayer.getTileHeight() * unitScale;
				
				final int x1 = Math.max(0, (int) (camLeft / layerTileWidth));
				final int x2 = Math.min(layerWidth, (int) ((camRight + layerTileWidth) / layerTileWidth));

				final int y1 = Math.max(0, (int) (camBottom / layerTileHeight));
				final int y2 = Math.min(layerHeight, (int) ((camTop + layerTileHeight) / layerTileHeight));				
				
				for (int x = x1; x < x2; x++) {
					for (int y = y1; y < y2; y++) {
						final TiledMapTileLayer.Cell cell = tileLayer.getCell(x, y);
						final TiledMapTile tile = cell.getTile();
						if (tile != null) {
							
							final boolean flipX = cell.getFlipHorizontally();
							final boolean flipY = cell.getFlipVertically();
							final boolean rotate = cell.getFlipDiagonally();
							
							TextureRegion region = tile.getTextureRegion();
							float drawX = x * layerTileWidth;
							float drawY = y * layerTileHeight;
							float width = layerTileWidth;
							float height = layerTileHeight;
							float originX = width * 0.5f;
							float originY = height * 0.5f;
							float scaleX = 1;
							float scaleY = 1;
							float rotation = 0;
							int sourceX = region.getRegionX();
							int sourceY = region.getRegionY();
							int sourceWidth = region.getRegionWidth();
							int sourceHeight = region.getRegionHeight();
							
							if (rotate) {
								if (flipX && flipY) {
									rotation = -90;
									sourceX += sourceWidth;
									sourceWidth = -sourceWidth;
								} else if (flipX) {
									rotation = -90;
									
								} else if (flipY) {
									rotation = +90;
									
								} else {
									rotation = -90;
									sourceY += sourceHeight;
									sourceHeight = -sourceHeight;
								}
							} else {
								if (flipX) {
									sourceX += sourceWidth;
									sourceWidth = -sourceWidth;
								}
								if (flipY) {
									sourceY += sourceHeight;
									sourceHeight = -sourceHeight;
								}
							}

							spriteBatch.draw(
								region.getTexture(),
								drawX,
								drawY,
								originX,
								originY,
								width,
								height,
								scaleX,
								scaleY,
								rotation,
								sourceX,
								sourceY,
								sourceWidth,
								sourceHeight,
								false,
								false
							);
							
						}
					}
				}				
			}
		}		
	}
}
