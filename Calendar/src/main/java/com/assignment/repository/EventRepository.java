package com.assignment.repository;

import com.assignment.dao.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e " +
            "WHERE (e.creator.userId = :userId OR CONCAT(',', e.attendees, ',') LIKE CONCAT('%,', :userId, ',%'))")
    List<Event> findAllEvents(@Param("userId") Long userId);

    @Query("SELECT e FROM Event e " +
            "WHERE (e.creator.userId = :userId OR CONCAT(',', e.attendees, ',') LIKE CONCAT('%,', :userId, ',%')) " +
            "AND (:date BETWEEN DATE(e.startTime) AND DATE(e.endTime))")
    List<Event> findEventsByDate(@Param("userId") Long userId, @Param("date") Date date);
}
