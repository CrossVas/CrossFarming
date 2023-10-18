package dev.crossvas.farming.gui.menus.base;

public interface IEnergyHolderMenu {

    default boolean hasEnergy(int data) {
        return data > 0;
    }

    default int getScaledEnergyBar(int data) {
        int maxEnergyLevel = 10000;
        int energyBarSize = 16;
        return data != 0 ? data * energyBarSize / maxEnergyLevel : 0;
    }
}
