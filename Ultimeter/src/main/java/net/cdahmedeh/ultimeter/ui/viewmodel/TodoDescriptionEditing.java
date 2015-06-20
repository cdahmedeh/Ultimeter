package net.cdahmedeh.ultimeter.ui.viewmodel;

import net.cdahmedeh.ultimeter.domain.Todo;
import net.cdahmedeh.ultimeter.ui.controller.TodoController;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * For editing the todo description.
 * 
 * @author cdahmedeh
 *
 */
public class TodoDescriptionEditing extends EditingSupport {
    private TodoController todoController;

    private TextCellEditor editor;

    public TodoDescriptionEditing(TreeViewer viewer, TodoController todoController) {
        super(viewer);
        editor = new TextCellEditor(viewer.getTree());
        this.todoController = todoController;
    }

    @Override
    protected CellEditor getCellEditor(Object element) {
        return editor;
    }

    @Override
    protected boolean canEdit(Object element) {
        return true;
    }

    @Override
    protected Object getValue(Object element) {
        final Todo todo = (Todo) element;
        return todo.getDescription();
    }

    @Override
    protected void setValue(Object element, Object value) {
        final Todo todo = (Todo) element;
        todo.setDescription(value.toString());
        todoController.save(todo);
        getViewer().update(element, null);
    }

}
