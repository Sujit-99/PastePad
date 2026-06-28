package com.pastepad.api.service;

import com.pastepad.api.dto.RoomResponseDto;
import com.pastepad.api.dto.WorkspaceResponseDto;

public interface RoomService {
    RoomResponseDto createRoom();
    WorkspaceResponseDto getWorkspaceByRoomCode(String code);
}
