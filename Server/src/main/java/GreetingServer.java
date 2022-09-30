import java.net.*;
import java.io.*;

public class GreetingServer {

    public static void main(String[] args) throws IOException
    {
        try {

            int port = 8088;

            // Server Key
            int b = 3;

            // Client p, g, and key
            double clientP, clientG, clientA, B, B_dash;
            String B_str;

            // Established the Connection
            ServerSocket serverSocket = new ServerSocket(port);
            System.out.println("Waiting for client on port " + serverSocket.getLocalPort() + "...");
            Socket server = serverSocket.accept();
            System.out.println("Just connected to " + server.getRemoteSocketAddress());

            // Server's Private Key
            System.out.println("From Server : Private Key = " + b);

            // Accepts the data from client
            DataInputStream in = new DataInputStream(server.getInputStream());

            clientP = Integer.parseInt(in.readUTF()); // to accept p
            System.out.println("From Client : P = " + clientP);

            clientG = Integer.parseInt(in.readUTF()); // to accept g
            System.out.println("From Client : G = " + clientG);

            clientA = Double.parseDouble(in.readUTF()); // to accept A
            System.out.println("From Client : Public Key = " + clientA);

            B = ((Math.pow(clientG, b)) % clientP); // calculation of B
            B_str = Double.toString(B);

            // Sends data to client
            // Value of B
            OutputStream outToClient = server.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToClient);

            out.writeUTF(B_str); // Sending B

            B_dash = ((Math.pow(clientA, b)) % clientP); // calculation of B_dash

            System.out.println("Secret Key to perform Symmetric Encryption = " + B_dash);

            System.out.println("=====================================================================================");

            AES aew= new AES();
            String d = String.valueOf(B_dash);
            aew.setKey(d);

            // Read the encrypted msg from the client and decrypted it
            String messageFromClient = in.readUTF(); // to accept message from client
            System.out.println("The Cipher text coming from the client is: " + messageFromClient);
            String messageDecrypted = aew.decrypt(messageFromClient,d);
            System.out.println("The message received from the client after decryption: " + messageDecrypted);

            // Send an encrypted message to client
            String welcomeMsgToEncrypt = "Welcome to the server!";
            String messageEncrypted = aew.encrypt(welcomeMsgToEncrypt,d);
            out.writeUTF(messageEncrypted); // to send the encrypted message to client

            server.close();
        }

        catch (SocketTimeoutException s) {
            System.out.println("Socket timed out!");
        }
        catch (IOException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


