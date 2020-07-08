//****************************************************************************
//       3D triangle mesh with normals
//****************************************************************************
// History :
//   Nov 6, 2014 Created by Stan Sclaroff
//

public class Mesh3D {
	public int cols, rows;
	public Point3D[][] v;
	public Point3D[][] n;
	
	public Mesh3D(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		v = new Point3D[this.rows][this.cols];
		n = new Point3D[this.rows][this.cols];
		
		for(int i = 0; i < this.rows; ++i) {
			for(int j = 0; j < this.cols; ++j) {
				v[i][j] = new Point3D();
				n[i][j] = new Point3D();
			}
		}
	}
	
	public void rotate_mesh (Quaternion q, Point3D center) {
		Quaternion q_inv = q.conjugate();
		Point3D rotVec;
		
		Quaternion p;
		
		for(int i = 0; i < rows; ++i) {
			for(int j = 0; j < cols; ++j) {
				// Apply rotation to vertex
				p = new Quaternion(0.0f, v[i][j].minus(center));
				p = q.multiply(p);
				p = p.multiply(q_inv);
				rotVec = p.get_v();
				v[i][j] = rotVec.plus(center);
				
				// Apply rotation to normal
				p = new Quaternion(0.0f, n[i][j]);
				p = q.multiply(p);
				p = p.multiply(q_inv);
				n[i][j] = p.get_v();
			}
		}
	}
}
