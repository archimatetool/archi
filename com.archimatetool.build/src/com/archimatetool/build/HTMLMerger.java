/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.build;


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
import java.util.Scanner;


/**
 * Utility class called from "merge-html.xml" Ant script.
 * Merges the HTML Help files from the com.archimatetool.help/help folder into one HTML file.
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class HTMLMerger {
    
    private static File HELP_SRC_FOLDER = new File("../../com.archimatetool.help/help/Text");
    private static File TARGET_FOLDER = new File("../temp/Text");
    private static File HTML_OUTPUT_FILE = new File(TARGET_FOLDER, "Archi User Guide.html");
    private static File HTML_FILES_LIST = new File("html_contents.list");
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            System.out.println("Opening Output Stream...");
            TARGET_FOLDER.mkdirs();
            Writer out = new OutputStreamWriter(new FileOutputStream(HTML_OUTPUT_FILE));
            
            System.out.println("Writing Header...");
            out.append("<html>\n");
            out.append("<head>\n");
            out.append("<title>Archi User Guide</title>\n");
            out.append("<link href='../Styles/style.css' rel='stylesheet' type='text/css' />\n");
            out.append("</head>\n");
            out.append("<body>\n");
            
            // Insert cover page with version number
            System.out.println("Writing Cover...");
            out.append("<div style='text-align:center;'>\n");
            out.append("<img src='../Images/cover.png' />\n");
            out.append("<p>Version " + System.getProperty("ArchiVersion") + "</p>\n");
            out.append("</div>\n");
            
            List<String> fileList = readFilesList();

            for(String fileName : fileList) {
                System.out.println("Writing " + fileName);
                
                String content = getBodyContent(new File(HELP_SRC_FOLDER, fileName));
                
                // Add an Anchor tag
                out.append("<a name=\"" + fileName + "\"></a>\n");

                // Replace local html links with anchor type links
                for(String s : fileList) {
                    content = content.replaceAll("<a href=\"" + s, "<a href=\"#" + s);
                }
                
                Scanner scanner = new Scanner(content);
                while(scanner.hasNextLine()) {
                    String line = scanner.nextLine();
                    
                    // MS Word Page Break before
                    if(line.contains("class=\"pagebreak\"")) {
                        out.append("<br style='mso-special-character:line-break;page-break-before:always'>\n");
                    }
                    
                    out.append(line + "\n");
                }
                scanner.close();
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
        
        BufferedReader in = new BufferedReader(new FileReader(HTML_FILES_LIST));
        
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
    
    private static String getBodyContent(File file) {
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
