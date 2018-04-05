package com.forecast.results;

import com.forecast.entries.ForecastResult;
import com.forecast.entries.Match;
import com.forecast.entries.Result;
import com.forecast.forecasts.ForecastTableModel;

import javax.swing.table.AbstractTableModel;
import java.util.HashMap;
import java.util.Map;

import static com.forecast.resources.ResourceUtils.getString;

public class ResultTableModel extends AbstractTableModel {
    public static final int MATCH_COLUMN = 0;
    public static final int SCORE_COLUMN = 1;
    private final ForecastTableModel forecastTableModel;
    private final Map<Match, Result> result = new HashMap<>();


    public ResultTableModel(ForecastTableModel forecastTableModel) {
        this.forecastTableModel = forecastTableModel;
        forecastTableModel.addTableModelListener(() -> fireTableChanged(null));
    }

    @Override
    public int getRowCount() {
        return forecastTableModel.getMatches().size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Match match = forecastTableModel.getMatches().get(rowIndex);
        if (columnIndex == MATCH_COLUMN) {
            return match;
        }
        Result forecast = result.get(match);
        if (forecast != null) {
            return forecast.getHostScore() + "-" + forecast.getGuestScore();
        }
        return "";
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex != 0;
    }

    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (columnIndex > 0 && aValue instanceof String) {
            Match match = forecastTableModel.getMatches().get(rowIndex);
            ForecastResult forecastResult = ForecastResult.parseFromString(match.toString() + " " + aValue);
            if (forecastResult == null) {
//                JOptionPane.showMessageDialog();
            } else {
                Result result = new Result(forecastResult.getForecast().getHostScore(),
                        forecastResult.getForecast().getGuestScore());
                this.result.put(match, result);
                forecastTableModel.fillScore(match, result);
            }
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case MATCH_COLUMN:
                return getString("match");
            case SCORE_COLUMN:
                return getString("score");
        }
        return super.getColumnName(column);
    }
}
