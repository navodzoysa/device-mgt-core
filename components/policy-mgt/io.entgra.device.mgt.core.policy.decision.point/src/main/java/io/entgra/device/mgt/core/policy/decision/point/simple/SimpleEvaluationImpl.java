/*
 * Copyright (c) 2018 - 2023, Entgra (Pvt) Ltd. (http://www.entgra.io) All Rights Reserved.
 *
 * Entgra (Pvt) Ltd. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package io.entgra.device.mgt.core.policy.decision.point.simple;

import io.entgra.device.mgt.core.device.mgt.common.DeviceIdentifier;
import io.entgra.device.mgt.core.device.mgt.common.policy.mgt.Policy;
import io.entgra.device.mgt.core.policy.decision.point.internal.PolicyDecisionPointDataHolder;
import io.entgra.device.mgt.core.policy.mgt.common.*;
import io.entgra.device.mgt.core.policy.mgt.core.PolicyManagerService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleEvaluationImpl implements SimpleEvaluation {

    private static final Log log = LogFactory.getLog(SimpleEvaluationImpl.class);
    private PolicyManagerService policyManagerService;
    private volatile List<Policy> policyList = new ArrayList<Policy>();

    @Override
    public Policy getEffectivePolicy(DeviceIdentifier deviceIdentifier) throws PolicyEvaluationException {
        Policy policy = new Policy();
        PolicyAdministratorPoint policyAdministratorPoint;
        PolicyInformationPoint policyInformationPoint;
        policyManagerService = getPolicyManagerService();

        try {
            if (policyManagerService != null) {

                policyInformationPoint = policyManagerService.getPIP();
                PIPDevice pipDevice = policyInformationPoint.getDeviceData(deviceIdentifier);
                policyList = policyInformationPoint.getRelatedPolicies(pipDevice);
                policyAdministratorPoint = policyManagerService.getPAP();
                sortPolicies();
                if(!policyList.isEmpty()) {
                    policy = policyList.get(0);
                } else {
                    policyAdministratorPoint.removePolicyUsed(deviceIdentifier);
                    return null;
                }
                //TODO : UNCOMMENT THE FOLLOWING CASE
                policyAdministratorPoint.setPolicyUsed(deviceIdentifier, policy);

            }

        } catch (PolicyManagementException e) {
            String msg = "Error occurred when retrieving the policy related data from policy management service.";
            log.error(msg, e);
            throw new PolicyEvaluationException(msg, e);
        }
        return policy;
    }

    @Override
    public synchronized void sortPolicies() throws PolicyEvaluationException {
        Collections.sort(policyList);
    }

    private PolicyManagerService getPolicyManagerService() {
        return PolicyDecisionPointDataHolder.getInstance().getPolicyManagerService();
    }
}
