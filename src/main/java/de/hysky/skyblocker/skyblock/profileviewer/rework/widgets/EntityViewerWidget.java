package de.hysky.skyblocker.skyblock.profileviewer.rework.widgets;

import com.mojang.authlib.GameProfile;
import de.hysky.skyblocker.SkyblockerMod;
import de.hysky.skyblocker.skyblock.profileviewer.rework.ProfileViewerWidget;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.network.OtherClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerModelPart;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.List;

public class EntityViewerWidget implements ProfileViewerWidget {

	public static final Identifier BACKGROUND = Identifier.of(SkyblockerMod.NAMESPACE, "textures/gui/profile_viewer/entity_widget.png");
	public static final int WIDTH = 82, HEIGHT = 110, SCALE = 2;
	public static final List<UUID> Devs = List.of(
			UUID.fromString("d3cb85e2-3075-48a1-b213-a9bfb62360c1"),
			UUID.fromString("842204e6-6880-487b-ae5a-0595394f9948"),
			UUID.fromString("add71246-c46e-455c-8345-c129ea6f146c"),
			UUID.fromString("b491990d-53fd-4c5f-a61e-19d58cc7eddf"),
			UUID.fromString("8a9f1841-48e9-48ed-b14f-76a124e6c9df"),
			UUID.fromString("ed3b9805-117c-47f7-80df-d05120f1d4a3"),
			UUID.fromString("647ffd4c-f99c-4b06-a8f8-66cf1a587e57"),
			UUID.fromString("28657546-de5f-4eaf-993d-1b06ee28d129"),
			UUID.fromString("e079840c-50fa-464d-8041-c6716a5253fc"),
			UUID.fromString("80e3b708-0df7-4d91-8f84-d43f438a3e81"),
			UUID.fromString("6b1b3977-6a82-4880-9086-b67b0e872aac")
	);

	sealed interface LoadedEntity {
		record Success(GameProfile profile, OtherClientPlayerEntity entity) implements LoadedEntity {
			public String name() {
				return profile.getName();
			}
		}

		record Loading() implements LoadedEntity {}

		record Failure() implements LoadedEntity {}
	}

	CompletableFuture<LoadedEntity> loadedEntity;
	TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

	public EntityViewerWidget(UUID uuid) {
		loadedEntity = SkullBlockEntity.fetchProfileByUuid(uuid)
				.<LoadedEntity>thenApply(profile -> {
					var profileUnwrapped = profile.get();
					var entity = new OtherClientPlayerEntity(MinecraftClient.getInstance().world, profileUnwrapped) {
						@Override
						public SkinTextures getSkinTextures() {
							PlayerListEntry playerListEntry = new PlayerListEntry(profileUnwrapped, false);
							return playerListEntry.getSkinTextures();
						}

						@Override
						public boolean isPartVisible(PlayerModelPart modelPart) {
							return !(modelPart.getName().equals(PlayerModelPart.CAPE.getName()));
						}

						@Override
						public boolean isInvisibleTo(PlayerEntity player) {
							return true;
						}
					};
					entity.setCustomNameVisible(false);
					return new LoadedEntity.Success(profileUnwrapped, entity);
				}).exceptionally(ex -> {
//					// "Player not found" doesn't fit on the screen lol
//					this.playerName = "User not found";
//					this.errorMessage = "Player skin not found";
//					this.profileNotFound = true;
					return new LoadedEntity.Failure();
				});
	}

	@Override
	public void render(DrawContext drawContext, int x, int y, int mouseX, int mouseY, float deltaTicks) {
		drawContext.drawTexture(RenderPipelines.GUI_TEXTURED, BACKGROUND, x, y, 0, 0, WIDTH, HEIGHT, WIDTH, HEIGHT);
		switch (loadedEntity.getNow(new LoadedEntity.Loading())) {
			case LoadedEntity.Failure failure -> {
				// TODO: I should totally add an error message here
			}
			case LoadedEntity.Loading loading -> {
				// I should totally add a throbber here...
			}
			case LoadedEntity.Success success -> {
				var username = success.name();
				boolean isDev = Devs.contains(success.entity.getUuid());
				InventoryScreen.drawEntity(
						drawContext,
						x,
						y + 16,
						x + WIDTH,
						y + (HEIGHT * (isDev ? (SCALE / 2) : 1)),
						42 / (isDev ? SCALE : 1),
						0.0625F,
						mouseX,
						mouseY,
						success.entity
				);
				drawContext.drawCenteredTextWithShadow(
						textRenderer,
						username.length() > 15 ? username.substring(0, 15) : username,
						x + 40,
						y + (14 * (isDev ? SCALE : 1)),
						Color.WHITE.getRGB()
				);
			}
		}
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
