import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Main{
	public static void main(String[] args) throws FileNotFoundException{
		
		//Scanner (use this scanner when uploading your code)
		//Scanner in = new Scanner(System.in);
                
		//scanner for local test (delete this part when uploading to onlinejudge)
		File file = new File("input_HW0.txt") ;
                Scanner in = new Scanner(file);
        
		//Read int k
		int k = Integer.parseInt(in.nextLine());
                //System.out.println(k);
                
		//Read k nucleotide sequences
                String[] seq = new String[k];
                for ( int i = 0; i < k; i++){
                    seq[i] = in.nextLine();
                    //System.out.println(seq[i]);
                }
                
		//Read reference sequence
                String ref = in.nextLine();
                //System.out.println(ref);
		
                //Mapping (you might use "indexof")
                int fromidx;
                int[] count = new int[k];
                
                for ( int i = 0; i < k; i++){
                    fromidx = 0;
                    count[i] = 0;
                    while (fromidx >= 0){
                        fromidx = ref.indexOf(seq[i], fromidx);
                        //System.out.println(fromidx);
                        if (fromidx != -1){
                            fromidx += 1;
                            count[i]++;
                        }
                        
                    }
                    
                }
		int once = 0;
                int many = 0;
                //print out Mapping consequences
                for (int i = 0; i < k; i++){
                    if (count[i] != 0)
                        once++;
                    if (count[i] > 1)
                        many++;
                
                }
                System.out.println(once);
                System.out.println(many);
	}
}
