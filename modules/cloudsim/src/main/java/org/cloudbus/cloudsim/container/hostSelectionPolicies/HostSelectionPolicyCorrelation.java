package org.cloudbus.cloudsim.container.hostSelectionPolicies;

import org.cloudbus.cloudsim.container.core.*;
import org.cloudbus.cloudsim.container.utils.Correlation;

import java.util.List;
import java.util.Set;

/**
 * Created by Walid on 1/10/17.
 */
public class HostSelectionPolicyCorrelation extends HostSelectionPolicy {

	private HostSelectionPolicy fallbackPolicy;

	/**
	 * Instantiates a new power vm selection policy maximum correlation.
	 *
	 * @param fallbackPolicy
	 *            the fallback policy
	 */
	public HostSelectionPolicyCorrelation(
			final HostSelectionPolicy fallbackPolicy) {
		super();
		setFallbackPolicy(fallbackPolicy);
	}

	@Override
	public ContainerHost getHost(List<ContainerHost> hostList, Object obj,
			Set<? extends ContainerHost> excludedHostList) {

		double[] utilizationHistory;
		if (obj instanceof Container) {

			utilizationHistory = ((PowerContainer) obj)
					.getUtilizationHistoryList();
		} else {

			utilizationHistory = ((PowerContainerVm) obj)
					.getUtilizationHistoryList();
		}
		ContainerHost selectedHost = null;

		Correlation correlation = new Correlation();
		double minCor = 0.1;
		while (minCor <= 1) {
			for (ContainerHost host : hostList) {
				if (excludedHostList.contains(host)) {
					continue;
				}
				if (host instanceof PowerContainerHostUtilizationHistory) {
					double[] hostUtilization = ((PowerContainerHostUtilizationHistory) host)
							.getUtilizationHistory();
					if (hostUtilization.length >= 5) {
						double cor = correlation.getCor(hostUtilization,
								utilizationHistory);
						if (cor < minCor) {
							return host;

						}
					} else {
						return fallbackPolicy.getHost(hostList, obj,
								excludedHostList);
					}

				}
			}
			minCor+=0.1;
		}
		return fallbackPolicy.getHost(hostList, obj, excludedHostList);
	}

	public HostSelectionPolicy getFallbackPolicy() {
		return fallbackPolicy;
	}

	public void setFallbackPolicy(HostSelectionPolicy fallbackPolicy) {
		this.fallbackPolicy = fallbackPolicy;
	}

}
