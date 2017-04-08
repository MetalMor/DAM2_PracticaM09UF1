package client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import lib.Crypter;
import lib.Message;

public class Client {
    private String _name;
    private Crypter _crypter;
    private Socket _clientSocket;
    private ObjectOutputStream _out;
    private ObjectInputStream _in;
    private final String _address = "127.0.0.1";
    private final int _port = 1123;

    public void run() {
        connect();
        initStreams();
        processConnection();
        closeConnection();
    }

    public void connect() {
        System.out.println("Trying to connect to the server");
        try {
            _clientSocket = new Socket(_address, _port);
        } catch (UnknownHostException e) {
            System.err.println("Server unavailable");
            System.exit(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initStreams() {
        try {
            _out = new ObjectOutputStream(_clientSocket.getOutputStream());
            _out.flush();
            _in = new ObjectInputStream(_clientSocket.getInputStream());
            System.out.println("Client established I/O Stream");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void processConnection() {
        //sendData("Connection established with the client");
        Object input;
        Message m;
        boolean willShow, isName;
        
        new Thread() {
            Scanner sc = new Scanner(System.in);

            @Override
            public void run() {
                Message outputMessage = new Message(_name);
                String userInput;
                do {
                    userInput = sc.nextLine();
                    outputMessage.setContent(userInput.getBytes(StandardCharsets.UTF_8));
                    sendData(_name == null ? userInput : outputMessage);
                } while (!userInput.equals("QUIT"));
                closeConnection();
            }
        }.start();
        while (true) {
            try {
                input = _in.readObject();
                if (input instanceof String) {
                    System.out.println((String) input);
                } else if (input instanceof Message) {
                    m = (Message) input;
                    willShow = _name == null || !m.getSender().equals(_name);
                    isName = _name == null && m.getSender() != null;
                    if (willShow) System.out.println(m.getStringContent());
                    if (isName) _name = m.getSender();
                }
            } catch (ClassNotFoundException e) {
                System.err.println("Object of an unknown type was recieved");
            } catch (EOFException e) {
                System.err.println("Connection error, closing.");
                System.exit(1);
            } catch (IOException e) {
                System.err.println("Connection error, closing.");
                System.exit(1);
            }
        }
    }
    
    public void sendData(Object o) {
        try {
            _out.writeObject(o);
            _out.flush();
        } catch (IOException e) {
            System.err.println("Error writting the message");
        }
    }

    public void sendData(Message m) {
        sendData((Object) m);
    }
    
    public void sendData(String s) {
        sendData((Object) s);
    }

    public void closeConnection() {
        try {
            _out.close();
            _in.close();
            _clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}