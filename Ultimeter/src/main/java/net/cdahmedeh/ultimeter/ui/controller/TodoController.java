package net.cdahmedeh.ultimeter.ui.controller;

import java.util.List;

import net.cdahmedeh.ultimeter.core.domain.Todo;
import net.cdahmedeh.ultimeter.persistence.dao.TodoManager;

public class TodoController {
    private TodoManager todoManager;

    public TodoController(TodoManager todoManager) {
        this.todoManager = todoManager;
    }
    
    /**
     * Retrieves of all todos in the database. The todos are sorted according
     * to their ordinal. 
     * 
     * @return A list of all todos stored.
     */
    public List<Todo> getAllTodos() {
        return todoManager.getAllTodos();
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
}
