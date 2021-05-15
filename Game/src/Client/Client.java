package Client;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.awt.event.*;

import javax.swing.*;


public class Client extends JFrame implements Runnable,ActionListener{

    public static int SQRT_OF_NUMBER_OF_NUMBERS = 10;   //Thêm cái này cho đỡ viết số 10
    static int current_number;                        //Số hiện tại cần phải bấm để có điểm
    private int player_score;
    private int opponent_score;

    public static Boolean check = false;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message="";
    private String chatServer;
    private Socket client;
    Board board;
    JButton bt_start;
    JButton bt_quit;
    private JLabel player_score_label;
    private JLabel opponent_score_label;
    private JLabel background;

    Boolean play= false;
    boolean start = false;


    Chat boxChat;
    String username = "";
    private static int i,j;


    private boolean dacowinner = false;


    public Client(String host, String username){

        super("Client");
        this.setSize(770, 600);
        this.getContentPane().setLayout(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        this.username = username;
        chatServer=host;
        connectToServer();
        getStreams();
        sendData(Key.LOGIN.toString() + ",đã kết nối!");

        boxChat = new Chat();
        boxChat.setBounds(510, 200,260, 350);
        add(boxChat);
        boxChat.notice(username, Key.LOGIN.toString());
        board = new Board();
        board.setBounds(0, 60, 500, 500);
        add(board);


        boxChat.getBt_send().addActionListener(this);
        boxChat.getTf_enterchat().addActionListener(this);


        player_score_label = new JLabel("MY SCORE: " + Integer.toString(player_score));
        player_score_label.setLayout(null);
        player_score_label.setBounds(550, 100, 250, 20);
        player_score_label.setFont(new Font("Consolas", Font.BOLD, 18));
        player_score_label.setForeground(Color.RED);
        player_score_label.setVisible(true);
        opponent_score_label = new JLabel("ENEMY'S SCORE: " + Integer.toString(opponent_score));
        opponent_score_label.setLayout(null);
        opponent_score_label.setBounds(550, 150, 250, 20);
        opponent_score_label.setFont(new Font("Consolas", Font.PLAIN, 14));
        opponent_score_label.setForeground(Color.BLACK);
        opponent_score_label.setVisible(true);
        add(player_score_label);
        add(opponent_score_label);


        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                int confirmedPane = JOptionPane.showConfirmDialog(null,
                        "Are you sure you want to exit the program?",
                        "Exit Program Message Box",
                        JOptionPane.YES_NO_OPTION);

                if (confirmedPane == JOptionPane.YES_OPTION) {
                    sendData(Key.EXIT.toString() + ",da out");
                    System.out.println("Yes is the option");
                    System.exit(0);
                }
            }
        });

        for (int i = 0; i < SQRT_OF_NUMBER_OF_NUMBERS; i++) {
            for (int j = 0; j < SQRT_OF_NUMBER_OF_NUMBERS; j++) {
                board.getArrayButton()[i][j].addActionListener(this);
            }
        }
        ButtonPanel();
        bt_start.addActionListener(this);
        bt_quit.addActionListener(this);




        //   setSize(700,600);
        setVisible(true);
        setResizable(false);
    }

    private void ButtonPanel() {

        ImageIcon img_bt_Start = new ImageIcon("Game\\image\\buttonStart.png");
        ImageIcon img_bt_Exit = new ImageIcon("Game\\image\\buttonExit.png");

        bt_start = new JButton("", img_bt_Start);
        bt_start.setBackground(Color.green);
        bt_start.setBounds(10,10, img_bt_Start.getIconWidth(), img_bt_Start.getIconHeight());

        bt_quit = new JButton("", img_bt_Exit);
        bt_quit.setBackground(Color.RED);
        bt_quit.setBounds(120, 10, img_bt_Exit.getIconWidth(), img_bt_Exit.getIconHeight());

        add(bt_start);
        add(bt_quit);
    }

    public void run(){
        try{

            processConnection();
            closeConnection();
        }
        catch(EOFException e){
            System.out.println("Server ket thuc ket noi");
            System.exit(0);
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    private void connectToServer(){
        try{
            client=new Socket(InetAddress.getByName(chatServer),8989);

        }
        catch(Exception e){
            //displayArea.append("Rot mang ,hay co gang thu lai xem sao");
        }
        //displayArea.append("ket noi vao :"+client.getInetAddress().getHostName());
    }

    private void getStreams()  {


        try {
            output = new ObjectOutputStream(client.getOutputStream());
            output.flush();
            input = new ObjectInputStream(client.getInputStream());
        } catch (NullPointerException e) {
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //displayArea.append("\n got IO stream \n");
    }

    private void processConnection() throws IOException{
        do{
            try{
                //xu ly phan ket qua nha duoc tu server

                message=(String) input.readObject();
                String[] tmp = message.split(",");

                System.out.println(username+" nhan: "+message);
                //phan tich message
                if (tmp[0].equals(Key.CHAT.toString())) {

                    boxChat.receivedMessage(tmp[1], tmp[2]);

                } else if (tmp[0].equals(Key.LOGIN.toString())) {

                    boxChat.notice(tmp[2], tmp[0]);

                } else if (tmp[0].equals(Key.INVITE.toString())) {

                    int output = JOptionPane.showConfirmDialog(this, "Thông báo",
                            tmp[2] + " mời bạn chơi game.", JOptionPane.YES_NO_OPTION);
                    if(output == JOptionPane.YES_OPTION) {
                        sendData(Key.ACCEPT.toString() + ",play");

                    }

                } else if (tmp[0].equals(Key.ACCEPT.toString())) {
                    start = true;
                    play = true;
                    createNewGame();
                    String matrixNumber = "";
                    for (int i = 0; i < SQRT_OF_NUMBER_OF_NUMBERS; i++){
                        for (int j = 0; j < SQRT_OF_NUMBER_OF_NUMBERS; j++) {
                            matrixNumber += board.getNumber_matrix()[i][j] + ",";
                        }
                    }
                    sendData(Key.PLAY.toString() + "," + matrixNumber + username);

                } else if (tmp[0].equals(Key.PLAY.toString())){

                    int k = 1;
                    int[][] matrix = new int[SQRT_OF_NUMBER_OF_NUMBERS][SQRT_OF_NUMBER_OF_NUMBERS];
                    for (int i = 0; i < SQRT_OF_NUMBER_OF_NUMBERS; i++) {
                        for (int j = 0; j < SQRT_OF_NUMBER_OF_NUMBERS; j++) {
                            matrix[i][j] = Integer.valueOf(tmp[k]);
                            k++;
                        }
                        board.setNumber_matrix(matrix);
                        board.createNumberOfButton();
                        play = true;
                        player_score = 0;
                        opponent_score = 0;
                        current_number = 1;
                    }

                }else if (tmp[0].equals(Key.EXIT.toString())) {
                    boxChat.notice(tmp[2], tmp[0]);
                    if (play) {
                        JOptionPane.showMessageDialog(this, "You win");
                        play = false;
                        board.resetButton();
                    }

                } else if (tmp[0].equals(Key.GAME.toString())) {
                    int i = Integer.valueOf(tmp[2]);
                    int j = Integer.valueOf(tmp[3]);
                    current_number = Integer.valueOf(tmp[1]);
                    board.setVisibleButton(i, j);
                    current_number++;
                    opponent_score++;
                    opponent_score_label.setText("ENEMY'S SCORE: " + Integer.toString(opponent_score));
                    System.out.println("So tiep theo: " + current_number);

                } else if (tmp[0].equals(Key.QUIT.toString())) {
                    JOptionPane.showMessageDialog(this , "You win");
                    play = false;
                    board.resetButton();

                } else if (tmp[0].equals(Key.WIN.toString())) {
                    JOptionPane.showMessageDialog(this , "You lose");
                    play = false;
                    board.resetButton();
                }


            }
            catch(ClassNotFoundException e){
                //displayArea.append("\n Unknown object type received");
            }
        }while(true);
    }

    private void closeConnection() throws IOException{
        input.close();
        output.close();
        client.close();
    }

    private void sendData(String message){
        try{
            System.out.println("gui: "+message);
            output.writeObject(message + "," + username);
            output.flush();
        }
        catch(IOException e){
            System.err.println("Loi trong viec write Object");
        }
    }



    private void createNewGame() {
        if (start) {
            player_score = 0;
            opponent_score = 0;
            current_number = 1;
            board.createNumberMatrix();
            board.createNumberOfButton();
            start = false;
        }
    }


    public void actionPerformed(ActionEvent event) {
        StringBuffer buffer = new StringBuffer(2048);


        if (event.getSource() == boxChat.getTf_enterchat()) {
            boxChat.setFontMessage();
            String msg = boxChat.sendMessage();
            if (!msg.equals(""))
                sendData(msg);
        }

        if (event.getSource() ==boxChat.getBt_send()) {
            boxChat.setFontMessage();
            String msg = boxChat.sendMessage();
            if (!msg.equals(""))
                sendData(msg);
        }

        if (event.getSource() == bt_start) {
            sendData(Key.INVITE.toString() + ",play game" + username);
        }

        if (event.getSource() == bt_quit) {
            if (play) {
                sendData(Key.QUIT.toString() + ",thoat game," + username);
                JOptionPane.showMessageDialog(this, "You lose");
                play = false;
                board.resetButton();
            }
        }

        if (play) {
            for(int i= 0;i<SQRT_OF_NUMBER_OF_NUMBERS ;i++) {
                for (int j = 0; j < SQRT_OF_NUMBER_OF_NUMBERS; j++) {
                    //tim xem nut nao duoc nhan
                    //nham lay vi tri cua button duoc kich hoat
                    if (event.getSource() == board.getArrayButton()[i][j] && board.getNumber_matrix()[i][j] == current_number) {
                        //client 1
                        System.out.println("Bam nut " + i + j);
                        board.setVisibleButton(i, j);
                        //message gui di cho server
                        buffer.append(Key.GAME.toString() + ",").append(board.getNumber_matrix()[i][j]).append(",").append(i).append(",").append(j);
                        sendData(buffer.toString());
                        current_number++;
                        player_score++;
                        updatePlayerScoreLabel();
                    }
                    board.getArrayButton()[i][j].setAutoscrolls(false);
                    board.getArrayButton()[i][j].setRolloverEnabled(false);
                    if(isWinner()){
                        dacowinner = true;
                        break;
                    }
                }
            }
            if(dacowinner) {
                sendData(Key.WIN.toString() + ",I am winer," + username);
                JOptionPane.showMessageDialog(this, "You win");
                play = false;
                board.resetButton();
                dacowinner = false;
            }

        }
    }

    private void updatePlayerScoreLabel() {
        player_score_label.setText("MY SCORE: " + player_score);
        switch (player_score) {
            case 10: {
                player_score_label.setForeground(Color.ORANGE);
                player_score_label.setFont(new Font("Consolas", Font.BOLD, 20));
                break;
            }
            case 20: {
                player_score_label.setForeground(Color.YELLOW);
                player_score_label.setFont(new Font("Consolas", Font.BOLD, 21));
                break;
            }
            case 30: {
                player_score_label.setForeground(Color.GREEN);
                player_score_label.setFont(new Font("Consolas", Font.BOLD, 22));
                break;
            }
            case 40: {
                player_score_label.setForeground(Color.CYAN);
                player_score_label.setFont(new Font("Consolas", Font.BOLD, 23));
                break;
            }
            case 50: {
                player_score_label.setForeground(Color.MAGENTA);
                player_score_label.setFont(new Font("Consolas", Font.BOLD, 28));
            }
            default:
        }
    }


    public boolean isWinner() {
        if (player_score > (SQRT_OF_NUMBER_OF_NUMBERS*SQRT_OF_NUMBER_OF_NUMBERS)/2) {
            return true;
        }
        return false;
    }

    public static void main(String args[]){
        Client app;
        Login login = new Login();
        while (CheckConnect.check == false) {
            Boolean check = CheckConnect.check;
        }
        if (CheckConnect.check) {
            if (args.length == 0) {
                app = new Client("127.0.0.1", login.createUsername());
                System.out.println(0);
            } else {
                app = new Client(args[0], login.createUsername());
                System.out.println(1);
            }
            app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            Thread t = new Thread(app);
            t.start();

        }
        //app.runClient();
    }


}