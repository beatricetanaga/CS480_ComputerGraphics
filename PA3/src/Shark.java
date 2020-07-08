import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.gl2.GLUT;
import java.util.*;

public class Shark extends Component implements Animate{
	
	// Handles for different display lists
	protected int sharkHandle;
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
	
	private double bodyLength2 = bodyRadius*2.5;
	private double tailLength2 = 0.1 + tailRadius*5.5*Math.sin(Math.PI/4);
	private double sphereRadius = (bodyLength2 + tailLength2)*0.5;
	
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
	
//	private ArrayList<Configuration> configurations;
	Point3D startPoint;
	Point3D targetPoint;
	
	int frameCount = 0;
	private GLUT glut;
	private double speed;
	
	public Shark(Point3D p, GLUT glut, double speed) {
		
		super(new Point3D(p));
		
		this.glut = glut;
		this.speed = speed;
		
		startPoint = position();
		
		this.setYNegativeExtent(-30);
		this.setYPositiveExtent(30);
		
		this.setExtentSwitch(false);
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
			gl.glScaled(3, 1, 7);
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
			gl.glTranslated(0, 0.3, 0);
			gl.glScaled(3.5, 7, 1);
			glut.glutSolidSphere(dorsalRadius, 12, 12);
		gl.glPopMatrix();
		gl.glEndList();

		// Tail
		rightTail = gl.glGenLists(1);
		gl.glNewList(rightTail, GL2.GL_COMPILE);
			gl.glPushMatrix();
				gl.glTranslated(-1, 0.1, 0.1);
				gl.glRotated(45, 0, 1, 0);
				gl.glRotated(45, 0, 0, 1);
				gl.glScaled(2.5, 5.5, 1);
				glut.glutSolidSphere(tailRadius, 12, 12);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslated(-1, -0.1, 0.1);
				gl.glRotated(45, 0, 1, 0);
				gl.glRotated(-45, 0, 0, 1);
				gl.glScaled(1.7, 3.5, 1);
				glut.glutSolidSphere(tailRadius, 12, 12);
			gl.glPopMatrix();
		gl.glEndList();
		
		midTail = gl.glGenLists(1);
		gl.glNewList(midTail, GL2.GL_COMPILE);
			gl.glPushMatrix();
				gl.glTranslated(-1, 0.1, 0);
				gl.glRotated(45, 0, 0, 1);
				gl.glScaled(2.5, 5.5, 1);
				glut.glutSolidSphere(tailRadius, 12, 12);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslated(-1, -0.1, 0);
				gl.glRotated(-45, 0, 0, 1);
				gl.glScaled(1.7, 3.5, 1);
				glut.glutSolidSphere(tailRadius, 12, 12);
			gl.glPopMatrix();
		gl.glEndList();
		
		leftTail = gl.glGenLists(1);
		gl.glNewList(leftTail, GL2.GL_COMPILE);
			gl.glPushMatrix();
				gl.glTranslated(-1, 0.1, -0.1);
				gl.glRotated(-45, 0, 1, 0);
				gl.glRotated(45, 0, 0, 1);
				gl.glScaled(2.5, 5.5, 1);
				glut.glutSolidSphere(tailRadius, 12, 12);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslated(-1, -0.1, -0.1);
				gl.glRotated(-45, 0, 1, 0);
				gl.glRotated(-45, 0, 0, 1);
				gl.glScaled(1.7, 3.5, 1);
				glut.glutSolidSphere(tailRadius, 12, 12);
			gl.glPopMatrix();
		gl.glEndList();
		
		// Body
		body = gl.glGenLists(1);
		gl.glNewList(body, GL2.GL_COMPILE);
		gl.glPushMatrix();
			gl.glScaled(2.5, 1, 1);
			glut.glutSolidSphere(bodyRadius, 12, 12);
		gl.glPopMatrix();
		gl.glEndList();
		
		// Draw all of the components
		gl.glNewList(sharkHandle, GL2.GL_COMPILE);
		gl.glPushMatrix();
//			gl.glTranslated(xLocation, yLocation, zLocation);
			gl.glScaled(0.75, 0.75, 0.75);
			gl.glColor3d(0.6, 0.6, 0.6);
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
			gl.glScaled(0.75, 0.75, 0.75);
			gl.glColor3d(0.6, 0.6, 0.6);
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

	@Override
	public void setModelStates(ArrayList<Configuration> configList) {
//		configurations = configList;
		
		if (configList.size() > 1) {
			this.setConfiguration(configList.get(0));
		}
	}
	
	public boolean nextState() {
		return targetPoint == null;
	}
	
	public Point3D directionToFace() {
		
		Point3D start = position();
		Point3D end = targetPoint;

		if (targetPoint == null) {
			end = new Point3D(start.x()+1,start.y(),start.z());
		}
		
		double b1 = end.x()-start.x();
		double b2 = end.y()-start.y();
		double b3 = end.z()-start.z();
		
		Point3D direction = new Point3D(b1,b2,b3);
		return direction;
	}
	
	void setFaceDirection(GL2 gl) {
		
		final double epsilon = 1e-7;
		
		double a1 = 1;
		double a2 = 0;
		double a3 = 0;
		
		Point3D direction = directionToFace();
		
		double b1 = direction.x();
		double b2 = direction.y();
		double b3 = direction.z();
		
		double bLength = Math.sqrt(b1*b1+b2*b2+b3*b3);
		// Check if start and end position are the same
		if (bLength < epsilon) {
			return;
		} 
		
		// Make b a unit length vector to get direction it should be facing
		b1 /= bLength;
		b2 /= bLength;
		b3 /= bLength;
		
		// If intended direction is either the original position or the opposite
		if (Math.abs(b2) < epsilon && Math.abs(b3) < epsilon) {
			if (b1 < 0) {
				gl.glRotated(180, 0, 1, 0);
			} 
			return;
		}
		
		if (b1 < 0) {
			a1 = -1;
		}
		
		// Computing cross product to find axis to rotate around
		double s1 = a2*b3 - a3*b2;
		double s2 = a3*b1 - a1*b3;
		double s3 = a1*b2 - a2*b1;
		
		// Angle at which to rotate
		double sLength = Math.sqrt(s1*s1+s2*s2+s3*s3);
		double angle = Math.asin(sLength)*180/Math.PI;
		
		
		gl.glRotated(angle, s1, s2, s3);
		
		if (b1 < 0) {
			gl.glRotated(180, 0, 1, 0);
		}
	}
	
	public void setNextConfig(Configuration config) {
		frameCount = 0;
		targetPoint = config.position();
		startPoint = position();
	}
	
	public double sphereRadius() {
		return sphereRadius;
	}
	
	final double NUMFRAMES = 100;
	
	@Override
	public void animationUpdate(GL2 gl) {
		frameCount+= speed;
		if (frameCount >= NUMFRAMES) {
			frameCount = 0;
			startPoint = targetPoint;
			targetPoint = null;
		}
		
		// Interpolation 
		double alpha = frameCount/NUMFRAMES;
		Point3D p;
		
		double x;
		double y;
		double z;
		
		if (targetPoint == null) {
			x = position().x();
			y = position().y();
			z = position().z();
		}
		else {
			x = alpha*targetPoint.x()+(1-alpha)*startPoint.x();
			y = alpha*targetPoint.y()+(1-alpha)*startPoint.y();
			z = alpha*targetPoint.z()+(1-alpha)*startPoint.z();
		}
		
		// Linear Interpolation
		p = new Point3D(x,y,z);
		this.setConfiguration(new BaseConfiguration(p));

		if (this.checkRotationReachedExtent(Axis.Y)) {
			rotateSpeed = -rotateSpeed;
		}
		
		this.rotate(Axis.Y, rotateSpeed);
	}
	
}
