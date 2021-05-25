package com.shopcl.shopclbackend.service;

import com.shopcl.common.entity.User;
import com.shopcl.shopclbackend.repository.UserRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> listAll() {
        List<User> userList = new ArrayList<>();
        userRepository.findAll().forEach(userList::add);
        return userList;
    }
}
