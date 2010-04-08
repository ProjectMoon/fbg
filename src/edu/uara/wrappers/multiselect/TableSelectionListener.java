package edu.uara.wrappers.multiselect;
 
import java.util.EventListener;

/**
 * The listener that's notified when a table selection value changes.
 * @author Jan-Friedrich Mutter (jmutter@bigfoot.de)
 */
public interface TableSelectionListener extends EventListener {

    /**
     * Called whenever the value of the selection changes.
     */
    public void valueChanged(TableSelectionEvent e);
}
