package net.cdahmedeh.ultimeter.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;

import net.cdahmedeh.ultimeter.persistence.type.ZonedDateTimePersister;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import com.j256.ormlite.field.DatabaseField;

/**
 * A Todo represents a task to be performed. Todos can be ordered by the user
 * and organized in a hierarchy.
 * 
 * Todo ordering is determined by the ordinal field. The first todo starts
 * with index 1 upwards. The ordinal field is dependent on the parent, so
 * ordinal numbers are only unique per parent.
 *
 * @author Ahmed El-Hajjar
 *
 */
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class Todo implements Serializable {
    private static final long serialVersionUID = 4826727042878058610L;

    @Getter @Setter
    @DatabaseField(generatedId = true)
    private long id = -1L;

    @Getter @Setter
    @DatabaseField(uniqueCombo = true, foreign = true)
    private Todo parent = null;

    @Getter @Setter
    @DatabaseField(uniqueCombo = true)
    private long ordinal = -1L;
    
    @Getter @Setter @NonNull
    @DatabaseField(canBeNull = false)
    private String description = "";
    
    @Getter @Setter
    @DatabaseField(persisterClass = ZonedDateTimePersister.class)
    private ZonedDateTime dueDate = null;
}