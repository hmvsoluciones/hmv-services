package com.hmvsoluciones.saas.service.mapper;

import com.hmvsoluciones.saas.domain.Cita;
import com.hmvsoluciones.saas.domain.Servicio;
import com.hmvsoluciones.saas.service.dto.CitaDTO;
import com.hmvsoluciones.saas.service.dto.ServicioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Servicio} and its DTO {@link ServicioDTO}.
 */
@Mapper(componentModel = "spring")
public interface ServicioMapper extends EntityMapper<ServicioDTO, Servicio> {
  @Mapping(target = "cita", source = "cita", qualifiedByName = "citaId")
  ServicioDTO toDto(Servicio s);

  @Named("citaId")
  @BeanMapping(ignoreByDefault = true)
  @Mapping(target = "id", source = "id")
  CitaDTO toDtoCitaId(Cita cita);
}
