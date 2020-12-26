package org.cloudbus.cloudsim.examples.container;

import java.io.IOException;

/**
 * This Example is following the format for
 * {@link org.cloudbus.cloudsim.examples.power.planetlab.Dvfs} It specifically
 * studies the placement of containers.
 *
 * @author Walid A. Hanafy
 */
public class MySimmulation {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public static void main(String[] args) throws IOException {
		/**
		 * The experiments can be repeated for (repeat - runtime +1) times.
		 * Please set these values as the arguments of the main function or set
		 * them bellow:
		 */
		int runTime = Integer.parseInt("1");
		int repeat = Integer.parseInt("1");
		boolean enableOutput = true;
		boolean outputToFile = true;
		/**
		 * Getting the path of the planet lab workload that is included in the
		 * cloudSim Package
		 */
		String inputFolder = MySimmulation.class.getClassLoader()
				.getResource("workload/planetlab").getPath();
		/**
		 * The output folder for the logs. The log files would be located in
		 * this folder.
		 */
		String outputFolder = "/Users/whanafy/Documents/cloud-results";
		/**
		 * The allocation policy for VMs. It has the under utilization and over
		 * utilization thresholds used for determining the underloaded and
		 * oberloaded hosts.
		 */
		// String vmAllocationPolicy = "MSThreshold-Under_0.90_0.70"; // DVFS
		// policy
		// without
		// VM
		// migrations
		String[] vmAllocationPolicies = { "MSThreshold-Under_0.90_0.70",
				"ANT_0.90_0.70" };
		/**
		 * The selection policy for containers where a container migration is
		 * triggered.
		 */
		String[] containerSelectionPolicies = {
		"Cor",
		"MaxUsage",
		"MaxVariance",
		"MinVariance",
		"MinChangeRate",
		"MaxChangeRate",
		//--
		"MinMig",
		"MinUsage", // Very Bad results
		"MedUsage"
		};
		/**
		 * The allocation policy used for allocating containers to VMs. only at
		 * start
		 */
		String[] containerAllocationPolicies = {
		"Simple",
		"MostFull",
		"FirstFit",
		"LeastFull",
		"Random"
		};

		/**
		 * The host selection policy determines which hosts should be selected
		 * as the migration destination.
		 */
		String[] hostSelectionPolicies = {
		 "FirstFit",
		 "LeastFull",
//		 //"MostFull", //Dead Lock Dangerous
		 "RandomSelection",
		 "Cor",
		 "MinCor",
		"MaxCor",
		 "MaxVar",
		"LeastVar",
		"MinChangeRate",
		"MaxChangeRate"
		};
		/**
		 * The VM Selection Policy is used for selecting VMs to migrate when a
		 * host status is determined as "Overloaded"
		 */
		String[] vmSelectionPolicies = {
		"VmMaxC",
		"VmMaxU" };
		for (int vmAllocationPolicy = 0; vmAllocationPolicy < vmAllocationPolicies.length; vmAllocationPolicy++) {
			
			if(vmAllocationPolicy == 1)
			{
				hostSelectionPolicies = new String []{
						"Ant",
						"MostFull"
						};
			}
			for (int hostSelectionPolicy = 0; hostSelectionPolicy < hostSelectionPolicies.length; hostSelectionPolicy++) {
				for (int vmSelectionPolicy = 0; vmSelectionPolicy < vmSelectionPolicies.length; vmSelectionPolicy++) {
					for (int containerSelectionPolicy = 0; containerSelectionPolicy < containerSelectionPolicies.length; containerSelectionPolicy++) {
						for (int containerAllocationPolicy = 0; containerAllocationPolicy < containerAllocationPolicies.length; containerAllocationPolicy++) {
							for (int i = 1; i <= repeat; i++) {
							//	if(hostSelectionPolicies[hostSelectionPolicy] .equalsIgnoreCase("MostFull") && containerAllocationPolicies[containerAllocationPolicy].equalsIgnoreCase("MostFull"))
							//		continue;
								new RunnerInitiator(
										enableOutput,
										outputToFile,
										inputFolder,
										outputFolder,
										vmAllocationPolicies[vmAllocationPolicy],
										containerAllocationPolicies[containerAllocationPolicy],
										vmSelectionPolicies[vmSelectionPolicy],
										containerSelectionPolicies[containerSelectionPolicy],
										hostSelectionPolicies[hostSelectionPolicy],
										i, Integer.toString(runTime),
										outputFolder);
							}
						}
					}
				}
			}
		}
	}
}