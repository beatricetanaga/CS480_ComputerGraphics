/**
 * A base class for an object which has angles around three axes and position.
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @author Zezhou Sun <micou@bu.edu>
 * @since Spring 2011
 */
public class BaseConfiguration implements Configuration {
	/** The current angle at which this is rotated around the x axis. */
	private double xAngle = 0.0;
	/** The current angle at which this is rotated around the y axis. */
	private double yAngle = 0.0;
	/** The current angle at which this is rotated around the z axis. */
	private double zAngle = 0.0;
	/** The current position at where local origin located */
	private Point3D position = new Point3D(0.0, 0.0, 0.0);

	BaseConfiguration(final double xAngle, final double yAngle, final double zAngle, final Point3D p) {
		this.xAngle = xAngle;
		this.yAngle = yAngle;
		this.zAngle = zAngle;
		this.position = p;
	}

	BaseConfiguration(final double xAngle, final double yAngle, final double zAngle) {
		this.xAngle = xAngle;
		this.yAngle = yAngle;
		this.zAngle = zAngle;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	public double xAngle() {
		return this.xAngle;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	public double yAngle() {
		return this.yAngle;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	public double zAngle() {
		return this.zAngle;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	public Point3D position() {
		return this.position;
	}

	public void setXAngle(final double xAngle) {
		this.xAngle = xAngle;
	}

	public void setYAngle(final double yAngle) {
		this.yAngle = yAngle;
	}

	public void setZAngle(final double zAngle) {
		this.zAngle = zAngle;
	}

	public void setPosition(Point3D p) {
		this.position = p;
	}

}
