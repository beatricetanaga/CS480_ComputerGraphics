/**
 * AmbientLight.java
 * 
 * Ambient light source to illuminate from all directions
 * 
 *
 **/
public class AmbientLight extends Light {

	public Point3D direction;
	public ColorType color;
	
	public AmbientLight(ColorType color, Point3D direction) {
		this.color = color;
		this.direction = direction;
		super.lightIsOn = true;
	}
	
	public ColorType applyLight(Material mat, Point3D v, Point3D n, Point3D p) {
		ColorType res = new ColorType();
		
		if (mat.isAmbient()) {
			res.r = (float) (color.r * mat.get_ka().r);
			res.g = (float) (color.g * mat.get_ka().g);
			res.b = (float) (color.b * mat.get_ka().b);
		}

		// Clamp
		res.r = (float) Math.min(1.0, res.r);
		res.g = (float) Math.min(1.0, res.g);
		res.b = (float) Math.min(1.0, res.b);
		return(res);
	}
	
	public void toggleLight() {
		lightIsOn = !lightIsOn;
	}
}
