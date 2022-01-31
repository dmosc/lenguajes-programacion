


import java.util.logging.Level;
import java.util.logging.Logger;

public class Producer extends Thread {
    Buffer buffer;
    static int sleep;
    static int i=1;
    boolean pStop=false;
    
    public static void detener(){
       i=0;
    }
    
    Producer(Buffer _buffer) {
        buffer = _buffer;
    }
    
    @Override
    public void run() {
        if(i==0){
                pStop=true;
            }
        while (!pStop) {
            if(i==0){
                pStop=true;
                //System.out.print("DETENERP");
                break;
            }    
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
