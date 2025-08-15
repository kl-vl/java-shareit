package ru.practicum.shareit.item;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.util.List;

@Component
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "owner", source = "ownerId")
    @Mapping(target = "request", source = "requestId")
    Item toItem(ItemDto dto);

    @Named("toSimpleDto")
    @Mapping(target = "ownerId", source = "owner.id")
    @Mapping(target = "requestId", source = "request")
    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    @Mapping(target = "comments", ignore = true)
    ItemDto toDto(Item item);

    @Mapping(target = "owner", source = "ownerId")
    @Mapping(target = "request", source = "requestId")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDto(ItemDto dto, @MappingTarget Item item);

    @Named("toDtoWithComments")
    @Mapping(target = "requestId", source = "request.id")
    @Mapping(target = "ownerId", source = "owner.id")
    @Mapping(target = "comments", source = "comments")
    ItemDto toDtoWithComments(Item item);

    List<CommentDto> mapComments(List<Comment> comments);

    default User map(Long ownerId) {
        if (ownerId == null) return null;
        User user = new User();
        user.setId(ownerId);
        return user;
    }

    default void patchItem(ItemDto dto, @MappingTarget Item item) {
        if (dto.getName() != null) {
            item.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            item.setDescription(dto.getDescription());
        }
        if (dto.getAvailable() != null) {
            item.setAvailable(dto.getAvailable());
        }

    }

    default ItemRequest mapRequestIdToItemRequest(Long requestId) {
        if (requestId == null) return null;
        ItemRequest request = new ItemRequest();
        request.setId(requestId);
        return request;
    }

    default Long mapItemRequestToRequestId(ItemRequest request) {
        if (request == null) return null;
        return request.getId();
    }

}