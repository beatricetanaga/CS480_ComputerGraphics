
import javax.media.opengl.*;
import com.jogamp.opengl.util.*;
import com.jogamp.opengl.util.gl2.GLUT;
import java.util.*;

public class Vivarium implements Displayable, Animate {
	private Tank tank;
	public ArrayList<Food> foods = new ArrayList<>();
	public ArrayList<Food> inUseFoods = new ArrayList<>();
	public ArrayList<Shark> shark = new ArrayList<>();
	public ArrayList<Fish> fish = new ArrayList<>();

	
	public Vivarium(GLUT glut) {
		tank = new Tank(4.0f, 4.0f, 4.0f);
		for (int i = 0; i < 5; i++) {
			foods.add(new Food(glut));
		}
		shark.add(new Shark(new Point3D(0, 0, 0), glut, 1));
		fish.add(new Fish(new Point3D(1, 0, 0), glut, 1.5));
		fish.add(new Fish(new Point3D(0, 0, 1), glut, 1.5));
		fish.add(new Fish(new Point3D(0, 1, 0), glut, 1.5));
		this.setModelStates(null);
	}

	public void initialize(GL2 gl) {
		tank.initialize(gl);
		for (Component object : shark) {
			object.initialize(gl);
		}
		for (Component object : fish) {
			object.initialize(gl);
		}
		for (Food object : foods) {
			object.initialize(gl);
		}
	}

	public void update(GL2 gl) {
		tank.update(gl);
		for (Component object : shark) {
			object.update(gl);
		}
		for (Component object : fish) {
			object.update(gl);
		}
		for (Food food:inUseFoods) {
			food.update();
		}
	}
	
	public void addFood() {
		if (foods.size()>0) {
			inUseFoods.add(foods.get(0));
			foods.remove(0);
		}
	}

	public void draw(GL2 gl) {
		for (Food food:inUseFoods) {
			food.draw(gl);
		}
		tank.draw(gl);
		for (Component object : shark) {
			object.draw(gl);
		}
		for (Component object : fish) {
			object.draw(gl);
		}
	}

	@Override
	public void setModelStates(ArrayList<Configuration> config_list) {
		
		// assign configurations in config_list to all Components in here
		for (Shark example : shark) {
			ArrayList<Configuration> configs = new ArrayList<>();
			configs.add(nextConfig(example));
			configs.add(nextConfig(example));
			(example).setModelStates(configs);
		}
		for (Fish example : fish) {
			ArrayList<Configuration> configs = new ArrayList<>();
			configs.add(nextConfig(example));
			configs.add(nextConfig(example));
			(example).setModelStates(configs);
		}
	}
	
	private double randomCoordinate(double radius) {
		double random = Math.random()*(4-2*radius)-(2-radius);
		return random;
	}
	
	private double clampToTank(double c, Shark shark) {
		if (c > 2-shark.sphereRadius()) {
			c = 2 - shark.sphereRadius();
		}
		if (c < -2+shark.sphereRadius()) {
			c = -2 + shark.sphereRadius();
		}
		return c;
	}

	private Configuration nextConfig(Shark shark, Fish fish) {
		if (fish == null) {
			return nextConfig(shark);
		}
		
		Point3D target = fish.position();
		
		double x = clampToTank(target.x(), shark);
		double y = clampToTank(target.y(), shark);
		double z = clampToTank(target.z(), shark);
		
		Point3D p1 = new Point3D(x,y,z);
		Configuration c = new BaseConfiguration(0, 0, 0, p1);
		return c;
	}
	
	private Configuration nextConfig(Fish fish, Food food) {
		//  || Math.random() < 0.5
		if (food == null) {
			return nextConfig(fish);
		}
		
		Point3D target = food.position();
		
		double x = clampToTank(target.x(), fish);
		double y = clampToTank(target.y(), fish);
		double z = clampToTank(target.z(), fish);
		
		Point3D p1 = new Point3D(x,y,z);
		Configuration c = new BaseConfiguration(0, 0, 0, p1);
		return c;
	}
	
