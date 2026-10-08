package utb.fai;

import java.io.IOException;

public class App {

    public static void main(String[] args) {
        if (args.length < 6) {
            System.err.println("Usage: app.jar <host> <port> <from> <to> <subject> <text>");
            return;
        }
        EmailSender sender = null;
        try {
            sender = new EmailSender(args[0], Integer.parseInt(args[1]));
            sender.send(args[2], args[3], args[4], args[5]);
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        } finally {
            if (sender != null) sender.close();
        }
    }
}