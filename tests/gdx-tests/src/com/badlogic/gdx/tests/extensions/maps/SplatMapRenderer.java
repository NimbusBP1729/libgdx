package com.badlogic.gdx.tests.extensions.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class SplatMapRenderer implements MapRenderer {

	private Map map;
	private SpriteBatch spriteBatch;
	
	public SplatMapRenderer(Map map) {
		this.map = map;
		spriteBatch = new SpriteBatch();
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
		for (MapLayer layer : map.getLayers()) {
			renderLayer(camLeft, camTop, camRight, camBottom, layer);
		}
		spriteBatch.end();
	}

	Rectangle viewBounds = new Rectangle();
	Rectangle tempBounds = new Rectangle();
	Array<TextureMapObject> textureMapObjects = new Array<TextureMapObject>();
	public void renderLayer(float camLeft, float camTop, float camRight, float camBottom, MapLayer layer) {
		MapObjects objects = layer.getObjects();
		
		objects.getObjectsByType(TextureMapObject.class, textureMapObjects);
		
		viewBounds.set(camLeft, camBottom, camRight - camLeft, camTop - camBottom);
		
		if (textureMapObjects.size > 0) {
			for (TextureMapObject textureMapObject : textureMapObjects) {
				if (textureMapObject.getVisible()) {
					TextureRegion textureRegion = textureMapObject.getTextureRegion();
					tempBounds.set(textureMapObject.getX(), textureMapObject.getY(), textureRegion.getRegionWidth(), textureRegion.getRegionHeight());
					if (Intersector.intersectRectangles(tempBounds, viewBounds)) {
						spriteBatch.draw(textureRegion, tempBounds.x, tempBounds.y);
					}
				}
			}
		}
		
	}
}