	private Configuration nextConfig(Shark shark) {
		double x = randomCoordinate(shark.sphereRadius());
		double y = randomCoordinate(shark.sphereRadius());
		double z = randomCoordinate(shark.sphereRadius());
		
		Point3D p1 = new Point3D(x,y,z);
		Configuration c = new BaseConfiguration(0, 0, 0, p1);
		return c;
	}
	
	// Shark colliding with fish 
	private void animalCollision() {
		for (int i = 0; i < fish.size() ; i++) {
			Fish fish1 = fish.get(i);
			Point3D fish1Location = fish1.position();
			double fish1Radius = fish1.sphereRadius();
			for (Shark theShark : shark) {
				Point3D sharkLocation = theShark.position();
				double sharkRadius = theShark.sphereRadius();
				boolean getsEaten = eatCheck(theShark, fish1);
				if (collisionCheck(fish1Location, fish1Radius, sharkLocation, sharkRadius)) {
					Configuration c1 = changeDirection(fish1, theShark);
					fish1.setNextConfig(c1);
				}
				if (getsEaten) {
					fish1.isEaten = true;
//					System.out.println("Eaten by shark");
				}
			}
			for (int j = i+1; j < fish.size() ; j++) {
				Fish fish2 = fish.get(j);
				Point3D fish2Location = fish2.position();
				double fish2Radius = fish2.sphereRadius();
				if (collisionCheck(fish1Location, fish1Radius, fish2Location, fish2Radius)) {
					Configuration c1 = changeDirection(fish1, fish2);
					fish1.setNextConfig(c1);
					Configuration c2 = changeDirection(fish2, fish1);
					fish2.setNextConfig(c2);
				}
			}
		}
	}
	
	private Configuration changeDirection(Shark shark1, Shark shark2) {
		Point3D p1 = shark1.position();
		Point3D p2 = shark2.position();
		
		double randomDistance = Math.random()*3;
		
		double xTarget = clampToTank(p1.x()+(p1.x()-p2.x())*randomDistance, shark1);
		double yTarget = clampToTank(p1.y()+(p1.y()-p2.y())*randomDistance, shark1);
		double zTarget = clampToTank(p1.z()+(p1.z()-p2.z())*randomDistance, shark1);
		
		Point3D target = new Point3D(xTarget, yTarget, zTarget);
		
		Configuration c1 = new BaseConfiguration(target);
		return c1;
	}
	
	// Fish colliding with food
	private void foodCollision() {
		for (Food food : inUseFoods) {
			Point3D foodLocation = food.position();
			double foodRadius = food.sphereRadius();
			for (Fish fish : fish) {
				Point3D fishLocation = fish.position();
				double fishRadius = fish.sphereRadius();
				boolean getsEaten = eatCheck(fish, food);
				if (getsEaten) {
					food.isEaten = true;
//					System.out.println("Food eaten by fish");
				}
			}
		}
	}
	
	private boolean collisionCheck(Point3D p1, double radius1, Point3D p2, double radius2) {
		double xDiff = p1.x()-p2.x();
		double yDiff = p1.y()-p2.y();
		double zDiff = p1.z()-p2.z();
		
		double vectorLength = Math.sqrt(xDiff*xDiff + yDiff*yDiff + zDiff*zDiff);
		if (vectorLength <= radius1+radius2) {
			return true;
		}
		return false;
	}
	
