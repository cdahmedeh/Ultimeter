package net.cdahmedeh.ultimeter.ui.viewmodel;

import java.time.Duration;

import net.cdahmedeh.ultimeter.core.domain.Todo;
import net.cdahmedeh.ultimeter.core.parser.DurationParser;

import org.eclipse.jface.viewers.ColumnLabelProvider;

/**
 * To display the todo estimate.
 * 
 * @author Ahmed El-Hajjar
 *
 */
public class TodoEstimateProvider extends ColumnLabelProvider {
    public TodoEstimateProvider() {
        
    }
	
    @Override
    public String getText(Object element) {
        final Todo todo = (Todo) element;
        Duration estimate = todo.getEstimate();
        return DurationParser.unparse(estimate);
    }
    
}
