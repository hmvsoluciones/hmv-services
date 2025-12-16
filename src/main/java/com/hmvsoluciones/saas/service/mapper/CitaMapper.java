package com.hmvsoluciones.saas.service.mapper;

import com.hmvsoluciones.saas.domain.Cita;
import com.hmvsoluciones.saas.domain.Cliente;
import com.hmvsoluciones.saas.domain.Empleado;
import com.hmvsoluciones.saas.service.dto.CitaDTO;
import com.hmvsoluciones.saas.service.dto.ClienteDTO;
import com.hmvsoluciones.saas.service.dto.EmpleadoDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cita} and its DTO {@link CitaDTO}.
 */
@Mapper(componentModel = "spring")
public interface CitaMapper extends EntityMapper<CitaDTO, Cita> {
    @Mapping(target = "cliente", source = "cliente", qualifiedByName = "clienteId")
    @Mapping(target = "empleado", source = "empleado", qualifiedByName = "empleadoId")
    CitaDTO toDto(Cita s);

    @Named("clienteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ClienteDTO toDtoClienteId(Cliente cliente);

    @Named("empleadoId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmpleadoDTO toDtoEmpleadoId(Empleado empleado);
}
