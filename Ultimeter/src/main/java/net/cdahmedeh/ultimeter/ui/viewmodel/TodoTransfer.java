package net.cdahmedeh.ultimeter.ui.viewmodel;

import static org.apache.commons.lang3.SerializationUtils.deserialize;
import static org.apache.commons.lang3.SerializationUtils.serialize;

import java.io.Serializable;

import lombok.Getter;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;

/**
 * For Todo drag and drop support. Just serializes the Todo object and saves it
 * as a byte array.
 * 
 * @author Ahmed El-Hajjar
 *
 */
public class TodoTransfer extends ByteArrayTransfer {
    @Getter
    private static final TodoTransfer instance = new TodoTransfer();

    private static final String typeName = "Todo";
    private static final int typeId = registerType(typeName);

    @Override
    protected int[] getTypeIds() {
        return new int[] { typeId };
    }

    @Override
    protected String[] getTypeNames() {
        return new String[] { typeName };
    }

    @Override
    protected void javaToNative(Object object, TransferData transferData) {
        super.javaToNative(serialize((Serializable) object), transferData);
    }

    @Override
    protected Object nativeToJava(TransferData transferData) {
        return deserialize((byte[]) super.nativeToJava(transferData));
    }
}
