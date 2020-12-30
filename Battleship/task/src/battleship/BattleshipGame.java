package battleship;

import java.util.Arrays;
import java.util.Scanner;

public class BattleshipGame {
    private final BattleshipView battleshipView;
    private final String[][] field;
    private final Scanner scanner = new Scanner(System.in);

    public BattleshipGame() {
        this.battleshipView = new BattleshipView(this);
        this.field = new String[10][10];
        for (String[] strings : field) {
            Arrays.fill(strings, "~");
        }
    }

    public void start() {
        placeShips();
    }

    private void placeShips() {
        battleshipView.showField();
        placeShip("Aircraft Carrier", 5);
        battleshipView.showField();
        placeShip("Battleship", 4);
        battleshipView.showField();
        placeShip("Submarine", 3);
        battleshipView.showField();
        placeShip("Cruiser", 3);
        battleshipView.showField();
        placeShip("Destroyer", 2);
        battleshipView.showField();
    }

    private void placeShip(String shipName, int size) {
        while (true) {
            System.out.println("\nEnter the coordinates of the " + shipName + " (" + size + " cells):");
            String[] coordinates = scanner.nextLine().split(" ");
            if (coordinates.length < 2) {
                System.out.println("Error");
                continue;
            }
            int x1 = coordinates[0].charAt(0) - 'A';
            int x2 = coordinates[1].charAt(0) - 'A';
            if (x1 < 0 || x1 > 9 || x2 < 0 || x2 > 9) {
                System.out.println("Error");
                continue;
            }
            int y1;
            int y2;
            try {
                y1 = Integer.parseInt(coordinates[0].substring(1)) - 1;
                y2 = Integer.parseInt(coordinates[1].substring(1)) - 1;
            } catch (NumberFormatException exception) {
                System.out.println("Error");
                continue;
            }
            if (y1 < 0 || y1 > 9 || y2 < 0 || y2 > 9) {
                System.out.println("Error");
                continue;
            }
//            System.out.println(x1 + " " + y1 + " " + x2 + " " + y2);

            if (x1 != x2 && y1 != y2) {
                System.out.println("Error");
                continue;
            }
            if (x1 == x2 && Math.abs(y2 - y1) + 1 != size
                    || y1 == y2 && Math.abs(x2 - x1) + 1 != size) {
                System.out.println("Error! Wrong length of the " + shipName + "! Try again:");
                continue;
            }
            if (!checkOtherShips(x1, y1, x2, y2)) {
                System.out.println("Error! You placed it too close to another one. Try again:");
                continue;
            }

            if (x1 == x2) {
                int start = Math.min(y1, y2);
                for (int i = 0; i < size; i++) {
                    field[x1][start + i] = "O";
                }
            } else {
                int start = Math.min(x1, x2);
                for (int i = 0; i < size; i++) {
                    field[start + i][y1] = "O";
                }
            }
            return;
        }
    }

    private boolean checkOtherShips(int x1, int y1, int x2, int y2) {
        int startX = Math.max(0, Math.min(x1 - 1, x2 - 1));
        int startY = Math.max(0, Math.min(y1 - 1, y2 - 1));
        int endX = Math.min(9, Math.max(x1 + 1, x2 + 1));
        int endY = Math.min(9, Math.max(y1 + 1, y2 + 1));
//        System.out.println("Check [" + startX + ", " + startY + "] - [" + endX + ", " + endY + "]");
        for (int i = startX; i <= endX; i++) {
            for (int j = startY; j <= endY; j++) {
//                System.out.print(" " + field[i][j]);
                if (!field[i][j].equals("~")) {
//                    System.out.println("Error in checkOtherShips");
                    return false;
                }
            }
        }
//        System.out.println();
        return true;
    }

    public String[][] getField() {
        return field;
    }
}
