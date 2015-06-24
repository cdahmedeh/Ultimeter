package net.cdahmedeh.ultimeter.ui.viewmodel;

import net.cdahmedeh.ultimeter.ui.controller.TodoController;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Used for displaying todo list in todo table.
 * 
 * @author Ahmed El-Hajjar
 *
 */
public class TodoTableProvider implements IStructuredContentProvider {
    private TodoController todoController;

    public TodoTableProvider(TodoController todoController) {
        this.todoController = todoController;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {

    }

    @Override
    public Object[] getElements(Object inputElement) {
        return todoController.getAllTodos().toArray();
    }

}
