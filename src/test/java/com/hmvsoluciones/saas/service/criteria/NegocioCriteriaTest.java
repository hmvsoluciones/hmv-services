package com.hmvsoluciones.saas.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NegocioCriteriaTest {

  @Test
  void newNegocioCriteriaHasAllFiltersNullTest() {
    var negocioCriteria = new NegocioCriteria();
    assertThat(negocioCriteria).is(criteriaFiltersAre(Objects::isNull));
  }

  @Test
  void negocioCriteriaFluentMethodsCreatesFiltersTest() {
    var negocioCriteria = new NegocioCriteria();

    setAllFilters(negocioCriteria);

    assertThat(negocioCriteria).is(criteriaFiltersAre(Objects::nonNull));
  }

  @Test
  void negocioCriteriaCopyCreatesNullFilterTest() {
    var negocioCriteria = new NegocioCriteria();
    var copy = negocioCriteria.copy();

    assertThat(negocioCriteria).satisfies(
      criteria ->
        assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))),
      criteria -> assertThat(criteria).isEqualTo(copy),
      criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
    );

    assertThat(copy).satisfies(
      criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
      criteria -> assertThat(criteria).isEqualTo(negocioCriteria)
    );
  }

  @Test
  void negocioCriteriaCopyDuplicatesEveryExistingFilterTest() {
    var negocioCriteria = new NegocioCriteria();
    setAllFilters(negocioCriteria);

    var copy = negocioCriteria.copy();

    assertThat(negocioCriteria).satisfies(
      criteria ->
        assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))),
      criteria -> assertThat(criteria).isEqualTo(copy),
      criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
    );

    assertThat(copy).satisfies(
      criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
      criteria -> assertThat(criteria).isEqualTo(negocioCriteria)
    );
  }

  @Test
  void toStringVerifier() {
    var negocioCriteria = new NegocioCriteria();

    assertThat(negocioCriteria).hasToString("NegocioCriteria{}");
  }

  private static void setAllFilters(NegocioCriteria negocioCriteria) {
    negocioCriteria.id();
    negocioCriteria.nombre();
    negocioCriteria.responsable();
    negocioCriteria.celular();
    negocioCriteria.correo();
    negocioCriteria.subscriptionKey();
    negocioCriteria.esActivo();
    negocioCriteria.distinct();
  }

  private static Condition<NegocioCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
    return new Condition<>(
      criteria ->
        condition.apply(criteria.getId()) &&
        condition.apply(criteria.getNombre()) &&
        condition.apply(criteria.getResponsable()) &&
        condition.apply(criteria.getCelular()) &&
        condition.apply(criteria.getCorreo()) &&
        condition.apply(criteria.getSubscriptionKey()) &&
        condition.apply(criteria.getEsActivo()) &&
        condition.apply(criteria.getDistinct()),
      "every filter matches"
    );
  }

  private static Condition<NegocioCriteria> copyFiltersAre(NegocioCriteria copy, BiFunction<Object, Object, Boolean> condition) {
    return new Condition<>(
      criteria ->
        condition.apply(criteria.getId(), copy.getId()) &&
        condition.apply(criteria.getNombre(), copy.getNombre()) &&
        condition.apply(criteria.getResponsable(), copy.getResponsable()) &&
        condition.apply(criteria.getCelular(), copy.getCelular()) &&
        condition.apply(criteria.getCorreo(), copy.getCorreo()) &&
        condition.apply(criteria.getSubscriptionKey(), copy.getSubscriptionKey()) &&
        condition.apply(criteria.getEsActivo(), copy.getEsActivo()) &&
        condition.apply(criteria.getDistinct(), copy.getDistinct()),
      "every filter matches"
    );
  }
}
