package com.badlogic.gdx.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;

public interface MapRenderer {

	public void render(OrthographicCamera camera);
	
	public void render(OrthographicCamera camera, int[] layers);

}
