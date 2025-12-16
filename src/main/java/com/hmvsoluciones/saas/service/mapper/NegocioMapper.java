package com.hmvsoluciones.saas.service.mapper;

import com.hmvsoluciones.saas.domain.Negocio;
import com.hmvsoluciones.saas.service.dto.NegocioDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Negocio} and its DTO {@link NegocioDTO}.
 */
@Mapper(componentModel = "spring")
public interface NegocioMapper extends EntityMapper<NegocioDTO, Negocio> {}
