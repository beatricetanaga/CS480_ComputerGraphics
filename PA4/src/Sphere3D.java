//****************************************************************************
//      Sphere class
//****************************************************************************
// creates model for sphere

public class Sphere3D extends Shape {
	private Point3D center;
	private float r;
	private int stacks, slices;
	public Mesh3D mesh;
	
	private float umin = (float)-Math.PI/2;
	private float umax = (float)Math.PI/2;
	private float vmin = (float)-Math.PI;
	private float vmax = (float)Math.PI;
	
	
	public Sphere3D(float _x, float _y, float _z, float _r, int _stacks, int _slices, Material mat) {
		center = new Point3D(_x, _y, _z);
		r = _r;
		stacks = _stacks;
		slices = _slices;
		super.mat = mat;
		initMesh();
	}
	
	public void set_center(float _x, float _y, float _z) {
		center.x=_x;
		center.y=_y;
		center.z=_z;
		fillMesh();  // update the triangle mesh
	}
	
	public Point3D get_center(){
		return center;
	}
	
	public void set_radius(float _r)
	{
		r = _r;
		fillMesh(); // update the triangle mesh
	}
	
	public void set_stacks(int _stacks) {
		stacks = _stacks;
		initMesh(); // resized the mesh, must re-initialize
	}
	
	public void set_slices(int _slices)
	{
		slices = _slices;
		initMesh(); // resized the mesh, must re-initialize
	}
	
	public float getRadius() {
		return r;
	}
	
	public int get_n()
	{
		return slices;
	}
	
	public int get_m()
	{
		return stacks;
	}
	
	private void initMesh() 
	{
		mesh = new Mesh3D(stacks,slices);
		fillMesh(); // set the mesh vertices and normals
	}
	
	// fill the triangle mesh vertices and normals
	// using the current parameters for the sphere
	private void fillMesh() 
	{
		// ****************Implement Code here*******************//
		int i, j;
		float theta, phi;
		float d_phi = (umax-umin)/((float)slices-1);
		float d_theta = (vmax-vmin)/((float)stacks-1);
		
		float cos_theta, sin_theta, cos_phi, sin_phi;
		
		for (i = 0, theta = vmin; i < stacks; ++i, theta += d_theta){
			cos_theta = (float)Math.cos(theta);
			sin_theta = (float)Math.sin(theta);
			
			for (j = 0, phi = umin; j < slices; ++j, phi += d_phi) {
				cos_phi = (float)Math.cos(phi);
				sin_phi = (float)Math.sin(phi);
				
				mesh.v[i][j].x = center.x+r*cos_phi*cos_theta;
				mesh.v[i][j].y = center.y+r*cos_phi*sin_theta;
				mesh.v[i][j].z = center.z+r*sin_phi;
				
				mesh.n[i][j].x = cos_phi * cos_theta;
				mesh.n[i][j].y = cos_phi * sin_theta;
				mesh.n[i][j].z = sin_phi;
				
				mesh.n[i][j].normalize();
			}
		}
	}
}
