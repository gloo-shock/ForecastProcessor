package com.forecast.utils;

import com.forecast.entries.Forecast;
import com.forecast.entries.Match;
import com.forecast.entries.Person;
import com.forecast.entries.Team;

import javax.swing.table.AbstractTableModel;
import java.util.*;
import java.util.stream.Collectors;

public class ForecastTableModel extends AbstractTableModel {
    private final Map<Person, Set<Forecast>> forecastMap = new HashMap<>();
    private final List<Match> matches = new ArrayList<>();
    private final List<Person> persons = new ArrayList<>();

    public ForecastTableModel() {
        matches.addAll(Arrays.asList(
                new Match(new Team("Реал"), new Team("Барселона")),
                new Match(new Team("Интер"), new Team("Милан")),
                new Match(new Team("ЦСКА"), new Team("Спартак")),
                new Match(new Team("ПСЖ"), new Team("Монако"))));


        Person andrey = new Person("Андрей", "Глушок");
        forecastMap.put(andrey,
                matches.stream().map(match -> new Forecast(match, 1, 2)).collect(Collectors.toSet()));
        Person sergey = new Person("Сергей", "Ерошенко");
        forecastMap.put(sergey,
                matches.stream().map(match -> new Forecast(match, 2, 3)).collect(Collectors.toSet()));
        persons.addAll(Arrays.asList(andrey, sergey));
    }

    @Override
    public int getRowCount() {
        return forecastMap.values().stream().map(Set::size).max(Integer::compareTo).orElse(0) + 1;
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
            return match.getHost().getName() + "-" + match.getGuest().getName();
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

    public void addEntry(String entry) {

    }
}
