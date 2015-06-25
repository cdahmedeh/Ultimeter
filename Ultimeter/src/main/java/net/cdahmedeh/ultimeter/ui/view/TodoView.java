package net.cdahmedeh.ultimeter.ui.view;

import java.awt.BorderLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.DropMode;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.TransferHandler;

import net.cdahmedeh.ultimeter.core.domain.Todo;
import net.cdahmedeh.ultimeter.ui.controller.TodoController;
import net.cdahmedeh.ultimeter.ui.util.Icons;
import net.cdahmedeh.ultimeter.ui.viewmodel.TodoTableModel;
import net.cdahmedeh.ultimeter.ui.viewmodel.TodoTransferable;

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
        
        JButton addTodoItem = new JButton("Add Todo", Icons.getIcon("add-todo"));
        toolbar.add(addTodoItem);
        addTodoItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                todoController.addNewTodo();
                refreshTable();
            }
        });
        
        JButton deleteTodoButton = new JButton("Delete Todo", Icons.getIcon("delete-todo"));
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
        
        todoTable.setDragEnabled(true);
        todoTable.setDropMode(DropMode.INSERT_ROWS);

        todoTable.setTransferHandler(new TransferHandler("todo"){
            private static final long serialVersionUID = -8859181787553330853L;
            
            @Override
            public int getSourceActions(JComponent c) {
                return MOVE;
            }
            
            @Override
            public boolean canImport(TransferSupport support) {
                return true;
            }
            
            @Override
            protected Transferable createTransferable(JComponent c) {
                return new TodoTransferable(getSelectedTodo());
            }
            
            @Override
            public boolean importData(TransferSupport support) {
                JTable.DropLocation drop = ((JTable.DropLocation)support.getDropLocation());

                Todo todo = null;
                try {
                    todo = (Todo)support.getTransferable().getTransferData(DataFlavor.stringFlavor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                Todo target = null;
                try {
                    target = todoTableModel.getTodoAt(drop.getRow());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                
                if (target != null) {
                    todoController.moveTodoBefore(todo, target);    
                } else {
                    todoController.moveTodoAfter(todo, todoTableModel.getTodoAt(todoTableModel.getRowCount() - 1));
                }
                
                
                refreshTable();
                
                return true;
            }
        });
    }

    public void refreshTable() {
        todoTableModel.fireTableDataChanged();
    }
    
    private Todo getSelectedTodo() {
        int row = todoTable.getSelectedRow();
        return todoTableModel.getTodoAt(row);
    }
}
