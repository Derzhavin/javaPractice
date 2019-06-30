package com.codebind;

import javax.swing.JButton;
import java.awt.Color;


enum ButtonState {
    ACTIVE,
    INACTIVE
}

class Button extends JButton{
    private ButtonState state;

    void changeState() {
        if (state == ButtonState.ACTIVE) {
            this.setBackground(new Color(165, 165, 197));
        } else {
            this.setBackground(new Color(205, 210, 255));
        }
    }

    void setState(ButtonState state) {
        this.state = state;
    }

    ButtonState getState() {
        return state;
    }
}