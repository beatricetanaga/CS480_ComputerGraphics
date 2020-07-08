
import javax.media.opengl.*;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;

import com.jogamp.opengl.util.*;

import com.jogamp.opengl.util.gl2.GLUT;

import java.util.*;

public class ExampleModel extends Component implements Animate{

	private double rotateSpeed = 1;
	
	public ExampleModel(Point3D p, float scale) {
		super(new Point3D(p));
		
		Component fishBody = new Component(new Point3D(0, 0, 0), new ExampleModelDisplayable(scale));
		fishBody.setColor(new FloatColor(0.3f, 0.6f, 1f));
		this.addChild(fishBody);
		
		this.setYNegativeExtent(-30);
		this.setYPositiveExtent(30);
		
//		this.setExtentSwitch(false);
	}

	@Override
	public void setModelStates(ArrayList<Configuration> config_list) {
		if (config_list.size() > 1) {
			this.setConfiguration(config_list.get(0));
		}
	}
	
	
	@Override
	public void animationUpdate(GL2 gl) {
		
		if (this.checkRotationReachedExtent(Axis.Y)) {
			rotateSpeed = -rotateSpeed;
		}
		
		this.rotate(Axis.Y, rotateSpeed);
	}
	
}
