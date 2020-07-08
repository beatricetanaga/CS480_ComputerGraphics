//****************************************************************************
//       Infinite light source class
//****************************************************************************
// History :
//   Nov 6, 2014 Created by Stan Sclaroff
//

public class LightInfinite extends Light {
	public Point3D direction;
	public ColorType color;
	
	public LightInfinite(ColorType color, Point3D direction) {
		this.color = new ColorType(color);
		this.direction = new Point3D(direction);
		super.lightIsOn = true;
	}
	
	public ColorType applyLight(Material mat, Point3D v, Point3D n, Point3D p) {
		ColorType res = new ColorType();
		
		double dot = direction.dotProduct(n);
		
		if (dot > 0.0) {
			if (mat.isDiffuse()) {
				res.r += (float)(dot * mat.get_kd().r * color.r);
				res.g += (float)(dot * mat.get_kd().g * color.g);
				res.b += (float)(dot * mat.get_kd().b * color.b);
				
			}
			
			if (mat.isSpecular()) {
				Point3D r = direction.reflect(n);
				dot = r.dotProduct(v);
				if (dot > 0.0) {
					res.r += (float)Math.pow((dot * mat.get_ks().r * color.r), mat.get_ns());
					res.g += (float)Math.pow((dot * mat.get_ks().g * color.g), mat.get_ns());
					res.b += (float)Math.pow((dot * mat.get_ks().b * color.b), mat.get_ns());
				}
			}
			
			// Clamp
			res.r = (float)Math.min(1.0, res.r);
			res.g = (float)Math.min(1.0, res.g);
			res.b = (float)Math.min(1.0, res.b);
		}
		return(res);
	}
	
	public void toggleLight() {
		lightIsOn = !lightIsOn;
	}
}
