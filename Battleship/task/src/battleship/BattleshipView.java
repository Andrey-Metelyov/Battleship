package battleship;

public class BattleshipView {
    BattleshipGame battleshipGame;

    BattleshipView(BattleshipGame battleshipGame) {
        this.battleshipGame = battleshipGame;
    }

    public void showField() {
        show(battleshipGame.getField(), false);
    }

    private void show(String[][] field, boolean fogged) {
        System.out.print(" ");
        for (int i = 0; i < field[0].length; i++) {
            System.out.print(" " + (i + 1));
        }
        System.out.println();
        char currentRow = 'A';
        for (String[] rows : field) {
            System.out.print(currentRow++);
            for (String cell : rows) {
                if (fogged && cell.equals("O")) {
                    System.out.print(" ~");
                } else {
                    System.out.print(" " + cell);
                }
            }
            System.out.println();
        }
    }

    public void showFoggedField() {
        show(battleshipGame.getField(), true);
    }
}
