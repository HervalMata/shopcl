package com.shopcl.shopclbackend.security;

import com.shopcl.common.entity.User;
import com.shopcl.shopclbackend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class ShopclUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.getUserByEmail(email);
        if (user != null) {
            return new ShopclUserDetails(user);
        }
        throw new UsernameNotFoundException("Não foi possível encontrar um usuário com este email: " + email);
    }
}
