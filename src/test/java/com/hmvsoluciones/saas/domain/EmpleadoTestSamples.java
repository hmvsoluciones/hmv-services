package com.hmvsoluciones.saas.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EmpleadoTestSamples {

  private static final Random random = new Random();
  private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

  public static Empleado getEmpleadoSample1() {
    return new Empleado().id(1L).nombre("nombre1").celular("celular1").especialidad("especialidad1");
  }

  public static Empleado getEmpleadoSample2() {
    return new Empleado().id(2L).nombre("nombre2").celular("celular2").especialidad("especialidad2");
  }

  public static Empleado getEmpleadoRandomSampleGenerator() {
    return new Empleado()
      .id(longCount.incrementAndGet())
      .nombre(UUID.randomUUID().toString())
      .celular(UUID.randomUUID().toString())
      .especialidad(UUID.randomUUID().toString());
  }
}
