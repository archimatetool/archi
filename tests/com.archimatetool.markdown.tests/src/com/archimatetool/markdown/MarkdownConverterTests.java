/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.markdown;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


@SuppressWarnings("nls")
public class MarkdownConverterTests {
    
    private static MarkdownConverter defaultConverter;
    
    @BeforeAll
    static void setup() {
        defaultConverter = MarkdownConverter.getDefault();
    }
    
    @Test
    void toText_Blank() {
        assertEquals("", defaultConverter.toText(null));
        assertEquals("", defaultConverter.toText(""));
        assertEquals(" ", defaultConverter.toText(" "));
    }

    @Test
    void toText_Rendered() {
        assertEquals("Hello World", defaultConverter.toText("Hello World"));
        
        String markdown = "# H1"
                        + "\n"
                        + "*italic* and **bold**"
                        + "\n"
                        + "[link](https://www.go.com)"
                        + "\n"
                        + "- list";
        
        String expected = "H1"
                        + "\n\n"
                        + "italic and bold"
                        + "\n"
                        + "https://www.go.com"
                        + "\n\n"
                        + "- list";
        
        assertEquals(expected, defaultConverter.toText(markdown));
    }
    
    @Test
    void toText_Rendered_NoSeparateBlocks() {
        MarkdownConverter converter = MarkdownConverter.builder()
                                                       .separateBlocks(false)
                                                       .build();
        
        assertEquals("Hello World", converter.toText("Hello World"));
        
        String markdown = "# H1"
                        + "\n"
                        + "*italic* and **bold**"
                        + "\n"
                        + "[link](https://www.go.com)"
                        + "\n"
                        + "- list";
        
        String expected = "H1"
                        + "\n"
                        + "italic and bold"
                        + "\n"
                        + "https://www.go.com"
                        + "\n"
                        + "- list";
        
        assertEquals(expected, converter.toText(markdown));
    }
    
    @Test
    void toText_LineBreaks() {
        assertEquals("Hello\n\nWorld", defaultConverter.toText("Hello\n\n\nWorld"));
    }
    
    @Test
    void toText_Links() {
        // Custom TextContentNodeRendererFactory should make links simple
        assertEquals("https://www.somewhere.com", defaultConverter.toText("https://www.somewhere.com"));
        assertEquals("https://www.somewhere.com", defaultConverter.toText("[Link](https://www.somewhere.com)"));
        assertEquals("https://www.somewhere.com", defaultConverter.toText("[](https://www.somewhere.com)"));
    }
    
    @Test
    void toText_HtmlRemoved() {
        // HtmlBlock is cleared 
        assertEquals("", defaultConverter.toText("<p>Hello</p>"));
        assertEquals("", defaultConverter.toText("<div>Hello</div>"));
        
        // HtmlInline tags are removed
        assertEquals("Hello", defaultConverter.toText("<span>Hello</span>"));
        assertEquals("This text is red", defaultConverter.toText("<font color=\"red\">This text is red</font>"));
    }

    @Test
    void toDiv_Blank() {
        assertEquals("", defaultConverter.toDiv(null));
        assertEquals("", defaultConverter.toDiv(""));
        assertEquals(" ", defaultConverter.toDiv(" "));
    }

    @Test
    void toDiv_Rendered() {
        String markdown = "Hello World";
        String expected = "<div class=\"markdown-body\"><p>Hello World</p>\n</div>";
        assertEquals(expected, defaultConverter.toDiv(markdown));
        
        markdown = "[link](https://www.somewhere.com)";
        expected = "<div class=\"markdown-body\"><p><a rel=\"nofollow\" href=\"https://www.somewhere.com\">link</a></p>\n</div>";
        assertEquals(expected, defaultConverter.toDiv(markdown));
    }
    
    @Test
    void toHtmlBody_Blank() {
        // Empty string no dark mode is blank
        String html = defaultConverter.toHtmlBody("", false);
        assertEquals("", html);
        
        // Dark mode always has a html body
        html = defaultConverter.toHtmlBody("", true);
        assertTrue(html.startsWith("<!DOCTYPE html>"));
        assertTrue(html.endsWith("</html>"));
        assertTrue(html.contains("<body class=\"dark-mode\">"));
        assertTrue(html.contains("<div class=\"markdown-body\"></div>"));
    }

