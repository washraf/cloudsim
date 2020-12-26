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
public class HostSelectionPolicyMinChangeRate extends HostSelectionPolicy {

    @Override
    public ContainerHost getHost(List<ContainerHost> hostList, Object obj,Set<? extends ContainerHost> excludedHostList) {
        ContainerHost selectedHost = null;
        if(CloudSim.clock() >1.0){
        double min = Double.MAX_VALUE;
        for (ContainerHost host : hostList) {
            if (excludedHostList.contains(host)) {
                continue;
            }
            if (host instanceof PowerContainerHostUtilizationHistory) {
            	double [] history = ((PowerContainerHostUtilizationHistory) host).getUtilizationHistory();
            	double hostMag= MathUtil.magnitude(history);
                if (hostMag < min) {
                    min = hostMag;
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
