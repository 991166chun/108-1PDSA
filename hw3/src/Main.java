
import java.io.File;
import java.util.Iterator;
import java.util.Scanner;

import java.io.FileNotFoundException;
import java.util.NoSuchElementException;


public class Main<Item> implements Iterable<Item> {
    
    private Object[] dequeA;
    public int arraysize;
    public int start;
    public int end;
    
    public Main() { // construct an empty deque
        this.dequeA = new Object[200000];
        
        start = 100000;
        end = 100000;
    }
    
    public boolean isEmpty() {// is the deque empty?
        return (end-start) == 0;
    }
    
    public int size() {// return the number of items on the deque
        return (end-start);
    }
    
    public void addFirst(Item item){ // add the item to the end
        if (item == null)
                throw new NullPointerException();

        start--;
        dequeA[start] = item;
    }
    
    public void addLast(Item item) {// add the item to the front
        if (item == null)
            throw new NullPointerException();
        
        end++;
        dequeA[end-1] = item;
    }
    
    public Item removeFirst(){ // remove and return the item from the front
        if (isEmpty())
            throw new NoSuchElementException();
        
        Item reItem = (Item) dequeA[start];
        dequeA[start] = null;
        start++;
        
        return reItem;
    }
    
    public Item removeLast(){ // remove and return the item from the end
        if(isEmpty())
            throw new NoSuchElementException();
        
        Item reItem = (Item) dequeA[end-1];
        dequeA[end-1] = null;
        end--;
        
        return reItem;
    }
    
    public Item peekFirst(){ //return the fist item of the deque
        if (isEmpty())
            return  null;
        
        return (Item) dequeA[start];
        
    }
    
    public Item peekLast(){ //return the last item of the deque
        if (isEmpty())
            return  null;
        
        return (Item) dequeA[end-1];
        
    }
    
    public Item peekId(int i){
        if(isEmpty())
            throw new NoSuchElementException();
        
        i = i + start;
        if (i > (end-1))
            throw new NoSuchElementException();
        
        return (Item) dequeA[i];
    }
    
    public void printDQ(){ 
        if (!isEmpty()){
            System.out.print("[");
            for (int i = start; i < end; i++){
                System.out.print(dequeA[i]);
                System.out.print(", ");
            }
            System.out.print("] and max is ");
        }
    }
    
    @Override
    public Iterator<Item> iterator(){ // return an iterator over items in order from front to end
        Iterator<Item> iter = new Iterator<Item>() {
            int idx = start;
            
            @Override
            public boolean hasNext() {
                return idx < end;
            }
            
            @Override
            public Item next() {
                if (!hasNext())
                    throw new NoSuchElementException();
                
                idx++;
                return (Item) dequeA[idx-1];
            }
            
            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return iter;
    }
    
    public static void main (String[] args) throws FileNotFoundException{

        File file = new File(args[0]) ;   // file name assigned

        //File file = new File("test.in") ; // filename for local test (delete this part when uploading to onlinejudge)
        
        // file reading 
        Scanner in = new Scanner(file);
        
        Main DQ ;
        int M ;
        int N ;
        int newone;
        
        //Read int k
	int k = Integer.parseInt(in.nextLine());
        
        //System.out.println(k);
        
        // implement sliding window problem

        DQ = new Main();
        
        
        M = Integer.parseInt(in.nextLine());
        DQ.addLast(M);
        
        while ( DQ.size() < k ){
            newone = Integer.parseInt(in.next());
            DQ.addLast(newone);
            
            if (M < newone)
                M = newone;
        }
        
        //DQ.printDQ();
        System.out.println(M);
        
        while (in.hasNext()){
            
            newone = Integer.parseInt(in.next());
            
            if (DQ.peekFirst().equals(M) && M > newone){
                //System.out.println("firstmax");
                /*
                Iterator DQit = DQ.iterator();
                M = (Integer) DQit.next();
                while (DQit.hasNext()){
                    N = (Integer) DQit.next();
                    if (M < N)
                        M = N;
                }        */
                
                M = (Integer) DQ.peekId(1);
                for (int i = 2; i < DQ.size() ; i++){
                if (M < (Integer) DQ.peekId(i))
                M = (Integer) DQ.peekId(i);
                }
            }
            
            DQ.removeFirst();
            DQ.addLast(newone);

            if (M < newone)
                M = newone;

            //DQ.printDQ();
            System.out.println(M);

        }
    }
}