    @Test
    void toHtmlBody_Rendered() {
        String html = defaultConverter.toHtmlBody("Hello World", false);
        assertTrue(html.startsWith("<!DOCTYPE html>"));
        assertTrue(html.endsWith("</html>"));
        assertTrue(html.contains("<body class=\"\">"));
        assertTrue(html.contains("<div class=\"markdown-body\"><p>Hello World</p>\n</div>"));
    }
    
    @Test
    void toHtmlBody_DarkMode() {
        String html = defaultConverter.toHtmlBody("Hello World", true);
        assertTrue(html.startsWith("<!DOCTYPE html>"));
        assertTrue(html.endsWith("</html>"));
        assertTrue(html.contains("<body class=\"dark-mode\">"));
        assertTrue(html.contains("<div class=\"markdown-body\"><p>Hello World</p>\n</div>"));
    }
    
    @Test
    void toHtml_Blank() {
        assertEquals("", defaultConverter.toHtml(null));
        assertEquals("", defaultConverter.toHtml(""));
        assertEquals(" ", defaultConverter.toHtml(" "));
    }

    @Test
    void toHtml_Rendered() {
        assertEquals("<p>Hello World</p>\n", defaultConverter.toHtml("Hello World"));
        
        String markdown = "# H1\n"
                        + "*italic* and **bold**"
                        + "\n"
                        + "[link](https://www.somewhere.com)"
                        + "\n"
                        + "- list";
        
        String expected = "<h1>H1</h1>"
                        + "\n"
                        + "<p><em>italic</em> and <strong>bold</strong>"
                        + "<br/>"
                        + "<a rel=\"nofollow\" href=\"https://www.somewhere.com\">link</a></p>\n"
                        + "<ul>"
                        + "\n"
                        + "<li>list</li>"
                        + "\n"
                        + "</ul>"
                        + "\n";
        
        assertEquals(expected, defaultConverter.toHtml(markdown));
    }

    @Test
    void toHtml_Softbreak() {
        // One break should be <br>
        assertEquals("<p>Hello<br/>World</p>\n", defaultConverter.toHtml("Hello\nWorld"));
        
        // Two or more breaks should be separate <p> blocks
        assertEquals("<p>Hello</p>\n<p>World</p>\n", defaultConverter.toHtml("Hello\n\nWorld"));
    }
    
    @Test
    void toHtml_NotEscapeHtml() {
        // By default, Html should not be escaped
        String html = "<a href=\"https://www.somewhere.com\">Link</a>";
        assertEquals("<p>" + html + "</p>\n", defaultConverter.toHtml(html));
    }
    
    @Test
    void toHtml_EscapeHtml() {
        // Html should be escaped
        MarkdownConverter converter = MarkdownConverter.builder()
                                                       .escapeHtml(true)
                                                       .build();
        
        String html = "<a href=\"https://www.somewhere.com\">Link</a>";
        assertEquals("<p>&lt;a href=&quot;https://www.somewhere.com&quot;&gt;Link&lt;/a&gt;</p>\n", converter.toHtml(html));
    }

    @Test
    void toHtml_LinkNotExternal() {
        // By default, links not external
        assertEquals("<p><a rel=\"nofollow\" href=\"https://www.archimatetool.com\">https://www.archimatetool.com</a></p>\n",
                defaultConverter.toHtml("https://www.archimatetool.com"));
    }
    
    @Test
    void toHtml_LinkExternal() {
        // Links are external
        MarkdownConverter converter = MarkdownConverter.builder()
                                                       .externalLinks(true)
                                                       .build();
        
        assertEquals("<p><a rel=\"nofollow\" href=\"https://www.archimatetool.com\" target=\"_blank\">https://www.archimatetool.com</a></p>\n",
                converter.toHtml("https://www.archimatetool.com"));
    }
}
