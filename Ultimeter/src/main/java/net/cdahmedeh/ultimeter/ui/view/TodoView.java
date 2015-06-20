package net.cdahmedeh.ultimeter.ui.view;

import static org.eclipse.jface.viewers.ColumnViewerEditor.KEYBOARD_ACTIVATION;
import static org.eclipse.jface.viewers.ColumnViewerEditor.TABBING_HORIZONTAL;
import static org.eclipse.jface.viewers.ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR;
import static org.eclipse.jface.viewers.ColumnViewerEditor.TABBING_VERTICAL;
import static org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION;
import static org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent.PROGRAMMATIC;
import lombok.val;
import net.cdahmedeh.ultimeter.domain.Todo;
import net.cdahmedeh.ultimeter.ui.controller.TodoController;
import net.cdahmedeh.ultimeter.ui.util.Icons;
import net.cdahmedeh.ultimeter.ui.viewmodel.TodoDescriptionEditing;
import net.cdahmedeh.ultimeter.ui.viewmodel.TodoDescriptionProvider;
import net.cdahmedeh.ultimeter.ui.viewmodel.TodoTransfer;
import net.cdahmedeh.ultimeter.ui.viewmodel.TodoTreeProvider;

import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ITreeSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.TreeViewerColumn;
import org.eclipse.jface.viewers.TreeViewerEditor;
import org.eclipse.jface.viewers.ViewerDropAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;

/**
 * Contains the todo table and toolbar.
 * 
 * @author Ahmed El-Hajjar
 *
 */
public class TodoView {
    private TodoController todoController;
    
    private Composite container;
    private TreeViewer todoTreeViewer;
    private TreeViewerColumn descriptionColumn;

    public TodoView(Composite parent, TodoController todoController) {
        this.todoController = todoController;
        
        container = new Composite(parent, SWT.NONE);
        GridLayout gridLayout = new GridLayout(1,false);
        container.setLayout(gridLayout);
        
        createToolBar();
        createTodoTable();
    }

    private void createToolBar() {
        ToolBar toolbar = new ToolBar(container, SWT.HORIZONTAL | SWT.RIGHT | SWT.FLAT);
       
        GridData barGridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
        toolbar.setLayoutData(barGridData);
        
        final ToolItem addTodoItem = new ToolItem(toolbar, SWT.PUSH);
        addTodoItem.setText("Add Todo");
        addTodoItem.setImage(Icons.getIcon(addTodoItem, "add-todo"));
        addTodoItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                todoController.addNewTodo();
                refreshTable();
            }
        });
        
        final ToolItem deleteTodoItem = new ToolItem(toolbar, SWT.PUSH);
        deleteTodoItem.setText("Delete Todo");
        deleteTodoItem.setImage(Icons.getIcon(deleteTodoItem, "delete-todo"));
        deleteTodoItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                Todo selectedTodo = getSelectedTodo();
                if (selectedTodo != null) {
                    todoController.delete(selectedTodo);
                    refreshTable();
                }
            }
        });
        
        new ToolItem(toolbar, SWT.SEPARATOR);
        
        final ToolItem expandAllTodosItem = new ToolItem(toolbar, SWT.PUSH);
        expandAllTodosItem.setText("Expand All Todos");
        expandAllTodosItem.setImage(Icons.getIcon(expandAllTodosItem, "expand-all-todos"));
        expandAllTodosItem.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                todoTreeViewer.expandAll();
            }
        });
    }

    private void createTodoTable() {
        // Build the table.
        todoTreeViewer = new TreeViewer(container, SWT.FULL_SELECTION | SWT.BORDER);
        todoTreeViewer.getTree().setHeaderVisible(true);
        
        // Position the table so that it takes all available space.
        GridData treeGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
        todoTreeViewer.getTree().setLayoutData(treeGridData);

        // Create the columns for the table
        descriptionColumn = new TreeViewerColumn(todoTreeViewer, SWT.NONE);
        descriptionColumn.getColumn().setText("Description");
        descriptionColumn.getColumn().setWidth(200);
        
        // Enable editing table entries with double-clicking the mouse.
        val actSupport = new ColumnViewerEditorActivationStrategy(todoTreeViewer) {
            @Override
            protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
                return event.eventType == MOUSE_DOUBLE_CLICK_SELECTION || 
                        event.eventType == PROGRAMMATIC;
            }
        };

        // Enable continuous editing via tab-key.
        final int feature = TABBING_HORIZONTAL
                | TABBING_MOVE_TO_ROW_NEIGHBOR
                | TABBING_VERTICAL
                | KEYBOARD_ACTIVATION;

        TreeViewerEditor.create(todoTreeViewer, actSupport, feature);
        
        // Enable drag and drop support for todo re-ordering.
        final int dndOperations = DND.DROP_MOVE;
        final Transfer[] transferTypes = new Transfer[] { TodoTransfer.getInstance() };

        DragSourceAdapter sourceAdapter = new DragSourceAdapter() {
            @Override
            public void dragSetData(DragSourceEvent event) {
                event.data = getSelectedTodo();
            }
        };
        
        ViewerDropAdapter dropAdapter = new ViewerDropAdapter(todoTreeViewer) {
            @Override
            public boolean validateDrop(Object target, int operation, TransferData transferType) {
                return true;
            }
            
            @Override
            public boolean performDrop(Object data) {
                final Todo todo = (Todo) data;
                final Todo target = (Todo) getCurrentTarget();
                
                if (target == null || todo.equals(target.getId())) {
                    return false;
                }
                
                if (getCurrentLocation() == LOCATION_ON) {
                    todoController.setTodoParent(todo, target);
                } else if (getCurrentLocation() == LOCATION_BEFORE) {
                    todoController.moveTodoBefore(todo, target);
                } else if (getCurrentLocation() == LOCATION_AFTER) {
                    todoController.moveTodoAfter(todo, target);
                }
                
                refreshTable();
                
                return true;
            }
        };
        
        todoTreeViewer.addDragSupport(dndOperations, transferTypes, sourceAdapter);
        todoTreeViewer.addDropSupport(dndOperations, transferTypes, dropAdapter);
        
        // Load models, providers and editors.
        descriptionColumn.setLabelProvider(new TodoDescriptionProvider());
        descriptionColumn.setEditingSupport(new TodoDescriptionEditing(todoTreeViewer, todoController));

        todoTreeViewer.setContentProvider(new TodoTreeProvider(todoController));
        todoTreeViewer.setInput(todoController.getRootTodo());
    }

    /**
     * Refreshes the entire todo tree from the database. Restores expanded/closed
     * states to nodes.
     */
    private void refreshTable() {
        TreePath[] expandedTreePaths = todoTreeViewer.getExpandedTreePaths();
        todoTreeViewer.refresh(true);
        todoTreeViewer.setExpandedTreePaths(expandedTreePaths);
    }

    /**
     * Retrieves a reference to the todo that is selected in the todo table.
     * 
     * @return A reference to the selected todo.
     */
    private Todo getSelectedTodo() {
        final ITreeSelection selection = (ITreeSelection) todoTreeViewer.getSelection();
        return (Todo) selection.getFirstElement();
    }
}
