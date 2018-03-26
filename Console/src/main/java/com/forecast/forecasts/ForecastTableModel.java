package com.forecast.forecasts;

import com.forecast.entries.Forecast;
import com.forecast.entries.ForecastResult;
import com.forecast.entries.Match;
import com.forecast.entries.Person;

import java.util.*;

import static com.forecast.resources.ResourceUtils.getString;
import static java.util.stream.Collectors.toList;

public class ForecastTableModel {
    private final Map<Person, Set<ForecastResult>> data = new HashMap<>();
    private final Map<Match, Forecast> results = new HashMap<>();
    private final List<Match> matches = new ArrayList<>();
    private final List<Runnable> listeners = new ArrayList<>();

    public int getRowCount() {
        return matches.size() + 2;
    }

    Object getValueAt(int rowIndex, int columnIndex, List<Person> persons) {
        if (rowIndex == 0 && columnIndex == 0) {
            return "";
        }
        Match match = (rowIndex == 0 || rowIndex > matches.size()) ? null : matches.get(rowIndex - 1);
        if (columnIndex == 0) {
            return rowIndex == getRowCount() - 1 ? getString("sum") : match;
        }
        Person person = persons.get(columnIndex - 1);
        if (rowIndex == 0) {
            return person.getFirstName();
        }
        if (rowIndex == getRowCount() - 1) {
            return data.get(person).stream().mapToInt(ForecastResult::getScore).sum() + " " + getString("pts");
        }
        ForecastResult result = data.get(person).stream()
                .filter(forecast1 -> forecast1.getMatch().equals(match))
                .findAny().orElse(null);
        if (result == null) {
            return "";
        }
        return result.getForecast().getHostScore() + "-" + result.getForecast().getGuestScore() + " (" + result.getScore() + " " + getString("pts") + ")";
    }

    void addEntry(Person person, List<ForecastResult> forecasts) {
        matches.addAll(forecasts.stream()
                .map(ForecastResult::getMatch)
                .filter(match -> !matches.contains(match))
                .collect(toList()));
        Set<ForecastResult> currentForecasts = data.computeIfAbsent(person, person1 -> new HashSet<>());
        currentForecasts.removeIf(forecast -> forecasts.stream().map(ForecastResult::getMatch)
                .anyMatch(match -> match.equals(forecast.getMatch())));
        forecasts.forEach(forecastResult ->
                forecastResult.setResultAndUpdateScore(results.get(forecastResult.getMatch())));
        currentForecasts
                .addAll(forecasts);
        fireModelChanged();
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
        fireModelChanged();
    }

    public void addTableModelListener(Runnable runnable) {
        listeners.add(runnable);
    }

    private void fireModelChanged() {
        listeners.forEach(Runnable::run);
    }
}
