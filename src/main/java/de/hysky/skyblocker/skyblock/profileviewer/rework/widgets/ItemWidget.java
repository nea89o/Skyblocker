package de.hysky.skyblocker.skyblock.profileviewer.rework.widgets;

import de.hysky.skyblocker.SkyblockerMod;
import de.hysky.skyblocker.skyblock.profileviewer.rework.ProfileViewerWidget;
import de.hysky.skyblocker.utils.render.HudHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Colors;
import net.minecraft.util.Identifier;

public class ItemWidget implements ProfileViewerWidget {

	private static final Identifier BACKGROUND = Identifier.of(SkyblockerMod.NAMESPACE, "profile_viewer/generic_background");

	public static final int WIDTH = 22;
	public static final int HEIGHT = 22;

	private final ItemStack item;
	private final Boolean background;
	private final float itemScale;

	public ItemWidget(ItemStack item, Boolean background, float itemScale) {
		this.item = item;
		this.background = background;
		this.itemScale = itemScale;
	}

	@Override
	public void render(DrawContext drawContext, int x, int y, int mouseX, int mouseY, float deltaTicks) {
		if (background) HudHelper.renderNineSliceColored(drawContext, BACKGROUND, x, y, WIDTH, HEIGHT, Colors.WHITE);
		drawContext.getMatrices().pushMatrix();
		drawContext.getMatrices().translate(x + 2 / itemScale, y + 2 / itemScale);
		drawContext.getMatrices().scale(itemScale, itemScale);
		drawContext.drawItem(item, 0, 0);
		drawContext.getMatrices().popMatrix();
	}

	@Override
	public int getHeight() {
		return HEIGHT;
	}

	@Override
	public int getWidth() {
		return WIDTH;
	}

}
