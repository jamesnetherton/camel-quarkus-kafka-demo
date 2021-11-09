package org.apache.camel.example;

import org.junit.jupiter.api.Test;

import java.util.Random;

public class Foo {

    @Test
    public void foo() {
        Random random = new Random(860000);

        for (int i = 0; i < 10;  i++) {
            System.out.println(random.nextInt((865000 - 860000) + 1) + 860000);
        }

    }
}
