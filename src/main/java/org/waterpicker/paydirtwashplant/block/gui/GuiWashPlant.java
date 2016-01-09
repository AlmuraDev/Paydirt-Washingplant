package org.waterpicker.paydirtwashplant.block.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.waterpicker.paydirtwashplant.PDWPMod;
import org.waterpicker.paydirtwashplant.tileentity.TileEntityWashplant;

public class GuiWashPlant extends GuiContainer {
    private static final ResourceLocation washplantBackground = new ResourceLocation(PDWPMod.MODID + ":textures/gui/washplant.png");
    private TileEntityWashplant washplant;
    public GuiWashPlant(InventoryPlayer playerInv, TileEntityWashplant te) {
        super(new ContainerWashPlant(playerInv, te));
        washplant = te;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(washplantBackground);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        int i1;

        if (washplant.isWashing()) {
            i1 = washplant.washTimeScaled(20);
            drawTexturedModalRect(k + 124, l + 38, 176, 0, i1, 19);
        }

        i1 = washplant.powerScaled(24);
        drawTexturedModalRect(k + 22, l + 44, 208, 0, i1, 9);


        i1 = washplant.fluidScaled(72);
        drawTexturedModalRect(k + 86, l + 24 + 72 - i1, 196, 72 - i1, 12, i1);

    }
}
