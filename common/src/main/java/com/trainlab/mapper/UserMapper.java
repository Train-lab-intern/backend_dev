package com.trainlab.mapper;

import com.trainlab.Enum.eSpecialty;
import com.trainlab.Enum.eUserLevel;
import com.trainlab.dto.*;
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
    @Mapping(target = "authenticationInfo.email", source = "email")
    @Mapping(target = "authenticationInfo.userPassword", source = "password")
    @Mapping(target = "created", expression = "java(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now().withNano(0)))")
    @Mapping(target = "changed", expression = "java(java.sql.Timestamp.valueOf(java.time.LocalDateTime.now().withNano(0)))")
    User toEntity(UserCreateDto userCreateDto);


    @Mapping(target = "authenticationInfo.email", source = "email")
    @Mapping(target = "authenticationInfo.userPassword", source = "userPassword")
    User toEntity(UserDto userDto);

//    @Mapping(target = "userLevel.level", source = "userLevel")

    @Mapping(target = "email", source = "authenticationInfo.email")
    @Mapping(target = "userPassword", source = "authenticationInfo.userPassword")
    UserDto toDto(User user);

//    @Mapping(target = "userLevel", source = "userLevel.level")

    @Mapping(target = "id", ignore = true) // Поле id сущности User генерируется автоматически
    @Mapping(target = "authenticationInfo.email", source = "email")
    User toEntity(UserPageDto dto);

    @Mapping(target = "email", source = "authenticationInfo.email")
    UserPageDto toUserPageDto(User entity);

    @Mapping(target = "id", ignore = true) // Поле id сущности UserPageUpdateDto не используется при обновлении
    User toEntity(UserPageUpdateDto dto, eUserLevel userLevel, eSpecialty speciality);

    UserPageUpdateDto toPageUpdateDto(User entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User partialUpdateToEntity(UserPageUpdateDto userPageUpdateDto, @MappingTarget User user);
}