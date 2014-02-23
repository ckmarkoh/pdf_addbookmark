
 
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
 
 
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
 

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AddBookmarks {
 
    public static  String Index_File    = "";
    
    public static  String Input_File	= "";
    
    public static  String Output_File    = "";

    public static int offset = 0;
    
    
    public static List<String> bm_list = null;
    public static int bm_list_id = 0;

    /**
     * 
     * 
     * Main method.
     * @param args no arguments needed
     * @throws DocumentException 
     * @throws IOException
     */
    public static void main(String[] args) throws IOException, DocumentException {
    	//addBookmarkToFile();
    	
		if (args.length<3){
			System.out.println("pdf_addbookmark <index_file> <pdf_file> <output_file> <offset>");
		}
		else{
			if (args.length>3){
				offset=Integer.parseInt(args[3]);
			}
			//System.out.println("start");
			Index_File=args[0];
			Input_File=args[1];
			Output_File=args[2];
			bm_list = readLines(Index_File);
			PdfReader reader = new PdfReader(Input_File);
			ArrayList<HashMap<String, Object>> outlines=createBookmark(0);
			PdfStamper stamper =new PdfStamper(reader, new FileOutputStream(Output_File)); 
			stamper.setOutlines(outlines);                        
			stamper.close();
    	}

    }

    
    public static ArrayList<HashMap<String, Object>> 
    createBookmark(int pre_depth) throws IOException, DocumentException{
		
    	ArrayList<HashMap<String, Object>> cur_list= new ArrayList<HashMap<String, Object>> ();
    	HashMap<String, Object> pre_map = null;
        //ArrayList<HashMap<String, Object>> outlines= new ArrayList<HashMap<String, Object>> ();                 
 		while (true){
	 		// = new HashMap<String, Object> ();
	    	String line=bm_list.get(bm_list_id);
 			//System.out.println(line);

	 	    Pattern p = Pattern.compile("(\t*)(.*) ([0-9]*)");
	 	    Matcher m = p.matcher(line);
	 	    
	 	    if(m.find()){
	        	//System.out.println("g1:"+m.group(1).length());
	        	//System.out.println("x1:"+m.group(1)+m.group(2));
	        	//System.out.println("g3:"+m.group(3));
	    		int current_depth=m.group(1).length();	        
	 	        
				if (current_depth > pre_depth){
					ArrayList<HashMap<String, Object>> kids= createBookmark(pre_depth+1);
					pre_map.put("Kids", kids);
				}
				else if( current_depth < pre_depth) {
					break;
				}
				else{				
			    	HashMap<String, Object> map = new HashMap<String, Object> ();
					map.put("Title", m.group(2));
		        	//System.out.println("x2:"+m.group(1)+m.group(2));
					map.put("Action", "GoTo");
					map.put("Page", String.format("%d Fit", Integer.parseInt(m.group(3))+offset));
			        cur_list.add(map);
			        pre_map=map;
			        bm_list_id++;
					if(bm_list_id==bm_list.size()) {
						break;
					}
				}
	 	    }
 	    }
		return cur_list;
    }
    
    
     public static List<String> readLines(String filename) throws IOException {
            FileReader fileReader = new FileReader(filename);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            List<String> lines = new ArrayList<String>();
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line);
            }
            bufferedReader.close();
            return lines;
        }    

        
}
