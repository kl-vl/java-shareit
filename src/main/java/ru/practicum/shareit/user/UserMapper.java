package ru.practicum.shareit.user;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Component
@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(target = "id", ignore = true)
    User toUserFromCreateDto(@Validated(UserDto.Create.class) UserDto dto);

    @Mapping(target = "id", ignore = true)
    User toUserFromUpdateDto(@Validated(UserDto.Update.class) UserDto dto);

    @Mapping(target = "id", source = "id")
    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true) // ID не обновляем
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromDto(UserDto dto, @MappingTarget User entity);
}
