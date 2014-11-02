package com.digiturtle.ui;

import com.digiturtle.common.ComponentRegion;
import com.digiturtle.common.StaticVBO;
import com.digiturtle.common.StaticVBO.TexCoord;
import com.digiturtle.common.StaticVBO.Vertex;
import com.digiturtle.common.Texture;

public class NinePatch {
	
	public static StaticVBO generate(float[] texcoords, ComponentRegion region, Texture texture, float[] textureSize) {
		return generate(texcoords, region, 3, 3, texture, textureSize);
	}
	
	private static void upload(Vertex[] allVertices, TexCoord[] allCoordinates, float nx, float ny, float sectionWidth, 
			float sectionHeight, float[] coords, int pointer) {
		if (pointer >= allVertices.length) return; 
		allVertices[pointer] = new Vertex(nx, ny);
		allVertices[pointer + 1] = new Vertex(nx + sectionWidth, ny);
		allVertices[pointer + 2] = new Vertex(nx + sectionWidth, ny + sectionHeight);
		allVertices[pointer + 3] = new Vertex(nx, ny + sectionHeight);
		allCoordinates[pointer] = new TexCoord(coords[0], coords[1]);
		allCoordinates[pointer + 1] = new TexCoord(coords[2], coords[1]);
		allCoordinates[pointer + 2] = new TexCoord(coords[2], coords[3]);
		allCoordinates[pointer + 3] = new TexCoord(coords[0], coords[3]);
	}
	
