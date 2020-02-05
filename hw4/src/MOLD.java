
import edu.princeton.cs.algs4.Point2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class Main {

        public static int[] ConvexHullVertex(Point2D[] a) {

            // 回傳ConvexHullVertex的index set，
            
            int N = a.length;
            int yMin = 0;
            Point2D[] a2 = new Point2D[N];
            
            // copy array
            for (int i = 0; i < N; i++){
                a2[i] = a[i];
            }
            
            // 找出最低點p
            for (int i = 0; i < N; i++){
                if (i>0){
                    int yOrder = Point2D.Y_ORDER.compare(a[yMin], a[i]);
                    if (yOrder == 1)
                        yMin = i;
                }
            }
            Point2D p = new Point2D(a[yMin].x(), a[yMin].y());
            
            // 把a2照角度排序
            Arrays.sort(a2, p.atan2Order());
            
            int[] sortId = new int[N+1];
            
            for (int i = 0; i < N; i++){
                
                boolean e = false;
                int j = 0;
                while (!e) {
                    e = a2[i].equals(a[j]);
                    if (e)
                        sortId[i] = j;
                    else
                        j++;
                }
            }
            sortId[N] = sortId[0];
            
            //--------------------------------------------------------------
            Stack<Integer> mapS = new Stack<>();
            int[] ang = new int[3];
            int isccw;
            int temp;
            int out;
            int lenR = 0;
            for (int i = 0; i < 2; i++) {
                ang[i] = sortId[i];
                Integer last = mapS.push(sortId[i]);
                lenR++;
            }
            
            for (int i = 2; i < N + 1; i++) {
                ang[2] = sortId[i];
                mapS.push(sortId[i]);
                lenR++;
                isccw = Point2D.ccw(a[ang[0]], a[ang[1]], a[ang[2]]);
                
                if (isccw == 1){
                    ang[0] = sortId[1];
                    ang[1] = sortId[2];
                }else {
                    ang[1] = sortId[2];
                    temp = mapS.pop();
                    out = mapS.pop();
                    mapS.push(temp);
                    lenR--;
                }
            }
            //out = mapS.pop();
            
            int[] map = new int[lenR-1];
            for (int i = lenR-1; i > 0; i--){
                map[i-1] = (int) mapS.pop();
                System.out.print(a[map[i-1]].x());
                System.out.print(",");
                System.out.println(a[map[i-1]].y());

            }
            
            // index 請依該點在a陣列中的順序編號：0, 1, 2, 3, 4, ....a.length-1
            return map;
        }
        
        public static int getRoot(int[] parents, int index){
            while(parents[index] != index)
                index = parents[index];
            
            return index;
        }

        public static void main(String[] args) throws FileNotFoundException {

    	    //File file = new File(args[0]) ;// file name assigned

            File file = new File("test.in") ; // filename for local test (delete this part when uploading to onlinejudge)
            
            Scanner in = new Scanner(file);
            String[] coordinates = new String[2];
            double distance = Double.parseDouble(in.nextLine());
            int num = Integer.parseInt(in.nextLine());
            
            double x, y;
            
            Point2D[] a = new Point2D[num];
            
            // 1. read in the file containing N 2-dimentional points

            for (int i = 0; i < num; i++) {
                coordinates = in.nextLine().split(" ");
                x = Double.parseDouble(coordinates[0]);
                y = Double.parseDouble(coordinates[1]);
                a[i] = new Point2D(x,y);
            }
            
            // 2. create an edge for each pair of points with a distance <= d

            int[] parents = new int[num];
            
            for (int i = 0; i < parents.length; i++)
                parents[i] = i;
            
            for (int i = 0; i < num; i++) {
                Point2D p1 = a[i];
                
                for (int j = 0; j < num; j++) {
                    Point2D p2 = a[j];
                    if (p1.distanceTo(p2) < distance) {
                        int root1 = getRoot(parents, i);
                        int root2 = getRoot(parents, j);
                        if (root1 <= root2)
                            parents[root2] = root1;
                        else
                            parents[root1] = root2;
                    }
                }
            }
            
            for (int i = 0; i < parents.length; i++) {
                parents[i] = getRoot(parents, i); 
            }
            
            // 3. find connected components (CCs) with a size >= 3
            
            Map<Integer, Integer> map = new HashMap<Integer, Integer>();
            
            for (int i : parents) {
                if (map.containsKey(i))
                    map.put(i, map.get(i) + 1);
                else
                    map.put(i, 1);
            }
            
            int ans = 0;
            
            // 4. for each CC, find its convex hull vertices by calling ConvexHullVertex(a[])

            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                
                Point2D[] pointGroup = new Point2D[entry.getValue()];
                int count = 0;
                
                for (int i = 0; i < parents.length; i++){
                    if (parents[i] == entry.getKey())
                        pointGroup[count++] = a[i];
                }
                
                if (pointGroup.length > 2) {
                    int[] convex = ConvexHullVertex(pointGroup);
                    ans += convex.length;
                }
            }
            
            // 5. count the number of points in N serving as a convex hull vertex, print it

            System.out.println(ans);
        }
    }