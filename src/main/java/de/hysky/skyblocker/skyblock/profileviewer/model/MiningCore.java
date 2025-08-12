package de.hysky.skyblocker.skyblock.profileviewer.model;

import com.google.gson.annotations.SerializedName;
import de.hysky.skyblocker.skyblock.tabhud.util.Ico;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;

public class MiningCore {
	@SerializedName("received_free_tier")
	public boolean receivedFreeTier;
	@SerializedName("tokens")
	public int tokens;
	@SerializedName("powder_mithril")
	public int powderMithril;
	@SerializedName("powder_mithril_total")
	public int powderMithrilTotal;
	@SerializedName("powder_spent_mithril")
	public int powderSpentMithril;
	@SerializedName("retroactive_tier2_token")
	public boolean retroactiveTier2Token;
	@SerializedName("daily_ores_mined_day_mithril_ore")
	public int dailyOresMinedDayMithrilOre;
	@SerializedName("daily_ores_mined_mithril_ore")
	public int dailyOresMinedMithrilOre;
	@SerializedName("greater_mines_last_access")
	public int greaterMinesLastAccess;

	@SerializedName("crystals")
	public Crystals crystals = new Crystals();

	public static class Crystals {
		@SerializedName("jade_crystal")
		public CrystalData jade = new CrystalData();
		@SerializedName("amber_crystal")
		public CrystalData amber = new CrystalData();
		@SerializedName("amethyst_crystal")
		public CrystalData amethyst = new CrystalData();
		@SerializedName("sapphire_crystal")
		public CrystalData sapphire = new CrystalData();
		@SerializedName("topaz_crystal")
		public CrystalData topaz = new CrystalData();
		@SerializedName("jasper_crystal")
		public CrystalData jasper = new CrystalData();
		@SerializedName("ruby_crystal")
		public CrystalData ruby = new CrystalData();
		@SerializedName("opal_crystal")
		public CrystalData opal = new CrystalData();
		@SerializedName("aquamarine_crystal")
		public CrystalData aquamarine = new CrystalData();
		@SerializedName("peridot_crystal")
		public CrystalData peridot = new CrystalData();
		@SerializedName("onyx_crystal")
		public CrystalData onyx = new CrystalData();
		@SerializedName("citrine_crystal")
		public CrystalData citrine = new CrystalData();
		public static int getNucleusRuns(Crystals crystals) {
			int small = 0;
			for (Crystals.Crystal crystal : Crystals.Crystal.values()) {
				int totalPlaced = crystal.getTotalPlaced(crystals);
				if (totalPlaced > small && totalPlaced > 1) small = totalPlaced;
			}
			return small;
		}
		public static class CrystalData {
			@SerializedName("state")
			public String state;
			@SerializedName("total_placed")
			public int totalPlaced;
			@SerializedName("total_found")
			public int totalFound;
		}
		public enum Crystal {
			JADE("Jade", Ico.PERFECT_JADE_SKULL, Formatting.GREEN),
			AMBER("Amber", Ico.PERFECT_AMBER_SKULL, Formatting.GOLD),
			TOPAZ("Topaz", Ico.PERFECT_TOPAZ_SKULL, Formatting.YELLOW),
			SAPPHIRE("Sapphire", Ico.PERFECT_SAPPHIRE_SKULL, Formatting.BLUE),
			AMETHYST("Amethyst", Ico.PERFECT_AMETHYST_SKULL, Formatting.DARK_PURPLE),
			JASPER("Jasper", Ico.PERFECT_JASPER_SKULL, Formatting.LIGHT_PURPLE),
			RUBY("Ruby", Ico.PERFECT_RUBY_SKULL, Formatting.RED),
			OPAL("Opal", Ico.PERFECT_OPAL_SKULL, Formatting.WHITE),
			ONYX("Onyx", Ico.PERFECT_ONYX_SKULL, Formatting.GRAY),
			AQUAMARINE("Aquamarine", Ico.PERFECT_AQUAMARINE_SKULL, Formatting.DARK_BLUE),
			CITRINE("Citrine", Ico.PERFECT_CITRINE_SKULL, Formatting.DARK_RED),
			PERIDOT("Peridot", Ico.PERFECT_PERIDOT_SKULL, Formatting.DARK_GREEN);

			private final String name;
			private final ItemStack itemStack;
			private final Formatting color;

			Crystal(String name, ItemStack itemStack, Formatting color) {
				this.name = name;
				this.itemStack = itemStack;
				this.color = color;
			}

