package com.digiturtle.common;

import java.awt.Point;
import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.digiturtle.common.Logger.LoggingSystem;
import com.digiturtle.common.StaticColoredVBO.Color;
import com.digiturtle.common.StaticColoredVBO.Vertex;

public class SplineInterpolater {
	
	private ArrayList<Point> selectedValues;
	
	public SplineInterpolater(ArrayList<Point> points) {
		selectedValues = points;
	}
	
	private ArrayList<Point> outerPoints = new ArrayList<Point>();
	private float cx, cy;
	
	public void interpolate() {
		
	}
	
	/** @author http://blog.ivank.net/interpolation-with-cubic-splines.html */
	public float evalSpline(float x, float[] xs, float[] ys, float[] ks) {
		int i = 1;
		while (xs[i]<x) i++;
		float t = (x - xs[i-1]) / (xs[i] - xs[i-1]);
		float a =  ks[i-1]*(xs[i]-xs[i-1]) - (ys[i]-ys[i-1]);
		float b = -ks[i  ]*(xs[i]-xs[i-1]) + (ys[i]-ys[i-1]);
		float q = (1-t)*ys[i-1] + t*ys[i] + t*(1-t)*(a*(1-t)+b*t);
		return q;
	}
	
	/** @author http://blog.ivank.net/interpolation-with-cubic-splines.html */
	public float[][] zeroesMat(int rows, int cols) {
		float[][] matrix = new float[rows][cols];
		for (int index = 0; index < rows; index++) {
			float[] row = new float[cols];
			for (int col = 0; col < cols; col++) {
				row[col] = 0;
			}
			matrix[index] = row;
		}
		return matrix;
	}
	
	/** @author http://blog.ivank.net/interpolation-with-cubic-splines.html */
	public float[][] swapRows(float[][] matrix, int row1, int row2) {
		float[] pointer = matrix[row1];
		matrix[row1] = matrix[row2];
		matrix[row2] = pointer;
		return matrix;
	}
	
	/** @author http://blog.ivank.net/interpolation-with-cubic-splines.html */
	public float[] getNaturalKs(float[] xs, float[] ys)	// in x values, in y values, out k values
	{
		int n = xs.length-1;
		float[][] A = zeroesMat(n+1, n+2);
			
		for(int i=1; i<n; i++)	// rows
		{
			A[i][i-1] = 1/(xs[i] - xs[i-1]);
			
			A[i][i  ] = 2 * (1/(xs[i] - xs[i-1]) + 1/(xs[i+1] - xs[i])) ;
			
			A[i][i+1] = 1/(xs[i+1] - xs[i]);
			
			A[i][n+1] = 3*( (ys[i]-ys[i-1])/((xs[i] - xs[i-1])*(xs[i] - xs[i-1]))  +  (ys[i+1]-ys[i])/ ((xs[i+1] - xs[i])*(xs[i+1] - xs[i])) );
		}
		
		A[0][0  ] = 2/(xs[1] - xs[0]);
		A[0][1  ] = 1/(xs[1] - xs[0]);
		A[0][n+1] = 3 * (ys[1] - ys[0]) / ((xs[1]-xs[0])*(xs[1]-xs[0]));
		
		A[n][n-1] = 1/(xs[n] - xs[n-1]);
		A[n][n  ] = 2/(xs[n] - xs[n-1]);
		A[n][n+1] = 3 * (ys[n] - ys[n-1]) / ((xs[n]-xs[n-1])*(xs[n]-xs[n-1]));
			
		float[] ks = solve(A);
		return ks;
	}
	
	/** @author http://blog.ivank.net/interpolation-with-cubic-splines.html */
	public float[] solve(float[][] A) {
		int m = A.length;
		float[] x = new float[m];
		for(int k=0; k<m; k++)	// column
		{
			// pivot for column
			int i_max = 0; float vali = Float.NEGATIVE_INFINITY;
			int i;
			for(i=k; i<m; i++) if(A[i][k]>vali) { i_max = i; vali = A[i][k];}
			A = swapRows(A, k, i_max);
			
			if(A[i_max][i] == 0) LoggingSystem.warn("matrix is singular!");
			
			// for all rows below pivot
			for(i=k+1; i<m; i++)
			{
				for(int j=k+1; j<m+1; j++)
					A[i][j] = A[i][j] - A[k][j] * (A[i][k] / A[k][k]);
				A[i][k] = 0;
			}
		}
		
		for(int i=m-1; i>=0; i--)	// rows = columns
		{
			float v = A[i][m] / A[i][i];
			x[i] = v;
			for(int j=i-1; j>=0; j--)	// rows
			{
				A[j][m] -= A[j][i] * v;
				A[j][i] = 0;
			}
		}
		return x;
	}
	
	public StaticColoredVBO generateVBO() {
		int vertices = outerPoints.size() * 3;
		StaticColoredVBO vbo = new StaticColoredVBO(vertices);
		vbo.setForm(GL11.GL_TRIANGLE_FAN);
		Vertex[] vertexData = new Vertex[vertices];
		int vPointer = 0;
		for (int index = 0; index < outerPoints.size() - 1; index++) {
			Point point1 = outerPoints.get(index);
			Point point2 = outerPoints.get(index + 1);
			Point point3 = new Point(Math.round(cx), Math.round(cy));
			vertexData[vPointer] = new Vertex(point1);
			vertexData[vPointer + 1] = new Vertex(point2);
			vertexData[vPointer + 2] = new Vertex(point3);
		}
		vbo.uploadColors(new Color(0, 1, 1), vertices);
		return vbo;
	}

}
