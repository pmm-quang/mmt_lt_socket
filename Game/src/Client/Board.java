package Client;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Random;

public class Board extends JPanel {


    private static final int SQRT_OF_NUMBER_OF_NUMBERS = 10;
    private int[][] number_matrix = new int[Client.SQRT_OF_NUMBER_OF_NUMBERS][Client.SQRT_OF_NUMBER_OF_NUMBERS];
    private Square[][] arrayButton;

    Board() {
        setLayout(new GridLayout(10, 10));
        setSize(500, 500);
        createButton();

    }

    public void createNumberMatrix() {
        for (int i = 0; i < Client.SQRT_OF_NUMBER_OF_NUMBERS; ++i) {
            number_matrix[i] = new int[Client.SQRT_OF_NUMBER_OF_NUMBERS];
        }
        boolean[] is_this_number_being_used = new boolean[Client.SQRT_OF_NUMBER_OF_NUMBERS * Client.SQRT_OF_NUMBER_OF_NUMBERS];
        Arrays.fill(is_this_number_being_used, false);
        Random random = new Random();
        int random_number;

        for (int i = 0; i < Client.SQRT_OF_NUMBER_OF_NUMBERS; ++i) {
            for (int j = 0; j < Client.SQRT_OF_NUMBER_OF_NUMBERS; ++j) {
                do {
                    random_number = random.nextInt(Client.SQRT_OF_NUMBER_OF_NUMBERS * Client.SQRT_OF_NUMBER_OF_NUMBERS) + 1;
                } while (is_this_number_being_used[random_number - 1]);
                number_matrix[i][j] = random_number;
                is_this_number_being_used[random_number - 1] = true;
            }
        }
    }
    private void createButton() {

        arrayButton = new Square[Client.SQRT_OF_NUMBER_OF_NUMBERS][Client.SQRT_OF_NUMBER_OF_NUMBERS];
        for(int i = 0; i< Client.SQRT_OF_NUMBER_OF_NUMBERS; i++) {
            for (int j = 0; j < Client.SQRT_OF_NUMBER_OF_NUMBERS; j++) {
                arrayButton[i][j] = new Square(false);

                arrayButton[i][j].setBackground(Color.LIGHT_GRAY);
                arrayButton[i][j].setMargin(new Insets(0, 0, 0, 0));
                arrayButton[i][j].setFont(new Font("Consolas", Font.PLAIN, 14));

                add(arrayButton[i][j]);
                System.out.print(number_matrix[i][j] + " ");
                int finalI = i;
                int finalJ = j;
            }
            System.out.println("\n");
        }
    }
    public void createNumberOfButton() {
        for (int i = 0; i < Client.SQRT_OF_NUMBER_OF_NUMBERS; i++) {
            for (int j = 0; j < Client.SQRT_OF_NUMBER_OF_NUMBERS; j++) {
                arrayButton[i][j].setText(Integer.toString(number_matrix[i][j]));
                System.out.print(number_matrix[i][j] + " ");
            }
            System.out.println("\n");
        }
    }

    public int[][] getNumber_matrix() {
        return number_matrix;
    }

    public void setNumber_matrix(int[][] number_matrix) {
        this.number_matrix = number_matrix;
    }

    public void setVisibleButton(int i, int j) {
        arrayButton[i][j].setVisible(false);
    }

    public Square[][] getArrayButton() {
        return arrayButton;
    }
    public void resetButton() {
        for (int i = 0; i < Client.SQRT_OF_NUMBER_OF_NUMBERS; i++) {
            for (int j = 0; j < Client.SQRT_OF_NUMBER_OF_NUMBERS; j++) {
                arrayButton[i][j].setText("");
                arrayButton[i][j].setVisible(true);
            }
        }
    }

}
