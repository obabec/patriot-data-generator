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

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@JsonPropertyOrder({"type", "auth-id", "secrets"})
public class AuthData {
    private String type;
    @JsonProperty("auth-id")
    private String auth_id;

    private List<Map<String, String>> secrets = new ArrayList<>();

    public AuthData(String type, String auth_id, Map<String, String> secrets) {
        this.type = type;
        this.auth_id = auth_id;
        this.secrets.add(secrets);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuth_id() {
        return auth_id;
    }

    public void setAuth_id(String auth_id) {
        this.auth_id = auth_id;
    }

    public List<Map<String, String>> getSecrets() {
        return secrets;
    }

    public void setSecrets(List<Map<String, String>> secrets) {
        this.secrets = secrets;
    }
}
