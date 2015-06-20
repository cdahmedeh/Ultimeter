package net.cdahmedeh.ultimeter.ui.viewmodel;

import net.cdahmedeh.ultimeter.core.domain.Todo;
import net.cdahmedeh.ultimeter.core.parser.DateParser;

import org.eclipse.jface.viewers.ColumnLabelProvider;

/**
 * To display the todo due date.
 * 
 * @author Ahmed El-Hajjar
 *
 */
public class TodoDueDateProvider extends ColumnLabelProvider {
    @Override
    public String getText(Object element) {
        final Todo todo = (Todo) element;
        return DateParser.unparse(todo.getDueDate());
    }
}
