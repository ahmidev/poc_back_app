package com.poc_back_app.listener;

import com.poc_back_app.service.ActiveUserService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketEventListener {

    private final ActiveUserService activeUserService;
    private final Map<String, String> activeSessions = new ConcurrentHashMap<>();

    public WebSocketEventListener(ActiveUserService activeUserService) {
        this.activeUserService = activeUserService;
    }

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        System.out.println("Connexion détectée via WebSocket : " + event);

        // Récupérer les en-têtes du message de connexion
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        // Récupérer le header "username"
        List<String> usernameList = headerAccessor.getNativeHeader("username");

        if (usernameList != null && !usernameList.isEmpty()) {
            String username = usernameList.get(0);
            String sessionId = headerAccessor.getSessionId();
            activeSessions.put(sessionId, username);
            activeUserService.addUser(username);
        } else {
            System.out.println("Aucun nom d'utilisateur fourni lors de la connexion.");
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {

        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId(); // Récupérer l'ID de session

        // Récupérer l'utilisateur correspondant à cet ID de session
        String username = activeSessions.remove(sessionId);
        if (username != null) {
            activeUserService.removeUser(username);
            System.out.println("Utilisateur déconnecté : " + username);
        } else {
            System.out.println("Aucun utilisateur trouvé pour la session : " + sessionId);
        }
    }
}
