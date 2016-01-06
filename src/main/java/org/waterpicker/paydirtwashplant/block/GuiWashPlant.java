package org.waterpicker.paydirtwashplant.block;

import net.minecraft.client.gui.GuiConfirmOpenLink;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;
import org.waterpicker.paydirtwashplant.tileentity.ContainerWashPlant;
import org.waterpicker.paydirtwashplant.tileentity.WashPlantTile;

public class GuiWashPlant extends GuiContainer {
    public GuiWashPlant(IInventory playerInv, WashPlantTile te) {
        super(new ContainerWashPlant(playerInv, te));
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
    }
}
