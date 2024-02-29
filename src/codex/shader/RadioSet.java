/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.shader;

import codex.boost.Listenable;
import com.jme3.scene.Node;
import com.simsilica.lemur.Checkbox;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

/**
 *
 * @author codex
 */
public class RadioSet extends Node implements Listenable<RadioEventListener> {
    
    private final Checkbox[] boxes;
    private int current;
    private final LinkedList<RadioEventListener> listeners = new LinkedList<>();

    public RadioSet(Checkbox... boxes) {
        assert boxes.length > 0;
        this.boxes = boxes;
        current = 0;
        this.boxes[current].setChecked(true);
        for (int i = 1; i < this.boxes.length; i++) {
            if (this.boxes[i].isChecked()) {
                this.boxes[i].setChecked(false);
            }
        }
    }
    
    @Override
    public void updateLogicalState(float tpf) {
        super.updateLogicalState(tpf);
        boolean checked = false;
        for (int i = 0; i < boxes.length; i++) {
            if (i == current) continue;
            var r = boxes[i];
            if (r.isChecked() && !checked) {
                setCurrentBox(i);
                checked = true;
            } else {
                r.setChecked(false);
            }
        }
        if (!getCurrentBox().isChecked()) {
            getCurrentBox().setChecked(true);
        }
    }
    @Override
    public Collection<RadioEventListener> getListeners() {
        return listeners;
    }
    
    public void setCurrentBox(int i) {
        if (i != current) {
            notifyListeners(l -> l.stateChanged(this, current, i));
            boxes[current].setChecked(false);
            current = i;
            boxes[current].setChecked(true);
        }
    }
    
    public Checkbox getCurrentBox() {
        return boxes[current];
    }
    public Checkbox[] getBoxes() {
        return boxes;
    }
    public Checkbox getBox(int i) {
        return boxes[i];
    }    
    public int getCurrentIndex() {
        return current;
    }
    public boolean isCurrentBox(int i) {
        return i == current;
    }
    
}
