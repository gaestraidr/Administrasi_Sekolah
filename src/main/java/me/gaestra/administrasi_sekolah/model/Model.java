/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.administrasi_sekolah.model;

import java.util.LinkedHashMap;

/**
 *
 * @author Ganesha
 */
public class Model {
    
    protected boolean fromDatabase = false;
    
    public boolean isEdited = false;
    public int id = 0;
    
    public Model() {}
    
    public void get() {}
    public void store() {}
    public void update() {}
    public void delete() {}
    
    // Comparator Hook
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Model) {
            return ((Model)obj).id == this.id; 
        }
        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
        
}
