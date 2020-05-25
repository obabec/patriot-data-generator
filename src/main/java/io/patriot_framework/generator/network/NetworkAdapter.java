/*
 * Copyright 2019 Patriot project
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

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.patriot_framework.generator.Data;
import io.patriot_framework.generator.network.wrappers.DataWrapper;

import java.util.List;

/**
 * Provides possibility to send data by network protocol to right destination.
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Rest.class, name = "rest"),
        @JsonSubTypes.Type(value = Enmasse.class, name = "online"),
        @JsonSubTypes.Type(value = MQTT.class, name = "mqtt")
})
public interface NetworkAdapter {

    /**
     * Sends data which are wrapped by DataWrappers, there fore in String format.
     *
     * @param data data to be send
     */
    void send(List<Data> data);

    /**
     * Returns DataWrapper that is used by Device
     *
     * @return instance of DataWrapper
     */
    DataWrapper getDataWrapper();

    /**
     * Sets DataWrapper for Device, which allows to transform generated data and important
     * information about its origin to common data structures e.g. JSON, XML, for better manipulation.
     *
     * @param dataWrapper instance of DataWrapper
     */
    void setDataWrapper(DataWrapper dataWrapper);

}
