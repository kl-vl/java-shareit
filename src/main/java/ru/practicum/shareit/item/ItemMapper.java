package ru.practicum.shareit.item;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.User;

@Component
@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", source = "ownerId")
    @Mapping(target = "request", source = "requestId")
    Item toItem(ItemDto dto);

    @Mapping(target = "ownerId", source = "owner.id")
    @Mapping(target = "requestId", source = "request")
    ItemDto toDto(Item item);

    @Mapping(target = "owner", source = "ownerId")
    @Mapping(target = "request", source = "requestId")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(ItemDto dto, @MappingTarget Item item);

    default User map(Long ownerId) {
        if (ownerId == null) return null;
        return User.builder().id(ownerId).build();
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", ignore = true)
    @Mapping(target = "request", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateItemFromDto(ItemDto dto, @MappingTarget Item entity);
}