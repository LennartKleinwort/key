package de.uka.ilkd.key.nui.tests.guitests;

import static org.junit.Assert.assertTrue;

import org.loadui.testfx.GuiTest;

import de.uka.ilkd.key.nui.DataModel;
import de.uka.ilkd.key.nui.NUI;
import de.uka.ilkd.key.nui.TreeViewState;
import javafx.geometry.Point2D;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.input.KeyCode;

/**
 * Common base class for testing with TestFX.
 * 
 * @author Florian Breitfelder
 *
 */

@SuppressWarnings({ "PMD.BeanMembersShouldSerialize",
        "PMD.AtLeastOneConstructor" })
// Why would anyone ever serialize a JUnit Test?
// PMD will also complain after adding a constructor, then saying "avoid useless
// constructors"
public class NUITestHelper extends GuiTest {

    /**
     * Allows access to all loaded files.
     */
    public NUI nui;
    // TODO is it necessary that this is public?
    // If yes, surround with
    // CHECKSTYLE.OFF: VisibilityModifierCheck <justification>
    // <code to ignore>
    // CHECKSTYLE.ON: VisibilityModifierCheck

    /**
     * DataModel contains all loaded proofs.
     */

    public DataModel dataModel;
    // TODO is it necessary that this is public?
    // If yes, surround with
    // CHECKSTYLE.OFF: VisibilityModifierCheck <justification>
    // <code to ignore>
    // CHECKSTYLE.ON: VisibilityModifierCheck

    @Override
    /**
     * loads all GUI components and returns the root of the current scene.
     * 
     * This method is needed for testfx
     */
    protected Parent getRootNode() {
        nui = new NUI();
        try {
            // load all GUI components
            nui.initializeNUI();
            dataModel = nui.getDataModel();
        }
        catch (Exception e) { // NOPMD
            System.out.println(e.getMessage()); // NOPMD
        }
        return nui.getRoot();
    }

    /**
     * Can used to wait until some event occur.
     * 
     * Wait until NUI statustext is equal to the parameter statustext.
     * 
     * @param statustext
     *            The status text to wait for.
     */
    protected void waitUntilStatusIs(final String statustext) {
        final Label label = ((Label) find("#statustext"));

        while (!label.getText().equals(statustext)) {
            // CHECKSTYLE.OFF: MagicNumberCheck -- externalizing this makes no
            // sense
            sleep(2000);
            // CHECKSTYLE.ON: MagicNumberCheck
        }
    }

    /**
     * Can be used to load a file in GUI mode.
     * 
     * @param filename
     *            the filename of the proof.
     * @param cancelLoading
     *            indicates whether the loading process should be canceled.
     * 
     */

    protected void loadProof(final String filename,
            final boolean cancelLoading) {

        // open load file dialog
        clickOn("File").clickOn("Open Proof...");

        sleep(2000);

        // Enter file name: example01.proof
        final KeyCodeHelper key = new KeyCodeHelper(this);
        key.typeKeys(key.getKeyCode(filename.toUpperCase()));
        
        // press enter to load file
        type(KeyCode.ENTER);

        if (cancelLoading) {// cancel loading process
            clickOn("#cancelButton");

            final Label label = ((Label) find("#statustext"));
            // Loading process was canceled
            assertTrue(label.getText().equals("Loading has been cancelled."));
        }
        else {

            // wait until load file is finished
            waitUntilStatusIs("Ready.");

            // check if proof was loaded and stored in the datamodel

            final TreeViewState treeViewState = dataModel
                    .getTreeViewState(filename);

            assertTrue(treeViewState != null);
            assertTrue(treeViewState.getProof() != null);
            assertTrue(treeViewState.getTreeItem() != null);

            final Label label = ((Label) find("#statustext"));
            // Loading process was canceled
            assertTrue(!label.getText().equals("Loading has been cancelled."));
        }

        final ProgressIndicator progressIndicator = ((ProgressIndicator) find(
                "#progressIndicator"));

        // ProgressIndicator is not visible
        assertTrue(!progressIndicator.isVisible());

        final Button cancelButton = ((Button) find("#cancelButton"));
        assertTrue(!cancelButton.isVisible());
    }

}