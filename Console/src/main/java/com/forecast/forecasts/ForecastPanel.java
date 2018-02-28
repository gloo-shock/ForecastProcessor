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

    private JScrollPane forecastTable() {
        JTable table = new JTable();
        table.setModel(tableModel);
        table.setTableHeader(null);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setMinimumSize(new Dimension(300, 500));
        return scrollPane;
    }

    private JScrollPane resultTable() {
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
        return scrollPane;
    }
}
