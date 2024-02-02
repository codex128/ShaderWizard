/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.shader;

import com.jme3.asset.AssetInfo;
import com.jme3.asset.AssetLoader;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

/**
 *
 * @author codex
 */
public class Template implements AssetLoader {
    
    private final LinkedList<String> text = new LinkedList<>();
    private TemplateKeyRenderer renderer;
    
    public Template() {}
    
    @Override
    public Template load(AssetInfo assetInfo) throws IOException {
        var template = new Template();
        var br = new BufferedReader(new InputStreamReader(assetInfo.openStream()));
        String line;
        while ((line = br.readLine()) != null) {
            template.text.add(line);
        }
        return template;
    }
    
    public void write(FileWriter writer, String indent) throws IOException {
        for (String t : text) {
            if (renderer != null) {
                t = render(t);
            }
            writer.write(indent+t+"\n");
        }
    }
    private String render(String line) {
        boolean build = false;
        StringBuilder render = new StringBuilder();
        StringBuilder chunk = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '[') {
                build = true;
            } else if (build) {
                if (c == ']') {
                    build = false;
                    String replacement = renderer.makeReplacementString(chunk.toString());
                    render.append(replacement);
                    chunk.delete(0, chunk.length());
                } else {
                    chunk.append(c);
                }
            } else {
                render.append(c);
            }
        }
        return render.toString();
    }
    
    public void setKeyRenderer(TemplateKeyRenderer replacer) {
        this.renderer = replacer;
    }
    
}
