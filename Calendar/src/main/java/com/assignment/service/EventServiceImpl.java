package com.assignment.service;

import com.assignment.dao.Event;
import com.assignment.dto.TimeSlotDto;
import com.assignment.repository.EventRepository;
import org.springframework.stereotype.Service;

import java.time.*;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Map<Long, List<Event>> getEvents(String userIds) {
        Map<Long, List<Event>> eventsByUser = new HashMap<>();

        for (Long userId : convertToList(userIds)) {
            List<Event> userEvents = eventRepository.findAllEvents(userId);

            eventsByUser.put(userId, userEvents);
        }

        return eventsByUser;
    }

    @Override
    public Map<Long, List<Event>> getEventsByDate(String userIds, LocalDate date) {
        Map<Long, List<Event>> eventsByUserByDate = new HashMap<>();

        for (Long userId : convertToList(userIds)) {
            List<Event> userEvents = eventRepository.findEventsByDate(userId, convertLocalDateToDate(date));

            eventsByUserByDate.put(userId, userEvents);
        }

        return eventsByUserByDate;
    }

    @Override
    public List<Event> getConflictingEvents(Long userId, LocalDate date) {
        List<Event> userEvents = eventRepository.findEventsByDate(userId, convertLocalDateToDate(date));

        return findConflictingEvents(userEvents);
    }

    @Override
    public TimeSlotDto getFavorableTimeSlots(String userIds, long duration, LocalDate date) {
        Map<Long, List<Event>> eventMap = getEventsByDate(userIds, date);

        return findFavorableSlot(eventMap, convertToList(userIds), duration);
    }

    @Override
    public List<Event> createRecurringEvent(Event recurringEvent, int intervalDays, int totalOccurrences) {
        List<Event> recurringInstances = new ArrayList<>();

        for (int occurrenceIndex = 0; occurrenceIndex < totalOccurrences; occurrenceIndex++) {

            Event instance = generateRecurringInstance(recurringEvent, occurrenceIndex, intervalDays);
            recurringInstances.add(instance);
        }

        eventRepository.saveAll(recurringInstances);
        return recurringInstances;
    }

    @Override
    public Optional<Event> findEventById(Long eventId) {
        return eventRepository.findById(eventId);
    }

    private List<Event> findConflictingEvents(List<Event> events) {
        List<Event> conflictingEvents = new ArrayList<>();

        for (int i = 0; i < events.size(); i++) {
            Event event1 = events.get(i);

            for (int j = i + 1; j < events.size(); j++) {
                Event event2 = events.get(j);

                if (isConflict(event1, event2)) {
                    conflictingEvents.add(event1);
                    conflictingEvents.add(event2);
                }
            }
        }

        return conflictingEvents;
    }

    private boolean isConflict(Event event1, Event event2) {
        return event1.getEndTime().after(event2.getStartTime()) &&
                event1.getStartTime().before(event2.getEndTime());
    }

    private TimeSlotDto findFavorableSlot(Map<Long, List<Event>> eventMap, List<Long> userIds, long duration) {
        LocalDateTime commonStart = LocalDateTime.now();
        LocalDateTime commonEnd = LocalDateTime.now().plusYears(1);

        for (Long userId : userIds) {
            if (eventMap.containsKey(userId)) {
                LocalDateTime maxEnd = getMaxEndTime(eventMap.get(userId));
                LocalDateTime minStart = getMinStartTime(eventMap.get(userId));

                commonStart = commonStart.isBefore(minStart) ? minStart : commonStart;
                commonEnd = commonEnd.isAfter(maxEnd) ? maxEnd : commonEnd;
            }
        }

        for (LocalDateTime currentStart = commonStart; currentStart.isBefore(commonEnd); ) {
            LocalDateTime currentEnd = currentStart.plus(Duration.ofMinutes(duration));

            if (isTimeSlotAvailable(eventMap, userIds, currentStart, currentEnd)) {
                return new TimeSlotDto(currentStart, currentEnd);
            }

            currentStart = currentStart.plusMinutes(15);
        }

        return null;
    }

    private LocalDateTime getMaxEndTime(List<Event> events) {
        LocalDateTime maxEnd = LocalDateTime.now();
        for (Event event : events) {
            LocalDateTime endTime = convertToLocalDateTime(event.getEndTime());
            if (endTime.isAfter(maxEnd)) {
                maxEnd = endTime;
            }
        }
        return maxEnd;
    }

    private LocalDateTime getMinStartTime(List<Event> events) {
        LocalDateTime minStart = LocalDateTime.now();
        for (Event event : events) {
            LocalDateTime startTime = convertToLocalDateTime(event.getStartTime());
            if (startTime.isBefore(minStart)) {
                minStart = startTime;
            }
        }
        return minStart;
    }

    private boolean isTimeSlotAvailable(Map<Long, List<Event>> eventsMap, List<Long> selectedUserIds,
                                        LocalDateTime start, LocalDateTime end) {
        for (Long userId : selectedUserIds) {
            if (eventsMap.containsKey(userId) && isOverlap(eventsMap.get(userId), start, end)) {
                return false;
            }
        }
        return true;
    }

    private boolean isOverlap(List<Event> events, LocalDateTime start, LocalDateTime end) {
        for (Event event : events) {
            if (event.getEndTime().after(convertToDate(start)) && event.getStartTime().after(convertToDate(end))) {
                return true;
            }
        }
        return false;
    }

    private static LocalDateTime convertToLocalDateTime(Date date) {
        Instant instant = date.toInstant();
        return instant.atZone(ZoneId.of("UTC")).toLocalDateTime();
    }

    private static Date convertToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.of("UTC")).toInstant());
    }

    private static List<Long> convertToList(String userIds) {
        return Arrays.asList(userIds.split(","))
                .stream()
                .map(s -> Long.valueOf(s))
                .collect(Collectors.toList());
    }

    private static Date convertLocalDateToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.of("UTC")).toInstant());
    }

    private Event generateRecurringInstance(Event recurringEvent, int occurrenceIndex, int durationBetweenOccurrences) {
        Event instance = new Event();
        instance.setTitle(recurringEvent.getTitle());
        instance.setDescription(recurringEvent.getDescription());
        instance.setAttendees(recurringEvent.getAttendees());
        instance.setCreator(recurringEvent.getCreator());

        LocalDateTime originalStartTime = convertToLocalDateTime(recurringEvent.getStartTime());
        LocalDateTime originalEndTime = convertToLocalDateTime(recurringEvent.getEndTime());

        LocalDateTime currentStartTime = originalStartTime.plusDays(occurrenceIndex * durationBetweenOccurrences);
        LocalDateTime currentEndTime = originalEndTime.plusDays(occurrenceIndex * durationBetweenOccurrences);

        instance.setStartTime(convertToDate(currentStartTime));
        instance.setEndTime(convertToDate(currentEndTime));

        return instance;
    }
}
