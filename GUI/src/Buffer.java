


import java.util.logging.Level;
import java.util.logging.Logger;

public class Buffer {
    
    private Operation[] buffer = new Operation[10]; 
    
    Buffer() {
        buffer[0] = new Operation();
        this.buffer[0].flag = -1;
    }
    
    synchronized Operation consume() {
        Operation product = new Operation();
        
        if(this.buffer[0].flag == -1) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        product = this.buffer[0];
        this.buffer[0].flag = -1;
        notify();
        
        return product;
    }
    
    synchronized void produce(Operation product) {
        if(this.buffer[0].flag != -1) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Buffer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.buffer[0] = product;
        
        notify();
    }
    
    static int count = 1;
    synchronized static void print(String string) {
        System.out.print(count++ + " ");
        System.out.println(string);
    }
    
}
