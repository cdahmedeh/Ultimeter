package net.cdahmedeh.ultimeter.persistence.dao;

import java.util.List;

import lombok.SneakyThrows;
import lombok.val;
import net.cdahmedeh.ultimeter.domain.Todo;
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
     * The root todo is the ancestor of all todos stored. However, it is a 
     * dummy entity in the sense that the user is unable to use it or modify
     * it. 
     * 
     * @return A reference to the root todo.
     */
    @SneakyThrows
    public Todo getRootTodo() {
        return todoDao.queryBuilder().
                where().isNull("parent_id").
                queryForFirst();
    }

    /**
     * Retrieves of all sub-todos of the provided todo. The children are sorted
     * according to their ordinal. 
     * 
     * @param parent A Todo reference.
     * @return A list of all immediate children of parent.
     */
    @SneakyThrows
    public List<Todo> getChildren(Todo parent) {
        return todoDao.queryBuilder().
                orderBy("ordinal", true).
                where().eq("parent_id", parent).
                query();
    }

    /**
     * Creates a new todo as a child of the root todo.
     */
    @SneakyThrows
    public Todo addNewTodo() {
        final Todo todo = createTodo();
        insertTodoLast(todo, getRootTodo());
        todoDao.refresh(todo);
        return todo;
    }
    
    /**
     * Updates todo data in the database with parameters stored in the provided
     * reference. 
     * 
     * This method does not update data for any children todos. 
     * 
     * @param todo The todo to update.
     */
    @SneakyThrows
    public void update(Todo todo) {
        todoDao.update(todo);
    }
    
    /**
     * Moves the provided todo such that it is after the target todo. The 
     * provided todo will share the same parent as target todo.
     * 
     * This method will silently fail if the todo is a child of target. This 
     * is to prevent orphaned entries.
     * 
     * @param todo The todo that will be moved.
     * @param target Where to move the todo after.
     */
    @SneakyThrows
    public void moveTodoAfter(Todo todo, Todo target) {
        if (isChild(target, todo)) { return; }

        deorderTodo(todo);
        insertTodoAfter(todo, target);
    }

    /**
     * Moves the provided todo such that it is before the target todo. The 
     * provided todo will share the same parent as target todo.
     *  
     * This method will silently fail if the todo is a child of target. This 
     * is to prevent orphaned entries.
     *
     * @param todo The todo that will be moved.
     * @param target Where to move the todo before.
     */
    @SneakyThrows
    public void moveTodoBefore(Todo todo, Todo target) {
        if (isChild(target, todo)) { return; }

        deorderTodo(todo);
        insertTodoBefore(todo, target);
    }

    /**
     * Moves the provided as the last child of the provided parent.
     * 
     * This method will silently fail if the todo is a child of parent. This 
     * is to prevent orphaned entries.
     * 
     * @param todo The todo to moved.
     * @param parent The new parent for the todo.
     */
    @SneakyThrows
    public void setTodoParent(Todo todo, Todo parent) {
        if (isChild(parent, todo)) { return; }

        deorderTodo(todo);
        insertTodoLast(todo, parent);
    }
    
    /**
     * Builds a new Todo and stores it in the database. However, it is not 
     * set with parent or ordinal.
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
     * Inserts the provided as the last child of the provided parent.
     * 
     * @param todo The todo to insert.
     * @param parent The new parent for the todo.
     */
    @SneakyThrows
    private void insertTodoLast(Todo todo, Todo parent) {
        final int ordinal = getChildren(parent).size();

        todo.setParent(parent);
        todo.setOrdinal(ordinal + 1L);
        todoDao.update(todo);
    }

    /**
     * Inserts the provided todo such that it is after the target todo. The 
     * provided todo will share the same parent as target todo. 
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
        update.where().gt("ordinal", targetOrdinal).and().eq("parent_id", target.getParent());
        update.update();

        // Move the todo after the target.
        todo.setParent(target.getParent());
        todo.setOrdinal(targetOrdinal + 1L);
        todoDao.update(todo);
    }

    /**
     * Inserts the provided todo such that it is before the target todo. The 
     * provided todo will share the same parent as target todo. 
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
        update.where().gt("ordinal", targetOrdinal).and()
                .eq("parent_id", target.getParent());
        update.update();

        // Move the target todo down.
        target.setOrdinal(targetOrdinal + 1L);
        todoDao.update(target);

        // Put the todo in the target's original place.
        todo.setParent(target.getParent());
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
        final long todoOrdinal = todo.getOrdinal();

        // Invalidate the todo ordinal
        todo.setOrdinal(-1L);
        todoDao.update(todo);

        // Push any following todos up a slot.
        val update = todoDao.updateBuilder();
        update.updateColumnExpression("ordinal", "ordinal - 1");
        update.where().gt("ordinal", todoOrdinal).and().eq("parent_id", todo.getParent());
        update.update();
    }

    /**
     * Checks if the provided todo is a child of the provided parent.
     * 
     * @param child The todo to verify ancestry.
     * @param parent A potential ancestor of the provided todo.
     * @return 'true' if child is a descendant of parent, 'false' otherwise.
     */
    @SneakyThrows
    private boolean isChild(Todo child, Todo parent) {
        Todo pointer = child;

        while (pointer != null) {
            if (pointer.equals(parent)) {
                return true;
            }

            // Ensures that foreign object is loaded by ORM.
            todoDao.refresh(pointer);
            
            pointer = pointer.getParent();
        }

        return false;
    }
}
