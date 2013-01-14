package com.badlogic.gdx.maps.objects;

import com.badlogic.gdx.maps.MapObject;

public class PolylineMapObject extends MapObject {

	private float[] vertices;

	public float[] getVertices() {
		return vertices;
	}
	
	public PolylineMapObject(float[] vertices) {
		super();
		this.vertices = vertices;
	}

}
