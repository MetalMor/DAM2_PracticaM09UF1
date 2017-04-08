package server;

import java.io.EOFException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import lib.Message;
import lib.User;

public class Server {
    private static Server _instance;
    
    private ServerSocket serverSocket;
    private final List<User> _users = new ArrayList<>();

    private Server() {}
    
    public static Server getInstance() {
        if (_instance == null) _instance = new Server();
        return _instance;
    }
    
    public void run() {
        try {
            serverSocket = new ServerSocket(1123, 100);
        } catch (IOException e) {
            System.err.println("Invalid port number");
        }
        while (true) processConnection(new User(waitForConnection()));
    }

    private Socket waitForConnection() {
        System.out.println("Server is ready to accept conenctions");
        try {
            Socket socket = serverSocket.accept(); // code will stop here until a
                                                // connection occurs
            System.out.println("Conenction established with the client");
            return socket;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void processConnection(User newUser) {
        new Thread() {
            @Override
            public void run() {
                String message;
                boolean ok;
                do {
                    try {
                        message = newUser.askName();
                        ok = checkValidName(message);
                        if (!ok) System.out.println("Name not valid.");
                    } catch (EOFException e) {
                        ok = false;
                        System.out.println("Lost connection with user.");
                        break;
                    }
                } while (!ok);
                if (ok) {
                    sendData("New user: " + newUser.getName());
                    _users.add(newUser);
                    Message m = new Message(newUser.getName());
                    m.setContent(("Welcome, " + newUser.getName()).getBytes(StandardCharsets.UTF_8));
                    newUser.sendData(m);
                    while (true) {
                        try {
                            sendData(newUser.waitForMessage());
                        } catch(EOFException e) {
                            System.out.println("Lost connection with user " + newUser.getName() + ".");
                            break;
                        }
                    }
                }
                newUser.closeConnection();
            }
        }.start();
    }
    
    public boolean checkValidName(String name) {
        if(name == null || name.isEmpty()) return false;
        for(User user : _users) 
            if(user.getName() != null && user.getName().equals(name)) 
                return false;
        return true;
    }

    public void closeConnection() {
        for (User user : _users) user.closeConnection();
    }

    public void sendData(String s) {
        sendData((Object) s);
    }
    
    public void sendData(Message m) {
        sendData((Object) m);
    }
    
    public void sendData(Object o) {
        List<User> disconnectedUsers = new ArrayList<>();
        for (User user : _users) 
            if(user.isConnected()) user.sendData(o);
            else disconnectedUsers.add(user);
        _users.removeAll(disconnectedUsers);
    }
}