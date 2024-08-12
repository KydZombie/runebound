package io.github.kydzombie.runebound.item;

import com.matthewperiut.accessoryapi.api.Accessory;
import com.matthewperiut.accessoryapi.api.render.AccessoryRenderer;
import com.matthewperiut.accessoryapi.api.render.HasCustomRenderer;
import com.matthewperiut.accessoryapi.api.render.builtin.ConfigurableRenderer;
import com.matthewperiut.accessoryapi.api.render.builtin.GloveRenderer;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Optional;

public class GauntletItem extends TemplateItem implements Accessory, HasCustomRenderer {
    protected ConfigurableRenderer renderer;
    public GauntletItem(Identifier identifier) {
        super(identifier);
        setTranslationKey(identifier);
        setMaxCount(1);
    }

    @Override
    public String[] getAccessoryTypes(ItemStack item) {
        return new String[] { "gloves" };
    }

    @Override
    public Optional<AccessoryRenderer> getRenderer() {
        return Optional.ofNullable(renderer);
    }

    @Override
    public void constructRenderer() {
        renderer = new GloveRenderer("assets/runebound/textures/gauntlet.png");
    }
}
