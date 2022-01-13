package com.example.server;

import javafx.scene.layout.VBox;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {

    private ServerSocket serverSocket;
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;

    public Server(ServerSocket serverSocket) {
        try {
            this.serverSocket = serverSocket;
            this.socket = serverSocket.accept();
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch (IOException e){
            System.out.println("Blad w tworzeniu");
            e.printStackTrace();
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    public  void  sendMessToClinet(String messToClient){
        try {
            bufferedWriter.write(messToClient);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }catch (IOException e){
            e.printStackTrace();
            System.out.println("blad w przesylaniu do klienta");
            closeEverything(socket,bufferedReader,bufferedWriter);
        }
    }

    public void receiveMessFromClient(VBox vBox){
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (socket.isConnected()){
                    try {
                        String messFromClient =  bufferedReader.readLine();
                        Controller.addLabel(messFromClient, vBox);
                    }catch (IOException e){
                        e.printStackTrace();
                        System.out.println("blad w odebraniu");
                        closeEverything(socket, bufferedReader, bufferedWriter);
                        break;
                    }
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter){
        try {
            if (bufferedReader != null){
                bufferedReader.close();
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
            }
            if (socket != null){
                socket.close();
            }

        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
