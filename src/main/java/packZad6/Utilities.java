package packZad6;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Utilities {
    public String getStringFromFile(String file) {
        String stringFromFile = "";
        StringBuilder builder = new StringBuilder();
        String line;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
//            reader.lines().forEach(builder::append);
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append(",");
            }
        } catch (FileNotFoundException e) {
            System.err.println("Błąd pliku.");
        } catch (IOException e) {
            System.err.println("Błąd wejścia-wyjścia.");
        }
        stringFromFile = builder.toString();
        return stringFromFile;
    }

    public String[] createTabFromString(String stringFromFile) {
        String[] tab = stringFromFile.split(",");
        return tab;
    }

    public List<GameResults> createGameResultsListFromTabString(String[] tab) {
        List<GameResults> list = new ArrayList<>();
        for (int i = 0; i < tab.length; i+=3) {
            GameResults gameResults = new GameResults();
            gameResults.setTimestamp(tab[i].substring(0, 10));
            gameResults.setScore(Integer.valueOf(tab[i + 1]));
            gameResults.setName(tab[i + 2]);
            list.add(gameResults);
        }
        return list;
    }

    public void printList(List<GameResults> list) {
        list.forEach(System.out::println);
    }

    public void mainWork(List<GameResults> list) {
        ScannerWork scannerWork = new ScannerWork();

        System.out.println("Prezentacja wyników gry w Mario:");
        boolean flag = false;
        char znak = 'a';
        do {
            System.out.println();
            System.out.println("Wybierz:\n a) trzy najlepsze wyniki\n b) użytkownika, który grał najczęściej (jest na liście najwięcej razy)" +
                    "\n c) użytkownika, którego suma wszystkich punktów jest najwyższa" +
                    "\n d) 3 dni, w które zostało rozegrane najwięcej gier" +
                    "\n w) koniec");
            znak = scannerWork.getChar();
            switch (znak) {
                case 'a':
                    System.out.println();
                    System.out.println("3 najlepsze wyniki:");
                    List<Integer> listaAllInt = get3BestResults(list);
                    List<Integer> lista3Best = new ArrayList<>();
                    for (int i = 0; i < 3; i++) {
                        lista3Best.add(listaAllInt.get(i));
                    }
                    lista3Best.forEach(System.out::println);
                    break;
                case 'b':
                    System.out.println();
                    System.out.println("Użytkownicy, którzy grali najczęściej:");
                    Optional<Map.Entry<String, Integer>> optTheBest = getTheOftenestPlayer(list);
                    if (optTheBest.isPresent()) {
                        Map.Entry<String, Integer> mapEntryBest = optTheBest.get();
                        String name = mapEntryBest.getKey();
                        int number = mapEntryBest.getValue();

                        Map<String, Integer> map = getMapNameHowManyTimes(list);

                        Map<String, Integer> mapBest = map.entrySet().stream()
                                .filter(stringIntegerEntry -> stringIntegerEntry.getValue() == number)
                                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

                        mapBest.forEach((k, v) -> System.out.println(k + " " + v));
                    }
                    break;
                case 'c':
                    System.out.println();
                    System.out.println("Użytkownik, którego suma wszystkich punktów jest najwyższa:");
                    Optional<Map.Entry<String, Integer>> optNameMaxScoreSum = getOptMapEntryNameScoresSum(list);
                    if (optNameMaxScoreSum.isPresent()) {
                        Map.Entry<String, Integer> nameMaxScoreSum = optNameMaxScoreSum.get();
                        System.out.println(nameMaxScoreSum.getKey() + " " + nameMaxScoreSum.getValue());
                    }
                    break;
                case 'd':
                    System.out.println();
                    System.out.println("3 dni, w które zostało rozegrane najwięcej gier:");
                    Map<String, Integer> mapDateNumberOfGames = getMapDateNumberOfGames(list);
                    mapDateNumberOfGames.forEach((k, v) -> System.out.println(k + " " + v));
                    break;
                case 'w':
                    flag = true;
                    break;
            }
        } while (!flag);
    }

    public List<Integer> get3BestResults(List<GameResults> gameResultsList) {
        List<Integer> listResult = gameResultsList.stream()
                .map(gameResults -> gameResults.getScore())
//                .collect(Collectors.toList())
//                .stream()
                .sorted(Integer::compareTo)
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());
        return listResult;
    }

    public Optional<Map.Entry<String, Integer>> getTheOftenestPlayer(List<GameResults> gameResultsList) {
        Map<String, Integer> map = getMapNameHowManyTimes(gameResultsList);

        Optional<Map.Entry<String, Integer>> optTheBest = getStringIntegerEntryFirst(map);

        return optTheBest;
    }

    private Optional<Map.Entry<String, Integer>> getStringIntegerEntryFirst(Map<String, Integer> map) {
        Map<String, Integer> mapSorted = map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        return mapSorted.entrySet().stream()
                .findFirst();
    }

    private Map<String, Integer> getMapNameHowManyTimes(List<GameResults> gameResultsList) {
        Set<String> setName = getSetNames(gameResultsList);

        return setName.stream()
                .collect(Collectors.toMap(
                        n -> n,
                        n -> gameResultsList.stream()
                            .filter(gameResults -> gameResults.getName().equals(n))
                            .collect(Collectors.toList())
                            .size()
                ));
    }

    private Set<String> getSetNames(List<GameResults> gameResultsList) {
        return gameResultsList.stream()
                    .map(gameResults -> gameResults.getName())
                    .collect(Collectors.toSet());
    }

    private Optional<Map.Entry<String, Integer>> getOptMapEntryNameScoresSum(List<GameResults> list) {
        Set<String> setNames = getSetNames(list);

        Map<String, Integer> map = setNames.stream()
                .collect(Collectors.toMap(
                        n -> n,
                        n -> list.stream()
                            .filter(gameResults -> gameResults.getName().equals(n))
                            .mapToInt(gameResults -> gameResults.getScore())
                            .sum()
                ));


        Optional<Map.Entry<String, Integer>> optMaxScore = getStringIntegerEntryFirst(map);

        return optMaxScore;
    }

    public Map<String, Integer> getMapDateNumberOfGames(List<GameResults> gameResultsList) {
        Set<String> setDates = gameResultsList.stream()
                .map(gameResults -> gameResults.getTimestamp())
                .collect(Collectors.toSet());

        Map<String, Integer> mapDateNumberOfGames = setDates.stream()
                .collect(Collectors.toMap(
                        d -> d,
                        d -> gameResultsList.stream()
                            .filter(gameResults -> gameResults.getTimestamp().equals(d))
                            .collect(Collectors.toList()).size()
                ));

        Collection<Integer> integerCollections = mapDateNumberOfGames.values();
        List<Integer> listaIloscigier = integerCollections.stream()
                .sorted(Comparator.reverseOrder())
                .collect(Collectors.toList());

        Map<String, Integer> mapResult = new HashMap<>();

        Set<Map.Entry<String, Integer>> entrySet = mapDateNumberOfGames.entrySet();
        for (int i = 0; i < 3; i++) {
            for (Map.Entry<String, Integer> stringIntegerEntry : entrySet) {
                if (stringIntegerEntry.getValue() == listaIloscigier.get(i)) {
                    mapResult.put(stringIntegerEntry.getKey(), stringIntegerEntry.getValue());
                }
            }
        }
        Map<String, Integer> mapResultSorted = mapResult.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        return mapResultSorted;
    }
}
