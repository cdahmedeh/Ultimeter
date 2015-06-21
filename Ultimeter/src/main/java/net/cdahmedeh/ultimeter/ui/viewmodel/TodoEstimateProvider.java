package net.cdahmedeh.ultimeter.ui.viewmodel;

import net.cdahmedeh.ultimeter.core.domain.Todo;
import net.cdahmedeh.ultimeter.core.parser.DurationParser;
import net.cdahmedeh.ultimeter.ui.controller.TodoController;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TreeViewer;

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
        return DurationParser.unparse(todo.getEstimate());
    }
}
