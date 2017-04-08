/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package lib;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import util.Functions;

/**
 *
 * @author m0r
 */
public class User {
    
    private String _name;
    private Socket _connection;
    private ObjectInputStream _in;
    private ObjectOutputStream _out;
    private boolean _connected = false;
    
    public User(Socket connection) {
        if(_connected = initConnection(connection)) _connection = connection;
    }
    
    private boolean initConnection(Socket connection) {
        try {
            _in = new ObjectInputStream(connection.getInputStream());
            _out = new ObjectOutputStream(connection.getOutputStream());
            return true;
        } catch (IOException ex) {
            System.err.println("Could not open connection with client.");
            return false;
        }
    }
    
    public String askName() throws EOFException {
        String inputName = "";
        sendData("Write your user name: ");
        boolean ok;
        do {
            try {
                inputName = (String) waitForMessage();
            } catch(EOFException e) {
                throw new EOFException();
            }
            ok = !inputName.isEmpty();
            if (!ok) sendData("Wrong input, try again.");
        } while(!ok);
        _name = inputName;
        return _name;
    }
    
    public Object waitForMessage() throws EOFException {
        try {
            return _in.readObject();
        } catch (ClassNotFoundException e) {
            System.err.println("Object of an unknown type was recieved");
        } catch (EOFException e) {
            throw new EOFException();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public void sendData(Message m) {
        sendData((Object) m);
    }
    
    public void sendData(String s) {
        sendData((Object) s);
    }
    
    public void sendData(Object o) {
        try {
            _out.writeObject(o);
            _out.flush();
        } catch (IOException e) {
            System.err.println("Error writting the message");
        }
    }
    
    public void closeConnection() {
        try {
            _in.close();
            _out.close();
            _connection.close();
            _connected = false;
        } catch (IOException ex) {
            System.out.println("Error closing connection with user.");
        }
    }
    
    public Socket getConnection() {
        return _connection;
    }
    
    public String getName() {
        return _name;
    }
    
    public boolean isConnected() {
        return _connected;
    }
}
