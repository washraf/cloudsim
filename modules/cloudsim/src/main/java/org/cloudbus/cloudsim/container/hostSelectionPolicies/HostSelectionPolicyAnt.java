package org.cloudbus.cloudsim.container.hostSelectionPolicies;

import org.cloudbus.cloudsim.container.core.ContainerHost;
import org.cloudbus.cloudsim.container.core.PowerContainerHostUtilizationHistory;
import org.cloudbus.cloudsim.util.MathUtil;
import org.cloudbus.cloudsim.util.RandomSelector;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Walid on 1/10/17.
 */
public class HostSelectionPolicyAnt extends HostSelectionPolicy{

    @Override
    public ContainerHost getHost(List<ContainerHost> hostList, Object obj,Set<? extends ContainerHost> excludedHostList) {
      
    	List<PowerContainerHostUtilizationHistory> targetHosts = new ArrayList<>();
        ContainerHost selectedHost = null;
        for (ContainerHost host : hostList) {
            if (excludedHostList.contains(host)) {
                continue;
            }
            if (host instanceof PowerContainerHostUtilizationHistory) {
                targetHosts.add((PowerContainerHostUtilizationHistory)host);
            }
        }
        //for (int i = 0; i < targetHosts.size(); i++) {
		//	System.out.println("Host number "+ i+" has cpu of "+ targetHosts.get(i).getUtilizationOfCpu());
		//}
        if(targetHosts.size()==0) return null;
        double [] phermone = new double [targetHosts.size()];
        for (int i = 0; i < phermone.length; i++) {
			phermone[i] = 0.1;
		}
        double [] new_phermone = new double [targetHosts.size()];
        for (int i = 0; i < 50; i++) {
    		//ants
        	double summation = MathUtil.sum(phermone);
			for (int j = 0; j < 50; j++) {
				double [] probability = new double [targetHosts.size()];
				
				for (int k = 0; k < targetHosts.size(); k++) {
					probability[k] = phermone[k]/summation;
				}
				int selectedTarget = RandomSelector.SelectItem(probability);
				//update the phermone
				new_phermone[selectedTarget] += targetHosts.get(selectedTarget).getUtilizationOfCpu();
			}
			for (int j = 0; j < phermone.length; j++) {
				phermone[j] *=0.8;
				phermone[j]+= new_phermone[j];
				if(phermone[j]<0)
					phermone[j] = 0;
				new_phermone[j] = 0;
			}
		}
        int selectedTarget = RandomSelector.SelectItem(phermone);
        //System.out.println("Selected target is = " + selectedTarget);
        return targetHosts.get(selectedTarget);
    }
}
