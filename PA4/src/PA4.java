/*
 * PA4.java
 * 
 * Author: Beatrice Tanaga (btanaga@bu.edu)
 * Boston University CS480
 * 
 * Runs a depth buffering and shaded rendering program
 * 
 * Supports Flat, Gouraud, and Phong shading
 * Infinite Light, Point Light (radial and angular attenuation, and Ambient Light (including colored light)
 * Spheres, Ellipsoids, Toruses, Cylinders, and Boxes
 * Ambient, Diffuse, and Specular light toggles
 * 
 */

import javax.swing.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.*;
import java.awt.image.*;
//import java.io.File;
//import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;

//import javax.imageio.ImageIO;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.awt.GLCanvas;//for new version of gl
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;

import com.jogamp.opengl.util.FPSAnimator;//for new version of gl

public class PA4 extends JFrame 
	implements GLEventListener, KeyListener, MouseListener, MouseMotionListener 
{
	private static final long serialVersionUID = 1L;
	private final int DEFAULT_WINDOW_WIDTH = 800;
	private final int DEFAULT_WINDOW_HEIGHT = 512;
	private final float DEFAULT_LINE_WIDTH = 1.0f;

	private GLCapabilities capabilities;
	private GLCanvas canvas;
	private FPSAnimator animator;

	final private int numTestCase;
	private int testCase;
	private BufferedImage buff;
	public int[][] d_buff;
	@SuppressWarnings("unused")
	private ColorType color;
	private Random rng;

	/** specular exponent for materials **/
	private int ns = 5;

	private ArrayList<Point2D> lineSegs;
	private ArrayList<Point2D> triangles;
	private boolean doSmoothShading;
	private boolean phong;
	private boolean flat;
	private boolean gouraud;
	private int Nsteps;

	/** The quaternion which controls the rotation of the world. */
	private Quaternion viewing_quaternion = new Quaternion();
	private Point3D viewing_center = new Point3D((float)(DEFAULT_WINDOW_WIDTH/2),(float)(DEFAULT_WINDOW_HEIGHT/2),(float)0.0);
	/** The last x and y coordinates of the mouse press. */
	private int last_x = 0, last_y = 0;
	/** Whether the world is being rotated. */
	private boolean rotate_world = false;
	
	/** Random colors **/
    private ColorType[] colorMap = new ColorType[100];
    private Random rand = new Random();
    
	private List<Shape> objects;
	
	private Light light;
	private Light selectedLight;
	private boolean toggleLights;
	private boolean lightsInitialized;
	private ColorType k_a, k_d, k_s, kd_temp, ka_temp;
	private Boolean specular, diffuse, ambient;
	
	private boolean ellipsoid_scaled = false;
  	private boolean sphere_scaled = false;
  	private boolean torus_scaled = false;
  	private boolean cylinder_scaled = false;
  	private boolean box_scaled = false;
  	private boolean ellipsoid_rotated = false;
  	private boolean sphere_rotated = false;
  	private boolean torus_rotated = false;
  	private boolean cylinder_rotated = false;
  	private boolean box_rotated = false;
  	private boolean ellipsoid_translated = false;
  	private boolean sphere_translated = false;
  	private boolean torus_translated = false;
  	private boolean cylinder_translated = false;
  	private boolean box_translated = false;
  	
  	private int buff_x;
  	private int buff_y;
  	private int renderingModel=0;

	public PA4() 
	{
		capabilities = new GLCapabilities(null);
		capabilities.setDoubleBuffered(true);

		canvas = new GLCanvas(capabilities);
		canvas.addGLEventListener(this);
		canvas.addMouseListener(this);
		canvas.addMouseMotionListener(this);
		canvas.addKeyListener(this);
		canvas.setAutoSwapBufferMode(true); // true by default. Just to be explicit
		canvas.setFocusable(true);
		getContentPane().add(canvas);

		animator = new FPSAnimator(canvas, 60); // drive the display loop @ 60 FPS

		numTestCase = 5;
		testCase = 0;
		Nsteps = 12;

		setTitle("CS480/680 PA4");
		setSize(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setResizable(false);

		rng = new Random();
		color = new ColorType(1.0f, 0.0f, 0.0f);
		lineSegs = new ArrayList<Point2D>();
		triangles = new ArrayList<Point2D>();
		doSmoothShading = false;
		
		flat = true;
		phong = false;
		gouraud = false;
		
		objects = new ArrayList<Shape>();
		
		light = new Light();
		selectedLight = null;
		toggleLights = false;
		
		float r = rng.nextFloat();
		float g = rng.nextFloat();
		float b = rng.nextFloat();
		k_a = new ColorType(r/6, g/6, b/6);
		k_s = new ColorType(1.0, 1.0, 1.0);
		k_d = new ColorType(r,g,b);
		
		// Temps for toggles
		kd_temp = new ColorType(k_d);
		ka_temp = new ColorType(k_a);
		
		specular = true;
		diffuse = true;
		ambient = true;
		
		for (int i=0; i<100; i++) {
	    	this.colorMap[i] = new ColorType(i*0.005f+0.5f, i*-0.005f+1f, i*0.0025f+0.75f);
	    }
	}

	public void run() {
		animator.start();
	}

	public static void main(String[] args) {
		PA4 P = new PA4();
		P.run();
	}

	// ***********************************************
	// GLEventListener Interfaces
	// ***********************************************
	public void init(GLAutoDrawable drawable) {
		GL gl = drawable.getGL();
		gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		gl.glLineWidth(DEFAULT_LINE_WIDTH);
		Dimension sz = this.getContentPane().getSize();
		buff = new BufferedImage(sz.width, sz.height, BufferedImage.TYPE_3BYTE_BGR);
		buff_x = sz.width;
	    buff_y = sz.height;
		clearPixelBuffer();
	}

	// Redisplaying graphics
	public void display(GLAutoDrawable drawable) 
	{
		GL2 gl = drawable.getGL().getGL2();
		WritableRaster wr = buff.getRaster();
		DataBufferByte dbb = (DataBufferByte) wr.getDataBuffer();
		byte[] data = dbb.getData();

		gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);
		gl.glDrawPixels(buff.getWidth(), buff.getHeight(), GL2.GL_BGR,
				GL2.GL_UNSIGNED_BYTE, ByteBuffer.wrap(data));
		
		drawTestCase();
	}

	// Window size change
	public void reshape(GLAutoDrawable drawable, int x, int y, int w, int h) 
	{
		// deliberately left blank
	}

	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) 
	{
		// deliberately left blank
	}

	void clearPixelBuffer() {
		lineSegs.clear();
		triangles.clear();
		Graphics2D g = buff.createGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, buff.getWidth(), buff.getHeight());
		g.dispose();
	}

	// drawTest
	void drawTestCase() 
	{
		clearPixelBuffer();


		switch (testCase) {
		case 0:
			testOne();
			break;
		}
		
	}

	// ***********************************************
	// KeyListener Interfaces
	// ***********************************************
	public void keyTyped(KeyEvent key) {
		// Q,q: quit
		// C,c: clear pixel buffer
		// R,r: randomly change the color
		// P,p: toggle phong shading
		// F,f: toggle flat shading
		// G,g: toggle gouraud shading
		// A,a: toggle ambient term
		// S,s: toggle specular term
		// D,d: toggle diffuse term
		// L,l: toggle light control
		// 1,2: toggle lights on and off
		// T,t: show testing examples
		// >: increase the step number for examples
		// <: decrease the step number for examples
		// +,-: increase or decrease spectral exponent

		switch (key.getKeyChar()) {
		case 'Q':
		case 'q':
			new Thread() {
				public void run() {
					animator.stop();
				}
			}.start();
			System.exit(0);
			break;
			
		case 'L':
		case 'l':
			toggleLights = !toggleLights;
			break;
		
		/** Toggle lights on/off **/
		case '1':
			if (toggleLights) {
				if (light.lights.size() >= 1) {
					light.lights.get(0).toggleLight();
				}
			}
			break;	
		case '2':
			if (toggleLights) {
				if (light.lights.size() >= 2) {
					light.lights.get(1).toggleLight();
				}
			}
			break;
		
		/** Random color generator **/
		case 'R':
		case 'r':
			k_d = new ColorType(rng.nextFloat(), rng.nextFloat(),
					rng.nextFloat());
			k_a = new ColorType(k_d.r/6, k_d.g/6, k_d.b/6);
			kd_temp = new ColorType(k_d);
			ka_temp = new ColorType(k_a);
			break;
			
		case 'C':
		case 'c':
			clearPixelBuffer();
			break;
		
		/** Different Lighting **/
		case 'S':
		case 's':
			if (specular) {
				specular = false;
				k_s = new ColorType(0.0, 0.0, 0.0);
			} else {
				specular = true;
				k_s = new ColorType(1.0, 1.0, 1.0);
			}
			break;
		case 'D':
		case 'd':
			if (diffuse) {
				diffuse = false;
				kd_temp = new ColorType(k_d);
				k_d = new ColorType(0.0, 0.0, 0.0);
			} else {
				diffuse = true;
				k_d = new ColorType(kd_temp);
			}
			break;
		case 'A':
		case 'a':
			if (ambient) {
				ambient = false;
				ka_temp = new ColorType(k_a);
				k_a = new ColorType(0.0, 0.0, 0.0);
			} else {
				ambient = true;
				k_a = new ColorType(ka_temp);
			}
			break;
		
		/** Three rendering methods **/
		case 'G':
		case 'g':
			gouraud = true;
			flat = phong = false;
			drawTestCase();
			break;
		case 'F':
		case 'f':
			flat = true;
			gouraud = phong = false;
			drawTestCase();
			break;
		case 'P':
		case 'p':
			phong = true;
			flat = gouraud = false;
			drawTestCase();
			break;
			
		/** Individual Model Scaling **/
	    case 'x':
	    	sphere_scaled = !sphere_scaled;
	    	break;
	    case 'v':
	    	ellipsoid_scaled = !ellipsoid_scaled;
	    	break;
	    case 'b':
	    	torus_scaled = !torus_scaled;
	    	break;
	    case 'n':
	    	cylinder_scaled = !cylinder_scaled;
	    	break;
	    case 'm':
	    	box_scaled = !box_scaled;
	    	break;
	    
	    /** Individual Model Rotation **/
	    case 'X':
	    	sphere_rotated = !sphere_rotated;
	    	break;
	    case 'V':
	    	ellipsoid_rotated = !ellipsoid_rotated;
	    	break;
	    case 'B':
	    	torus_rotated = !torus_rotated;
	    	break;
	    case 'N':
	    	cylinder_rotated = !cylinder_rotated;
	    	break;
	    case 'M':
	    	box_rotated = !box_rotated;
	    	break;
	    
	    /** Individual Model Translation **/
	    case '6':
	    	sphere_translated = !sphere_translated;
	    	break;
	    case '7':
	    	ellipsoid_translated = !ellipsoid_translated;
	    	break;
	    case '8':
	    	torus_translated = !torus_translated;
	    	break;
	    case '9':
	    	cylinder_translated = !cylinder_translated;
	    	break;
	    case '0':
	    	box_translated = !box_translated;
	    	break;
		
		/** Switch test cases **/
		case 'T':
		case 't':
			testCase = (testCase + 1) % numTestCase;
			lightsInitialized = false;
			drawTestCase();
			break;
			
		case '<':
			Nsteps = Nsteps < 4 ? Nsteps : Nsteps / 2;
			System.out.printf("Nsteps = %d \n", Nsteps);
			drawTestCase();
			break;
		case '>':
			Nsteps = Nsteps > 190 ? Nsteps : Nsteps * 2;
			System.out.printf("Nsteps = %d \n", Nsteps);
			drawTestCase();
			break;
			
		case '+':
			ns++;
			drawTestCase();
			break;
		case '-':
			if (ns > 0)
				ns--;
			drawTestCase();
			break;
			
		default:
			break;
		}
	}

	public void keyPressed(KeyEvent key) 
	{
		switch (key.getKeyCode()) 
		{
		case KeyEvent.VK_ESCAPE:
			new Thread() {
				public void run() 
				{
					animator.stop();
				}
			}.start();
			System.exit(0);
			break;
		 default:
			break;
		}
	}

	public void keyReleased(KeyEvent key) 
	{
		// deliberately left blank
	}

	// **************************************************
	// MouseListener and MouseMotionListener Interfaces
	// **************************************************
	public void mouseClicked(MouseEvent mouse) 
	{
		// deliberately left blank
	}

	public void mousePressed(MouseEvent mouse) 
	{
		int button = mouse.getButton();
		if (button == MouseEvent.BUTTON1) {
			last_x = mouse.getX();
			last_y = mouse.getY();
			rotate_world = true;
		}
	}

	public void mouseReleased(MouseEvent mouse) 
	{
		int button = mouse.getButton();
		if (button == MouseEvent.BUTTON1) 
		{
			rotate_world = false;
		}
	}

	public void mouseMoved(MouseEvent mouse) {
		// Deliberately left blank
	}

	/**
	 * Updates the rotation quaternion as the mouse is dragged.
	 * 
	 * @param mouse
	 *            The mouse drag event object.
	 */
	public void mouseDragged(final MouseEvent mouse) {
		if (this.rotate_world) {
			// get the current position of the mouse
			final int x = mouse.getX();
			final int y = mouse.getY();

			// get the change in position from the previous one
			final int dx = x - this.last_x;
			final int dy = y - this.last_y;

			// create a unit vector in the direction of the vector (dy, dx, 0)
			final float magnitude = (float) Math.sqrt(dx * dx + dy * dy);
			if (magnitude > 0.0001) {
				// define axis perpendicular to (dx,-dy,0)
				// use -y because origin is in upper lefthand corner of the
				// window
				final float[] axis = new float[] { -(float) (dy / magnitude),
						(float) (dx / magnitude), 0 };

				// calculate appropriate quaternion
				final float viewing_delta = 3.1415927f / 180.0f;
				final float s = (float) Math.sin(0.5f * viewing_delta);
				final float c = (float) Math.cos(0.5f * viewing_delta);
				final Quaternion Q = new Quaternion(c, s * axis[0],
						s * axis[1], s * axis[2]);
				this.viewing_quaternion = Q.multiply(this.viewing_quaternion);

				// normalize to counteract acccumulating round-off error
				this.viewing_quaternion.normalize();

				// save x, y as last x, y
				this.last_x = x;
				this.last_y = y;
				drawTestCase();
			}
		}

	}

	public void mouseEntered(MouseEvent mouse) {
		// Deliberately left blank
	}

	public void mouseExited(MouseEvent mouse) {
		// Deliberately left blank
	}

	public void dispose(GLAutoDrawable drawable) {
		// TODO Auto-generated method stub

	}

	
	/** TESTCASE **/
	void testOne() {
		float radius = (float) 50.0;
		
		Material mat = new Material(k_a, k_d, k_s, ns);
		
		boolean phong;
		if (renderingModel==0){
			phong=false;
		} else{
			phong=true;
		}
		float sphere_radius, ellipsoid_radius, torus_radius, cylinder_radius, box_radius;
		
		/** Scaling boolean **/
		if (sphere_scaled)
			sphere_radius=(float)100.0;
		else 
			sphere_radius=(float)65.0;
		if (torus_scaled)
			torus_radius=(float)80.0;
		else 
			torus_radius=(float)40.0;
		if (ellipsoid_scaled)
			ellipsoid_radius=(float)70.0;
		else 
			ellipsoid_radius=(float)40.0;
		if (cylinder_scaled)
			cylinder_radius=(float)70.0;
		else 
			cylinder_radius=(float)40.0;
		if (box_scaled)
			box_radius=(float)60.0;
		else 
			box_radius=(float)40.0;
		
		
		Sphere3D sphere = new Sphere3D((float) 150.0, (float) 128.0, (float) 128.0, (float) sphere_radius, Nsteps, Nsteps, mat);
		Ellipsoid3D ellipsoid = new Ellipsoid3D((float) 256.0, (float) 325.0, (float) 128.0, (float)ellipsoid_radius, (float)1.5*ellipsoid_radius, (float)ellipsoid_radius, Nsteps, Nsteps, mat);
		Torus3D torus = new Torus3D((float)128.0*3, (float)128.0, (float)128.0, (float) 0.5*torus_radius, (float) torus_radius, Nsteps, Nsteps, mat);
		Cylinder3D cylinder = new Cylinder3D(256.0f*2, 325.0f, 128.0f, (float)1.5*cylinder_radius, (float)1.5*cylinder_radius, Nsteps, Nsteps, 3*cylinder_radius, mat);
		Box3D box = new Box3D(128.0f*5, 128.0f, 128.0f, 1.5f*box_radius, Nsteps, Nsteps, mat);
		
		objects = new ArrayList<Shape>();
		
		objects.add(sphere);
		objects.add(ellipsoid);
		objects.add(cylinder);
		objects.add(torus);
		objects.add(box);
		
		//translate if appropriate
		if (sphere_translated)
        	sphere.set_center(300, 200, 128);
		if (torus_translated)
			torus.set_center(128*4, 200, 128);
        if (ellipsoid_translated)
        	ellipsoid.set_center(256*2, 400, 256);
        if (cylinder_translated)
        	cylinder.set_center(256.0f*3, 425.0f, 128.0f);
        if (box_translated) {
        	for (int i = 0; i < 6; i++) {
        		box.set_center(128.0f*6, 256.0f, 128.0f);
        	}
        }
        
        
        
        //rotate if appropriate
        if (sphere_rotated) {
        	Quaternion sphere_= new Quaternion((float).3, (float)0, (float)1, (float)0);
        	sphere_.normalize();
        	sphere.mesh.rotate_mesh(sphere_, sphere.get_center());
        }
        if (ellipsoid_rotated) {
        	Quaternion ellipsoid_= new Quaternion((float).3, (float)0, (float)1, (float)0);
        	ellipsoid_.normalize();
        	ellipsoid.mesh.rotate_mesh(ellipsoid_, ellipsoid.get_center());
        }
        if (torus_rotated) {
        	Quaternion torus_= new Quaternion((float).3, (float)0, (float)1, (float)0);
        	torus_.normalize();
        	torus.mesh.rotate_mesh(torus_, torus.get_center());
        }
        if (cylinder_rotated) {
        	Quaternion cylinder_= new Quaternion((float).3, (float)0, (float)1, (float)0);
        	cylinder_.normalize();
        	cylinder.mesh.rotate_mesh(cylinder_, cylinder.get_center());
        }
        if (box_rotated) {
        	for (int i = 0; i < 6; i++) 
        	{
        		Quaternion box_= new Quaternion((float).3, (float)0, (float)1, (float)0);
            	box_.normalize();
            	box.meshes[i].rotate_mesh(box_, box.get_center());
        	}
        }

		// view vector is defined along z axis
		// this example assumes simple othorgraphic projection
		// view vector is used in
		// (a) calculating specular lighting contribution
		// (b) backface culling / backface rejection
		Point3D view_vector = new Point3D((float) 0.0, (float) 0.0, (float) 1.0);
		
		if (!lightsInitialized) {
			light = new Light();
			ColorType light_color = new ColorType(1.0, 1.0, 1.0);
			Point3D light_direction = new Point3D((float) 0.0,
					(float) (-1.0 / Math.sqrt(2.0)), (float) (1.0 / Math.sqrt(2.0)));
			LightInfinite infLight = new LightInfinite(light_color, light_direction);
			AmbientLight ambLight = new AmbientLight(light_color, light_direction);
			light.addLight(infLight);
			light.addLight(ambLight);
			lightsInitialized = true;
		}
		
		// normal to the plane of a triangle
        // to be used in backface culling / backface rejection
        Point3D triangle_normal = new Point3D();

		// a triangle mesh
		Mesh3D mesh = null;

		int i, j, n, m;
		
		// temporary variables for triangle 3D vertices and 3D normals
		Point3D v0,v1, v2, n0, n1, n2;

		// projected triangle, with vertex colors
		Point3D[] tri = {new Point3D(), new Point3D(), new Point3D()};

		d_buff = new int[buff_x][buff_y];
		//fill d_buff with 999s
		for (int k=0; k<buff_x; k++){
			for (int l=0; l<buff_y; l++){
				d_buff[k][l]=0;
			}
		}
		
		DepthBuffer depthBuff = new DepthBuffer(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT, buff);

		for (int k = 0; k < 5; ++k)
		{
			if (k == 0) {
				mesh = sphere.mesh;
				n = sphere.get_n();
				m = sphere.get_m();
				drawObject(mesh, sphere, n, m, view_vector, light, depthBuff);
			} else if (k == 1) {
				mesh = torus.mesh;
				n = torus.get_n();
				m = torus.get_m();
				drawObject(mesh, torus, n, m, view_vector, light, depthBuff);
			} else if (k == 2) {
				mesh = ellipsoid.mesh;
				n = ellipsoid.get_n();
				m = ellipsoid.get_m();
				drawObject(mesh, ellipsoid, n, m, view_vector, light, depthBuff);
			} else if (k == 3) {
				mesh = cylinder.mesh;
				n = cylinder.get_n();
				m = cylinder.get_m();
				drawObject(mesh, cylinder, n, m, view_vector, light, depthBuff);
			} else {
				n = box.get_n();
				m = box.get_m();
				for (Mesh3D boxMesh : box.meshes) {
					drawObject(boxMesh, box, n, m, view_vector, light, depthBuff);
				}
			}
			mesh.rotate_mesh(viewing_quaternion, viewing_center);
		}
	}
	
	
	private void drawObject(Mesh3D mesh, Shape obj, int n, int m, Point3D view_vector, Light light, DepthBuffer depthBuff) {
		int i, j;
		// temporary variables for triangle 3D vertices and 3D normals
		Point3D v0, v1, v2, n0, n1, n2;

		// projected triangle, with vertex colors
		Vector3D[] tri = { new Vector3D(), new Vector3D(), new Vector3D() };
		
		Point3D point = new Point3D();
		
		// normal to the plane of a triangle
		// to be used in backface culling / backface rejection
		Point3D triangle_normal = new Point3D();
		
		// rotate the surface's 3D mesh using quaternion
		mesh.rotate_mesh(viewing_quaternion, viewing_center);
		
		for (i = 0; i < m - 1; ++i) {
			for (j = 0; j < n - 1; ++j) {
				v0 = mesh.v[i][j];
				v1 = mesh.v[i][j + 1];
				v2 = mesh.v[i + 1][j + 1];
				triangle_normal = computeTriangleNormal(v0, v1, v2);

				if (view_vector.dotProduct(triangle_normal) > 0.0) // front-facing
																	// triangle?
				{
					if (phong) {
						n0 = mesh.n[i][j];
						n1 = mesh.n[i][j + 1];
						n2 = mesh.n[i + 1][j + 1];
					}
					else if (gouraud) {
						// vertex colors for Gouraud shading
						n0 = mesh.n[i][j];
						n1 = mesh.n[i][j + 1];
						n2 = mesh.n[i + 1][j + 1];
						tri[0].c = light.applyLight(obj.mat, view_vector,
								n0, v0);
						tri[1].c = light.applyLight(obj.mat, view_vector,
								n1, v1);
						tri[2].c = light.applyLight(obj.mat, view_vector,
								n2, v2);
						
					} else {
						// flat shading: use the normal to the triangle
						// itself
						n2 = n1 = n0 = triangle_normal;
						point = new Point3D((v0.x + v1.x + v2.x)/3, 
								(v0.y + v1.y + v2.y)/3, 
								(v0.z + v1.z + v2.z)/3);
						tri[2].c = tri[1].c = tri[0].c = light.applyLight(
								obj.mat, view_vector, triangle_normal,
								point);
					}

					tri[0].x = (int) v0.x;
					tri[0].y = (int) v0.y;
					tri[0].z = (int) v0.z;
					tri[1].x = (int) v1.x;
					tri[1].y = (int) v1.y;
					tri[1].z = (int) v1.z;
					tri[2].x = (int) v2.x;
					tri[2].y = (int) v2.y;
					tri[2].z = (int) v2.z;

					if (phong) {
						Triangle.drawTriangleWithPhong(buff, depthBuff, tri[0], tri[1], tri[2], n0, n1, n2, light, obj.mat, view_vector);
					} else {
						Triangle.drawTriangle(buff, depthBuff, tri[0], tri[1], tri[2], gouraud);
					}
				}

				v0 = mesh.v[i][j];
				v1 = mesh.v[i + 1][j + 1];
				v2 = mesh.v[i + 1][j];
				triangle_normal = computeTriangleNormal(v0, v1, v2);

				if (view_vector.dotProduct(triangle_normal) > 0.0) // front-facing
																	// triangle?
				{
					if (phong) {
						n0 = mesh.n[i][j];
						n1 = mesh.n[i + 1][j + 1];
						n2 = mesh.n[i + 1][j];
					}
					else if (gouraud) {
						// vertex colors for Gouraud shading
						n0 = mesh.n[i][j];
						n1 = mesh.n[i + 1][j + 1];
						n2 = mesh.n[i + 1][j];
						tri[0].c = light.applyLight(obj.mat, view_vector,
								n0, v0);
						tri[1].c = light.applyLight(obj.mat, view_vector,
								n1, v1);
						tri[2].c = light.applyLight(obj.mat, view_vector,
								n2, v2);
					} else {
						// flat shading: use the normal to the triangle
						// itself
						n2 = n1 = n0 = triangle_normal;
						point = new Point3D((v0.x + v1.x + v2.x)/3, 
								(v0.y + v1.y + v2.y)/3, 
								(v0.z + v1.z + v2.z)/3);
						tri[2].c = tri[1].c = tri[0].c = light.applyLight(
								obj.mat, view_vector, triangle_normal,
								point);
					}

					tri[0].x = (int) v0.x;
					tri[0].y = (int) v0.y;
					tri[0].z = (int) v0.z;
					tri[1].x = (int) v1.x;
					tri[1].y = (int) v1.y;
					tri[1].z = (int) v1.z;
					tri[2].x = (int) v2.x;
					tri[2].y = (int) v2.y;
					tri[2].z = (int) v2.z;

					if (phong) {
						Triangle.drawTriangleWithPhong(buff, depthBuff, tri[0], tri[1], tri[2], n0, n1, n2, light, obj.mat, view_vector);
					} else {
						Triangle.drawTriangle(buff, depthBuff, tri[0], tri[1], tri[2], gouraud);
					}
				}
			}
		}
	}
	// helper method that computes the unit normal to the plane of the triangle
	// degenerate triangles yield normal that is numerically zero
	private Point3D computeTriangleNormal(Point3D v0, Point3D v1, Point3D v2) {
		Point3D e0 = v1.minus(v2);
		Point3D e1 = v0.minus(v2);
		Point3D norm = e0.crossProduct(e1);

		if (norm.magnitude() > 0.000001)
			norm.normalize();
		else
			// detect degenerate triangle and set its normal to zero
			norm.set((float) 0.0, (float) 0.0, (float) 0.0);

		return norm;
	}

}