	// Checks if fish is eaten by shark
	private boolean eatCheck(Shark shark, Fish fish) {
		Point3D p1 = shark.position();
		Point3D p2 = fish.position();
		
		// Vector from shark center to fish
		double xDiff = p1.x()-p2.x();
		double yDiff = p1.y()-p2.y();
		double zDiff = p1.z()-p2.z();
		
		// Vector from shark center to shark mouth
		Point3D sharkFacing = shark.directionToFace();
		
		double vectorLength = Math.sqrt(xDiff*xDiff + yDiff*yDiff + zDiff*zDiff);
		
		double unitVectorX = xDiff/vectorLength;
		double unitVectorY = yDiff/vectorLength;
		double unitVectorZ = zDiff/vectorLength;
		
		double sfxLength = sharkFacing.x()*sharkFacing.x();
		double sfyLength = sharkFacing.y()*sharkFacing.y();
		double sfzLength = sharkFacing.z()*sharkFacing.z();
		double sharkFacingLength = Math.sqrt(sfxLength+sfyLength+sfzLength);
		
		double unitSFX = sharkFacing.x()/sharkFacingLength; 
		double unitSFY = sharkFacing.y()/sharkFacingLength; 
		double unitSFZ = sharkFacing.z()/sharkFacingLength;
		
		// Cos(angle) of shark mouth to fish
		double dotProduct = sfxLength*unitSFX + sfyLength*unitSFY +sfzLength*unitSFZ;
		
		// If dotP is small => angle is big, then fish is not at the shark's mouth
		if (dotProduct <= Math.cos((10*Math.PI)/180)) {
			return false;
		}
		
		double radius1 = shark.sphereRadius();
		double radius2 = fish.sphereRadius();
		
		if (vectorLength <= radius1+radius2) {
			return true;
		}
		return false;
	}
	
	// Checks if food is eaten by fish
	private boolean eatCheck(Fish fish, Food food) {
		Point3D p1 = fish.position();
		Point3D p2 = food.position();
		
		double xDiff = p1.x()-p2.x();
		double yDiff = p1.y()-p2.y();
		double zDiff = p1.z()-p2.z();
		
		Point3D fishFacing = fish.directionToFace();
		
		double vectorLength = Math.sqrt(xDiff*xDiff + yDiff*yDiff + zDiff*zDiff);
		
		double unitVectorX = xDiff/vectorLength;
		double unitVectorY = yDiff/vectorLength;
		double unitVectorZ = zDiff/vectorLength;
		
		double ffxLength = fishFacing.x()*fishFacing.x();
		double ffyLength = fishFacing.y()*fishFacing.y();
		double ffzLength = fishFacing.z()*fishFacing.z();
		double fishFacingLength = Math.sqrt(ffxLength+ffyLength+ffzLength);
		
		double unitFFX = fishFacing.x()/fishFacingLength; 
		double unitFFY = fishFacing.y()/fishFacingLength; 
		double unitFFZ = fishFacing.z()/fishFacingLength;
		
		double dotProduct = ffxLength*unitFFX + ffyLength*unitFFY +ffzLength*unitFFZ;
		
		if (dotProduct <= Math.cos((10*Math.PI)/180)) {
//			System.out.println(dotProduct);
			return false;
		}
		
		double radius1 = fish.sphereRadius();
		double radius2 = food.sphereRadius();
		
		if (vectorLength <= radius1+radius2) {
			return true;
		}
		return false;
	}
	
	ArrayList<Configuration> states = new ArrayList<>();
	
	@Override
	public void animationUpdate(GL2 gl) {
		// Animating fish getting eaten by shark
		animalCollision();
		for (int i = fish.size() - 1; i >= 0 ; --i) {
			if (fish.get(i).isEaten) {
				fish.remove(i);
			}
		}
		Fish theFish = null;
		if (!fish.isEmpty()) {
			theFish = fish.get(0);
		}
		for (Shark example : shark) {
			(example).animationUpdate(gl);
			if (example.nextState()) {
				Configuration configuration = nextConfig(example, theFish);
				example.setNextConfig(configuration);
			}
		}
		
		// Animating food getting eaten by fish
		foodCollision();
		for (int i = inUseFoods.size() - 1; i >= 0 ; --i) {
			if (inUseFoods.get(i).isEaten) {
				Food eatenFood = inUseFoods.get(i);
				eatenFood.isEaten = false;
				eatenFood.setNewLocation();
				foods.add(eatenFood);
				inUseFoods.remove(i);
			}
		}
		Food theFood = null;

		for (Fish example : fish) {
			(example).animationUpdate(gl);
			if (example.nextState()) {
				if (!inUseFoods.isEmpty()) {
					int index = (int) (Math.random()*inUseFoods.size());
					theFood = inUseFoods.get(index);
				}
				Configuration configuration = nextConfig(example, theFood);
				example.setNextConfig(configuration);
			}
		}
	}
}
