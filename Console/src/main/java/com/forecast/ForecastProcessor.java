package com.forecast;

import javax.swing.*;

import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

public class ForecastProcessor {

    public static void main(String[] args) {
        try {
            JFrame frame = new MainFrame();
            frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
            frame.setVisible(true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
