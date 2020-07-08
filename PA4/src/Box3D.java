/** Box Model **/

public class Box3D extends Shape{
	private Point3D center, v, uVector, vVector;
	private float r;
	private int stacks, slices;
	public Mesh3D[] meshes;
	
	private float umin = -1;
	private float umax = 1;
	private float vmin = -1;
	private float vmax = 1;
	
	public Box3D(float _x, float _y, float _z, float _r, int _stacks, int _slices, Material mat) {
		center = new Point3D(_x, _y, _z);
		r = _r;
		stacks = _stacks;
		slices = _slices;
		uVector = new Point3D(0, -1*r, 0);
		vVector = new Point3D(0, 0, 1*r);
		v = center.plus(new Point3D(-r, 0, 0));
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
	
	public void setRadius(float _r) 
	{
		r = _r;
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
	
	public Point3D get_center() 
	{
		return center;
	}
	
	public float getRadius() 
	{
		return r;
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
		meshes = new Mesh3D[6];
		fillMesh();
	}
	
	private void fillMesh() {
		int i, j;
		float theta, phi;
		float d_phi = (umax-umin)/((float)slices-1);
		float d_theta = (vmax-vmin)/((float)stacks-1);
		
		for (int side = 0; side < 6; ++side) {
			meshes[side] = new Mesh3D(stacks,slices);
			if(side == 1) {
				v = center.plus(new Point3D(r, 0, 0));
				// negative for normal
				uVector = uVector.scale(-1);
			} else if (side == 2) {
				v = center.plus(new Point3D(0, -r, 0));
				uVector = new Point3D(1*r, 0, 0);
				vVector = new Point3D(0, 0, 1*r);
			} else if (side == 3) {
				v = center.plus(new Point3D(0, r, 0));
				// Swap for normals
				Point3D temp = uVector;
				uVector = vVector;
				vVector = temp; 
			} else if (side == 4) {
				v = center.plus(new Point3D(0, 0, -r));
				uVector = new Point3D(-1*r, 0, 0);
				vVector = new Point3D(0, 1*r, 0);
			} else if (side == 5) {
				v = center.plus(new Point3D(0, 0, r));
				// negative for normal
				uVector = uVector.scale(-1);
			}
			
			for (i = 0, theta = vmin; i < stacks; ++i, theta += d_theta) {
				for (j = 0, phi = umin; j < slices; ++j, phi += d_phi) {
					

					meshes[side].v[i][j] = v.plus(uVector.scale(theta).plus(vVector.scale(phi)));

					meshes[side].n[i][j] = uVector.crossProduct(vVector);
					meshes[side].n[i][j].normalize();
				}
			}
		}
	}
}
