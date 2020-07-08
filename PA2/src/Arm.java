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

public class Arm extends Component implements Animate, Selection{
	/** The OpenGL utility toolkit object. */
	private final GLUT glut = new GLUT();
	
	
	/** The upper arm to be modeled. */
	private final Component upperArm;
	/** The hand to be modeled. */
	private final Component hand;
	/** The forearm to be modeled. */
	private final Component forearm;
	/** The fingers on the hand to be modeled. */
	private final Finger[] fingers;
	/** The set of all components. */
	private final List<Component> components;
	/** The set of components which are currently selected for rotation. */
	private final Set<Component> selectedComponents = new HashSet<Component>(18);
	
	/** The color for components which are selected for rotation. */
	public static final FloatColor ACTIVE_COLOR = FloatColor.RED;
  	/** The color for components which are not selected for rotation. */
	public static final FloatColor INACTIVE_COLOR = FloatColor.ORANGE;
	
	
	/** The radius of the components which comprise the arm. */
	public static final double ARM_RADIUS = 0.25;
	/** The height of the distal joint on each of the fingers. */
	public static final double DISTAL_JOINT_HEIGHT = 0.2;
	/** The radius of each joint which comprises the finger. */
	public static final double FINGER_RADIUS = 0.09;
	/** The height of the forearm. */
	public static final double FOREARM_HEIGHT = 1.5;
	/** The radius of the hand. */
	public static final double HAND_RADIUS = 0.5;
	/** The height of the middle joint on each of the fingers. */
	public static final double MIDDLE_JOINT_HEIGHT = 0.25;
	/** The height of the palm joint on each of the fingers. */
	public static final double PALM_JOINT_HEIGHT = 0.25;
	/** The height of the upper arm. */
	public static final double UPPER_ARM_HEIGHT = 1.8;
	
	
	public static final String PINKY_PALM_NAME = "pinky palm";
	public static final String PINKY_MIDDLE_NAME = "pinky middle";
  	public static final String PINKY_DISTAL_NAME = "pinky distal";
	public static final String RING_PALM_NAME = "ring palm";
	public static final String RING_MIDDLE_NAME = "ring middle";
	public static final String RING_DISTAL_NAME = "ring distal";
  	public static final String MIDDLE_PALM_NAME = "middle palm";
	public static final String MIDDLE_MIDDLE_NAME = "middle middle";
	public static final String MIDDLE_DISTAL_NAME = "middle distal";
  	public static final String INDEX_PALM_NAME = "index palm";
	public static final String INDEX_MIDDLE_NAME = "index middle";
	public static final String INDEX_DISTAL_NAME = "index distal";
  	public static final String THUMB_PALM_NAME = "thumb palm";
  	public static final String THUMB_MIDDLE_NAME = "thumb middle";
  	public static final String THUMB_DISTAL_NAME = "thumb distal";
  	public static final String HAND_NAME = "hand";
  	public static final String FOREARM_NAME = "forearm";
  	public static final String UPPER_ARM_NAME = "upper arm";
	
  	private Component mapNum2Component(int componentNum) {
  		switch(componentNum) {
  			case  0: return this.upperArm;
			case  1: return this.forearm;
			case  2: return this.hand;
			case  3: return this.fingers[0].palmJoint();
			case  4: return this.fingers[0].middleJoint();
			case  5: return this.fingers[0].distalJoint();
			case  6: return this.fingers[1].palmJoint();
			case  7: return this.fingers[1].middleJoint();
			case  8: return this.fingers[1].distalJoint();
			case  9: return this.fingers[2].palmJoint();
			case 10: return this.fingers[2].middleJoint();
			case 11: return this.fingers[2].distalJoint();
			case 12: return this.fingers[3].palmJoint();
			case 13: return this.fingers[3].middleJoint();
			case 14: return this.fingers[3].distalJoint();
			case 15: return this.fingers[4].palmJoint();
			case 16: return this.fingers[4].middleJoint();
			case 17: return this.fingers[4].distalJoint();
			default: throw new IllegalArgumentException("componentNum over index"); 
  		}
  	}
  	
