


import java.util.logging.Level;
import java.util.logging.Logger;

public class Consumer extends Thread {
    Buffer buffer;
    static int sleep;
    static int i=1;
    boolean cStop=false;
    
    public static void detener(){
       i=0;
    }
    
    Consumer(Buffer _buffer) {
        buffer = _buffer;
    }
    
    @Override
    public void run() {
        if(i==0){
                cStop=true;
            }
        while (!cStop) {
            if(i==0){
                cStop=true;
                //System.out.print("DETENERC");
                break;
            }
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
