/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.shader;

import codex.boost.control.TypeControl;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.simsilica.lemur.Panel;

/**
 *
 * @author codex
 */
public class FadeControl extends TypeControl<Panel> {
    
    private float rate;
    
    public FadeControl(float rate) {
        super(Panel.class);
        this.rate = rate;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (tSpatial.getAlpha() > 0) {
            tSpatial.setAlpha(Math.max(0, tSpatial.getAlpha()-rate*tpf));
        }
    }
    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {}
    
}
