package tr.com.obss.googlecalendarservice.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import tr.com.obss.googlecalendarservice.dto.CalendarEventDTO;
import tr.com.obss.googlecalendarservice.dto.GoogleAccountDTO;
import tr.com.obss.googlecalendarservice.entity.GoogleAccount;
import com.google.api.services.calendar.model.Event;
import java.util.List;

@org.mapstruct.Mapper(componentModel = "spring", injectionStrategy = InjectionStrategy.CONSTRUCTOR)
@DecoratedWith(GoogleMapperDecorator.class)
public interface GoogleMapper {

    GoogleAccountDTO toGoogleAccountDTO(GoogleAccount googleAccount);

    GoogleAccount toEntity(GoogleAccountDTO googleAccountDTO);

    @Mapping(target = "creator",ignore = true)
    @Mapping(target = "start",ignore = true)
    @Mapping(target = "end",ignore = true)
    Event toEvent(CalendarEventDTO calendarEvent);

    List<GoogleAccountDTO> toDTOList(List<GoogleAccount> accounts);

    void updateGoogleAccount(GoogleAccountDTO googleAccountDTO,@MappingTarget GoogleAccount entity);
}
