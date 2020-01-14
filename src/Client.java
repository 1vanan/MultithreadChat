import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;


// TODO: how to close all threads during logout
public class Client
{
    final static int ServerPort = 1234;

    public static void main(String args[]) throws UnknownHostException, IOException
    {
        AtomicBoolean isRunning = new AtomicBoolean(true);

        Scanner scn = new Scanner(System.in);

        // getting localhost ip
        InetAddress ip = InetAddress.getByName("localhost");

        // establish the connection
        Socket s = new Socket(ip, ServerPort);

        // obtaining input and out streams
        DataInputStream dis = new DataInputStream(s.getInputStream());
        DataOutputStream dos = new DataOutputStream(s.getOutputStream());

        // sendMessage thread
        Thread sendMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {
                while (isRunning.get()) {
                    System.out.println("~~! " + isRunning.get());

                    // read the message to deliver.
                    String msg = scn.nextLine();

                    try {
                        // write on the output stream
                        dos.writeUTF(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("Totally destroy");

            }
        });

        // readMessage thread
        Thread readMessage = new Thread(new Runnable()
        {
            @Override
            public void run() {

                while (isRunning.get()) {
                    System.out.println("~~! " + isRunning.get());
                    try {
                        // read the message sent to this client
//                        if(dis.read())
                        String msg = dis.readUTF();
                        System.out.println(msg);
                    } catch (IOException e) {
//                        if (e.getClass().equals(EOFException.class))
                        System.out.println("set");
                            isRunning.set(false);
                        e.printStackTrace();
                    }
                }
            }
        });

        sendMessage.start();
        readMessage.start();
        System.out.println("end of the reading");

    }
}