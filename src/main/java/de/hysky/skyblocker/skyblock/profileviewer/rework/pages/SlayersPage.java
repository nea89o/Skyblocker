package de.hysky.skyblocker.skyblock.profileviewer.rework.pages;

import de.hysky.skyblocker.annotations.Init;
import de.hysky.skyblocker.skyblock.profileviewer.model.SlayerData;
import de.hysky.skyblocker.skyblock.profileviewer.rework.*;
import de.hysky.skyblocker.skyblock.profileviewer.rework.widgets.BarWidget;
import de.hysky.skyblocker.skyblock.profileviewer.rework.widgets.EntityViewerWidget;
import de.hysky.skyblocker.skyblock.profileviewer.rework.widgets.PlayerMetaWidget;
import de.hysky.skyblocker.skyblock.profileviewer.rework.widgets.SlayerWidget;
import de.hysky.skyblocker.skyblock.tabhud.util.Ico;
import net.minecraft.item.ItemStack;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class SlayersPage implements ProfileViewerPage {

	List<ProfileViewerWidget.Instance> widgets = new ArrayList<>();

	public SlayersPage(ProfileLoadState.SuccessfulLoad load) {
		var playerWidget = widget(0, 0, new EntityViewerWidget(load.mainMemberId()));
		widgets.add(playerWidget);
		var playerStatsWidget = widget(0, playerWidget.getY() + playerWidget.getHeight() + ProfileViewerScreenRework.GAP, new PlayerMetaWidget(load));
		widgets.add(playerStatsWidget);
		var slayerData = load.member().slayer;
		List<ProfileViewerWidget> slayerWidgets = new ArrayList<>();
		for (SlayerData.Slayer slayer : SlayerData.Slayer.values()) {
			slayerWidgets.add(new BarWidget(slayer.getName(), slayer.getIcon(), slayerData.getSkillLevel(slayer), OptionalInt.empty(), OptionalInt.empty()));
		}
		for (SlayerData.Slayer slayer : SlayerData.Slayer.values()) {
			slayerWidgets.add(new SlayerWidget(slayer, slayerData.getSlayerData(slayer)));
		}

		int skillIndex = 0;
		int defaultSlayerX = calculateX();
		for (var widget : slayerWidgets) {
			int x = (skillIndex < 6 ? 0 : widget.getWidth() + ProfileViewerScreenRework.GAP) + ProfileViewerScreenRework.GAP;
			int y = (skillIndex % 6) * (2 + widget.getHeight());
			widgets.add(widget(defaultSlayerX + x, y, widget));
			skillIndex++;
		}
	}

	@Init
	public static void init() {
		ProfileViewerScreenRework.PAGE_CONSTRUCTORS.add(SlayersPage::new);
	}

	@Override
	public int getSortIndex() {
		return 1;
	}

	@Override
	public ItemStack getIcon() {
		return Ico.AATROX_BATPHONE_SKULL;
	}

	@Override
	public String getName() {
		return "Slayers";
	}

	@Override
	public List<ProfileViewerWidget.Instance> getWidgets() {
		// TODO: add player widget to the left only on this page.
		return widgets;
	}
}
