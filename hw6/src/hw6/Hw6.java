/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw6;

import edu.princeton.cs.algs4.Queue;

/**
 *
 * @author Chun
 */
public class Hw6 {
    
    
    
    public static class Main <Key extends Comparable<Key>, Value> {
        
        private Node root;
        private static final boolean RED = true;
        private static final boolean BLACK = false;
        
        public Main() {
            
        }
        
        private class Node {
            private Key lo;
            private Key hi;
            private int max;
            private boolean color;
            private Value val;
            private Node left, right;
            
            public Node (Key lo, Key hi, Value val) {
                this.lo = lo;
                this.hi = hi;
                this.max = (Integer) hi;
                
                this.val = val;
                this.left = null;
                this.right = null;
            }
            
            public boolean intersect(Key lo, Key hi) {
                
                if (this.hi.compareTo(lo) < 0) return false;
                if (this.lo.compareTo(hi) > 0) return false;
                
                return true;
            }
            
            public boolean contain(Key lo, Key hi) {
                
                return ( (this.lo.compareTo(lo) <= 0) && (this.hi.compareTo(hi) >= 0)); 
            }
            
            public boolean containedBy(Key lo, Key hi) {
                
                return ((this.lo.compareTo(lo)) >= 0 && (this.hi.compareTo(hi) <= 0) ); 
            }
                    
        }
        
        public void put(Key lo, Key hi, Value val){
            root = put(root, lo, hi, val);
        }
        
        private Node put(Node x, Key lo, Key hi, Value val) {
            
            if (x == null) return new Node(lo, hi, val);
            
            int cmp = lo.compareTo(x.lo);
            if (cmp < 0)
                x.left = put(x.left, lo, hi, val);
            else if (cmp > 0)
                x.right = put(x.right, lo, hi, val);
            else if (cmp == 0 && hi.compareTo(x.hi) == 0)
                x.val = val;
                
            subMax(x);
            
            return x;     
        }
        
        private void subMax(Node x) {
            
            if (x == null) return;
            
            int Lmax = Integer.MIN_VALUE;
            int Rmax = Integer.MIN_VALUE;
            
            if (x.left != null) Lmax = x.left.max;
            if (x.right != null) Lmax = x.right.max;
            
            x.max = Math.max(x.max, Math.max(Lmax, Rmax));
        }
        
        public Value get(Key lo, Key hi) {
         // value paired with given interval
            Value gotVal;
            gotVal = get(root, lo, hi);
            return gotVal;
        }
        
        private Value get(Node x, Key lo, Key hi) {
            
            if (x == null)         return null;
            
            int cmp = lo.compareTo(x.lo);
            if (cmp < 0)           return get(x.left, lo, hi);
            else if (cmp > 0)      return get(x.right, lo, hi);
            
            int cmphi = hi.compareTo(x.hi);
            if (cmphi < 0)         return get(x.left, lo, hi);
            else if (cmphi > 0)    return get(x.right, lo, hi);
            
            return x.val;
            }
        
        
        public void delete(Key lo, Key hi) {
             // delete the given interval
             root = deleteVal(root, lo, hi);
        }
        
        private Node deleteVal(Node x, Key lo, Key hi) {
            
            if (x == null)          return null;
            int cmp = lo.compareTo(x.lo);
            if (cmp < 0)            return deleteVal(x.left, lo, hi);
            else if (cmp > 0)       return deleteVal(x.right, lo, hi);
            else if (cmp == 0 && hi.compareTo(x.hi) == 0)   x.val = null;
            
            return x;
        }
        
        
        
        public Iterable<Value> intersects(Key lo, Key hi) {
             // all intervals that intersect the given interval
            Queue<Value> q = new Queue<>();
            intersects(lo, hi, root, q);
            return q;
        }
        
        private boolean intersects(Key lo, Key hi,Node x, Queue<Value> q) {
            
            boolean inter = false;
            boolean interAtLeft = false;
            boolean interAtRight = false;
            
            if (x == null)                  return false;
            if (x.intersect(lo, hi)) {
                if (x.val != null) {
                    q.enqueue(x.val);
                    inter = true;
                }
            }
            if (x.left != null && x.left.max >= (Integer) lo) {
                interAtLeft = intersects(lo, hi, x.left, q);
            }
            if (interAtLeft || x.left == null || x.left.max < (Integer) lo) {
                interAtRight = intersects(lo, hi, x.right, q);
            }
            return (inter || interAtLeft || interAtRight);
            
        }
        
