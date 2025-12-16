package com.hmvsoluciones.saas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ClienteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Cliente getClienteSample1() {
        return new Cliente()
            .id(1L)
            .nombre("nombre1")
            .celular("celular1")
            .correo("correo1")
            .identificacion("identificacion1")
            .direccion("direccion1");
    }

    public static Cliente getClienteSample2() {
        return new Cliente()
            .id(2L)
            .nombre("nombre2")
            .celular("celular2")
            .correo("correo2")
            .identificacion("identificacion2")
            .direccion("direccion2");
    }

    public static Cliente getClienteRandomSampleGenerator() {
        return new Cliente()
            .id(longCount.incrementAndGet())
            .nombre(UUID.randomUUID().toString())
            .celular(UUID.randomUUID().toString())
            .correo(UUID.randomUUID().toString())
            .identificacion(UUID.randomUUID().toString())
            .direccion(UUID.randomUUID().toString());
    }
}
