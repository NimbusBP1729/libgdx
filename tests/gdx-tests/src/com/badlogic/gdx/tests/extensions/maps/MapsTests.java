package com.badlogic.gdx.tests.extensions.maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.loaders.TmxMapLoader;
import com.badlogic.gdx.maps.loaders.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.tests.utils.GdxTest;
import com.badlogic.gdx.tests.utils.OrthoCamController;

public class MapsTests extends GdxTest {
	
	private TiledMap map;
	private TiledMapRenderer renderer;
	private OrthographicCamera camera;
	
	AssetManager assetManager;
	
	@Override
	public void create() {		
		float w = Gdx.graphics.getWidth();
		float h = Gdx.graphics.getHeight();
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, (w / h) * 320, 320);
		camera.update();
		
		OrthoCamController controller = new OrthoCamController(camera);
		Gdx.input.setInputProcessor(controller);
	
		assetManager = new AssetManager();
		assetManager.setLoader(TiledMap.class, new TmxMapLoader(new InternalFileHandleResolver()));
		assetManager.load("data/maps/tiles.tmx", TiledMap.class);
		assetManager.finishLoading();
		
		
		//map = new TmxMapLoader().load(Gdx.files.internal("data/maps/tiles.tmx"));
		map = assetManager.get("data/maps/tiles.tmx");
		renderer = new TiledMapRenderer(map);		
	}
	
	@Override
	public void render() {
		Gdx.gl.glClearColor(100f / 255f, 100f / 255f, 250f / 255f, 1f);
		Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
		camera.update();
		renderer.render(camera);
	}
	
}
