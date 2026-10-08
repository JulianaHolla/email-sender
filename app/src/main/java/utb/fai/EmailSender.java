package utb.fai;

import java.net.*;
import java.io.*;

public class EmailSender {
    private final Socket socket;
    private final BufferedReader reader;
    private final OutputStream output;

    public EmailSender(String host, int port) throws UnknownHostException, IOException {
        socket = new Socket(host, port);
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        output = socket.getOutputStream();
        readResponse();
    }

    public void send(String from, String to, String subject, String text) throws IOException {
        command("EHLO localhost");
        command("MAIL FROM:<" + from + ">");
        command("RCPT TO:<" + to + ">");
        command("DATA");
        String body = "From: " + from + "\r\n"
                + "To: " + to + "\r\n"
                + "Subject: " + subject + "\r\n"
                + "\r\n"
                + text.replace("\n.", "\n..") + "\r\n"
                + ".";
        command(body);
    }

    public void close() {
        try {
            command("QUIT");
        } catch (IOException e) {
        }
        try {
            socket.close();
        } catch (IOException e) {
        }
    }

    private String command(String cmd) throws IOException {
        output.write((cmd + "\r\n").getBytes());
        output.flush();
        return readResponse();
    }

    private String readResponse() throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
            if (line.length() < 4 || line.charAt(3) != '-') {
                break;
            }
        }
        return sb.toString();
    }
}