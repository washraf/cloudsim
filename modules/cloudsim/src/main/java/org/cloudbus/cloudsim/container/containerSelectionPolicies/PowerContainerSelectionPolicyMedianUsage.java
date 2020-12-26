package org.cloudbus.cloudsim.container.containerSelectionPolicies;

import org.cloudbus.cloudsim.container.core.PowerContainerHost;
import org.cloudbus.cloudsim.container.core.Container;
import org.cloudbus.cloudsim.container.core.PowerContainer;
import org.cloudbus.cloudsim.container.lists.PowerContainerList;

import java.util.List;

/**
 * Created by Walid on 1/10/17.
 */
public class PowerContainerSelectionPolicyMedianUsage extends PowerContainerSelectionPolicy {
    /*
    * (non-Javadoc)
    * @see
    * PowerContainerSelectionPolicy#getContainerToMigrate
    */
    @Override
    public Container getContainerToMigrate(PowerContainerHost host) {
        List<PowerContainer> migratableContainers = getMigratableContainers(host);
        if (migratableContainers.isEmpty()) {
            return null;
        }
        PowerContainerList.sortByCpuUtilization(migratableContainers);
        int m = migratableContainers.size()/2;
        return migratableContainers.get(m);
    }
}
