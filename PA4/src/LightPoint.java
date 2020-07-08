/*
 * PointLight.java
 * 
 * Outpits light source from a specific positions
 * 
 */
public class LightPoint extends Light {
	public Point3D direction;
	public Point3D lightPosition;
	public ColorType color;
	public Boolean radial = false;
	public Boolean angular = false;
	public float alpha;
	private float a0,a1,a2;
	
	public LightPoint(ColorType c, Point3D _direction, Point3D _lightPosition) {
		color = c;
		direction = _direction;
		lightPosition = _lightPosition;
		a0 = 1;
		a1 = a2 = .000001f;
		alpha = 45;
		super.lightIsOn = true;
	}
	
	public ColorType applyLight(Material mat, Point3D v, Point3D n, Point3D point) {
		ColorType res = new ColorType();
		
		Point3D L = lightPosition.minus(point);
		L.normalize();
		double dot = L.dotProduct(n);
		if (dot > 0.0) {
			if (mat.isDiffuse()) {
				res.r = (float)(dot * mat.get_kd().r * color.r);
				res.g = (float)(dot * mat.get_kd().g * color.g);
				res.b = (float)(dot * mat.get_kd().b * color.b);
			}
			
			if (mat.isSpecular()) {
				Point3D r = L.reflect(n);
				dot = r.dotProduct(v);
				if (dot > 0.0) {
					res.r += (float)Math.pow((dot * mat.get_ks().r * color.r), mat.get_ns());
					res.g += (float)Math.pow((dot * mat.get_ks().g * color.g), mat.get_ns());
					res.r += (float)Math.pow((dot * mat.get_ks().b * color.b), mat.get_ns());
				}
			}
			
			if (radial) {
				float d = (float) Math.sqrt(Math.pow(lightPosition.x - point.x, 2) + Math.pow(lightPosition.y - point.y, 2) + Math.pow(lightPosition.z - point.z, 2));
				float radialFactor = 1 / (a0 + a1 * d + a2 * (float)Math.pow(d, 2));
				res.r *= radialFactor;
				res.g *= radialFactor;
				res.b *= radialFactor;
			}
			
			if (angular) {
				dot = L.dotProduct(direction);
				if (dot < Math.cos(1.57079633)) {
					res.r *= Math.pow(dot, alpha);
					res.g *= Math.pow(dot, alpha);
					res.b *= Math.pow(dot, alpha);
				}
			}
			
			// Clamp
			res.r = (float)Math.min(1.0, res.r);
			res.g = (float)Math.min(1.0, res.g);
			res.b = (float)Math.min(1.0, res.b);
		}
		return(res);
	}
	
	public void toggleRadial() {
		radial = !radial;
	}
	
	public void toggleAngular() {
		angular = !angular;
	}
	
	public void toggleLight() {
		lightIsOn = !lightIsOn;
	}
}
