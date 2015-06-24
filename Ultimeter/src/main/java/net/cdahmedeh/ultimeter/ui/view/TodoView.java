package net.cdahmedeh.ultimeter.ui.view;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;

import net.cdahmedeh.ultimeter.core.domain.Todo;
import net.cdahmedeh.ultimeter.ui.controller.TodoController;
import net.cdahmedeh.ultimeter.ui.viewmodel.TodoTableModel;

public class TodoView extends JComponent {
    private static final long serialVersionUID = 3020014379823678798L;

    private TodoController todoController;

    private JTable todoTable;
    private TodoTableModel todoTableModel;
    
    public TodoView(TodoController todoController) {
        this.todoController = todoController;
        
        setLayout(new BorderLayout());
        
        createToolBar();
        createTodoTable();
    }

    private void createToolBar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false);
        add(toolbar, BorderLayout.NORTH);
        
        JButton addTodoItem = new JButton("Add Todo", null);
        toolbar.add(addTodoItem);
        addTodoItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                todoController.addNewTodo();
                refreshTable();
            }
        });
        
        JButton deleteTodoButton = new JButton("Delete Todo", null);
        toolbar.add(deleteTodoButton);
        deleteTodoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Todo selectedTodo = getSelectedTodo();
                if (selectedTodo != null) {
                    todoController.delete(selectedTodo);
                    refreshTable();
                }
            }
        });
    }
    
    private void createTodoTable() {
        todoTable = new JTable();
        
        JScrollPane todoPane = new JScrollPane(todoTable);
        add(todoPane, BorderLayout.CENTER);
        
        todoTableModel = new TodoTableModel(todoController);
        todoTable.setModel(todoTableModel);
    }

    public void refreshTable() {
        todoTableModel.fireTableDataChanged();
    }
    
    private Todo getSelectedTodo() {
        int row = todoTable.getSelectedRow();
        return todoTableModel.getTodoAt(row);
    }
}
