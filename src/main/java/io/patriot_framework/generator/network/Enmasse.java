/*
 * Copyright 2020 Patriot project
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.patriot_framework.generator.network;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.patriot_framework.generator.Data;
import io.patriot_framework.generator.network.wrappers.DataWrapper;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Enmasse implements NetworkAdapter {
    private String endpoint;
    private DataWrapper dataWrapper;
    private String token;
    private String registry_host;
    private String http_adapter_address;
    private int id;
    public boolean registered = false;
    List<AuthData> authDatas = new ArrayList<>();
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @JsonCreator
    public Enmasse(@JsonProperty("endpoint") String endpoint, @JsonProperty("auth-id") String auth_id, @JsonProperty("token") String token,
                   @JsonProperty("registry-host") String registry_host, @JsonProperty("http-adapter") String http_adapter_address,
                   @JsonProperty("id") int id, @JsonProperty("secrets") Map<String, String> secrets, @JsonProperty("wrapper") DataWrapper dataWrapper) {
        this.endpoint = endpoint;
        this.dataWrapper = dataWrapper;
        this.registry_host = registry_host;
        this.http_adapter_address = http_adapter_address;
        this.token = token;
        this.id = id;
        this.authDatas.add(new AuthData("hashed-password", auth_id, secrets));
    }

    private void setDefaultUnirestConfig() {
        Unirest.config().verifySsl(false);
        Unirest.config().addDefaultHeader("Content-Type", "application/json");
    }

    private void registerDeviceId() throws Exception {
        HttpResponse<JsonNode> response = Unirest.post("https://" + registry_host + "/v1/devices/myapp.iot/" + id)
                .header("Authorization", "Bearer " + token)
                .asJson();
        if (!response.isSuccess()) {
            throw new Exception("Could not register device ID. " + response.getStatusText());
        }
    }

    private void registerDeviceCredentials() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String authDatajson = mapper.writeValueAsString(authDatas);

        HttpResponse<JsonNode> response = Unirest.put("https://" + registry_host + "/v1/credentials/myapp.iot/" + id)
                .header("Authorization", "Bearer " + token)
                .body(authDatajson)
                .asJson();
        if (!response.isSuccess()) {
            throw new Exception("Could not register device credentials. " + response.getStatusText());
        }
    }

    public void registerDevice() throws Exception {
        setDefaultUnirestConfig();
        registerDeviceId();
        registerDeviceCredentials();
        LOGGER.info("Device is now registered to device registry!");
        registered = true;
    }

    @Override
    public void send(List<Data> data) {
        if (registered) {
            try {
                send_req(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            LOGGER.debug("Device is not registered to device registry");
            LOGGER.info("Registering device");
            try {
                registerDevice();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void send_req(List<Data> data) throws IOException {
        String se = dataWrapper.wrapData(data);
        HashMap<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");
        Unirest.config().verifySsl(false);
        LOGGER.info("Sending data to: " + "https://" + http_adapter_address);
        HttpResponse<JsonNode> response = Unirest.post("https://" + http_adapter_address)
                //HttpResponse<JsonNode> response = Unirest.post("https://envjp0tny67i.x.pipedream.net/")
                .headers(headers)
                .basicAuth(authDatas.get(0).getAuth_id() + "@myapp.iot", authDatas.get(0).getSecrets().get(0).get("pwd-plain"))
                .body(se)
                .asJson();
        if (!response.isSuccess()) {
            LOGGER.warn("Posting data ended with error code: " + response.getStatus());
            throw new IOException(response.getStatusText());
        }
    }

    @Override
    public void setDataWrapper(DataWrapper dataWrapper) {
        this.dataWrapper = dataWrapper;
    }

    @Override
    public DataWrapper getDataWrapper() {
        return dataWrapper;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRegistry_host() {
        return registry_host;
    }

    public void setRegistry_host(String registry_host) {
        this.registry_host = registry_host;
    }

    public String getHttp_adapter_address() {
        return http_adapter_address;
    }

    public void setHttp_adapter_address(String http_adapter_address) {
        this.http_adapter_address = http_adapter_address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
