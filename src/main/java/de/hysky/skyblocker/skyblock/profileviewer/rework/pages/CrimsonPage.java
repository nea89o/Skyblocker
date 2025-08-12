package de.hysky.skyblocker.skyblock.profileviewer.rework.pages;

import de.hysky.skyblocker.annotations.Init;
import de.hysky.skyblocker.skyblock.profileviewer.model.*;
import de.hysky.skyblocker.skyblock.profileviewer.rework.ProfileLoadState;
import de.hysky.skyblocker.skyblock.profileviewer.rework.ProfileViewerPage;
import de.hysky.skyblocker.skyblock.profileviewer.rework.ProfileViewerScreenRework;
import de.hysky.skyblocker.skyblock.profileviewer.rework.ProfileViewerWidget;
import de.hysky.skyblocker.skyblock.profileviewer.rework.widgets.BarWidget;
import de.hysky.skyblocker.skyblock.profileviewer.rework.widgets.BoxedTextWidget;
import de.hysky.skyblocker.skyblock.profileviewer.rework.widgets.ItemWidget;
import de.hysky.skyblocker.skyblock.tabhud.util.Ico;
import de.hysky.skyblocker.utils.TextUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

import static de.hysky.skyblocker.utils.Formatters.INTEGER_NUMBERS;
import static de.hysky.skyblocker.utils.Formatters.SHORT_INTEGER_NUMBERS;

public class CrimsonPage implements ProfileViewerPage {

	List<ProfileViewerWidget.Instance> widgets = new ArrayList<>();

