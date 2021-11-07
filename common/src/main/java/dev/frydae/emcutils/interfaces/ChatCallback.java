/*
 * Copyright (c) 2021 MrFrydae
 * Copyright (c) 2021 wafflecoffee
 * Copyright (c) 2021 djlawler
 * Copyright (c) 2020 TeamMidnightDust (MidnightConfig only)
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

package dev.frydae.emcutils.interfaces;

import dev.frydae.emcutils.event.Event;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;

public interface ChatCallback {
  Event<PreSendMessage> PRE_SEND_MESSAGE = new Event<>(PreSendMessage.class,
          (listeners) -> (player, message) -> {
            for (PreSendMessage listener : listeners) {
              ActionResult result = listener.onPreSendMessage(player, message);

              if (result != ActionResult.PASS) {
                return result;
              }
            }

            return ActionResult.PASS;
          }
  );

  Event<PostSendMessage> POST_SEND_MESSAGE = new Event<>(PostSendMessage.class,
          (listeners) -> (player, message) -> {
            for (PostSendMessage listener : listeners) {
              ActionResult result = listener.onPostSendMessage(player, message);

              if (result != ActionResult.PASS) {
                return result;
              }
            }

            return ActionResult.PASS;
          }
  );

  Event<PreReceiveMessage> PRE_RECEIVE_MESSAGE = new Event<>(PreReceiveMessage.class,
          (listeners) -> (player, text) -> {
            for (PreReceiveMessage listener : listeners) {
              ActionResult result = listener.onPreReceiveMessage(player, text);

              if (result != ActionResult.PASS) {
                return result;
              }
            }

            return ActionResult.PASS;
          }
  );

  Event<PostReceiveMessage> POST_RECEIVE_MESSAGE = new Event<>(PostReceiveMessage.class,
          (listeners) -> (player, text) -> {
            for (PostReceiveMessage listener : listeners) {
              ActionResult result = listener.onPostReceiveMessage(player, text);

              if (result != ActionResult.PASS) {
                return result;
              }
            }

            return ActionResult.PASS;
          }
  );

  @FunctionalInterface
  interface PreSendMessage {
    ActionResult onPreSendMessage(ClientPlayerEntity player, String message);
  }

  @FunctionalInterface
  interface PostSendMessage {
    ActionResult onPostSendMessage(ClientPlayerEntity player, String message);
  }

  @FunctionalInterface
  interface PreReceiveMessage {
    ActionResult onPreReceiveMessage(ClientPlayerEntity player, Text message);
  }

  @FunctionalInterface
  interface PostReceiveMessage {
    ActionResult onPostReceiveMessage(ClientPlayerEntity player, Text message);
  }
}
