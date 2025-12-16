package com.hmvsoluciones.saas.service.mapper;

import com.hmvsoluciones.saas.domain.Template;
import com.hmvsoluciones.saas.service.dto.TemplateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Template} and its DTO {@link TemplateDTO}.
 */
@Mapper(componentModel = "spring")
public interface TemplateMapper extends EntityMapper<TemplateDTO, Template> {}
