package client;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

public class Client {
    private String _name;
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
        String inputMessage = "";
        new Thread() {
            Scanner sc = new Scanner(System.in);

            @Override
            public void run() {
                String outputMessage = "";
                do {
                    outputMessage = sc.nextLine();
                    sendData(outputMessage);
                } while (!outputMessage.equals("QUIT"));
            }
        }.start();
        while (true) {

            try {
                inputMessage = (String) _in.readObject();
                System.out.println(inputMessage);
            } catch (ClassNotFoundException e) {
                System.err.println("Object of an unknown type was recieved");
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    public void sendData(String s) {
        try {
            _out.writeObject(s);
            _out.flush();
        } catch (IOException e) {
            System.err.println("Error writting the message");
        }
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