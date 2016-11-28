package DataPreProcess;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class combine {
	public static void main(String[] args){
		String csvFile1 = "1999.csv";
		String csvFile2 = "2000.csv";
		String csvFile3 = "2001.csv";
		String csvFile4 = "2002.csv";
		String csvFile5 = "2003.csv";
		
        BufferedReader br1 = null;
        BufferedReader br2 = null;
        BufferedReader br3 = null;
        BufferedReader br4 = null;
        BufferedReader br5 = null;
     
        try {
            br1 = new BufferedReader(new FileReader(csvFile1));
            br2 = new BufferedReader(new FileReader(csvFile2));
            br3 = new BufferedReader(new FileReader(csvFile3));
            br4 = new BufferedReader(new FileReader(csvFile4));
            br5 = new BufferedReader(new FileReader(csvFile5));
            
            PrintWriter writer = new PrintWriter("combine.csv", "UTF-8");
            
            String l1 = "";
            String l2 = "";
            String l3 = "";
            String l4 = "";
            String l5 = "";
            
            l1 = br1.readLine();
            while ((l1 = br1.readLine()) != null) {
        	    writer.println(l1);
            }
            /*
            l2 = br2.readLine();
            while ((l2 = br2.readLine()) != null) {
    	    	writer.println(l2);
            }
            
            l3 = br3.readLine();
            while ((l3 = br3.readLine()) != null) {
    	    	writer.println(l3);
            }
            
            l4 = br4.readLine();
            while ((l4 = br4.readLine()) != null) {
    	    	writer.println(l4);
            }
            
            l5 = br5.readLine();
            while ((l5 = br5.readLine()) != null) {
    	    	writer.println(l5);
            }*/
            
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {}
	}
}
