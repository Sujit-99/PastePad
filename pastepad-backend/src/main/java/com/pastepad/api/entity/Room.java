package com.pastepad.api.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "rooms")
public class Room {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int rid;
	
	@Column(name = "room_code", unique = true, nullable = false)
	private String code;
	
	@OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<User> users = new ArrayList<>();
	
	@OneToOne(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JsonManagedReference
	private WorkSpace workSpace;
	
	public Room() {}
	
	public int getRid() {
		return rid;
	}
	
	public void setRid(int rid) {
		this.rid = rid;
	}
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	public WorkSpace getWorkSpace() {
	    return this.workSpace;
	}
	 
	public void setWorkspace(WorkSpace workSpace) { 
	    this.workSpace = workSpace;
	    if (workSpace != null && workSpace.getRoom() != this) {
	          workSpace.setRoom(this);
	    }
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}
	
	// Helpers
	public void addUser(User user) {
        users.add(user);
        user.setRoom(this);
    }

    public void removeUser(User user) {
        users.remove(user);
        user.setRoom(null);
    }
}
