package com.github.ipecter.rtu.lightanticheat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LACPlayer {

    private boolean isPunishmentTimer = false;
    private boolean isBypass = false;
    private boolean isApiBypass = false;
    private int ping = 0;
    private boolean isGeyser = false;
    private byte counterForViolationUpdate = 0;
    private short movementViolations = 0;
    private short accurateViolations = 0;
    private short interactViolations = 0;
    private short killauraViolations = 0;
    private byte temporarilyViolations = 0;
    private boolean IsTemporarilyViolationAllowed = false;
    private short morepacketsViolations = 0;
    private byte morepacketsFinalViolations = 0;
    private long lastFireworkTime = 0;
    private float lastElytraSpeed = 0.0F;
    private byte forInaccurateViolationMethod = 0;
    private byte forCancelFirstViolationMethod = 0;
    private float lastNonGroundViolationX = 0.0F;
    private float lastNonGroundViolationY = 0.0F;
    private float lastNonGroundViolationZ = 0.0F;
    private float lastY = 1.2345F;
    private float lastLastY = 1.2345F;
    private long lastJumpTime = 0;
    private float lastFallDistance = (float) 1.2345;
    private byte sameFallDistance = 0;
    private byte fallDuration = 0;
    private double lastVerticalVelocity = (float) 1.2345;
    private byte sameVerticalVelocity = 0;
    private long joinTime = 0;
    private long lastGlidingTime = 0;
    private long lastPreventTime = 0;
    private long lastGroundTime = 0;
    private long lastNonGroundTime = 0;
    private long lastQuickBreakTime = 0;
    private long lastHitTime = 0;
    private long lastWarningAlertTime = 0;
    private long lastTeleportTime = 0;
    private long lastClickTime = 0;

    public void initTime() {
        final long time = System.currentTimeMillis();
        this.joinTime = time;
        this.lastGroundTime = time;
        this.lastNonGroundTime = time;
        this.lastHitTime = time;
    }

}
