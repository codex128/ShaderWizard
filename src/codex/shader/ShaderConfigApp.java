package codex.shader;

import com.jme3.app.SimpleApplication;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.math.Vector3f;
import com.jme3.system.AppSettings;
import com.simsilica.lemur.Axis;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Checkbox;
import com.simsilica.lemur.Container;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Insets3f;
import com.simsilica.lemur.Label;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.VAlignment;
import com.simsilica.lemur.component.DynamicInsetsComponent;
import com.simsilica.lemur.component.SpringGridLayout;
import com.simsilica.lemur.style.ElementId;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class ShaderConfigApp extends SimpleApplication implements AnalogListener {
    
    private static final float INACTIVE = .15f;
    private static final float SCROLL_SPEED = 1500f;
    
    private Container main;
    private RadioSet template;
    private Checkbox glslCompat;
    private Checkbox vertexColors;
    private Checkbox normals, tangents;
    private Checkbox skinning;
    private Checkbox shadows;
    private Checkbox instancing;
    private Checkbox createVertShader, createFragShader;
    private Checkbox glow, seperateGlowFragment;
    private Checkbox useSharedFolder;
    private Checkbox glsl100, glsl110, glsl120, glsl300, glsl310, glsl450;
    private LinkedList<Checkbox> glslVersions = new LinkedList<>();
    private TextField matDefName, fragmentName, vertexName;
    private TextField existingVert, existingFrag;
    private TextField glowFragmentName;
    private TextField preShadowVert, preShadowFrag, postShadowVert, postShadowFrag;
    private TextField assetDirectory;
    private TextField matDefExport, shaderExport, sharedFolderExport;
    
    private FileWriter writer;
    private String indent = "";
    private int indentLength = 0;
    
    public static void main(String[] args) {
        ShaderConfigApp app = new ShaderConfigApp();
        var settings = new AppSettings(true);
        settings.setTitle("JME3 Shader Wizard");
        app.setSettings(settings);
        app.start();
    }

    @Override
    public void simpleInitApp() {
        
        GuiGlobals.initialize(this);
        AppStyles.load(assetManager);
        GuiGlobals.getInstance().getStyles().setDefaultStyle(AppStyles.STYLE);
        
        assetManager.registerLoader(Template.class, "temp");
        
        main = new Container();
        main.setBackground(null);
        main.setLocalTranslation(300, 750, 0);
        guiNode.attachChild(main);
        
        var temp = main.addChild(new Container());
        temp.addChild(new Label("Template:", new ElementId("title.label")));
        var tempSelect = temp.addChild(new Container());
        tempSelect.setBackground(null);
        template = new RadioSet(
            tempSelect.addChild(new Checkbox("Custom...")),
            tempSelect.addChild(new Checkbox("PBR Lighting")),
            tempSelect.addChild(new Checkbox("Phong Lighting")),
            tempSelect.addChild(new Checkbox("Unshaded"))
        );
        main.attachChild(template);
        template.setCurrentBox(0);
        for (int i = 1; i < 4; i++) {
            template.getBox(i).setAlpha(INACTIVE);
        }
        
        var fileSettings = main.addChild(new Container());
        fileSettings.addChild(new Label("Files:", new ElementId("title.label")));
        var nameInput = fileSettings.addChild(new Container());
        createVertShader = fileSettings.addChild(new Checkbox("Create Vertex Shader"));
        createFragShader = fileSettings.addChild(new Checkbox("Create Fragment Shader"));
        nameInput.setBackground(null);
        nameInput.setLayout(new SpringGridLayout(Axis.Y, Axis.X));
        nameInput.addChild(new Label("MatDef:", new ElementId("note.label")), 0, 0);
        matDefName = nameInput.addChild(new TextField("myMatDef"), 0, 1);
        matDefName.setInsets(new Insets3f(3, 0, 3, 0));
        nameInput.addChild(new Label(".j3md"), 0, 2);
        nameInput.addChild(new Label("New Vertex:", new ElementId("note.label")), 1, 0)
                .addControl(new CheckboxFadeLink(createVertShader, true, INACTIVE));
        vertexName = nameInput.addChild(new TextField("myVert"), 1, 1);
        vertexName.setInsets(new Insets3f(3, 0, 3, 0));
        vertexName.addControl(new CheckboxFadeLink(createVertShader, true, INACTIVE));
        nameInput.addChild(new Label(".vert"), 1, 2).addControl(new CheckboxFadeLink(createVertShader, true, INACTIVE));
        nameInput.addChild(new Label("Existing Vertex:", new ElementId("note.label")), 2, 0)
                .addControl(new CheckboxFadeLink(createVertShader, false, INACTIVE));
        existingVert = nameInput.addChild(new TextField("Common/MatDefs/Misc/Unshaded.vert"), 2, 1);
        existingVert.addControl(new CheckboxFadeLink(createVertShader, false, INACTIVE));
        existingVert.setInsets(new Insets3f(3, 0, 3, 0));
        nameInput.addChild(new Label("New Fragment:", new ElementId("note.label")), 3, 0)
                .addControl(new CheckboxFadeLink(createFragShader, true, INACTIVE));
        fragmentName = nameInput.addChild(new TextField("myFrag"), 3, 1);
        fragmentName.addControl(new CheckboxFadeLink(createFragShader, true, INACTIVE));
        fragmentName.setInsets(new Insets3f(3, 0, 3, 0));
        nameInput.addChild(new Label(".frag"), 3, 2)
                .addControl(new CheckboxFadeLink(createFragShader, true, INACTIVE));
        nameInput.addChild(new Label("Existing Fragment:", new ElementId("note.label")), 4, 0)
                .addControl(new CheckboxFadeLink(createFragShader, false, INACTIVE));
        existingFrag = nameInput.addChild(new TextField("Common/MatDefs/Misc/Unshaded.frag"), 4, 1);
        existingFrag.addControl(new CheckboxFadeLink(createFragShader, false, INACTIVE));
        existingFrag.setInsets(new Insets3f(3, 0, 3, 0));
        
        var export = main.addChild(new Container());
        export.addChild(new Label("Export:", new ElementId("title.label")));
        var assetRoot = export.addChild(new Container());
        assetRoot.setBackground(null);
        assetRoot.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
        assetRoot.addChild(new Label("Asset Folder:", new ElementId("note.label")));
        assetDirectory = assetRoot.addChild(new TextField(System.getProperty("user.home")));
        assetDirectory.setInsets(new Insets3f(3, 0, 3, 20));
        //CheckboxLink.link(useSharedFolder, sharedFolderExport, true, .4f);
        //useSharedFolder.addControl();
        var indExport = export.addChild(new Container());
        indExport.setLayout(new SpringGridLayout(Axis.Y, Axis.X));
        indExport.setBackground(null);
        indExport.addChild(new Label("MatDef Folder:", new ElementId("note.label")), 0, 0);
        matDefExport = indExport.addChild(new TextField("/MatDefs/"), 0, 1);
        indExport.addChild(new Label("Shader Folder:", new ElementId("note.label")), 1, 0);
        shaderExport = indExport.addChild(new TextField("/Shaders/"), 1, 1);
        //CheckboxLink.link(useSharedFolder, indExport, false, INACTIVE);
        //useSharedFolder.addControl(new CheckboxFadeLink(indExport, false, INACTIVE));
        var sharedExport = export.addChild(new Container());
        sharedExport.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
        sharedExport.setBackground(null);
        useSharedFolder = sharedExport.addChild(new Checkbox("Create Shared Folder"));
        sharedFolderExport = sharedExport.addChild(new TextField("/Shaders/myShader"));
        sharedFolderExport.addControl(new CheckboxFadeLink(useSharedFolder, true, INACTIVE));
        indExport.addControl(new CheckboxFadeLink(useSharedFolder, false, INACTIVE));
        var expBtnContainer = export.addChild(new Container());
        expBtnContainer.setBackground(null);
        expBtnContainer.setInsets(new Insets3f(6, 3, 6, 3));
        var exportButton = expBtnContainer.addChild(new Button("Export"));
        exportButton.setPreferredSize(new Vector3f(150, 30, 0));
        exportButton.setInsets(new Insets3f(5, 0, 5, 0));
        exportButton.setInsetsComponent(new DynamicInsetsComponent(.5f, .1f, .5f, .9f));
        exportButton.setTextHAlignment(HAlignment.Center);
        exportButton.setTextVAlignment(VAlignment.Center);
        exportButton.setFontSize(16);
        exportButton.addClickCommands((Button source) -> {
            export();
        });
        
        var versions = main.addChild(new Container());
        versions.addChild(new Label("GLSL Versions:", new ElementId("title.label")));
        var vInput = versions.addChild(new Container());
        vInput.setLayout(new SpringGridLayout(Axis.X, Axis.Y));
        vInput.setBackground(null);
        vInput.setInsets(new Insets3f(0, 0, 0, 170));
        glslVersions.add(vInput.addChild(new Checkbox("100"), 0, 0));
        glslVersions.add(vInput.addChild(new Checkbox("120"), 1, 0));
        glslVersions.add(vInput.addChild(new Checkbox("150"), 2, 0));
        glslVersions.add(vInput.addChild(new Checkbox("200"), 0, 1));
        glslVersions.add(vInput.addChild(new Checkbox("210"), 1, 1));
        glslVersions.add(vInput.addChild(new Checkbox("300"), 0, 2));
        glslVersions.add(vInput.addChild(new Checkbox("310"), 1, 2));
        glslVersions.add(vInput.addChild(new Checkbox("320"), 2, 2));
        glslVersions.add(vInput.addChild(new Checkbox("330"), 3, 2));
        glslVersions.add(vInput.addChild(new Checkbox("400"), 0, 3));
        glslVersions.add(vInput.addChild(new Checkbox("410"), 1, 3));
        glslVersions.add(vInput.addChild(new Checkbox("420"), 2, 3));
        glslVersions.add(vInput.addChild(new Checkbox("430"), 3, 3));
        glslVersions.add(vInput.addChild(new Checkbox("440"), 4, 3));
        glslVersions.add(vInput.addChild(new Checkbox("450"), 5, 3));
        
        var shadersMain = main.addChild(new Container());
        shadersMain.addChild(new Label("Shader Config:", new ElementId("title.label")));
        var shadersConfig = shadersMain.addChild(new Container());
        shadersConfig.setBackground(null);
        glslCompat = shadersConfig.addChild(new Checkbox("Import GLSLCompat"));
        vertexColors = shadersConfig.addChild(new Checkbox("Vertex Colors"));
        normals = shadersConfig.addChild(new Checkbox("Normals"));
        tangents = shadersConfig.addChild(new Checkbox("Tangents"));
        skinning = shadersConfig.addChild(new Checkbox("Skinning"));
        instancing = shadersConfig.addChild(new Checkbox("Instancing"));
        createGlowWindow(shadersConfig);
        createShadowsWindow(shadersConfig);
        var padding = shadersMain.addChild(new Container());
        padding.setPreferredSize(new Vector3f(20, 7, 0));
        padding.setBackground(null);
        
        setDefaultSettings();
        
        inputManager.addMapping("scroll-up", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));
        inputManager.addMapping("scroll-down", new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addListener(this, "scroll-up", "scroll-down");
        
    }
    @Override
    public void onAnalog(String name, float value, float tpf) {
        if (name.equals("scroll-up")) {
            main.move(0, tpf*SCROLL_SPEED, 0);
        } else if (name.equals("scroll-down")) {
            main.move(0, -tpf*SCROLL_SPEED, 0);
        }
    }
    
    private void setDefaultSettings() {
        glslCompat.setChecked(true);
        normals.setChecked(true);
        skinning.setChecked(true);
        instancing.setChecked(true);
        createVertShader.setChecked(true);
        createFragShader.setChecked(true);
        autocheckVersions("310", "300", "150", "100");
    }  
    private void autocheckVersions(String... versions) {
        main: for (var b : glslVersions) {
            for (String v : versions) {
                if (b.getText().equals(v)) {
                    b.setChecked(true);
                    continue main;
                }
            }
            b.setChecked(false);
        }
    }
    
    private void createGlowWindow(Container container) {
        glow = container.addChild(new Checkbox("Glow"));
        var glowSettings = container.addChild(new Container());
        glowSettings.setBackground(null);
        glowSettings.setInsets(new Insets3f(0, 50, 0, 0));
        glowSettings.setLayout(new SpringGridLayout(Axis.Y, Axis.X));
        seperateGlowFragment = glowSettings.addChild(new Checkbox("Use Seperate Glow Fragment:"), 0, 0);
        seperateGlowFragment.setInsets(new Insets3f(3, 0, 3, 10));
        glowFragmentName = glowSettings.addChild(new TextField("Shaders/myGlow.frag"), 0, 1);
        glowFragmentName.setInsets(new Insets3f(3, 5, 3, 20));
        //CheckboxLink.link(glow, glowSettings, true, INACTIVE);  
        glowSettings.addControl(new CheckboxFadeLink(glow, true, INACTIVE));
    }
    private void createShadowsWindow(Container container) {
        shadows = container.addChild(new Checkbox("Shadows"));
        var shadowSettings = container.addChild(new Container());
        shadowSettings.setBackground(null);
        shadowSettings.setInsets(new Insets3f(0, 50, 0, 0));
        shadowSettings.setLayout(new SpringGridLayout(Axis.Y, Axis.X));
        preShadowVert = createLabelledTextField(shadowSettings, "Pre-Shadow Vertex Shader:", "", 0, 0);
        preShadowVert.setText("Common/MatDefs/Shadow/PreShadow.vert");
        preShadowFrag = createLabelledTextField(shadowSettings, "Pre-Shadow Fragment Shader:", "", 1, 0);
        preShadowFrag.setText("Common/MatDefs/Shadow/PreShadow.frag");
        postShadowVert = createLabelledTextField(shadowSettings, "Post-Shadow Vertex Shader:", "", 2, 0);
        postShadowVert.setText("Common/MatDefs/Shadow/PostShadow.vert");
        postShadowFrag = createLabelledTextField(shadowSettings, "Post-Shadow Fragment Shader:", "", 3, 0);
        postShadowFrag.setText("Common/MatDefs/Shadow/PostShadow.frag");
        //CheckboxLink.link(shadows, shadowSettings, true, INACTIVE);
        shadowSettings.addControl(new CheckboxFadeLink(shadows, true, INACTIVE));
    }    
    private TextField createLabelledTextField(Container container, String name, String text, int y, int x) {
        float paddingVertical = 3;
        var label = container.addChild(new Label(name, new ElementId("note.label")), y, x);
        label.setInsets(new Insets3f(paddingVertical, 10, paddingVertical, 5));
        var field = container.addChild(new TextField(text), y, x+1);
        field.setInsets(new Insets3f(paddingVertical, 1, paddingVertical, 20));
        return field;
    }
    
    private boolean export() {
        if (!verifyComplete()) {
            return false;
        }
        createSharedFolder();
        try {
            int i = template.getCurrentIndex();
            switch (i) {
                case 0 -> exportCustom();
            }
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    private boolean verifyComplete() {
        if (matDefName.getText().isBlank() || fragmentName.getText().isBlank() || vertexName.getText().isBlank()) {
            return false;
        }
        if (useSharedFolder.isChecked() && sharedFolderExport.getText().isBlank()) {
            return false;
        }
        return true;
    }
    private void createSharedFolder() {
        if (useSharedFolder.isChecked()) {
            var folder = new File(getExportPath(""));
            if (!folder.exists()) {
                folder.mkdirs();
            }
        }
    }
    
    private void exportCustom() throws IOException {
        exportCustomMatDef();
        if (createVertShader.isChecked()) {
            exportCustomVertexShader();
        } else {
            
        }
        if (createFragShader.isChecked()) {
            exportCustomFragmentShader();
        } else {
            
        }
    }
    private void exportCustomMatDef() throws IOException {
        resetIndent();
        var file = new File(getExportPath(getMatDefExportTarget()));
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        writer = new FileWriter(file);
        write("MaterialDef "+matDefName.getText()+" {");
        indent(1);
        write("MaterialParameters {");
        indent(1);
        if (vertexColors.isChecked()) {
            write("");
            write("Boolean UseVertexColor");
        }
        if (instancing.isChecked()) {
            write("");
            write("// Instancing");
            write("Boolean UseInstancing");
        }
        if (skinning.isChecked()) {
            write("");
            write("// Skinning");
            writeTemplate("Templates/skinningMatParams.temp");
        }
        if (shadows.isChecked()) {
            write("");
            write("// Shadows");
            writeTemplate("Templates/shadowMatParams.temp");
        }
        write("");
        indent(-1);
        write("}");
        (new TechniqueWriter(writer, 1, "") {
            @Override
            public void writeShaderDeclaration() throws IOException {
                writeShaderDeclaration(getSupportedGlslVersions(), getVertexExportTarget(), getFragmentExportTarget());
            }
            @Override
            public void writeWorldParameters() throws IOException {
                writeTemplate(assetManager, "Templates/defaultWorldParameters.temp");
            }
            @Override
            public void writeDefines() throws IOException {
                if (vertexColors.isChecked()) {
                    write("VERTEX_COLOR : UseVertexColor");
                }
                if (instancing.isChecked()) {
                    write("INSTANCING : UseInstancing");
                }
                if (skinning.isChecked()) {
                    writeTemplate(assetManager, "Templates/skinningDefines.temp");
                }
            }
            @Override
            public boolean useForcedRenderState() throws IOException {
                return false;
            }
            @Override
            public void writeForcedRenderState() throws IOException {}
        }).write();
        if (shadows.isChecked()) {
            (new TechniqueWriter(writer, 1, "PreShadow") {
                @Override
                public void writeShaderDeclaration() throws IOException {
                    writeShaderDeclaration(getSupportedGlslVersions(), preShadowVert.getText(), preShadowFrag.getText());
                }
                @Override
                public void writeWorldParameters() throws IOException {
                    writeTemplate(assetManager, "Templates/defaultWorldParameters.temp");
                }
                @Override
                public void writeDefines() throws IOException {
                    if (instancing.isChecked()) {
                        write("INSTANCING : UseInstancing");
                    }
                    if (skinning.isChecked()) {
                        writeTemplate(assetManager, "Templates/skinningDefines.temp");
                    }
                }
                @Override
                public boolean useForcedRenderState() throws IOException {
                    return true;
                }
                @Override
                public void writeForcedRenderState() throws IOException {
                    writeTemplate(assetManager, "Templates/preShadowForcedRenderState.temp");
                }
            }).write();
            (new TechniqueWriter(writer, 1, "PostShadow") {
                @Override
                public void writeShaderDeclaration() throws IOException {
                    writeShaderDeclaration(getSupportedGlslVersions(), postShadowVert.getText(), postShadowFrag.getText());
                }
                @Override
                public void writeWorldParameters() throws IOException {
                    writeTemplate(assetManager, "Templates/defaultWorldParameters.temp");
                }
                @Override
                public void writeDefines() throws IOException {
                    if (instancing.isChecked()) {
                        write("INSTANCING : UseInstancing");
                    }
                    if (skinning.isChecked()) {
                        writeTemplate(assetManager, "Templates/skinningDefines.temp");
                    }
                    writeTemplate(assetManager, "Templates/postShadowDefines.temp");
                }
                @Override
                public boolean useForcedRenderState() throws IOException {
                    return true;
                }
                @Override
                public void writeForcedRenderState() throws IOException {
                    writeTemplate(assetManager, "Templates/postShadowForcedRenderState.temp");
                }
            }).write();
        }
        if (glow.isChecked()) {
            (new TechniqueWriter(writer, 1, "Glow") {
                @Override
                public void writeShaderDeclaration() throws IOException {
                    if (seperateGlowFragment.isChecked()) {
                        writeShaderDeclaration(getSupportedGlslVersions(), getVertexExportTarget(), glowFragmentName.getText());
                    } else {
                        writeShaderDeclaration(getSupportedGlslVersions(), getVertexExportTarget(), getFragmentExportTarget());
                    }
                }
                @Override
                public void writeWorldParameters() throws IOException {
                    writeTemplate(assetManager, "Templates/defaultWorldParameters.temp");
                }
                @Override
                public void writeDefines() throws IOException {
                    if (vertexColors.isChecked()) {
                        write("VERTEX_COLOR : UseVertexColor");
                    }
                    if (instancing.isChecked()) {
                        write("INSTANCING : UseInstancing");
                    }
                    if (skinning.isChecked()) {
                        writeTemplate(assetManager, "Templates/skinningDefines.temp");
                    }
                }
                @Override
                public boolean useForcedRenderState() throws IOException {
                    return false;
                }
                @Override
                public void writeForcedRenderState() throws IOException {}
            }).write();
        }
        indent(-1);
        write("}");
        writer.close();
    }
    private void exportCustomVertexShader() throws IOException {
        resetIndent();
        var file = new File(getExportPath(getVertexExportTarget()));
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();
        writer = new FileWriter(file);
        write("");
        if (glslCompat.isChecked()) {
            write("#import \"Common/ShaderLib/GLSLCompat.glsllib\"");
        }
        if (instancing.isChecked()) {
            write("#import \"Common/ShaderLib/Instancing.glsllib\"");
        }
        if (skinning.isChecked()) {
            write("#import \"Common/ShaderLib/Skinning.glsllib\"");
            write("#import \"Common/ShaderLib/MorphAnim.glsllib\"");
        }
        write("");
        write("attribute vec3 inPosition;");
        if (normals.isChecked()) {
            write("attribute vec3 inNormal;");
        }
        if (tangents.isChecked()) {
            write("attribute vec4 inTangent;");
        }
        write("attribute vec2 inTexCoord;");
        write("");
        write("varying vec3 wPosition;");
        if (normals.isChecked()) {
            write("varying vec3 wNormal;");
        }
        if (tangents.isChecked()) {
            write("varying vec4 wTangent;");
        }
        write("varying vec2 texCoord;");
        if (vertexColors.isChecked()) {
            write("");
            write("#ifdef VERTEX_COLOR");
                indent(1);
                write("attribute vec4 inColor;");
                write("varying vec4 vertexColor;");
            indent(-1);
            write("#endif");
        }
        write("");
        write("void main() {");
            indent(1);
            write("");
            write("vec4 modelSpacePos = vec4(inPosition, 1.0);");
            if (normals.isChecked()) {                
                write("vec3 modelSpaceNorm = inNormal;");
            }
            if (tangents.isChecked()) {
                write("vec3 modelSpaceTan = inTangent.xyz;");
            }
            if (skinning.isChecked()) {
                write("");
                write("#ifdef NUM_MORPH_TARGETS");
                    indent(1);
                    if (!normals.isChecked()) {
                        write("Morph_Compute(modelSpacePos);");
                    } else if (!tangents.isChecked()) {
                        write("Morph_Compute(modelSpacePos, modelSpaceNorm);");
                    } else {
                        write("Morph_Compute(modelSpacePos, modelSpaceNorm, modelSpaceTan);");
                    }
                indent(-1);
                write("#endif");
                write("#ifdef NUM_BONES");
                    indent(1);
                    if (!normals.isChecked()) {
                        write("Skinning_Compute(modelSpacePos);");
                    } else if (!tangents.isChecked()) {
                        write("Skinning_Compute(modelSpacePos, modelSpaceNorm);");
                    } else {
                        write("Skinning_Compute(modelSpacePos, modelSpaceNorm, modelSpaceTan);");
                    }
                indent(-1);
                write("#endif");
            }
            write("");
            if (instancing.isChecked()) {
                write("gl_Position = TransformWorldViewProjection(modelSpacePos);");
                write("wPosition = TransformWorld(modelSpacePos).xyz;");
                if (normals.isChecked()) {
                    write("wNormal = TransformWorldNormal(modelSpaceNorm);");
                }
                if (tangents.isChecked()) {
                    write("wTangent = vec4(TransformWorldNormal(modelSpaceTan), inTangent.w);");
                }
            } else {
                write("gl_Position = g_WorldViewProjectionMatrix * modelSpacePos;");
                write("wPosition = (g_WorldMatrix * modelSpacePos).xyz;");
                if (normals.isChecked()) {
                    write("wNormal = g_WorldNormalMatrix * modelSpaceNorm);");
                }
                if (tangents.isChecked()) {
                    write("wTangent = vec4(g_WorldNormalMatrix * modelSpaceTan, inTangent.w);");
                }
            }
            write("texCoord = inTexCoord;");
            if (vertexColors.isChecked()) {
                write("");
                write("#ifdef VERTEX_COLOR");
                    indent(1);
                    write("vertexColor = inColor;");
                indent(-1);
                write("#endif");
            }
            write("");
        indent(-1);
        write("}");
        writer.close();
    }
    private void exportCustomFragmentShader() throws IOException {
        resetIndent();
        var file = new File(getExportPath(getFragmentExportTarget()));
        writer = new FileWriter(file);
        write("");
        if (glslCompat.isChecked()) {
            write("#import \"Common/ShaderLib/GLSLCompat.glsllib\"");
        }
        write("");
        write("varying vec3 wPosition;");
        if (normals.isChecked()) {
            write("varying vec3 wNormal;");
        }
        if (tangents.isChecked()) {
            write("varying vec4 wTangent;");
        }
        write("varying vec2 texCoord;");
        if (vertexColors.isChecked()) {
            write("");
            write("#ifdef VERTEX_COLOR");
                indent(1);
                write("varying vec4 vertexColor;");
            indent(-1);
            write("#endif");
        }
        write("");
        write("void main() {");
            indent(1);
            write("gl_FragColor = vec4(1.0);");
        indent(-1);
        write("}");
        writer.close();
    }
    
    private void exportPBR() {
        
    }    
    private void exportPhong() {
        
    }
    private void exportUnshaded() {
        
    }
    
    private String getMatDefExportTarget() {
        String name = addExtension(matDefName.getText().trim(), ".j3md");
        if (useSharedFolder.isChecked()) {
            return name;
        } else {
            return combinePaths(matDefExport.getText(), name);
        }
    }
    private String getVertexExportTarget() {
        if (!createVertShader.isChecked()) {
            return existingVert.getText();
        }
        String name = addExtension(vertexName.getText().trim(), ".vert");
        if (useSharedFolder.isChecked()) {
            return name;
        } else {
            return combinePaths(shaderExport.getText(), name);
        }
    }
    private String getFragmentExportTarget() {
        if (!createFragShader.isChecked()) {
            return existingFrag.getText();
        }
        String name = addExtension(fragmentName.getText().trim(), ".frag");
        if (useSharedFolder.isChecked()) {
            return name;
        } else {
            return combinePaths(shaderExport.getText(), name);
        }
    }
    private String getExportPath(String ext) {
        if (useSharedFolder.isChecked()) {
            return combinePaths(combinePaths(assetDirectory.getText().trim(),
                    sharedFolderExport.getText().trim()), ext);
        } else {
            return combinePaths(assetDirectory.getText().trim(), ext);
        }
    }
    private String combinePaths(String path1, String path2) {
        boolean a = path1.endsWith("/");
        boolean b = path2.startsWith("/");
        return path1+(!a && !b ? "/" : "")+(a && b ? path2.substring(1) : path2);
    }
    private String addExtension(String path, String extension) {
        if (path.endsWith(extension)) {
            return path;
        } else {
            return path+extension;
        }
    }
    private String getSupportedGlslVersions() {
        String render = "";
        for (var it = glslVersions.descendingIterator(); it.hasNext();) {
            var v = it.next();
            if (v.isChecked()) {
                render += (render.isEmpty() ? "" : " ")+"GLSL"+v.getText();
            }
        }
        return render;
    }
    
    private void indent(int n) {
        indentLength += n;
        boolean reset = n < 0;
        if (reset) indent = "";
        for (int i = 0; i < (!reset ? n : indentLength); i++) {
            indent += ContentWriter.TAB;
        }
    }
    private void resetIndent() {
        indentLength = 0;
        indent = "";
    }
    private void write(String line) throws IOException {
        if (writer != null) {
            writer.write(indent+line+"\n");
        }
    }
    private void writeTemplate(String asset) throws IOException {
        var t = (Template)assetManager.loadAsset(asset);
        t.write(writer, indent);
    }
    
}