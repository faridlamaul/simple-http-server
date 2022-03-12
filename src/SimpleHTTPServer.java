import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SimpleHTTPServer {
    public static void main(String[] args) throws Exception {
        try {
            String websiteRoot = "../../";
            ServerSocket server = new ServerSocket(2022);
            while(true) {
                System.out.println("1 - Server started");
                Socket client = server.accept();
                System.out.println("2 - Client connected");
                
                BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
                System.out.println("3 - BufferedReader and BufferedWriter created");
                System.out.println("4 - Waiting for client to send data");
    
                // read the message from client (readline lebih baik karena text-based protocol ex. HTTP)
                String message = br.readLine();
                String urn = message.split(" ")[1];
                urn = urn.substring(1);
                String fileContent;
                String statusCode; 

                try {
                    FileInputStream fis = new FileInputStream(websiteRoot + urn);
                    fileContent = new String(fis.readAllBytes());
                    statusCode = "200 OK";
                } catch (FileNotFoundException e) {
                    fileContent = "File not found";
                    statusCode = "404 Not Found";
                }

                while (!message.isEmpty()) {
                    System.out.println(message);
                    message = br.readLine();
                }

                System.out.println("5 - Message received");
                // print the message
                // System.out.println("Message from client: " + message);
    
                bw.write("HTTP/1.1 " + statusCode + "\r\nContent-Type: text/html\r\nContent-Length: " + fileContent.length() + "\r\n\r\n" + fileContent);
                bw.flush();
                // write the message to client
                // close the connection
    
                client.close();
            }
                // server.close();
        } catch (Exception e) {
            Logger.getLogger(SimpleHTTPServer.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}