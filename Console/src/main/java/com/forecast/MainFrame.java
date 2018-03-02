package com.forecast;

import com.forecast.forecasts.ForecastPanel;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    public MainFrame() throws HeadlessException {
        super("Forecast Processor");
        setPreferredSize(new Dimension(1000, 500));
        setMinimumSize(new Dimension(1000, 500));
        initTabbedPane();
    }

    private void initTabbedPane() {
        JTabbedPane tabbedPane = new JTabbedPane();
//        tabbedPane.addTab(ResourceBundle.getBundle("string", new Locale("ru"), this.getClass().getClassLoader()).getString("forecasts"), new ForecastPanel(this));
        tabbedPane.addTab("Прогнозы", new ForecastPanel(this));
        add(tabbedPane);
    }
}
