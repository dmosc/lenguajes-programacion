
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.JProgressBar;
import javax.swing.table.DefaultTableModel;

public class Buffer {

    private final Lock _mutex = new ReentrantLock(true);
    Operation buffer[];
    int left, right;
    static boolean stop = false;
    private DefaultTableModel model;
    private javax.swing.JProgressBar jProgressBar;
    int min_bar, max_bar;
    private DefaultTableModel model2;

    Buffer(int size, javax.swing.JTable _jTable, javax.swing.JProgressBar _jProgressBar, javax.swing.JTable _jTable2) {
        buffer = new Operation[size + 1];
        left = 0;
        right = 0;
        model = (DefaultTableModel) _jTable.getModel();
        model2 = (DefaultTableModel) _jTable2.getModel();
        jProgressBar = _jProgressBar;
        min_bar = jProgressBar.getMinimum();
        max_bar = jProgressBar.getMaximum();
    }

    boolean isEmpty() {
        return left == right;
    }

    boolean isFull() {
        return Math.abs(right - left) == 1 || (left == 0 && right == buffer.length - 1) || (left == buffer.length - 1 && right == 0);
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
            product = buffer[left];
            left = (left + 1) % buffer.length;
            model.removeRow(0);
            // double value = model.getRowCount() + model2.getRowCount() != 0 ? model2.getRowCount() / (model.getRowCount() + model2.getRowCount()) : 0;
            // System.out.println(model.getRowCount() + " " + model2.getRowCount());
            // jProgressBar.setValue((int) (value * max_bar));
        } finally {
            _mutex.unlock();
            notify();
        }
        return product;
    }

    synchronized void produce(Operation product) {
        while (isFull()) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        _mutex.lock();
        try {
            buffer[right] = product;
            if (!isFull()) {
                right = (right + 1) % buffer.length;
            }
            model.addRow(product.procPorHacer());
            // double value = model.getRowCount() + model2.getRowCount() != 0 ? model2.getRowCount() / (model.getRowCount() + model2.getRowCount()) : 0;
            // System.out.println(model.getRowCount() + " " + model2.getRowCount());
            // jProgressBar.setValue((int) (value * max_bar));
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
