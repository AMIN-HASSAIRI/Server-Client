import java.net.*;
import java.io.*;

public class GreetingClient {

    public static void main(String[] args)
    {
        try {
            String p_str, g_str, A_str;
            String serverName = "localhost";
            int port = 8088;

            // Declare p, g, and Key of client
            int p = 23;
            int g = 9;
            int a = 4;
            double A_dash, serverB;

            // Established the connection
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);
            System.out.println("Just connected to " + client.getRemoteSocketAddress());

            // Sends the data to client
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);

            p_str = Integer.toString(p);
            out.writeUTF(p_str); // Sending p

            g_str = Integer.toString(g);
            out.writeUTF(g_str); // Sending g

            double A = ((Math.pow(g, a)) % p); // calculation of A
            A_str = Double.toString(A);
            out.writeUTF(A_str); // Sending A

            // Client's Private Key
            System.out.println("From Client : Private Key = " + a);

            // Accepts the data
            DataInputStream in = new DataInputStream(client.getInputStream());

            serverB = Double.parseDouble(in.readUTF());
            System.out.println("From Server : Public Key = " + serverB);

            A_dash = ((Math.pow(serverB, a)) % p); // calculation of A_dash

            System.out.println("Secret Key to perform Symmetric Encryption = " + A_dash);

            System.out.println("------------------------------------------------------------------------------------");

            String messageToEncrypt = "Hello server! this is Amin!";

            AES aes = new AES();
            String d = String.valueOf(A_dash);
            aes.setKey(d);
            String encryptedMsg = aes.encrypt(messageToEncrypt,d);
            System.out.println("The Cipher text to be send to the server is: " + encryptedMsg);
            out.writeUTF(encryptedMsg); // sending the encrypted message

            System.out.println("------------------------------------------------------------------------------------");

            // Read the encrypted msg from the server and decrypted it
            String messageFromServer = in.readUTF(); // to accept message from server
            System.out.println("The Cipher text received from the server is: " + messageFromServer);
            String messageDecrypted = aes.decrypt(messageFromServer,d);
            System.out.println("The message received from the server after decryption: " + messageDecrypted);

            client.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

