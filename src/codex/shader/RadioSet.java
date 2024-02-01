/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package codex.shader;

import com.jme3.scene.Node;
import com.simsilica.lemur.Checkbox;

/**
 *
 * @author codex
 */
public class RadioSet extends Node {
    
    private final Checkbox[] boxes;
    private Checkbox current;

    public RadioSet(Checkbox... boxes) {
        assert boxes.length > 0;
        this.boxes = boxes;
        for (int i = 0; i < this.boxes.length; i++) {
            if (this.boxes[i].isChecked()) {
                if (current != null) {
                    this.boxes[i].setChecked(false);
                } else {
                    current = this.boxes[i];
                }
            }
        }
        if (current == null) {
            current = this.boxes[0];
        }
    }
    
    @Override
    public void updateLogicalState(float tpf) {
        super.updateLogicalState(tpf);
        boolean checked = false;
        for (Checkbox r : boxes) {
            if (r != current && r.isChecked() && !checked) {
                setCurrentBox(r);
                checked = true;
            } else {
                r.setChecked(false);
            }
        }
        if (!current.isChecked()) {
            current.setChecked(true);
        }
    }
    
    private void setCurrentBox(Checkbox box) {
        current.setChecked(false);
        current = box;
        current.setChecked(true);
    }
    public void setCurrentBox(int i) {
        setCurrentBox(boxes[i]);
    }
    
    public Checkbox getCurrentBox() {
        return current;
    }
    public Checkbox[] getBoxes() {
        return boxes;
    }
    public Checkbox getBox(int i) {
        return boxes[i];
    }
    
    public int getCurrentIndex() {
        for (int i = 0; i < boxes.length; i++) {
            if (boxes[i] == current) {
                return i;
            }
        }
        return -1;
    }
    public boolean isCurrentBox(int i) {
        return boxes[i] == current;
    }
    
}
