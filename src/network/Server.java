// src/network/Server.java
package network;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private final int port;
    // private final Juego juego; // Temporarily disconnected
    private final List<ClientHandler> clients;

    public Server(int port) {
        this.port = port;
        // this.juego = new Juego(); // Temporarily disconnected
        this.clients = new ArrayList<>();
    }

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server started on port " + port);

        while (true) {
            Socket socket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(socket, this);
            clients.add(clientHandler);
            new Thread(clientHandler).start();
        }
    }

    public void broadcast(String message) {
        for (ClientHandler client : clients) {
            client.sendMessage(message);
        }
    }

    public static void main(String[] args) throws IOException {
        Server server = new Server(12345);
        server.start();
    }
}

class ClientHandler implements Runnable {
    private final Socket socket;
    // private final Juego juego; // Temporarily disconnected
    private final Server server;
    private PrintWriter out;
    private BufferedReader in;
    // private Jugador jugador; // Temporarily disconnected

    public ClientHandler(Socket socket, Server server) {
        this.socket = socket;
        // this.juego = juego; // Temporarily disconnected
        this.server = server;
    }

    @Override
    public void run() {
        try {
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            out.println("Enter your name:");
            String name = in.readLine();
            // jugador = new Jugador(name); // Temporarily disconnected
            // juego.getJugadores().add(jugador); // Temporarily disconnected

            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                // Handle client messages
                server.broadcast(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void sendMessage(String message) {
        out.println(message);
    }
}