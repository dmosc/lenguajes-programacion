
import java.util.logging.Level;
import java.util.logging.Logger;

public class Consumer extends Thread {

    Buffer buffer;
    static int sleep;

    Consumer(Buffer _buffer) {
        buffer = _buffer;
    }

    @Override
    public void run() {
        while (!Buffer.stop) {
            Operation product = this.buffer.consume();
            if (product != null) {
                Buffer.print("C-" + this.getId() + " processed: " + product.formatted() + " -> " + product.solve());
            }

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
