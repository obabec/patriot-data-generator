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

package io.patriot_framework.generator.device.passive.sensors;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import io.patriot_framework.generator.Data;
import io.patriot_framework.generator.controll.server.SensorCoapController;
import io.patriot_framework.generator.dataFeed.DataFeed;
import io.patriot_framework.generator.device.AbstractDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**

 * Abstract class for device Composition - one unit with multiple DataFeeds
 */
public abstract class AbstractSensor extends AbstractDevice implements Sensor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractSensor.class);

    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
    private List<DataFeed> dataFeeds = new ArrayList<>();

    public AbstractSensor() {
    }

    public AbstractSensor(String label, DataFeed... dataFeeds) {
        super(label);
        Arrays.asList(dataFeeds).forEach(this::addDataFeed);
        setCoapController(new SensorCoapController(this));
    }

    @Override
    public List<Data> requestData(Object... param) {
        if (isEnabled()) return null;

        List<Data> result = new ArrayList<>();

        for(DataFeed df : dataFeeds) {
            Data newData = df.getNextValue();
            LOGGER.info(this.toString() + " new data: " + newData.toString());
            result.add(newData);
        }

        if(getNetworkAdapter() != null) {
            LOGGER.info("HELE MAM TADY POSILAT PICOVINY");
            getNetworkAdapter().send(result);
        }

        // TODO: Pipeline, all data modifiers should be replaced by pipeline implementation

        return result;
    }

    @Override
    @JsonSetter("dataFeeds")
    @JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "className")
    public void addDataFeed(DataFeed dataFeed) {
        this.dataFeeds.add(dataFeed);
    }

    @Override
    public void removeDataFeed(DataFeed dataFeed) {
        this.dataFeeds.remove(dataFeed);
    }

    @Override
    public List<DataFeed> getDataFeeds() {
        return Collections.unmodifiableList(dataFeeds);
    }

}
