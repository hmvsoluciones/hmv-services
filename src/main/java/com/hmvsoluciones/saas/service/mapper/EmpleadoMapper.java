package com.hmvsoluciones.saas.service.mapper;

import com.hmvsoluciones.saas.domain.Empleado;
import com.hmvsoluciones.saas.service.dto.EmpleadoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Empleado} and its DTO {@link EmpleadoDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmpleadoMapper extends EntityMapper<EmpleadoDTO, Empleado> {}
