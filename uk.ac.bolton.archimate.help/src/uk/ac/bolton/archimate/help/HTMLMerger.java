package uk.ac.bolton.archimate.help;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;


/**
 * Utility class called from "merge-html.xml" Ant script.
 * Not part of the Help plug-in.
 * Merge the HTML Help files into one HTML file to open in MS Word and Export as PDF.
 * 
 * @author Phillip Beauvoir
 */
public class HTMLMerger {
    
    // Source and Output folder has to be the same because the generated HTML file will reference the image files in "img" folder
    static String HELP_SRC_FOLDER = "help/";
    static String HTML_OUTPUT_FILE = "Archi User Guide.html";
    static String HTML_FILES_LIST = "files.list";
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            System.out.println("Opening Output Stream...");
            Writer out = new OutputStreamWriter(new FileOutputStream(HELP_SRC_FOLDER + HTML_OUTPUT_FILE));
            
            System.out.println("Writing Header...");
            out.append("<html>\n");
            out.append("<head>\n");
            out.append("<title>Archi User Guide</title>\n");
            out.append("<link href=\"style.css\" rel=\"stylesheet\" type=\"text/css\" />\n");
            out.append("</head>\n");
            out.append("<body>\n");
            
            boolean firstPage = true;
            
            List<String> fileList = readFilesList();

            for(String file : fileList) {
                System.out.println("Writing " + file);
                String content = getContent(new File(HELP_SRC_FOLDER + file));
                
                // MS Word Page Break for H1 tag before
                if(!firstPage && isHeading1(content)) {
                    out.append("<br style='mso-special-character:line-break;page-break-before:always'>\n");
                }
                
                // Add an Anchor tag
                if(!firstPage) {
                    out.append("<a name=\"" + file + "\"></a>");
                }

                // Replace local html links with anchor type links
                for(String s : fileList) {
                    content = content.replaceAll("<a href=\"" + s, "<a href=\"#" + s);
                }
                
                out.append(content);

                if(firstPage) {
                    firstPage = false;
                }
            }
            
            out.append("</body>\n");
            out.append("</html>\n");
            out.close();
            System.out.println("Closed Output Stream");
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
    }
    
    /**
     * Read the list of files 
     * @throws IOException 
     */
    private static List<String> readFilesList() throws IOException {
        List<String> list = new ArrayList<String>();
        
        File file = new File(HTML_FILES_LIST);
        BufferedReader in = new BufferedReader(new FileReader(file));
        
        String line;
        while((line = in.readLine()) != null) {
            line = line.trim();
            if(!line.startsWith("#") && !line.isEmpty()) {
                list.add(line);
            }
        }

        in.close();
        
        return list;
    }
    
    private static boolean isHeading1(String content) {
        return content.indexOf("<h1>") != -1 || content.indexOf("<H1>") != -1;
    }
    
    private static String getContent(File file) {
        String s = "";
        try {
            s = readFileAsString(file);
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
        int start = s.indexOf("<body>") + 6;
        int end = s.indexOf("</body>");
        return s.substring(start, end);
    }
    

    private static String readFileAsString(File file) throws IOException {
        byte[] buffer = new byte[(int) file.length()];
        BufferedInputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
            is.read(buffer);
        }
        finally {
            if(is != null) {
                is.close();
            }
        }
        return new String(buffer);
    }

}
