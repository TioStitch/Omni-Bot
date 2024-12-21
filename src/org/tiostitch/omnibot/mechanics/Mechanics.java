package org.tiostitch.omnibot.mechanics;

import lombok.RequiredArgsConstructor;
import org.tiostitch.omnibot.OmniCore;
import org.tiostitch.omnibot.abstraction.MechanicImpl;

import java.util.ArrayList;

@RequiredArgsConstructor
public class Mechanics {

    private final OmniCore omniCore;

    private final ArrayList<MechanicImpl> registered_mechanics = new ArrayList<>();

    public void init() {



    }

}
