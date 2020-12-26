package org.cloudbus.cloudsim.container.resourceAllocatorMigrationEnabled;

import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.container.containerSelectionPolicies.PowerContainerSelectionPolicy;
import org.cloudbus.cloudsim.container.core.Container;
import org.cloudbus.cloudsim.container.core.ContainerHost;
import org.cloudbus.cloudsim.container.core.ContainerVm;
import org.cloudbus.cloudsim.container.core.PowerContainerHost;
import org.cloudbus.cloudsim.container.core.PowerContainerHostUtilizationHistory;
import org.cloudbus.cloudsim.container.hostSelectionPolicies.HostSelectionPolicy;
import org.cloudbus.cloudsim.container.lists.PowerContainerList;
import org.cloudbus.cloudsim.container.lists.PowerContainerVmList;
import org.cloudbus.cloudsim.container.vmSelectionPolicies.PowerContainerVmSelectionPolicy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by walid on 2/10/17.
 * http://ieeexplore.ieee.org/document/7314707/
 */
public class PowerContainerVmAllocationPolicyMigrationAnt extends PowerContainerVmAllocationPolicyMigrationStaticThresholdMCUnderUtilized{


    /**
     * Instantiates a new power vm allocation policy migration mad.
     *
     * @param hostList             the host list
     * @param vmSelectionPolicy    the vm selection policy
     * @param utilizationThreshold the utilization threshold
     */
    public PowerContainerVmAllocationPolicyMigrationAnt(
            List<? extends ContainerHost> hostList,
            PowerContainerVmSelectionPolicy vmSelectionPolicy, PowerContainerSelectionPolicy containerSelectionPolicy,
            HostSelectionPolicy hostSelectionPolicy, double utilizationThreshold, double underUtilizationThresh,
            int numberOfVmTypes, int[] vmPes, float[] vmRam, long vmBw, long vmSize, double[] vmMips) {
        super(hostList, vmSelectionPolicy, containerSelectionPolicy, hostSelectionPolicy,utilizationThreshold,underUtilizationThresh,
        		 numberOfVmTypes, vmPes, vmRam, vmBw, vmSize, vmMips);
        setUtilizationThreshold(utilizationThreshold);
        
    }

    
    /**
     * To be overridden with Ant 
     */
    @Override
    public List<Map<String, Object>> getNewContainerPlacement(List<? extends Container> containersToMigrate, Set<? extends ContainerHost> excludedHosts) {
        List<Map<String, Object>> migrationMap = new LinkedList<Map<String, Object>>();
        PowerContainerList.sortByCpuUtilization(containersToMigrate);
        for (Container container : containersToMigrate) {
            Map<String, Object> allocationMap = findHostForContainer(container, excludedHosts, false);

            if (allocationMap.get("host") != null && allocationMap.get("vm") != null) {
                ContainerVm vm = (ContainerVm) allocationMap.get("vm");
                Log.printConcatLine("Container #", container.getId(), " allocated to host #", ((PowerContainerHost) allocationMap.get("host")).getId(), "The VM ID is #", vm.getId());
                Map<String, Object> migrate = new HashMap<String, Object>();
                migrate.put("container", container);
                migrate.put("vm", vm);
                migrate.put("host", (PowerContainerHost) allocationMap.get("host"));
                migrationMap.add(migrate);
            } else {
                Map<String, Object> migrate = new HashMap<String, Object>();
                migrate.put("NewVmRequired", container);
                migrationMap.add(migrate);

            }

        }
        containersToMigrate.clear();
        return migrationMap;

    }

    //Calculate the ant based on the bandwidth between hosts and load assumed to be equal
    /*
    private void CalculateAnt(List<? extends Container> containersToMigrate,
			List<PowerContainerHostUtilizationHistory> targetHosts) {
		// TODO Auto-generated method stub
    	 
    	 List<List<Double>> phermones = new ArrayList<List<Double>>(); 
         
         for (int i = 0; i < phermones.size(); i++) {
			for (int j = 0; j < phermones.get(i).size(); j++) {
				
			}
		}
         
         //Iterations
    	for (int i = 0; i < 3; i++) {
    		//ants
			for (int j = 0; j < 3; j++) {
				double [] probability = new double [targetHosts.size()];
				
				for (int k = 0; k < targetHosts.size(); k++) {
					probability[k] = 
				}
			}
		}
		
	}
*/

	@Override
    public Map<String, Object> findHostForContainer(Container container, Set<? extends ContainerHost> excludedHosts, boolean checkForVM) {

        PowerContainerHost allocatedHost = null;
        ContainerVm allocatedVm = null;
        Map<String, Object> map = new HashMap<>();
        Set<ContainerHost> excludedHost1 = new HashSet<>();
        if(excludedHosts.size() == getContainerHostList().size()){
            return map;}
        //List<PowerContainerHost> offhosts = getSwitchedOffHosts();
        excludedHost1.addAll(excludedHosts);
        //excludedHost1.addAll(offhosts);
        while (true) {
            if(getContainerHostList().size()==0){
                return map;
            }
            ContainerHost host = getHostSelectionPolicy().getHost(getContainerHostList(), container, excludedHost1);
            boolean findVm = false;
            List<ContainerVm> vmList = host.getVmList();
            PowerContainerVmList.sortByCpuUtilization(vmList);
            for (int i = 0; i < vmList.size(); i++) {
                ContainerVm vm = vmList.get(vmList.size() - 1 - i);
                if(checkForVM){
                    if(vm.isInWaiting()){

                        continue;
                    }

                }
                if (vm.isSuitableForContainer(container)) {

                    // if vm is overutilized or host would be overutilized after the allocation, this host is not chosen!
                    if (!isVmOverUtilized(vm)) {
                        continue;
                    }
                    if (getUtilizationOfCpuMips((PowerContainerHost) host) != 0 && isHostOverUtilizedAfterContainerAllocation((PowerContainerHost) host, vm, container)) {
                        continue;
                    }
                    vm.containerCreate(container);
                    allocatedVm = vm;
                    findVm = true;
                    allocatedHost = (PowerContainerHost) host;
                    break;


                }
            }
            if (findVm) {

                map.put("vm", allocatedVm);
                map.put("host", allocatedHost);
                map.put("container", container);
                excludedHost1.clear();
                return map;


            } else {
                excludedHost1.add(host);
                if (getContainerHostList().size() == excludedHost1.size()) {
                    excludedHost1.clear();
                    return map;
                }
            }

            if(excludedHost1.size()==getContainerHostList().size()){
            	excludedHost1.remove(getSwitchedOffHosts().indexOf(0));
            	System.out.println("Open a host");
            }
        }


    }

}
