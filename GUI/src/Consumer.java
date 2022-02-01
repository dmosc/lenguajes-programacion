
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Consumer extends Thread {

    Buffer buffer;
    static int sleep;
    private DefaultTableModel model;

    Consumer(Buffer _buffer, javax.swing.JTable _jTable) {
        buffer = _buffer;
        model = (DefaultTableModel) _jTable.getModel();
    }

    @Override
    public void run() {
        while (!Buffer.stop) {
            Operation product = this.buffer.consume();
            if (product != null) {
                model.addRow(product.procRealizados());
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