  	private Component mapName2Component(String componentName) {
  		switch(componentName) {
	  		case  UPPER_ARM_NAME: return this.upperArm;
			case  FOREARM_NAME: return this.forearm;
			case  HAND_NAME: return this.hand;
			case  THUMB_PALM_NAME: return this.fingers[4].palmJoint();
			case  THUMB_MIDDLE_NAME: return this.fingers[4].middleJoint();
			case  THUMB_DISTAL_NAME: return this.fingers[4].distalJoint();
			case  INDEX_PALM_NAME: return this.fingers[3].palmJoint();
			case  INDEX_MIDDLE_NAME: return this.fingers[3].middleJoint();
			case  INDEX_DISTAL_NAME: return this.fingers[3].distalJoint();
			case  MIDDLE_PALM_NAME: return this.fingers[2].palmJoint();
			case  MIDDLE_MIDDLE_NAME: return this.fingers[2].middleJoint();
			case  MIDDLE_DISTAL_NAME: return this.fingers[2].distalJoint();
			case  RING_PALM_NAME: return this.fingers[1].palmJoint();
			case  RING_MIDDLE_NAME: return this.fingers[1].middleJoint();
			case  RING_DISTAL_NAME: return this.fingers[1].distalJoint();
			case  PINKY_PALM_NAME: return this.fingers[0].palmJoint();
			case  PINKY_MIDDLE_NAME: return this.fingers[0].middleJoint();
			case  PINKY_DISTAL_NAME: return this.fingers[0].distalJoint();
			default: throw new IllegalArgumentException("componentName doesn't exist");
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
  		if ( 0 <= selectionNum && selectionNum <= 17) {
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
  	
	public Arm(final Point3D position, final String name) {
		// Arm object itself as a top level component, need initialization
		super(position, name);
		
		// all the distal joints
	    final Component distal1 = new Component(new Point3D(0, 0,
	        MIDDLE_JOINT_HEIGHT), new RoundedCylinder(FINGER_RADIUS,
	        DISTAL_JOINT_HEIGHT, this.glut), PINKY_DISTAL_NAME);
	    final Component distal2 = new Component(new Point3D(0, 0,
	        MIDDLE_JOINT_HEIGHT), new RoundedCylinder(FINGER_RADIUS,
	        DISTAL_JOINT_HEIGHT, this.glut), RING_DISTAL_NAME);
	    final Component distal3 = new Component(new Point3D(0, 0,
	        MIDDLE_JOINT_HEIGHT), new RoundedCylinder(FINGER_RADIUS,
	        DISTAL_JOINT_HEIGHT, this.glut), MIDDLE_DISTAL_NAME);
	    final Component distal4 = new Component(new Point3D(0, 0,
	        MIDDLE_JOINT_HEIGHT), new RoundedCylinder(FINGER_RADIUS,
	        DISTAL_JOINT_HEIGHT, this.glut), INDEX_DISTAL_NAME);
	    final Component distal5 = new Component(new Point3D(0, 0,
	        MIDDLE_JOINT_HEIGHT), new RoundedCylinder(FINGER_RADIUS,
	        DISTAL_JOINT_HEIGHT, this.glut), THUMB_DISTAL_NAME);

	    // all the middle joints
	    final Component middle1 = new Component(new Point3D(0, 0,
	        PALM_JOINT_HEIGHT), new RoundedCylinder(FINGER_RADIUS,
	        MIDDLE_JOINT_HEIGHT, this.glut), PINKY_MIDDLE_NAME);
	    final Component middle2 = new Component(new Point3D(0, 0,
	        PALM_JOINT_HEIGHT), new RoundedCylinder(FINGER_RADIUS,
	        MIDDLE_JOINT_HEIGHT, this.glut), RING_MIDDLE_NAME);
	    final Component middle3 = new Component(new Point3D(0, 0,
	        PALM_JOINT_HEIGHT), new RoundedCylinder(FINGER_RADIUS,
	        MIDDLE_JOINT_HEIGHT, this.glut), MIDDLE_MIDDLE_NAME);
	    final Component middle4 = new Component(new Point3D(0, 0,
	        PALM_JOINT_HEIGHT), new RoundedCylinder(FINGER_RADIUS,
	        MIDDLE_JOINT_HEIGHT, this.glut), INDEX_MIDDLE_NAME);
	    final Component middle5 = new Component(new Point3D(0, 0,
	        PALM_JOINT_HEIGHT), new RoundedCylinder(FINGER_RADIUS,
	        MIDDLE_JOINT_HEIGHT, this.glut), THUMB_MIDDLE_NAME);

	    // all the palm joints, displaced by various amounts from the palm
	    final Component palm1 = new Component(new Point3D(-0.3, 0, 0.7),
	        new RoundedCylinder(FINGER_RADIUS, PALM_JOINT_HEIGHT, this.glut),
	        PINKY_PALM_NAME);
	    final Component palm2 = new Component(new Point3D(-.1, 0, 0.9),
	        new RoundedCylinder(FINGER_RADIUS, PALM_JOINT_HEIGHT, this.glut),
	        RING_PALM_NAME);
	    final Component palm3 = new Component(new Point3D(0.1, 0, 0.95),
	        new RoundedCylinder(FINGER_RADIUS, PALM_JOINT_HEIGHT, this.glut),
	        MIDDLE_PALM_NAME);
	    final Component palm4 = new Component(new Point3D(0.3, 0, 0.75),
	        new RoundedCylinder(FINGER_RADIUS, PALM_JOINT_HEIGHT, this.glut),
	        INDEX_PALM_NAME);
	    final Component palm5 = new Component(new Point3D(0.24, 0, 0.23),
	        new RoundedCylinder(FINGER_RADIUS, PALM_JOINT_HEIGHT, this.glut),
	        THUMB_PALM_NAME);

	    // put together the fingers for easier selection by keyboard input later on
	    this.fingers = new Finger[] { new Finger(palm1, middle1, distal1),
	        new Finger(palm2, middle2, distal2),
	        new Finger(palm3, middle3, distal3),
	        new Finger(palm4, middle4, distal4),
	        new Finger(palm5, middle5, distal5), };

	    // the hand, which models the wrist joint
	    this.hand = new Component(new Point3D(0, 0, FOREARM_HEIGHT), new Body(
	        HAND_RADIUS, this.glut), HAND_NAME);

	    // the forearm, which models the elbow joint
	    this.forearm = new Component(new Point3D(0, 0, UPPER_ARM_HEIGHT),
	        new RoundedCylinder(ARM_RADIUS, FOREARM_HEIGHT, this.glut),
	        FOREARM_NAME);

	    // the upper arm which models the shoulder joint
	    this.upperArm = new Component(new Point3D(0, 0, 0), new RoundedCylinder(
	        ARM_RADIUS, UPPER_ARM_HEIGHT, this.glut), UPPER_ARM_NAME);

	    this.addChild(this.upperArm);
	    // the funny bone's connected to the...forearm
	    this.upperArm.addChild(this.forearm);
	    // the forearm's connected to the...wrist bone
	    this.forearm.addChild(this.hand);
	    // the wrist bone's connected to the...fingers
	    this.hand.addChildren(palm1, palm2, palm3, palm4, palm5);
	    palm1.addChild(middle1);
	    palm2.addChild(middle2);
	    palm3.addChild(middle3);
	    palm4.addChild(middle4);
	    palm5.addChild(middle5);
	    middle1.addChild(distal1);
	    middle2.addChild(distal2);
	    middle3.addChild(distal3);
	    middle4.addChild(distal4);
	    middle5.addChild(distal5);
	    
	    // turn the whole arm to be at an arm-like angle
	    this.rotate(Axis.Y, 225);
	    this.rotate(Axis.X, -90);

	    // rotate the elbow to be half bent
	    this.forearm.rotate(Axis.Y, 90);

	    // rotate the thumb so that it is at a thumb-like angle
	    palm5.rotate(Axis.Y, 50);
	    palm5.rotate(Axis.Z, -60);
	    
	    // set rotation limits for the shoulder
	    this.upperArm.setXPositiveExtent(45);
	    this.upperArm.setXNegativeExtent(-45);
	    this.upperArm.setYPositiveExtent(135);
	    this.upperArm.setYNegativeExtent(-135);
	    this.upperArm.setZPositiveExtent(45);
	    this.upperArm.setZNegativeExtent(-90);

	    // set rotation limits for the elbow
	    this.forearm.setXPositiveExtent(180);
	    this.forearm.setXNegativeExtent(0);
	    this.forearm.setYPositiveExtent(0);
	    this.forearm.setYNegativeExtent(0);
	    this.forearm.setZPositiveExtent(45);
	    this.forearm.setZNegativeExtent(-90);

	    // set rotation limits for the wrist
	    this.hand.setXPositiveExtent(90);
	    this.hand.setXNegativeExtent(-90);
	    this.hand.setYPositiveExtent(12);
	    this.hand.setYNegativeExtent(-12);
	    this.hand.setZPositiveExtent(0);
	    this.hand.setZNegativeExtent(0);

	    // set rotation limits for the palm joints of the fingers
	    for (final Component palmJoint : Arrays.asList(palm1, palm2, palm3, palm4)) {
	      palmJoint.setXPositiveExtent(90);
	      palmJoint.setXNegativeExtent(-15);
	      palmJoint.setYPositiveExtent(10);
	      palmJoint.setYNegativeExtent(-10);
	      palmJoint.setZPositiveExtent(0);
	      palmJoint.setZNegativeExtent(0);
	    }

	    // and set the rotation limits for the palm joint of the thumb
	    palm5.setXPositiveExtent(80);
	    palm5.setXNegativeExtent(0);
	    palm5.setYPositiveExtent(62);
	    palm5.setYNegativeExtent(38);
	    palm5.setZPositiveExtent(-60);
	    palm5.setZNegativeExtent(-60);

	    // set rotation limits for the middle joints of the finger
	    for (final Component middleJoint : Arrays.asList(middle1, middle2,
	        middle3, middle4, middle5)) {
	      middleJoint.setXPositiveExtent(110);
	      middleJoint.setXNegativeExtent(0);
	      middleJoint.setYPositiveExtent(0);
	      middleJoint.setYNegativeExtent(0);
	      middleJoint.setZPositiveExtent(0);
	      middleJoint.setZNegativeExtent(0);
	    }

	    // set rotation limits for the distal joints of the finger
	    for (final Component distalJoint : Arrays.asList(distal1, distal2,
	        distal3, distal4, distal5)) {
	      distalJoint.setXPositiveExtent(80);
	      distalJoint.setXNegativeExtent(-5);
	      distalJoint.setYPositiveExtent(0);
	      distalJoint.setYNegativeExtent(0);
	      distalJoint.setZPositiveExtent(0);
	      distalJoint.setZNegativeExtent(0);
	    }

	    // create the list of all the components for debugging purposes
	    this.components = Arrays.asList(palm1, middle1, distal1, palm2, middle2,
	        distal2, palm3, middle3, distal3, palm4, middle4, distal4, palm5,
	        middle5, distal5, this.hand, this.forearm, this.upperArm);
	}
	
	private class Finger {
	    /** The distal joint of this finger. */
	    private final Component distalJoint;
	    /** The list of all the joints in this finger. */
	    private final List<Component> joints;
	    /** The middle joint of this finger. */
	    private final Component middleJoint;
	    /** The palm joint of this finger. */
	    private final Component palmJoint;

	    /**
	     * Instantiates this finger with the three specified joints.
	     * 
	     * @param palmJoint
	     *          The palm joint of this finger.
	     * @param middleJoint
	     *          The middle joint of this finger.
	     * @param distalJoint
	     *          The distal joint of this finger.
	     */
	    public Finger(final Component palmJoint, final Component middleJoint,
	        final Component distalJoint) {
	      this.palmJoint = palmJoint;
	      this.middleJoint = middleJoint;
	      this.distalJoint = distalJoint;

	      this.joints = Collections.unmodifiableList(Arrays.asList(this.palmJoint,
	          this.middleJoint, this.distalJoint));
	    }

	    /**
	     * Gets the distal joint of this finger.
	     * 
	     * @return The distal joint of this finger.
	     */
	    Component distalJoint() {
	      return this.distalJoint;
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
	    Component palmJoint() {
	      return this.palmJoint;
	    }
	}


}

