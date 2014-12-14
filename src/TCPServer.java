import javax.swing.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
 

public class TCPServer extends Thread {
 
    public static final int SERVERPORT = 4444;
    private boolean running = false;
    private PrintWriter mOut;
    private OnMessageReceived messageListener;
 
    public static void main(String[] args) {
 
        ServerBoard frame = new ServerBoard();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
 
    }
 
    public TCPServer(OnMessageReceived messageListener) {
        this.messageListener = messageListener;
    }
 
    public void sendMessage(String message){
        if (mOut != null && !mOut.checkError()) {
            mOut.println(message);
            mOut.flush();
        }
    }
 
    @Override
    public void run() {
        super.run();
 
        running = true;
 
        try {
            System.out.println("S: Connecting...");
            ServerSocket serverSocket = new ServerSocket(SERVERPORT);
 
            Socket client = serverSocket.accept();
            System.out.println("S: Receiving...");
 
            try {
 
                mOut = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
 
                BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
 
                while (running) {
                    String message = in.readLine();
 
                    if (message != null && messageListener != null) {
                        messageListener.messageReceived(message);
                    }
                }
 
            } catch (Exception e) {
                System.out.println("S: Error");
                e.printStackTrace();
            } finally {
                client.close();
                System.out.println("S: Done.");
            }
 
        } catch (Exception e) {
            System.out.println("S: Error");
            e.printStackTrace();
        }
 
    }
 
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
 
}