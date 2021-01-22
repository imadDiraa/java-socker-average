import java.net.*;
import java.io.*;
import java.util.Arrays;

public class ServerService {

    ServerSocket server;
    Socket socket = null;
    int port;
    DataInputStream dataInputStream = null;
    DataOutputStream dataOutputStream = null;
    public ServerService(int port) throws IOException {
        this.port = port;
        this.server = new ServerSocket(this.port);
        System.out.println("server listening in port: " + port);
    }

    public void estabilishConnection() throws IOException {
        this.socket = this.server.accept();
        System.out.println("client connected");
        this.dataInputStream = new DataInputStream(this.socket.getInputStream());
        this.dataOutputStream = new DataOutputStream(this.socket.getOutputStream());
    }

    public void closeConnection() throws IOException {
        this.sendMessage("/bye");
        this.socket.close();
    }

    public String getMessage() throws IOException {
        return this.dataInputStream.readUTF();
    }

    public void sendMessage(String message) throws IOException {
        this.dataOutputStream.writeUTF(message);
    }

    public void sendMessageWithoutResponse(String message) throws IOException {
        this.dataOutputStream.writeUTF("/WithoutResponse");
        this.dataOutputStream.writeUTF(message);
    }

    public static void main(String... args) throws IOException {


        ServerService serverService = new ServerService(55555);
        serverService.estabilishConnection();

        String clientMessage;
        int clientIntMessage;
        int[] numbers;

        while (true) {

            // show the first menu to the client
            do {
                // send menu
                serverService.sendMessage("0. exit\n1. calculate average\nchoice :__");
                // get response
                clientMessage = serverService.getMessage();
                // if the response is 0 the connection will be close
                if (clientMessage.equals("0")) {
                    serverService.closeConnection();
                    System.exit(1);
                }
                // if the response is not 0 or 1 it will repeat otherwise the program will progress with the second part
                else if (!clientMessage.equals("1")) {
                    continue;
                }
                break;
            } while (true);

            // show the second message
            do {
                // send menu
                serverService.sendMessage("how much number you want to calculate?\nchoice :__");
                // get response
                clientMessage = serverService.getMessage();
                // check number format and parse it
                try {
                    clientIntMessage = Integer.parseInt(clientMessage);
                } catch (Exception e) {
                    continue;
                }
                break;
            } while (true);

            if (clientIntMessage > 0) {
                numbers = new int[clientIntMessage];

                for (int i = 0; i < clientIntMessage; i++) {
                    do {
                        serverService.sendMessage("number N." + (i + 1) + " : __");
                        clientMessage = serverService.getMessage();
                        try {
                            numbers[i] = Integer.parseInt(clientMessage);
                        } catch (Exception e) {
                            continue;
                        }
                        break;
                    } while (true);
                }

                serverService.sendMessageWithoutResponse("average is : " + Arrays.stream(numbers).average() + "\n");
            }
        }
    }
}
