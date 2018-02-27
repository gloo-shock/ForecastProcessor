package com.forecast.forecasts;

import com.forecast.utils.MyGridBagLayout;

import javax.swing.*;
import java.awt.*;

import static com.forecast.utils.MyGridBagConstraints.Anchor.*;
import static com.forecast.utils.MyGridBagConstraints.Fill.GB_BOTH;
import static com.forecast.utils.MyGridBagConstraints.Fill.GB_NONE;
import static com.forecast.utils.MyGridBagLayout.getSharedConstraints;

public class ForecastPanel extends JPanel {

    private final ForecastTableModel tableModel = new ForecastTableModel();
    private final AddForecastDialog addDialog;

    public ForecastPanel(JFrame parent) {
        super(new MyGridBagLayout());
        JButton addButton = new JButton("Добавить прогноз");
        addDialog = new AddForecastDialog(parent, tableModel);
        addButton.addActionListener(e -> addDialog.setVisible(true));
        add(addButton, getSharedConstraints(0, 0, 1, 1, 0, 0, GB_NORTHWEST, GB_NONE, 8, 8, 0, 0));
        add(forecastTable(), getSharedConstraints(0, 1, 1, 1, 1, 1, GB_EAST, GB_BOTH, 8, 8, 0, 0));
        add(resultTable(), getSharedConstraints(1, 1, 1, 1, 0, 1, GB_WEST, GB_BOTH, 8, 8, 0, 0));
    }

    private JTable forecastTable() {
        JTable table = new JTable();
        table.setModel(tableModel);
        return table;
    }

    private JTable resultTable() {
        JTable table = new JTable();
        table.setModel(new ResultTableModel(tableModel));
        table.setMinimumSize(new Dimension(500, 500));
        return table;
    }
}
