package battleship;

import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

public class BattleshipGame {
    private final BattleshipView battleshipView;
    private final String[][] field;
    private final Scanner scanner = new Scanner(System.in);

    class Point {
        int x;
        int y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public BattleshipGame() {
        this.battleshipView = new BattleshipView(this);
        this.field = new String[10][10];
        for (String[] strings : field) {
            Arrays.fill(strings, "~");
        }
    }

    public void start() {
        placeShips();
        startGame();
    }

    private void startGame() {
        System.out.println("\nThe game starts!");
        battleshipView.showField();
        takeShot();
    }

    private void takeShot() {
        while (true) {
            System.out.println("\nTake a shot!");
            String coordinate = scanner.nextLine();
            Optional<Point> point = checkCoordinate(coordinate);
            if (point.isEmpty()) {
                System.out.println("Error! You entered the wrong coordinates! Try again:");
                continue;
            }
            if (field[point.get().x][point.get().y].equals("O")) {
                field[point.get().x][point.get().y] = "X";
                battleshipView.showField();
                System.out.println("\nYou hit a ship!");
            } else {
                field[point.get().x][point.get().y] = "M";
                battleshipView.showField();
                System.out.println("\nYou missed!");
            }
        }
    }

    private Optional<Point> checkCoordinate(String coordinate) {
        int x = coordinate.charAt(0) - 'A';
        if (x < 0 || x > 9) {
            return Optional.empty();
        }
        int y;
        try {
            y = Integer.parseInt(coordinate.substring(1)) - 1;
            if (y < 0 || y > 9) {
                return Optional.empty();
            }
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
        return Optional.of(new Point(x, y));
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
            Optional<Point> startPoint = checkCoordinate(coordinates[0]);
            Optional<Point> endPoint = checkCoordinate(coordinates[1]);
            if (startPoint.isEmpty() || endPoint.isEmpty()) {
                System.out.println("Error");
                continue;
            }
            int x1 = startPoint.get().x;
            int x2 = endPoint.get().x;
            int y1 = startPoint.get().y;
            int y2 = endPoint.get().y;

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
