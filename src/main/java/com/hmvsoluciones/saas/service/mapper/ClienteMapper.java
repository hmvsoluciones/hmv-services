package com.hmvsoluciones.saas.service.mapper;

import com.hmvsoluciones.saas.domain.Cliente;
import com.hmvsoluciones.saas.service.dto.ClienteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Cliente} and its DTO {@link ClienteDTO}.
 */
@Mapper(componentModel = "spring")
public interface ClienteMapper extends EntityMapper<ClienteDTO, Cliente> {}
