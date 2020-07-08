/**
 * Arm object
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @author Zezhou Sun <micou@bu.edu>
 * @since Spring 2011
 */

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.jogamp.opengl.util.gl2.GLUT;

public class Spider extends Component implements Animate, Selection{
	/** The OpenGL utility toolkit object. */
	private final GLUT glut = new GLUT();
	

	/** The body to be modeled. */
	private final Component body;
	/** The head to be modeled. */
	private final Component head;
	/** The fingers on the hand to be modeled. */
	private final Leg[] legs;
	/** The set of all components. */
	private final List<Component> components;
	/** The set of components which are currently selected for rotation. */
	private final Set<Component> selectedComponents = new HashSet<Component>(18);
	
	
	/** The color for components which are selected for rotation. */
	public static final FloatColor ACTIVE_COLOR = FloatColor.RED;
  	/** The color for components which are not selected for rotation. */
	public static final FloatColor INACTIVE_COLOR = FloatColor.ORANGE;
	
	
	/** The radius of the components which comprise the arm. */
	public static final double JOINT_RADIUS = 0.25;
	/** The height of the distal joint on each of the fingers. */
	public static final double DISTAL_JOINT_HEIGHT = 0.2;
	/** The radius of each joint which comprises the finger. */
	public static final double SECTION_RADIUS = 0.09;
	/** The radius of the hand. */
	public static final double BODY_RADIUS = 0.4;
	public static final double HEAD_RADIUS = 0.3;
	
	/** The height of the middle joint on each of the fingers. */
	public static final double UPPER_JOINT_HEIGHT = 0.25;
	/** The height of the middle joint on each of the fingers. */
	public static final double MIDDLE_JOINT_HEIGHT = 0.25;
	/** The height of the middle joint on each of the fingers. */
	public static final double LOWER_JOINT_HEIGHT = 0.25;
	/** The height of the body. */
	public static final double BODY_HEIGHT = 0.4;
	public static final double HEAD_HEIGHT = 0.5;
	
	public static final String UPPER_SECTION_NAME = "upper section";
	public static final String MIDDLE_SECTION_NAME = "middle section";
	public static final String LOWER_SECTION_NAME = "lower section";
	

  	public static final String BODY_NAME = "body";
  	public static final String HEAD_NAME = "head";

	
  	private Component mapNum2Component(int componentNum) {
  		if (componentNum == 0)return this.body;
  		if (componentNum > 0 && componentNum <= 1+3*legs.length) {
  			/* Find out which leg */
  			int legIndex = (componentNum-1)/3;
  			/* Find out which joint */
  			int jointIndex = (componentNum-1)%3;
  			
  			return legs[legIndex].joints.get(jointIndex);
  		}
  		return null;
  	}
  	
  	private Component mapName2Component(String componentName) {
  		switch(componentName) {
  		/* create a unique name for each component of leg section respectively */
  			case BODY_NAME: return this.body;
  			case HEAD_NAME: return this.head;
  			case UPPER_SECTION_NAME: return this.legs[0].upperJoint();
  			case MIDDLE_SECTION_NAME: return this.legs[0].middleJoint();
  			case LOWER_SECTION_NAME: return this.legs[0].lowerJoint();
			default: throw new IllegalArgumentException("componentName doesn't exist "+ componentName);
  		}
  	}
  	
  	public void setModelStates(final ArrayList<Configuration> config_list) {
  		for (int i = 0; i < config_list.size(); i++) {
  			if ( 0 <= i && i <= 17) {
  				mapNum2Component(i).setAngles(config_list.get(i));
  			}
  		}
  	}
  	
  	public void setModelStates(final Map<String, Configuration> state) {
  		for (Map.Entry<String, Configuration> entry: state.entrySet()) {
  			this.mapName2Component(entry.getKey()).setAngles(entry.getValue());
  		}
  	}
  	
