package com.forecast;

import com.forecast.forecasts.ForecastPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.UnsupportedEncodingException;

import static com.forecast.resources.ResourceUtils.getString;
import static javax.swing.JOptionPane.CANCEL_OPTION;
import static javax.swing.JOptionPane.showConfirmDialog;

public class MainFrame extends JFrame {

    public MainFrame() throws HeadlessException, UnsupportedEncodingException {
        super("Forecast Processor");
        setPreferredSize(new Dimension(1000, 700));
        setMinimumSize(new Dimension(1000, 700));
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int closeOption = showConfirmDialog(MainFrame.this, getString("confirmClosing"));
                switch (closeOption) {
                    case JOptionPane.OK_OPTION:
                        e.getWindow().dispose();
                        break;
                    case JOptionPane.NO_OPTION:
                        e.getWindow().dispose();
                        break;
                    case CANCEL_OPTION:
                        break;
                }
            }
        });
        initTabbedPane();
    }

    private void initTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab(getString("forecasts"), new ForecastPanel(this));
        add(tabbedPane);
    }
}
