// This file is part of KeY - Integrated Deductive Software Design 
//
// Copyright (C) 2001-2011 Universitaet Karlsruhe (TH), Germany 
//                         Universitaet Koblenz-Landau, Germany
//                         Chalmers University of Technology, Sweden
// Copyright (C) 2011-2013 Karlsruhe Institute of Technology, Germany 
//                         Technical University Darmstadt, Germany
//                         Chalmers University of Technology, Sweden
//
// The KeY system is protected by the GNU General 
// Public License. See LICENSE.TXT for details.
//

package de.uka.ilkd.key.gui.nodeviews;

import de.uka.ilkd.key.gui.SearchBar;
import de.uka.ilkd.key.pp.Range;
import de.uka.ilkd.key.util.Pair;
import java.awt.Color;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.JCheckBox;

/*
 * Search bar implementing search function for SequentView.
 */

public class SequentSearchBar extends SearchBar {

    public static final Color SEARCH_HIGHLIGHT_COLOR_1 =
            new Color(255, 140, 0, 178);
    public static final Color SEARCH_HIGHLIGHT_COLOR_2 =
            new Color(255, 140, 0, 100);
    
    private List<Pair<Integer,Object>> searchResults;
    private int resultIteratorPos;
    private boolean regExpSearch = false;
    private SequentView sequentView;

    public SequentSearchBar(SequentView sequentView) {
        this.sequentView = sequentView;
        searchResults = new ArrayList<Pair<Integer,Object>>();
    }
    
    public void setSequentView(SequentView sequentView){
        this.sequentView = sequentView;
        search();
    }
    
    @Override
    public void createUI(){
        super.createUI();
        JCheckBox checkBox = new JCheckBox("RegExp");
            checkBox.addItemListener(new ItemListener() {
                @Override
                public void itemStateChanged(ItemEvent e) {
                    regExpSearch = ((JCheckBox)e.getItemSelectable())
                            .isSelected();
                    searchField.requestFocus();
                    search();
                }
            });
            checkBox.setSelected(regExpSearch);
            checkBox.setToolTipText("Evaluate as regular expression");
        add(checkBox);
    }

    public void searchNext() {
        if (!searchResults.isEmpty()) {
            resetExtraHighlight();
            resultIteratorPos++;
            resultIteratorPos %= searchResults.size();
            setExtraHighlight(resultIteratorPos);
        }
    }

    public void searchPrevious() {
        if (!searchResults.isEmpty()) {
            resetExtraHighlight();
            resultIteratorPos++;
            resultIteratorPos %= searchResults.size();
            setExtraHighlight(resultIteratorPos);
        }
    }

    @Override
    public void setVisible(boolean vis) {
        super.setVisible(vis);
        if (sequentView != null) {
            if (vis) {
                search();
            } else {
                clearSearchResults();
            }
        }
    }
    
    /**
     * searches for the occurence of the specified string
     */
    public boolean search(String search) {
        clearSearchResults();

        if (sequentView == null || sequentView.getText() == null || search.equals("")
                || !this.isVisible()) {
            return true;
        }
        
        resultIteratorPos = 0;
        int searchFlag = 0;
        if (search.toLowerCase().equals(search)) {
            // no capital letters used --> case insensitive matching
            searchFlag = searchFlag | Pattern.CASE_INSENSITIVE
                    | Pattern.UNICODE_CASE;
        }
        if (!regExpSearch) {
            // search for literal string instead of regExp
            searchFlag = searchFlag | Pattern.LITERAL;
        }

        Pattern p;
        try {
            p = Pattern.compile(search, searchFlag);
        } catch (PatternSyntaxException pse) {
            return false;
        } catch (IllegalArgumentException iae) {
            return false;
        }
        Matcher m = p.matcher(sequentView.getText());

        boolean loopEnterd = false;
        while (m.find()) {
                int foundAt = m.start();
                Object highlight = sequentView.getColorHighlight(SEARCH_HIGHLIGHT_COLOR_2);
                searchResults.add(new Pair<Integer,Object>(foundAt, highlight));
                sequentView.paintHighlight(new Range(foundAt, m.end()), highlight);
                loopEnterd = true;
        }
        if (loopEnterd) {
            return true;
        } else {
            return false;
        }
    }
    
    private void setExtraHighlight(int resultIndex) {
        resetHighlight(resultIndex,
                       sequentView.getColorHighlight(SEARCH_HIGHLIGHT_COLOR_1));
        sequentView.setCaretPosition(searchResults.get(resultIndex).first);
    }


    private void resetExtraHighlight() {
        resetHighlight(resultIteratorPos,
                       sequentView.getColorHighlight(SEARCH_HIGHLIGHT_COLOR_2));
    }
    
    private void resetHighlight(int resultIndex, Object highlight) {
        int pos = searchResults.get(resultIndex).first;
        sequentView.removeHighlight(searchResults.get(resultIndex).second);
        Pair<Integer, Object> highlightPair =
                new Pair<Integer, Object>(pos, highlight);
        sequentView.paintHighlight(new Range(pos, pos + searchField.getText().length()), highlight);
        searchResults.set(resultIndex, highlightPair);
    }
    
    private void clearSearchResults() {
        for (Pair result : searchResults) {
            sequentView.removeHighlight(result.second);
        }
        searchResults.clear();
    }
}