package net.cdahmedeh.ultimeter.ui.viewmodel;

import java.time.ZonedDateTime;

import net.cdahmedeh.ultimeter.core.domain.Todo;
import net.cdahmedeh.ultimeter.core.parser.DateParser;
import net.cdahmedeh.ultimeter.ui.controller.TodoController;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.TreeViewer;

/**
 * For editing the todo due date.
 * 
 * @author Ahmed El-Hajjar
 *
 */
public class TodoDueDateEditing extends EditingSupport {
    private TodoController todoController;

    private TextCellEditor editor;

    public TodoDueDateEditing(TreeViewer viewer, TodoController todoController) {
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
        ZonedDateTime dueDate = todo.getDueDate();
        return dueDate == null ? "" : dueDate.toString();
    }

    @Override
    protected void setValue(Object element, Object value) {
        final Todo todo = (Todo) element;
        todo.setDueDate(DateParser.parse(value.toString()));
        todoController.save(todo);
        getViewer().update(element, null);
    }

}
