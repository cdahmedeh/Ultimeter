package net.cdahmedeh.ultimeter.ui.viewmodel;

import java.time.Duration;
import java.time.ZonedDateTime;

import net.cdahmedeh.ultimeter.core.domain.Todo;
import net.cdahmedeh.ultimeter.core.parser.DurationParser;
import net.cdahmedeh.ultimeter.ui.controller.TodoController;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;

/**
 * To display the todo estimate.
 * 
 * @author Ahmed El-Hajjar
 *
 */
public class TodoEstimateProvider extends ColumnLabelProvider {
	private TodoController todoController;
    private TreeViewer viewer;

    public TodoEstimateProvider(TreeViewer viewer, TodoController todoController) {
        this.viewer = viewer;
        this.todoController = todoController;
    }
	
    @Override
    public String getText(Object element) {
        final Todo todo = (Todo) element;
        
        Duration estimate = todo.getEstimate();
        
        if (estimate == null) {
            estimate = todoController.getDescendantTotalEstimate(todo);
        }
        
        return DurationParser.unparse(estimate);
    }
    
    @Override
    public Color getForeground(Object element) {
        final Todo todo = (Todo) element;
        
        Duration estimate = todo.getEstimate();
        
        if (estimate == null) {
            Color color = viewer.getTree().getDisplay().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
            return color;
        }
        
        return super.getForeground(element);
    }
}
