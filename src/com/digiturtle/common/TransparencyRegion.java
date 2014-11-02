package com.digiturtle.common;

import java.awt.Color;
import java.awt.Point;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;

import com.digiturtle.ui.Theme;


public class TransparencyRegion {
	
	private static HashMap<String, ArrayList<Point>> transparentPixels = new HashMap<String, ArrayList<Point>>();
	private static HashMap<String, Dimension> textureDimensions = new HashMap<String, Dimension>();
	
	protected ArrayList<Point> transparency;
	protected float width, height;
	
	public TransparencyRegion(String texture, Theme theme) {
		if (transparentPixels.keySet().contains(texture)) {
			transparency = transparentPixels.get(texture);
			Dimension dimension = textureDimensions.get(texture);
			width = dimension.width;
			height = dimension.height;
		} else {
			transparency = new ArrayList<Point>();
			Texture texel = null;
			try {
				texel = Texture.loadTexture(texture);
			} catch(Exception e) {
				
			}
			if (texel == null) {
				texel = theme.unmapped(texture);
			}
			Dimension dimension = new Dimension(texel.getWidth(), texel.getHeight());
			textureDimensions.put(texture, dimension);
			width = texel.getWidth();
			height = texel.getHeight();
			ByteBuffer pixels = texel._bytebuffer;
			int index = 0;
			ByteBuffer pixelBuffer = pixels.duplicate();
			while (pixelBuffer.hasRemaining()) {
				int pixel = pixelBuffer.getInt();
				Color color = new Color(pixel, true);
				if (color.getAlpha() < 150) {
					int x = index % texel.getWidth();
					int y = (index - x) / texel.getWidth();
					transparency.add(new Point(x, y));
				}
				index++;
			}
			transparentPixels.put(texture, transparency);			
		}
	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public boolean transparent(int x, int y) {
		for (Point transparent : transparency) {
			if (transparent.x == x && transparent.y == y) {
				return true;
			}
		}
		return false;
	}
	
	public boolean transparent(int x, int y, int startx, int starty) {
		return transparent(x - startx, y - starty);
	}

}
