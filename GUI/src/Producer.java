


import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Producer extends Thread {
    Buffer buffer;
    
    Producer(Buffer buffer, int tiempoE, int rangoMe, int rangoMa) {
        this.buffer = buffer;
    }
    
    @Override
    public void run() {
        System.out.println("Running Producer...");
        Random r = new Random(System.currentTimeMillis());
        Operation product;
        
        for(int i=0 ; i<5 ; i++) {
            product = new Operation(r.nextInt(4), r.nextInt(10), r.nextInt(10));
            this.buffer.produce(product);
            //System.out.println("Producer produced: " + product);
            Buffer.print("Producer produced: " + "(" + product.cType + " " + product.val1 + " " + product.val2 + ")");
            
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Producer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
}
