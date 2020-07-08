/**
 * A base class for an object which has angles around three axes and position.
 * 
 * @author Jeffrey Finkelstein <jeffrey.finkelstein@gmail.com>
 * @author Zezhou Sun <micou@bu.edu>
 * @since Spring 2011
 */
public class BaseConfiguration implements Configuration {
	private class Angle {
		public double angle = 0.0;

		public Angle(double angle) {
			this.angle = angle;
		}
	}

	/** The current angle at which this is rotated around the x axis. */
	private Angle xAngle = new Angle(0);
	/** The current angle at which this is rotated around the y axis. */
	private Angle yAngle = new Angle(0);
	/** The current angle at which this is rotated around the z axis. */
	private Angle zAngle = new Angle(0);
	/** The current position at where local origin located */
	private Point3D position = new Point3D(0, 0, 0);

	BaseConfiguration(final Point3D p) {
		this.position = new Point3D(p.x(), p.y(), p.z());
	}

	BaseConfiguration(final double xAngle, final double yAngle, final double zAngle, final Point3D p) {
		this.xAngle = new Angle(xAngle);
		this.yAngle = new Angle(yAngle);
		this.zAngle = new Angle(zAngle);
		this.position = new Point3D(p.x(), p.y(), p.z());
	}

	BaseConfiguration(final double xAngle, final double yAngle, final double zAngle) {
		this.xAngle = new Angle(xAngle);
		this.yAngle = new Angle(yAngle);
		this.zAngle = new Angle(zAngle);
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	public double xAngle() {
		
		return this.xAngle.angle;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	public double yAngle() {
		return this.yAngle.angle;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return {@inheritDoc}
	 */
	public double zAngle() {
		return this.zAngle.angle;
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
		this.xAngle = new Angle(xAngle);
	}

	public void setYAngle(final double yAngle) {
		this.yAngle = new Angle(yAngle);
	}

	public void setZAngle(final double zAngle) {
		this.zAngle = new Angle(zAngle);
	}

	public void setPosition(Point3D p) {
		this.position = new Point3D(p.x(), p.y(), p.z());
	}

}
