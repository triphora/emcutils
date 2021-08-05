/*
 * Copyright (c) 2021 MrFrydae
 * Copyright (c) 2021 wafflecoffee
 * Copyright (c) 2021 djlawler
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package dev.frydae.emcutils.mixins;

import com.google.common.collect.Lists;
import dev.frydae.emcutils.interfaces.ChatCallback;
import dev.frydae.emcutils.interfaces.CommandCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.stream.Collectors;

@Mixin(ClientPlayerEntity.class)
public class ClientEntityPlayerMixin {
  @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V"), method = "sendChatMessage", cancellable = true)
  public void onPreSendMessage(String message, CallbackInfo info) {
    if (message.startsWith("/")) {
      message = message.substring(1);
      String[] parts = message.split(" ");
      ActionResult commandResult = CommandCallback.PRE_EXECUTE_COMMAND.invoker().onPreExecuteCommand(MinecraftClient.getInstance().player, parts[0], parts.length > 1 ? Arrays.stream(parts, 1, parts.length).collect(Collectors.toList()) : Lists.newArrayList());

      if (commandResult == ActionResult.FAIL) {
        info.cancel();
      }
    } else {
      ActionResult messageResult = ChatCallback.PRE_SEND_MESSAGE.invoker().onPreSendMessage(MinecraftClient.getInstance().player, message);

      if (messageResult == ActionResult.FAIL) {
        info.cancel();
      }
    }
  }

  @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/ClientPlayNetworkHandler;sendPacket(Lnet/minecraft/network/Packet;)V", shift = At.Shift.AFTER), method = "sendChatMessage")
  public void onPostSendMessage(String message, CallbackInfo info) {
    if (message.startsWith("/")) {
      message = message.substring(1);
      String[] parts = message.split(" ");
      CommandCallback.POST_EXECUTE_COMMAND.invoker().onPostExecuteCommand(MinecraftClient.getInstance().player, parts[0], parts.length > 1 ? Arrays.stream(parts, 1, parts.length).collect(Collectors.toList()) : Lists.newArrayList());
    } else {
      ChatCallback.POST_SEND_MESSAGE.invoker().onPostSendMessage(MinecraftClient.getInstance().player, message);
    }
  }
}
