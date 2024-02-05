package com.example.chatapplicationserverfx;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    // This object listens for incoming connections
    private ServerSocket serverSocket;
    // The socket object to communicate with the client
    private Socket socket;
    // To read in the data
    private BufferedReader bufferedReader;
    // TO write data
    private BufferedWriter bufferedWriter;

    public Server(ServerSocket serverSocket){
        try {
                this.serverSocket = serverSocket;
                this.socket = serverSocket.accept();
                this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            } catch(IOException e) {
                e.printStackTrace();
                System.out.println("ERROR CREATING THE SERVER.");

            }


    }


    // Method to close all the resources that we are using - prevents null pointer exceptions
    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try{
            if(bufferedReader != null){
                // closes the underlying streams as well
                bufferedReader.close();
            }
            if(bufferedWriter != null){
                bufferedWriter.close();
            }
            if(socket != null){
                socket.close();
            }

        } catch(IOException e){
            e.printStackTrace();
        }

    }


}
