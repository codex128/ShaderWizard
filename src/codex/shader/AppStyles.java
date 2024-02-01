/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.shader;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.simsilica.lemur.Button;
import com.simsilica.lemur.Checkbox;
import com.simsilica.lemur.Command;
import com.simsilica.lemur.GuiGlobals;
import com.simsilica.lemur.HAlignment;
import com.simsilica.lemur.Insets3f;
import com.simsilica.lemur.TextField;
import com.simsilica.lemur.component.IconComponent;
import com.simsilica.lemur.component.QuadBackgroundComponent;
import com.simsilica.lemur.component.TbtQuadBackgroundComponent;
import com.simsilica.lemur.style.Attributes;
import com.simsilica.lemur.style.Styles;

/**
 *
 * @author codex
 */
public class AppStyles {
    
    public static final String STYLE = "app";
    
    public static void load(AssetManager assetManager) {
        
        Styles styles = GuiGlobals.getInstance().getStyles();
        
        TextureKey texKey = new TextureKey("/com/simsilica/lemur/icons/bordered-gradient.png");
        texKey.setGenerateMips(false);        
        TbtQuadBackgroundComponent gradient = TbtQuadBackgroundComponent.create(
            assetManager.loadTexture(texKey),
            1, 1, 1, 126, 126, 1f, false
        );
        texKey = new TextureKey("/com/simsilica/lemur/icons/double-gradient-128.png");
        texKey.setGenerateMips(false);
        QuadBackgroundComponent doubleGradient = new QuadBackgroundComponent(new ColorRGBA(0.5f, 0.75f, 0.85f, 0.5f));
        doubleGradient.setTexture(assetManager.loadTexture(texKey));
        
        final Command<Button> pressedCommand = (Button source) -> {
            if (source.isPressed()) {
                source.move(1, -1, 0);
            } else {
                source.move(-1, 1, 0);
            }
        };
        final Command<Button> repeatCommand = new Command<Button>() {
            private long startTime;
            private long lastClick;
            @Override
            public void execute( Button source ) {
                // Only do the repeating click while the mouse is
                // over the button (and pressed of course)
                if( source.isPressed() && source.isHighlightOn() ) {
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    // After half a second pause, click 8 times a second
                    if( elapsedTime > 500 ) {
                        if( elapsedTime - lastClick > 125 ) {  
                            source.click();
                            // Try to quantize the last click time to prevent drift
                            lastClick = ((elapsedTime - 500) / 125) * 125 + 500;
                        }
                    } 
                } else {
                    startTime = System.currentTimeMillis();
                    lastClick = 0;
                }
            }       
        };
        
        Attributes attr = styles.getSelector(STYLE);
        attr.set("font", assetManager.loadFont("Interface/Fonts/FreeMono.fnt"));
        attr.set("fontSize", 14);
        //attr.set("insets", new Insets3f(3, 0, 3, 0));
        
        attr = styles.getSelector(TextField.ELEMENT_ID, STYLE);
        attr.set("insets", new Insets3f(3, 0, 3, 20));
        attr.set("preferredWidth", 250);
        attr.set("fontSize", 13);
        
        attr = styles.getSelector(Checkbox.ELEMENT_ID, STYLE);
        attr.set("insets", new Insets3f(3, 3, 5, 0));
        
        attr = styles.getSelector("label", STYLE);
        attr.set("insets", new Insets3f(2, 2, 0, 2));
        attr.set("color", new ColorRGBA(0.5f, 0.75f, 0.75f, 1f));
        
        attr = styles.getSelector("title.label", STYLE);
        attr.set("fontSize", 18);
        attr.set("insets", new Insets3f(2, 15, 7, 2));
        
        attr = styles.getSelector("describer.label", STYLE);
        attr.set("insets", new Insets3f(3, 2, 3, 2));
        
        attr = styles.getSelector("note.label", STYLE);
        attr.set("textHAlignment", HAlignment.Right);
        attr.set("fontSize", 13);
        attr.set("insets", new Insets3f(2, 2, 0, 5));
        
        attr = styles.getSelector("container", STYLE);
        attr.set("background", gradient.clone());
        attr.get("background", TbtQuadBackgroundComponent.class).setColor(new ColorRGBA(0.25f, 0.5f, 0.5f, 0.4f));
        
        attr = styles.getSelector("title", STYLE);
        attr.set("color", new ColorRGBA(0.8f, 0.9f, 1, 0.85f));
        attr.set("highlightColor", new ColorRGBA(1, 0.8f, 1, 0.85f));
        attr.set("shadowOffset", new Vector3f(2, -2, -1));
        attr.set("background", new QuadBackgroundComponent(new ColorRGBA(0.75f, 0.75f, 0.85f, 0.5f)));
        attr.set("insets", new Insets3f(2, 2, 2, 2));
        
        attr = styles.getSelector("button", STYLE);
        attr.set("background", gradient.clone());
        attr.get("background", TbtQuadBackgroundComponent.class).setColor(new ColorRGBA(0, 0.75f, 0.75f, 0.5f));
        attr.set("color", new ColorRGBA(0.8f, 0.9f, 1, 0.85f));
        attr.set("insets", new Insets3f(2, 2, 2, 2));
        
        attr = styles.getSelector("slider", STYLE);
        attr.set("insets", new Insets3f(1, 3, 1, 2));
        
        attr = styles.getSelector("slider", "button", STYLE);
        attr.set("background", doubleGradient.clone());
        attr.get("background", QuadBackgroundComponent.class).setColor(new ColorRGBA(0.5f, 0.75f, 0.75f, 0.5f));
        attr.set("insets", new Insets3f(0, 0, 0, 0));
        
        attr = styles.getSelector("slider.thumb.button", STYLE);
        attr.set("text", "[]");
        attr.set("color", new ColorRGBA(0.6f, 0.8f, 0.8f, 0.85f));
        
        attr = styles.getSelector("slider.left.button", STYLE);
        attr.set("text", "-");
        attr.set("background", doubleGradient.clone());
        attr.get("background", QuadBackgroundComponent.class).setColor(new ColorRGBA(0.5f, 0.75f, 0.75f, 0.5f));
        attr.get("background", QuadBackgroundComponent.class).setMargin(5, 0);
        attr.set("color", new ColorRGBA(0.6f, 0.8f, 0.8f, 0.85f));
        
        attr = styles.getSelector("slider.range", STYLE);
        attr.set("background", new QuadBackgroundComponent(new ColorRGBA(0.05f, 0.2f, 0.2f, 0.5f)));
        
        attr = styles.getSelector("checkbox", STYLE);
        IconComponent onView = new IconComponent("/com/simsilica/lemur/icons/Glass-check-on.png", 1f, 0, 0, 1f, false);
        onView.setColor(new ColorRGBA(0.5f, 0.9f, 0.9f, 0.9f));
        onView.setMargin(5, 0);
        IconComponent offView = new IconComponent("/com/simsilica/lemur/icons/Glass-check-off.png", 1f, 0, 0, 1f, false);
        offView.setColor(new ColorRGBA(0.6f, 0.8f, 0.8f, 0.25f));
        offView.setMargin(5, 0);
        attr.set("onView", onView);
        attr.set("offView", offView);
        attr.set("color", new ColorRGBA(0.8f, 0.9f, 1, 0.85f));
        
        attr = styles.getSelector("rollup", STYLE);
        attr.set("background", gradient.clone());
        attr.get("background", TbtQuadBackgroundComponent.class).setColor(new ColorRGBA(0.25f, 0.5f, 0.5f, 0.5f));
        
        attr = styles.getSelector("tabbedPanel", STYLE);
        attr.set("activationColor", new ColorRGBA(0.8f, 0.9f, 1, 0.85f));
        
        attr = styles.getSelector("tabbedPanel.container", STYLE);
        attr.set("background", null);
        
        attr = styles.getSelector("tab.button", STYLE);
        attr.set("background", gradient.clone());
        attr.get("background", TbtQuadBackgroundComponent.class).setColor(new ColorRGBA(0.25f, 0.5f, 0.5f, 0.5f));
        attr.set("color", new ColorRGBA(0.4f, 0.45f, 0.5f, 0.85f));
        attr.set("insets", new Insets3f(4, 2, 0, 2));
        
    }
    
}
