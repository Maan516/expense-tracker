package org.example.expensetracker.service;

import org.example.expensetracker.entity.Role;
import org.example.expensetracker.entity.User;
import org.example.expensetracker.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class AdminServiceImp implements AdminService {

    private final UserRepository userRepository;

    public AdminServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public long getTotalUsers() {
        // Optional improvement: exclude admins from count
        return userRepository.findByRole(Role.USER).size();
    }

    @Override
    public List<User> getAllNormalUsers() {
        return userRepository.findByRole(Role.USER);
    }
}
