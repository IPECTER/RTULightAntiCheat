package com.github.ipecter.rtu.lightanticheat.api.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@RequiredArgsConstructor
public class FlagEvent  extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player player;
    private final String checkType;
    private boolean cancelled = false;

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
