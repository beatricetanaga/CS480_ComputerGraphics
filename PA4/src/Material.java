/*
 * Material.java
 * 
 * Class for a surface material containing ambient, diffuse and specular properties
 * 
 */
public class Material 
{
	public ColorType k_a, k_d, k_s;
	private int n_s;
	public boolean specular, ambient, diffuse, attenuation;
	
	public Material(ColorType _ka, ColorType _kd, ColorType _ks, int _ns) {
		k_s = new ColorType(_ks);  // specular coefficient for r,g,b
		k_a = new ColorType(_ka);  // ambient coefficient for r,g,b
		k_d = new ColorType(_kd);  // diffuse coefficient for r,g,b
		n_s = _ns;  // specular exponent
		
		// set boolean variables 
		specular = (n_s>0 && (k_s.r > 0.0 || k_s.g > 0.0 || k_s.b > 0.0));
		diffuse = (k_d.r > 0.0 || k_d.g > 0.0 || k_d.b > 0.0);
		ambient = (k_a.r > 0.0 || k_a.g > 0.0 || k_a.b > 0.0);
		attenuation = true;
	}
	
	public Material(Material mat) {
		k_a = new ColorType(mat.k_a);
		k_d = new ColorType(mat.k_d);
		k_s = new ColorType(mat.k_s);
		n_s = mat.n_s;
		specular = mat.specular;
		diffuse = mat.diffuse;
		ambient = mat.ambient;
	}
	
	public Material copy() {
		return new Material(k_a, k_d, k_s, n_s);
	}
	
	public ColorType get_ka() {
		return k_a;
	}
	
	public ColorType get_kd() {
		return k_d;
	}
	
	public ColorType get_ks() {
		return k_s;
	}
	
	public int get_ns() {
		return n_s;
	}
	
	public boolean isDiffuse() {
		return diffuse;
	}
	
	public boolean isAmbient() {
		return ambient;
	}
	
	public boolean isSpecular() {
		return specular;
	}
}
