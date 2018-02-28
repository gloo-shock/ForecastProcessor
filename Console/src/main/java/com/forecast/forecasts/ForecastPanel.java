package com.forecast.forecasts;

import com.forecast.utils.MyGridBagLayout;

import javax.swing.*;
import javax.swing.table.JTableHeader;
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

    private JPanel forecastTable() {
        JPanel panel = new JPanel(new MyGridBagLayout());
        JTable table = new JTable();
        table.setModel(tableModel);
        table.setTableHeader(null);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setMinimumSize(new Dimension(300, 500));
        panel.add(new JLabel("Прогнозы участников"),
                getSharedConstraints(0, 0, 1, 1, 0, 0, GB_CENTER, GB_NONE, 0, 0, 0, 0));
        panel.add(scrollPane,
                getSharedConstraints(0, 1, 1, 1, 1, 1, GB_CENTER, GB_BOTH, 8, 0, 0, 0));
        return panel;
    }

    private JPanel resultTable() {
        JTable table = new JTable() {
            @Override
            public void createDefaultColumnsFromModel() {
                super.createDefaultColumnsFromModel();
                if (getColumnCount() > 0) {
                    getColumnModel().getColumn(0).setMinWidth(200);
                }
            }
        };
        table.setModel(new ResultTableModel(tableModel));
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setMinimumSize(new Dimension(300, 500));
        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, table.getRowHeight()));
        JPanel panel = new JPanel(new MyGridBagLayout());
        panel.add(new JLabel("Результаты матчей"),
                getSharedConstraints(0, 0, 1, 1, 0, 0, GB_CENTER, GB_NONE, 0, 0, 0, 0));
        panel.add(scrollPane,
                getSharedConstraints(0, 1, 1, 1, 1, 1, GB_CENTER, GB_BOTH, 8, 0, 0, 0));
        return panel;
    }
}
