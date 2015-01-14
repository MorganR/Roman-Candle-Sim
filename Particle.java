
public abstract class Particle extends Firework implements ODESystem {
	
	private final static double DRAG_C = 0.4;
	private final static int SYSTEM_SIZE = 2;
	
	private double creationTime;
	private double lifetime;
	private double initialMass;
	private String colour;
	public double density;
	private double wind;
	//The burn rate of the particle
	public double burn;
	private double radius;
	
	
	public Particle(double creationTimeIn, double xPosIn, double yPosIn, double xVelIn, double yVelIn, String colourIn) {
		super(xPosIn, yPosIn, xVelIn, yVelIn);
		creationTime = creationTimeIn;
		colour = colourIn;	
	}

	public String getColour() {
		return colour;
	}
	
	public double getCreationTime() {
		return creationTime;
	}
	
	public double getLifetime() {
		return lifetime;
	}
	
	public abstract int getRenderSize();
	
	public void setLifetime(double lifetime) {
		this.lifetime = lifetime;
	}
	
	//Uses randomization to vary mass
	public void setStartingMass(double mass, double var) {
		initialMass = getRand(mass - var, mass + var);
	}
	
	public void setStartingRadius(double radius) {
		this.radius = radius;
	}
	
	public double findMass(double time) {
		return (initialMass - time*burn);
	}
	
	//Magnitude of V
	private double findV(double vxa, double vy) {
		return Math.sqrt(Math.pow(vxa, 2)+Math.pow(vy, 2));
	}
	
	//Cross-sectional area
	private double findA(double mass) {
		double A = Math.PI*Math.pow(radius, 2);
		return A;
	}
	
	//Drag force
	private double forceD(double v, double mass) {
		double fD = Environment.AIR_DENSITY*Math.pow(v, 2)*findA(mass)*DRAG_C/2;
		return fD;
	}
	
	//Derivative of Vx
	private double dVx(double time, double[] values) {
		double vxa = values[0] - wind;
		double v = findV(vxa, values[1]);
		double mass = findMass(time);
		return -forceD(v, mass)*vxa/mass/v;
	}
	
	//Derivative of Vy
	private double dVy(double time, double[] values) {
		double vxa = values[0] - wind;
		double v = findV(vxa, values[1]);
		double mass = findMass(time);
		return -Environment.G-forceD(v, mass)*values[1]/findMass(time)/v;
	}
	
	public void updatePosition(double time, double deltaT, Environment earth) {
		//Make time age of the particle
		time = time - creationTime;
		wind = earth.getWind();
		setVelocity(RungeKutta.solve(this, time, deltaT));
		double[] newPosition = {getPosition()[X] + getVelocity()[X]*deltaT, getPosition()[Y] + getVelocity()[Y]*deltaT};
		setPosition(newPosition);
	}
	
	//Allow type  input so "LaunchSpark" can have a different type to "Spark"
	public String toString(String type) {
		String s = "" + type + "\t" + getColour() + "\t" + super.toString();
		return s;
	}
	
	@Override
	public int getSystemSize() {
		return SYSTEM_SIZE;
	}

	@Override
	public double[] getCurrentValues() {
		return getVelocity();
	}

	@Override
	public double[] getFunction(double time, double[] values) {
		double[] functionData = new double[2];
		functionData[0] = dVx(time, values);
		functionData[1] = dVy(time, values);
		return functionData;
	}
	
}
