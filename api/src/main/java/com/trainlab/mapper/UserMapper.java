package com.trainlab.mapper;

import com.trainlab.model.User;
import com.trainlab.security.principal.UserPrincipal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

// Changed
@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "roles", source = "roles")
    UserPrincipal toUserPrincipal(User user);
}
