package com.digiturtle.common;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.input.Cursor;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.digiturtle.common.Logger.LoggingSystem;

import static org.lwjgl.opengl.GL11.*;


// OpenGL mouse
public class OSUtil {
	
	public static void renderMouse(StaticVBO texture, int x, int y) {
		glPushMatrix();
		glTranslatef(x, y, 0);
		texture.render();
		glPopMatrix();
	}

	public static void wipeCursor() {
		try {
			Color transparent = new Color(255, 255, 255, 255);
			int rgb = transparent.getRGB();
			IntBuffer buffer = (IntBuffer) BufferUtils.createIntBuffer(1).put(rgb).flip();
			Cursor cursor = new Cursor(1, 1, 0, 0, 1, buffer, null);
			Mouse.setNativeCursor(cursor);
		} catch (LWJGLException e) {
			LoggingSystem.error("LWJGLException in wipeCursor()", e);
		}
	}
	
	public static void setIcons(String[] sources) {
		ICON_16 = sources[0];
		ICON_32 = sources[1];
		ICON_128 = sources[2];
	}
	
	public static String ICON_16, ICON_32, ICON_128;
	
	public static void setIcon() {
		switch(OS.get()) {
		case LINUX:
			setIcon(ICON_32);
			break;
		case MACOSX:
			setIcon(ICON_128);
			break;
		case WINDOWS:
			setIcon(ICON_16, ICON_32);
			break;
		}
	}
	public static void setIcon(String... sources) {
		String src16, src32, src128;
		switch(OS.get()) {
			case LINUX:
				src32 = sources[0];
				ByteBuffer iconBufferLinux = Texture.loadTexture(src32)._bytebuffer;
				Display.setIcon(new ByteBuffer[]{ iconBufferLinux });
				break;
			case MACOSX:
				src128 = sources[0];
				ByteBuffer iconBufferMacosx = Texture.loadTexture(src128)._bytebuffer;
				Display.setIcon(new ByteBuffer[]{ iconBufferMacosx });
				break;
			case WINDOWS:
				src16 = sources[0];
				src32 = sources[1];
				ByteBuffer iconBufferWindows16 = Texture.loadTexture(src16)._bytebuffer;
				ByteBuffer iconBufferWindows32 = Texture.loadTexture(src32)._bytebuffer;
				Display.setIcon(new ByteBuffer[]{ iconBufferWindows16, iconBufferWindows32 });
				break;
		}
	}
	
	public enum OS {
		WINDOWS(3, "windows"),
		LINUX(1, "linux"),
		MACOSX(2, "macosx");
		int lwjgl;
		String name;
		OS(int lwjgl, String name) {
			this.lwjgl = lwjgl;
			this.name = name;
		}
		
		public static OS get() {
			int lwjglResult = LWJGLUtil.getPlatform();
			for (OS os : values()) {
				if (os.lwjgl == lwjglResult) {
					return os;
				}
			}
			return null;
		}
	}
	
}
