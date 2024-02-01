/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.shader;

import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author codex
 */
public abstract class TechniqueWriter extends ContentWriter {
    
    private String name;

    public TechniqueWriter(FileWriter writer, int indent, String name) {
        super(writer, indent);
        this.name = name;
    }
    
    @Override
    public void write() throws IOException {
        write("Technique "+name+(name.isEmpty() ? "" : " ")+"{");
        indent(1);
        writeShaderDeclaration();
        write("WorldParameters {");
        indent(1);
        writeWorldParameters();
        indent(-1);
        write("}");
        write("Defines {");
        indent(1);
        writeDefines();
        indent(-1);
        write("}");
        if (useForcedRenderState()) {
            write("ForcedRenderState {");
            indent(1);
            writeForcedRenderState();
            indent(-1);
            write("}");
        }
        indent(-1);
        write("}");
    }
    
    public abstract void writeShaderDeclaration() throws IOException;
    public abstract void writeWorldParameters() throws IOException;
    public abstract void writeDefines() throws IOException;
    public abstract boolean useForcedRenderState() throws IOException;
    public abstract void writeForcedRenderState() throws IOException;
    
}
