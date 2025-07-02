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

package io.entgra.device.mgt.core.ui.request.interceptor.util;

public class HandlerConstants {
    public static final String PUBLISHER_APPLICATION_NAME = "application-mgt-publisher";
    public static final String APP_REG_ENDPOINT = "/api-application-registration/register";
    public static final String UI_CONFIG_ENDPOINT = "/api/device-mgt-config/v1.0/configurations/ui-config";
    public static final String OAUTH2_TOKEN_ENDPOINT = "/oauth2/token";
    public static final String INTERNAL_TOKEN_ENDPOINT = "/token";
    public static final String INTROSPECT_ENDPOINT = "/oauth2/introspect";
    public static final String AUTHORIZATION_ENDPOINT = "/oauth2/authorize";
    public static final String APIM_APPLICATIONS_ENDPOINT = "/api/am/devportal/v3/applications";
    public static final String IDENTITY_APP_MGT_ENDPOINT = "/services/IdentityApplicationManagementService.IdentityApplicationManagementServiceHttpsSoap11Endpoint";
    public static final String LOGIN_PAGE = "/login";
    public static final String SSO_LOGIN_CALLBACK = "/ssoLoginCallback";
    public static final String BASIC = "Basic ";
    public static final String BEARER = "Bearer ";
    public static final String X_FRAME_OPTIONS = "X-Frame-Options";
    public static final String X_FRAME_OPTIONS_SAMEORIGIN = "SAMEORIGIN";
    public static final String UPGRADE = "Upgrade";
    public static final String WEB_SOCKET = "websocket";
    public static final String HTTP_UPGRADE = "HTTP/1.1";
    public static final String UI_REQUEST_HANDLER_SUFFIX = "-ui-request-handler";
    public static final String LOGIN_SUFFIX = "-login";
    public static final String TENANT_CONTEXT_SUFFIX = "-tenant-context";
    public static final String TENANT_CONTEXT_ENABLED_APPS_KEY = "tenantContextEnabledApps";
    public static final String APP_REG_KEY = "appRegistration";
    public static final String TAGS_KEY = "tags";
    public static final String SCOPES_KEY = "scopes";
    public static final String APP_NAME_KEY = "applicationName";
    public static final String CLIENT_ID_KEY = "client_id";
    public static final String CLIENT_SECRET_KEY = "client_secret";
    public static final String ACCESS_TOKEN_KEY = "access_token";
    public static final String REFRESH_TOKEN_KEY = "refresh_token";
    public static final String SCOPE_KEY = "scope";
    public static final String SESSION_AUTH_DATA_KEY = "authInfo";
    public static final String SESSION_DEFAULT_AUTH_DATA_KEY = "defaultAuthInfo";
    public static final String SESSION_TENANT_CONTEXT_AUTH_DATA_KEY = "tenantContextAuthInfo";
    public static final String SESSION_TIMEOUT_KEY = "sessionTimeOut";
    public static final String UI_CONFIG_KEY = "ui-config";
    public static final String CALLBACK_URL_KEY = "callbackUrl";
    public static final String GRANT_TYPE_KEY = "supportedGrantTypes";
    public static final String IS_ALLOWED_TO_ALL_DOMAINS_KEY = "isAllowedToAllDomains";
    public static final String REGISTER_ON_SAME_TENANT = "registerOnSameTenant";
    public static final String JSESSIONID_KEY = "JSESSIONID";
    public static final String COMMON_AUTH_ID_KEY = "commonAuthId";
    public static final String PLATFORM = "platform";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String API_TENANT_CONTEXT = "/t/";
    public static final String API_COMMON_CONTEXT = "/api";
    public static final String EXECUTOR_EXCEPTION_PREFIX = "ExecutorException-";
    public static final String TOKEN_IS_EXPIRED = "ACCESS_TOKEN_IS_EXPIRED";
    public static final String DEFAULT_TOKEN_IS_EXPIRED = "{\"active\":false}";
    public static final String REPORTS = "Reports";
    public static final String APP_NAME = "App-Name";
    public static final String[] SSO_LOGOUT_COOKIE_PATHS = new String[]{"/", "/entgra-ui-request-handler",
            "/store-ui-request-handler", "/publisher-ui-request-handler", "/mdm-reports-ui-request-handler", "/devicemgt"};
    public static final String CODE_GRANT_TYPE = "authorization_code";
    public static final String REFRESH_TOKEN_GRANT_TYPE = "refresh_token";
    public static final String PASSWORD_GRANT_TYPE = "password";
    public static final String JWT_BEARER_GRANT_TYPE = "urn:ietf:params:oauth:grant-type:jwt-bearer";
    public static final String PRODUCTION_KEY = "PRODUCTION";
    public static final String LOGIN_CACHE_CAPACITY_KEY = "loginCacheCapacity";

