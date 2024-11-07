import java.io.*;
import java.net.*;
import java.util.*;

public class QuizClient {
    public static void main(String[] args) {
        BufferedReader in = null;   // read from server
        BufferedWriter out = null;  // write to server
        Socket socket = null;
        Scanner scanner = new Scanner(System.in);
        
        try{
            String host = "localhost";  // default host
            int port = 9999;    // port number

            List<String> lines = new ArrayList<>(); // use array to get host number and port number from the txt file
            String file = "C:\\Users\\user\\Desktop\\coding\\github\\HW1\\IPadress.txt";    // location of txt file

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
                if(!lines.isEmpty()){
                    host = lines.get(0);    // first line will be saved in host
                    port = Integer.parseInt(lines.get(1));  // second line will be saved in port
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            while(true){
                String inputMessage = in.readLine();    // read from server
                System.out.println(inputMessage);       // and show it

                String outputMessage = scanner.nextLine();  // scan your answer
                out.write(outputMessage + "\n");
                out.flush();                                // and send it to the server

                if(outputMessage.equalsIgnoreCase("no")){   // if you said 'no' 
                    System.out.println("Quiz finished.");               // quiz will be finished and disconnected from the server
                    break; 
                }
                else if (outputMessage.equalsIgnoreCase("yes")){ // if you said 'yes' then quiz starts
                    for(int i=0; i<10; i++){
                        inputMessage = in.readLine();           // read the question first
                        System.out.println(inputMessage);
                        outputMessage = scanner.nextLine();     // write your answer
                        out.write(outputMessage + "\n");
                        out.flush();

                        inputMessage = in.readLine();           // read whether your answer is right or not
                        System.out.println(inputMessage);
                    }
                    inputMessage = in.readLine();               // read your total score
                    System.out.println(inputMessage);
                }
                

            }
        }catch(IOException e){
            System.out.println(e.getMessage());
        }finally{
            try{
                scanner.close();
                if(socket!=null)
                    socket.close();
            }catch(IOException e){
                System.out.println("Error occurred!!");
            }
        }
    }
}
