package DataPreProcess;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

public class parseData {
	public static void main(String[] args){
		String txtFile = "data.txt";
        BufferedReader br = null;
        String line = "";
        try {
            br = new BufferedReader(new FileReader(txtFile));
            PrintWriter writer = new PrintWriter("Labeled.txt", "UTF-8");
            HashMap<String,Integer> C = new HashMap<String,Integer>();
            HashMap<String,Integer> A = new HashMap<String,Integer>();
            
            int valC = 1;
            int valA = 1;
            while ((line = br.readLine()) != null) {
            	String s = "";
                String[] col = line.split(" ");
                String l1 = "";
                String l2 = "";
                
                int ar_delay = Integer.parseInt(col[8]);
                if(ar_delay >= 15)
                	l1 = "1";
                else
                	l1 = "0";
                
                int dep_delay = Integer.parseInt(col[9]);
                if(dep_delay >= 15)
                	l2 = "1";
                else
                	l2 = "0";
                
                String label = l1+" "+col[8]+" "+l2+" "+col[9];
                s += label+",";
                
                for(int in=0;in<col.length;in++){
//                	if(in == 3){
//                		int day = Integer.parseInt(col[in]);
//                		if(day == 1 || day == 7)
//                			s += "1 ";
//                		else
//                			s += "0 ";
//                	}
//                	else 
                	if(in == 6){
                		if(C.containsKey(col[in]))
                			s += C.get(col[in])+" ";
                		else{
                			C.put(col[in], valC);
                			valC++;
                			s += C.get(col[in])+" ";
                		}
                			
                	}
                	else if(in == 10 || in == 11){
                		if(A.containsKey(col[in]))
                			s += A.get(col[in])+" ";
                		else{
                			A.put(col[in], valA);
                			valA++;
                			s += A.get(col[in])+" ";
                		}
                			
                	}
                	else if(in != 8 && in != 9)
                		s += col[in]+" ";
                }
                		
                writer.println(s);
            }
            writer.close();
            printMap(A,"Airport.csv");
            printMap(C,"Carrier.csv");
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
	
	public static void printMap(HashMap<String,Integer> hmap,String file){
		try{
			PrintWriter w = new PrintWriter(file, "UTF-8");
			for(String s: hmap.keySet()){
				w.println(s+","+hmap.get(s));
			}
			w.close();
		}catch (FileNotFoundException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }
	}
}