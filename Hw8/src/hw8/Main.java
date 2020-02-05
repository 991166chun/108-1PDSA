/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw8;

import edu.princeton.cs.algs4.DepthFirstDirectedPaths;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.RedBlackBST;


public class Main {
    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

    public static class CriPath {

        private MinPQ<Pair> pq;        
        private final Point2D[] points;
        private Digraph dg;
        private int N;
        private int start;
        private int target;
        private DepthFirstDirectedPaths dfs;
                

        CriPath(Point2D[] in_points) {
            
            points = in_points.clone();
            N = in_points.length;
            dg = new Digraph(N);
            pq = new MinPQ<>();
            // create clustering tree
            start = 0;
            target = 0;
            
            double maxXY = points[0].x() + points[0].y();
            double minXY = points[0].x() + points[0].y();
            
            for (int i = 0; i < N-1; i++) {
            
                double newXY = points[i+1].x() + points[i+1].y();
                
                if (newXY > maxXY) {
                    maxXY = newXY;
                    target = i+1;
                }
                if (newXY < minXY) {
                    minXY = newXY;
                    start = i+1;
                }
                
                for (int j = i+1; j < N; j++){
                    double dis = points[i].distanceTo(points[j]);
                    
                    if (points[i].x() > points[j].x() && points[i].y() > points[j].y() ) 
                        pq.insert(new Pair(dis, i, j));
                    if (points[i].x() < points[j].x() && points[i].y() < points[j].y() ) 
                        pq.insert(new Pair(dis, j, i)); 
                    
                }
            }
            
            // make graph and judge if path is exist
            }
        
        public double criticalPath() {
            
            Pair P = pq.delMin();
            dg.addEdge(P.b, P.a);
            
            dfs = new DepthFirstDirectedPaths(dg, start);

            while(!dfs.hasPathTo(target)) {
                
                P = pq.delMin();
                dg.addEdge(P.b, P.a);
                dfs = new DepthFirstDirectedPaths(dg, start);

            }
            
            return P.dis;
        }
        
        private void pathExist(Pair P) {
            
            Point2D a = points[P.a];
            Point2D b = points[P.b];
            
            if (a.x() > b.x() && a.y() > b.y() ) 
                dg.addEdge(P.b, P.a);
            if (a.x() < b.x() && a.y() < b.y() ) 
                dg.addEdge(P.a, P.b);
        }
        
        private class Pair implements Comparable<Pair> {

            private final double dis;       // distance of two points
            private final int a, b;         // id of the points

            // create a new pair 
            public Pair(double t, int a, int b) {
                this.dis = t;
                this.a    = a;
                this.b    = b;
            }

            // compare distance between two pairs
            @Override
            public int compareTo(Pair that) {
                return Double.compare(this.dis, that.dis);
            }
        }
    }          
    
    
    public static void main(String[] args) throws FileNotFoundException {
        // TODO code application logic here
        boolean test = true;
        File file;
        
        test = false;
        
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
        
        CriPath main = new CriPath(points); // point2Ds is an array of Point2D
        
        System.out.printf("%5.5f\n", main.criticalPath());
        
        }
        
    }
    

