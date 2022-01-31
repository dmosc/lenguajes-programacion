


import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Buffer {
    private final Lock _mutex = new ReentrantLock(true);
    Operation buffer[];
    int next;
    
    Buffer(int size) {
        buffer = new Operation[size];
        next = 0;
    }
    
    boolean isEmpty() {
        return next == 0;
    }
    
    boolean isFull() {
        return next + 1 >= buffer.length;
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
            product = buffer[--next];
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
            buffer[next++] = product;
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
