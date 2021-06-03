package dev.frydae.emcutils.utils;

import com.mojang.authlib.Agent;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.authlib.yggdrasil.YggdrasilUserAuthentication;
import com.mojang.util.UUIDTypeAdapter;
import dev.frydae.emcutils.mixins.MinecraftClientAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;

import java.util.UUID;
import java.util.concurrent.CompletionException;

public class DevLogin {

  /**
   * Attempts to login and set a new session for the current Minecraft instance.
   *
   * @return completable future for the new session
   */
  public static void login()
  {
    try {
      YggdrasilAuthenticationService yas = new YggdrasilAuthenticationService(MinecraftClient.getInstance().getNetworkProxy(),
              UUID.randomUUID().toString());
      YggdrasilUserAuthentication yua = (YggdrasilUserAuthentication) yas.createUserAuthentication(Agent.MINECRAFT);

      Log.info("Logging into a new session with username");

      // Set credentials and login
      yua.setUsername(System.getProperty("devUsername"));
      yua.setPassword(System.getProperty("devPassword"));
      yua.logIn();

      // Fetch useful session data
      final String name = yua.getSelectedProfile().getName();
      final String uuid = UUIDTypeAdapter.fromUUID(yua.getSelectedProfile().getId());
      final String token = yua.getAuthenticatedToken();
      final String type = yua.getUserType().getName();

      // Logout after fetching what is needed
      yua.logOut();

      // Persist the new session to the Minecraft instance
      final Session session = new Session(name, uuid, token, type);
      ((MinecraftClientAccessor) MinecraftClient.getInstance()).setSession(session);

      Log.info("Session login successful");
    } catch (Exception e) {
      Log.exception("Session login failed: ", e);
      throw new CompletionException(e);
    }
  }
}