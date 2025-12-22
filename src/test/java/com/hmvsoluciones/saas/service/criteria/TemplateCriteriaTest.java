package com.hmvsoluciones.saas.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TemplateCriteriaTest {

  @Test
  void newTemplateCriteriaHasAllFiltersNullTest() {
    var templateCriteria = new TemplateCriteria();
    assertThat(templateCriteria).is(criteriaFiltersAre(Objects::isNull));
  }

  @Test
  void templateCriteriaFluentMethodsCreatesFiltersTest() {
    var templateCriteria = new TemplateCriteria();

    setAllFilters(templateCriteria);

    assertThat(templateCriteria).is(criteriaFiltersAre(Objects::nonNull));
  }

  @Test
  void templateCriteriaCopyCreatesNullFilterTest() {
    var templateCriteria = new TemplateCriteria();
    var copy = templateCriteria.copy();

    assertThat(templateCriteria).satisfies(
      criteria ->
        assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))),
      criteria -> assertThat(criteria).isEqualTo(copy),
      criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
    );

    assertThat(copy).satisfies(
      criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
      criteria -> assertThat(criteria).isEqualTo(templateCriteria)
    );
  }

  @Test
  void templateCriteriaCopyDuplicatesEveryExistingFilterTest() {
    var templateCriteria = new TemplateCriteria();
    setAllFilters(templateCriteria);

    var copy = templateCriteria.copy();

    assertThat(templateCriteria).satisfies(
      criteria ->
        assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))),
      criteria -> assertThat(criteria).isEqualTo(copy),
      criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
    );

    assertThat(copy).satisfies(
      criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
      criteria -> assertThat(criteria).isEqualTo(templateCriteria)
    );
  }

  @Test
  void toStringVerifier() {
    var templateCriteria = new TemplateCriteria();

    assertThat(templateCriteria).hasToString("TemplateCriteria{}");
  }

  private static void setAllFilters(TemplateCriteria templateCriteria) {
    templateCriteria.id();
    templateCriteria.nombre();
    templateCriteria.activo();
    templateCriteria.distinct();
  }

  private static Condition<TemplateCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
    return new Condition<>(
      criteria ->
        condition.apply(criteria.getId()) &&
        condition.apply(criteria.getNombre()) &&
        condition.apply(criteria.getActivo()) &&
        condition.apply(criteria.getDistinct()),
      "every filter matches"
    );
  }

  private static Condition<TemplateCriteria> copyFiltersAre(TemplateCriteria copy, BiFunction<Object, Object, Boolean> condition) {
    return new Condition<>(
      criteria ->
        condition.apply(criteria.getId(), copy.getId()) &&
        condition.apply(criteria.getNombre(), copy.getNombre()) &&
        condition.apply(criteria.getActivo(), copy.getActivo()) &&
        condition.apply(criteria.getDistinct(), copy.getDistinct()),
      "every filter matches"
    );
  }
}
