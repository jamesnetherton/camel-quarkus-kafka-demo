/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.camel.example;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class Routes extends RouteBuilder {

    private final List<ExchangeRate> exchangeRates = new CopyOnWriteArrayList<>();

    @Override
    public void configure() throws Exception {

        // Generate exchange rates
        from("timer:generateExchangeRates?period={{timer.period}}&delay=5s")
            .bean("exchangeRateGenerator")
            .marshal("jackson")
            .log("Producing to Kafka topic: ${body}")
            .to("kafka:{{kafka.topic}}");

        // Consume exchange rates
        from("kafka:{{kafka.topic}}")
            .unmarshal("jackson")
            .log("Consumed from Kafka topic: ${body}")
            .process(new Processor() {
                @Override
                public void process(Exchange exchange) throws Exception {
                    if (exchangeRates.size() == 20) {
                        exchangeRates.remove(0);
                    }
                    exchangeRates.add(exchange.getMessage().getBody(ExchangeRate.class));
                }
            });

        // Expose collected exchange rates to the web UI
        from("platform-http:/exchangerates")
                .setBody().constant(exchangeRates)
                .marshal("jackson");
    }

    @Singleton
    @Named("jackson")
    public JacksonDataFormat jackson (ObjectMapper objectMapper) {
        JacksonDataFormat dataFormat = new JacksonDataFormat();
        dataFormat.setObjectMapper(objectMapper);
        dataFormat.setUnmarshalType(ExchangeRate.class);
        return dataFormat;
    }
}
