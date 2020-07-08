//****************************************************************************
// SketchBase.  
//****************************************************************************
// Comments : 
//   Subroutines to manage and draw points, lines an triangles
//
// History :
//   Aug 2014 Created by Jianming Zhang (jimmie33@gmail.com) based on code by
//   Stan Sclaroff (from CS480 '06 poly.c)

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class SketchBase 
{
	public SketchBase()
	{
		// deliberately left blank
	}
	
	// draw a point
	public static void drawPoint(BufferedImage buff, Point2D p)
	{
		buff.setRGB(p.x, buff.getHeight()-p.y-1, p.c.getBRGUint8());
	}
	
	//////////////////////////////////////////////////
	//	Implement the following two functions
	//////////////////////////////////////////////////
	
	// Helper to find square for distance between 2 points
	private static float square (float x) {
		return x*x;
	}
	
	// Finds distance between 2 points for color interpolation
	private static float distance (Point2D p1, Point2D p2) {
		float dist = (float)Math.sqrt((double)(square(p2.x-p1.x)+square(p2.y-p1.y)));
		return dist;
	}
	
	// Color interpolation, mid gets the color closer to the point its closer to
	public static void colorSmooth(Point2D p1, Point2D p2, Point2D mid) {
		
		float totalDist = distance(p1, p2);
		float midDist = distance(mid, p2);
		
		float w1 = midDist/totalDist;
		float w2 = 1-w1;
		ColorType c1 = p1.c;
		ColorType c2 = p2.c;
		ColorType mc = mid.c;
		mc.r = c1.r*w1+c2.r*w2;
		mc.g = c1.g*w1+c2.g*w2;
		mc.b = c1.b*w1+c2.b*w2;
	}
	
	// Get color first then draw point of that color
	private static void drawPointColor(BufferedImage buff, Point2D p1, Point2D p2, Point2D mid) {
		colorSmooth(p1, p2, mid);
		drawPoint(buff, mid);
	}
	
	// 4 cases to handle, according to the Bresenham’s algorithm //
	// Top left to bottom right
	public static void smallPos(BufferedImage buff, Point2D p1, Point2D p2) {
		int dx = p2.x - p1.x;
		int dy = p2.y - p1.y;
		int y = p1.y;
		int eps = 0;
		Point2D temp = new Point2D();
		
		for (int x = p1.x; x <= p2.x; x++) {
			temp.x = x;
			temp.y = y;
			drawPointColor(buff, p1, p2, temp);
			
			eps += dy;
			
			if (2*eps >= dx) {
				y++;
				eps -= dx;
			}
		}
	}
	// Bottom left to top right
	public static void smallNeg(BufferedImage buff, Point2D p1, Point2D p2) {
		int dx = p2.x - p1.x;
		int dy = -(p2.y - p1.y);
		int y = p1.y;
		int eps = 0;
		Point2D temp = new Point2D();
		
		for (int x = p1.x; x <= p2.x; x++) {
			temp.x = x;
			temp.y = y;
			drawPointColor(buff, p1, p2, temp);
			
			eps += dy;
			
			if (2*eps >= dx) {
				y--;
				eps -= dx;
			}
		}
	}
	// Top right to bottom left
	public static void bigPos(BufferedImage buff, Point2D p1, Point2D p2) {
		int dx = p2.x - p1.x;
		int dy = p2.y - p1.y;
		int x = p1.x;
		int eps = 0;
		Point2D temp = new Point2D();
		
		for (int y = p1.y; y <= p2.y; y++) {
			temp.x = x;
			temp.y = y;
			drawPointColor(buff, p1, p2, temp);
			
			eps += dx;
			
			if (2*eps >= dy) {
				x++;
				eps -= dy;
			}
		}
	}
	// Bottom right to top left
	public static void bigNeg(BufferedImage buff, Point2D p1, Point2D p2) {
		if (p1.y > p2.y) {
			Point2D temp = p1;
			p1 = p2;
			p2 = temp;
		}
		int dx = -(p2.x - p1.x);
		int dy = p2.y - p1.y;
		int x = p1.x;
		int eps = 0;
		Point2D temp = new Point2D();
		
		for (int y = p1.y; y <= p2.y; y++) {
			temp.x = x;
			temp.y = y;
			drawPointColor(buff, p1, p2, temp);
			
			eps += dx;
			
			if (2*eps >= dy) {
				x--;
				eps -= dy;
			}
		}
	}
	
	// Line function
	public static void drawLine(BufferedImage buff, Point2D p1, Point2D p2)
	{
		if (p1.x > p2.x) {
			Point2D temp = p1;
			p1 = p2;
			p2 = temp;
		}
		
		// replace the following line with your implementation
		//drawPoint(buff, p2);
		int dx = p2.x - p1.x;
		int dy = p2.y - p1.y;
		
		// Takes care of positive slope
		if (dy >= 0) {
			if (dy <= dx) {
				smallPos(buff, p1, p2);
				return;
			}
			
			if (dy > dx) {
				bigPos(buff, p1, p2);
				return;
			}
		}
		
		// Takes care of negative slope
		if (dy < 0) {
			if (-dy <= dx) {
				smallNeg(buff, p1, p2);
				return;
			}
			
			if (-dy > dx) {
				bigNeg(buff, p1, p2);
				return;
			}
		}
	}

	// Helper functions to split triangle into top and bottom flat base triangles
	private static void flatTopTriangle(BufferedImage buff, Point2D p1, Point2D p2, Point2D p3) {
		float invslope1 = ((float)p3.x-p1.x)/(p3.y-p1.y);
		float invslope2 = ((float)p3.x-p2.x)/(p3.y-p2.y);
		
		float curx1 = p3.x;
		float curx2 = p3.x;
		
		Point2D leftPoint = new Point2D();
		Point2D rightPoint = new Point2D();
		
		for (int lineY = p3.y; lineY > p1.y; lineY--) {
			leftPoint.x = (int)curx1;
			leftPoint.y = lineY;
			colorSmooth(p1, p3, leftPoint);
			
			rightPoint.x = (int)curx2;
			rightPoint.y = lineY;		
			colorSmooth(p2, p3, rightPoint);
			
			drawLine(buff, leftPoint, rightPoint);
			curx1 -= invslope1;
			curx2 -= invslope2;
		}
	}
	
	private static void flatBottomTriangle(BufferedImage buff, Point2D p1, Point2D p2, Point2D p3) {
		float invslope1 = ((float)p2.x-p1.x)/(p2.y-p1.y);
		float invslope2 = ((float)p3.x-p1.x)/(p3.y-p1.y);
		
		float curx1 = p1.x;
		float curx2 = p1.x;
		
		Point2D leftPoint = new Point2D();
		Point2D rightPoint = new Point2D();
		
		for (int lineY = p1.y; lineY <= p2.y; lineY++) {
			leftPoint.x = (int)curx1;
			leftPoint.y = lineY;
			colorSmooth(p1, p2, leftPoint);
			
			rightPoint.x = (int)curx2;
			rightPoint.y = lineY;
			colorSmooth(p1, p3, rightPoint);
			
			drawLine(buff, leftPoint, rightPoint);
			curx1 += invslope1;
			curx2 += invslope2;
		}
	}
	
	// Triangle function
	public static void drawTriangle(BufferedImage buff, Point2D p1, Point2D p2, Point2D p3, boolean do_smooth)
	{
		ColorType c2 = p2.c;
		ColorType c3 = p3.c;
		if(!do_smooth) {
			p2.c = p1.c;
			p3.c = p1.c;
		}
		
		Comparator<Point2D> compareById = new Comparator<Point2D>() {
		    @Override
		    public int compare(Point2D p1, Point2D p2) {
		    	if (p1.y != p2.y) {
		    		return Float.compare(p1.y, p2.y);
		    	}
		    	return Float.compare(p1.x, p2.x);
		    }
		};
		
		ArrayList<Point2D> points = new ArrayList<>();
		points.add(p1);
		points.add(p2);
		points.add(p3);
		Collections.sort(points, compareById);
		
		Point2D v1 = points.get(0);
		Point2D v2 = points.get(1);
		Point2D v3 = points.get(2);

		if (v1.y == v2.y) {
			flatTopTriangle(buff, v1, v2, v3);
		}
		else if (v2.y == v3.y) {
			flatBottomTriangle(buff, v1, v2, v3);
		}
		else {
			Point2D v4 = new Point2D();
			v4.y = v2.y;
			v4.x = (int)(v1.x + ((float)(v2.y-v1.y)/(v3.y-v1.y))*(v3.x-v1.x));
			colorSmooth(v1, v3, v4);
			flatTopTriangle(buff, v2, v4, v3);
			flatBottomTriangle(buff, v1, v2, v4);
		}
		p2.c = c2;
		p3.c = c3;
	}
	
	
	/////////////////////////////////////////////////
	// for texture mapping (Extra Credit for CS680)
	/////////////////////////////////////////////////
	public static void triangleTextureMap(BufferedImage buff, BufferedImage texture, Point2D p1, Point2D p2, Point2D p3)
	{
		// replace the following line with your implementation
		drawPoint(buff, p3);
	}
}
