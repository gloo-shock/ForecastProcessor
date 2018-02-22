package com.forecast.forecasts;

import com.forecast.entries.Forecast;
import com.forecast.entries.Person;
import com.forecast.utils.MyGridBagLayout;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.forecast.utils.MyGridBagConstraints.Anchor.GB_NORTH;
import static com.forecast.utils.MyGridBagConstraints.Fill.GB_BOTH;

public class ForecastPanel extends JPanel {

    private final Map<Person, Set<Forecast>> forecastMap = new HashMap<>();

    public ForecastPanel() {
        super(new GridBagLayout());
        add(matchesTable(), MyGridBagLayout.getSharedConstraints(0, 0, 1, 1, 1, 1, GB_NORTH, GB_BOTH, 8, 8, 0, 0));
    }

    private JTable matchesTable() {

        JTable table = new JTable();
        table.setModel(new AbstractTableModel() {
            @Override
            public int getRowCount() {
                return 0;
            }

            @Override
            public int getColumnCount() {
                return 0;
            }

            @Override
            public Object getValueAt(int rowIndex, int columnIndex) {
                return null;
            }
        });
        return table;
    }
}
