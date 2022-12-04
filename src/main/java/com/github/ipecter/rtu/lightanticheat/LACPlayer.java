package com.github.ipecter.rtu.lightanticheat;

public class LACPlayer {

    public boolean isPunishmentTimer = false;
    public boolean isBypass = false;
    public boolean isApiBypass = false;
    public int ping = 0;
    public boolean isGeyser = false;
    public byte counterForViolationUpdate = 0;
    public short movementViolations = 0;
    public short accurateViolations = 0;
    public short interactViolations = 0;
    public short killauraViolations = 0;
    public byte temporarilyViolations = 0;
    public boolean IsTemporarilyViolationAllowed = false;
    public short morepacketsViolations = 0;
    public byte morepacketsFinalViolations = 0;
    public long lastFireworkTime = 0;
    public float lastElytraSpeed = 0.0F;
    public byte forInaccurateViolationMethod = 0;
    public byte forCancelFirstViolationMethod = 0;
    public float lastNonGroundViolationX = 0.0F;
    public float lastNonGroundViolationY = 0.0F;
    public float lastNonGroundViolationZ = 0.0F;
    public float lastY = 1.2345F;
    public float lastLastY = 1.2345F;
    public long lastJumpTime = 0;
    public float lastFallDistance = (float) 1.2345;
    public byte sameFallDistance = 0;
    public byte fallDuration = 0;
    public double lastVerticalVelocity = (float) 1.2345;
    public byte sameVerticalVelocity = 0;
    public long joinTime = 0;
    public long lastGlidingTime = 0;
    public long lastPreventTime = 0;
    public long lastGroundTime = 0;
    public long lastNonGroundTime = 0;
    public long lastQuickBreakTime = 0;
    public long lastHitTime = 0;
    public long lastWarningAlertTime = 0;
    public long lastTeleportTime = 0;
    public long lastClickTime = 0;

}
