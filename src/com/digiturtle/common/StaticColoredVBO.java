package com.digiturtle.common;

import static org.lwjgl.opengl.ARBBufferObject.*;
import static org.lwjgl.opengl.ARBVertexBufferObject.*;
import static org.lwjgl.opengl.GL11.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class StaticColoredVBO {
	
	private FloatBuffer vertexBuffer, colorBuffer;
	private int vertexHandle, colorHandle, vertices, form = GL11.GL_QUADS;
	
	public StaticColoredVBO(int vertices) {
		vertexBuffer = BufferUtils.createFloatBuffer(vertices * 3);
		colorBuffer = BufferUtils.createFloatBuffer(vertices * 4);
		IntBuffer intBuffer = BufferUtils.createIntBuffer(2);
		glGenBuffersARB(intBuffer);
		vertexHandle = intBuffer.get(0);
		colorHandle = intBuffer.get(1);
		intBuffer.put(0, vertexHandle);
		intBuffer.put(1, colorHandle);
		this.vertices = vertices;
	}
	
	public void setForm(int form) {
		this.form = form;
	}
	
	public StaticColoredVBO(FloatBuffer vertexBuffer, FloatBuffer colorBuffer) {
		this.vertexBuffer = vertexBuffer;
		this.colorBuffer = colorBuffer;
		IntBuffer intBuffer = BufferUtils.createIntBuffer(2);
		glGenBuffersARB(intBuffer);
		vertexHandle = intBuffer.get(0);
		colorHandle = intBuffer.get(1);
		intBuffer.put(0, vertexHandle);
		intBuffer.put(1, colorHandle);
	}
	
	public void clear() {
		vertexBuffer.clear();
		colorBuffer.clear();
	}
	
	public void addVertices(Vertex[] vertices) {
		// Upload all the vertex data
		for (Vertex vertex : vertices) {
			vertexBuffer.put(vertex.x);
			vertexBuffer.put(vertex.y);
			vertexBuffer.put(vertex.z);
		}
		vertexBuffer.flip();
		// Bind this data to the VBO
		glEnableClientState(GL_VERTEX_ARRAY);
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, vertexHandle);
	    glBufferDataARB(GL_ARRAY_BUFFER_ARB, vertexBuffer, GL_DYNAMIC_DRAW_ARB);
	    glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);
		glDisableClientState(GL_VERTEX_ARRAY);
	}
	
	public void uploadVertices(float x1, float y1, float x2, float y2) {
		Vertex v1 = new Vertex(x1, y1, 0);
		Vertex v2 = new Vertex(x2, y1, 0);
		Vertex v3 = new Vertex(x2, y2, 0);
		Vertex v4 = new Vertex(x1, y2, 0);
		uploadVertices(new Vertex[]{ v1, v2, v3, v4 });
	}
	
	public void uploadVertices(Vertex[] vertices) {
		vertexBuffer.clear();
		// Upload all the vertex data
		for (Vertex vertex : vertices) {
			vertexBuffer.put(vertex.x);
			vertexBuffer.put(vertex.y);
			vertexBuffer.put(vertex.z);
		}
		vertexBuffer.flip();
		// Bind this data to the VBO
		glEnableClientState(GL_VERTEX_ARRAY);
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, vertexHandle);
	    glBufferDataARB(GL_ARRAY_BUFFER_ARB, vertexBuffer, GL_DYNAMIC_DRAW_ARB);
	    glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);
		glDisableClientState(GL_VERTEX_ARRAY);
	}
	
	public void reuploadVertices(Vertex[] vertices) {
		vertexBuffer.clear();
		// Upload all the vertex data
		for (Vertex vertex : vertices) {
			vertexBuffer.put(vertex.x);
			vertexBuffer.put(vertex.y);
			vertexBuffer.put(vertex.z);
		}
		vertexBuffer.flip();
		// Bind this data to the VBO
		glEnableClientState(GL_VERTEX_ARRAY);
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, vertexHandle);
	    glBufferSubDataARB(GL_ARRAY_BUFFER_ARB, 0, vertexBuffer);
	    glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);
		glDisableClientState(GL_VERTEX_ARRAY);
	}
	
	public void addColors(Color[] colors) {
		// Upload all the color data
		for (Color color : colors) {
			colorBuffer.put(color.r);
			colorBuffer.put(color.g);
			colorBuffer.put(color.b);
			colorBuffer.put(color.a);
		}
		colorBuffer.flip();
		// Bind this data to the VBO
		glEnableClientState(GL_COLOR_ARRAY);
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, colorHandle);
	    glBufferDataARB(GL_ARRAY_BUFFER_ARB, colorBuffer, GL_DYNAMIC_DRAW_ARB);
	    glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);
		glDisableClientState(GL_COLOR_ARRAY);
	}
	
	public void uploadColors(Color color) {
		uploadColors(new Color[]{ new Color(color), new Color(color), new Color(color), new Color(color) });
	}
	public void uploadColors(Color color, int vertices) {
		for (int index = 0; index < vertices; index++) {
			addColors(new Color[]{ new Color(color) });
		}
	}
	public void uploadColors(Color[] colors) {
		colorBuffer.clear();
		// Upload all the color data
		for (Color color : colors) {
			colorBuffer.put(color.r);
			colorBuffer.put(color.g);
			colorBuffer.put(color.b);
			colorBuffer.put(color.a);
		}
		colorBuffer.flip();
		// Bind this data to the VBO
		glEnableClientState(GL_COLOR_ARRAY);
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, colorHandle);
	    glBufferDataARB(GL_ARRAY_BUFFER_ARB, colorBuffer, GL_DYNAMIC_DRAW_ARB);
	    glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);
		glDisableClientState(GL_COLOR_ARRAY);
	}
	
	public void reuploadColors(Color[] colors) {
		colorBuffer.clear();
		// Upload all the color data
		for (Color color : colors) {
			colorBuffer.put(color.r);
			colorBuffer.put(color.g);
			colorBuffer.put(color.b);
			colorBuffer.put(color.a);
		}
		colorBuffer.flip();
		// Bind this data to the VBO
		glEnableClientState(GL_COLOR_ARRAY);
		glBindBufferARB(GL_ARRAY_BUFFER_ARB, colorHandle);
	    glBufferSubDataARB(GL_ARRAY_BUFFER_ARB, 0, colorBuffer);
	    glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);
		glDisableClientState(GL_COLOR_ARRAY);
	}
	
	public void uploadColors(float[] rgba) {
		float[] clone = new float[4];
		for(int index = 0; index < 4; index++) {
			if (index < rgba.length) {
				clone[index] = rgba[index];
			} else {
				clone[index] = 1;
			}
		}
		Color[] colors = new Color[vertices];
		for (int index = 0; index < colors.length; index++) {
			colors[index] = new Color().upload(clone);
		}
		uploadColors(colors);
	}
	
	public void render() {
		glEnableClientState(GL_VERTEX_ARRAY);
		glEnableClientState(GL_COLOR_ARRAY);

		glBindBufferARB(GL_ARRAY_BUFFER_ARB, vertexHandle);
		glVertexPointer(3, GL_FLOAT, /* stride */3 << 2, 0L);

		glBindBufferARB(GL_ARRAY_BUFFER_ARB, colorHandle);
		glColorPointer(4, GL_FLOAT, /* stride */4 << 2, 0L);

		glDrawArrays(form, 0, vertices /* elements */);

		glBindBufferARB(GL_ARRAY_BUFFER_ARB, 0);

		glDisableClientState(GL_COLOR_ARRAY);
		glDisableClientState(GL_VERTEX_ARRAY);
	}
	
	public static class Vertex {
		
		public float x, y, z;
		public Vertex(float x, float y, float z) {
			this.x = x;
			this.y = y;
			this.z = z;
		}
		public Vertex(java.awt.Point point) {
			this(point.x, point.y, 0);
		}
		
	}
	
	public static class Color {
		
		public float r, g, b, a;
		public Color() {
			
		}
		public Color(java.awt.Color color) {
			this(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f);
		}
		public Color(Color color) {
			this(color.r, color.g, color.b, color.a);
		}
		public Color(float[] rgb) {
			this(rgb[0], rgb[1], rgb[2]);
		}
		public Color(float r, float g, float b) {
			this(r, g, b, 1); 
		}
		public Color(float r, float g, float b, float a) {
			this.r = r;
			this.g = g;
			this.b = b;
			this.a = a;
		}
		
		public Color upload(float[] rgba) {
			this.r = rgba[0];
			this.g = rgba[1];
			this.b = rgba[2];
			this.a = rgba[3];
			return this; // Return the instance
		}
		
	}

}
