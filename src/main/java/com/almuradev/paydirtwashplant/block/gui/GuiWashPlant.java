/*
 * This file is part of Paydirt-Washplant.
 *
 * Copyright (c) AlmuraDev <https://github.com/AlmuraDev/>
 *
 * All Rights Reserved.
 */
package com.almuradev.paydirtwashplant.block.gui;

import com.almuradev.paydirtwashplant.Config;
import com.almuradev.paydirtwashplant.PDWPMod;
import com.almuradev.paydirtwashplant.tileentity.TileEntityWashplant;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public final class GuiWashPlant extends GuiContainer {

    private static final ResourceLocation washplantBackground = new ResourceLocation(PDWPMod.MODID + ":textures/gui/washplant.png");
    private static final int WHITE = 0xFFFFFF;
    private final TileEntityWashplant washplant;
    private int mouseX, mouseY;

    public GuiWashPlant(final InventoryPlayer playerInv, final TileEntityWashplant te) {
        super(new ContainerWashPlant(playerInv, te));
        this.washplant = te;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(washplantBackground);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);

        int i1;

        if (this.washplant.isWashing()) {
            i1 = this.washplant.washTimeScaled(20);
            drawTexturedModalRect(k + 124, l + 38, 176, 0, i1, 19);
        }

        i1 = this.washplant.powerScaled(24);
        drawTexturedModalRect(k + 22, l + 44, 208, 0, i1, 9);


        i1 = this.washplant.fluidScaled(23); //TODO this does not work
        this.mc.player.sendChatMessage("The fluid level is: " + this.washplant.getFluidLevel());
        this.mc.player.sendChatMessage("The fluid max is: " + this.washplant.tank.getCapacity());
        this.mc.player.sendChatMessage("The fluid level pixels: " + i1);
        drawTexturedModalRect(k + 86, l + 24 + 72 - i1, 196, 72 - i1, 12, i1);

        drawCenteredString(this.fontRenderer, this.washplant.getDisplayName().getFormattedText(), k + 7, l + 5, WHITE);

        drawCursor(k, l);
    }

    @Override
    public void drawScreen(int x, int y, float f) {
        this.mouseX = x;
        this.mouseY = y;

        super.drawScreen(x, y, f);
    }

    private void drawCursor(int x, int y) {
        int z = this.mouseX - x;
        int w = this.mouseY - y;

        if (isBetween(18, 49, z) && isBetween(40, 49, w)) {
            drawString(this.fontRenderer, this.washplant.getPowerLevel() + " EU @ " + Config.EU_PER_OPERATION + " per cycle", this.mouseX + 10,
                this.mouseY, WHITE);
        }
        if (isBetween(82, 101, z) && isBetween(20, 74, w)) {
            drawString(this.fontRenderer, this.washplant.getFluidLevel() + " mb @ " + Config.WATER_PER_OPERATION + " per cycle", this.mouseX + 10,
                this.mouseY, WHITE);
        }
    }

    private boolean isBetween(int a, int b, int c) {
        return a <= c && c <= b;
    }
}
