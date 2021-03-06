
import java.util.Random;

/**
 *
 * @author(s) Gustavo, Oscar
 */
enum OpType {
    sum, subtraction, multiplication, division
};

public class Operation {

    static int min;
    static int max;
    int leftOperand, rightOperand;
    OpType operator;

    Operation() {
        Random random = new Random();

        leftOperand = (int) (Math.random() * (max - min + 1) + min);
        rightOperand = (int) (Math.random() * (max - min + 1) + min);

        operator = OpType.values()[random.nextInt(OpType.values().length)];
    }

    int solve() {
        switch (operator) {
            case sum:
                return leftOperand + rightOperand;
            case subtraction:
                return leftOperand - rightOperand;
            case multiplication:
                return leftOperand * rightOperand;
            case division:
                if (rightOperand == 0) {
                    return 0;
                }
                return leftOperand / rightOperand;
            default:
                return 0;
        }
    }

    String formatted() {
        char type;
        switch (operator) {
            case sum:
                type = '+';
                break;
            case subtraction:
                type = '-';
                break;
            case multiplication:
                type = '*';
                break;
            case division:
                type = '/';
                break;
            default:
                type = '_';
                break;
        }

        return "(" + type + " " + leftOperand + " " + rightOperand + ")";
    }
}
