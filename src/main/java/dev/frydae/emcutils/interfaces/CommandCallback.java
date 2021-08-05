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

package dev.frydae.emcutils.interfaces;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.ActionResult;

import java.util.List;

public interface CommandCallback {
  Event<PreExecuteCommand> PRE_EXECUTE_COMMAND = EventFactory.createArrayBacked(PreExecuteCommand.class,
          (listeners) -> (player, command, args) -> {
            for (PreExecuteCommand listener : listeners) {
              ActionResult result = listener.onPreExecuteCommand(player, command, args);

              if (result != ActionResult.PASS) {
                return result;
              }
            }

            return ActionResult.PASS;
          }
  );

  Event<PostExecuteCommand> POST_EXECUTE_COMMAND = EventFactory.createArrayBacked(PostExecuteCommand.class,
          (listeners) -> (player, command, args) -> {
            for (PostExecuteCommand listener : listeners) {
              ActionResult result = listener.onPostExecuteCommand(player, command, args);

              if (result != ActionResult.PASS) {
                return result;
              }
            }

            return ActionResult.PASS;
          }
  );


  @FunctionalInterface
  interface PreExecuteCommand {
    ActionResult onPreExecuteCommand(ClientPlayerEntity player, String command, List<String> args);
  }

  @FunctionalInterface
  interface PostExecuteCommand {
    ActionResult onPostExecuteCommand(ClientPlayerEntity player, String command, List<String> args);
  }
}
