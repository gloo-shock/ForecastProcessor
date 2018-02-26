package com.forecast.forecasts;

import com.forecast.utils.ForecastTableModel;
import com.forecast.utils.MyGridBagLayout;

import javax.swing.*;

import static com.forecast.utils.MyGridBagConstraints.Anchor.GB_CENTER;
import static com.forecast.utils.MyGridBagConstraints.Anchor.GB_NORTHWEST;
import static com.forecast.utils.MyGridBagConstraints.Fill.GB_BOTH;
import static com.forecast.utils.MyGridBagConstraints.Fill.GB_NONE;
import static com.forecast.utils.MyGridBagLayout.getSharedConstraints;
import static java.awt.EventQueue.invokeLater;

public class ForecastPanel extends JPanel {

    private final ForecastTableModel tableModel = new ForecastTableModel();
    private final AddForecastDialog addDialog;

    public ForecastPanel(JFrame parent) {
        super(new MyGridBagLayout());
        JButton addButton = new JButton("Добавить прогноз");
        addDialog = new AddForecastDialog(parent, tableModel);
        addButton.addActionListener(e -> addDialog.setVisible(true));
        add(addButton, getSharedConstraints(0, 0, 1, 1, 0, 0, GB_NORTHWEST, GB_NONE, 8, 8, 0, 0));
        add(matchesTable(), getSharedConstraints(0, 1, 1, 1, 1, 1, GB_CENTER, GB_BOTH, 8, 8, 0, 0));
    }

    private JTable matchesTable() {
        JTable table = new JTable();
        table.setModel(tableModel);
        tableModel.addTableModelListener(e -> invokeLater(table::repaint));
        return table;
    }
}
