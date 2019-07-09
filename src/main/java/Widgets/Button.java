package Widgets;

import javax.swing.JButton;
import java.awt.Color;


public class Button extends JButton{
    private ButtonState state;

    public void changeState() {
        if (state == ButtonState.ACTIVE) {
            this.setBackground(new Color(170, 170, 199));
        } else {
            this.setBackground(new Color(219, 232, 254));
        }
    }

    public void setState(ButtonState state) {
        this.state = state;
    }

    ButtonState getState() {
        return state;
    }
}