package de.hysky.skyblocker.skyblock.profileviewer.rework.pages;

import de.hysky.skyblocker.annotations.Init;
import de.hysky.skyblocker.skyblock.profileviewer.model.GlacitePlayerData;
import de.hysky.skyblocker.skyblock.profileviewer.model.MiningCore;
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
import java.util.stream.Stream;

import static de.hysky.skyblocker.utils.Formatters.*;

public class MiningPage implements ProfileViewerPage {

	List<ProfileViewerWidget.Instance> widgets = new ArrayList<>();

	public MiningPage(ProfileLoadState.SuccessfulLoad load) {
		var miningData = load.member().miningCore;
		List<Text> crystalsText = new ArrayList<>();
		for (MiningCore.Crystals.Crystal crystal : MiningCore.Crystals.Crystal.values()) {
			crystalsText.add(Text.literal(crystal.getName() + ": ").formatted(crystal.getColor()).copy().append(Text.literal(crystal.isFound(miningData.crystals) ? "✔" : "✖").formatted(crystal.isFound(miningData.crystals) ? Formatting.GREEN : Formatting.RED)));
		}
		var crystalsWidget = widget(0, 0, BoxedTextWidget.boxedText(BarWidget.WIDTH + (ItemWidget.WIDTH / 2), crystalsText, ItemWidget.WIDTH, 4));
		widgets.add(crystalsWidget);
		int crystalIndex = 0;
		var textRenderer = MinecraftClient.getInstance().textRenderer;
		// Please never let me work on this again. I spent 5 FUCKING HOURS TRYYING TO MAKE IT LOOK GOOD PLEASE END ME - Kath
		for (MiningCore.Crystals.Crystal crystal : MiningCore.Crystals.Crystal.values()) {
			widgets.add(widget(0, ((textRenderer.fontHeight + ProfileViewerScreenRework.GAP) * crystalIndex) - 3, new ItemWidget(crystal.getIcon(), false, 0.85f)));
			crystalIndex++;
		}
		List<BoxedTextWidget.TextWithHover> powderText = new ArrayList<>();
		for (MiningCore.Powder powder : MiningCore.Powder.values()) {
			powderText.add(
					BoxedTextWidget.hover(
							Text.literal(powder.getName() + " Powder : ").formatted(powder.getColor()).copy().append(Text.literal(SHORT_INTEGER_NUMBERS.format(powder.getPowder(miningData))).formatted(Formatting.WHITE)),
							List.of(
									Text.literal(INTEGER_NUMBERS.format(powder.getPowder(miningData)))
							)
					)
			);
			powderText.add(
					BoxedTextWidget.hover(
							Text.literal("Total: ").formatted(powder.getColor()).copy().append(Text.literal(SHORT_INTEGER_NUMBERS.format(powder.getTotalPowder(miningData))).formatted(Formatting.WHITE)),
							List.of(
									Text.literal(INTEGER_NUMBERS.format(powder.getTotalPowder(miningData)))
							)
					)
			);
 		}
		var powderWidget = widget(crystalsWidget.getWidth() + ProfileViewerScreenRework.GAP, 0, BoxedTextWidget.boxedTextWithHover((ProfileViewerScreenRework.PAGE_WIDTH - crystalsWidget.getWidth()) - 10, powderText, ItemWidget.WIDTH));
		widgets.add(powderWidget);
		int tierIndex = 0;
		for (MiningCore.Powder powder : MiningCore.Powder.values()) {
			widgets.add(widget(powderWidget.getX(),  ((textRenderer.fontHeight * 2) + 2) * tierIndex, new ItemWidget(powder.getIcon(), false, 1f)));
			tierIndex++;
		}
		var miscDataWidget = widget(crystalsWidget.getWidth() + ProfileViewerScreenRework.GAP, powderWidget.getHeight() + ProfileViewerScreenRework.GAP, BoxedTextWidget.boxedTextWithHover((ProfileViewerScreenRework.PAGE_WIDTH - crystalsWidget.getWidth()) - 10,
				List.of(
						BoxedTextWidget.hover(
								Text.literal("Nucleus Runs Completed: " + SHORT_INTEGER_NUMBERS.format(MiningCore.Crystals.getNucleusRuns(miningData.crystals))),
								List.of(
										Text.literal(INTEGER_NUMBERS.format(MiningCore.Crystals.getNucleusRuns(miningData.crystals)))
								)
						)
				)
		));
		widgets.add(miscDataWidget);
		var glaciteData = load.member().glacitePlayerData;
		var glaciteWidget = widget(
				crystalsWidget.getWidth() + ProfileViewerScreenRework.GAP, miscDataWidget.getY() + miscDataWidget.getHeight() + ProfileViewerScreenRework.GAP, BoxedTextWidget.boxedTextWithHover((ProfileViewerScreenRework.PAGE_WIDTH - crystalsWidget.getWidth()) - 10,
						List.of(
								BoxedTextWidget.hover(
										Text.literal("Mineshafts Entered: " + SHORT_INTEGER_NUMBERS.format(glaciteData.mineshaftsEntered)),
										List.of(
												Text.literal(INTEGER_NUMBERS.format(glaciteData.mineshaftsEntered))
										)
								),
								BoxedTextWidget.hover(
										Text.literal("Fossil Dust: " + SHORT_INTEGER_NUMBERS.format((int) glaciteData.fossilDust)),
										List.of(
												Text.literal(DOUBLE_NUMBERS.format(glaciteData.fossilDust))
										)
								),
								BoxedTextWidget.hover(
										Text.literal("Corpses Looted: " + SHORT_INTEGER_NUMBERS.format(GlacitePlayerData.getTotalCorpsesLooted(glaciteData))),
										Stream.concat(
												Stream.of(Text.literal("Total: " + INTEGER_NUMBERS.format(GlacitePlayerData.getTotalCorpsesLooted(glaciteData)))),
												glaciteData.corpsesLooted.entrySet().stream()
														.sorted((a, b) -> Integer.compare(b.getValue(), a.getValue()))
														.map(it -> (Text) Text.literal(TextUtils.titleCase(it.getKey()) + ": " + INTEGER_NUMBERS.format(it.getValue())))
										).toList()
								)
						)
				)
		);
		widgets.add(glaciteWidget);
		List<Text> fossilText = new ArrayList<>();
		List<Text> fossilTextHalf = new ArrayList<>();
		for (int i = 0; i < GlacitePlayerData.Fossil.values().length; i++) {
			GlacitePlayerData.Fossil fossil = GlacitePlayerData.Fossil.values()[i];
			Text fossilEntry = Text.literal(fossil.getName() + ": ").copy().append(Text.literal(fossil.isDonated(glaciteData) ? "✔" : "✖").formatted(fossil.isDonated(glaciteData) ? Formatting.GREEN : Formatting.RED));

			if (i < GlacitePlayerData.Fossil.values().length / 2) {
				fossilText.add(fossilEntry);
			} else {
				fossilTextHalf.add(fossilEntry);
			}
		}
		var fossilTextWidget = widget(crystalsWidget.getWidth() + ProfileViewerScreenRework.GAP, glaciteWidget.getY() + glaciteWidget.getHeight() + ProfileViewerScreenRework.GAP, BoxedTextWidget.boxedText((((ProfileViewerScreenRework.PAGE_WIDTH - crystalsWidget.getWidth()) - 10) / 2) - ProfileViewerScreenRework.GAP, fossilText));
		widgets.add(fossilTextWidget);
		widgets.add(widget((crystalsWidget.getWidth() + ProfileViewerScreenRework.GAP) + (fossilTextWidget.getWidth() + ProfileViewerScreenRework.GAP), glaciteWidget.getY() + glaciteWidget.getHeight() + ProfileViewerScreenRework.GAP, BoxedTextWidget.boxedText((((ProfileViewerScreenRework.PAGE_WIDTH - crystalsWidget.getWidth()) - 10) / 2) - ProfileViewerScreenRework.GAP, fossilTextHalf)));
}

	@Init
	public static void init() {
		ProfileViewerScreenRework.PAGE_CONSTRUCTORS.add(MiningPage::new);
	}

	@Override
	public int getSortIndex() {
		return 4;
	}

	@Override
	public ItemStack getIcon() {
		return Ico.IRON_PICKAXE;
	}

	@Override
	public String getName() {
		return "Mining";
	}

	@Override
	public List<ProfileViewerWidget.Instance> getWidgets() {
		// TODO: add player widget to the left only on this page.
		return widgets;
	}
}
