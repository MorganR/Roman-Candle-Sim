/**
 * The Projectile interface combines with the Environment interface to 
 * provide the RungeKutta class with everything it needs to be correctly 
 * implemented. This enables the RungeKutta solver to be used with different 
 * types of projectiles and projectile environments.
 * 
 * @author Morgan Roff, ID: 10044329, NetID: 11mr60
 * @version 1.0
 */
public interface ODESystem {
	
	int getSystemSize();
	double[] getCurrentValues();
	double[] getFunction(double time, double[] values);
	
	
	
//	/**
//	 * Return a double of the coefficient of drag due to
//	 * friction with the air (a constant).
//	 * @return the coefficient of drag
//	 */
//	public double getDragC();
//	
//
//	/**
//	 * Return a double of the density of the projectile (a constant).
//	 * @return the density of the projectile (kg/m^3)
//	 */
//	public double getDensity();
//	
//	public int getSystemSize();
//	
//	public double[] getCurrentValues();
//	
//	public double[] getFunction(double time, double[] values);
	
}
