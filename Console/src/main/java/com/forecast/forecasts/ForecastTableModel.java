package com.forecast.forecasts;

import com.forecast.entries.Forecast;
import com.forecast.entries.ForecastResult;
import com.forecast.entries.Match;
import com.forecast.entries.Person;

import javax.swing.table.AbstractTableModel;
import java.util.*;

import static java.util.stream.Collectors.toSet;

public class ForecastTableModel extends AbstractTableModel {
    private final Map<Person, Set<ForecastResult>> data = new HashMap<>();
    private final Map<Match, Forecast> results = new HashMap<>();
    private final List<Match> matches = new ArrayList<>();
    private final List<Person> persons = new ArrayList<>();

    @Override
    public int getRowCount() {
        return matches.size() + 1;
    }

    @Override
    public int getColumnCount() {
        return data.size() + 1;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex == 0 && columnIndex == 0) {
            return "";
        }
        Match match = rowIndex == 0 ? null : matches.get(rowIndex - 1);
        if (columnIndex == 0) {
            return match;
        }
        Person person = persons.get(columnIndex - 1);
        if (rowIndex == 0) {
            return person.getFirstName();
        }
        ForecastResult result = data.get(person).stream()
                .filter(forecast1 -> forecast1.getMatch().equals(match))
                .findAny().orElse(null);
        if (result == null) {
            return "";
        }
        return result.getForecast().getHostScore() + "-" + result.getForecast().getGuestScore() + " (" + result.getScoreString() + ")";
    }

    public void addEntry(Person person, Set<ForecastResult> forecasts) {
        if (!persons.contains(person)) {
            persons.add(person);
        }
        matches.addAll(forecasts.stream()
                .map(ForecastResult::getMatch)
                .filter(match -> !matches.contains(match))
                .collect(toSet()));
        Set<ForecastResult> currentForecasts = data.computeIfAbsent(person, person1 -> new HashSet<>());
        currentForecasts.removeIf(forecast -> forecasts.stream().map(ForecastResult::getMatch)
                .anyMatch(match -> match.equals(forecast.getMatch())));
        forecasts.forEach(forecastResult ->
                forecastResult.setResultAndUpdateScore(results.get(forecastResult.getMatch())));
        currentForecasts
                .addAll(forecasts);
        fireTableChanged(null);
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void fillScore(Match match, Forecast result) {
        results.put(match, result);
        data.values().stream()
                .flatMap(Collection::stream)
                .filter(forecastResult -> forecastResult.getMatch().equals(match))
                .forEach(forecastResult -> forecastResult.setResultAndUpdateScore(result));
        int row = matches.indexOf(match) + 1;
        fireTableRowsUpdated(row, row);
    }
}
