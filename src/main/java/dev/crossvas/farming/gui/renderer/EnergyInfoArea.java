package dev.crossvas.farming.gui.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.List;

public class EnergyInfoArea extends InfoArea {

    private IEnergyStorage energy;

    public EnergyInfoArea(int xMin, int yMin)  {
        this(xMin, yMin, null,16,16);
    }

    public EnergyInfoArea(int xMin, int yMin, IEnergyStorage energy)  {
        this(xMin, yMin, energy,16,16);
    }

    public EnergyInfoArea(int xMin, int yMin, IEnergyStorage energy, int width, int height)  {
        super(new Rect2i(xMin, yMin, width, height));
        this.energy = energy;
    }

    public List<Component> getTooltips() {
        return List.of(Component.literal(energy.getEnergyStored() + "/" + energy.getMaxEnergyStored()+" FE"));
    }

    @Override
    public void draw(PoseStack transform) {
        final int height = BOX_AREA.getHeight();
        int stored = (int)(height*(energy.getEnergyStored()/(float)energy.getMaxEnergyStored()));
        fillGradient(
                transform,
                BOX_AREA.getX(), BOX_AREA.getY()+(height-stored),
                BOX_AREA.getX() + BOX_AREA.getWidth(), BOX_AREA.getY() + BOX_AREA.getHeight(),
                0xffb51500, 0xff600b00
        );
    }
}
