import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.gl2.GLUT;
import java.util.*;

public class Fish extends Shark {
	
	// Handles for different display lists
	private int body;
	private int leftFin;
	private int rightFin;
	private int dorsalFin;
	private int rightTail;
	private int midTail;
	private int leftTail;
	
	// Dimensions
	private double bodyRadius = 0.4;
	private double bodyLength = 1;
	private double finRadius = 0.05;
	private double finLength = 0.2;
	private double dorsalRadius = 0.05;
	private double dorsalLength= 0.15;
	private double tailRadius = 0.05;
	private double tailLength = 0.2;
	
	private double bodyLength2 = bodyRadius*1.5;
	private double tailLength2 = 0.05 + tailRadius*4*Math.sin(Math.PI/4);
	private double sphereRadiusFish = (bodyLength2 + tailLength2)*0.25;
	
	// Movement angles
	private float finX;
	private float finY;
	private float tailX;
	private float tailY;
	// extra
	private float bodyX;
	private float bodyY;
	
	// Movement angles
	public  double xLocation;
	private float xDirection;
	public double yLocation;
	private float yDirection;
	public double zLocation;
	private float zDirection;
	
	public double moveSpeed;
	private double rotateSpeed = 1;
	
	private ArrayList<Configuration> configurations;
	
	int currentConfiguration = -1;
	private GLUT glut;
	
	public boolean isEaten = false;
	
	public Fish(Point3D p, GLUT glut, double speed) {
		super (p, glut, speed);
		this.glut = glut;
		
		this.setYNegativeExtent(-30);
		this.setYPositiveExtent(30);
		
//		this.setExtentSwitch(false);
	}
	
	@Override
	public double sphereRadius() {
		return sphereRadiusFish;
	}
	
	@Override
	public void initialize(final GL2 gl) {
		super.initialize(gl);
		sharkHandle = gl.glGenLists(1);
		
		// Right fin
		rightFin = gl.glGenLists(1);
		gl.glNewList(rightFin, GL2.GL_COMPILE);
		gl.glPushMatrix();
			gl.glTranslated(0, 0, 0.4);
			gl.glScaled(1, 1, 5);
			glut.glutSolidSphere(finRadius, 12, 12);
		gl.glPopMatrix();
		gl.glEndList();
		
		// Left fin
		leftFin = gl.glGenLists(1);
		gl.glNewList(leftFin, GL2.GL_COMPILE);
		gl.glPushMatrix();
			gl.glScaled(1, 1, -1);
			gl.glCallList(rightFin);
		gl.glPopMatrix();
		gl.glEndList();
		
		// Dorsal fin
		dorsalFin = gl.glGenLists(1);
		gl.glNewList(dorsalFin, GL2.GL_COMPILE);
		gl.glPushMatrix();
			gl.glTranslated(0, 0.35, 0);
			gl.glScaled(3, 4, 1);
			glut.glutSolidSphere(dorsalRadius, 12, 12);
		gl.glPopMatrix();
		gl.glEndList();

		// Tail
		rightTail = gl.glGenLists(1);
		gl.glNewList(rightTail, GL2.GL_COMPILE);
		gl.glPushMatrix();
			gl.glTranslated(-0.6, 0.05, 0);
			gl.glRotated(45, 0, 1, 0);
			gl.glRotated(45, 0, 0, 1);
			gl.glScaled(2, 4, 1);
			glut.glutSolidSphere(tailRadius, 12, 12);
		gl.glPopMatrix();
		gl.glPushMatrix();
			gl.glTranslated(-0.6, -0.05, 0);
			gl.glRotated(45, 0, 1, 0);
			gl.glRotated(-45, 0, 0, 1);
			gl.glScaled(2, 4, 1);
			glut.glutSolidSphere(tailRadius, 12, 12);
		gl.glPopMatrix();
		gl.glEndList();
		
		midTail = gl.glGenLists(1);
		gl.glNewList(midTail, GL2.GL_COMPILE);
		gl.glPushMatrix();
			gl.glTranslated(-0.6, 0.05, 0);
			gl.glRotated(45, 0, 0, 1);
			gl.glScaled(2, 4, 1);
			glut.glutSolidSphere(tailRadius, 12, 12);
		gl.glPopMatrix();
		gl.glPushMatrix();
			gl.glTranslated(-0.6, -0.05, 0);
			gl.glRotated(-45, 0, 0, 1);
			gl.glScaled(2, 4, 1);
			glut.glutSolidSphere(tailRadius, 12, 12);
		gl.glPopMatrix();
		gl.glEndList();
		
		leftTail = gl.glGenLists(1);
		gl.glNewList(leftTail, GL2.GL_COMPILE);
		gl.glPushMatrix();
			gl.glTranslated(-0.6, 0.05, 0);
			gl.glRotated(-45, 0, 1, 0);
			gl.glRotated(45, 0, 0, 1);
			gl.glScaled(2, 4, 1);
			glut.glutSolidSphere(tailRadius, 12, 12);
		gl.glPopMatrix();
		gl.glPushMatrix();
			gl.glTranslated(-0.6, -0.05, 0);
			gl.glRotated(-45, 0, 1, 0);
			gl.glRotated(-45, 0, 0, 1);
			gl.glScaled(2, 4, 1);
			glut.glutSolidSphere(tailRadius, 12, 12);
		gl.glPopMatrix();
		gl.glEndList();
		
		// Body
		body = gl.glGenLists(1);
		gl.glNewList(body, GL2.GL_COMPILE);
		gl.glPushMatrix();
			gl.glScaled(1.5, 1, 1);
			glut.glutSolidSphere(bodyRadius, 12, 12);
		gl.glPopMatrix();
		gl.glEndList();
		
		// Draw all of the components
		gl.glNewList(sharkHandle, GL2.GL_COMPILE);
		gl.glPushMatrix();
			gl.glTranslated(xLocation, yLocation, zLocation);
			gl.glScaled(0.25, 0.25, 0.25);
			gl.glColor3d(0.85, 0.35, 0.45);
			gl.glCallList(rightFin);
			gl.glCallList(leftFin);
			gl.glCallList(dorsalFin);
			gl.glCallList(body);
		
		gl.glPopMatrix();
		gl.glEndList();
	}
	
	@Override
	public void draw(GL2 gl) {
		Point3D p = position();
		gl.glPushMatrix();
			gl.glTranslated(p.x(),p.y(),p.z());
//			glut.glutWireSphere(sphereRadius(), 12, 12);
			setFaceDirection(gl);
			gl.glCallList(sharkHandle);
		gl.glPopMatrix();
		
		gl.glPushMatrix();
			gl.glTranslated(p.x(),p.y(),p.z());
			setFaceDirection(gl);
			gl.glScaled(0.25, 0.25, 0.25);
			gl.glColor3d(0.85, 0.35, 0.45);
			if(frameCount < NUMFRAMES/3.0) {
				gl.glCallList(leftTail);
			}
			else if(frameCount < NUMFRAMES*0.666) {
				gl.glCallList(midTail);
			}
			else {
				gl.glCallList(rightTail);
			}
		gl.glPopMatrix();
	}
	
}
