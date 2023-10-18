package dev.crossvas.farming.gui.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.Rect2i;

public abstract class InfoArea extends GuiComponent {

    protected final Rect2i BOX_AREA;

    protected InfoArea(Rect2i box_area) {
        this.BOX_AREA = box_area;
    }

    public abstract void draw(PoseStack transform);
}
