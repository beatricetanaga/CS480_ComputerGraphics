//****************************************************************************
//      Torus class
//****************************************************************************
// Creates model for Torus solid

public class Torus3D extends Shape {
	private Point3D center;
	private float r, r_axial;
	private int rings, nsides;
	public Mesh3D mesh;
	
	private float umin = (float)-Math.PI;
	private float umax = (float)Math.PI;
	private float vmin = (float)-Math.PI;
	private float vmax = (float)Math.PI;
	
	public Torus3D(float _x, float _y, float _z, float _r, float _r_axial, int _nsides, int _rings, Material mat) 
	{
		center = new Point3D(_x,_y,_z);
		r = _r;
		r_axial = _r_axial;
		rings = _rings;
		nsides = _nsides;
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
	
	public void setRadius(float r) 
	{
		this.r = r;
		fillMesh();
	}
	
	public void set_radius(float _r)
	{
		r = _r;
		fillMesh(); // update the triangle mesh
	}
	
	public void set_axial(float _r_axial) 
	{
		r_axial = _r_axial;
		fillMesh();
	}
	
	public void set_nsides(int _nsides)
	{
		nsides = _nsides;
		initMesh();
	}
	
	public void set_rings(int _rings) 
	{
		rings = _rings;
		initMesh();
	}
	
	public Point3D get_center() 
	{
		return center;
	}
	
	public float get_radius() 
	{
		return r;
	}
	
	public float get_axial() 
	{
		return r_axial;
	}
	
	public int get_m()
	{
		return rings;
	}
	
	public int get_n()
	{
		return nsides;
	}
	
	private void initMesh()
	{
		mesh = new Mesh3D(rings, nsides);
		fillMesh();  // set the mesh vertices and normals
	}
	
	// fill the triangle mesh vertices and normals
	// using the current parameters for the sphere
	private void fillMesh() 
	{
		// ****************Implement Code here*******************//
		int i, j;
		float theta, phi;
		float d_phi = (umax-umin)/((float)nsides-1);
		float d_theta = (vmax-vmin)/((float)rings-1);
		
		float cos_theta, sin_theta, cos_phi, sin_phi;
		
		Point3D du = new Point3D();
		Point3D dv = new Point3D();
		
		for (i = 0, theta = vmin; i < rings; ++i, theta += d_theta){
			cos_theta = (float)Math.cos(theta);
			sin_theta = (float)Math.sin(theta);
			
			for (j = 0, phi = umin; j < nsides; ++j, phi += d_phi) {
				cos_phi = (float)Math.cos(phi);
				sin_phi = (float)Math.sin(phi);
				
				// Compute vertex
				mesh.v[i][j].x = center.x + (r_axial + r * cos_phi) * cos_theta;
				mesh.v[i][j].y = center.y + (r_axial + r * cos_phi) * sin_theta;
				mesh.v[i][j].z = center.z + r * sin_phi;
				
				// Compute unit normal at vertex
				du.x = -(r_axial + r * cos_phi) * sin_theta;
				du.y = (r_axial + r * cos_phi) * cos_theta;
				du.z = 0;
				
				dv.x = -r * sin_phi * cos_theta;
				dv.y = -r * sin_phi * sin_theta;
				dv.z = r * cos_phi;
				
				du.crossProduct(dv, mesh.n[i][j]);
				mesh.n[i][j].normalize();
			}
		}
	}
}
