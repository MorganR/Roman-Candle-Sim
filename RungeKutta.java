/**
 * The RungeKutta class is able to solve for new x and y velocities of
 * Projectiles after a small time increment "delta t." This class implements
 * the Projectile and Environment interfaces for its inputs.
 * 
 * @author Morgan Roff, ID: 10044329, NetID: 11mr60
 * @version 2.0
 */
public class RungeKutta {
	
	/**
	 * Solve is the main method of the RungeKutta class. With a Projectile and an Environment
	 * input, it can be run to produce the new velocity of the projectile after time delta t.
	 * @param setup The Environment class which holds constant projectile and environment data
	 * @param ode The Projectile class which holds position, time, mass, and velocity data for the projectile. 
	 */
	public static double[] solve(ODESystem ode, double time, double deltaT) {
		int systemSize = ode.getSystemSize();
		double[] q1 = new double[systemSize];
		double[] q2 = new double[systemSize];
		double[] q3 = new double[systemSize];
		double[] q4 = new double[systemSize];
		double[] intermediateVals = new double[systemSize];
		int i;
		double[] values = ode.getCurrentValues();
		q1 = ode.getFunction(time, values);
		for (i = 0; i < systemSize; i++)
			intermediateVals[i] = values[i] + deltaT * q1[i] / 2.0;
		q2 = ode.getFunction(time + deltaT/2.0, intermediateVals);
		for (i = 0; i < systemSize; i++)
			intermediateVals[i] = values[i] + deltaT * q2[i] / 2.0;
		q3 = ode.getFunction(time + deltaT/2.0, intermediateVals);
		for (i = 0; i < systemSize; i++)
			intermediateVals[i] = values[i] + deltaT * q3[i];
		q4 = ode.getFunction(time + deltaT, intermediateVals);
		double[] newVel = new double[systemSize];
		for (i = 0; i < systemSize; i++)
			newVel[i] = values[i] + deltaT * (q1[i] + 2.0 * q2[i] +
					2.0 * q3[i] + q4[i]) / 6.0;
//		System.out.println("vx original: " + values[0]);
//		System.out.println("vx after: " + newVel[0]);
		return newVel;
	}
}