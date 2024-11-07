import java.io.*;
import java.net.*;
import java.util.*;

public class QuizClient {
    public static void main(String[] args) {
        BufferedReader in = null;
        BufferedWriter out = null;
        Socket socket = null;
        Scanner scanner = new Scanner(System.in);
        
        try{
            String host = "localhost";
            int port = 9999;

            List<String> lines = new ArrayList<>();
            String file = "C:\\Users\\user\\Desktop\\coding\\github\\HW1\\IPadress.txt";

            try (BufferedReader br = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = br.readLine()) != null) {
                    lines.add(line);
                }
                if(!lines.isEmpty()){
                    host = lines.get(0);
                    port = Integer.parseInt(lines.get(1));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            socket = new Socket(host, port);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            while(true){
                String inputMessage = in.readLine();
                System.out.println(inputMessage);

                String outputMessage = scanner.nextLine();
                out.write(outputMessage + "\n");
                out.flush();

                if(outputMessage.equalsIgnoreCase("no")){
                    System.out.println("Quiz finished.");
                    break;
                }
                else if (outputMessage.equalsIgnoreCase("yes")){
                    for(int i=0; i<10; i++){
                        inputMessage = in.readLine();
                        System.out.println(inputMessage);
                        outputMessage = scanner.nextLine();
                        out.write(outputMessage + "\n");
                        out.flush();

                        inputMessage = in.readLine();
                        System.out.println(inputMessage);
                    }
                    inputMessage = in.readLine();
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
