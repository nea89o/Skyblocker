package de.hysky.skyblocker.skyblock.profileviewer.rework.pages;

import de.hysky.skyblocker.annotations.Init;
import de.hysky.skyblocker.skyblock.profileviewer.model.PlayerData;
import de.hysky.skyblocker.skyblock.profileviewer.rework.ProfileLoadState;
import de.hysky.skyblocker.skyblock.profileviewer.rework.ProfileViewerPage;
import de.hysky.skyblocker.skyblock.profileviewer.rework.ProfileViewerScreenRework;
import de.hysky.skyblocker.skyblock.profileviewer.rework.ProfileViewerWidget;
import de.hysky.skyblocker.skyblock.profileviewer.rework.widgets.BarWidget;
import de.hysky.skyblocker.skyblock.profileviewer.rework.widgets.EntityViewerWidget;
import de.hysky.skyblocker.skyblock.profileviewer.rework.widgets.PlayerMetaWidget;
import de.hysky.skyblocker.skyblock.profileviewer.utils.LevelFinder;
import de.hysky.skyblocker.skyblock.tabhud.util.Ico;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;

public class SkillsPage implements ProfileViewerPage {

	List<ProfileViewerWidget.Instance> widgets = new ArrayList<>();

	public SkillsPage(ProfileLoadState.SuccessfulLoad load) {
		var playerWidget = widget(0, 0, new EntityViewerWidget(load.mainMemberId()));
		widgets.add(playerWidget);
		var playerStatsWidget = widget(0, playerWidget.getY() + playerWidget.getHeight() + ProfileViewerScreenRework.GAP, new PlayerMetaWidget(load));
		widgets.add(playerStatsWidget);
		var playerData = load.member().playerData;
		List<ProfileViewerWidget> skillsWidgets = new ArrayList<>();
		for (PlayerData.Skill skill : PlayerData.Skill.values()) {
			OptionalInt max = OptionalInt.empty();

			if (skill == PlayerData.Skill.FARMING || skill == PlayerData.Skill.FORAGING || skill == PlayerData.Skill.TAMING) {
				max = OptionalInt.of(50);
			}

			LevelFinder.LevelInfo levelInfo = playerData.getSkillLevel(skill);

			if (skill == PlayerData.Skill.SOCIAL) {
				levelInfo = PlayerData.Skill.SOCIAL.getLevelInfo(load.profile().members.values().stream().mapToDouble(m -> m.playerData.getSkillExperience(PlayerData.Skill.SOCIAL)).sum());
			} else if (skill == PlayerData.Skill.CATACOMBS) {
				levelInfo = PlayerData.Skill.CATACOMBS.getLevelInfo(load.member().dungeons.dungeonInfo.catacombs.experience);
			}

			skillsWidgets.add(new BarWidget(skill.getName(), skill.getIcon(), levelInfo, OptionalInt.empty(), max));
		}

		int skillIndex = 0;
		int defaultSkillX = calculateX();
		for (var widget : skillsWidgets) {
			int x = (skillIndex < 6 ? 0 : widget.getWidth() + ProfileViewerScreenRework.GAP) + ProfileViewerScreenRework.GAP;
			int y = (skillIndex % 6) * (2 + widget.getHeight());
			widgets.add(widget(defaultSkillX + x, y, widget));
			skillIndex++;
		}
	}

	@Init
	public static void init() {
		ProfileViewerScreenRework.PAGE_CONSTRUCTORS.add(SkillsPage::new);
	}

	@Override
	public int getSortIndex() {
		return 0;
	}

	@Override
	public ItemStack getIcon() {
		return Ico.IRON_SWORD;
	}

	@Override
	public String getName() {
		return "Skills";
	}

	@Override
	public List<ProfileViewerWidget.Instance> getWidgets() {
		// TODO: add player widget to the left only on this page.
		return widgets;
	}
}
