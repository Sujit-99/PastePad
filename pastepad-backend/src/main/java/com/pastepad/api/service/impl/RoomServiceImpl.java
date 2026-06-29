package com.pastepad.api.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pastepad.api.dto.RoomCreateRequest;
import com.pastepad.api.dto.RoomResponseDto;
import com.pastepad.api.dto.WorkspaceResponseDto;
import com.pastepad.api.dto.WorkspaceUpdateDto;
import com.pastepad.api.entity.Room;
import com.pastepad.api.entity.User;
import com.pastepad.api.entity.WorkSpace;
import com.pastepad.api.repository.RoomRepository;
import com.pastepad.api.service.RoomService;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@Service
public class RoomServiceImpl implements RoomService {

    private static final Logger log = LoggerFactory.getLogger(RoomServiceImpl.class);
    private final RoomRepository roomRepository;
    private final Random random = new Random();

    private static final String[] ADJECTIVES = {"fast", "blue", "silent", "clever", "cosmic", "rapid"};
    private static final String[] NOUNS = {"tiger", "falcon", "ninja", "robot", "wizard", "matrix"};

    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    @Transactional
    public RoomResponseDto createRoom(RoomCreateRequest request) {
        String generatedCode = generateReadableToken();
        
        if (roomRepository.existsByCode(generatedCode)) {
            generatedCode = generateReadableToken() + "-backup";
        }

        Room room = new Room();
        room.setCode(generatedCode);

        
        WorkSpace workSpace = new WorkSpace();
        workSpace.setContent(""); 
        room.setWorkspace(workSpace); 

        
        User hostUser = new User(request.username());
        room.addUser(hostUser); 

        Room savedRoom = roomRepository.save(room);
        log.info("New Room Successfully Created: [Code: {}, Host: {}]", savedRoom.getCode(), request.username());

        return new RoomResponseDto(savedRoom.getRid(), savedRoom.getCode());
    }

    @Override
    @Transactional(readOnly = true)
    public WorkspaceResponseDto getWorkspaceByRoomCode(String code) {
        Room room = roomRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Room not found with code: " + code));

        WorkSpace workSpace = room.getWorkSpace();
        String currentTextContent = (workSpace != null) ? workSpace.getContent() : "";

        return new WorkspaceResponseDto(
                room.getCode(),
                currentTextContent,
                "Successfully fetched active workspace payload."
        );
    }

    @Override
    @Transactional
    public void updateWorkspaceContent(String roomCode, WorkspaceUpdateDto updateDto) {
        Room room = roomRepository.findByCode(roomCode)
                .orElseThrow(() -> new RuntimeException("Room code '" + roomCode + "' does not exist."));

        WorkSpace workSpace = room.getWorkSpace();
        if (workSpace == null) {
            workSpace = new WorkSpace();
            room.setWorkspace(workSpace);
        }

        String freshCodePayload = (updateDto.content() != null) ? updateDto.content() : "";
        workSpace.setContent(freshCodePayload);
    }

    private String generateReadableToken() {
        String adjective = ADJECTIVES[random.nextInt(ADJECTIVES.length)];
        String noun = NOUNS[random.nextInt(NOUNS.length)];
        int numericSuffix = random.nextInt(90) + 10;
        return adjective + "-" + noun + "-" + numericSuffix;
    }
}