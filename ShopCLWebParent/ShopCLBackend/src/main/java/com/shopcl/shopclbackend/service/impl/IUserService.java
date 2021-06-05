package com.shopcl.shopclbackend.service.impl;

import com.shopcl.common.entity.Role;
import com.shopcl.common.entity.User;
import com.shopcl.shopclbackend.error.UserNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IUserService {

    public List<User> listAll();
    public List<Role> listRoles();
    public User save(User user);
    public boolean isEmailUnique(Long id, String email);
    public User get(Long id) throws UserNotFoundException;
    public void delete(Long id) throws UserNotFoundException;
    public void updateUserEnabledStatus(Long id, boolean enabled);
    public Page<User> listByPage(int pageNum, String sortField, String sortDir, String keyword);
    public User getByEmail(String email);
    public User updateAccount(User userInForm);
}
