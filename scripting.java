package DataPreProcess;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class scripting {
	public static void main(String[] args){
		String txtFile = "df.txt";
        BufferedReader br = null;
        String line = "";
        try {
            br = new BufferedReader(new FileReader(txtFile));
            PrintWriter writer = new PrintWriter("classify.txt", "UTF-8");
            
            while ((line = br.readLine()) != null) {
            	String s = line.split(",")[0]+",";
            	String[] col = line.split(",")[1].split(" ");
                boolean flag = true;              
            	for(int in=0;in<col.length;in++){
            		int qwe = Integer.parseInt(col[in]);
            		if(qwe<0)
            			flag = false;
            		/*	if(in == 0){
            			//remove year
            		}
            		else*/ if(in == 4 || in == 5){
            			int t = Integer.parseInt(col[in]);
            			t = t/100;
            			t = t*100;
            			s += t+" ";
            		}
            		else
            			s += col[in]+" ";
            	}		
            	if(flag)
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