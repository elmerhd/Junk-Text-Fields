package com.junk.application;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.text.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JTextField;

/**
 * <code>JunkTextField</code> is a textfield that supports placeholder, rounded border and etc
 * 
 * @author Elmer Duron
 * @version 3.1
 */
public class JunkTextField extends JTextField{
    private Placeholder placeholder_comp;
    public static enum Movement{ UP,DOWN,LEFT,RIGHT}
    
    public JunkTextField() {
        super();
    }

    public JunkTextField(String placeholder) {
        placeholder_comp = new Placeholder(placeholder, this);
        placeholder_comp.setForeground( Color.BLACK );
        placeholder_comp.changeAlpha(0.5f);
        placeholder_comp.initFontStyle(Font.PLAIN);
        
    }
    public void setPlaceHolder(String placeholder){
        placeholder_comp = new Placeholder(placeholder, this);
        placeholder_comp.setForeground(Color.BLACK);
        placeholder_comp.changeAlpha(0.5f);
        placeholder_comp.initFontStyle(Font.PLAIN);
    }
    public void setPlaceHolderColor(Color col){
        placeholder_comp.setForeground(col);
    }
    public void setFontStyle(int font){
        placeholder_comp.initFontStyle(font);
    }
    public void roundedBorderRadius(){
        this.setBorder(new RoundedCornerBorder());
    }
    
    
    
