package DataPreProcess;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class reduce {
	public static void main(String[] args){
		String txtFile = "Labeled.txt";
        BufferedReader br = null;
        String line = "";
        try {
        	int limit1 = count(txtFile);
        	System.out.println(limit1);
        	int limit0 = limit1/2;
            br = new BufferedReader(new FileReader(txtFile));
            PrintWriter writer = new PrintWriter("df.txt", "UTF-8"); 
            
            while ((limit1>0 || limit0>0) && (line = br.readLine()) != null) {
            	String[] col = line.split(",")[0].split(" ");
                              
            	if(Integer.parseInt(col[2]) == 1){
            		if(limit1>0){
            			writer.println(line);
            			limit1--;
            		}
            	}
            	else{
            		if(limit0>0){
            			writer.println(line);
            			limit0--;
            		}
            	}                
            }
            System.out.println("1:"+limit1+" 0:"+limit0);
        
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
	
	public static int count(String file) throws IOException{
		int count = 0;
		BufferedReader br = new BufferedReader(new FileReader(file));
		String line = "";
		while((line = br.readLine()) != null){
			String[] col = line.split(",")[0].split(" ");
			if(Integer.parseInt(col[2]) == 1){
				count++;
			}
		}
		br.close();
		return count;
	}
}
