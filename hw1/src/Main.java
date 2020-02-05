import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;


class rootUF{

    private int[] id;
    private int[] sz;
    private int[] id2;
    private int[] sz2;
    private int[] tree;
    private int count = 0;
    
    public void rootUF(int N){
        id = new int[N];
        sz = new int[N];
        for (int i = 0; i < N; i++){
            id[i] = i;
            sz[i] = 1;
        }
    }
    public void extend(int M){
        id2 = Arrays.copyOf(id, M);
        sz2 = Arrays.copyOf(sz, M);
        for (int i = id.length; i < M; i++){
            id2[i] = i;
            sz2[i] = 1;
        }
        id = Arrays.copyOf(id2, M);
        sz = Arrays.copyOf(sz2, M);
    }
    private int root(int i){
        while (i != id[i]){
            //id[i] = id[id[i]];
            i = id[i];
        }
        return i;
    }

    public boolean connected(int p, int q)
    {
        return root(p) == root(q);
    }

    public void union(int p, int q){
        int pr = root(p);
        int qr = root(q);
        id[pr] = qr;
        /*if (pr == qr) return;
        if (sz[pr] < sz[qr]) {
            id[pr] = qr;
            sz[qr] += sz[pr];
        }else{
            id[qr] = pr;
            sz[pr] += sz[qr];
        }*/
        count--;
    }
    
    public int count(){
        return count;
    }
    
    public int trees(){
        int n = 1000;
        int c = 0;
        tree = new int[n];
        Arrays.fill(tree, 0);
        for (int i = 1; i < id.length; i++){
            tree[root(i)]++;
        }
        for (int j = 1; j < n; j++){
            if (tree[j] > 1)
                c++;
        }
        return c;
    }
}

public class Main{
    
	public static void main(String[] args) throws FileNotFoundException{
		
		//Scanner (use this scanner when uploading your code)
		//Scanner in = new Scanner(System.in);
                
		//scanner for local test (delete this part when uploading to onlinejudge)
		File file = new File("HW01.txt") ;
                Scanner in = new Scanner(file);
                
                // initial Union Find class
                int n = 50;
                WeightedQuickUnionUF uf = new WeightedQuickUnionUF(n);
                //uf.rootUF(50);
		//Read threshold int t
		int k = Integer.parseInt(in.nextLine());
                //System.out.println(k);
                
		//Read k nucleotide sequences
                HashMap<String,Integer> pairs = new HashMap<>();
                int i = 1;
                int p;
                int q;
                String[] seq = new String[3];
                while (in.hasNext()){
                    seq = in.nextLine().split(" ");
                    int PPI = Integer.parseInt(seq[2]);
                    if (PPI > k){
                        
                        if (pairs.containsKey(seq[0])){
                            p = pairs.get(seq[0]);
                        }else{
                            p = i;
                            pairs.put(seq[0], p);
                            i++;
                        }
                        
                        if (pairs.containsKey(seq[1])){
                            q = pairs.get(seq[1]);
                        }else{
                            q = i;
                            pairs.put(seq[1], q);
                            i++;
                        }
                        
                        if (!uf.connected(p, q)){
                            uf.union(p, q);
                        }
                        
                    }

                }
                System.out.println(pairs.size());
                System.out.println(pairs.size() + uf.count() - n);
                
        }
        
}

