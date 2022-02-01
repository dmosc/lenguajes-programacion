
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class Consumer extends Thread {

    Buffer buffer;
    static int sleep;
    private DefaultTableModel model;
    private javax.swing.JSpinner jSpinner;

    Consumer(Buffer _buffer, javax.swing.JTable _jTable, javax.swing.JSpinner _jSpinner) {
        buffer = _buffer;
        model = (DefaultTableModel) _jTable.getModel();
        jSpinner = _jSpinner;
    }

    @Override
    public void run() {
        while (!Buffer.stop) {
            Operation product = this.buffer.consume();
            if (product != null) {
                String[] values = {Long.toString(this.getId()), product.formatted(), Integer.toString(product.solve())};
                model.addRow(values);
                jSpinner.setValue(model.getRowCount());
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
