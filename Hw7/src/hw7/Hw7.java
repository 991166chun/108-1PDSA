/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw7;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.princeton.cs.algs4.Point2D;

public class Hw7 {
    
    public static void main(String[] args) throws FileNotFoundException {
        // TODO code application logic here
        boolean test = true;
        File file;
        
        //test = false;
        
        if (test) 
            file = new File("test.in") ; // filename for local test (delete this part when uploading to onlinejudge)
        else
            file = new File(args[0]) ;// file name assigned
        
        Scanner in = new Scanner(file);
        
        int N = Integer.parseInt(in.nextLine());
        
        double x, y;
        
        String[] point = new String[2];
      
        Point2D[] points = new Point2D[N];
        
        for (int i = 0; i < N ; i++) {
            point = in.nextLine().split(" ");
            x = Double.parseDouble(point[0]);
            y = Double.parseDouble(point[1]);
            points[i] = new Point2D(x, y);
        }
        
        Main main = new Main(points); // point2Ds is an array of Point2D
        
        
        int[] clusters0 = new int[1];
        clusters0 = main.cluster(1); // the tree would be split by the dash line in fugure2 
        for (int w: clusters0){
            System.out.println(w);
        }
        
        int[] clusters = new int[3];
        clusters = main.cluster(3); // the tree would be split by the dash line in fugure2 
        for (int w: clusters){
            System.out.println(w);
        }
        
        int[] clusters2 = new int[4];
        
        clusters2 = main.cluster(4); // the tree would be split by the dash line in fugure2 
        for (int w: clusters2){
            System.out.println(w);
        }
                 // return [2,2,1]
    }
    
}
