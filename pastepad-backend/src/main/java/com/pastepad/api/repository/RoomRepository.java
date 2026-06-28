package com.pastepad.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.pastepad.api.entity.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {

    boolean existsByCode(String code);
}
