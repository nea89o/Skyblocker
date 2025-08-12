package de.hysky.skyblocker.skyblock.profileviewer.rework.pages;

import de.hysky.skyblocker.annotations.Init;
import de.hysky.skyblocker.skyblock.profileviewer.model.DefaultCatacombs;
import de.hysky.skyblocker.skyblock.profileviewer.model.Dungeons;
import de.hysky.skyblocker.skyblock.profileviewer.model.GenericCatacombs;
import de.hysky.skyblocker.skyblock.profileviewer.model.PlayerData;
import de.hysky.skyblocker.skyblock.profileviewer.rework.ProfileLoadState;
import de.hysky.skyblocker.skyblock.profileviewer.rework.ProfileViewerPage;
import de.hysky.skyblocker.skyblock.profileviewer.rework.ProfileViewerScreenRework;
import de.hysky.skyblocker.skyblock.profileviewer.rework.ProfileViewerWidget;
import de.hysky.skyblocker.skyblock.profileviewer.rework.widgets.BarWidget;
import de.hysky.skyblocker.skyblock.profileviewer.rework.widgets.BoxedTextWidget;
import de.hysky.skyblocker.skyblock.profileviewer.utils.LevelFinder;
import de.hysky.skyblocker.skyblock.tabhud.util.Ico;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.IntStream;

import static de.hysky.skyblocker.utils.Formatters.*;

public class DungeonsPage implements ProfileViewerPage {

	List<ProfileViewerWidget.Instance> widgets = new ArrayList<>();

	public DungeonsPage(ProfileLoadState.SuccessfulLoad load) {
		var dungeonsData = load.member().dungeons;
		List<BarWidget> dungeonClassWidgets = new ArrayList<>();
		for (Dungeons.Class dungeonClass : Dungeons.Class.values()) {
			String name = dungeonClass.getName();
			if (dungeonsData.selectedDungeonClass.equals(name.toLowerCase())) name = "§a" + name + "§r";
			dungeonClassWidgets.add(new BarWidget(name, dungeonClass.getIcon(), dungeonsData.getClassData(dungeonClass).getLevelInfo(), OptionalInt.empty(), OptionalInt.empty()));
		}

		LevelFinder.LevelInfo classAverageLevelInfo = new LevelFinder.LevelInfo(0, 0);
		for (var dungeonClassWidget : dungeonClassWidgets) {
			var currentClassInfo = dungeonClassWidget.getLevelInfo();
			classAverageLevelInfo.level += currentClassInfo.level;
			// Should partial levels count towards class average?
			classAverageLevelInfo.fill += currentClassInfo.fill;

			if (classAverageLevelInfo.nextLevelXP == 0
					|| classAverageLevelInfo.nextLevelXP < currentClassInfo.nextLevelXP
					|| (classAverageLevelInfo.nextLevelXP == currentClassInfo.nextLevelXP && classAverageLevelInfo.levelXP < currentClassInfo.levelXP)
			) {
				classAverageLevelInfo.levelXP = currentClassInfo.levelXP;
				// TODO: this model for XP to next level is not really correct. This is XP towards the next fifth of a level. A more correct approach would be a lot more complicated than this.
				classAverageLevelInfo.nextLevelXP = currentClassInfo.nextLevelXP;
			}
			classAverageLevelInfo.xp += currentClassInfo.xp;
		}
		double classAverage = (classAverageLevelInfo.level + classAverageLevelInfo.fill) / 5.0;
		classAverageLevelInfo.level = (int) classAverage;
		classAverageLevelInfo.fill = classAverage - classAverageLevelInfo.level;
		dungeonClassWidgets.addFirst(new BarWidget("All Classes", Ico.NETHER_STAR, classAverageLevelInfo, OptionalInt.empty(), OptionalInt.empty()));

		int dungeonClassIndex = 0;
		int defaultDungeonsClassX = calculateX();
		for (var widget : dungeonClassWidgets) {
			int x = dungeonClassIndex < 6 ? 0 : ((widget.getWidth() + ProfileViewerScreenRework.GAP) + ProfileViewerScreenRework.GAP);
			int y = (dungeonClassIndex % 6) * (2 + widget.getHeight());
			widgets.add(widget(defaultDungeonsClassX + x, y, widget));
			System.out.println(defaultDungeonsClassX + x);
			dungeonClassIndex++;
		}

		int column2 = calculateX();
		var levelWidget = widget(column2 + ProfileViewerScreenRework.GAP, 0, new BarWidget(PlayerData.Skill.CATACOMBS.getName(), PlayerData.Skill.CATACOMBS.getIcon(), PlayerData.Skill.CATACOMBS.getLevelInfo(dungeonsData.dungeonInfo.catacombs.experience), OptionalInt.empty(), OptionalInt.empty()));
		widgets.add(levelWidget);
		int runTotal = (int) (dungeonsData.dungeonInfo.catacombs.tierCompletions.getManuallyCalculatedTotal() + dungeonsData.dungeonInfo.masterModeCatacombs.tierCompletions.getManuallyCalculatedTotal());
		widgets.add(widget(
				column2 + ProfileViewerScreenRework.GAP, levelWidget.getY() + levelWidget.getHeight() + ProfileViewerScreenRework.GAP, BoxedTextWidget.boxedText(BarWidget.WIDTH - BoxedTextWidget.PADDING * 2,
						List.of(
								Text.of("Secrets: " + INTEGER_NUMBERS.format(dungeonsData.secrets)),
								Text.of("Secrets/Run: " + DOUBLE_NUMBERS.format(dungeonsData.secrets / (float) runTotal))
						))
		));

		int column3 = calculateX();
		var runWidget = widget(column3 + ProfileViewerScreenRework.GAP, 0, createFloorStatWidget(dungeonsData.dungeonInfo.catacombs, "F", column3 + ProfileViewerScreenRework.GAP));
		widgets.add(runWidget);
		widgets.add(widget(column3 + ProfileViewerScreenRework.GAP, runWidget.getHeight() + ProfileViewerScreenRework.GAP,createFloorStatWidget(dungeonsData.dungeonInfo.masterModeCatacombs, "M", column3 + ProfileViewerScreenRework.GAP)));
		// TODO: for tomorrow morning me: add a toggle button
	}

