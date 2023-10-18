package dev.crossvas.farming.blockentities.base;

import net.minecraftforge.energy.EnergyStorage;

public abstract class CustomEnergyStorage extends EnergyStorage {

    public CustomEnergyStorage() {
        super(10000, 500);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int extractedEnergy = super.extractEnergy(maxExtract, simulate);
        if (extractedEnergy != 0) {
            onEnergyChanged();
        }
        return extractedEnergy;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int energyReceived = super.receiveEnergy(maxReceive, simulate);
        if (energyReceived != 0) {
            onEnergyChanged();
        }
        return energyReceived;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    public void setEnergy(int energy) {
        this.energy = energy;
        this.onEnergyChanged();
    }

    public void consumeEnergy(int energy) {
        this.energy -= energy;
        if (this.energy < 0) {
            this.energy = 0;
        }
        this.onEnergyChanged();
    }

    public abstract void onEnergyChanged();
}
