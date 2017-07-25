package com.junk.application;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JPasswordField;

/**
 *
 * @author Elmer Duron
 * @version 3
 */
public class JunkPasswordField extends JPasswordField{
    
    private JunkTextField.Placeholder placeholder_comp;
    
    public JunkPasswordField() {
        super();
    }
    
    public JunkPasswordField(String placeholder) {
        placeholder_comp = new JunkTextField.Placeholder(placeholder, this);
        placeholder_comp.setForeground( Color.BLACK );
        placeholder_comp.changeAlpha(0.5f);
        placeholder_comp.initFontStyle(Font.PLAIN);
    }
    /**
     * Sets the placeholder of a password field
     * @param placeholder 
     */
    public void setPlaceHolder(String placeholder){
        placeholder_comp = new JunkTextField.Placeholder(placeholder, this);
        placeholder_comp.setForeground(Color.BLACK);
        placeholder_comp.changeAlpha(0.5f);
        placeholder_comp.initFontStyle(Font.PLAIN);
    }
    /**
     * Sets the placeholder color
     * @param col the color of the placeholder
     */
    public void setPlaceHolderColor(Color col){
        placeholder_comp.setForeground(col);
    }
    /**
     * Sets the placeholder font style e.g bold
     * @param font the font of the placeholder
     */
    public void initFontStyle(int font){
         placeholder_comp.initFontStyle(font);
    }
    public void roundedBorderRadius(){
        this.setBorder(new JunkTextField.RoundedCornerBorder());
    }
}
