package org.example.expensetracker.service;

import org.example.expensetracker.entity.User;
import java.util.List;

public interface AdminService {

    long getTotalUsers();

    List<User> getAllNormalUsers();
}
