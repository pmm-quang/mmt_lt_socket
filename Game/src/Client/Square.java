package Client;

import javax.swing.*;
public class Square extends JButton{
    private boolean status=false;//trang thai cua nut do

    private int value;

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public Square(boolean status) {
        super();
        this.status = status;
    }
    public boolean isStatus() {
        return status;
    }
    public void setStatus(boolean status) {
        this.status = status;
    }



}