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

package io.patriot_framework.sample;

import io.patriot_framework.generator.dataFeed.DataFeed;
import io.patriot_framework.generator.dataFeed.NormalDistributionDataFeed;
import io.patriot_framework.generator.device.impl.basicDevices.Thermometer;
import io.patriot_framework.generator.device.passive.sensors.SimpleSensor;
import io.patriot_framework.generator.network.Enmasse;
import io.patriot_framework.generator.network.wrappers.JSONWrapper;

import java.util.HashMap;

public class TestMain {

    public static void main(String[] args) {
        DataFeed dataFeed = new NormalDistributionDataFeed(18, 3);
        HashMap<String, String> secrets = new HashMap<>();
        secrets.put("plain", "shit");
        Enmasse enmasse = new Enmasse("xxx", "xxx", "xxx",
                "fuckit", "fuckit2", 2, secrets, new JSONWrapper());
        SimpleSensor device = new Thermometer("ThermometerExample", dataFeed);
        device.setNetworkAdapter(enmasse);

        enmasse.send(device.requestData());
    }

}