	ProfileViewerWidget createFloorStatWidget(GenericCatacombs cata, String prefix, int x) {
		return BoxedTextWidget.boxedTextWithHover((ProfileViewerScreenRework.PAGE_WIDTH - x) - ProfileViewerScreenRework.GAP,
				IntStream.of(1, 2, 3, 4, 5, 6, 7)
						.mapToObj(floor ->
								BoxedTextWidget.hover(
										Text.of(prefix + floor + " Runs: " + INTEGER_NUMBERS.format(cata.tierCompletions.getValueOrZero(floor))),
										List.of(
												Text.literal("Personal Best: ").formatted(Formatting.GRAY).append(formatPB(cata.fastestTime.getValue(floor))),
												Text.literal("Personal Best (S): ").formatted(Formatting.GRAY).append(formatPB(cata.fastestTimeS.getValue(floor))),
												Text.literal("Personal Best (S+): ").formatted(Formatting.GREEN).append(formatPB(cata.fastestTimeSPlus.getValue(floor)))
										))
						).toList());
	}


	private static Text formatPB(@Nullable Double d) {
		if (d != null)
			return Text.literal(formatTimespan(Duration.ofMillis(d.longValue()))).formatted(Formatting.GOLD);
		return Text.literal("N/A").formatted(Formatting.DARK_GRAY);
	}

	@Init
	public static void init() {
		ProfileViewerScreenRework.PAGE_CONSTRUCTORS.add(DungeonsPage::new);
	}

	@Override
	public int getSortIndex() {
		return 2;
	}

	@Override
	public ItemStack getIcon() {
		return Ico.CATACOMBS_SKULL;
	}

	@Override
	public String getName() {
		return "Dungeons";
	}

	@Override
	public List<ProfileViewerWidget.Instance> getWidgets() {
		// TODO: add player widget to the left only on this page.
		return widgets;
	}
}
