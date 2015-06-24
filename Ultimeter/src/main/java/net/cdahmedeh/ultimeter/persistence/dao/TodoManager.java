package net.cdahmedeh.ultimeter.persistence.dao;

import java.util.List;

import lombok.SneakyThrows;
import lombok.val;
import net.cdahmedeh.ultimeter.core.domain.Todo;
import net.cdahmedeh.ultimeter.persistence.manager.PersistenceManager;

import com.j256.ormlite.dao.Dao;

/**
 * Used for reading and persisting data related to Todo objects.
 * 
 * @author Ahmed El-Hajjar
 *
 */
public class TodoManager {
    private Dao<Todo, Long> todoDao;

    public TodoManager(PersistenceManager persistenceManager) {
        this.todoDao = persistenceManager.getTodoDao();
    }

    /**
     * Retrieves of all todos in the database. The todos are sorted according
     * to their ordinal. 
     * 
     * @return A list of all todos stored.
     */
    @SneakyThrows
    public List<Todo> getAllTodos() {
        return todoDao.queryBuilder().
                orderBy("ordinal", true).
                query();
    }

    /**
     * Creates a new todo and save it into database.
     */
    @SneakyThrows
    public Todo addNewTodo() {
        final Todo todo = createTodo();
        insertTodoLast(todo);
        todoDao.refresh(todo);
        return todo;
    }
    
    /**
     * Updates todo data in the database with parameters stored in the provided
     * reference. 
     * 
     * @param todo The todo to update.
     */
    @SneakyThrows
    public void update(Todo todo) {
        todoDao.update(todo);
    }
    
    /**
     * Deletes provided todo from the database. 
     * 
     * @param todo The todo to delete.
     */
    @SneakyThrows
    public void delete(Todo todo) {
        deorderTodo(todo);
        todoDao.delete(todo);
    }
    
    /**
     * Moves the provided todo such that it is after the target todo.
     * 
     * @param todo The todo that will be moved.
     * @param target Where to move the todo after.
     */
    @SneakyThrows
    public void moveTodoAfter(Todo todo, Todo target) {
        deorderTodo(todo);
        insertTodoAfter(todo, target);
    }

    /**
     * Moves the provided todo such that it is before the target todo.
     *  
     * @param todo The todo that will be moved.
     * @param target Where to move the todo before.
     */
    @SneakyThrows
    public void moveTodoBefore(Todo todo, Todo target) {
        deorderTodo(todo);
        insertTodoBefore(todo, target);
    }

    /**
     * Builds a new Todo and stores it in the database. However, it is not 
     * set with an ordinal.
     * 
     * @return A reference to the Todo that was just created.
     */
    @SneakyThrows
    private Todo createTodo() {
        final Todo todo = new Todo();
        todo.setDescription("Blank Todo");
        todoDao.create(todo);
        return todo;
    }
    
    /**
     * Inserts the provided at the end of the todo list.
     * 
     * @param todo The todo to insert.
     */
    @SneakyThrows
    private void insertTodoLast(Todo todo) {
        final int ordinal = getAllTodos().size();

        todo.setOrdinal(ordinal + 1L);
        todoDao.update(todo);
    }

    /**
     * Inserts the provided todo such that it is after the target todo.
     * 
     * @param todo The todo that will be inserted.
     * @param target Where to insert the todo after.
     */
    @SneakyThrows
    private void insertTodoAfter(Todo todo, Todo target) {
        // Keep ordinal updated due to other operations.
        todoDao.refresh(todo);
        todoDao.refresh(target);
        
        final long targetOrdinal = target.getOrdinal();

        // Push following todos down. This excludes the target todo.
        val update = todoDao.updateBuilder();
        update.updateColumnExpression("ordinal", "ordinal + 1");
        update.where().gt("ordinal", targetOrdinal);
        update.update();

        // Move the todo after the target.
        todo.setOrdinal(targetOrdinal + 1L);
        todoDao.update(todo);
    }

    /**
     * Inserts the provided todo such that it is before the target todo.
     * 
     * @param todo The todo that will be inserted.
     * @param target Where to insert the todo before.
     */
    @SneakyThrows
    private void insertTodoBefore(Todo todo, Todo target) {
        // Keep ordinal updated due to other operations.
        todoDao.refresh(todo);
        todoDao.refresh(target);
        
        final long targetOrdinal = target.getOrdinal();

        // Push following todos down. This excludes the target todo.
        val update = todoDao.updateBuilder();
        update.updateColumnExpression("ordinal", "ordinal + 1");
        update.where().gt("ordinal", targetOrdinal);
        update.update();

        // Move the target todo down.
        target.setOrdinal(targetOrdinal + 1L);
        todoDao.update(target);

        // Put the todo in the target's original place.
        todo.setOrdinal(targetOrdinal);
        todoDao.update(todo);
    }

    /**
     * This removes the provided todo from the ordinal sequence so that any
     * following todos can move up. This is the equivalent to a remove
     * operation except that the todo is still stored in the database albeit
     * with an invalid ordinal.
     * 
     * This method is used only by move operations.
     * 
     * @param todo The todo to temporarily 'remove'.
     */
    @SneakyThrows
    private void deorderTodo(Todo todo) {
        // Keep ordinal updated due to other operations.
        todoDao.refresh(todo);
        
        final long todoOrdinal = todo.getOrdinal();

        // Invalidate the todo ordinal
        todo.setOrdinal(-1L);
        todoDao.update(todo);

        // Push any following todos up a slot.
        val update = todoDao.updateBuilder();
        update.updateColumnExpression("ordinal", "ordinal - 1");
        update.where().gt("ordinal", todoOrdinal);
        update.update();
    }
}
