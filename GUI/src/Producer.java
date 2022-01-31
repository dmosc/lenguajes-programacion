


import java.util.logging.Level;
import java.util.logging.Logger;

public class Producer extends Thread {
    Buffer buffer;
    static int sleep;
    
    Producer(Buffer _buffer) {
        buffer = _buffer;
    }
    
    @Override
    public void run() {
        while (true) {        
            Operation product = new Operation();
            this.buffer.produce(product);
            Buffer.print("P-" + this.getId() + " yielded: " + product.formatted());

            try {
                Thread.sleep(sleep);
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
