public class Spark extends Particle {
	
	private int renderSize;
	
	public Spark(double creationTimeIn, double xPosIn, double yPosIn,
			double xVelIn, double yVelIn, double lifetime, String colourIn) {
		super(creationTimeIn, xPosIn, yPosIn, xVelIn, yVelIn, colourIn);
		//These particles do not account for loss of mass over time
		burn = 0;
		//Allow for variation in mass between spars
		setStartingMass(2.0*Math.pow(10, -6), 3.0*Math.pow(10, -7));
		renderSize = 2;
		setLifetime(lifetime);
		setStartingRadius(0.0015);
	}
	
	@Override
	public int getRenderSize() {
		return renderSize;
	}
	
	@Override
	public String toString() {
		String s = super.toString("Spark");
		return s;
	}
	
}
