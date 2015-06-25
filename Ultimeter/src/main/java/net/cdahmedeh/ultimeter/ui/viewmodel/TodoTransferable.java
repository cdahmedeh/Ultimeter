package net.cdahmedeh.ultimeter.ui.viewmodel;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import net.cdahmedeh.ultimeter.core.domain.Todo;

public class TodoTransferable implements Transferable {
    private Todo todo;

    public TodoTransferable(Todo todo) {
        this.todo = todo;
    }
    
    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{};
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return true;
    }

    @Override
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        return todo;
    }

    public Todo getTodo() {
        return todo;
    }
}
