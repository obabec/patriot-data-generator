/*
 * Copyright 2018 Patriot project
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

package com.redhat.patriot.generator.device;

import com.redhat.patriot.generator.dataFeed.DataFeed;
import com.redhat.patriot.generator.network.NetworkAdapter;

import java.util.Collections;
import java.util.List;

/**
 * Created by jsmolar on 7/3/18.
 */
public abstract class AbstractSensor<E,T> extends AbstractDevice implements Sensor {

    private DataFeed<T> dataFeed;

    private Class<E> outputType;
    private Class<T> inputType;

    public AbstractSensor(String label) {
        super(label);
    }

    public AbstractSensor(String label, DataFeed<T> dataFeed) {
        super(label);
        this.dataFeed = dataFeed;

        if (!inputType.isAssignableFrom(outputType)) {

        }
    }

    @Override
    public List<E> requestData(Object... param) {
        if(dataFeed == null) {
            throw new IllegalArgumentException("Data Feed cannot be null");
        }

        T newData = dataFeed.getNextValue(param);
        E result = outputType.cast(newData);

        if(getNetworkAdapter() != null) {
            sendData(result);
        }

        return Collections.singletonList(result);
    }

    private void sendData(E data) {
        String dw = getDataWrapper().wrapData(this, data);
        NetworkAdapter networkAdapter = getNetworkAdapter();
        if(networkAdapter != null) {
            networkAdapter.send(dw);
        }
    }

    @Override
    public void setDataFeed(DataFeed dataFeed) {
        this.dataFeed = dataFeed;
    }

    @Override
    public DataFeed getDataFeed() {
        return dataFeed;
    }

}
