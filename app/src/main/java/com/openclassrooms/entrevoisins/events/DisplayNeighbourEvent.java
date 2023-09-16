package com.openclassrooms.entrevoisins.events;

import com.openclassrooms.entrevoisins.model.Neighbour;

/**
 * Event fired when a Neighbour is clicked
 */
public class DisplayNeighbourEvent {

    /**
     * Neighbour to display
     */
    public Neighbour neighbour;

    /**
     * Constructor.
     * @param neighbour
     */
    public DisplayNeighbourEvent(Neighbour neighbour) {
        this.neighbour = neighbour;
    }
}
