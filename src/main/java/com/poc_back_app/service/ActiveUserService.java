package com.poc_back_app.service;

import org.springframework.stereotype.Service;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ActiveUserService {

    private final Set<String> activeUsers = ConcurrentHashMap.newKeySet();

    public void addUser(String username) {
        activeUsers.add(username);
        System.out.println("Utilisateur déconnecté : " + username);
        System.out.println("Utilisateurs actifs : " + activeUsers);
        System.out.println("Utilisateur connecté : " + username);
    }

    public void removeUser(String username) {
        activeUsers.remove(username);
        System.out.println("Utilisateur déconnecté : " + username);
    }

    public boolean isUserActive(String username) {
        return activeUsers.contains(username);
    }
}
