package com.forecast;

import com.forecast.forecasts.ForecastPanel;

import javax.swing.*;
import java.awt.*;
import java.io.UnsupportedEncodingException;

import static com.forecast.resources.ResourceUtils.getString;

public class MainFrame extends JFrame {

    public MainFrame() throws HeadlessException, UnsupportedEncodingException {
        super("Forecast Processor");
        setPreferredSize(new Dimension(1000, 500));
        setMinimumSize(new Dimension(1000, 500));
        initTabbedPane();
    }

    private void initTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab(getString("forecasts"), new ForecastPanel(this));
        add(tabbedPane);
    }
}
