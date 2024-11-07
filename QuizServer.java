import java.net.*;
import java.io.*;

public class QuizServer{
    public static void main(String[] args) {
        ServerSocket welcomeSocket = null;
        Socket socket = null;
        BufferedReader in = null;
        BufferedWriter out = null;

        String[] questions = new String[]{"Where is the capital of South Korea?", "What's the name of the planet that humanity lives on?", "Who established Microsoft?",
                    "What is the largest mammal on Earth?", "Which country is famous for the Eiffel Tower?", "What is the chemical symbol for water?", "Who wrote Romeo and Juliet?",
                    "What is the smallest prime number?", "In what year did Korean War end?", "What is the boiling point of water ub Celsius?"};
        String[] answers = new String[]{"seoul", "earth", "bill gates", "blue whale", "france", "h2o", "william shakespeare", "2", "1953", "100"};

        int nPort = 9999;

        try{
            welcomeSocket = new ServerSocket(nPort);
            System.out.println("Server started... Waiting fot new connection..");
            socket = welcomeSocket.accept();
            System.out.println("Connected!");
            int score = 0;
            in = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(
                new OutputStreamWriter(socket.getOutputStream()));
            String inputMessage;
            while(true){
                out.write("Do you want to start the Quiz?(yes or no)\n");
                out.flush();
                inputMessage = in.readLine();
                if(inputMessage.equalsIgnoreCase("no")){
                    System.out.println("Disconnected.");
                    break;
                }
                else if(inputMessage.equalsIgnoreCase("yes")){
                    for(int i=0; i<10; i++){
                        out.write(questions[i] + "\n");
                        out.flush();
                        inputMessage = in.readLine();

                        if(inputMessage.equalsIgnoreCase(answers[i])){
                            score += 10;
                            out.write("Correct!\n");
                        }
                        else{
                            out.write("Incorrect\n");
                        }
                        out.flush();
                    }
                    out.write("Your total score is: " + score + "\n");
                    out.flush();
                    
                }
            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }finally{
            try{
                if(socket != null)
                    socket.close();
                if(welcomeSocket != null)
                    welcomeSocket.close();
            }catch(IOException e){
                System.out.println("Error occured while chating with client!");
            }
        }

    }
}