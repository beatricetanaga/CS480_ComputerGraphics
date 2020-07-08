/**
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @author Zezhou Sun <micou@bu.edu>
 * @since Spring 2011
 */

import java.util.HashMap;
import java.util.Map;

public class TestCases extends CyclicIterator<Map<String, Configuration>> {

	public static String INDEX_PALM_NAME = "index palm";
	public static String INDEX_MIDDLE_NAME = "index middle";
	public static String INDEX_DISTAL_NAME = "index distal";
	public static String RING_PALM_NAME = "ring palm";
	public static String RING_MIDDLE_NAME = "ring middle";
	public static String RING_DISTAL_NAME = "ring distal";
	public static String MIDDLE_PALM_NAME = "middle palm";
	public static String MIDDLE_MIDDLE_NAME = "middle middle";
	public static String MIDDLE_DISTAL_NAME = "middle distal";
	public static String PINKY_PALM_NAME = "pinky palm";
	public static String PINKY_MIDDLE_NAME = "pinky middle";
	public static String PINKY_DISTAL_NAME = "pinky distal";
	public static String THUMB_PALM_NAME = "thumb palm";
	public static String THUMB_MIDDLE_NAME = "thumb middle";
	public static String THUMB_DISTAL_NAME = "thumb distal";
	public static String HAND_NAME = "hand";
	public static String FOREARM_NAME = "forearm";
	public static String UPPER_ARM_NAME = "upper arm";

	Map<String, Configuration> stop() {
		return this.stop;
	}

	private final Map<String, Configuration> stop;

