package net.cdahmedeh.ultimeter.ui.viewmodel;

import java.time.Duration;

import net.cdahmedeh.ultimeter.core.domain.Todo;
import net.cdahmedeh.ultimeter.core.parser.DurationParser;
import net.cdahmedeh.ultimeter.ui.controller.TodoController;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

/**
 * For editing the todo estimate.
 * 
 * @author Ahmed El-Hajjar
 *
 */
public class TodoEstimateEditing extends EditingSupport {
    private TodoController todoController;

    private TextCellEditor editor;

    public TodoEstimateEditing(TableViewer viewer, TodoController todoController) {
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
        Duration duration = todo.getEstimate();
        return duration == null ? "" : duration.toString();
    }

    @Override
    protected void setValue(Object element, Object value) {
        final Todo todo = (Todo) element;
        todo.setEstimate(DurationParser.parse(value.toString()));
        todoController.save(todo);
        updateTable(element);
    }
    
    private void updateTable(Object element) {
        getViewer().refresh(true);
    }
}
