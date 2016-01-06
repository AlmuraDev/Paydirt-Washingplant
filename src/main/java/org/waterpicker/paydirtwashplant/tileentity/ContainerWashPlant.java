package org.waterpicker.paydirtwashplant.tileentity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;

public class ContainerWashPlant extends Container {
    private WashPlantTile washplant;
    private int washTime;
    private int fluidLevel;
    private int powerLevel;

    public ContainerWashPlant(IInventory inventory, WashPlantTile washplant) {
        this.washplant = washplant;
        addSlotToContainer(new Slot(washplant, 0, 125, 16));
        addSlotToContainer(new Slot(washplant, 1, 125, 61));
    }

    @Override
    public boolean canInteractWith(EntityPlayer p_75145_1_) {
        return true;
    }

    @Override
    public void addCraftingToCrafters(ICrafting iCrafting) {
        super.addCraftingToCrafters(iCrafting);
        iCrafting.sendProgressBarUpdate(this, 0, washplant.getWashTime());
        iCrafting.sendProgressBarUpdate(this, 1, washplant.getFluidLevel());
        iCrafting.sendProgressBarUpdate(this, 2, washplant.getPowerLevel());
    }

    @Override
    public void detectAndSendChanges() {
        super.detectAndSendChanges();

        for(int i = 0; i < crafters.size(); ++i) {
            ICrafting iCrafting = (ICrafting) crafters.get(i);

            if(washTime != washplant.getWashTime())
                iCrafting.sendProgressBarUpdate(this, 0, washplant.getWashTime());
            if(fluidLevel != washplant.getFluidLevel())
                iCrafting.sendProgressBarUpdate(this, 1, washplant.getFluidLevel());
            if(powerLevel != washplant.getPowerLevel())
                iCrafting.sendProgressBarUpdate(this, 2, washplant.getPowerLevel());
        }

        washTime = washplant.getWashTime();
        fluidLevel = washplant.getFluidLevel();
        powerLevel = washplant.getPowerLevel();
    }
}
