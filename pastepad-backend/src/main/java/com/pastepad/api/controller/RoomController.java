package com.pastepad.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pastepad.api.dto.RoomResponseDto;
import com.pastepad.api.service.RoomService;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*") 
public class RoomController {

    private final RoomService roomService;

    // Constructor Injection
    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/create")
    public ResponseEntity<RoomResponseDto> handleCreateRoom() {
        RoomResponseDto response = roomService.createRoom();
        
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
