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


package io.entgra.device.mgt.core.device.mgt.api.jaxrs.service.api;

import io.entgra.device.mgt.core.apimgt.annotations.Scope;
import io.entgra.device.mgt.core.apimgt.annotations.Scopes;
import io.entgra.device.mgt.core.device.mgt.api.jaxrs.util.Constants;
import io.swagger.annotations.*;

import javax.validation.constraints.Size;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@SwaggerDefinition(
        info = @Info(
                version = "1.0.0",
                title = "",
                extensions = {
                        @Extension(properties = {
                                @ExtensionProperty(name = "name", value = "remote_session_services"),
                                @ExtensionProperty(name = "context", value = "/api/device-mgt/v1.0/remote-session-services"),
                        })
                }
        ),
        tags = {
                @Tag(name = "device_management", description = "")
        }
)
@Scopes(
        scopes = {
                @Scope(
                        name = "Remote Session Connection",
                        description = "",
                        key = "dm:remote:connect",
                        roles = {"Internal/devicemgt-user"},
                        permissions = {"/device-mgt/devices/owning-device/remote-session"}
                )
        }
)
@Path("/remote-session-services")
@Api(value = "Remote Session Service",
        description = "This carries all the resources related to the remote session service functionality.")
public interface RemoteSessionService {
    /**
     * Retrieve Analytics for the device type
     */
    @GET
    @Path("connect/{deviceType}/{deviceId}")
    @ApiOperation(
            consumes = "application/json",
            produces = "application/json",
            httpMethod = "GET",
            value = "Connecting to a Device",
            notes = "You able to connect to a device to start a remote session using this API.",
            response = Response.class,
            tags = "Remote Session Service Management",
            extensions = {
                    @Extension(properties = {
                            @ExtensionProperty(name = Constants.SCOPE, value = "dm:remote:connect")
                    })
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "OK.",
                    response = Response.class,
                    responseHeaders = {
                            @ResponseHeader(
                                    name = "Content-Type",
                                    description = "The content type of the body"),
                            @ResponseHeader(
                                    name = "Last-Modified",
                                    description = "Date and time the resource was last modified.\n" +
                                            "Used by caches, or in conditional requests."),
                    }),
            @ApiResponse(
                    code = 400,
                    message = "Bad Request. \n Invalid Device Identifiers found.",
                    response = Response.class),
            @ApiResponse(
                    code = 401,
                    message = "Unauthorized. \n Unauthorized request."),
            @ApiResponse(
                    code = 500,
                    message = "Internal Server Error. \n Error on retrieving stats",
                    response = Response.class)
    })
    Response getRemoteSessionDeviceConnect(
            @ApiParam(
                    name = "deviceId",
                    value = "The device ID.",
                    required = true)
            @PathParam("deviceId") String deviceId,
            @ApiParam(
                    name = "deviceType",
                    value = "The device type, such as ios, android, or windows.",
                    required = true)
            @PathParam("deviceType")
            @Size(max = 45)
                    String deviceType);
}