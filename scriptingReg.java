package DataPreProcess;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class scriptingReg {
	public static void main(String[] args){
		String txtFile = "Labeled.txt";
        BufferedReader br = null;
        String line = "";
        String cvsSplitBy = " ";
        try {
            br = new BufferedReader(new FileReader(txtFile));
            PrintWriter writer = new PrintWriter("magnitude.txt", "UTF-8"); 
            while ((line = br.readLine()) != null) {
            	String s = "";
                String[] col = line.split(",")[0].split(cvsSplitBy);
                
                s += col[0]+" ";
                int reg = Integer.parseInt(col[1]);
                if(reg>=65)
                	reg = 13;
                else if(reg<0)
                	reg = 0;
                else
                	reg = reg/5;
                s += reg+" ";
                
                s += col[2]+" ";
                reg = Integer.parseInt(col[3]);
                if(reg>=65)
                	reg = 13;
                else if(reg<0)
                	reg = 0;
                else
                	reg = reg/5;
                s += reg+",";
                
                s += line.split(",")[1];		
                writer.println(s);
            }
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
}