package org.cloudbus.cloudsim.container.containerSelectionPolicies;

import org.cloudbus.cloudsim.container.core.PowerContainerHost;
import org.cloudbus.cloudsim.container.core.Container;
import org.cloudbus.cloudsim.container.core.PowerContainer;
import org.cloudbus.cloudsim.container.core.PowerContainerHostUtilizationHistory;
import org.cloudbus.cloudsim.util.MathUtil;

import java.util.List;

/**
 * Created by Walid on 1/10/17.
 */
public class PowerContainerSelectionPolicyMinChangeRate extends PowerContainerSelectionPolicy {
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
        Container containerToMigrate = null;
        double minMetric = Double.MAX_VALUE;
        for (Container container : migratableContainers) {
            if (container.isInMigration()) {
                continue;
            }
           double metric = MathUtil.magnitude(((PowerContainer) container).getUtilizationHistoryList());
            if (minMetric > metric) {
                minMetric = metric;
                containerToMigrate = container;
            }
        }
        return containerToMigrate;
    }
}
