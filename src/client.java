import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class client {

    public static int port = 55555;
    public static String host = "localhost";

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(host, port);
        DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

        Scanner scanner = new Scanner(System.in);
        String serverMessage;

        do {
            serverMessage = dataInputStream.readUTF();
            if (serverMessage.equals("/WithoutResponse")) {
                serverMessage = dataInputStream.readUTF();
                System.out.print(serverMessage);
            } else {
                System.out.print(serverMessage);
                String content = scanner.nextLine();
                dataOutputStream.writeUTF(content);
            }
        } while (!serverMessage.equals("/bye"));
    }
}
