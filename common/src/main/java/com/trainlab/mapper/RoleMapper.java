package com.trainlab.mapper;

import com.trainlab.dto.RoleCreateDto;
import com.trainlab.dto.RoleDto;
import com.trainlab.dto.RoleUpdateDto;
import com.trainlab.model.Role;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface RoleMapper {
    @Mapping(target = "created", expression = "java(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now().withNano(0)))")
    @Mapping(target = "changed", expression = "java(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now().withNano(0)))")
    Role toEntity(RoleCreateDto roleCreateDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "changed", expression = "java(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now().withNano(0)))")
    Role partialUpdateToEntity(RoleUpdateDto roleUpdateDto, @MappingTarget Role role);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    RoleDto toDto(Role role);
    List<RoleDto> toListDto(List<Role> roles);
}