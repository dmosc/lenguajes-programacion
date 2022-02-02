
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.JProgressBar;
import javax.swing.table.DefaultTableModel;

public class Buffer {

    private final Lock _mutex = new ReentrantLock(true);
    Operation buffer[]; // circular queue
    int left, right, next;
    static boolean stop = false;
    private DefaultTableModel model;
    private javax.swing.JProgressBar jProgressBar;
    int min_bar, max_bar;

    Buffer(int size, javax.swing.JTable _jTable, javax.swing.JProgressBar _jProgressBar) {
        buffer = new Operation[size];
        left = 0;
        right = 0;
        model = (DefaultTableModel) _jTable.getModel();
        jProgressBar = _jProgressBar;
        min_bar = jProgressBar.getMinimum();
        max_bar = jProgressBar.getMaximum();
        next = 0;
    }

    /*
     * boolean isEmpty() {
     * return left == right;
     * }
     */
    boolean isEmpty() {
        return next == 0;
    }

    boolean isFull() {
        return next + 1 >= buffer.length;
    }

    /*
     * boolean isFull() {
     * return Math.abs(right - left) == 1 || (left == 0 && right == buffer.length -
     * 1) || (left == buffer.length - 1 && right == 0);
     * }
     */
    synchronized Operation consume() {
        Operation product = null;

        while (isEmpty()) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        _mutex.lock();
        try {
            product = buffer[--next];
            // product = buffer[left];
            // left = (left + 1) % buffer.length;
            model.removeRow(0);
            jProgressBar.setValue((int) (100 * model.getRowCount() / (buffer.length)));
        } finally {
            _mutex.unlock();
            notify();
        }
        return product;
    }

    synchronized void produce(Operation product, String id) {
        while (isFull()) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        _mutex.lock();
        try {
            buffer[next++] = product;
            /*
             * System.out.println("------------------------------");
             * for (int i = 0; i < buffer.length; i++) {
             * if (buffer[i] != null) {
             * System.out.println(buffer[i].formatted());
             * }
             * }
             */
            // buffer[right] = product;
            // if (!isFull()) {
            // right = (right + 1) % buffer.length;
            // }
            String[] values = { id, product.formatted() };
            model.addRow(values);
            jProgressBar.setValue((int) (100 * model.getRowCount() / buffer.length));
        } finally {
            _mutex.unlock();
            notify();
        }
    }

    static int count = 1;

    synchronized static void print(String string) {
        System.out.print(count++ + " ");
        System.out.println(string);
    }

}
