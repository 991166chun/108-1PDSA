/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw3;

import java.util.*;

/**
 *
 * @author Chun
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    /*
    private Node first = null;
    
    private class Node{
        String item;
        Node next;
    }
    */
    private int count = 0;
    private int i = 1;
    
    private Stack<Integer> adjLists[];
    private HashMap<String,Integer> pairs;
    
    
    public Main(int n){
        
        // constructor, n is the number of proteins in 
        //the network (the adjacency list)
        pairs = new HashMap<>();
        adjLists = new Stack[n];
        for (int j = 0; j < n; j++)
            adjLists[j] = new Stack();
        
    }
    
    public void add(String a, String b) throws IndexOutOfBoundsException{
        // add a pair of proteins as an edge of the network
        // throw an `IndexOutOfBoundsException` if adding
        // more than n proteins
        int src, des;
        if (pairs.containsKey(a)){
            src = pairs.get(a);
        }else{
            src = i;
            pairs.put(a, src);
            i++;
        }

        if (pairs.containsKey(b)){
            des = pairs.get(b);
        }else{
            des = i;
            pairs.put(b, des);
            i++;
        }
        
        adjLists[src].add(des);
    
        count++;
    };
    
    public int size(){
        // return the number of unique edges of the network
        return count;
    };
    
    public String[] neighbors(String a) throws IllegalArgumentException{
    
        // return all the neighbors of a protein as an array, 
        // where the proteins in the array are sorted alphabetically
        // throw an `IllegalArgumentException` if protein not found
        int src;
        String[] 
        src = pairs.get(a);
        
        String[] out = new String[1];
        out[0] = a;
        return out;
    };
    
    public String[] interactions(String a, int b){
     
        // return all the interacting proteins 
        // (including both direct or indirect interactions)
        // of a protein through at most k edges, where the proteins in the array
        // are sorted alphabetically. The parameter k could be 0 or any positive
        // integers.
        // throw an `IllegalArgumentException` if protein not found
        String[] out = new String[1];
        out[0] = a;
        return out;
    };
   
}
