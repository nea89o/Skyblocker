package de.hysky.skyblocker.skyblock.profileviewer.model;

import com.google.gson.annotations.SerializedName;
import de.hysky.skyblocker.skyblock.tabhud.util.Ico;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Formatting;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetherIslandPlayerData {
	@SerializedName("selected_faction")
	@Nullable
	public String selectedFaction;
	@SerializedName("mages_reputation")
	public int magesReputation;
	@SerializedName("barbarians_reputation")
	public int barbariansReputation;

	public Matriarch matriarch = new Matriarch();

	public static class Matriarch {
		@SerializedName("pearls_collected")
		public int pearlsCollected;
		@SerializedName("last_attempt")
		public long lastAttempt;
		@SerializedName("recent_refreshes")
		public List<Long> recentRefreshes = new ArrayList<>();
	}

	@SerializedName("last_minibosses_killed")
	public List<String> lastMinibossesKilled = new ArrayList<>();

	public Dojo dojo = new Dojo();

	public static class Dojo {
		@SerializedName("dojo_points_mob_kb")
		public int testOfForce;
		@SerializedName("dojo_points_wall_jump")
		public int testOfStamina;
		@SerializedName("dojo_points_archer")
		public int testOfMastery;
		@SerializedName("dojo_points_sword_swap")
		public int testOfDiscipline;
		@SerializedName("dojo_points_snake")
		public int testOfSwiftness;
		@SerializedName("dojo_points_lock_head")
		public int testOfControl;
		@SerializedName("dojo_points_fireball")
		public int testOfTenacity;

		@SerializedName("dojo_time_mob_kb")
		public int testOfForceTime;
		@SerializedName("dojo_time_wall_jump")
		public int testOfStaminaTime;
		@SerializedName("dojo_time_archer")
		public int testOfMasteryTime;
		@SerializedName("dojo_time_sword_swap")
		public int testOfDisciplineTime;
		@SerializedName("dojo_time_snake")
		public int testOfSwiftnessTime;
		@SerializedName("dojo_time_lock_head")
		public int testOfControlTime;
		@SerializedName("dojo_time_fireball")
		public int testOfTenacityTime;

		public enum DojoTest {
			FORCE("Force", Formatting.GOLD),
			STAMINA("Stamina", Formatting.LIGHT_PURPLE),
			MASTERY("Mastery", Formatting.YELLOW),
			DISCIPLINE("Discipline", Formatting.RED),
			SWIFTNESS("Swiftness", Formatting.GREEN),
			CONTROL("Control", Formatting.BLUE),
			TENACITY("Tenacity", Formatting.GOLD);

			private final String name;
			private final Formatting color;

			DojoTest(String name, Formatting color) {
				this.name = name;
				this.color = color;
			}

			public String getName() {
				return name;
			}

			public Formatting getColor() {
				return color;
			}

			public int getScore(Dojo dojo) {
				return switch (this) {
					case FORCE -> dojo.testOfForce;
					case STAMINA -> dojo.testOfStamina;
					case MASTERY -> dojo.testOfMastery;
					case DISCIPLINE -> dojo.testOfDiscipline;
					case SWIFTNESS -> dojo.testOfSwiftness;
					case CONTROL -> dojo.testOfControl;
					case TENACITY -> dojo.testOfTenacity;
				};
			}

			public int getTime(Dojo dojo) {
				return switch (this) {
					case FORCE -> dojo.testOfForceTime;
					case STAMINA -> dojo.testOfStaminaTime;
					case MASTERY -> dojo.testOfMasteryTime;
					case DISCIPLINE -> dojo.testOfDisciplineTime;
					case SWIFTNESS -> dojo.testOfSwiftnessTime;
					case CONTROL -> dojo.testOfControlTime;
					case TENACITY -> dojo.testOfTenacityTime;
				};
			}
		}

		public static int getTotalScore(Dojo dojo) {
			int total = 0;
			total += dojo.testOfForce;
			total += dojo.testOfStamina;
			total += dojo.testOfMastery;
			total += dojo.testOfDiscipline;
			total += dojo.testOfSwiftness;
			total += dojo.testOfControl;
			total += dojo.testOfTenacity;
			return total;
		}

		public enum ScoreRank {
			S(1000),
			A(800),
			B(600),
			C(400),
			D(200),
			F(0);

			private final int minScore;

			ScoreRank(int minScore) {
				this.minScore = minScore;
			}

			public int getMinScore() {
				return minScore;
			}

			public static ScoreRank fromScore(int score) {
				for (ScoreRank rank : values()) {
					if (score >= rank.minScore) {
						return rank;
					}
				}
				return F;
			}
		}

		public enum Belt {
			BLACK("Black", 7000, Formatting.BLACK),
			BROWN("Brown", 6000, Formatting.GOLD),
			BLUE("Blue", 4000, Formatting.BLUE),
			GREEN("Green", 2000, Formatting.GREEN),
			YELLOW("Yellow", 1000, Formatting.YELLOW),
			WHITE("White", 0, Formatting.WHITE);

			private final String name;
			private final int minScore;
			private final Formatting color;

			Belt(String name, int minScore, Formatting color) {
				this.name = name;
				this.minScore = minScore;
				this.color = color;
			}

			public String getName() {
				return name;
			}

			public int getMinScore() {
				return minScore;
			}

			public Formatting getColor() {
				return color;
			}

			public static Belt fromScore(int score) {
				for (Belt beltColor : values()) {
					if (score >= beltColor.minScore) {
						return beltColor;
					}
				}
				return WHITE;
			}
		}

	}

	@SerializedName("kuudra_completed_tiers")
	public KuudraCompletedTiers kuudraCompletedTiers = new KuudraCompletedTiers();

	public static class KuudraCompletedTiers {
		@SerializedName("none")
		public int basicTier;
		@SerializedName("hot")
		public int hotTier;
		@SerializedName("burning")
		public int burningTier;
		@SerializedName("fiery")
		public int fieryTier;
		@SerializedName("infernal")
		public int infernalTier;

		@SerializedName("highest_wave_none")
		public int highestBasicWave;
		@SerializedName("highest_wave_hot")
		public int highestHotWave;
		@SerializedName("highest_wave_burning")
		public int highestBurningWave;
		@SerializedName("highest_wave_fiery")
		public int highestFieryWave;
		@SerializedName("highest_wave_infernal")
		public int highestInfernalWave;

		public enum Tier {
			BASIC("Basic", Ico.KUUDRA_KEY_SKULL),
			HOT("Hot", Ico.HOT_KUUDRA_KEY_SKULL),
			BURNING("Burning", Ico.BURNING_KUUDRA_KEY_SKULL),
			FIERY("Fiery", Ico.FIERY_KUUDRA_KEY_SKULL),
			INFERNAL("Infernal", Ico.INFERNAL_KUUDRA_KEY_SKULL);

			private final String name;
			private final ItemStack itemStack;

			Tier(String name, ItemStack itemStack) {
				this.name = name;
				this.itemStack = itemStack;
			}

			public String getName() {
				return name;
			}

			public ItemStack getIcon() {return itemStack;}

			public int getHighestWave(KuudraCompletedTiers completedTiers) {
				return switch (this) {
					case BASIC -> completedTiers.highestBasicWave;
					case HOT -> completedTiers.highestHotWave;
					case BURNING -> completedTiers.highestBurningWave;
					case FIERY -> completedTiers.highestFieryWave;
					case INFERNAL -> completedTiers.highestInfernalWave;
				};
			}

			public int getCompletions(KuudraCompletedTiers completedTiers) {
				return switch (this) {
					case BASIC -> completedTiers.basicTier;
					case HOT -> completedTiers.hotTier;
					case BURNING -> completedTiers.burningTier;
					case FIERY -> completedTiers.fieryTier;
					case INFERNAL -> completedTiers.infernalTier;
				};
			}
		}

		public static int getTotalCompletions(KuudraCompletedTiers completedTiers) {
			int total = 0;
			total += completedTiers.basicTier;
			total += completedTiers.burningTier;
			total += completedTiers.fieryTier;
			total += completedTiers.hotTier;
			total += completedTiers.infernalTier;
			return total;
		}
	}

	public Abiphone abiphone = new Abiphone();

	public static class Abiphone {
		@SerializedName("last_dye_called_year")
		public int lastDyeCalledYear;
		@SerializedName("has_used_sirius_personal_phone_number_item")
		public boolean hasSiriusContactedUnlocked;
		@SerializedName("selected_sort")
		public String selectedSort;
		@SerializedName("selected_ringtone")
		public String selectedRingtone;
		@SerializedName("trio_contact_addons")
		public int contactTrio;
		@SerializedName("active_contacts")
		public List<String> activeContacts = new ArrayList<>();

		public OperatorChip operatorChip = new OperatorChip();
		public static class OperatorChip {
			@SerializedName("repaired_index")
			public int repairedIndex;
		}

		public Games games = new Games();
		public static class Games {
			@SerializedName("tic_tac_toe_draws")
			public int ticTacToeDraws;
			@SerializedName("tic_tac_toe_losses")
			public int ticTacToeLoses;
			@SerializedName("snake_best_score")
			public int snakeBestScore;
		}

		@SerializedName("contact_data")
		public Map<String, ContactData> contactData = new HashMap<>();
		public static class ContactData {
			@SerializedName("talked_to")
			public Boolean talkedTo;
			@SerializedName("completed_quest")
			public Boolean completedQuest;
			@SerializedName("last_call")
			public Long lastCall;
			@SerializedName("dnd_enabled")
			public Boolean dndEnabled;
			@SerializedName("incoming_calls_count")
			public Integer incomingCallsCount;
			@SerializedName("last_call_incoming")
			public Long lastCallIncoming;
			@SerializedName("specific")
			public SpecificContactData specific;

			public static class SpecificContactData {
				@SerializedName("unlocked_target_practice_iv")
				public Boolean unlockedTargetPracticeIv;
				@SerializedName("last_reward_year")
				public Integer lastRewardYear;
				@SerializedName("last_mistake")
				public Long lastMistake;
				@SerializedName("color_index_given")
				public Integer colorIndexGiven;
				@SerializedName("gave_saving_grace")
				public Boolean gaveSavingGrace;
			}
		}
	}
	//TODO Quests Data, Kuudra Party Finder Data
}
