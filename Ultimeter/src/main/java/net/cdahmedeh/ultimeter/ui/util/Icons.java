package net.cdahmedeh.ultimeter.ui.util;

import java.io.IOException;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Widget;

import com.google.common.io.Resources;

public class Icons {
    
    /**
     * Retrieves a SWT Image from the icon set found in the resources/icons 
     * folder.
     * 
     * It will return a blank 16x16 image if the file does not exist or cannot
     * be loaded.
     * 
     * @param widget A reference to a widget to get the Display from. 
     * @param iconName The name of the icon without the file extension or folder.
     * @return An Image reference to the created image.
     */
    public static Image getIcon(Widget widget, String iconName) {
        try {
            return new Image(widget.getDisplay(), Resources.getResource("icons/" + iconName + ".png").openStream());
        } catch (IOException e) {
            return new Image(widget.getDisplay(), 16, 16);
        }
    }
}
