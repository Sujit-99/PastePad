package com.pastepad.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pastepad.api.dto.RoomCreateRequest;
import com.pastepad.api.dto.RoomResponseDto;
import com.pastepad.api.dto.WorkspaceResponseDto;
import com.pastepad.api.dto.WorkspaceUpdateDto;
import com.pastepad.api.service.RoomService;

@RestController
@RequestMapping("/api/rooms")
@CrossOrigin(origins = "*") 
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    @PostMapping("/create")
    public ResponseEntity<RoomResponseDto> handleCreateRoom(@RequestBody RoomCreateRequest request) {
        return new ResponseEntity<>(roomService.createRoom(request), HttpStatus.CREATED);
    }

    @GetMapping("/{roomCode}")
    public ResponseEntity<WorkspaceResponseDto> handleJoinRoom(@PathVariable String roomCode) {
        return ResponseEntity.ok(roomService.getWorkspaceByRoomCode(roomCode));
    }

    @PutMapping("/{roomCode}/workspace")
    public ResponseEntity<Void> handleUpdateWorkspace(
            @PathVariable String roomCode,
            @RequestBody WorkspaceUpdateDto updateDto) {
        roomService.updateWorkspaceContent(roomCode, updateDto);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}