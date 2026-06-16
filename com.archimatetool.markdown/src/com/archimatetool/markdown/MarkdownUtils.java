/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.markdown;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.ins.InsExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.renderer.text.LineBreakRendering;
import org.commonmark.renderer.text.TextContentRenderer;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.ILog;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * Some useful Markdown Utilities
 *
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public final class MarkdownUtils {
    
    /**
     * Commonmark extensions
     */
    private static final List<Extension> extensions = List.of(
                                                            AutolinkExtension.create(),
                                                            InsExtension.create(),
                                                            StrikethroughExtension.create(),
                                                            TablesExtension.create());

    /**
     * Singleton Parser
     */
    private static final Parser parser = Parser.builder()
                                               .extensions(extensions)
                                               .build();
    
    /**
     * Singleton HtmlRenderer
     */
    private static final HtmlRenderer htmlRendererer = HtmlRenderer.builder()
                                                                   .extensions(extensions)
                                                                   .sanitizeUrls(true)
                                                                   .softbreak("<br/>")
                                                                   .build();

    /**
     * Singleton TextContentRenderer
     */
    private static final TextContentRenderer textRendererer = TextContentRenderer.builder()
                                                                                 .extensions(extensions)
                                                                                 .lineBreakRendering(LineBreakRendering.SEPARATE_BLOCKS)
                                                                                 .build();

    private static String htmlWrapper;
    
    /**
     * Convert given markdown to Text string
     * @param markdown The markdown text
     * @return The converted text
     */
    public static String convertMarkdownToText(String markdown) {
        if(!isMarkdown(markdown)) {
            return markdown;
        }

        try {
            Node document = parser.parse(markdown);
            return textRendererer.render(document);
        }
        catch(Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
    
    /**
     * Convert given markdown to HTML string with surrounding <html> tag and style
     * @param markdown The markdown text
     * @param darkMode If true use the dark mode class in the CSS
     * @return The converted text
     */
    public static String convertMarkdownToFullHtml(String markdown, boolean darkMode) {
        return wrapWithHTMLBody(convertMarkdownToHtml(markdown), darkMode);
    }
    
    /**
     * Convert given markdown to HTML string with surrounding <div> tag
     * @param markdown The markdown text
     * @return The converted text
     */
    public static String convertMarkdownToDiv(String markdown) {
        if(!isMarkdown(markdown)) {
            return markdown;
        }
        
        return "<div class=\"markdown-body\">%s</div>".formatted(convertMarkdownToHtml(markdown));
    }
    
    /**
     * Convert given markdown to HTML string
     * @param markdown The markdown text
     * @return The converted text
     */
    public static String convertMarkdownToHtml(String markdown) {
        if(!isMarkdown(markdown)) {
            return markdown;
        }
        
        try {
            Node document = parser.parse(markdown);
            return htmlRendererer.render(document);
        }
        catch(Exception ex) {
            ex.printStackTrace();
            return "<pre style='color:red'>Error rendering Markdown:<br/>" + ex.getMessage() + "</pre>";
        }
    }
    
    public static boolean isMarkdown(String markdown) {
        if(markdown == null || markdown.length() == 0) {
            return false;
        }
        
        return true;
    }
    
    /**
     * Wrap the given HTML in full HTML tags in the wrapper.html file
     * @param html The HTML to wrap
     * @param darkMode If true use the dark mode class in the CSS
     * @return The wrapped html
     */
    private static String wrapWithHTMLBody(String html, boolean darkMode) {
        // Load wrapper html from file
        if(htmlWrapper == null) {
            final String fallback = "<div class=\"%s\">%s</div>";
            
            Bundle bundle = FrameworkUtil.getBundle(MarkdownUtils.class);
            if(bundle != null) {
                try {
                    URL url = FileLocator.resolve(bundle.getEntry("/wrapper.html"));
                    htmlWrapper = url != null ? Files.readString(Path.of(url.toURI())) : fallback;
                }
                catch(IOException | URISyntaxException ex) {
                    ILog.of(bundle).error("Error loading CSS file", ex);
                    htmlWrapper = fallback;
                }
            }
        }
        
        return htmlWrapper.formatted(darkMode ? "dark-mode" : "", html);
    }

}