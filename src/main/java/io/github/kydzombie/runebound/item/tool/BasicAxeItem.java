package io.github.kydzombie.runebound.item.tool;

import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.template.item.TemplateAxeItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class BasicAxeItem extends TemplateAxeItem {
    public BasicAxeItem(Identifier identifier, ToolMaterial material) {
        super(identifier, material);
        setTranslationKey(identifier);
    }
}
