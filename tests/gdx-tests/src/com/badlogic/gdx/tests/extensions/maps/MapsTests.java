package com.badlogic.gdx.tests.extensions.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.Map;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.CircleMapObject;
import com.badlogic.gdx.maps.objects.PolygonMapObject;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.objects.TextureMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.tests.utils.GdxTest;
import com.badlogic.gdx.tests.utils.OrthoCamController;

public class MapsTests extends GdxTest {
	
	private TiledMap map;
	private TiledMapRenderer2 renderer;
	private OrthographicCamera camera;
	private OrthoCamController cameraController;
	
	AssetManager assetManager;
	
	private Map shapeMap;
	private Map splatMap;
	private Map superMap;
	private ShapeMapRenderer shapeMapRenderer;
	private SplatMapRenderer splatMapRenderer;
	private SuperMapRenderer superMapRenderer;
	
	Texture tiles;
	
	Texture texture;
	
	BitmapFont font;
	SpriteBatch batch;
	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, (w / h) * 320, 320);
		camera.update();
		
		cameraController = new OrthoCamController(camera);
		Gdx.input.setInputProcessor(cameraController);
	
		font = new BitmapFont();
		batch = new SpriteBatch();
		
//		assetManager = new AssetManager();
//		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
//		assetManager.load("data/maps/tiles.tmx", TiledMap.class);
//		assetManager.finishLoading();
//		map = assetManager.get("data/maps/tiles.tmx");
//		renderer = new TiledMapRenderer2(map);
	
		tiles = new Texture(Gdx.files.internal("data/maps/tiles.png"));
		{
			TextureRegion[][] splitTiles = TextureRegion.split(tiles, 32, 32);
			map = new TiledMap();
			MapLayers layers = map.getLayers();
			for (int l = 0; l < 10; l++) {
				TiledMapTileLayer layer = new TiledMapTileLayer(150, 100, 32, 32);
				for (int x = 0; x < 150; x++) {
					for (int y = 0; y < 100; y++) {
						int ty = (int)(Math.random() * splitTiles.length);
						int tx = (int)(Math.random() * splitTiles[ty].length);
						layer.setCell(x, y, new StaticTiledMapTile(splitTiles[ty][tx]));
					}
				}
				layers.addLayer(layer);
			}
			renderer = new TiledMapRenderer2(map);
		}
		
		texture = new Texture(Gdx.files.internal("data/badlogicsmall.jpg"));
		
		{
			shapeMap = new Map();
			MapLayers layers = shapeMap.getLayers();
			MapLayer layer = new MapLayer();
			MapObjects objects = layer.getObjects();
			CircleMapObject circle1 = new CircleMapObject(100, 100, 25);
			objects.addObject(circle1);
			RectangleMapObject rectangle1 = new RectangleMapObject(200, 200, 100, 50);
			objects.addObject(rectangle1);
			polygon1 = new PolygonMapObject(new float[] {100, 100, 100, 200, 200, 100});
			objects.addObject(polygon1);
			polygon = polygon1.getPolygon();
			Rectangle bounds = polygon.getBoundingRectangle();
			polygon.setOrigin(bounds.x + bounds.width / 4, bounds.y + bounds.height / 4);
			PolylineMapObject polyline1 = new PolylineMapObject(new float[] {300, 100, 350, 200, 400, 125, 500, 150});
			objects.addObject(polyline1);
			layers.addLayer(layer);
		}
		
		{		
			splatMap = new Map();
			MapLayers layers = splatMap.getLayers();
			MapLayer layer = new MapLayer();
			MapObjects objects = layer.getObjects();
			TextureMapObject texture1 = new TextureMapObject(new TextureRegion(texture));
			texture1.setX(100);
			texture1.setY(100);
			objects.addObject(texture1);
			TextureMapObject texture2 = new TextureMapObject(texture1);
			texture2.setX(texture2.getX() + 100);
			objects.addObject(texture2);
			layers.addLayer(layer);
		}
		
		{	
			superMap = new Map();
			MapLayers layers = superMap.getLayers();
			MapLayer layer = new MapLayer();
			MapObjects objects = layer.getObjects();
			TextureMapObject texture1 = new TextureMapObject(new TextureRegion(texture));
			texture1.setX(100);
			texture1.setY(100);
			objects.addObject(texture1);
			TextureMapObject texture2 = new TextureMapObject(texture1);
			texture2.setX(texture2.getX() + 100);
			objects.addObject(texture2);
			CircleMapObject circle1 = new CircleMapObject(100, 100, 25);
			objects.addObject(circle1);
			RectangleMapObject rectangle1 = new RectangleMapObject(200, 200, 100, 50);
			objects.addObject(rectangle1);
			polygon1 = new PolygonMapObject(new float[] {100, 100, 100, 200, 200, 100});
			objects.addObject(polygon1);
			polygon = polygon1.getPolygon();
			Rectangle bounds = polygon.getBoundingRectangle();
			polygon.setOrigin(bounds.x + bounds.width / 4, bounds.y + bounds.height / 4);
			PolylineMapObject polyline1 = new PolylineMapObject(new float[] {300, 100, 350, 200, 400, 125, 500, 150});
			objects.addObject(polyline1);
			layers.addLayer(layer);
		}
		
		shapeMapRenderer = new ShapeMapRenderer(shapeMap);
		splatMapRenderer = new SplatMapRenderer(splatMap);
		superMapRenderer = new SuperMapRenderer(superMap);
	}
	Polygon polygon;
	PolygonMapObject polygon1;
	
	float stateTime = 0;
	@Override
	public void render() {
		Gdx.gl.glClearColor(100f / 255f, 100f / 255f, 250f / 255f, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		camera.update();
		
//		stateTime += Gdx.graphics.getDeltaTime();
//		if (stateTime > 1) {
//			stateTime -= 1;
//			polygon1.setVisible(!polygon1.getVisible());
//		}
//		polygon.rotate(1);
		
		if (cameraController.dirty) {
			renderer.setProjectionMatrix(camera.combined);
			cameraController.dirty = false;
		}
		renderer.begin();
		renderer.render(camera);
		renderer.end();
		
//		shapeMapRenderer.render(camera);
//		splatMapRenderer.render(camera);
		//superMapRenderer.render(camera);
	
		batch.begin();
		font.draw(batch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 20); 
		batch.end();
	}
	
	public class OrthoCamController extends InputAdapter {
		final OrthographicCamera camera;
		final Vector3 curr = new Vector3();
		final Vector3 last = new Vector3(-1, -1, -1);
		final Vector3 delta = new Vector3();

		boolean dirty = true;
		
		public OrthoCamController (OrthographicCamera camera) {
			this.camera = camera;
		}

		@Override
		public boolean touchDragged (int x, int y, int pointer) {
			camera.unproject(curr.set(x, y, 0));
			if (!(last.x == -1 && last.y == -1 && last.z == -1)) {
				camera.unproject(delta.set(last.x, last.y, 0));
				delta.sub(curr);
				camera.position.add(delta.x, delta.y, 0);
				dirty = true;
			}
			last.set(x, y, 0);
			return false;
		}

		@Override
		public boolean touchUp (int x, int y, int pointer, int button) {
			last.set(-1, -1, -1);
			return false;
		}
	}
	
}