        public Iterable<Value> contains(Key lo, Key hi) {
             // all intervals that contain the given interval
             Queue<Value> q2 = new Queue<>();
             contains(lo, hi, root, q2);
             return q2;
        }
        
        private boolean contains(Key lo, Key hi,Node x, Queue<Value> q) {
            boolean contain = false;
            boolean contnLeft = false;
            boolean contnRight = false;
            
            if (x == null)              return false;
            if (x.contain(lo, hi)) {
                if (x.val != null) {
                     /*
                    System.out.print("Node ");
                    System.out.print( x.lo);
                    System.out.print( ",");
                    System.out.print( x.hi);
                    System.out.print( " contain ");
                    System.out.print( lo);
                    System.out.print( ",");
                    System.out.println( hi);
                    */
                    q.enqueue(x.val);
                    contain = true;
                }
            }
            if (x.left != null && x.left.max >= (Integer) lo) {
                contnLeft = contains(lo, hi, x.left, q);
            }
            if (x.left == null || contnLeft || x.left.max < (Integer) lo ) {
                contnRight = contains(lo, hi, x.right, q);
            }
            return (contain || contnLeft || contnRight);
        }
        
        public Iterable<Value> containedBy(Key lo, Key hi) {
             // all intervals that are contained by the given interval
             Queue<Value> q3 = new Queue<>();
             containedBy(lo, hi, root, q3);
             return q3;
        }
        
        private boolean containedBy(Key lo, Key hi,Node x, Queue<Value> q) {
            boolean contained = false;
            boolean contedLeft = false;
            boolean contedRight = false;
            
            if (x == null)              return false;
            if (x.containedBy(lo, hi)) {
                if (x.val != null) {
                    /*
                    System.out.print("Node ");
                    System.out.print( x.lo);
                    System.out.print( ",");
                    System.out.print( x.hi);
                    System.out.print( " containedby ");
                    System.out.print( lo);
                    System.out.print( ",");
                    System.out.println( hi);
                    */
                    q.enqueue(x.val);
                    contained = true;
                }
            }
            
            if (x.left != null && x.left.max >= (Integer) lo) {
                contedLeft = containedBy(lo, hi, x.left, q);
            }
            if (x.left == null || contedLeft || x.left.max < (Integer) lo ) {
                contedRight = containedBy(lo, hi, x.right, q);
            }
            
            return (contained || contedLeft || contedRight);
        }
        
        public Iterable<Value> inorderTraversal() {
             // inorder traversal of the tree
             Queue<Value> q4 = new Queue<>(); 
             inOrder(root, q4);
             return  q4;
        }
        
        private void inOrder(Node x, Queue<Value> q) {
            if (x == null) return;
            inOrder(x.left, q);
            if (x.val != null) {
                q.enqueue(x.val);
            }
            inOrder(x.right, q);
        }
        
        public Iterable<Integer> inorderMax() {
             // inorder traversal of the tree
             Queue<Integer> q5 = new Queue<>(); 
             inOrderM(root, q5);
             return  q5;
        }
        
        private void inOrderM(Node x, Queue<Integer> q) {
            if (x == null) return;
            inOrderM(x.left, q);
            if (x.val != null) {
                q.enqueue(x.max);
            }
            inOrderM(x.right, q);
        }
        
    } 
    
    
    
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        Main main = new Main();
        
        main.put(0, 3, "ACAC");
        main.put(2, 7, "TCAATG");
        main.put(5, 6, "AT");
/*
        main.put(17, 19, "A");
        main.put(5 , 8 , "B");
        main.put(21, 24, "C");
        main.put(4 , 9 , "D");
        main.put(15, 18, "E");
        main.put(7 , 10, "F");
        main.put(16, 22, "G");
        
        */
        System.out.println("intersects");
        for (Object s : main.intersects(1, 4)){
            System.out.println(s);
        }

        System.out.println("contains");
        for (Object s : main.contains(3,5)){
            System.out.println(s);
        }

        System.out.println("containedBy");
        for (Object s : main.containedBy(0, 6)){
            System.out.println(s);
        }
        /*
        System.out.println("traversal");
        for (Object s : main.inorderTraversal()){
            System.out.println(s);
        }
        
        System.out.println("traversalMax");
        for (Object s : main.inorderMax()){
            System.out.println(s);
        }
        */
    }
    
}
