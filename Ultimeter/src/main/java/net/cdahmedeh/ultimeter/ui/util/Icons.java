package net.cdahmedeh.ultimeter.ui.util;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Widget;

import com.google.common.io.Resources;

public class Icons {
    
    /**
     * Retrieves a SWT Image from the icon set found in the resources/icons 
     * folder.
     * 
     * @param widget A reference to a widget to get the Display from. 
     * @param iconName The name of the icon without the file extension or folder.
     * @return An Image reference to the created image.
     */
    public static Image getIcon(Widget widget, String iconName) {
        return new Image(widget.getDisplay(), Resources.getResource("icons/" + iconName + ".png").getFile());
    }
}
