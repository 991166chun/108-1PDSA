/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hw7;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import java.util.Arrays;

public class Main {

    private MinPQ<Pair> pq;        
    private  PointW[] points;
    private Stack<Merge> merges;
    private int N;
    private final int all;
    private int newid;
    private int root;

    Main(Point2D[] in_points) {
        N = 0;
        all = in_points.length*3;
        points = new PointW[all];
        newid = 0;
        pq = new MinPQ<Pair>();
        merges = new Stack<>();
        // create clustering tree
        for (Point2D point : in_points) {
            if (point != null) {
                points[newid] = new PointW(point.x(), point.y(), 1);
                put(newid);
            }
        }
        root = buildTree();
    }
    
    private int buildTree() {
        
        while(1 < N) {
            Pair P = pq.delMin();

            if (!points[P.a].del() && !points[P.b].del()) {
                
                points[newid] = points[P.a].centoid(points[P.b]);
                merges.push(new Merge(P.a, P.b, newid));
                
                points[P.a].delete();
                N--;
                points[P.b].delete();
                N--;

                put(newid);
            }
        }
        return newid-1;
    }
    
    private void put(int i) {

        for (int j = 0; j < all; j++){

            if (points[j] == null)
                continue;
            if (points[j].del())
                continue;
            if (i == j)
                continue;
            double dis = points[i].distanceTo(points[j]);

            pq.insert(new Pair(dis, i, j));

        }
        newid++;
        N++;
    }

    public int[] cluster (int k){

        // k is the number of clusters. return the number of nodes in each cluster (in ascending order)
        
        boolean[] select = new boolean[all];
        Stack<Merge> temp = new Stack<>();
        Arrays.fill(select, false);
        select[root] = true;
        Merge abc;
        
        for (int j = 1; j < k; j++){
            abc = merges.pop();
            temp.push(abc);
            
            select[abc.a] = true;
            select[abc.b] = true;
            select[abc.c] = false;
        }
        
        while(!temp.isEmpty()){
            abc = temp.pop();
            merges.push(abc);
        }
        
        MinPQ<Integer> clusQ = new MinPQ<>();  
        for (int i = 0; i < all; i++){
            if (select[i])
            clusQ.insert(points[i].w);
        }
        
        int[] clusters = new int[k];
        for (int i = 0; i < k; i++) {
            clusters[i] = clusQ.delMin();
        }
        
        return clusters;
    }
    
    private class Merge {
        private final int a, b, c;         // id of the points
        public Merge(int a, int b, int c) {
                    this.a = a;
                    this.b = b;
                    this.c = c;
                }
    }
    
    private class PointW implements Comparable<PointW> {

        private final double x;    // x coordinate
        private final double y;    // y coordinate
        private int w;
        private boolean del;
        private PointW left, right;

        public PointW(double x, double y, int w) {
            if (Double.isInfinite(x) || Double.isInfinite(y))
                throw new IllegalArgumentException("Coordinates must be finite");
            if (Double.isNaN(x) || Double.isNaN(y))
                throw new IllegalArgumentException("Coordinates cannot be NaN");
            if (x == 0.0) this.x = 0.0;  // convert -0.0 to +0.0
            else          this.x = x;

            if (y == 0.0) this.y = 0.0;  // convert -0.0 to +0.0
            else          this.y = y;

            this.w = w;
            this.del = false;
        }

        public double x() {  return x; }

        public double y() {  return y; }

        public double w() {  return w; }

        public double distanceTo(PointW that) {
            double dx = this.x - that.x;
            double dy = this.y - that.y;
            return Math.sqrt(dx*dx + dy*dy);
        }

        public PointW centoid(PointW that) {
            int nw = this.w + that.w;
            double nx = (this.x * this.w + that.x * that.w) / nw;
            double ny = (this.y * this.w + that.y * that.w) / nw;
            return new PointW(nx, ny, nw);
        }

        public void delete(){
            del = true;
        }
        
        public boolean del(){
            return del;
        }

        @Override
        public int compareTo(PointW that) {
            if (this.y < that.y) return -1;
            if (this.y > that.y) return +1;
            if (this.x < that.x) return -1;
            if (this.x > that.x) return +1;
            return 0;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) return true;
            if (other == null) return false;
            if (other.getClass() != this.getClass()) return false;
            PointW that = (PointW) other;
            return this.x == that.x && this.y == that.y && this.w == that.w;
        }
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