	protected static StaticVBO generate(float[] texcoords, ComponentRegion region, int rows, int cols, Texture texture, float[] textureSize) {
		float sectionWidth = textureSize[0] / cols * (texcoords[2] - texcoords[0]);
		float sectionHeight = textureSize[1] / rows * (texcoords[3] - texcoords[1]);
		float x0 = texcoords[0], x1 = texcoords[0] + (texcoords[2] - texcoords[0]) / 3, 
				x2 = texcoords[0] + 2 * (texcoords[2] - texcoords[0]) / 3, x3 = texcoords[2];
		float y0 = texcoords[1], y1 = texcoords[1] + (texcoords[3] - texcoords[1]) / 3, 
				y2 = texcoords[1] + 2 * (texcoords[3] - texcoords[1]) / 3, y3 = texcoords[3];
		float[] topleftCoords = new float[]{ x0, y0, x1, y1 };
		float[] topCoords = new float[]{ x1, y0, x2, y1 };
		float[] topRightCoords = new float[]{ x2, y0, x3, y1 };
		float[] leftCoords = new float[]{ x0, y1, x1, y2 };
		float[] centerCoords = new float[]{ x1, y1, x2, y2 };
		float[] rightCoords = new float[]{ x2, y1, x3, y2 };
		float[] bottomLeftCoords = new float[]{ x0, y2, x1, y3 };
		float[] bottomCoords = new float[]{ x1, y2, x2, y3 };
		float[] bottomRightCoords = new float[]{ x2, y2, x3, y3 };
		int cellCountWidth = (int) Math.floor(region.width / sectionWidth) - 1;
		int cellCountHeight = (int) Math.floor(region.height / sectionHeight);
		StaticVBO resultant = new StaticVBO(cellCountWidth * cellCountHeight * 4 + ((cellCountHeight + cellCountWidth + 2) * 4), texture.getID());
		int pointer = 0;
		System.out.println(sectionWidth + "x" + sectionHeight + " -- " + cellCountWidth + "x" + cellCountHeight);
		Vertex[] allVertices = new Vertex[cellCountWidth * cellCountHeight * 4 + ((cellCountHeight + cellCountWidth + 2) * 4)];
		TexCoord[] allCoordinates = new TexCoord[cellCountWidth * cellCountHeight * 4 + ((cellCountHeight + cellCountWidth + 2) * 4)];
		System.out.println(allVertices.length);
		try {	// Upload the top row
			float widthOverall = 0;
			float nx = region.x + sectionWidth, ny = region.y;
			while (widthOverall < (region.width - (2 * sectionWidth))) {
				upload(allVertices, allCoordinates, nx, ny, sectionWidth, sectionHeight, topCoords, pointer);
				nx += sectionWidth;
				widthOverall += sectionWidth;
				pointer += 4; System.out.println("Top block");
			}
		} catch (Exception e) { e.printStackTrace(); }
		try {	// Upload the left column
			float heightOverall = 0;
			float nx = region.x, ny = region.y + sectionHeight;
			while (heightOverall < (region.height - (2 * sectionHeight))) {
				upload(allVertices, allCoordinates, nx, ny, sectionWidth, sectionHeight, leftCoords, pointer);
				ny += sectionHeight;
				heightOverall += sectionHeight;
				pointer += 4; System.out.println("Left block");
			}
		} catch (Exception e) { e.printStackTrace(); }
		try {	// Upload the right column
			float heightOverall = 0;
			float nx = region.x + region.width - sectionWidth, ny = region.y + sectionHeight;
			while (heightOverall < (region.height - (2 * sectionHeight))) {
				upload(allVertices, allCoordinates, nx, ny, sectionWidth, sectionHeight, rightCoords, pointer);
				ny += sectionHeight;
				heightOverall += sectionHeight;
				pointer += 4; System.out.println("Right block");
			}
		} catch (Exception e) { e.printStackTrace(); }
		try {	// Upload the bottom row
			float widthOverall = 0;
			float nx = region.x + sectionWidth, ny = region.y + region.height - sectionHeight;
			while (widthOverall < (region.width - (2 * sectionWidth))) {
				upload(allVertices, allCoordinates, nx, ny, sectionWidth, sectionHeight, bottomCoords, pointer);
				nx += sectionWidth;
				widthOverall += sectionWidth;
				pointer += 4; System.out.println("Bottom block");
			}
		} catch (Exception e) { e.printStackTrace(); }
		try {	// Upload the top left
				float nx = region.x, ny = region.y;
				upload(allVertices, allCoordinates, nx, ny, sectionWidth, sectionHeight, topleftCoords, pointer);
				pointer += 4; System.out.println("Top left block");
		} catch (Exception e) { e.printStackTrace(); }
		try {	// Upload the top right
				float nx = region.x + region.width - sectionWidth, ny = region.y;
				upload(allVertices, allCoordinates, nx, ny, sectionWidth, sectionHeight, topRightCoords, pointer);
				pointer += 4; System.out.println("Top right block");
		} catch (Exception e) { e.printStackTrace(); }
		try {	// Upload the bottom left
				float nx = region.x, ny = region.y + region.height
						- sectionHeight;
				upload(allVertices, allCoordinates, nx, ny, sectionWidth, sectionHeight, bottomLeftCoords, pointer);
				pointer += 4; System.out.println("Bottom left block");
		} catch (Exception e) { e.printStackTrace(); }
		try {	// Upload the bottom right
				float nx = region.x + region.width - sectionWidth, ny = region.y
						+ region.height - sectionHeight;
				upload(allVertices, allCoordinates, nx, ny, sectionWidth, sectionHeight, bottomRightCoords, pointer);
				pointer += 4; System.out.println("Bottom right block");
		} catch (Exception e) { e.printStackTrace(); }
		try {	// Upload the center tiles
			float heightOverall = 0, widthOverall;
			float nx = region.x + sectionWidth, ny = region.y + sectionHeight;
			while (heightOverall <= (region.height - (2 * sectionHeight))) {
				widthOverall = 0;
				nx = region.x + sectionWidth;
				while (widthOverall < (region.width - (2 * sectionWidth))) {
					if (pointer >= allVertices.length) break; 
					upload(allVertices, allCoordinates, nx, ny, sectionWidth, sectionHeight, centerCoords, pointer);
					pointer += 4;
					nx += sectionWidth;
					widthOverall += sectionWidth;
				}
				ny += sectionHeight;
				heightOverall += sectionHeight;
				System.out.println("Center block");
			}
			widthOverall = 0;
			nx = region.x + sectionWidth;
			ny += sectionHeight;
//			while (widthOverall < (region.width - (2 * sectionWidth))) {
//				if (pointer >= allVertices.length) break; 
//				upload(allVertices, allCoordinates, nx, ny, sectionWidth, sectionHeight / 2, centerCoords, pointer);
//				pointer += 4;
//				nx += sectionWidth;
//				widthOverall += sectionWidth;
//			}
//			heightOverall += sectionHeight;
//			System.out.println("Center block");
		} catch (Exception e) { e.printStackTrace(); }
		int nonnull = 0;
		for (Vertex vertex : allVertices) if (vertex != null) nonnull++;
		Vertex[] vertices = new Vertex[nonnull];
		int iPointer = 0;
		for (Vertex vertex : allVertices) {
			if (vertex != null) {
				vertices[iPointer] = vertex;
				iPointer++;
			}
		}
		resultant.uploadVertices(vertices);
		nonnull = 0;
		for (TexCoord texcoord : allCoordinates) if (texcoord != null) nonnull++;
		TexCoord[] texCoords = new TexCoord[nonnull];
		iPointer = 0;
		for (TexCoord texCoord : allCoordinates) {
			if (texCoord != null) {
				texCoords[iPointer] = texCoord;
				iPointer++;
			}
		}
		resultant.uploadTextures(texCoords);
		return resultant;
	}

}
