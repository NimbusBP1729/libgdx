package com.badlogic.gdx.maps.objects;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class TextureMapObject {
	
	private TextureRegion textureRegion;

	public TextureRegion getTextureRegion() {
		return textureRegion;
	}
	
	public TextureMapObject(TextureRegion textureRegion) {
		super();
		this.textureRegion = textureRegion;
	}
	
}
