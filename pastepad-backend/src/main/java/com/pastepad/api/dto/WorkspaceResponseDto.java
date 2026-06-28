package com.pastepad.api.dto;

public record WorkspaceResponseDto(
	    String roomCode,
	    String content,
	    String statusMessage
	) {}
