package com.almuradev.paydirtwashplant.block.gui;

import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.almuradev.paydirtwashplant.PDWPMod;
import com.almuradev.paydirtwashplant.tileentity.TileEntityWashplant;

import java.awt.*;

public class GuiWashPlant extends GuiContainer {
	private static final ResourceLocation washplantBackground = new ResourceLocation(PDWPMod.MODID + ":textures/gui/washplant.png");
	private TileEntityWashplant washplant;
	private int WHITE = 0xFFFFFF;
	private int mouseX, mouseY;

	public GuiWashPlant(InventoryPlayer playerInv, TileEntityWashplant te) {
		super(new ContainerWashPlant(playerInv, te));
		washplant = te;

		new Rectangle();
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

		drawCursor(k,l);
	}

	@Override
	public void drawScreen(int x, int y, float f) {
		mouseX = x;
		mouseY = y;

		super.drawScreen(x,y,f);
	}

	private void drawCursor(int x, int y) {
		int z = mouseX - x;
		int w = mouseY - y;

		if(isBetween(18,49,z) && isBetween(40,49,w))
			drawString(fontRendererObj, washplant.getPowerLevel() + " EU", mouseX + 10, mouseY, WHITE);
		if(isBetween(82,101,z) && isBetween(20,74,w))
			drawString(fontRendererObj, washplant.getFluidLevel() + " mb", mouseX + 10, mouseY, WHITE);
	}

	private boolean isBetween(int a, int b, int c) {
		return (a <= c && c <= b) ? true : false;
	}
}
