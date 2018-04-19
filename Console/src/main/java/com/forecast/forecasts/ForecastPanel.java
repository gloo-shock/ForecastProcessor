package com.forecast.forecasts;

import com.forecast.entries.Tour;
import com.forecast.results.ResultTableModel;
import com.forecast.utils.MyGridBagLayout;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.ArrayList;

import static com.forecast.resources.ResourceUtils.getString;
import static com.forecast.rest.RestClient.restClient;
import static com.forecast.utils.MyGridBagConstraints.Anchor.*;
import static com.forecast.utils.MyGridBagConstraints.Fill.GB_BOTH;
import static com.forecast.utils.MyGridBagConstraints.Fill.GB_NONE;
import static com.forecast.utils.MyGridBagLayout.getSharedConstraints;

public class ForecastPanel extends JPanel {

    private final ForecastTableModel tableModel;

    public ForecastPanel(JFrame parent) {
        super(new MyGridBagLayout());
        this.tableModel = new ForecastTableModel(new Tour(new ArrayList<>()));
        add(forecastTable(parent, "team1"), getSharedConstraints(0, 0, 1, 1, 1, 1, GB_EAST, GB_BOTH, 8, 8, 0, 0));
        add(forecastTable(parent, "team2"), getSharedConstraints(0, 1, 1, 1, 1, 1, GB_EAST, GB_BOTH, 8, 8, 0, 0));
        add(resultTable(), getSharedConstraints(1, 0, 1, 1, 0, 1, GB_WEST, GB_BOTH, 42, 8, 0, 0));
    }

    private JPanel forecastTable(JFrame parent, String teamName) {
        JPanel panel = new JPanel(new MyGridBagLayout());
        JTable table = new JTable() {
            @Override
            public void createDefaultColumnsFromModel() {
                super.createDefaultColumnsFromModel();
                if (getColumnCount() > 0) {
                    getColumnModel().getColumn(0).setMinWidth(200);
                }
            }
        };
        ForecastTableModelWrapper modelWrapper = new ForecastTableModelWrapper(tableModel);
        JButton addButton = new JButton(getString("addForecast." + teamName));
        AddForecastDialog addDialog = new AddForecastDialog(parent, modelWrapper);
        addButton.addActionListener(e -> addDialog.setVisible(true));

        table.setModel(modelWrapper);
        table.setTableHeader(null);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setMinimumSize(new Dimension(300, 500));
        panel.add(addButton, getSharedConstraints(0, 0, 1, 1, 0, 0, GB_NORTHWEST, GB_NONE, 8, 8, 0, 0));
        panel.add(new JLabel(getString("forecastTable.name." + teamName)),
                getSharedConstraints(0, 1, 1, 1, 0, 0, GB_CENTER, GB_NONE, 0, 0, 0, 0));
        panel.add(scrollPane,
                getSharedConstraints(0, 2, 1, 1, 1, 1, GB_CENTER, GB_BOTH, 8, 0, 0, 0));
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
        panel.add(new JLabel(getString("resultsTable.name")),
                getSharedConstraints(0, 0, 1, 1, 0, 0, GB_CENTER, GB_NONE, 0, 0, 0, 0));
        panel.add(scrollPane,
                getSharedConstraints(0, 1, 1, 1, 1, 1, GB_CENTER, GB_BOTH, 8, 0, 0, 0));
        return panel;
    }

    public void saveData() {
        restClient().saveTour(tableModel.getTour());
    }
}
