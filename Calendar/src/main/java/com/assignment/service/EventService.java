package com.assignment.service;

import com.assignment.dao.Event;
import com.assignment.dto.TimeSlotDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface EventService {

    Event addEvent(Event event);

    Map<Long, List<Event>> getEvents(String userIds);

    Map<Long, List<Event>> getEventsByDate(String userIds, LocalDate date);

    List<Event> getConflictingEvents(Long userId, LocalDate date);

    TimeSlotDto getFavorableTimeSlots(String userIds, long duration, LocalDate date);

    List<Event> createRecurringEvent(Event recurringEvent, int intervalDays, int totalOccurrences);

    Optional<Event> findEventById(Long eventId);
}
