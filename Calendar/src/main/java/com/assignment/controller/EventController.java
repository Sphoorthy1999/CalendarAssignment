package com.assignment.controller;

import com.assignment.dao.Event;
import com.assignment.dto.TimeSlotDto;
import com.assignment.service.EventService;
import io.swagger.annotations.ApiOperation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @ApiOperation(value = "Add a new event - Busy event/event with others")
    @PostMapping("/add-event")
    public Event addEvent(@RequestBody Event event) {
        return eventService.addEvent(event);
    }

    @ApiOperation(value = "Fetch a event by id")
    @PostMapping("/fetch-event/{eventId}")
    public Optional<Event> getUser(Long eventId) {
        return eventService.findEventById(eventId);
    }

    @ApiOperation(value = "Fetch all events scheduled for a single user/multiple users")
    @GetMapping("/fetch-events/{userIds}")
    public Map<Long, List<Event>> getEvents(@PathVariable String userIds) {
        return eventService.getEvents(userIds);
    }

    @ApiOperation(value = "Fetch all events scheduled for a single user/multiple users on a particular date")
    @GetMapping("/fetch-events-by-date/{userIds}")
    public Map<Long, List<Event>> getEventsByDate(
            @PathVariable String userIds, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return eventService.getEventsByDate(userIds, date);
    }

    @ApiOperation(value = "Fetch all conflicting events for a user on a particular date")
    @GetMapping("/fetch-conflicting-events/{userId}")
    public List<Event> getConflictingEvents(
            @PathVariable Long userId, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return eventService.getConflictingEvents(userId, date);
    }

    @ApiOperation(value = "Get favourable time slots for selected users")
    @GetMapping("/time-slots/{userIds}/{duration}")
    public TimeSlotDto getFavorableTimeSlots(@PathVariable String userIds,
                                             @PathVariable long duration,
                                             @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return eventService.getFavorableTimeSlots(userIds, duration, date);
    }

    @ApiOperation(value = "Add recurring events")
    @PostMapping("/recurring")
    public List<Event> createRecurringEvent(@RequestBody Event recurringEvent,
                                            @RequestParam int intervalDays,
                                            @RequestParam int totalOccurrences) {
        return eventService.createRecurringEvent(recurringEvent, intervalDays, totalOccurrences);
    }

}
