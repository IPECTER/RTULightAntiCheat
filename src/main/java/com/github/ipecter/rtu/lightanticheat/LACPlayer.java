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

    public boolean isPunishmentTimer() {
        return isPunishmentTimer;
    }

    public void setPunishmentTimer(boolean punishmentTimer) {
        isPunishmentTimer = punishmentTimer;
    }

    public boolean isBypass() {
        return isBypass;
    }

    public void setBypass(boolean bypass) {
        isBypass = bypass;
    }

    public boolean isApiBypass() {
        return isApiBypass;
    }

    public void setApiBypass(boolean apiBypass) {
        isApiBypass = apiBypass;
    }

    public int getPing() {
        return ping;
    }

    public void setPing(int ping) {
        this.ping = ping;
    }

    public boolean isGeyser() {
        return isGeyser;
    }

    public void setGeyser(boolean geyser) {
        isGeyser = geyser;
    }

    public byte getCounterForViolationUpdate() {
        return counterForViolationUpdate;
    }

    public void setCounterForViolationUpdate(byte counterForViolationUpdate) {
        this.counterForViolationUpdate = counterForViolationUpdate;
    }

    public short getMovementViolations() {
        return movementViolations;
    }

    public void setMovementViolations(short movementViolations) {
        this.movementViolations = movementViolations;
    }

    public short getAccurateViolations() {
        return accurateViolations;
    }

    public void setAccurateViolations(short accurateViolations) {
        this.accurateViolations = accurateViolations;
    }

    public short getInteractViolations() {
        return interactViolations;
    }

    public void setInteractViolations(short interactViolations) {
        this.interactViolations = interactViolations;
    }

    public short getKillauraViolations() {
        return killauraViolations;
    }

    public void setKillauraViolations(short killauraViolations) {
        this.killauraViolations = killauraViolations;
    }

    public byte getTemporarilyViolations() {
        return temporarilyViolations;
    }

    public void setTemporarilyViolations(byte temporarilyViolations) {
        this.temporarilyViolations = temporarilyViolations;
    }

    public boolean isTemporarilyViolationAllowed() {
        return IsTemporarilyViolationAllowed;
    }

    public void setTemporarilyViolationAllowed(boolean temporarilyViolationAllowed) {
        IsTemporarilyViolationAllowed = temporarilyViolationAllowed;
    }

    public short getMorepacketsViolations() {
        return morepacketsViolations;
    }

    public void setMorepacketsViolations(short morepacketsViolations) {
        this.morepacketsViolations = morepacketsViolations;
    }

    public byte getMorepacketsFinalViolations() {
        return morepacketsFinalViolations;
    }

    public void setMorepacketsFinalViolations(byte morepacketsFinalViolations) {
        this.morepacketsFinalViolations = morepacketsFinalViolations;
    }

    public long getLastFireworkTime() {
        return lastFireworkTime;
    }

    public void setLastFireworkTime(long lastFireworkTime) {
        this.lastFireworkTime = lastFireworkTime;
    }

    public float getLastElytraSpeed() {
        return lastElytraSpeed;
    }

    public void setLastElytraSpeed(float lastElytraSpeed) {
        this.lastElytraSpeed = lastElytraSpeed;
    }

    public byte getForInaccurateViolationMethod() {
        return forInaccurateViolationMethod;
    }

    public void setForInaccurateViolationMethod(byte forInaccurateViolationMethod) {
        this.forInaccurateViolationMethod = forInaccurateViolationMethod;
    }

    public byte getForCancelFirstViolationMethod() {
        return forCancelFirstViolationMethod;
    }

    public void setForCancelFirstViolationMethod(byte forCancelFirstViolationMethod) {
        this.forCancelFirstViolationMethod = forCancelFirstViolationMethod;
    }

    public float getLastNonGroundViolationX() {
        return lastNonGroundViolationX;
    }

    public void setLastNonGroundViolationX(float lastNonGroundViolationX) {
        this.lastNonGroundViolationX = lastNonGroundViolationX;
    }

    public float getLastNonGroundViolationY() {
        return lastNonGroundViolationY;
    }

    public void setLastNonGroundViolationY(float lastNonGroundViolationY) {
        this.lastNonGroundViolationY = lastNonGroundViolationY;
    }

    public float getLastNonGroundViolationZ() {
        return lastNonGroundViolationZ;
    }

    public void setLastNonGroundViolationZ(float lastNonGroundViolationZ) {
        this.lastNonGroundViolationZ = lastNonGroundViolationZ;
    }

    public float getLastY() {
        return lastY;
    }

    public void setLastY(float lastY) {
        this.lastY = lastY;
    }

    public float getLastLastY() {
        return lastLastY;
    }

    public void setLastLastY(float lastLastY) {
        this.lastLastY = lastLastY;
    }

    public long getLastJumpTime() {
        return lastJumpTime;
    }

    public void setLastJumpTime(long lastJumpTime) {
        this.lastJumpTime = lastJumpTime;
    }

    public float getLastFallDistance() {
        return lastFallDistance;
    }

    public void setLastFallDistance(float lastFallDistance) {
        this.lastFallDistance = lastFallDistance;
    }

    public byte getSameFallDistance() {
        return sameFallDistance;
    }

    public void setSameFallDistance(byte sameFallDistance) {
        this.sameFallDistance = sameFallDistance;
    }

    public byte getFallDuration() {
        return fallDuration;
    }

    public void setFallDuration(byte fallDuration) {
        this.fallDuration = fallDuration;
    }

    public double getLastVerticalVelocity() {
        return lastVerticalVelocity;
    }

    public void setLastVerticalVelocity(double lastVerticalVelocity) {
        this.lastVerticalVelocity = lastVerticalVelocity;
    }

    public byte getSameVerticalVelocity() {
        return sameVerticalVelocity;
    }

    public void setSameVerticalVelocity(byte sameVerticalVelocity) {
        this.sameVerticalVelocity = sameVerticalVelocity;
    }

    public long getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(long joinTime) {
        this.joinTime = joinTime;
    }

    public long getLastGlidingTime() {
        return lastGlidingTime;
    }

    public void setLastGlidingTime(long lastGlidingTime) {
        this.lastGlidingTime = lastGlidingTime;
    }

    public long getLastPreventTime() {
        return lastPreventTime;
    }

    public void setLastPreventTime(long lastPreventTime) {
        this.lastPreventTime = lastPreventTime;
    }

    public long getLastGroundTime() {
        return lastGroundTime;
    }

    public void setLastGroundTime(long lastGroundTime) {
        this.lastGroundTime = lastGroundTime;
    }

    public long getLastNonGroundTime() {
        return lastNonGroundTime;
    }

    public void setLastNonGroundTime(long lastNonGroundTime) {
        this.lastNonGroundTime = lastNonGroundTime;
    }

    public long getLastQuickBreakTime() {
        return lastQuickBreakTime;
    }

    public void setLastQuickBreakTime(long lastQuickBreakTime) {
        this.lastQuickBreakTime = lastQuickBreakTime;
    }

    public long getLastHitTime() {
        return lastHitTime;
    }

    public void setLastHitTime(long lastHitTime) {
        this.lastHitTime = lastHitTime;
    }

    public long getLastWarningAlertTime() {
        return lastWarningAlertTime;
    }

    public void setLastWarningAlertTime(long lastWarningAlertTime) {
        this.lastWarningAlertTime = lastWarningAlertTime;
    }

    public long getLastTeleportTime() {
        return lastTeleportTime;
    }

    public void setLastTeleportTime(long lastTeleportTime) {
        this.lastTeleportTime = lastTeleportTime;
    }

    public long getLastClickTime() {
        return lastClickTime;
    }

    public void setLastClickTime(long lastClickTime) {
        this.lastClickTime = lastClickTime;
    }
}
