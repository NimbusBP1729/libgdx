package com.badlogic.gdx.tests.extensions.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class ShapeMapRenderer implements MapRenderer {

	private Map map;
	private ShapeRenderer shapeRenderer;
	
	public ShapeMapRenderer(Map map) {
		this.map = map;
		shapeRenderer = new ShapeRenderer();
	}

	@Override
	public void render(OrthographicCamera camera) {
		final float camTop = camera.position.y + camera.viewportHeight / 2 * camera.zoom;
		final float camLeft = camera.position.x - camera.viewportWidth / 2 * camera.zoom;
		final float camRight = camera.position.x + camera.viewportWidth / 2 * camera.zoom;
		final float camBottom = camera.position.y - camera.viewportHeight / 2 * camera.zoom;
		
		shapeRenderer.setProjectionMatrix(camera.combined);
		for (MapLayer layer : map.getLayers()) {
			renderLayer(camLeft, camTop, camRight, camBottom, layer);
		}		
	}

	@Override
	public void render(OrthographicCamera camera, int[] layers) {
		final float camTop = camera.position.y + camera.viewportHeight / 2 * camera.zoom;
		final float camLeft = camera.position.x - camera.viewportWidth / 2 * camera.zoom;
		final float camRight = camera.position.x + camera.viewportWidth / 2 * camera.zoom;
		final float camBottom = camera.position.y - camera.viewportHeight / 2 * camera.zoom;
		
		shapeRenderer.setProjectionMatrix(camera.combined);
		for (MapLayer layer : map.getLayers()) {
			renderLayer(camLeft, camTop, camRight, camBottom, layer);
		}
	}

	Rectangle viewBounds = new Rectangle();
	Array<CircleMapObject> circleMapObjects = new Array<CircleMapObject>();
	Array<RectangleMapObject> rectangleMapObjects = new Array<RectangleMapObject>();
	Array<PolygonMapObject> polygonMapObjects = new Array<PolygonMapObject>();
	Array<PolylineMapObject> polylineMapObjects = new Array<PolylineMapObject>();
	public void renderLayer(float camLeft, float camTop, float camRight, float camBottom, MapLayer layer) {
		MapObjects objects = layer.getObjects();
		objects.getObjectsByType(CircleMapObject.class, circleMapObjects);
		objects.getObjectsByType(RectangleMapObject.class, rectangleMapObjects);
		objects.getObjectsByType(PolygonMapObject.class, polygonMapObjects);
		objects.getObjectsByType(PolylineMapObject.class, polylineMapObjects);
		
		viewBounds.set(camLeft, camBottom, camRight - camLeft, camTop - camBottom);
		
		if (circleMapObjects.size > 0) {
			shapeRenderer.begin(ShapeType.Circle);
			for (CircleMapObject circle : circleMapObjects) {
				if (circle.getVisible()) {
					Circle circleCircle = circle.getCircle();
					if (Intersector.overlapCircleRectangle(circleCircle, viewBounds)) {
						shapeRenderer.circle(circleCircle.x, circleCircle.y, circleCircle.radius);						
					}
				}
			}
			shapeRenderer.end();
		}
		if (rectangleMapObjects.size > 0) {
			shapeRenderer.begin(ShapeType.Rectangle);
			for (RectangleMapObject rectangle : rectangleMapObjects) {
				if (rectangle.getVisible()) {					
					Rectangle rectangleRectangle = rectangle.getRectangle();
					if (Intersector.intersectRectangles(rectangleRectangle, viewBounds)) {
						shapeRenderer.rect(rectangleRectangle.x, rectangleRectangle.y, rectangleRectangle.width, rectangleRectangle.height);						
					}
				}
			}
			shapeRenderer.end();
		}
		if (polygonMapObjects.size > 0 || polylineMapObjects.size > 0) {
			shapeRenderer.begin(ShapeType.Line);
			for (PolygonMapObject polygon : polygonMapObjects) {
				if (polygon.getVisible()) {
					Polygon polygonPolygon = polygon.getPolygon();
					float[] vertices = polygonPolygon.getTransformedVertices();
					if (Intersector.intersectRectangles(polygonPolygon.getBoundingRectangle(), viewBounds)); {
						for (int i = 0; i < vertices.length; i += 2) {
							shapeRenderer.line(vertices[i], vertices[i + 1], vertices[(i + 2) % vertices.length], vertices[(i + 3) % vertices.length]);							
						}
					}
				}
			}
			for (PolylineMapObject polyline : polylineMapObjects) {
				if (polyline.getVisible()) {
					Polygon polygonPolygon = polyline.getPolygon();
					float[] vertices = polygonPolygon.getTransformedVertices();
					if (Intersector.intersectRectangles(polygonPolygon.getBoundingRectangle(), viewBounds)); {
						for (int i = 0; i < vertices.length - 2; i += 2) {
							shapeRenderer.line(vertices[i], vertices[i + 1], vertices[(i + 2) % vertices.length], vertices[(i + 3) % vertices.length]);							
						}
					}
				}
			}
			shapeRenderer.end();
		}
	}
}
