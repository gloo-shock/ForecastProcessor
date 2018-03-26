package com.forecast.forecasts;

import com.forecast.entries.ForecastResult;
import com.forecast.entries.Person;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ForecastTableModelWrapper extends AbstractTableModel {
    private final ForecastTableModel forecastTableModel;
    private final List<Person> persons = new ArrayList<>();

    public ForecastTableModelWrapper(ForecastTableModel forecastTableModel) {
        this.forecastTableModel = forecastTableModel;
        forecastTableModel.addTableModelListener(() -> fireTableChanged(null));
    }

    @Override
    public int getRowCount() {
        return forecastTableModel.getRowCount();
    }

    @Override
    public int getColumnCount() {
        return persons.size() + 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return forecastTableModel.getValueAt(rowIndex, columnIndex, persons);
    }

    public void addEntry(Person person, List<ForecastResult> forecasts) {
        if (!persons.contains(person)) {
            persons.add(person);
        }
        forecastTableModel.addEntry(person, forecasts);
    }
}
