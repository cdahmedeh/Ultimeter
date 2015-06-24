package net.cdahmedeh.ultimeter.ui.view;

import static org.eclipse.jface.viewers.ColumnViewerEditor.KEYBOARD_ACTIVATION;
import static org.eclipse.jface.viewers.ColumnViewerEditor.TABBING_HORIZONTAL;
import static org.eclipse.jface.viewers.ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR;
import static org.eclipse.jface.viewers.ColumnViewerEditor.TABBING_VERTICAL;
import static org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION;
import static org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent.PROGRAMMATIC;
import lombok.val;
import net.cdahmedeh.ultimeter.core.domain.Todo;
import net.cdahmedeh.ultimeter.ui.controller.TodoController;
import net.cdahmedeh.ultimeter.ui.util.Icons;
import net.cdahmedeh.ultimeter.ui.viewmodel.TodoDescriptionEditing;
import net.cdahmedeh.ultimeter.ui.viewmodel.TodoDescriptionProvider;
import net.cdahmedeh.ultimeter.ui.viewmodel.TodoDueDateEditing;
import net.cdahmedeh.ultimeter.ui.viewmodel.TodoDueDateProvider;
import net.cdahmedeh.ultimeter.ui.viewmodel.TodoEstimateEditing;
import net.cdahmedeh.ultimeter.ui.viewmodel.TodoEstimateProvider;
import net.cdahmedeh.ultimeter.ui.viewmodel.TodoTableProvider;
import net.cdahmedeh.ultimeter.ui.viewmodel.TodoTransfer;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TableViewerEditor;
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
    private TableViewer todoTableViewer;
    private TableViewerColumn descriptionColumn;
    private TableViewerColumn dueDateColumn;
    private TableViewerColumn estimateColumn;

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
    }

    private void createTodoTable() {
    	// Wrap the table in a composite. Needed for column layout support.
    	Composite tableContainer = new Composite(container, SWT.NONE);
    	val columnLayout = new TableColumnLayout();
    	tableContainer.setLayout(columnLayout);
    	
    	// Position the container so that it takes all available space.
    	GridData tableGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
    	tableContainer.setLayoutData(tableGridData);
    	
        // Build the table.
        todoTableViewer = new TableViewer(tableContainer, SWT.FULL_SELECTION | SWT.BORDER);
        todoTableViewer.getTable().setHeaderVisible(true);
        
        // Create the columns for the table
        descriptionColumn = new TableViewerColumn(todoTableViewer, SWT.NONE);
        descriptionColumn.getColumn().setText("Description");
        descriptionColumn.getColumn().setResizable(false);
        
        estimateColumn = new TableViewerColumn(todoTableViewer, SWT.TRAIL);
        estimateColumn.getColumn().setText("Estimate");
        estimateColumn.getColumn().setResizable(false);
        
        dueDateColumn = new TableViewerColumn(todoTableViewer, SWT.TRAIL);
        dueDateColumn.getColumn().setText("Due Date");
        dueDateColumn.getColumn().setResizable(false);
        
        // Set column layout
        columnLayout.setColumnData(descriptionColumn.getColumn(), new ColumnWeightData(100));
        columnLayout.setColumnData(estimateColumn.getColumn(), new ColumnWeightData(0, 90));
        columnLayout.setColumnData(dueDateColumn.getColumn(), new ColumnWeightData(0, 90));
        
        // Enable editing table entries with double-clicking the mouse.
        val actSupport = new ColumnViewerEditorActivationStrategy(todoTableViewer) {
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

        TableViewerEditor.create(todoTableViewer, actSupport, feature);
        
        // Enable drag and drop support for todo re-ordering.
        final int dndOperations = DND.DROP_MOVE;
        final Transfer[] transferTypes = new Transfer[] { TodoTransfer.getInstance() };

        DragSourceAdapter sourceAdapter = new DragSourceAdapter() {
            @Override
            public void dragSetData(DragSourceEvent event) {
                event.data = getSelectedTodo();
            }
        };
        
        ViewerDropAdapter dropAdapter = new ViewerDropAdapter(todoTableViewer) {
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
                    
                } else if (getCurrentLocation() == LOCATION_BEFORE) {
                    todoController.moveTodoBefore(todo, target);
                } else if (getCurrentLocation() == LOCATION_AFTER) {
                    todoController.moveTodoAfter(todo, target);
                }
                
                refreshTable();
                
                return true;
            }
        };
        
        todoTableViewer.addDragSupport(dndOperations, transferTypes, sourceAdapter);
        todoTableViewer.addDropSupport(dndOperations, transferTypes, dropAdapter);
        
        // Load models, providers and editors.
        descriptionColumn.setLabelProvider(new TodoDescriptionProvider());
        descriptionColumn.setEditingSupport(new TodoDescriptionEditing(todoTableViewer, todoController));

        estimateColumn.setLabelProvider(new TodoEstimateProvider());
        estimateColumn.setEditingSupport(new TodoEstimateEditing(todoTableViewer, todoController));
        
        dueDateColumn.setLabelProvider(new TodoDueDateProvider());
        dueDateColumn.setEditingSupport(new TodoDueDateEditing(todoTableViewer, todoController));
        
        todoTableViewer.setContentProvider(new TodoTableProvider(todoController));
        todoTableViewer.setInput(new Object());
    }

    /**
     * Refreshes the entire todo list from the database. Restores expanded/closed
     * states to nodes.
     */
    private void refreshTable() {
        todoTableViewer.refresh(true);
    }

    /**
     * Retrieves a reference to the todo that is selected in the todo table.
     * 
     * @return A reference to the selected todo.
     */
    private Todo getSelectedTodo() {
        final IStructuredSelection selection = (IStructuredSelection) todoTableViewer.getSelection();
        return (Todo) selection.getFirstElement();
    }
}
