import java.awt.image.BufferedImage;

public class TriangleFan {
	public Point3D center;
	public Mesh3D mesh;
	
	public TriangleFan(float _x, float _y, float _z, Mesh3D mesh) {
		this.center = new Point3D(_x, _y, _z);
		this.mesh = mesh;
	}
	
	public void drawTriangleFan(BufferedImage buff, DepthBuffer depthBuff, Boolean do_smooth, Vector3D[] points, ColorType center_color) {
		Vector3D previous = points[points.length-1];
		Vector3D centerPoint = new Vector3D((int)center.x, (int)center.y, (int)center.z, center_color);
		for (Vector3D point : points) {
			Triangle.drawTriangle(buff, depthBuff, centerPoint, previous, point, do_smooth);
			previous = point;
		}
	}
}
