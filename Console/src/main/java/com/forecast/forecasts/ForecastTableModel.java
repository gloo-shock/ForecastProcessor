package com.forecast.forecasts;

import com.forecast.entries.Forecast;
import com.forecast.entries.Match;
import com.forecast.entries.Person;

import javax.swing.table.AbstractTableModel;
import java.util.*;

import static java.util.stream.Collectors.toSet;

public class ForecastTableModel extends AbstractTableModel {
    private final Map<Person, Set<Forecast>> forecastMap = new HashMap<>();
    private final List<Match> matches = new ArrayList<>();
    private final List<Person> persons = new ArrayList<>();

    @Override
    public int getRowCount() {
        return matches.size() + 1;
    }

    @Override
    public int getColumnCount() {
        return forecastMap.size() + 1;
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
        Forecast forecast = forecastMap.get(person).stream()
                .filter(forecast1 -> forecast1.getMatch().equals(match))
                .findAny().orElse(null);
        if (forecast == null) {
            return "";
        }
        return forecast.getHostScore() + "-" + forecast.getGuestScore() + " (0 очков)";
    }

    public void addEntry(Person person, Set<Forecast> forecasts) {
        if (!persons.contains(person)) {
            persons.add(person);
        }
        matches.addAll(forecasts.stream()
                .map(Forecast::getMatch)
                .filter(match -> !matches.contains(match))
                .collect(toSet()));
        Set<Forecast> currentForecasts = forecastMap.computeIfAbsent(person, person1 -> new HashSet<>());
        currentForecasts.removeIf(forecast -> forecasts.stream().map(Forecast::getMatch)
                .anyMatch(match -> match.equals(forecast.getMatch())));
        currentForecasts
                .addAll(forecasts);
        fireTableChanged(null);
    }

    public List<Match> getMatches() {
        return matches;
    }
}
