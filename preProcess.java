package DataPreProcess;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

	public class preProcess {
		public static void main(String[] args){
			String csvFile = "1999.csv";
	        BufferedReader br = null;
	        String line = "";
	        
	        try {
	            br = new BufferedReader(new FileReader(csvFile));
	            PrintWriter writer = new PrintWriter("data.txt", "UTF-8");
	            line = br.readLine();
	            while ((line = br.readLine()) != null) {
	            	String[] words = line.split(",");
	            	String l = "";
	        		boolean flag = true;
	        		for(int i=0;i<18;i++){
	        			if(i != 4 && i != 6 && i != 10 && i != 11 && i != 12 && i != 13){
	        				String s = words[i];
	        				l += s+" ";
	        				if(s.equals("NA"))
	        					flag = false;
	        			}
	        		}
	        		l += words[12]+" "+words[18];
	        	    if(flag)
	        	    	writer.println(l);
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