  	/**
     * Prints the joints on the specified PrintStream.
     * 
     * @param printStream
     *          The stream on which to print each of the components.
     */
    public void printJoints(final PrintStream printStream) {
      for (final Component component : this.components) {
        printStream.println(component);
      }
    }

  	
  	public void toggleSelection(int selectionNum) {
  		if ( 0 <= selectionNum && selectionNum < this.components.size()) {
  			Component component = mapNum2Component(selectionNum);
  			if ( this.selectedComponents.contains(component) ) {
  				this.selectedComponents.remove(component);
  				component.setColor(INACTIVE_COLOR);
  			}
  			else {
  		      this.selectedComponents.add(component);
  		      component.setColor(ACTIVE_COLOR);
  		    }
		}
  	}
  	
  	public void changeSelected(Configuration config) {
  		for(Component c: this.selectedComponents) {
  			c.changeConfiguration(config);
  		}
  	}
  	
  	// Append name to section names 
	public Spider(final Point3D position, final String name) {
		// Arm object itself as a top level component, need initialization
		super(position, name);
		
		Leg leg1 = new Leg(new Point3D(0.4, -0.25, 0.15), false, "FRONT_LEFT");
		Leg leg2 = new Leg(new Point3D(0.125, -0.3, 0.15), false, "MID1_LEFT");
		Leg leg3 = new Leg(new Point3D(-0.125, -0.3, 0.15), false, "MID2_LEFTT");
		Leg leg4 = new Leg(new Point3D(-0.4, -0.25,0.15), false, "FRONT_RIGHTT");
		
		Leg leg5 = new Leg(new Point3D(0.4,0.25,0.15), true, "FRONT_RIGHT");
		Leg leg6 = new Leg(new Point3D(0.125, 0.3, 0.15), true, "MID1_RIGHT");
		Leg leg7 = new Leg(new Point3D(-0.125, 0.3, 0.15), true, "MID2_RIGHT");
		Leg leg8 = new Leg(new Point3D(-0.4,0.25,0.15), true, "BACK_RIGHT");
		
		
	    // put together the fingers for easier selection by keyboard input later on
	    this.legs = new Leg[] {
	    		leg1,leg2,leg3,leg4, leg5, leg6, leg7, leg8
	    };

	    // the hand, which models the wrist joint
	    this.body = new Component(new Point3D(0, 0, BODY_HEIGHT), new Body(
	        BODY_RADIUS, this.glut), BODY_NAME);
	    
	    this.head = new Component(new Point3D(1, 0, HEAD_HEIGHT), new Body(
		        HEAD_RADIUS, this.glut), HEAD_NAME);
	    
	    this.addChild(this.body);
	    this.addChild(this.head);
	    for (Leg individualLeg:this.legs) {
	    	Component lowerJoint = individualLeg.lowerJoint();
	    	this.body.addChild(individualLeg.lowerJoint());
	    }
	    

	    this.components = new ArrayList<>();
	    this.components.add(body);
	    for (Leg individualLeg:this.legs) {
	    	this.components.addAll(individualLeg.joints);
	    }
	}
	
	private class Leg {
		
		private class ReversedComponent extends Component {
			
			public ReversedComponent(final Point3D position, final String name) {
				super(position, null, name);
			}
			
			public ReversedComponent(final Point3D position, final Displayable displayable, final String name) {
				super(position, displayable, name);
			}
			
			public double xAngle() {
				return -super.xAngle();
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @return {@inheritDoc}
			 */
			public double yAngle() {
				return -super.yAngle();
			}

			/**
			 * {@inheritDoc}
			 * 
			 * @return {@inheritDoc}
			 */
			public double zAngle() {
				return -super.zAngle();
			}
		}
		/** The distal joint of this finger. */
	    private final Component upperJoint;
	    /** The list of all the joints in this finger. */
	    private final List<Component> joints;
	    /** The middle joint of this finger. */
	    private final Component middleJoint;
	    /** The palm joint of this finger. */
	    private final Component lowerJoint;
	    

