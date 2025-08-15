package ru.practicum.shareit.item.comment;

import org.mapstruct.AfterMapping;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.BookingMapperDependencies;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Component
@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CommentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "item", source = "itemId")
    @Mapping(target = "author", source = "userId")
    @Mapping(target = "created", ignore = true)
    Comment toEntity(CommentDto dto);

    @Mapping(target = "userId", source = "author.id")
    @Mapping(target = "itemId", source = "item.id")
    @Mapping(target = "authorName", expression = "java(helper.getUserName(entity.getAuthor()))")
    CommentDto toDto(Comment entity, @Context BookingMapperDependencies helper);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "item", source = "itemId")
    @Mapping(target = "author", source = "userId")
    @Mapping(target = "created", ignore = true)
    void updateFromDto(CommentDto dto, @MappingTarget Comment entity);

    default Item mapItemIdToItem(Long itemId) {
        if (itemId == null) return null;
        Item item = new Item();
        item.setId(itemId);
        return item;
    }

    default User mapUserIdToUser(Long userId) {
        if (userId == null) return null;
        User user = new User();
        user.setId(userId);
        return user;
    }

    @AfterMapping
    default void setCreatedTime(@MappingTarget Comment comment) {
        if (comment.getCreated() == null) {
            comment.setCreated(LocalDateTime.now());
        }
    }
}