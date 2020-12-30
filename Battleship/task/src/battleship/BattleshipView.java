package battleship;

public class BattleshipView {
    BattleshipGame battleshipGame;

    BattleshipView(BattleshipGame battleshipGame) {
        this.battleshipGame = battleshipGame;
    }

    public void showField() {
        String[][] field = battleshipGame.getField();
        System.out.print(" ");
        for (int i = 0; i < field[0].length; i++) {
            System.out.print(" " + (i + 1));
        }
        System.out.println();
        char currentRow = 'A';
        for (String[] rows : field) {
            System.out.print(currentRow++);
            for (String cell : rows) {
                System.out.print(" " + cell);
            }
            System.out.println();
        }
    }
}
