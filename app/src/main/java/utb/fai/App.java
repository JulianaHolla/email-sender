package org.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class App {

    private static void send(OutputStream output, InputStream input, String message)
            throws IOException, InterruptedException {
        byte[] buffer = message.getBytes();
        output.write(buffer, 0, buffer.length);
        output.flush();

        Thread.sleep(500);
        if (input.available() > 0) {
            byte[] response = new byte[1024];
            int len = input.read(response);
            System.out.write(response, 0, len);
            System.out.flush();
        }
    }

    public static void main(String[] args) throws InterruptedException {

        try (Socket socket = new Socket("localhost", 9999)) {
            System.out.println("Connected to server");

            InputStream input = socket.getInputStream();
            OutputStream output = socket.getOutputStream();

            // úvodní pozdrav serveru (220)
            Thread.sleep(500);
            if (input.available() > 0) {
                byte[] response = new byte[1024];
                int len = input.read(response);
                System.out.write(response, 0, len);
                System.out.flush();
            }

            send(output, input, "EHLO m1_krcma@utb.cz\r\n");
            send(output, input, "MAIL FROM:<m1_krcma@utb.cz>\r\n");
            send(output, input, "RCPT TO:<m1_krcma@utb.cz>\r\n");
            send(output, input, "DATA\r\n");
            send(output, input,
                    "From: m1_krcma@utb.cz\r\n" +
                    "To: m1_krcma@utb.cz\r\n" +
                    "Subject: Test\r\n" +
                    "\r\n" +
                    "Ahoj, toto je testovaci zprava.\r\n" +
                    ".\r\n");
            send(output, input, "QUIT\r\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}