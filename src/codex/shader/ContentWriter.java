/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.shader;

import com.jme3.asset.AssetManager;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author codex
 */
public abstract class ContentWriter {
    
    public static final String TAB = "    ";
    
    protected FileWriter writer;
    protected String indent = "";
    private int indentLength = 0;
    
    public ContentWriter(FileWriter writer, int indent) {
        this.writer = writer;
        indent(indent);
    }
    
    public abstract void write() throws IOException;
    
    protected final void indent(int n) {
        indentLength += n;
        boolean reset = n < 0;
        if (reset) indent = "";
        for (int i = 0; i < (!reset ? n : indentLength); i++) {
            indent += TAB;
        }
    }
    protected void write(String line) throws IOException {
        writer.write(indent+line+"\n");
    }
    protected void writeTemplate(AssetManager assetManager, String asset) throws IOException {
        var t = (Template)assetManager.loadAsset(asset);
        t.write(writer, indent);
    }
    protected void writeShaderDeclaration(String versions, String vert, String frag) throws IOException {
        if (vert.startsWith("/")) {
            vert = vert.substring(1);
        }
        if (frag.startsWith("/")) {
            frag = frag.substring(1);
        }
        write("VertexShader   "+versions+" : "+vert);
        write("FragmentShader "+versions+" : "+frag);
    }
    
}
