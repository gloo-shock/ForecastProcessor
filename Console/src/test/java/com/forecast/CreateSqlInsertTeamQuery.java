package com.forecast;

import com.forecast.entries.League;
import org.junit.Ignore;
import org.junit.Test;

import java.util.stream.Stream;

@Ignore
public class CreateSqlInsertTeamQuery {

    @Test
    public void russia() {
        String teams = "Амкар\n" +
                "Анжи\n" +
                "Арсенал\n" +
                "Ахмат\n" +
                "Динамо\n" +
                "Зенит\n" +
                "Краснодар\n" +
                "Локомотив\n" +
                "Ростов\n" +
                "Рубин\n" +
                "СКА-Хабаровск\n" +
                "Спартак\n" +
                "Тосно\n" +
                "Урал\n" +
                "Уфа\n" +
                "ЦСКА";
        printListOfTeams(teams, League.RUSSIA);
    }

    @Test
    public void spain() {
        String teams = "Алавес\n" +
                "Атлетик Бильбао\n" +
                "Атлетико Мадрид \n" +
                "Барселона\n" +
                "Бетис\n" +
                "Валенсия\n" +
                "Вильярреал\n" +
                "Депортиво\n" +
                "Жирона\n" +
                "Лас-Пальмас\n" +
                "Леванте\n" +
                "Леганес\n" +
                "Малага\n" +
                "Реал Мадрид\n" +
                "Реал Сосьедад\n" +
                "Севилья\n" +
                "Сельта\n" +
                "Хетафе\n" +
                "Эйбар\n" +
                "Эспаньол\n";
        printListOfTeams(teams, League.SPAIN);
    }

    @Test
    public void italy() {
        String teams = "Ювентус\n" +
                "Рома\n" +
                "Наполи\n" +
                "Аталанта\n" +
                "Лацио\n" +
                "Милан\n" +
                "Интер\n" +
                "Фиорентина\n" +
                "Торино\n" +
                "Сампдория\n" +
                "Кальяри\n" +
                "Сассуоло\n" +
                "Удинезе\n" +
                "Кьево\n" +
                "Болонья\n" +
                "Дженоа\n" +
                "Кротоне\n" +
                "СПАЛ\n" +
                "Эллас Верона \n" +
                "Беневенто\n";
        printListOfTeams(teams, League.ITALY);
    }

    @Test
    public void england() {
        String teams = "Арсенал\n" +
                "Бернли\n" +
                "Борнмут\n" +
                "Брайтон энд Хоув Альбион \n" +
                "Вест Бромвич Альбион\n" +
                "Вест Хэм Юнайтед\n" +
                "Кристал Пэлас\n" +
                "Лестер Сити\n" +
                "Ливерпуль\n" +
                "Манчестер Сити\n" +
                "Манчестер Юнайтед\n" +
                "Ньюкасл Юнайтед\n" +
                "Саутгемптон\n" +
                "Сток Сити\n" +
                "Суонси Сити\n" +
                "Тоттенхэм Хотспур\n" +
                "Уотфорд\n" +
                "Хаддерсфилд Таун\n" +
                "Челси\n" +
                "Эвертон\n";
        printListOfTeams(teams, League.ENGLAND);
    }

    @Test
    public void france() {
        String teams = "Монако\n" +
                "Пари Сен-Жермен \n" +
                "Ницца\n" +
                "Лион\n" +
                "Марсель\n" +
                "Бордо\n" +
                "Нант\n" +
                "Сент-Этьен\n" +
                "Ренн\n" +
                "Генгам\n" +
                "Лилль\n" +
                "Анже\n" +
                "Тулуза\n" +
                "Мец\n" +
                "Монпелье\n" +
                "Дижон\n" +
                "Кан\n" +
                "Страсбур\n" +
                "Амьен\n" +
                "Труа\n";
        printListOfTeams(teams, League.FRANCE);
    }

    @Test
    public void germany() {
        String teams = "Айнтрахт Франкфурт\n" +
                "Аугсбург\n" +
                "Бавария\n" +
                "Байер 04\n" +
                "Боруссия (Дортмунд)\n" +
                "Боруссия (Мёнхенгладбах)\n" +
                "Вердер\n" +
                "Вольфсбург\n" +
                "Гамбург\n" +
                "Ганновер 96\n" +
                "Герта\n" +
                "Кёльн\n" +
                "Майнц 05\n" +
                "РБ Лейпциг\n" +
                "Фрайбург\n" +
                "Хоффенхайм\n" +
                "Шальке 04\n" +
                "Штутгарт";
        printListOfTeams(teams, League.GERMANY);
    }




    private void printListOfTeams(String teams, League league) {
        StringBuilder stringBuilder = new StringBuilder("INSERT INTO TEAMS (NAME, NAME2, NAME3, LEAGUE) \n" +
                "    VALUES \n");
        String[] teamsArray = teams.split("\n");

        String[] lines = Stream.of(teamsArray).map(
                team -> String.format("('%s', NULL, NULL, '%s')", team, league.getLeagueString()))
                .toArray(value -> new String[teamsArray.length]);

        stringBuilder.append(String.join(", \n", lines)).append(";");
        System.out.println(stringBuilder);
    }

}