			public String getName() {
				return name;
			}

			public ItemStack getIcon() {
				return itemStack;
			}

			public Formatting getColor() {
				return color;
			}

			public boolean isFound(Crystals crystals) {
				return switch (this) {
					case JADE -> crystals.jade.state.equals("FOUND");
					case AMBER -> crystals.amber.state.equals("FOUND");
					case TOPAZ -> crystals.topaz.state.equals("FOUND");
					case SAPPHIRE -> crystals.sapphire.state.equals("FOUND");
					case AMETHYST -> crystals.amethyst.state.equals("FOUND");
					case JASPER -> crystals.jasper.state.equals("FOUND");
					case RUBY -> crystals.ruby.state.equals("FOUND");
					case OPAL -> crystals.opal.state.equals("FOUND");
					case ONYX -> crystals.onyx.state.equals("FOUND");
					case AQUAMARINE -> crystals.aquamarine.state.equals("FOUND");
					case CITRINE -> crystals.citrine.state.equals("FOUND");
					case PERIDOT -> crystals.peridot.state.equals("FOUND");
				};
			}

			public int getTotalPlaced(Crystals crystals) {
				return switch (this) {
					case JADE -> crystals.jade.totalPlaced;
					case AMBER -> crystals.amber.totalPlaced;
					case TOPAZ -> crystals.topaz.totalPlaced;
					case SAPPHIRE -> crystals.sapphire.totalPlaced;
					case AMETHYST -> crystals.amethyst.totalPlaced;
					case JASPER -> crystals.jasper.totalPlaced;
					case RUBY -> crystals.ruby.totalPlaced;
					case OPAL -> crystals.opal.totalPlaced;
					case ONYX -> crystals.onyx.totalPlaced;
					case AQUAMARINE -> crystals.aquamarine.totalPlaced;
					case CITRINE -> crystals.citrine.totalPlaced;
					case PERIDOT -> crystals.peridot.totalPlaced;
				};
			}
		}
	}
	@SerializedName("powder_gemstone")
	public int powderGemstone;
	@SerializedName("powder_gemstone_total")
	public int powderGemstoneTotal;
	public enum Powder {
		MITHRIL("Mithril", Ico.MITHRIL, Formatting.DARK_GREEN),
		GEMSTONE("Gemstone", Ico.AMETHYST_SHARD, Formatting.LIGHT_PURPLE),
		GLACITE("Glacite", Ico.BLUE_ICE, Formatting.BLUE);

		private final String name;
		private final ItemStack itemStack;
		private final Formatting color;

		Powder(String name, ItemStack itemStack, Formatting color) {
			this.name = name;
			this.itemStack = itemStack;
			this.color = color;
		}

		public String getName() {
			return name;
		}

		public ItemStack getIcon() {
			return itemStack;
		}

		public Formatting getColor() {
			return color;
		}

		public int getPowder(MiningCore miningCore) {
			return switch (this) {
				case MITHRIL -> miningCore.powderMithril;
				case GEMSTONE -> miningCore.powderGemstone;
				case GLACITE -> miningCore.powderGlacite;
			};
		}

		public int getPowderSpent(MiningCore miningCore) {
			return switch (this) {
				case MITHRIL -> miningCore.powderSpentMithril;
				case GEMSTONE -> miningCore.powderSpentGemstone;
				case GLACITE -> miningCore.powderSpentGlacite;
			};
		}

		public int getTotalPowder(MiningCore miningCore) {
			return getPowder(miningCore) + getPowderSpent(miningCore);
		}
	}
	@SerializedName("daily_ores_mined_day_gemstone")
	public int dailyOresMinedDayGemstone;
	@SerializedName("daily_ores_mined_gemstone")
	public int dailyOresMinedGemstone;
	@SerializedName("powder_spent_gemstone")
	public int powderSpentGemstone;
	@SerializedName("daily_ores_mined_day_glacite")
	public int dailyOresMinedDayGlacite;
	@SerializedName("daily_ores_mined_glacite")
	public int dailyOresMinedGlacite;
	@SerializedName("powder_glacite")
	public int powderGlacite;
	@SerializedName("powder_glacite_total")
	public int powderGlaciteTotal;
	@SerializedName("powder_spent_glacite")
	public int powderSpentGlacite;
	@SerializedName("daily_ores_mined")
	public int dailyOresMined;
	@SerializedName("daily_ores_mined_day")
	public int dailyOresMinedDay;
}
