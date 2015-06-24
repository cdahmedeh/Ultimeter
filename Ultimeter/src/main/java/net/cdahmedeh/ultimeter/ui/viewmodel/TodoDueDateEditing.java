package net.cdahmedeh.ultimeter.ui.viewmodel;

import java.time.ZonedDateTime;

import net.cdahmedeh.ultimeter.core.domain.Todo;
import net.cdahmedeh.ultimeter.core.parser.DateParser;
import net.cdahmedeh.ultimeter.ui.controller.TodoController;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

/**
 * For editing the todo due date.
 * 
 * @author Ahmed El-Hajjar
 *
 */
public class TodoDueDateEditing extends EditingSupport {
    private TodoController todoController;

    private TextCellEditor editor;

    public TodoDueDateEditing(TableViewer viewer, TodoController todoController) {
        super(viewer);
        editor = new TextCellEditor(viewer.getTable());
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
        updateTable(element);
    }

    private void updateTable(Object element) {
        getViewer().refresh(element, true);
    }
}