	    /**
	     * Instantiates this finger with the three specified joints.
	     * 
	     * @param lowerJoint
	     *          The palm joint of this finger.
	     * @param middleJoint
	     *          The middle joint of this finger.
	     * @param upperJoint
	     *          The distal joint of this finger.
	     */
	    public Leg(Point3D initialPoint, boolean reverseExtents, String prefix) {
	    	
	    	double x = initialPoint.x();
	    	double y = initialPoint.y();
	    	double z = initialPoint.z();
	    	
	    	Component upper;
	    	Component middle;
	    	Component lower;
	    	
	    
  	      
  	      if (reverseExtents == false) {
  	    	// Upper joint 
  			upper = new Component(new Point3D(0, 0,
  			    UPPER_JOINT_HEIGHT), new RoundedCylinder(SECTION_RADIUS,
  			    UPPER_JOINT_HEIGHT, glut), prefix+UPPER_SECTION_NAME);
  			
  			// Middle joint 
  			middle = new Component(new Point3D(0, 0,
  				MIDDLE_JOINT_HEIGHT), new RoundedCylinder(SECTION_RADIUS,
  				MIDDLE_JOINT_HEIGHT, glut), prefix+MIDDLE_SECTION_NAME);
  					
  			// Lower joint 
  			lower = new Component(new Point3D(0 + x, 0 + y,
  			    LOWER_JOINT_HEIGHT + z), new RoundedCylinder(SECTION_RADIUS,
  				LOWER_JOINT_HEIGHT, glut), prefix+LOWER_SECTION_NAME);
  			
	  	      
  	      } else {
  	    	// Upper joint 
  			upper = new ReversedComponent(new Point3D(0, 0,
  			    UPPER_JOINT_HEIGHT), new RoundedCylinder(SECTION_RADIUS,
  			    UPPER_JOINT_HEIGHT, glut), prefix+UPPER_SECTION_NAME);
  			
  			// Middle joint 
  			middle = new ReversedComponent(new Point3D(0, 0,
  				MIDDLE_JOINT_HEIGHT), new RoundedCylinder(SECTION_RADIUS,
  				MIDDLE_JOINT_HEIGHT, glut), prefix+MIDDLE_SECTION_NAME);
  					
  			// Lower joint 
  			lower = new ReversedComponent(new Point3D(0 + x, 0 + y,
  			    LOWER_JOINT_HEIGHT + z), new RoundedCylinder(SECTION_RADIUS,
  				LOWER_JOINT_HEIGHT, glut), prefix+LOWER_SECTION_NAME);
  			
	  	      
  	      }
  	      lower.setXPositiveExtent(90);
	      lower.setXNegativeExtent(15);
	      lower.setYPositiveExtent(10);
	      lower.setYNegativeExtent(-10);
	      lower.setZPositiveExtent(0);
	      lower.setZNegativeExtent(0);
	      
	      middle.setXPositiveExtent(80);
	      middle.setXNegativeExtent(15);
	      middle.setYPositiveExtent(10);
	      middle.setYNegativeExtent(-10);
	      middle.setZPositiveExtent(0);
	      middle.setZNegativeExtent(0);
	      
	      upper.setXPositiveExtent(70);
	      upper.setXNegativeExtent(15);
	      upper.setYPositiveExtent(10);
	      upper.setYNegativeExtent(-10);
	      upper.setZPositiveExtent(0);
	      upper.setZNegativeExtent(0);
	      
  	      this.lowerJoint = lower;
	      this.middleJoint = middle;
	      this.upperJoint = upper;
	      
	      this.upperJoint.setAngles(0, 0, 0);
	      this.middleJoint.setAngles(0, 0, 0);
	      this.lowerJoint.setAngles(0, 0, 0);
	      
	      this.lowerJoint.addChild(middleJoint);
	      
	      this.middleJoint.addChild(upperJoint);
	      
	      this.joints = Collections.unmodifiableList(Arrays.asList(this.lowerJoint,
	          this.middleJoint, this.upperJoint));
	    }

	    /**
	     * Gets the distal joint of this finger.
	     * 
	     * @return The distal joint of this finger.
	     */
	    Component upperJoint() {
	      return this.upperJoint;
	    }

	    /**
	     * Gets an unmodifiable view of the list of the joints of this finger.
	     * 
	     * @return An unmodifiable view of the list of the joints of this finger.
	     */
	    List<Component> joints() {
	      return this.joints;
	    }

	    /**
	     * Gets the middle joint of this finger.
	     * 
	     * @return The middle joint of this finger.
	     */
	    Component middleJoint() {
	      return this.middleJoint;
	    }

	    /**
	     * Gets the palm joint of this finger.
	     * 
	     * @return The palm joint of this finger.
	     */
	    Component lowerJoint() {
	      return this.lowerJoint;
	    }
	}


}


