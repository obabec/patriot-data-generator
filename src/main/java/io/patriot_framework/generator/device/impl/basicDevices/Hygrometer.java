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

package io.patriot_framework.generator.device.impl.basicDevices;

import io.patriot_framework.generator.dataFeed.DataFeed;
import io.patriot_framework.generator.device.passive.sensors.AbstractSimpleSensor;
import io.patriot_framework.generator.network.NetworkAdapter;

/**
 * Class should be used when creating Humidity sensor. Generic types of DataFeed and Sensor itself are the same.
 */
public class Hygrometer extends AbstractSimpleSensor {

    public Hygrometer() {
    }

    public Hygrometer(String label, DataFeed dataFeed) {
        super(label, dataFeed);
    }

}