	public CrimsonPage(ProfileLoadState.SuccessfulLoad load) {
		int column1 = calculateX();
		var crimsonIsleData = load.member().netherIslandPlayerData;
		List<Text> tiers = new ArrayList<>();
		for (NetherIslandPlayerData.KuudraCompletedTiers.Tier tier : NetherIslandPlayerData.KuudraCompletedTiers.Tier.values()) {
			tiers.add(Text.literal(tier.getName() + ": ").formatted(Formatting.RED).copy().append(Text.literal(String.valueOf(tier.getCompletions(crimsonIsleData.kuudraCompletedTiers))).formatted(Formatting.WHITE)));
			tiers.add(Text.literal("Highest Wave: ").formatted(Formatting.RED).copy().append(Text.literal(tier.getHighestWave(crimsonIsleData.kuudraCompletedTiers) != 0 ? String.valueOf(tier.getHighestWave(crimsonIsleData.kuudraCompletedTiers)) : "N/A").formatted(Formatting.WHITE)));
		}
		tiers.add(Text.literal("Total Runs: ").formatted(Formatting.RED).copy().append(Text.literal(String.valueOf(NetherIslandPlayerData.KuudraCompletedTiers.getTotalCompletions(crimsonIsleData.kuudraCompletedTiers))).formatted(Formatting.WHITE)));

		var tiersWidget = widget(column1, 0, BoxedTextWidget.boxedText(BarWidget.WIDTH + ItemWidget.WIDTH, tiers, ItemWidget.WIDTH));
		widgets.add(tiersWidget);

		int tierIndex = 0;
		var textRenderer = MinecraftClient.getInstance().textRenderer;
		for (NetherIslandPlayerData.KuudraCompletedTiers.Tier tier : NetherIslandPlayerData.KuudraCompletedTiers.Tier.values()) {
			widgets.add(widget(column1,  ((textRenderer.fontHeight * 2) + 2) * tierIndex, new ItemWidget(tier.getIcon(), false, 1f)));
			tierIndex++;
		}

		int column2 = calculateX() + ProfileViewerScreenRework.GAP;
		var factionsWidget = widget(column2, 0, BoxedTextWidget.boxedTextWithHover(ProfileViewerScreenRework.PAGE_WIDTH - column2 - ProfileViewerScreenRework.GAP,
				List.of(
						BoxedTextWidget.nohover(
								Text.literal("Faction: ").formatted(Formatting.GREEN).copy().append(crimsonIsleData.selectedFaction != null ? Text.literal(TextUtils.titleCase(crimsonIsleData.selectedFaction)).formatted(crimsonIsleData.selectedFaction.equals("mages") ? Formatting.DARK_PURPLE : Formatting.RED) : Text.literal("N/A").formatted(Formatting.WHITE))
						),
						BoxedTextWidget.hover(
								Text.literal("Mage Reputation: ").formatted(Formatting.DARK_PURPLE).copy().append(Text.literal(SHORT_INTEGER_NUMBERS.format(crimsonIsleData.magesReputation)).formatted(Formatting.WHITE)),
								List.of(
										Text.literal(INTEGER_NUMBERS.format(crimsonIsleData.magesReputation))
								)
						),
						BoxedTextWidget.hover(
								Text.literal("Barbarian Reputation: ").formatted(Formatting.RED).copy().append(Text.literal(SHORT_INTEGER_NUMBERS.format(crimsonIsleData.barbariansReputation)).formatted(Formatting.WHITE)),
								List.of(
										Text.literal(INTEGER_NUMBERS.format(crimsonIsleData.barbariansReputation))
								)
						)
				))
		);
		widgets.add(factionsWidget);

		List<BoxedTextWidget.TextWithHover> dojos = new ArrayList<>();
		for (NetherIslandPlayerData.Dojo.DojoTest test : NetherIslandPlayerData.Dojo.DojoTest.values()) {
			dojos.add(
					BoxedTextWidget.hover(
							Text.literal("Test of " + test.getName() + ": " + SHORT_INTEGER_NUMBERS.format(test.getScore(crimsonIsleData.dojo))).formatted(test.getColor()).copy().append(Text.literal(" (" + NetherIslandPlayerData.Dojo.ScoreRank.fromScore(test.getScore(crimsonIsleData.dojo)).name() + ")").formatted(Formatting.WHITE)),
							List.of(
									Text.literal(INTEGER_NUMBERS.format(test.getScore(crimsonIsleData.dojo)))
							)
					)
			);
		}
		dojos.add(
				BoxedTextWidget.hover(
						Text.of("Points: ").copy().append(Text.literal(SHORT_INTEGER_NUMBERS.format(NetherIslandPlayerData.Dojo.getTotalScore(crimsonIsleData.dojo))).formatted(Formatting.GOLD)),
						List.of(
								Text.literal(INTEGER_NUMBERS.format(NetherIslandPlayerData.Dojo.getTotalScore(crimsonIsleData.dojo)))
						)
				)
		);
		dojos.add(
				BoxedTextWidget.nohover(
						Text.of("Belt: ").copy().append(Text.literal(NetherIslandPlayerData.Dojo.Belt.fromScore(NetherIslandPlayerData.Dojo.getTotalScore(crimsonIsleData.dojo)).getName()).formatted(NetherIslandPlayerData.Dojo.Belt.fromScore(NetherIslandPlayerData.Dojo.getTotalScore(crimsonIsleData.dojo)).getColor()))
				)
		);
		widgets.add(widget(column2, factionsWidget.getY() + factionsWidget.getHeight() + ProfileViewerScreenRework.GAP, BoxedTextWidget.boxedTextWithHover(factionsWidget.getWidth() - ProfileViewerScreenRework.GAP, dojos)));

	}

	@Init
	public static void init() {
		ProfileViewerScreenRework.PAGE_CONSTRUCTORS.add(CrimsonPage::new);
	}

	@Override
	public int getSortIndex() {
		return 3;
	}

	@Override
	public ItemStack getIcon() {
		return Ico.NETHERRACK;
	}

	@Override
	public String getName() {
		return "Crimson Isle";
	}

	@Override
	public List<ProfileViewerWidget.Instance> getWidgets() {
		// TODO: add player widget to the left only on this page.
		return widgets;
	}
}
