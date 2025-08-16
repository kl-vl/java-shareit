package ru.practicum.shareit.booking;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserMapper;

@Mapper(componentModel = "spring", uses = {ItemMapper.class, UserMapper.class},
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface BookingMapper {


    @Mapping(target = "item", source = "item", qualifiedByName = "toSimpleDto")
    @Mapping(target = "booker", source = "booker")
    BookingDto toDto(Booking booking);

    @Mapping(target = "item", expression = "java(helper.findItemById(createDto.getItemId()))")
    @Mapping(target = "booker", expression = "java(helper.findUserById(createDto.getBookerId()))")
    @Mapping(target = "status", defaultExpression = "java(BookingStatus.WAITING)")
    Booking toBooking(BookingDto createDto, @Context BookingMapperDependencies helper);

    @Mapping(target = "status", ignore = true)
    @Mapping(target = "item", source = "itemId", qualifiedByName = "mapItem")
    @Mapping(target = "booker", source = "bookerId", qualifiedByName = "mapBooker")
    void updateFromDto(BookingDto dto, @MappingTarget Booking entity);

    @Named("mapItem")
    default Item map(Long itemId) {
        if (itemId == null) {
            return null;
        }
        Item item = new Item();
        item.setId(itemId);
        return item;
    }

    @Named("mapBooker")
    default User mapUser(Long bookerId) {
        if (bookerId == null) {
            return null;
        }
        User user = new User();
        user.setId(bookerId);
        return user;
    }

}
