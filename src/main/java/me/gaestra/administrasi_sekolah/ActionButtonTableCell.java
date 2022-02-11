/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package me.gaestra.administrasi_sekolah;

/**
 *
 * @author Ganesha
 */
import com.jfoenix.controls.JFXButton;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.util.function.Function;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class ActionButtonTableCell<S> extends TableCell<S, JFXButton> {

    private final JFXButton actionButton;
    private final FontAwesomeIconView iconView;

    public ActionButtonTableCell(String label, String style, FontAwesomeIcon icon, Function< S, S> function) {
        this.actionButton = new JFXButton(label);
        this.iconView = new FontAwesomeIconView(icon, "10px");
        for (var str : style.split(",")) {
            this.actionButton.getStyleClass().add(str.trim());
            this.iconView.getStyleClass().add(str.trim());
        }
        
        this.actionButton.setOnAction((ActionEvent e) -> {
            function.apply(getCurrentItem());
        });
        this.actionButton.setMaxWidth(Double.MAX_VALUE);
        this.actionButton.setGraphic(this.iconView);
        this.actionButton.setButtonType(JFXButton.ButtonType.RAISED);
    }

    public S getCurrentItem() {
        return (S) getTableView().getItems().get(getIndex());
    }

    public static <S> Callback<TableColumn<S, JFXButton>, TableCell<S, JFXButton>> forTableColumn(String label, String style, FontAwesomeIcon icon, Function< S, S> function) {
        return param -> new ActionButtonTableCell<>(label, style, icon, function);
    }

    @Override
    public void updateItem(JFXButton item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setGraphic(null);
        } else {                
            setGraphic(actionButton);
        }
    }
}
