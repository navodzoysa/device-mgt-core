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

package io.entgra.device.mgt.core.webapp.authenticator.framework.authorizer;

import io.entgra.device.mgt.core.webapp.authenticator.framework.AuthenticationInfo;
import io.entgra.device.mgt.core.webapp.authenticator.framework.authenticator.WebappAuthenticator;
import org.apache.catalina.connector.Request;

/**
 * This class represents the methods that are used to authorize requests based on the tenant subscription.
 */
public class WebappTenantAuthorizer {
	private static final String SHARED_WITH_ALL_TENANTS_PARAM_NAME = "isSharedWithAllTenants";
	private static final String PROVIDER_TENANT_DOMAIN_PARAM_NAME = "providerTenantDomain";

	public static WebappAuthenticator.Status authorize(Request request, AuthenticationInfo authenticationInfo) {
		String tenantDomain = authenticationInfo.getTenantDomain();
		if (tenantDomain != null && isSharedWithAllTenants(request) || isProviderTenant(request, tenantDomain)) {
			return WebappAuthenticator.Status.CONTINUE;
		}
		return WebappAuthenticator.Status.FAILURE;
	}

	private static boolean isSharedWithAllTenants(Request request) {
		String param = request.getContext().findParameter(SHARED_WITH_ALL_TENANTS_PARAM_NAME);
		return (param == null || Boolean.parseBoolean(param));
	}

	private static boolean isProviderTenant(Request request, String requestTenantDomain) {
        Object tenantDomain = request.getServletContext().getAttribute(PROVIDER_TENANT_DOMAIN_PARAM_NAME);
		String param = null;
        if (tenantDomain != null) {
            param = (String)tenantDomain;
        }
		return (param == null || requestTenantDomain.equals(param));
	}
}
