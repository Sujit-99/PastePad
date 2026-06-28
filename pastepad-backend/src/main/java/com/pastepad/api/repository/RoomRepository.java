package com.pastepad.api.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.pastepad.api.entity.Room;


public interface RoomRepository extends JpaRepository<Room, Integer> {

    boolean existsByCode(String code);
    
    Optional<Room> findByCode(String code);
}
