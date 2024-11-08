package Viergewinnt;

import common.*;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Viergewinnt extends Spiel implements Protokollierbar {

    private Scanner sc = new Scanner(System.in);
    boolean spielstein_typ = false;

    private static final int[][] RICHTUNGSARRAY = {
        { 1, -1 }, // unten links -> oben rechts
        { 1, 0 }, // unten -> oben
        { 1, 1 }, // unten rechts -> oben links
        { 0, 1 }, // rechts -> links
    };

    public Viergewinnt(Spieler spieler_1, Spieler spieler_2) {
        super(
            new Spieler[] { spieler_1, spieler_2 },
            new ViergewinntSpielfeld()
        );
    }

    private int getZeile(int y) {
        int x = super.spielfeld.getXSize() - 1;

        while (x >= 0) {
            if (!super.spielfeld.pruefeBelegt(x, y)) {
                return x;
            }
            x -= 1;
        }
        return x;
    }

    /**
     * Prüft, ob es vier zusammenhängende Felder des angegebenen Spielers an der Stelle (x,y) in der Richtung (a, b) gibt.
     */
    private boolean pruefeVier(int x, int y, int a, int b, Spieler spieler) {
        int counter = 0;

        for (int i = -3; i < 4; i++) {
            if (
                super.spielfeld.pruefeBelegtSpieler(
                    x + a * i,
                    y + b * i,
                    spieler
                )
            ) {
                if (counter == 3) {
                    return false;
                }
                counter++;
            } else {
                counter = 0;
            }
        }
        return true;
    }

    private boolean pruefeWeiterspielen(int x, int y, Spieler spieler) {
        for (int[] richtung : RICHTUNGSARRAY) {
            if (!this.pruefeVier(x, y, richtung[0], richtung[1], spieler)) {
                return false;
            }
            System.out.println();
        }
        return true;
    }

    @Override
    public void protokolliere(Spielzug spielzug) {
        protokoll.push(spielzug);
    }

    @Override
    public void entferne() {
        protokoll.pop();
    }

    @Override
    public boolean spielzugMensch(Spieler spieler) {
        while (true) {
            this.spielstein_typ = this.spielstein_typ ^ true;

            int y;
            while (true) {
                try {
                    System.out.print(
                        "Bitte wähle deine Eingabespalte " +
                        spieler.getName() +
                        ": "
                    );
                    y = this.sc.nextInt() - 1;
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Bitte gib eine Zahl ein!");
                    this.sc.next();
                }
            }

            int x = this.getZeile(y);
            if (
                x >= 0 &&
                x < super.spielfeld.getXSize() &&
                y >= 0 &&
                y < super.spielfeld.getYSize()
            ) {
                if (!super.spielfeld.pruefeBelegt(x, y)) {
                    super.spielfeld.setSpielstein(
                        new Spielstein(spieler.getToken(), x, y)
                    );
                    Spielzug spielzug = new Spielzug(x, y, spieler);
                    this.protokolliere(spielzug);
                    return this.pruefeWeiterspielen(x, y, spieler);
                } else {
                    System.out.println("Error: Feld ist schon belegt.");
                }
            } else if (x == -1) {
                System.out.println("Error: diese Spalte ist schon voll.");
            } else {
                System.out.println(
                    "Error: feld außerhalb des Spielfeldes gewählt."
                );
            }
        }
    }

    @Override
    public boolean spielzugComputer(Spieler spieler) {
        return false;
    }
    /*
    import java.util.HashMap;
    import java.util.InputMismatchException;
    import java.util.Scanner;

    public class reversi {

        // ANSI escape code: https://en.wikipedia.org/wiki/ANSI_escape_code
        static final String ANSI_RED = "\033[31m"; // red output color
        static final String ANSI_BLUE = "\033[34m"; // blue output color
        static final String ANSI_RESET = "\033[0m"; // reset output color
        static final String ANSI_CLEAR = "\033[H\033[J"; // move cursor to top left corner of screen, clear screen from cursor to end of screen
        static final int PLAYER = 1;
        static final int COMPUTER = -1;
        static final int[][] factors = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

        static int passes;

        static int algPasses;
        static int algMaxDepth;
        static int[] algCurrentBaseCoords;
        static HashMap<int[], int[]> algMoveValues;

        public static void main(String[] args) {

            int[][] board = new int[8][8];
            board[3][3] = board[4][4] = PLAYER;
            board[3][4] = board[4][3] = COMPUTER;

            Scanner input = new Scanner(System.in);

            System.out.print(ANSI_CLEAR);

            while (true) {
                System.out.println("Please enter the maximum search depth.");
                try {
                    algMaxDepth = input.nextInt();
                    if (algMaxDepth > 0) {
                        break;
                    } else {
                        System.out.println(ANSI_RED + "This value is invalid." + ANSI_RESET);
                    }
                } catch (InputMismatchException exception) {
                    System.out.println(ANSI_RED + "This input is invalid." + ANSI_RESET);
                    input.next();
                }
            }

            System.out.print(ANSI_CLEAR);
            System.out.println("You are playing as " + ANSI_BLUE + "blue" + ANSI_RESET + ".");
            System.out.println();
            printBoard(board);
            System.out.println();

            while (passes < 2) {
                int x;
                int y;

                if (canPlay(PLAYER, board)) {
                    passes = 0;

                    while (true) {
                        try {
                            System.out.println("Please input the x coordinate.");
                            y = input.nextInt() - 1;

                            System.out.println("Please input the y coordinate.");
                            x = input.nextInt() - 1;

                            if (x >= 0 && x < 8 && y >= 0 && y < 8) {
                                if (canPlace(x, y, PLAYER, board)) {
                                    break;
                                } else {
                                    System.out.println(ANSI_RED + "You can't place here." + ANSI_RESET);
                                }
                            } else {
                                System.out.println(ANSI_RED + "This position is invalid." + ANSI_RESET);
                            }
                        } catch (InputMismatchException exception) {
                            System.out.println(ANSI_RED + "This input is invalid." + ANSI_RESET);
                            input.next();
                        }
                    }
                    updateBoard(x, y, PLAYER, board);
                } else {
                    passes++;
                }

                System.out.print(ANSI_CLEAR);
                System.out.println("The computer is playing...");
                System.out.println();
                printBoard(board);

                if (canPlay(COMPUTER, board)) {
                    passes = 0;

                    int[][][] fakeBoards = new int[algMaxDepth + 1][8][8];
                    fakeBoards[algMaxDepth] = deepCopy(board);

                    algMoveValues = new HashMap<>();

                    nextComputerMove(fakeBoards, algMaxDepth, COMPUTER);

                    int min = Integer.MAX_VALUE;
                    int[] minMove = new int[2];
                    for (int[] move : algMoveValues.keySet()) {
                        if (algMoveValues.get(move)[0] / algMoveValues.get(move)[1] < min) {
                            min = algMoveValues.get(move)[0] / algMoveValues.get(move)[1];
                            minMove = move;
                        }
                    }

                    updateBoard(minMove[0], minMove[1], COMPUTER, board);

                    System.out.print(ANSI_CLEAR);
                    System.out.println("The computer chose to play at " + (minMove[1] + 1) + ", " + (minMove[0] + 1));
                } else {
                    passes++;
                    System.out.print(ANSI_CLEAR);
                    System.out.println("The computer needed to pass its turn.");
                }

                System.out.println();
                printBoard(board);
                System.out.println();
            }

            System.out.print(ANSI_CLEAR);
            int[] howMany = {0, 0};
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    if (board[x][y] == 1) {
                        howMany[0]++;
                    } else if (board[x][y] == -1) {
                        howMany[1]++;
                    }
                }
            }

            switch (Integer.compare(howMany[0], howMany[1])) {
                case PLAYER: {
                    System.out.println("You won with " + howMany[0] + " points!");
                    break;
                }
                case COMPUTER: {
                    System.out.println("The computer won with " + howMany[1] + " points!");
                    break;
                }
                case 0: {
                    System.out.println("The game ended in a tie!");
                }
            }

            System.out.println();
            printBoard(board);
            System.out.println();
        }

        static void nextComputerMove(int[][][] board, int depth, int player) {
            if (algPasses == 2 || depth == 0) {
                int placementValue = 0;
                for (int x = 0; x < 8; x++) {
                    for (int y = 0; y < 8; y++) {
                        if ((x == 0 || x == 7) && (y == 0 || y == 7)) {
                            placementValue += board[depth][x][y] * 8;
                        } else if (x == 0 || x == 7 || y == 0 || y == 7) {
                            placementValue += board[depth][x][y] * 3;
                        } else {
                            placementValue += board[depth][x][y];
                        }
                    }
                }

                if ((algCurrentBaseCoords[0] == 0 || algCurrentBaseCoords[0] == 7) && (algCurrentBaseCoords[1] == 0 ||
                        algCurrentBaseCoords[1] == 7)) {
                    placementValue += board[depth][algCurrentBaseCoords[0]][algCurrentBaseCoords[1]] * 32;
                } else if (algCurrentBaseCoords[0] == 1 || algCurrentBaseCoords[0] == 6 || algCurrentBaseCoords[1] == 1 ||
                        algCurrentBaseCoords[1] == 6) {
                    placementValue -= board[depth][algCurrentBaseCoords[0]][algCurrentBaseCoords[1]] * 4;
                } else if (algCurrentBaseCoords[0] == 0 || algCurrentBaseCoords[0] == 7 || algCurrentBaseCoords[1] == 0 ||
                        algCurrentBaseCoords[1] == 7) {
                    placementValue += board[depth][algCurrentBaseCoords[0]][algCurrentBaseCoords[1]] * 12;
                } else {
                    placementValue += board[depth][algCurrentBaseCoords[0]][algCurrentBaseCoords[1]] * 4;
                }

                algMoveValues.putIfAbsent(algCurrentBaseCoords, new int[]{0, 0});
                algMoveValues.get(algCurrentBaseCoords)[0] += placementValue;
                algMoveValues.get(algCurrentBaseCoords)[1]++;

                return;
            }

            boolean placed = false;

            algPasses = 0;
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    if (canPlace(x, y, player, board[depth])) {
                        if (depth == algMaxDepth) {
                            algCurrentBaseCoords = new int[]{x, y};
                        }
                        board[depth - 1] = deepCopy(board[depth]);
                        updateBoard(x, y, -1, board[depth - 1]);
                        nextComputerMove(board, depth - 1, player * -1);
                        placed = true;
                    }
                }
            }
            if (!placed) {
                algPasses++;
                board[depth - 1] = deepCopy(board[depth]);
                nextComputerMove(board, depth - 1, player * -1);
            }
        }

        static boolean canPlay(int player, int[][] board) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    if (canPlace(x, y, player, board)) {
                        return true;
                    }
                }
            }
            return false;
        }

        static boolean canPlace(int x, int y, int player, int[][] board) {
            if (board[x][y] == 0) {
                for (int[] factor : factors) {
                    if (checkPos(x, y, player, factor, board)) {
                        return true;
                    }
                }
            }
            return false;
        }

        static boolean checkPos(int x, int y, int player, int[] factor, int[][] board) {
            if (x + factor[0] >= 0 && x + factor[0] < 8 && y + factor[1] >= 0 && y + factor[1] < 8) {
                if (board[x + factor[0]][y + factor[1]] == 0) {
                    return false;
                } else if (board[x + factor[0]][y + factor[1]] == player) {
                    return board[x][y] == player * -1;
                } else {
                    return checkPos(x + factor[0], y + factor[1], player, factor, board);
                }
            }
            return false;
        }

        static void updateBoard(int x, int y, int player, int[][] board) {
            for (int[] factor : factors) {
                updatePos(x, y, player, factor, board);
            }
        }

        static void updatePos(int x, int y, int player, int[] factor, int[][] board) {
            if (x + factor[0] >= 0 && x + factor[0] < 8 && y + factor[1] >= 0 && y + factor[1] < 8) {
                if (board[x + factor[0]][y + factor[1]] == player * -1) {
                    updatePos(x + factor[0], y + factor[1], player, factor, board);
                }
                if (board[x + factor[0]][y + factor[1]] == player) {
                    board[x][y] = player;
                }
            }
        }

        static void printBoard(int[][] board) {
            System.out.println("    1 2 3 4 5 6 7 8");
            for (int x = 0; x < 8; x++) {
                System.out.print("  " + (x + 1) + " ");
                for (int y = 0; y < 8; y++) {
                    if (board[x][y] == 1) {
                        System.out.print(ANSI_BLUE + "◉ " + ANSI_RESET);
                    } else if (board[x][y] == -1) {
                        System.out.print(ANSI_RED + "◉ " + ANSI_RESET);
                    } else {
                        System.out.print("○ ");
                    }
                }
                System.out.println();
            }
        }

        static int[][] deepCopy(int[][] array) {
            int[][] deepCopy = new int[array.length][array[0].length];
            for (int i = 0; i < array.length; i++) {
                System.arraycopy(array[i], 0, deepCopy[i], 0, 8);
            }
            return deepCopy;
        }
    }
    */
}
