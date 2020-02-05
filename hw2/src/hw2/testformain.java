package hw3;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Chun
 */
import java.io.File;
import java.util.*;

public class testformain {
    
	public static void main (String[] args) throws Exception{
		Main a = new Main(20);
		File file = new File("hw2.txt") ;
		Scanner in = new Scanner(file);
		String pro1 = "";		
		while(in.hasNextLine())
		{
			pro1 = in.nextLine();
			String[] pro = pro1.split("\t");
			a.add(pro[0],pro[1]);
		}
		int problemId = 4;// you can change this to switch what function you are going to test;
		switch (problemId) {				
				case 1:// Test size function
					System.out.println(a.size());
					break;
				// 28					
				case 2:// Test add function throw exception
					try {
						a.add("test1", "test1");
					} catch (IndexOutOfBoundsException e) {
						System.out.println("IndexOutOfBoundsException");
					}
					break;
				// IndexOutOfBoundsException				
				case 3:// Test interactions function
					for(String o:a.interactions("protein01",2))
						System.out.println(o);
					break;
				
				// protein01
				// protein02
				// protein03
				// protein04
				// protein05
				// protein06
				// protein08
				// protein09
				// protein10
				// protein12
				// protein15
				case 4:// Test interactions function
					for(String o:a.neighbors("protein07"))
						System.out.println(o);
					break;
				}
				// protein06
				// protein07
				// protein12
				// protein14

	}	
}