    public static final String SCHEME_SEPARATOR = "://";
    public static final String URI_SEPARATOR = "/";
    public static final String QUERY_PARAM_KEY_VALUE_SEP = "=";
    public static final String COLON = ":";
    public static final String HTTP_PROTOCOL = "http";
    public static final String HTTPS_PROTOCOL = "https";
    public static final String UNDERSCORE = "_";
    public static final String URI_QUERY_SEPARATOR = "?";
    public static final String URL_ENCODE_SPACE = "%20";
    public static final String AT_SYMBOL = "@";
    public static final String HTTP_METHOD_DELETE = "DELETE";
    public static final String HTTP_METHOD_HEAD = "HEAD";
    public static final String HTTP_METHOD_GET = "GET";
    public static final String HTTP_METHOD_POST = "POST";
    public static final String HTTP_METHOD_PUT = "PUT";

    public static final int INTERNAL_ERROR_CODE = 500;
    public static final long TIMEOUT = 1200;

    public static final String OTP_HEADER = "one-time-token";

    public static final String AX_PREFIX = "ax2245:";
    public static final String PAYLOADS_DIR = "repository/resources/payloads";
    public static final String SOAP_ACTION_HEADER = "SOAPAction";
    public static final String REFERER_HEADER = "Referer";

    public static final String WSS_PROTOCOL = "wss";
    public static final String WS_PROTOCOL = "ws";
    public static final String REMOTE_SESSION_CONTEXT = "/remote/session/clients";
    public static final String GRAFANA_API = "/api/grafana-mgt/v1.0";

    public static final String IOT_CORE_HOST_ENV_VAR = "iot.core.host";
    public static final String IOT_CORE_HTTP_PORT_ENV_VAR = "iot.core.http.port";
    public static final String IOT_CORE_HTTPS_PORT_ENV_VAR = "iot.core.https.port";
    public static final String IOT_APIM_HOST_ENV_VAR = "iot.apim.host";
    public static final String IOT_APIM_HTTP_PORT_ENV_VAR = "iot.apim.http.port";
    public static final String IOT_APIM_HTTPS_PORT_ENV_VAR = "iot.apim.https.port";
    public static final String IOT_KM_HOST_ENV_VAR = "iot.keymanager.host";
    public static final String IOT_KM_HTTP_PORT_ENV_VAR = "iot.keymanager.http.port";
    public static final String IOT_KM_HTTPS_PORT_ENV_VAR = "iot.keymanager.https.port";
    public static final String IOT_GW_HOST_ENV_VAR = "iot.gateway.host";
    public static final String IOT_GW_HTTP_PORT_ENV_VAR = "iot.gateway.http.port";
    public static final String IOT_REMOTE_SESSION_HOST_ENV_VAR = "iot.remotesession.server.host";
    public static final String IOT_REMOTE_SESSION_HTTPS_PORT_ENV_VAR = "iot.remotesession.server.https.port";
    public static final String IOT_GATEWAY_WEBSOCKET_WSS_PORT_ENV_VAR = "iot.gateway.websocket.wss.port";
    public static final String IOT_GATEWAY_WEBSOCKET_WS_PORT_ENV_VAR = "iot.gateway.websocket.ws.port";
    public static final String IOT_GW_HTTPS_PORT_ENV_VAR = "iot.gateway.https.port";
    public static final String IOT_REPORTING_WEBAPP_HOST_ENV_VAR = "iot.reporting.webapp.host";
    public static final String USER_SCOPES = "userScopes";
    public static final String HUBSPOT_CHAT_URL = "api.hubapi.com";
    public static final String USERNAME_WITH_DOMAIN = "usernameWithDomain";
    public static final String JIT_PROVISION_CALLBACK_URL = "/jit-provision-callback";
    public static final String JIT_ENROLLMENT_HANDLER_CALLBACK_URL = "/jit-enrollment-callback";
    public static final String DCR_URL = "/client-registration/v0.17/register";
    public static final String SESSION_JIT_DATA_KEY = "JITInfo";
    public static final String SESSION_JIT_ENROLLMENT_DATA_KEY = "JITEnrollmentInfo";
    public static final String JIT_PROVISION_HANDLER = "/jit-provision";
    public static final String JIT_ENROLLMENT_AUTH_APP_KEY = "JIT_ENROLLMENT_AUTH_APP";
    public static final String CLIENT_CREDENTIAL_GRANT_TYPE = "client_credentials";
    public static final String OS_ANDROID = "android";
    public static final String OS_WINDOWS = "windows";
    public static final String OS_IOS = "ios";
    public static final String TAG_ANDROID_ENROLLMENT_SCOPES = "AndroidEnrollmentScopes";
    public static final String TAG_WINDOWS_ENROLLMENT_SCOPES = "WindowsEnrollmentScopes";
    public static final String TAG_IOS_ENROLLMENT_SCOPES = "IOSEnrollmentScopes";
}
