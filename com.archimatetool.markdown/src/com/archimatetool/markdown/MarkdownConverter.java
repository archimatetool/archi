/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.markdown;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;

import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.ins.InsExtension;
import org.commonmark.node.HtmlBlock;
import org.commonmark.node.HtmlInline;
import org.commonmark.node.Link;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.NodeRenderer;
import org.commonmark.renderer.html.AttributeProviderFactory;
import org.commonmark.renderer.html.HtmlRenderer;
import org.commonmark.renderer.text.LineBreakRendering;
import org.commonmark.renderer.text.TextContentNodeRendererFactory;
import org.commonmark.renderer.text.TextContentRenderer;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.ILog;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;

/**
 * MarkdownConverter
 * 
 * @author Phillip Beauvoir
 */
@SuppressWarnings("nls")
public class MarkdownConverter {
    
    // Commonmark extensions
    private static final List<Extension> EXTENSIONS = List.of(
                                                            AutolinkExtension.create(),
                                                            InsExtension.create(),
                                                            StrikethroughExtension.create(),
                                                            TablesExtension.create());

    // Commonmark Parser
    private static final Parser PARSER = Parser.builder()
                                               .extensions(EXTENSIONS)
                                               .build();
    
    // HTML wrapper string
    private static final String HTML_WRAPPER = loadHtmlWrapper();
    
    // Default instance
    private static final MarkdownConverter DEFAULT = builder().build();
    
    /**
     * Static Builder for configuring and creating MarkdownConverter instances
     */
    public static class Builder {
        private boolean useExternalLinks = false;
        private boolean escapeHtml = false;
        private boolean useSeparateBlocks = true; 

        /**
         * Sets whether external links should open in a new tab via target="_blank".
         * Default is false.
         * Only valid when converting to HTML.
         */
        public Builder externalLinks(boolean useExternalLinks) {
            this.useExternalLinks = useExternalLinks;
            return this;
        }
        
        /**
         * Sets whether Html should be escaped.
         * Default is false.
         * Only valid when converting to HTML.
         */
        public Builder escapeHtml(boolean escapeHtml) {
            this.escapeHtml = escapeHtml;
            return this;
        }
        
        /**
         * Sets whether to separate blocks by a blank line in TextContentRenderer
         * The default is true so that line breaks are preserved in case Markdown is not used in the text.
         * If set to false use single line breaks between blocks, not a blank line.
         * Only valid when converting to Text.
         */
        public Builder separateBlocks(boolean useSeparateBlocks) {
            this.useSeparateBlocks = useSeparateBlocks;
            return this;
        }

        /**
         * @return the configured {@link MarkdownConverter}
         */
        public MarkdownConverter build() {
            return new MarkdownConverter(this);
        }
    }
    
    /**
     * @return a new Builder for configuring a {@link MarkdownConverter}.
     */
    public static Builder builder() {
        return new Builder();
    }

    /**
     * @return The default MarkdownConverter instance with no options
     */
    public static MarkdownConverter getDefault() {
        return DEFAULT;
    }
    
    private final TextContentRenderer textRenderer;
    private final HtmlRenderer htmlRenderer;
    
    private MarkdownConverter(Builder builder) {
        textRenderer = createTextRenderer(builder.useSeparateBlocks);
        htmlRenderer = createHtmlRenderer(builder.useExternalLinks, builder.escapeHtml);
    }
    
    /**
     * Convert given markdown to Text string
     * @param markdown The markdown text
     * @return The converted String or empty string if markdown is null or original markdown if it's blank
     */
    public String toText(String markdown) {
        if(markdown == null || markdown.isBlank()) {
            return markdown == null ? "" : markdown;
        }
        
        try {
            Node document = PARSER.parse(markdown);
            return textRenderer.render(document);
        }
        catch(Exception ex) {
            ex.printStackTrace();
            return "";
        }
    }
    
    /**
     * Convert given markdown to HTML string with surrounding <div> tag
     * @param markdown The markdown text
     * @return The converted String or empty string if markdown is null or original markdown if it's blank
     */
    public String toDiv(String markdown) {
        if(markdown == null || markdown.isBlank()) {
            return markdown == null ? "" : markdown;
        }
        
        return "<div class=\"markdown-body\">%s</div>".formatted(toHtml(markdown));
    }
    
