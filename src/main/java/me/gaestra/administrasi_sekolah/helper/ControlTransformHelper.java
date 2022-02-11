/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.administrasi_sekolah.helper;

import javafx.animation.FillTransition;
import javafx.scene.control.Control;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

/**
 *
 * @author Ganesha
 */
public class ControlTransformHelper {
    
    public static void clipChildren(Region region) {
        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(region.widthProperty());
        clip.heightProperty().bind(region.heightProperty());
        region.setClip(clip);
    }
    
       
    public static void transitionFillColor(Control control) {
        // Instantiating Fill Transition
        FillTransition fill = new FillTransition();
        fill.setAutoReverse(true);
        fill.setCycleCount(50);
        fill.setDuration(Duration.millis(200));  
        fill.setFromValue(extractBackgroundColor(control));
        fill.setToValue(Color.BROWN);
        fill.setShape(control.getShape());
        fill.play();
    }
    
    public static Color extractBackgroundColor(Control control) {
        String[] stylesheet = control.getStyle().split(";");
        Color retCol = null;
        
        for (var style : stylesheet) {
            if (style.contains("-fx-background-color")) {
                String[] rgb = UtilityMiscHelper.slice(style.split(":")[1].replace("#", ""), 2);
                retCol = Color.rgb(Integer.parseInt(rgb[0],16), Integer.parseInt(rgb[1],16), Integer.parseInt(rgb[2],16));
                break;
            }
        }
        
        return retCol;
    }
    
    public static TableColumn createColumn(String title, String property, TableView table) {
        return createColumn(title, property, table, 0.1);
    }
    
    public static TableColumn createColumn(String title, String property, TableView table, double sizeMultiplier) {
        TableColumn column = new TableColumn(title);
        column.setCellValueFactory(new PropertyValueFactory<>(property));
        column.prefWidthProperty().bind(table.widthProperty().multiply(sizeMultiplier));
        column.setResizable(false);
        
        return column;
    }
}
