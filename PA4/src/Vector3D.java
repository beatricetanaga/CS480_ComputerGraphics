
public class Vector3D
{
	public int x, y, z;
	public float u, v, w; // uv coordinates for texture mapping
	public ColorType c;
	public Point3D n;
	public Vector3D(int _x, int _y, int _z, ColorType _c)
	{
		u = 0;
		v = 0;
		w = 0;
		x = _x;
		y = _y;
		z = _z;
		c = _c;
	}
	public Vector3D(int _x, int _y, int _z, ColorType _c, float _u, float _v, float _w)
	{
		u = _u;
		v = _v;
		w = _w;
		x = _x;
		y = _y;
		z = _z;
		c = _c;
	}
	public Vector3D()
	{
		c = new ColorType(1.0f, 1.0f, 1.0f);
	}
	public Vector3D( Vector3D p)
	{
		u = p.u;
		v = p.v;
		w = p.w;
		x = p.x;
		y = p.y;
		z = p.z;
		c = new ColorType(p.c.r, p.c.g, p.c.b);
	}
	public Point3D toVector() {
		return new Point3D(x, y, z);
	}
}