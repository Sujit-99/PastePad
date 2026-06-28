package com.pastepad.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pastepad.api.entity.User;


public interface UserRepositor extends JpaRepository<User, Integer> {

}
