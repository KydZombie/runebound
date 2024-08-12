package io.github.kydzombie.runebound.item.tool;

import net.minecraft.item.ToolMaterial;
import net.modificationstation.stationapi.api.template.item.TemplatePickaxeItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class BasicPickaxeItem extends TemplatePickaxeItem {
    public BasicPickaxeItem(Identifier identifier, ToolMaterial material) {
        super(identifier, material);
        setTranslationKey(identifier);
    }

}
