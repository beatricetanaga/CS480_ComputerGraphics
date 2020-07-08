
class Quaternion {
  /** The threshold below which to renormalize this quaternion, if necessary. */
  public static final float ROUND_OFF_THRESHOLD = 0.0001f;
  /** The scalar component of this quaternion. */
  private float s;

  /** The vector components of this quaternion. */
  private Point3D p = new Point3D();

  /**
   * Instantiates this quaternion initial value (1, 0, 0, 0).
   */
  public Quaternion() {
    this.set(1, 0, 0, 0);
  }

  /**
   * Instantiates this quaternion with the specified component values.
   * 
   * 
   * @param s
   *          The scalar component
   * @param v0
   *          The first vector component
   * @param v1
   *          The second vector component
   * @param v2
   *          The third vector component
   */
  public Quaternion(float s, float v0, float v1, float v2) {
    this.set(s, v0, v1, v2);
  }

  public Quaternion(float s, Point3D v) {
	    this.set(s, v.x, v.y, v.z);
	  }

  /**
   * Returns a new quaternion representing the product of this and the specified
   * other quaternion
   * 
   * @param that
   *          other quaternion with which to multiply
   * @return The product of this and the specified other quaternion.
   */
  public Quaternion multiply(final Quaternion that) {

	    final float newS = this.s * that.s - this.p.x * that.p.x - this.p.y
	        * that.p.y - this.p.z * that.p.z;

	    float i = (this.s * that.p.x) + (that.s * this.p.x)
	        + (this.p.y * that.p.z - this.p.z * that.p.y);
	    float j = (this.s * that.p.y) + (that.s * this.p.y)
	        + (this.p.z * that.p.x - this.p.x * that.p.z);
	    float k = (this.s * that.p.z) + (that.s * this.p.z)
	        + (this.p.x * that.p.y - this.p.y * that.p.x);

	    return new Quaternion(newS, i, j, k);
  }

  public Quaternion conjugate()
  {
	return(new Quaternion(s,p.scale((float)-1.0)));
  }
  
  /**
   * Returns the norm (magnitude)
   * 
   * @return The norm (magnitude)
   */
  private float norm() {
    return (float) Math.sqrt(this.s * this.s + this.p.x*this.p.x + this.p.y*this.p.y + this.p.z*this.p.z);
  }


  public void normalize() {
    final float mag = this.norm();

    if (mag > ROUND_OFF_THRESHOLD) {
      this.s /= mag;
      this.p.x /= mag;
      this.p.y /= mag;
      this.p.z /= mag;
    }
  }

  /**
   * Resets this quaternion to (1, 0, 0, 0).
   */
  public void reset() {
    this.set(1f, 0f, 0f, 0f);
  }


  private void set(float _s, float _v0, float _v1, float _v2) {
	  s = _s;
	  p.x = _v0;
	  p.y = _v1;
	  p.z = _v2;
  }

  public Point3D get_v() {
	    Point3D res = new Point3D(p);
	    return(res);
	  }


  /** Returns a 4 by 4 matrix which represents a transformation equivalent to that of quaternion **/
  public float[] toMatrix() {
	  final float[] M = new float[16];

	  final float a = p.x;
	  final float b = p.y;
	  final float c = p.z;

	  M[0] = 1 - 2 * b * b - 2 * c * c; // M[0][0]
	  M[1] = 2 * a * b + 2 * this.s * c; // M[1][0]
	  M[2] = 2 * a * c - 2 * this.s * b; // M[2][0]
	  M[3] = 0.0f; // M[3][0]

	  M[4] = 2 * a * b - 2 * this.s * c; // M[0][1]
	  M[5] = 1 - 2 * a * a - 2 * c * c; // M[1][1]
	  M[6] = 2 * b * c + 2 * this.s * a; // M[2][1]
	  M[7] = 0.0f; // M[3][1]

	  M[8] = 2 * a * c + 2 * this.s * b; // M[0][2]
	  M[9] = 2 * b * c - 2 * this.s * a; // M[1][2]
	  M[10] = 1 - 2 * a * a - 2 * b * b; // M[2][2]
	  M[11] = 0.0f; // M[3][2]

	  M[12] = 0.0f; // M[0][3]
	  M[13] = 0.0f; // M[1][3]
	  M[14] = 0.0f; // M[2][3]
	  M[15] = 1.0f; // M[3][3]

	  return M;
  }

}
