package net.cdahmedeh.ultimeter.ui.viewmodel;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import net.cdahmedeh.ultimeter.core.domain.Todo;
import net.cdahmedeh.ultimeter.core.parser.DateParser;
import net.cdahmedeh.ultimeter.core.parser.DurationParser;
import net.cdahmedeh.ultimeter.ui.controller.TodoController;

public class TodoTableModel extends AbstractTableModel {
    private static final long serialVersionUID = 4212425147873375913L;
    
    private final static String[] COLUMN_NAMES = new String[] {
        "Description", "Estimate", "Due Date"
    };
    
    private final static int DESCRIPTION = 0, ESTIMATE = 1, DUE_DATE = 2;
            
    private TodoController todoController;
    private List<Todo> todos = new ArrayList<>();
    

    public TodoTableModel(TodoController todoController) {
        this.todoController = todoController;
    }
    
    @Override
    public int getRowCount() {
        return todos.size();
    }

    public Todo getTodoAt(int rowIndex) {
        return todos.get(rowIndex);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Todo todo = getTodoAt(rowIndex);
        
        switch(columnIndex) {
        case DESCRIPTION:
            return todo.getDescription();
        case ESTIMATE:
            return DurationParser.unparse(todo.getEstimate());
        case DUE_DATE:
            return DateParser.unparse(todo.getDueDate());
        }
        
        return "";
    }
    
    @Override
    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        Todo todo = getTodoAt(rowIndex);
        String value = aValue.toString();
        
        switch(columnIndex) {
        case DESCRIPTION:
            todo.setDescription(value);
        case ESTIMATE:
            todo.setEstimate(DurationParser.parse(value));
        case DUE_DATE:
            todo.setDueDate(DateParser.parse(value));
        }
        
        todoController.save(todo);
    }
    
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }
    
    @Override
    public String getColumnName(int column) {
        return COLUMN_NAMES[column];
    }
    
    @Override
    public int getColumnCount() {
        return COLUMN_NAMES.length;
    }
    
    @Override
    public void fireTableDataChanged() {
        todos = todoController.getAllTodos();
        super.fireTableDataChanged();
    }

}
