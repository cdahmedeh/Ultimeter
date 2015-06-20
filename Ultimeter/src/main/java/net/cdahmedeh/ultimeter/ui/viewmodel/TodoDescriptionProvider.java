package net.cdahmedeh.ultimeter.ui.viewmodel;

import net.cdahmedeh.ultimeter.domain.Todo;

import org.eclipse.jface.viewers.ColumnLabelProvider;

/**
 * To display the todo name.
 * 
 * @author Ahmed El-Hajjar
 *
 */
public class TodoDescriptionProvider extends ColumnLabelProvider {
    @Override
    public String getText(Object element) {
        final Todo todo = (Todo) element;
        return todo.getDescription();
    }
}
