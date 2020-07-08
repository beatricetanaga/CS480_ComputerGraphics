/** Ellipsoid Model **/

public class Ellipsoid3D extends Shape
{
	private Point3D center;
	private float rx, ry, rz;
	private int stacks, slices;
	public Mesh3D mesh;
	
	private float umin = (float)-Math.PI/2;
	private float umax = (float)Math.PI/2;
	private float vmin = (float)-Math.PI;
	private float vmax = (float)Math.PI;
	
	
	public Ellipsoid3D(float _x, float _y, float _z, float _rx, float _ry, float _rz, int _stacks, int _slices, Material mat) 
	{
		center = new Point3D(_x,_y,_z);
		rx = _rx;
		ry = _ry;
		rz = _rz;
		stacks = _stacks;
		slices = _slices;
		super.mat = mat;
		initMesh();
	}
	
	public void set_center(float _x, float _y, float _z)
	{
		center.x=_x;
		center.y=_y;
		center.z=_z;
		fillMesh();  // update the triangle mesh
	}
	
	public Point3D get_center(){
		return center;
	}
	
	public void set_radius(float _rx, float _ry, float _rz)
	{
		rx = _rx;
		ry = _ry;
		rz = _rz;
		fillMesh(); // update the triangle mesh
	}
	
	public void set_stacks(int _stacks) 
	{
		stacks = _stacks;
		initMesh();
	}
	
	public void set_slices(int _slices) 
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
	
	public float get_rz() 
	{
		return rz;
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
		fillMesh(); // set the mesh vertices and normals
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
				
				mesh.v[i][j].x = center.x + rx * cos_phi * cos_theta;
				mesh.v[i][j].y = center.y + ry * cos_phi * sin_theta;
				mesh.v[i][j].z = center.z + rz * sin_phi;
				
				du.x = -(center.x + rx * cos_phi) * sin_theta;
				du.y = (center.y + ry * cos_phi) * cos_theta;
				du.z = 0;
				
				dv.x = -rx * sin_phi * cos_theta;
				dv.y = -ry * sin_phi * sin_theta;
				dv.z = rz * cos_phi;
				
				du.crossProduct(dv, mesh.n[i][j]);
				mesh.n[i][j].normalize();
			}
		}
	}
}
