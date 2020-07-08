/** Cylinder Model **/
public class Cylinder3D extends Shape
{
	private Point3D center;
	private float rx, ry;
	private int stacks, slices;
	public Mesh3D mesh;
	public Mesh3D bottom, top;
	
	private float umin, umax;
	private float vmin = (float)-Math.PI;
	private float vmax = (float)Math.PI;
	
	public Cylinder3D(float _x, float _y, float _z, float _rx, float _ry, int _stacks, int _slices, float _u, Material mat) 
	{
		center = new Point3D(_x, _y, _z);
		rx = _rx;
		ry = _ry;
		stacks = _stacks;
		slices = _slices;
		umin = -_u;
		umax = _u;
		super.mat = mat;
		initMesh();
	}
	
	public void set_center(float _x, float _y, float _z) 
	{
		center.x = _x;
		center.y = _y;
		center.z = _z;
		fillMesh();
	}
	
	public Point3D get_center(){
		return center;
	}
	
	public void set_rx(float _rx) 
	{
		rx = _rx;
		fillMesh();
	}
	
	public void set_ry(float _ry) 
	{
		ry = _ry;
		fillMesh();
	}
	
	public void set_m(int _stacks)
	{
		stacks = _stacks;
		initMesh();
	}
	
	public void set_n(int _slices) 
	{
		slices = _slices;
		initMesh();
	}
	
	public float get_rx() 
	{
		return rx;
	}
	
	public float get_ry() 
	{
		return ry;
	}
	
	public int get_m()
	{
		return stacks;
	}
	
	public int get_n()
	{
		return slices;
	}
	
	private void initMesh() 
	{
		mesh = new Mesh3D(stacks,slices);
		bottom = new Mesh3D(1,stacks);
		top = new Mesh3D(1,stacks);
		fillMesh();
	}
	
	private void fillMesh()
	{
		int i, j;
		float theta, phi;
		float d_phi = (umax-umin)/((float)slices-1);
		float d_theta = (vmax-vmin)/((float)stacks-1);
		
		float cos_theta, sin_theta, cos_phi, sin_phi;
		
		Point3D du = new Point3D();
		Point3D dv = new Point3D();
		
		for (i = 0, theta = vmin; i < stacks; ++i, theta += d_theta)
		{
			cos_theta = (float)Math.cos(theta);
			sin_theta = (float)Math.sin(theta);
			
			for (j = 0, phi = umin; j < slices; ++j, phi += d_phi) 
			{
				cos_phi = (float)Math.cos(phi);
				sin_phi = (float)Math.sin(phi);
				
				mesh.v[i][j].x = center.x + rx * cos_theta;
				mesh.v[i][j].y = center.y + ry * sin_theta;
				mesh.v[i][j].z = center.z + phi;

				du.x = -rx * sin_theta;
				du.y = ry * cos_theta;
				du.z = 0;
				
				dv.x = 0;
				dv.y = 0;
				dv.z = 1;
				
				du.crossProduct(dv, mesh.n[i][j]);
				mesh.n[i][j].normalize();
			}
		}
		
		// Building Endcaps
		for (i = 0, theta = vmin; i < stacks; ++i, theta += d_theta)
		{
			mesh.n[i][0] = new Point3D(0,0,-1);
			mesh.v[i][0] = new Point3D(center.x,center.y,center.z+umin);
			
			mesh.n[i][slices-1] = new Point3D(0,0,1);
			mesh.v[i][slices-1] = new Point3D(center.x,center.y,center.z+umax);
		}
	}
}
