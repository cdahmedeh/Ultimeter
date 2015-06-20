package net.cdahmedeh.ultimeter.ui.main;

import net.cdahmedeh.ultimeter.persistence.dao.TodoManager;
import net.cdahmedeh.ultimeter.persistence.manager.PersistenceManager;
import net.cdahmedeh.ultimeter.ui.controller.TodoController;
import net.cdahmedeh.ultimeter.ui.util.Icons;
import net.cdahmedeh.ultimeter.ui.view.TodoView;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * This class starts the Ultimeter UI.
 * 
 * @author Ahmed El-Hajjar
 *
 */
public class Ultimeter {
    public static void main(String[] args) throws Exception {
        // Prepare persistence utilities.
        final PersistenceManager persistenceManager = new PersistenceManager();
        final TodoManager todoManager = new TodoManager(persistenceManager);
        
        // Prepare controllers
        final TodoController todoController = new TodoController(todoManager);
        
        // Create the UI display
        final Display display = new Display();
        final Shell shell = new Shell(display);
        shell.setText("Ultimeter - Time Management System");
        shell.setImage(Icons.getIcon(shell, "ultimeter"));
        shell.setLayout(new FillLayout());

        // Create Views
        @SuppressWarnings("unused")
        final TodoView todoView = new TodoView(shell, todoController);

        // Display the UI
        shell.open();

        // Start event loop
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }

        // Clean up.
        display.dispose();
    }
}
