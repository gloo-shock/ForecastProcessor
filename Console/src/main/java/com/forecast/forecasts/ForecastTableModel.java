package com.forecast.forecasts;

import com.forecast.entries.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.forecast.resources.ResourceUtils.getString;
import static java.util.stream.Collectors.toList;

public class ForecastTableModel {
    private final Map<Match, Result> results = new HashMap<>();
    private List<Match> matches = new ArrayList<>();
    private final List<Runnable> listeners = new ArrayList<>();
    private final Tour tour;

    public ForecastTableModel(Tour tour) {
        this.tour = tour;
    }

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
        List<ForecastResult> forecastResults = tour.getForecastResults(person);
        if (rowIndex == getRowCount() - 1) {
            return forecastResults.stream().mapToInt(ForecastResult::getScore).sum() + " " + getString("pts");
        }
        ForecastResult result = forecastResults.stream()
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
        forecasts.forEach(forecastResult ->
                forecastResult.setResultAndUpdateScore(results.get(forecastResult.getMatch())));
        tour.addPersonForecast(person, forecasts);
        matches = tour.getPersonForecasts().stream()
                .flatMap(personForecast -> personForecast.getForecasts().stream().map(ForecastResult::getMatch))
                .distinct()
                .collect(toList());
        fireModelChanged();
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void fillScore(Match match, Result result) {
        results.put(match, result);
        tour.getPersonForecasts().stream()
                .flatMap(personForecast -> personForecast.getForecasts().stream())
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

    public Tour getTour() {
        return tour;
    }
}
