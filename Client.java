import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.BorderFactory;
// import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Client extends JFrame{

   Socket socket;
   BufferedReader br;
   PrintWriter out;

   //GUI components
   private JLabel heading = new JLabel("Chat: Client");
   private JTextArea msgArea = new JTextArea();
   private JTextField msgInput = new JTextField();
   private Font font = new Font("Ariel", Font.PLAIN, 18);

   // constructor of client
   public Client() {
      try {
         System.out.println("Sending request to server");
         socket = new Socket("127.0.0.1",7777);
         System.out.println("connection done.");

         br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
         out = new PrintWriter(socket.getOutputStream());


         // //Initiate GUI
         initGUI();
         handleEvents();
         startReading();
         startWriting();

      } catch (Exception e) {
      }
   }

   public void handleEvents(){

      msgInput.addKeyListener(new KeyListener() {

         @Override
         public void keyTyped(KeyEvent e) {
         }

         @Override
         public void keyPressed(KeyEvent e) {
         }

         @Override
         public void keyReleased(KeyEvent e) {
         
            if(e.getKeyCode() == 10){
               String contentToSend = msgInput.getText();
               msgArea.append("Me: "+contentToSend+"\n");
               out.println(contentToSend);
               out.flush();
               msgInput.setText("");   
            }

         }
         
      });
   
   }

   public void initGUI(){
      //GUI 
      this.setTitle("Client Window");
      this.setSize(600, 700);
      this.setLocationRelativeTo(null);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.setVisible(true);

      heading.setFont(font);
      msgArea.setFont(font);
      msgInput.setFont(font);

      msgArea.setEditable(false);
      // heading.setIcon(new ImageIcon("icon.png"));
      heading.setHorizontalAlignment(SwingConstants.CENTER);
      heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

      // Set window layout
      this.setLayout(new BorderLayout());

      this.add(heading, BorderLayout.NORTH);
      JScrollPane Scrollpane = new JScrollPane(msgArea);
      this.add(Scrollpane, BorderLayout.CENTER);
      this.add(msgInput, BorderLayout.SOUTH);      

   }
   
   public void startReading() {
      // thread - read krke deta hai
      Runnable r1 = () -> {

         System.out.println("reader started..");

         try {

            while (true) {

               String msg = br.readLine();
               if (msg.equals("exit")) {
                  System.out.println("Server terminated the chat");
                  JOptionPane.showMessageDialog(this, "Server Terminated the chat");
                  msgInput.setEnabled(false);
                  socket.close();
                  break;
               }

               // System.out.println("Server:" + msg);
               msgArea.append("Server: "+msg+"\n");

            }
         } catch (Exception e) {
            // e.printStackTrace();
            System.out.println("connection is closed");
         }

      };
      new Thread(r1).start();
   }

   public void startWriting() {
      // thread - data user se lega and then send krega client tak
      Runnable r2 = () -> {
         System.out.println("writer started...");

         try {
            while (!socket.isClosed()) {

               BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));

               String content = br1.readLine();

               out.println(content);
               out.flush();

               if (content.equals("exit")) {
                  socket.close();
                  break;
               }

            }
            System.out.println("connection is closed");

         } catch (Exception e) {
            e.printStackTrace();
         }
      };
      new Thread(r2).start();
   }

   public static void main(String[] args) {
      System.out.println("this is client...");
      new Client();
   }
}
