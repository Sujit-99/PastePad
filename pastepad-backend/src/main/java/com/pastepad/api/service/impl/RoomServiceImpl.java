package com.pastepad.api.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pastepad.api.dto.RoomResponseDto;
import com.pastepad.api.dto.WorkspaceResponseDto;
import com.pastepad.api.dto.WorkspaceUpdateDto;
import com.pastepad.api.entity.Room;
import com.pastepad.api.entity.WorkSpace;
import com.pastepad.api.repository.RoomRepository;
import com.pastepad.api.service.RoomService;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class RoomServiceImpl implements RoomService {

    //Logger
    private static final Logger log = LoggerFactory.getLogger(RoomServiceImpl.class);

    private final RoomRepository roomRepository;
    private final Random random = new Random();

    //Word Bank Pools
    private static final String[] ADJECTIVES = {"fast", "blue", "silent", "clever", "cosmic", "rapid", "bright", "hidden"};
    private static final String[] NOUNS = {"tiger", "falcon", "ninja", "robot", "wizard", "monkey", "matrix", "hacker"};

    //Constructor Injection
    public RoomServiceImpl(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    @Transactional
    public RoomResponseDto createRoom() {
        String generatedCode;
        boolean codeExists;
        int safetyLoopCounter = 0;

        // generate codes until uniqueness check passes
        do {
            generatedCode = generateReadableToken();
            codeExists = roomRepository.existsByCode(generatedCode);
            safetyLoopCounter++;
            
            if (safetyLoopCounter > 50) { // exit point to break dead loops
                throw new RuntimeException("Server timed out attempting to generate a unique room code.");
            }
        } while (codeExists);

        // New Room 
        Room room = new Room();
        room.setCode(generatedCode);

        //Instantiate an empty WorkSpace
        WorkSpace workSpace = new WorkSpace();
        workSpace.setContent(""); // Initialize with blank code space

        
        room.setWorkspace(workSpace);

        
        Room savedRoom = roomRepository.save(room);

        // 2. Logging in local terminal console
        log.info("New Room Successfully Created: [Code: {}, ID: {}]", savedRoom.getCode(), savedRoom.getRid());

        return new RoomResponseDto(
                savedRoom.getRid(),
                savedRoom.getCode(),
                "Workspace generated successfully."
        );
    }

    /**
     * Algorithm for Adjective-Noun-Number structures
     */
    private String generateReadableToken() {
        String adjective = ADJECTIVES[random.nextInt(ADJECTIVES.length)];
        String noun = NOUNS[random.nextInt(NOUNS.length)];
        int numericSuffix = random.nextInt(90) + 10; //Range between 10 and 99
        
        return adjective + "-" + noun + "-" + numericSuffix;
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
                "Successfully fetched active code sync workspace payload."
        );
    }
    
    
    @Override
    @org.springframework.transaction.annotation.Transactional // Starts an active write transaction block
    public void updateWorkspaceContent(String roomCode, WorkspaceUpdateDto updateDto) {
        
        // Step A: Find the parent Room via the existing findByCode repository abstraction
        // 🚨 STAGE FOR CUSTOM EXCEPTION: RoomNotFoundException
        Room room = roomRepository.findByCode(roomCode)
                .orElseThrow(() -> new RuntimeException("Cannot update workspace. Room code '" + roomCode + "' does not exist."));

        // Step B: Pull down the child object instance cleanly
        WorkSpace workSpace = room.getWorkSpace();
        
        // Core Structural Fallback Safeguard: Ensure a workspace row is present before mutating content fields
        if (workSpace == null) {
            workSpace = new WorkSpace();
            room.setWorkspace(workSpace); // Restores links if an empty room configuration ever presents itself
        }

        // Step C: Modify the text object state
        // Overwrites the string contents inside your database row memory block
        String freshCodePayload = (updateDto.content() != null) ? updateDto.content() : "";
        workSpace.setContent(freshCodePayload);

        // 💡 REMINDER: No manual repository.save() or workspaceRepository.save() is written here!
        // Because this method is marked with @Transactional, Spring closes the transaction boundary
        // when this method exits. Hibernate's engine instantly executes automated "dirty checking", 
        // detects that the WorkSpace text has mutated, and flushes an optimized SQL 'UPDATE' statement 
        // to your laptop storage drive automatically.
    }
}