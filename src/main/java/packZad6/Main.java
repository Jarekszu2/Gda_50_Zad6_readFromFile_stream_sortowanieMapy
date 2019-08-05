package packZad6;

import java.util.List;

/*
6 Zadanie:
W pliku dane_zad.txt znajdują się historyczne wyniki gry w mario użytkowników.
Wypisz:
    - 3 najlepsze wyniki
    - nazwę użytkownika który grał najczęściej (jest na liście najwięcej razy)
    - nazwę użytkownika którego suma wszystkich wyników jest największa
    - 3 dni w które zostało rozegrane najwięcej gier

 */
public class Main {
    public static void main(String[] args) {
        System.out.println();
        Utilities utilities = new Utilities();

        System.out.println("Program wczytuje historyczne wyniki gry w mario i pozwala na analizę wybranych pozycji:");
        System.out.println();
        String stringFromFile = utilities.getStringFromFile("dane_zad.csv");
        String[] tab = utilities.createTabFromString(stringFromFile);
        List<GameResults> list = utilities.createGameResultsListFromTabString(tab);
//        utilities.printList(list);

        utilities.mainWork(list);
    }
}
