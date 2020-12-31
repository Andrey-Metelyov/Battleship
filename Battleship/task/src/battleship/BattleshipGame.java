package battleship;

import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

public class BattleshipGame {
    private final BattleshipView battleshipView;
    private final String[][] field;
    private final String[][] field2;
    private final Scanner scanner = new Scanner(System.in);

    public final static void clearConsole()
    {
        try
        {
            final String os = System.getProperty("os.name");

            if (os.contains("Windows"))
            {
                Runtime.getRuntime().exec("cls");
            }
            else
            {
                Runtime.getRuntime().exec("clear");
            }
        }
        catch (final Exception e)
        {
            //  Handle any exceptions.
        }
    }

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
        field = initField();
        field2 = initField();
        start();
    }

    private String[][] initField() {
        final String[][] field;
        field = new String[10][10];
        for (String[] strings : field) {
            Arrays.fill(strings, "~");
        }
        return field;
    }

    public void start() {
        System.out.println("Player 1, place your ships on the game field");
        placeShips(field);
        System.out.println("Press Enter and pass the move to another player");
        scanner.nextLine();
        System.out.println("...");
        System.out.println("Player 2, place your ships to the game field");
        placeShips(field2);
        startGame();
    }

    private void startGame() {
        takeShot();
    }

    private void takeShot() {
        int currentPlayer = 1;
        String[][] currentPlayerField = field;
        String[][] otherPlayerField = field2;
        while (true) {
            System.out.println("Press Enter and pass the move to another player");
            scanner.nextLine();
//            System.out.println("...");
//            System.out.print("\033[H\033[2J");
//            System.out.flush();
//            clearConsole();
            battleshipView.showFields(otherPlayerField, currentPlayerField);
            System.out.println("\nPlayer " + currentPlayer + ", it's your turn:");
            String coordinate = scanner.nextLine();
            Optional<Point> point = checkCoordinate(coordinate);
            if (point.isEmpty()) {
                System.out.println("Error! You entered the wrong coordinates! Try again:");
                continue;
            }
            int x = point.get().x;
            int y = point.get().y;
            if (otherPlayerField[x][y].equals("O")) {
                otherPlayerField[x][y] = "X";
                if (shipSanked(otherPlayerField, x, y)) {
                    if (allShipsSanked(otherPlayerField)) {
                        System.out.println("You sank the last ship. You won. Congratulations!");
                        return;
                    } else {
                        System.out.println("\nYou sank a ship! Specify a new target:");
                    }
                } else {
                    System.out.println("\nYou hit a ship!");
                }
            } else if (otherPlayerField[x][y].equals("~")) {
                otherPlayerField[x][y] = "M";
                System.out.println("\nYou missed!");
            } else {
                System.out.println("\nError. Already shot!");
            }
//            battleshipView.showField();
            battleshipView.showFields(otherPlayerField, currentPlayerField);
            if (currentPlayer == 1) {
                currentPlayer = 2;
                currentPlayerField = field2;
                otherPlayerField = field;
            } else {
                currentPlayer = 1;
                currentPlayerField = field;
                otherPlayerField = field2;
            }
        }
    }

    private boolean allShipsSanked(String[][] field) {
        for (int i = 0; i < field.length; i++) {
            for (int j = 0; j < field[i].length; j++) {
                if (field[i][j].equals("O")) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean shipSanked(String[][] field, int x, int y) {
        // go left
        int curX = x - 1;
        while (curX > 0) {
            String cell = field[curX][y];
            if (cell.equals("O")) {
                return false;
            }
            if (cell.equals("M") || cell.equals("~")) {
                break;
            }
            curX--;
        }
        // go right
        curX = x + 1;
        while (curX < 9) {
            String cell = field[curX][y];
            if (cell.equals("O")) {
                return false;
            }
            if (cell.equals("M") || cell.equals("~")) {
                break;
            }
            curX++;
        }
        // go up
        int curY = y - 1;
        while (curY > 0) {
            String cell = field[x][curY];
            if (cell.equals("O")) {
                return false;
            }
            if (cell.equals("M") || cell.equals("~")) {
                break;
            }
            curY--;
        }
        // go down
        curY = y + 1;
        while (curY < 9) {
            String cell = field[x][curY];
            if (cell.equals("O")) {
                return false;
            }
            if (cell.equals("M") || cell.equals("~")) {
                break;
            }
            curY++;
        }
        return true;
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

    private void placeShips(String[][] field) {
        battleshipView.showField(field);
        placeShip(field, "Aircraft Carrier", 5);
        battleshipView.showField(field);
        placeShip(field, "Battleship", 4);
        battleshipView.showField(field);
        placeShip(field, "Submarine", 3);
        battleshipView.showField(field);
        placeShip(field, "Cruiser", 3);
        battleshipView.showField(field);
        placeShip(field, "Destroyer", 2);
        battleshipView.showField(field);
    }

    private void placeShip(String[][] field, String shipName, int size) {
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
            if (!checkOtherShips(field, x1, y1, x2, y2)) {
                System.out.println("Error! You placed it too close to another one. Try again:");
//                battleshipView.showField(field);
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

    private boolean checkOtherShips(String[][] field, int x1, int y1, int x2, int y2) {
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
