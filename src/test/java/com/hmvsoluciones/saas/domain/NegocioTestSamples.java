package com.hmvsoluciones.saas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class NegocioTestSamples {

  private static final Random random = new Random();
  private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

  public static Negocio getNegocioSample1() {
    return new Negocio()
      .id(1L)
      .nombre("nombre1")
      .responsable("responsable1")
      .celular("celular1")
      .correo("correo1")
      .subscriptionKey("subscriptionKey1");
  }

  public static Negocio getNegocioSample2() {
    return new Negocio()
      .id(2L)
      .nombre("nombre2")
      .responsable("responsable2")
      .celular("celular2")
      .correo("correo2")
      .subscriptionKey("subscriptionKey2");
  }

  public static Negocio getNegocioRandomSampleGenerator() {
    return new Negocio()
      .id(longCount.incrementAndGet())
      .nombre(UUID.randomUUID().toString())
      .responsable(UUID.randomUUID().toString())
      .celular(UUID.randomUUID().toString())
      .correo(UUID.randomUUID().toString())
      .subscriptionKey(UUID.randomUUID().toString());
  }
}
