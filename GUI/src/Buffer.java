
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Buffer {

    private final Lock _mutex = new ReentrantLock(true);
    Operation buffer[];
    int left, right;
    static boolean stop = false;

    Buffer(int size) {
        buffer = new Operation[size];
        left = 0;
        right = 0;
    }

    boolean isEmpty() {
        return Math.abs(right - left) == 0;
    }

    boolean isFull() {
        return Math.abs(right - left) == 1 || (left == 0 && right == buffer.length - 1) || (left == buffer.length - 1 && right == 0);
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
            product = buffer[left];
            left = (left + 1) % buffer.length;
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
            buffer[right] = product;
            right = (right + 1) % buffer.length;
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
