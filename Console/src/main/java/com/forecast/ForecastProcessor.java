package com.forecast;

import javax.swing.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class ForecastProcessor {

    public static void main(String[] args) {
        JFrame frame = new MainFrame();
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}
