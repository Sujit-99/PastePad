package com.pastepad.api.service.impl;

import org.springframework.stereotype.Service;

import com.pastepad.api.dto.RoomResponseDto;
import com.pastepad.api.entity.Room;
import com.pastepad.api.entity.WorkSpace;
import com.pastepad.api.repository.RoomRepository;
import com.pastepad.api.service.RoomService;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.transaction.Transactional;

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

        // new Room 
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
        int numericSuffix = random.nextInt(90) + 10; // Guarantees a two-digit range between 10 and 99
        
        return adjective + "-" + noun + "-" + numericSuffix;
    }
}