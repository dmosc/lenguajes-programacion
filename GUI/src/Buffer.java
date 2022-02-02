
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;

public class Buffer {

    private final Lock _mutex = new ReentrantLock(true);
    Operation buffer[];
    int next;
    static boolean stop = false;
    private DefaultTableModel model;
    private javax.swing.JProgressBar jProgressBar;
    int min_bar, max_bar;

    Buffer(int size, javax.swing.JTable _jTable, javax.swing.JProgressBar _jProgressBar) {
        buffer = new Operation[size];
        next = 0;
        model = (DefaultTableModel) _jTable.getModel();
        jProgressBar = _jProgressBar;
        min_bar = jProgressBar.getMinimum();
        max_bar = jProgressBar.getMaximum();
    }

    boolean isEmpty() {
        return next == 0;
    }

    boolean isFull() {
        return next + 1 >= buffer.length;
    }

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
            String[] values = {id, product.formatted()};
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
