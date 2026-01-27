package com.fitness.useftiness.service;

import com.fitness.useftiness.dto.RegisterRequest;
import com.fitness.useftiness.dto.UserResponse;
import com.fitness.useftiness.model.User;
import com.fitness.useftiness.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository repo;
    public UserResponse getUserProfile(String userId) {
        User user=repo.findById(userId).orElseThrow(()->new RuntimeException("user Not found"));
        UserResponse userResponse=new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setEmail(user.getEmail());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setUpdated(user.getUpdated());
        userResponse.setCreated(user.getCreated());
        return userResponse;

    }

    public UserResponse register(RegisterRequest request) {
        if(repo.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exist");
        }
        User user= User.builder().firstName(request.getFirstName()).lastName(request.getLastName()).password(request.getPassword()).email(request.getEmail()).build();
        User savedUser=repo.save(user);
        UserResponse userResponse=new UserResponse();
        userResponse.setId(savedUser.getId());
        userResponse.setEmail(savedUser.getEmail());
        userResponse.setFirstName(savedUser.getFirstName());
        userResponse.setLastName(savedUser.getLastName());
        userResponse.setUpdated(savedUser.getUpdated());
        userResponse.setCreated(savedUser.getCreated());

        
        return userResponse;
    }

    public Boolean existByUserId(String userId) {
        return repo.existsById(userId);
    }
}
