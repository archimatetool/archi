package uk.ac.bolton.archimate.help;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;


/**
 * Merge the HTML Help files into one HTML file to open in MS Word and Export as PDF.
 * 
 * IMPORTANT - In MS Word Options make sure in Advanced->Web Options->Pictures that Pixels per Inch is 144
 * so that images are sized properly.
 * 
 * @author Phillip Beauvoir
 */
public class HTMLMerger {
    
    static String rootFolder = "help/";
    
    static String[] files = {
        
        "front.html",
        "intro.html",
        "install.html",
        "archi_working.html",
        "new_model.html",
        
        "model_tree.html",
        "model_tree_add.html",
        "model_tree_folders.html",
        "model_tree_working.html",
        "model_tree_search.html",
        
        "views.html",
        "views_open_create.html",
        "views_working.html",
        "view_nav.html",
        "view_palette.html",
        "view_palette_tools.html",
        "view_palette_creation_tools.html",
        "view_format_painter.html",
        "view_addnew.html",
        "view_magic_connector.html",
        "view_addexisting.html",
        "view_group.html",
        "view_note.html",
        "view_junction.html",
        "view_reference.html",
        "view_conn_bendpoints.html",
        "view_conn_properties.html",
        "view_conn_router.html",
        "view_container_nested.html",
        "view_z_order.html",
        "view_copy_paste_delete.html",
        "view_align_grid.html",
        
        "sketch_view.html",
        
        "properties_window.html",
        "properties_model.html",
        "properties_element.html",
        "properties_element_appearance.html",
        "properties_relation.html",
        "properties_relation_appearance.html",
        "properties_view.html",
        "properties_viewref.html",
        "properties_folder.html",
        "properties_note.html",
        "properties_group.html",
        
        "navigator.html",
        "hints.html",
        "outline.html",
        
        "open_save_print.html",
        "importing.html",
        "exporting.html",
        "reporting.html",
        
        "templates.html",
        "templates_new.html",
        "templates_new_model.html",
        "templates_managing.html",
        
        "derived_rels.html",
        
        "preferences.html",
        "prefs_connections.html",
        "prefs_diagram.html",
        "prefs_figures.html",
        "prefs_general.html",
        "prefs_help.html",
        
    };

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            System.out.println("Opening Output Stream...");
            Writer out = new OutputStreamWriter(new FileOutputStream(rootFolder + "Archi User Guide.html"));
            
            System.out.println("Writing Header...");
            out.append("<html>\n");
            out.append("<head>\n");
            out.append("<title>Archi User Guide</title>\n");
            out.append("<link href=\"style.css\" rel=\"stylesheet\" type=\"text/css\" />\n");
            out.append("</head>\n");
            out.append("<body>\n");
            
            boolean firstPage = true;

            for(String file : files) {
                System.out.println("Writing " + file);
                String content = getContent(new File(rootFolder + file));
                
                // MS Word Page Break for H1 tag before
                if(!firstPage && isHeading1(content)) {
                    out.append("<br style='mso-special-character:line-break;page-break-before:always'>\n");
                }
                
                // Add an Anchor tag
                if(!firstPage) {
                    out.append("<a name=\"" + file + "\"></a>");
                }

                // Replace local html links with anchor type links
                for(String s : files) {
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
