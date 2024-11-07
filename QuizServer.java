import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;

public class QuizServer {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(10); // threads for multi client service
        int nPort = 9999;   // port number
        ServerSocket welcomeSocket = null;

        try {
            welcomeSocket = new ServerSocket(nPort);
            System.out.println("Server started... Waiting fot new connection..");

            while (true) {
                Socket socket = welcomeSocket.accept(); // accept clients' connection
                System.out.println("Connected!"); // if connected then prints

                executorService.execute(new QuizClientHandler(socket)); // excute runable class
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            executorService.shutdown();
        }

    }

    static class QuizClientHandler implements Runnable {    // shows the quiz and score
        private Socket socket;
        String[] questions = new String[] { "Where is the capital of South Korea?",
                "What's the name of the planet that humanity lives on?", "Who established Microsoft?",
                "What is the largest mammal on Earth?", "Which country is famous for the Eiffel Tower?",
                "What is the chemical symbol for water?", "Who wrote Romeo and Juliet?",
                "What is the smallest prime number?", "In what year did Korean War end?",
                "What is the boiling point of water ub Celsius?" }; // questions
        String[] answers = new String[] { "seoul", "earth", "bill gates", "blue whale", "france", "h2o",
                "william shakespeare", "2", "1953", "100" }; // answers

        public QuizClientHandler(Socket socket) {   // constructor
            this.socket = socket;
        }

        @Override
        public void run() {
            int score = 0; // show the score of client
            BufferedReader in = null;   // read from client
            BufferedWriter out = null;  // write to client
            try {
                in = new BufferedReader( 
                        new InputStreamReader(socket.getInputStream()));
                out = new BufferedWriter( 
                        new OutputStreamWriter(socket.getOutputStream()));

                String inputMessage;    // get answer from client by this

                while (true) {
                    out.write("Do you want to start the Quiz?(yes or no)\n");
                    out.flush();

                    inputMessage = in.readLine();   // get client answer whether he wants to start or not

                    if (inputMessage.equalsIgnoreCase("no")) {  // if he said no, then disconnect it
                        System.out.println("Disconnected.");
                        break;
                    } else if (inputMessage.equalsIgnoreCase("yes")) {  // if he said yes, then start the quiz
                        for (int i = 0; i < 10; i++) {
                            out.write(questions[i] + "\n"); // show the question
                            out.flush();
                            inputMessage = in.readLine();   // get the answer

                            if (inputMessage.equalsIgnoreCase(answers[i])) {    // corrects then + 10 score
                                score += 10;
                                out.write("Correct!\n");
                            } else {                                            // incorrects then 0 score
                                out.write("Incorrect\n");
                            }
                            out.flush();    // show 'Correct' or 'Incorrect'
                        }
                        out.write("Your total score is: " + score + "\n");  // show the total score
                        out.flush();
                    }
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            } finally {
                try {
                    if (socket != null) {
                        socket.close();
                    }
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}