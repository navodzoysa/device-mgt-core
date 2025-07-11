/*
 * Copyright (c) 2018 - 2024, Entgra (Pvt) Ltd. (http://www.entgra.io) All Rights Reserved.
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

package io.entgra.device.mgt.core.device.mgt.core.report.mgt;

import io.entgra.device.mgt.core.device.mgt.common.device.details.DeviceDetailsWrapper;
import io.entgra.device.mgt.core.device.mgt.common.exceptions.EventPublishingException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.net.ConnectException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ReportingPublisherManager {

    private static final Log log = LogFactory.getLog(ReportingPublisherManager.class);
    private final ExecutorService executorService;
    private static ReportingPublisherManager instance;
    private final CloseableHttpClient client;

    private ReportingPublisherManager() {
        executorService = Executors.newFixedThreadPool(10); //todo make this configurable
        PoolingHttpClientConnectionManager poolingManager = new PoolingHttpClientConnectionManager();
        poolingManager.setMaxTotal(10); //todo make this configurable
        poolingManager.setDefaultMaxPerRoute(10);
        client = HttpClients.custom()
                .setConnectionManager(poolingManager)
                .build();

    }

    public static synchronized ReportingPublisherManager getInstance() {
        if (instance == null) {
            instance = new ReportingPublisherManager();
        }
        return instance;
    }


//    public Future<Integer> publishData(DeviceDetailsWrapper deviceDetailsWrapper, String eventUrl) {
//        this.payload = deviceDetailsWrapper;
//        this.endpoint = eventUrl;
//        return executorService.submit(new ReportingPublisher());
//    }

    public Future<Integer> publishData(DeviceDetailsWrapper deviceDetailsWrapper, String eventUrl) {
        return executorService.submit(new ReportingPublisher(deviceDetailsWrapper, eventUrl));
    }

    private class ReportingPublisher implements Callable<Integer> {
        private final DeviceDetailsWrapper payload;
        private final String endpoint;

        public ReportingPublisher(DeviceDetailsWrapper payload, String endpoint) {
            this.payload = payload;
            this.endpoint = endpoint;
        }

        @Override
        public Integer call() throws EventPublishingException {
            HttpPost apiEndpoint = new HttpPost(endpoint);
            try  {
                apiEndpoint.setHeader(HTTP.CONTENT_TYPE, ContentType.APPLICATION_JSON.toString());
                StringEntity requestEntity = new StringEntity(payload.getJSONString(), ContentType.APPLICATION_JSON);
                apiEndpoint.setEntity(requestEntity);
                HttpResponse response = client.execute(apiEndpoint);
                int statusCode = response.getStatusLine().getStatusCode();
                if (log.isDebugEnabled()) {
                    log.debug("Published data to the reporting backend: " + endpoint + ", Response code: " + statusCode);
                }
                return statusCode;
            } catch (ConnectException e) {
                String message = "Connection refused while publishing reporting data to the API: " + endpoint;
                log.error(message, e);
                throw new EventPublishingException(message, e);
            } catch (IOException e) {
                String message = "Error occurred when publishing reporting data to the API: " + endpoint;
                log.error(message, e);
                throw new EventPublishingException(message, e);
            } finally {
                apiEndpoint.releaseConnection();
            }
        }
    }
}
