package packZad6;

import java.util.List;
import java.util.Scanner;

public class ScannerWork {
    Scanner scanner = new Scanner(System.in);

    public char getChar() {
        boolean flag = false;
        char znak = 'a';
        do {
            System.out.println();
            System.out.println("Wprowadź: a/b/c/d/w ?");
            znak = scanner.next().charAt(0);
            if (znak == 'a' || znak == 'b' ||znak == 'c' ||znak == 'd' ||znak == 'w') {
                flag = true;
            }
        } while (!flag);
        return znak;
    }
}
