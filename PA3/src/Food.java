import java.util.ArrayList;

import javax.media.opengl.GL2;

import com.jogamp.opengl.util.gl2.GLUT;

public class Food extends Component implements Animate {
	public int food;
//	public double xLocation;
	public double yLocation;
//	public double zLocation;

	private double foodRadius = 0.1;
	private double sphereRadius = foodRadius*0.5;

	private GLUT glut;
	
	public boolean isEaten = false;

	public Food(GLUT glut) {
		super(null);
		this.glut = glut;
		setNewLocation();
	}

	public void initialize( GL2 gl ) {
		GLUT glut = new GLUT();
		//display list for a simple cylinder
		food = gl.glGenLists(1);
		gl.glNewList(food, GL2.GL_COMPILE);
		gl.glPushMatrix();
		gl.glColor3d(0.45, 0.35, 0.45);
		gl.glScaled(0.5, 0.5, 0.5);
		glut.glutSolidSphere(foodRadius, 9, 9);
		gl.glPopMatrix();
		gl.glEndList();

	}
	
	public void setNewLocation() {
		double xLocation = (Math.random())*4 - 2;
		double yLocation = 1.95;
		double zLocation = (Math.random())*4 - 2;
		setPosition(new Point3D(xLocation, yLocation, zLocation));
	}

	public void update() {
		Point3D p = position();
		yLocation = p.y();
		
		yLocation -= 0.01;
		if (yLocation < -2+foodRadius) {
			yLocation = -2+foodRadius;
		}
		Point3D newPoint = new Point3D(p.x(),yLocation, p.z());
		setPosition(newPoint);
	}

	public void draw( GL2 gl ) {
		super.draw(gl);
		Point3D p = position();

		gl.glPushMatrix();
		gl.glPushAttrib( GL2.GL_CURRENT_BIT );
		gl.glTranslated(p.x(), p.y(), p.z());
		gl.glCallList(food);
		gl.glPopAttrib();
		gl.glPopMatrix();
	}
	public double sphereRadius() {
		return sphereRadius;
	}

	@Override
	public void setModelStates(ArrayList<Configuration> config_list) {
		// TODO Auto-generated method stub

	}

	@Override
	public void animationUpdate(GL2 gl) {
		// TODO Auto-generated method stub

	}
}