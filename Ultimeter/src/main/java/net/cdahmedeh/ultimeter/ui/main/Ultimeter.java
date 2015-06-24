package net.cdahmedeh.ultimeter.ui.main;

import javax.swing.JFrame;

import net.cdahmedeh.ultimeter.persistence.dao.TodoManager;
import net.cdahmedeh.ultimeter.persistence.manager.PersistenceManager;
import net.cdahmedeh.ultimeter.ui.controller.TodoController;
import net.cdahmedeh.ultimeter.ui.view.TodoView;

import com.alee.laf.WebLookAndFeel;

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
        
        // Set look and feel
        WebLookAndFeel.install();
        
        // Create the UI window
        final JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setTitle("Ultimeter - Time Management System");
        frame.setSize(800, 600);
        frame.setVisible(true);

        // Create Views
        final TodoView todoView = new TodoView(todoController);
        frame.add(todoView);
        todoView.refreshTable();

        // Display the UI
        frame.setVisible(true);
    }
}
