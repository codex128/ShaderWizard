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
import com.simsilica.lemur.core.VersionedReference;

/**
 *
 * @author codex
 */
public class CheckboxCheckLink extends TypeControl<Checkbox> {
    
    private final VersionedReference<Boolean> ref;
    
    public CheckboxCheckLink(Checkbox box) {
        super(Checkbox.class);
        this.ref = box.getModel().createReference();
    }
    
    @Override
    protected void controlUpdate(float tpf) {
        if (ref.get() && !tSpatial.isChecked()) {
            tSpatial.setChecked(true);
        }
    }
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {}
}
