/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.shader;

import codex.boost.control.TypeControl;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.simsilica.lemur.Checkbox;
import com.simsilica.lemur.Panel;
import com.simsilica.lemur.core.VersionedReference;

/**
 *
 * @author codex
 */
public class CheckboxFadeLink extends TypeControl<Panel> {
    
    private final VersionedReference<Boolean> ref;
    private final boolean fadeOnDisable;
    private final float fadeAlpha;
    
    public CheckboxFadeLink(Checkbox box, boolean fadeOnDisable, float fadeAlpha) {
        super(Panel.class);
        this.ref = box.getModel().createReference();
        this.fadeOnDisable = fadeOnDisable;
        this.fadeAlpha = fadeAlpha;
    }    
    
    @Override
    protected void controlUpdate(float tpf) {
        if (ref.update()) {
            update();
        }
    }
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {}
    @Override
    public void setSpatial(Spatial spatial) {
        super.setSpatial(spatial);
        if (tSpatial != null) {
            update();
        }
    }
    
    private void update() {
        tSpatial.setAlpha((ref.get() == fadeOnDisable ? 1 : fadeAlpha));
    }
    
}