    /**
     * Convert given markdown to HTML string with surrounding <html> tag and style from the wrapper.html file
     * If darkMode is true will always return the wrapper html even if markdown is blank so that it shows the dark body background.
     * If darkMode is false and markdown is null or empty return original markdown
     * @param markdown The markdown text
     * @param darkMode If true use the dark mode class in the wrapper HTML
     * @return The converted String in an HTML body or original markdown if blank if darkmode is false
     */
    public String toHtmlBody(String markdown, boolean darkMode) {
        if((markdown == null || markdown.isBlank()) && !darkMode) {
            return markdown == null ? "" : markdown;
        }
        
        return HTML_WRAPPER.formatted(darkMode ? "dark-mode" : "", toHtml(markdown));
    }
    
    /**
     * Convert given markdown to HTML string
     * @param markdown The markdown text
     * @return The converted String or empty string if markdown is null or markdown if it's blank
     */
    public String toHtml(String markdown) {
        if(markdown == null || markdown.isBlank()) {
            return markdown == null ? "" : markdown;
        }
        
        try {
            Node document = PARSER.parse(markdown);
            return htmlRenderer.render(document);
        }
        catch(Exception ex) {
            ex.printStackTrace();
            return "<pre style='color:red'>Error rendering Markdown:<br/>" + ex.getMessage() + "</pre>";
        }
    }
    
    /**
     * Create a TextContentRenderer
     */
    private TextContentRenderer createTextRenderer(boolean useSeparateBlocks) {
        // Factory for custom NodeRenderer. Allows us to hook into Nodes
        TextContentNodeRendererFactory nodeRendererFactory = context -> {
            return new NodeRenderer() {
                @Override
                public Set<Class<? extends Node>> getNodeTypes() {
                    return Set.of(Link.class, HtmlInline.class, HtmlBlock.class);
                }

                @Override
                public void render(Node node) {
                    // Ensure links are displayed "as is" and not like "https://www.somewhere.com" (https://www.somewhere.com)
                    if(node instanceof Link link) {
                        String destination = link.getDestination();
                        if(destination != null && !destination.isEmpty()) {
                            context.getWriter().write(destination);
                        }
                    }
                    // HtmlBlock will not render any content
                    // HtmlInline will partially render
                }
            };
        };
        
        return TextContentRenderer.builder()
                                  .extensions(EXTENSIONS)
                                  .lineBreakRendering(useSeparateBlocks ? LineBreakRendering.SEPARATE_BLOCKS : LineBreakRendering.COMPACT)
                                  .nodeRendererFactory(nodeRendererFactory)
                                  .build();
    }
    
    /**
     * Create a HtmlRenderer
     */
    private HtmlRenderer createHtmlRenderer(boolean useExternalLinks, boolean escapeHtml) {
        // Factory for custom AttributeProvider. Allows us to hook into Nodes and set attributes
        AttributeProviderFactory attributeProviderFactory = context -> {
            return(node, tagName, attributes) -> {
                // Add target=_blank to links in Markdown
                if(node instanceof Link && "a".equalsIgnoreCase(tagName) && useExternalLinks) {
                    attributes.put("target", "_blank");
                }
            };
        };
        
        return HtmlRenderer.builder()
                           .extensions(EXTENSIONS)
                           .sanitizeUrls(true)
                           .softbreak("<br/>")
                           .attributeProviderFactory(attributeProviderFactory)
                           .escapeHtml(escapeHtml)
                           .build();
    }

    /**
     * Load the HTML wrapper file
     */
    private static String loadHtmlWrapper() {
        Bundle bundle = FrameworkUtil.getBundle(MarkdownConverter.class);
        if(bundle != null) {
            try {
                URL url = bundle.getEntry("/wrapper.html");
                if(url != null) {
                    url = FileLocator.resolve(url);
                    try(InputStream is = url.openStream()) {
                        return new String(is.readAllBytes(), StandardCharsets.UTF_8);
                    }
                }
            }
            catch(IOException ex) {
                ILog.of(bundle).error("Error loading HTML file", ex);
            }
        }
        
        return "<html><body class=\"%s\"><div class=\"markdown-body\">%s</div></body></html>"; // fallback
    }
}