	@SuppressWarnings("unchecked")
	TestCases() {
		this.stop = new HashMap<String, Configuration>();
		final Map<String, Configuration> peace = new HashMap<String, Configuration>();
		final Map<String, Configuration> fist = new HashMap<String, Configuration>();
		final Map<String, Configuration> shaka = new HashMap<String, Configuration>();
		final Map<String, Configuration> spread = new HashMap<String, Configuration>();
		final Map<String, Configuration> claw = new HashMap<String, Configuration>();

		super.add(stop, peace, fist, shaka, spread, claw);

		// the upper arm, forearm, and hand angles do not change through any of the
		// test cases
		stop.put(UPPER_ARM_NAME, new BaseConfiguration(0, 0, 0));
		peace.put(UPPER_ARM_NAME, new BaseConfiguration(0, 0, 0));
		fist.put(UPPER_ARM_NAME, new BaseConfiguration(0, 0, 0));
		shaka.put(UPPER_ARM_NAME, new BaseConfiguration(0, 0, 0));
		spread.put(UPPER_ARM_NAME, new BaseConfiguration(0, 0, 0));
		claw.put(UPPER_ARM_NAME, new BaseConfiguration(0, 0, 0));

		stop.put(FOREARM_NAME, new BaseConfiguration(0, 90, 0));
		peace.put(FOREARM_NAME, new BaseConfiguration(0, 90, 0));
		fist.put(FOREARM_NAME, new BaseConfiguration(0, 90, 0));
		shaka.put(FOREARM_NAME, new BaseConfiguration(0, 90, 0));
		spread.put(FOREARM_NAME, new BaseConfiguration(0, 90, 0));
		claw.put(FOREARM_NAME, new BaseConfiguration(0, 90, 0));

		stop.put(HAND_NAME, new BaseConfiguration(0, 0, 0));
		peace.put(HAND_NAME, new BaseConfiguration(0, 0, 0));
		fist.put(HAND_NAME, new BaseConfiguration(0, 0, 0));
		shaka.put(HAND_NAME, new BaseConfiguration(0, 0, 0));
		spread.put(HAND_NAME, new BaseConfiguration(0, 0, 0));
		claw.put(HAND_NAME, new BaseConfiguration(0, 0, 0));

		// the stop test case
		stop.put(PINKY_DISTAL_NAME, new BaseConfiguration(0, 0, 0));
		stop.put(PINKY_MIDDLE_NAME, new BaseConfiguration(0, 0, 0));
		stop.put(PINKY_PALM_NAME, new BaseConfiguration(0, 0, 0));
		stop.put(RING_DISTAL_NAME, new BaseConfiguration(0, 0, 0));
		stop.put(RING_MIDDLE_NAME, new BaseConfiguration(0, 0, 0));
		stop.put(RING_PALM_NAME, new BaseConfiguration(0, 0, 0));
		stop.put(MIDDLE_DISTAL_NAME, new BaseConfiguration(0, 0, 0));
		stop.put(MIDDLE_MIDDLE_NAME, new BaseConfiguration(0, 0, 0));
		stop.put(MIDDLE_PALM_NAME, new BaseConfiguration(0, 0, 0));
		stop.put(INDEX_DISTAL_NAME, new BaseConfiguration(0, 0, 0));
		stop.put(INDEX_MIDDLE_NAME, new BaseConfiguration(0, 0, 0));
		stop.put(INDEX_PALM_NAME, new BaseConfiguration(0, 0, 0));
		stop.put(THUMB_DISTAL_NAME, new BaseConfiguration(0, 0, 0));
		stop.put(THUMB_MIDDLE_NAME, new BaseConfiguration(0, 0, 0));
		stop.put(THUMB_PALM_NAME, new BaseConfiguration(0, 50, -60));

		// the peace sign test case
		peace.put(PINKY_DISTAL_NAME, new BaseConfiguration(50, 0, 0));
		peace.put(PINKY_MIDDLE_NAME, new BaseConfiguration(90, 0, 0));
		peace.put(PINKY_PALM_NAME, new BaseConfiguration(60, 0, 0));
		peace.put(RING_DISTAL_NAME, new BaseConfiguration(50, 0, 0));
		peace.put(RING_MIDDLE_NAME, new BaseConfiguration(90, 0, 0));
		peace.put(RING_PALM_NAME, new BaseConfiguration(60, 0, 0));
		peace.put(MIDDLE_DISTAL_NAME, new BaseConfiguration(0, 0, 0));
		peace.put(MIDDLE_MIDDLE_NAME, new BaseConfiguration(0, 0, 0));
		peace.put(MIDDLE_PALM_NAME, new BaseConfiguration(0, 0, 0));
		peace.put(INDEX_DISTAL_NAME, new BaseConfiguration(0, 0, 0));
		peace.put(INDEX_MIDDLE_NAME, new BaseConfiguration(0, 0, 0));
		peace.put(INDEX_PALM_NAME, new BaseConfiguration(0, 0, 0));
		peace.put(THUMB_DISTAL_NAME, new BaseConfiguration(10, 0, 0));
		peace.put(THUMB_MIDDLE_NAME, new BaseConfiguration(90, 0, 0));
		peace.put(THUMB_PALM_NAME, new BaseConfiguration(30, 50, -60));

		// the fist test case
		fist.put(PINKY_DISTAL_NAME, new BaseConfiguration(50, 0, 0));
		fist.put(PINKY_MIDDLE_NAME, new BaseConfiguration(90, 0, 0));
		fist.put(PINKY_PALM_NAME, new BaseConfiguration(60, 0, 0));
		fist.put(RING_DISTAL_NAME, new BaseConfiguration(50, 0, 0));
		fist.put(RING_MIDDLE_NAME, new BaseConfiguration(90, 0, 0));
		fist.put(RING_PALM_NAME, new BaseConfiguration(60, 0, 0));
		fist.put(MIDDLE_DISTAL_NAME, new BaseConfiguration(50, 0, 0));
		fist.put(MIDDLE_MIDDLE_NAME, new BaseConfiguration(90, 0, 0));
		fist.put(MIDDLE_PALM_NAME, new BaseConfiguration(60, 0, 0));
		fist.put(INDEX_DISTAL_NAME, new BaseConfiguration(50, 0, 0));
		fist.put(INDEX_MIDDLE_NAME, new BaseConfiguration(90, 0, 0));
		fist.put(INDEX_PALM_NAME, new BaseConfiguration(60, 0, 0));
		fist.put(THUMB_DISTAL_NAME, new BaseConfiguration(50, 0, 0));
		fist.put(THUMB_MIDDLE_NAME, new BaseConfiguration(60, 0, 0));
		fist.put(THUMB_PALM_NAME, new BaseConfiguration(30, 50, -60));

		// the shaka test case
		shaka.put(PINKY_DISTAL_NAME, new BaseConfiguration(0, 0, 0));
		shaka.put(PINKY_MIDDLE_NAME, new BaseConfiguration(0, 0, 0));
		shaka.put(PINKY_PALM_NAME, new BaseConfiguration(0, -15, 0));
		shaka.put(RING_DISTAL_NAME, new BaseConfiguration(50, 0, 0));
		shaka.put(RING_MIDDLE_NAME, new BaseConfiguration(80, 0, 0));
		shaka.put(RING_PALM_NAME, new BaseConfiguration(45, 0, 0));
		shaka.put(MIDDLE_DISTAL_NAME, new BaseConfiguration(50, 0, 0));
		shaka.put(MIDDLE_MIDDLE_NAME, new BaseConfiguration(80, 0, 0));
		shaka.put(MIDDLE_PALM_NAME, new BaseConfiguration(45, 0, 0));
		shaka.put(INDEX_DISTAL_NAME, new BaseConfiguration(50, 0, 0));
		shaka.put(INDEX_MIDDLE_NAME, new BaseConfiguration(80, 0, 0));
		shaka.put(INDEX_PALM_NAME, new BaseConfiguration(45, 0, 0));
		shaka.put(THUMB_DISTAL_NAME, new BaseConfiguration(-10, 0, 0));
		shaka.put(THUMB_MIDDLE_NAME, new BaseConfiguration(0, 0, 0));
		shaka.put(THUMB_PALM_NAME, new BaseConfiguration(0, 60, -60));

		// the spread test case
		spread.put(PINKY_DISTAL_NAME, new BaseConfiguration(0, 0, 0));
		spread.put(PINKY_MIDDLE_NAME, new BaseConfiguration(0, 0, 0));
		spread.put(PINKY_PALM_NAME, new BaseConfiguration(0, -10, 0));
		spread.put(RING_DISTAL_NAME, new BaseConfiguration(0, 0, 0));
		spread.put(RING_MIDDLE_NAME, new BaseConfiguration(0, 0, 0));
		spread.put(RING_PALM_NAME, new BaseConfiguration(0, -7, 0));
		spread.put(MIDDLE_DISTAL_NAME, new BaseConfiguration(0, 0, 0));
		spread.put(MIDDLE_MIDDLE_NAME, new BaseConfiguration(0, 0, 0));
		spread.put(MIDDLE_PALM_NAME, new BaseConfiguration(0, 0, 0));
		spread.put(INDEX_DISTAL_NAME, new BaseConfiguration(0, 0, 0));
		spread.put(INDEX_MIDDLE_NAME, new BaseConfiguration(0, 0, 0));
		spread.put(INDEX_PALM_NAME, new BaseConfiguration(0, 10, 0));
		spread.put(THUMB_DISTAL_NAME, new BaseConfiguration(0, 0, 0));
		spread.put(THUMB_MIDDLE_NAME, new BaseConfiguration(0, 0, 0));
		spread.put(THUMB_PALM_NAME, new BaseConfiguration(0, 60, -60));

		// the claw test case
		claw.put(PINKY_DISTAL_NAME, new BaseConfiguration(60, 0, 0));
		claw.put(PINKY_MIDDLE_NAME, new BaseConfiguration(80, 0, 0));
		claw.put(PINKY_PALM_NAME, new BaseConfiguration(0, 0, 0));
		claw.put(RING_DISTAL_NAME, new BaseConfiguration(60, 0, 0));
		claw.put(RING_MIDDLE_NAME, new BaseConfiguration(80, 0, 0));
		claw.put(RING_PALM_NAME, new BaseConfiguration(0, 0, 0));
		claw.put(MIDDLE_DISTAL_NAME, new BaseConfiguration(60, 0, 0));
		claw.put(MIDDLE_MIDDLE_NAME, new BaseConfiguration(80, 0, 0));
		claw.put(MIDDLE_PALM_NAME, new BaseConfiguration(0, 0, 0));
		claw.put(INDEX_DISTAL_NAME, new BaseConfiguration(60, 0, 0));
		claw.put(INDEX_MIDDLE_NAME, new BaseConfiguration(80, 0, 0));
		claw.put(INDEX_PALM_NAME, new BaseConfiguration(0, 0, 0));
		claw.put(THUMB_DISTAL_NAME, new BaseConfiguration(70, 0, 0));
		claw.put(THUMB_MIDDLE_NAME, new BaseConfiguration(20, 0, 0));
		claw.put(THUMB_PALM_NAME, new BaseConfiguration(30, 50, -60));
	}
}
