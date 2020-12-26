package org.cloudbus.cloudsim.container.hostSelectionPolicies;

import org.cloudbus.cloudsim.container.core.ContainerHost;
import org.cloudbus.cloudsim.container.core.PowerContainerHostUtilizationHistory;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.power.PowerHostUtilizationHistory;
import org.cloudbus.cloudsim.util.MathUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Walid on 1/10/17.
 */
public class HostSelectionPolicyLeastVariance extends HostSelectionPolicy {

    @Override
    public ContainerHost getHost(List<ContainerHost> hostList, Object obj,Set<? extends ContainerHost> excludedHostList) {
        ContainerHost selectedHost = null;
        if(CloudSim.clock() >1.0){
        double minVariance = Double.MAX_VALUE;
        for (ContainerHost host : hostList) {
            if (excludedHostList.contains(host)) {
                continue;
            }
            if (host instanceof PowerContainerHostUtilizationHistory) {
            	ArrayList<Double> utilHistory =  new ArrayList<Double>();	
            	double [] history = ((PowerContainerHostUtilizationHistory) host).getUtilizationHistory();
            	for (int i = 0; i < history.length ; i++) {
            		utilHistory.add(history[i]);
				}
                double hostVar= MathUtil.variance(utilHistory);
                if (hostVar < minVariance) {
                    minVariance = hostVar;
                    selectedHost = host;
                }
            }
        }
        if(selectedHost==null){
        	selectedHost = new HostSelectionPolicyFirstFit().getHost(hostList,obj ,excludedHostList);
        }
        return selectedHost;
    }else {

//            At the simulation start all the VMs by leastFull algorithms.

            selectedHost = new HostSelectionPolicyFirstFit().getHost(hostList,obj ,excludedHostList);

            return selectedHost;
        }



    }


}
