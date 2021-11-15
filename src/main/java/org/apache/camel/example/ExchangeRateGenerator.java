package org.apache.camel.example;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.apache.camel.Exchange;

import javax.inject.Named;
import javax.inject.Singleton;
import java.time.Instant;
import java.util.Random;

@Singleton
@Named
@RegisterForReflection(fields = false)
public class ExchangeRateGenerator {

    private static final int LOWEST_QUOTE_VALUE = 860000;
    private static final int HIGHEST_QUOTE_VALUE = 860100;
    private final Random random = new Random(LOWEST_QUOTE_VALUE);

    public void process(Exchange exchange) throws Exception {
         ExchangeRate exchangeRate = new ExchangeRate();
         exchangeRate.setTimestamp(Instant.now().getEpochSecond());
         exchangeRate.setValue(getQuote());

         exchange.getMessage().setBody(exchangeRate);
    }

    private int getQuote() {
        return random.nextInt((HIGHEST_QUOTE_VALUE - LOWEST_QUOTE_VALUE) + 1) + LOWEST_QUOTE_VALUE;
    }
}
