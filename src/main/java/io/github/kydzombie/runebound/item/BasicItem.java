package io.github.kydzombie.runebound.item;

import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class BasicItem extends TemplateItem {
    public BasicItem(Identifier identifier) {
        super(identifier);
        setTranslationKey(identifier);
    }
}
