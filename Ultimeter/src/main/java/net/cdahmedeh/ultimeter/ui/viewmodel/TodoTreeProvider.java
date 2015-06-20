package net.cdahmedeh.ultimeter.ui.viewmodel;

import net.cdahmedeh.ultimeter.core.domain.Todo;
import net.cdahmedeh.ultimeter.ui.controller.TodoController;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * Used for displaying todo hierarchy in todo table.
 * 
 * @author Ahmed El-Hajjar
 *
 */
public class TodoTreeProvider implements ITreeContentProvider {
    private TodoController todoController;

    public TodoTreeProvider(TodoController todoController) {
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
        final Todo todo = (Todo) inputElement;
        return todoController.getChildren(todo).toArray();
    }

    @Override
    public Object[] getChildren(Object parentElement) {
        final Todo todo = (Todo) parentElement;
        return todoController.getChildren(todo).toArray();
    }

    @Override
    public Object getParent(Object element) {
        final Todo todo = (Todo) element;
        return todo.getParent();
    }

    @Override
    public boolean hasChildren(Object element) {
        final Todo todo = (Todo) element;
        return false == todoController.getChildren(todo).isEmpty();
    }

}
