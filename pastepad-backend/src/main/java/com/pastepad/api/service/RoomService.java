package com.pastepad.api.service;

import com.pastepad.api.dto.RoomCreateRequest;
import com.pastepad.api.dto.RoomResponseDto;
import com.pastepad.api.dto.WorkspaceResponseDto;
import com.pastepad.api.dto.WorkspaceUpdateDto;

public interface RoomService {
    RoomResponseDto createRoom(RoomCreateRequest request);
    WorkspaceResponseDto getWorkspaceByRoomCode(String code);
    void updateWorkspaceContent(String roomCode, WorkspaceUpdateDto updateDto);
}
