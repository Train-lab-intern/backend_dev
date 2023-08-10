package com.trainlab.mapper;

import com.trainlab.dto.UserCreateRequestDto;
import com.trainlab.dto.UserFindAllResponseDto;
import com.trainlab.dto.UserFindByIdResponseDto;
import com.trainlab.dto.UserUpdateRequestDto;
import com.trainlab.dto.UserUpdateResponseDto;
import com.trainlab.model.User;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "username", source = "username")
    @Mapping(target = "authenticationInfo.email", source = "email")
    @Mapping(target = "authenticationInfo.userPassword", source = "password")
    @Mapping(target = "created", expression = "java(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()))")
    @Mapping(target = "changed", expression = "java(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()))")
    User toEntity(UserCreateRequestDto userCreateRequestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "changed", expression = "java(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now()))")
    User partialUpdateToEntity(UserUpdateRequestDto userUpdateRequestDto, @MappingTarget User user);

    @Mapping(target = "username", source = "username")
    @Mapping(target = "email", source = "authenticationInfo.email")
    @Mapping(target = "password", source = "authenticationInfo.userPassword")
    UserUpdateResponseDto toDto(User user);

    @Mapping(target = "email", source = "authenticationInfo.email")
    UserFindAllResponseDto toFindAllDto(User user);

    @Mapping(target = "email", source = "authenticationInfo.email")
    UserFindByIdResponseDto toFindByIdDto(User user);
}