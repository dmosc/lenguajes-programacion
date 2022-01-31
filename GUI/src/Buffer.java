


import java.util.logging.Level;
import java.util.logging.Logger;

public class Buffer {
    
    private Operation buffer = new Operation(); 
    
    Buffer() {
        this.buffer.flag = -1;
    }
    
    synchronized Operation consume() {
        Operation product = new Operation();
        
        if(this.buffer.flag == -1) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        product = this.buffer;
        this.buffer.flag = -1;
        notify();
        
        return product;
    }
    
    synchronized void produce(Operation product) {
        if(this.buffer.flag != -1) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.buffer = product;
        
        notify();
    }
    
    static int count = 1;
    synchronized static void print(String string) {
        System.out.print(count++ + " ");
        System.out.println(string);
    }
    
}
