package com.example.chatapplicationserverfx;

import javafx.scene.layout.VBox;

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
    public void sendMessageToClient(String messageToSend){
        try {
            bufferedWriter.write(messageToSend);
            bufferedWriter.newLine(); // need to send overr a new line so it knows the message has ended
            bufferedWriter.flush(); // manually flushing the buffere because it won't be full


        } catch(IOException e){
            e.printStackTrace();
            System.out.println("Error sending message to client.");
            closeEverything(socket, bufferedReader, bufferedWriter);
        }

    }
    // Need to run this on a new thread so the program is not constantly waiting to receive messages, it can also send them
    public void receiveMessageFromClient(VBox vBox){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(socket.isConnected()) {
                    String messageFromClient = null;
                    try {
                        messageFromClient = bufferedReader.readLine();
                        HelloController.addLabel(messageFromClient, vBox);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("Error receiving message from the client.");
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;
                    }

                }


            }
        }).start();

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