    @Override
    protected void paintComponent(Graphics g) {
        if (!isOpaque() && getBorder() instanceof RoundedCornerBorder) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setPaint(getBackground());
            g2.fill(((RoundedCornerBorder) getBorder()).getBorderShape(
                0, 0, getWidth() - 1, getHeight() - 1));
            g2.dispose();
        }
        super.paintComponent(g);
    }
      
    @Override 
    public void updateUI() {
        super.updateUI();
        setOpaque(false);
//        setBorder(new RoundedCornerBorder());
    }
    
    
    /**
    *  The Placeholder class will display a placeholder over top of a text component when
    *  the Document of the text field is empty. The Focus property is used to
    *  determine the visibility of the placeholder.
    *
    *  The Font and foreground Color of the placeholder will default to those properties
    *  of the parent text component.
    *  
    */
    protected static class Placeholder extends JLabel implements FocusListener, DocumentListener {
        public enum ShowPlaceholder {
            ALWAYS,
            FOCUS_GAINED,
            FOCUS_LOST;
        }

        private JTextComponent component;
        private Document document;

        private ShowPlaceholder showPlaceholder;
        private boolean showPlaceholderOnce;
        private int focusLost;

        public Placeholder(String text, JTextComponent component){
            this(text, component, ShowPlaceholder.ALWAYS);
        }
        
        public Placeholder(String text, JTextComponent component, ShowPlaceholder show){
            this.component = component;
            setShowPlaceholder(show);
            document = component.getDocument();

            setText( text );
            setFont( component.getFont() );
            setForeground( component.getForeground() );
            setBorder( new EmptyBorder(component.getInsets()) );
            setHorizontalAlignment(JLabel.LEADING);

            component.addFocusListener( this );
            document.addDocumentListener( this );

            component.setLayout( new BorderLayout() );
            component.add(this);
            checkForPlaceholderToShow();
        }
        /**
         *  Convenience method to change the alpha value of the current foreground
         *  Color to the specific value.
         *
         *  @param alpha value in the range of 0 - 1.0.
         */
        public void changeAlpha(float alpha){
            changeAlpha( (int)(alpha * 255) );
        }

        /**
         *  Convenience method to change the alpha value of the current foreground
         *  Color to the specific value.
         *
         *  @param alpha value in the range of 0 - 255.
         */
        public void changeAlpha(int alpha){
            alpha = alpha > 255 ? 255 : alpha < 0 ? 0 : alpha;

            Color foreground = getForeground();
            int red = foreground.getRed();
            int green = foreground.getGreen();
            int blue = foreground.getBlue();

            Color withAlpha = new Color(red, green, blue, alpha);
            super.setForeground( withAlpha );
        }

        /**
         *  Convenience method to change the style of the current Font. The style
         *  values are found in the Font class. Common values might be:
         *  Font.BOLD, Font.ITALIC and Font.BOLD + Font.ITALIC.
         *
         *  @param style value representing the the new style of the Font.
         */
        public void initFontStyle(int style){
            setFont( getFont().deriveFont( style ) );
        }

        /**
         *  Get the ShowPlaceholder property
         *
         *  @return the ShowPlaceholder property.
         */
        public ShowPlaceholder getFocus(){
            return showPlaceholder;
        }

        /**
         *  Set the placeholder Show property to control when the placeholder is shown.
         *  Valid values are:
         *
         *  Focus.ALWAYS (default) - always show the prompt
         *  Focus.Focus_GAINED - show the prompt when the component gains focus
         *      (and hide the prompt when focus is lost)
         *  Focus.Focus_LOST - show the prompt when the component loses focus
         *      (and hide the prompt when focus is gained)
         *
         *  @param focus a valid Focus enum
         */
        public void setShowPlaceholder(ShowPlaceholder focus){
            this.showPlaceholder = focus;
        }

        /**
         *  Get the showPlaceholderOnce property
         *
         *  @return the showPromptOnce property.
         */
        public boolean getShowPlaceholderOnce(){
            return showPlaceholderOnce;
        }

        /**
         *  Show the placeholder once. Once the component has gained/lost focus
         *  once, the prompt will not be shown again.
         *
         *  @param showPromptOnce  when true the prompt will only be shown once,
         *                         otherwise it will be shown repeatedly.
         */
        public void setShowPlaceholderOnce(boolean showPromptOnce){
            this.showPlaceholderOnce = showPromptOnce;
        }

        /**
         * Check whether the placeholder should be visible or not. The visibility
         *  will change on updates to the Document and on focus changes.
         */
        private void checkForPlaceholderToShow(){
            //  Text has been entered, remove the prompt

            if (document.getLength() > 0){
                setVisible( false );
                return;
            }

            //  Prompt has already been shown once, remove it

            if (showPlaceholderOnce && focusLost > 0){
                setVisible(false);
                return;
            }

            //  Check the Show property and component focus to determine if the
            //  prompt should be displayed.

            if (component.hasFocus()){
                if (showPlaceholder == ShowPlaceholder.ALWAYS ||  showPlaceholder == ShowPlaceholder.FOCUS_GAINED){
                    setVisible( true );
                }else{
                    setVisible( false );
                }
            }else{
                if (showPlaceholder == ShowPlaceholder.ALWAYS ||  showPlaceholder == ShowPlaceholder.FOCUS_LOST){
                    setVisible( true );
                }else{
                    setVisible( false );
                }
            }
        }

   

        @Override
        public void focusGained(FocusEvent e){
            checkForPlaceholderToShow();
        }

       @Override
        public void focusLost(FocusEvent e){
            focusLost++;
            checkForPlaceholderToShow();
        }


        @Override
        public void insertUpdate(DocumentEvent e){
            checkForPlaceholderToShow();
        }

        @Override
        public void removeUpdate(DocumentEvent e){
            checkForPlaceholderToShow();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {}
    }
    
    protected static class RoundedCornerBorder extends AbstractBorder {
        private static final Color ALPHA_ZERO = new Color(0x0, true);
        @Override public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Shape border = getBorderShape(x, y, width - 1, height - 1);
            g2.setPaint(ALPHA_ZERO);
            Area corner = new Area(new Rectangle2D.Double(x, y, width, height));
            corner.subtract(new Area(border));
            g2.fill(corner);
            g2.setPaint(Color.GRAY);
            g2.draw(border);
            g2.dispose();
        }
        public Shape getBorderShape(int x, int y, int w, int h) {
            int r = h; //h / 2;
            return new RoundRectangle2D.Double(x, y, w, h, r, r);
        }
        @Override public Insets getBorderInsets(Component c) {
            return new Insets(4, 8, 4, 8);
        }
        @Override public Insets getBorderInsets(Component c, Insets insets) {
          insets.set(4, 8, 4, 8);
          return insets;
        }
    }
    
}
