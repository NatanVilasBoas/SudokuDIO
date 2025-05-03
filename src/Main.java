import com.sudoku.model.Board;
import com.sudoku.model.Field;
import com.sudoku.model.GameStatus;
import com.sudoku.utils.BoardTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class Main {

    private final static Scanner scanner = new Scanner(System.in);

    private static Board board;

    private final static int BOARD_LIMIT = 9;

    public static void main(String[] args) {
        final Map<String, String> positions = Stream.of(args)
                .collect(Collectors.toMap(
                        k -> k.split(";")[0],
                        v -> v.split(";")[1]
                ));
        int option = -1;

        while (true) {
            System.out.println("Selecione uma das opções a seguir");
            System.out.println("1 - Iniciar um novo Jogo");
            System.out.println("2 - Colocar um novo número");
            System.out.println("3 - Remover um número");
            System.out.println("4 - Visualizar jogo atual");
            System.out.println("5 - Verificar status do jogo");
            System.out.println("6 - limpar jogo");
            System.out.println("7 - Finalizar jogo");
            System.out.println("8 - Sair");

            option = scanner.nextInt();

            switch (option) {
                case 1 -> startGame(positions);
                case 2 -> insertNumber();
                case 3 -> removeNumber();
                case 4 -> showCurrentGame();
                case 5 -> showGameStatus();
                case 6 -> clearGame();
                case 7 -> finishGame();
                case 8 -> System.exit(0);
                default -> System.out.println("Opção inválida, selecione uma das opções do menu");
            }
        }


    }

    private static void startGame(Map<String, String> positions) {
        if (nonNull(board)) {
            System.out.println("O jogo já foi iniciado");
            return;
        }

        List<List<Field>> fields = new ArrayList<>();
        for (int i = 0; i < BOARD_LIMIT; i++) {
            fields.add(new ArrayList<>());
            for (int j = 0; j < BOARD_LIMIT; j++) {
                String positionConfig = positions.get("%s, %s".formatted(i, j));
                int expected = Integer.parseInt(positionConfig.split(",")[0]);
                boolean fixed = Boolean.parseBoolean(positionConfig.split(",")[1]);
                Field currentField = new Field(expected, fixed);
                fields.get(i).add(currentField);
            }
        }

        board = new Board(fields);
        System.out.println("O jogo começou");
    }

    private static void insertNumber() {
        if (isNull(board)) {
            System.out.println("Jogo ainda não iniciado");
            return;
        }

        System.out.println("Informe a coluna em que o número será inserido");
        int col = getValidNumber(0, 8);
        System.out.println("Informe a linha em que o número será inserido");
        int row = getValidNumber(0, 8);
        System.out.printf("Informe o núnero que será inserido na posição [%s,%s]\n", col, row);
        int value = getValidNumber(1, 9);
        if (!board.changeValue(col, row, value)) {
            System.out.printf("A posição [%s, %s] tem um valor fixo\n", col, row);
        }
    }

    private static void removeNumber() {
        if (isNull(board)) {
            System.out.println("Jogo ainda não iniciado");
            return;
        }

        System.out.println("Informe a coluna em que o número será inserido");
        int col = getValidNumber(0, 8);
        System.out.println("Informe a linha em que o número será inserido");
        int row = getValidNumber(0, 8);
        if (!board.clearField(col, row)) {
            System.out.printf("A posição [%s, %s] tem um valor fixo\n", col, row);
        }
    }

    private static void showGameStatus() {
        System.out.printf("O jogo atualmente se encontra no status %s\n", board.getStatus().getLabel());

        if (board.hasErrors()) {
            System.out.println("O jogo contém erros");
        } else {
            System.out.println("O jogo não contém erros");
        }
    }

    private static void showCurrentGame() {
        if (isNull(board)) {
            System.out.println("Jogo ainda não iniciado");
            return;
        }

        Object[] args = new Object[81];
        int argPos = 0;
        for (int i = 0; i < BOARD_LIMIT; i++) {
            for (List<Field> col : board.getFields()) {
                args[argPos++] = " " + ((isNull(col.get(i).getActual())) ? " " : col.get(i).getActual());
            }
        }
        System.out.println("Seu jogo está da seguinte forma");
        System.out.printf(BoardTemplate.BOARD_TEMPLATE + "\n", args);
    }

    private static void finishGame() {
        if (isNull(board)) {
            System.out.println("Jogo ainda não iniciado");
            return;
        }

        if(board.gameIsFinished()){
            System.out.println("Parabésn você concluiu o jogo!");
            showCurrentGame();
            board = null;
        } else if (board.hasErrors()){
            System.out.println("Seu jogo possui erros, por favor, corrija-os.");
        } else {
            System.out.println("Seu jogo possui campos não preenchidos.");
        }
    }

    private static void clearGame() {
        if (isNull(board)) {
            System.out.println("Jogo ainda não iniciado");
            return;
        }

        System.out.println("Tem certeza que deseja limpar o jogo? Você perderá todo seu progresso. Digite S/N");
        String confirm = scanner.next();
        while (!confirm.equalsIgnoreCase("s") || !confirm.equalsIgnoreCase("n")) {
            System.out.println("Digite S ou N");
            confirm = scanner.next();
        }

        if(confirm.equalsIgnoreCase("s")){
            board.reset();
        }
    }


    private static int getValidNumber(final int min, final int max) {
        Integer current = scanner.nextInt();
        while (current < min || current > max) {
            System.out.printf("Informe um número entre %s e %s\n", min, max);
            current = scanner.nextInt();
        }
        return current;
    }

}