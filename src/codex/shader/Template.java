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
    
    public Template() {}
    
    @Override
    public Template load(AssetInfo assetInfo) throws IOException {
        var template = new Template();
        var br = new BufferedReader(new InputStreamReader(assetInfo.openStream()));
        String line = null;
        while ((line = br.readLine()) != null) {
            template.text.add(line);
        }
        return template;
    }
    
    public void write(FileWriter writer, String indent) throws IOException {
        for (String t : text) {
            writer.write(indent+t+"\n");
        }
    }
    
}
