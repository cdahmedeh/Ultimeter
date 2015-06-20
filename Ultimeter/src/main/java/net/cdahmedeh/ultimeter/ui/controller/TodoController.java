package net.cdahmedeh.ultimeter.ui.controller;

import java.time.ZonedDateTime;
import java.util.List;

import lombok.SneakyThrows;
import net.cdahmedeh.ultimeter.core.domain.Todo;
import net.cdahmedeh.ultimeter.persistence.dao.TodoManager;

public class TodoController {
    private TodoManager todoManager;

    public TodoController(TodoManager todoManager) {
        this.todoManager = todoManager;
    }
    
    /**
     * Get the root todo.
     * 
     * @return A reference to the root todo.
     */
    public Todo getRootTodo() {
        return todoManager.getRootTodo();
    }

    /**
     * Get immediate children of the provided todo in their ordinal order.
     * 
     * @param todo The todo to get the children for.
     * @return A list of the immediate children of the provided todo.
     */
    public List<Todo> getChildren(Todo todo) {
        return todoManager.getChildren(todo);
    }

    /**
     * Create a new blank todo.
     * 
     * @return The newly created todo.
     */
    public Todo addNewTodo() {
        return todoManager.addNewTodo();
    }
    
    /**
     * Saves changes for provided todo.
     * 
     * @param todo A reference to an already existing todo.
     */
    public void save(Todo todo) {
        todoManager.update(todo);
    }
    
    /**
     * Delete todo and sub-todos.
     * 
     * @param todo The todo to delete along with sub-todos.
     */
    public void delete(Todo todo) {
        todoManager.delete(todo);
    }
    
    /**
     * Re-parents the provided todo to the target.
     * 
     * @param todo The todo to re-parent.
     * @param parent The new parent for the provided todo.
     */
    public void setTodoParent(Todo todo, Todo parent) {
        todoManager.setTodoParent(todo, parent);
    }

    /**
     * Moves the todo before the target.
     * 
     * @param todo The todo to move.
     * @param target Where to put the todo.
     */
    public void moveTodoBefore(Todo todo, Todo target) {
        todoManager.moveTodoBefore(todo, target);
    }
    
    /**
     * Moves the todo after the target.
     * 
     * @param todo The todo to move.
     * @param target Where to put the todo.
     */
    public void moveTodoAfter(Todo todo, Todo target) {
        todoManager.moveTodoAfter(todo, target);
    }
    
    /**
     * Retrieves the due date of the nearest ancestor with one. Null if none of 
     * them do.
     * 
     * @param todo The todo to find the ancestral due date for.
     * @return The nearest due date. Null if none found.
     */
    public ZonedDateTime getAncestorDueDate(Todo todo) {
    	return todoManager.getAncestorDueDate(todo);
    }
}
