package com.hmvsoluciones.saas.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ServicioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Servicio getServicioSample1() {
        return new Servicio().id(1L);
    }

    public static Servicio getServicioSample2() {
        return new Servicio().id(2L);
    }

    public static Servicio getServicioRandomSampleGenerator() {
        return new Servicio().id(longCount.incrementAndGet());
    }
}
