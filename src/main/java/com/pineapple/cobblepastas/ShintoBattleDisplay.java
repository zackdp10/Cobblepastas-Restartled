package com.pineapple.cobblepastas;

import net.minecraft.class_2561;
import net.minecraft.class_5250;
import java.util.Set;

/** Shared form check and translated label, with no Minecraft client dependencies. */
public final class ShintoBattleDisplay {
    private ShintoBattleDisplay() {}
    public static boolean isShitno(String speciesId, Set<String> aspects) {
        return "cobblemon:shinto".equals(speciesId) && aspects.contains("shitno-form");
    }
    public static class_5250 formName() {
        return class_2561.method_43471("cobblemon.species.shinto-shitno.name");
    }
    public static boolean isDefaultName(class_5250 displayed, class_5250 speciesName) {
        return displayed.equals(speciesName) || "Shinto".equals(displayed.getString());
    }
}
