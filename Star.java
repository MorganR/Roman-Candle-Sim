public class Star extends Particle {

	//Density of star in kg/m^3
	public static final int DENSITY = 1900;
	private int renderSize;
	
	public Star(double creationTimeIn, double xPosIn, double yPosIn,
			double xVelIn, double yVelIn, String colourIn) {
		super(creationTimeIn, xPosIn, yPosIn, xVelIn, yVelIn, colourIn);
		burn = 0.0030;
		//Allow variation in mass
		setStartingMass(0.008, 0.001);
		renderSize = 6;
		//Lifetime is mass dependent
		setLifetime(findMass(0)/burn);
		setStartingRadius(Math.pow(3.0/4.0*findMass(0)/DENSITY/Math.PI, 1.0/3.0));
	}
	
	@Override
	public int getRenderSize() {
		return renderSize;
	}
	
	@Override
	public String toString() {
		String s = super.toString("Star");
		return s;
	}
}
