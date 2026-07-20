package com.rehancode.ecommercebackend.Security;


import com.rehancode.ecommercebackend.Model.UserModel;
import com.rehancode.ecommercebackend.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private UserRepository userRepository;
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserModel userModel=userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("Username not found"));
        return new UserPrinciple(userModel);
    }
}
