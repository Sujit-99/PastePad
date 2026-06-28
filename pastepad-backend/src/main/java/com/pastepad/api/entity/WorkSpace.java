package com.pastepad.api.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "workspaces")
public class WorkSpace {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int wid;
	
	@Lob
    @Column(columnDefinition = "TEXT")
	private String content;
	
	@OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
	@JsonBackReference
	private Room room;
	
	public WorkSpace() {}

	public int getWid() {
		return wid;
	}

	public void setWid(int wid) {
		this.wid = wid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}
	
	
}
