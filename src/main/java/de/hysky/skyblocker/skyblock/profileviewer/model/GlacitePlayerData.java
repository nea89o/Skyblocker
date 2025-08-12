package de.hysky.skyblocker.skyblock.profileviewer.model;

import com.google.gson.annotations.SerializedName;
import de.hysky.skyblocker.skyblock.tabhud.util.Ico;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GlacitePlayerData {
	@SerializedName("fossil_dust")
	public double fossilDust;
	@SerializedName("mineshafts_entered")
	public int mineshaftsEntered;
	@SerializedName("corpses_looted")
	public Map<String, Integer> corpsesLooted = new HashMap<>();
	public static int getTotalCorpsesLooted(GlacitePlayerData glacitePlayerData) {
		int total = 0;
		for (int count : glacitePlayerData.corpsesLooted.values()) {
			total += count;
		}
		return total;
	}
	@SerializedName("fossils_donated")
	public List<String> fossilsDonated = new ArrayList<>();
	public enum Fossil {
		CLAW("Claw", Ico.CLAW_FOSSIL_SKULL),
		SPINE("Spine", Ico.SPINE_FOSSIL_SKULL),
		CLUBBED("Clubbed", Ico.CLUBBED_FOSSIL_SKULL),
		UGLY("Ugly", Ico.UGLY_FOSSIL_SKULL),
		HELIX("Helix", Ico.HELIX_FOSSIL_SKULL),
		FOOTPRINT("Footprint", Ico.FOOTPRINT_FOSSIL_SKULL),
		WEBBED("Webbed", Ico.WEBBED_FOSSIL_SKULL),
		TUSK("Tusk", Ico.TUSK_FOSSIL_SKULL);

		private final String name;
		private final ItemStack itemStack;

		Fossil(String name, ItemStack itemStack) {
			this.name = name;
			this.itemStack = itemStack;
		}

		public String getName() {
			return name;
		}

		public ItemStack getIcon() {
			return itemStack;
		}

		public boolean isDonated(GlacitePlayerData glacitePlayerData) {
			return switch (this) {
				case CLAW, SPINE, CLUBBED, UGLY, HELIX, FOOTPRINT, WEBBED, TUSK -> glacitePlayerData.fossilsDonated.contains(this.getName().toUpperCase());
			};
		}
	}
}
