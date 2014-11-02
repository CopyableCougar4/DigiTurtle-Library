package com.digiturtle.resource;

import java.io.InputStream;

import com.digiturtle.common.StaticVBO;
import com.digiturtle.common.Texture;
import com.digiturtle.common.StaticVBO.*;

public class VBOResource implements Resource {

	private StaticVBO vbo;
	private String name;
	private int ID, vertexCount;
	private Vertex[] vertices;
	private TexCoord[] texCoords;
	
	public VBOResource(String name, InputStream textureSource, String textureSet, String vertexSet) {
		String[] textureSetInfo = textureSet.split(";");
		TexCoord[] texCoords = new TexCoord[textureSetInfo.length];
		int index = 0;
		for (String textureSetData : textureSetInfo) {
			String[] overallData = textureSetData.split(",");
			texCoords[index++] = new TexCoord(Float.parseFloat(overallData[0]), Float.parseFloat(overallData[1]));
		}
		String[] vertexSetInfo = vertexSet.split(";");
		Vertex[] vertices = new Vertex[vertexSetInfo.length];
		index = 0;
		for (String vertexSetData : vertexSetInfo) {
			String[] overallData = vertexSetData.split(",");
			vertices[index++] = new Vertex(Float.parseFloat(overallData[0]), Float.parseFloat(overallData[1]));
		}
		{
			this.name = name;
			ID = Texture.loadTexture(name, textureSource).getID();
			this.vertices = vertices;
			this.texCoords = texCoords;
			this.vertexCount = vertices.length;
		}
	}
	public VBOResource(String name, Texture texture, TexCoord[] texCoords, Vertex[] vertices, int vertexCount) {
		this.name = name;
		ID = texture.getID();
		this.vertices = vertices;
		this.texCoords = texCoords;
		this.vertexCount = vertexCount;
	}
	
	@Override
	public void glCreate() {
		vbo = new StaticVBO(vertexCount, ID);
		vbo.uploadVertices(vertices);
		vbo.uploadTextures(texCoords);
	}
	
	public StaticVBO getVBO() {
		return vbo;
	}

	@Override
	public String getName() {
		return name;
	}

}
