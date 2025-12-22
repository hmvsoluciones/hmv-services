package com.hmvsoluciones.saas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CitaTestSamples {

  private static final Random random = new Random();
  private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
  private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

  public static Cita getCitaSample1() {
    return new Cita().id(1L).duracionMinutos(1).descripcion("descripcion1");
  }

  public static Cita getCitaSample2() {
    return new Cita().id(2L).duracionMinutos(2).descripcion("descripcion2");
  }

  public static Cita getCitaRandomSampleGenerator() {
    return new Cita().id(longCount.incrementAndGet()).duracionMinutos(intCount.incrementAndGet()).descripcion(UUID.randomUUID().toString());
  }
}
