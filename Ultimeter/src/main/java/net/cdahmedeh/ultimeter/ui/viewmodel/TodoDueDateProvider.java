package net.cdahmedeh.ultimeter.ui.viewmodel;

import java.time.ZonedDateTime;

import net.cdahmedeh.ultimeter.core.domain.Todo;
import net.cdahmedeh.ultimeter.core.parser.DateParser;
import net.cdahmedeh.ultimeter.ui.controller.TodoController;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

/**
 * To display the todo due date.
 * 
 * @author Ahmed El-Hajjar
 *
 */
public class TodoDueDateProvider extends ColumnLabelProvider {
	private TodoController todoController;
    private TreeViewer viewer;

    public TodoDueDateProvider(TreeViewer viewer, TodoController todoController) {
        this.viewer = viewer;
        this.todoController = todoController;
    }
	
    @Override
    public String getText(Object element) {
        final Todo todo = (Todo) element;
        
        ZonedDateTime dueDate = todo.getDueDate();
        
        // Display ancestral due date if one exists.
        if (dueDate == null) {
        	dueDate = todoController.getAncestorDueDate(todo);
        }
        
        return DateParser.unparse(dueDate);
    }
    
    @Override
    public Color getForeground(Object element) {
        final Todo todo = (Todo) element;
        
        // This will ensure that any blank or ancestral based due dates are
        // lighter in color.
        ZonedDateTime dueDate = todo.getDueDate();
        
        
        if (dueDate == null) {
            Color color = viewer.getTree().getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
            return color;
        }
        
        return super.getForeground(element);
    }
}
