/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.administrasi_sekolah;

import javafx.scene.layout.Pane;
import me.gaestra.administrasi_sekolah.helper.ControlTransformHelper;

/**
 *
 * @author Ganesha
 */
public class ClippedPane extends Pane {
    
    public ClippedPane() {
        super();
        
        ControlTransformHelper.clipChildren(this);
    }
}
