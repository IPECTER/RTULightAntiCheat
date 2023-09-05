package com.github.ipecter.rtu.lightanticheat.listeners.movement;

import com.github.ipecter.rtu.lightanticheat.LACPlayer;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.EnumSet;
import java.util.Objects;

public class Movement implements Listener {

    private static final BlockFace[] BLOCK_FACES = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST};
    private static final BlockFace[] FLUIDWALK_BLOCK_FACES = {BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST,
            BlockFace.NORTH_EAST, BlockFace.SOUTH_EAST, BlockFace.SOUTH_WEST, BlockFace.NORTH_WEST};

    private static void cancelFlightMovement(Player player, Location location, CheckTypes checkType, LACPlayer lacPlayer, Block block) {
        if (isBlockAround(block, EnumSet.of(Material.WATER, Material.LAVA)))
            return;

        // Does the player move
        if (lacPlayer.movementViolations == 0) {
            lacPlayer.lastNonGroundViolationX = (float) location.getX();
            lacPlayer.lastNonGroundViolationY = (float) location.getY();
            lacPlayer.lastNonGroundViolationZ = (float) location.getZ();
        } else if (location.getX() - lacPlayer.lastNonGroundViolationX < 0.7D &&
                location.getZ() - lacPlayer.lastNonGroundViolationZ < 0.7D &&
                location.getZ() - lacPlayer.lastNonGroundViolationZ < 4.0D) {
            return;
        }

        // Check if the player is on entity and teleport him
        if (lacPlayer.movementViolations < 5) {
            Violations.movementViolation(player, checkType, lacPlayer);
        } else {
            if (!player.getNearbyEntities(1.0D, 10.0D, 1.0D).isEmpty())
                return;

            Violations.movementViolation(player, checkType, lacPlayer);

            if (lacPlayer.movementViolations < 20 || !Config.cancelHacking)
                return;

            Block highestBlock = Objects.requireNonNull(location.getWorld()).getHighestBlockAt(location);
            if (highestBlock == null)
                return;
            Location highestLocation = highestBlock.getLocation().add(0, 1, 0);

            if (highestBlock.isEmpty()) {
                player.teleport(location.subtract(0, 1, 0.));
                location.add(0, 1, 0);
            } else if (location.getY() > highestLocation.getY()) {
                player.teleport(highestLocation);
                Violations.accurateViolation(player, checkType, lacPlayer);
            } else {
                player.teleport(location);
            }
        }
    }

    // Check if the player is on entity and cancel the movement
    private static void cancelMovement(Player player, Location location, CheckTypes checkType, LACPlayer lacPlayer, Block block) {
        if (isBlockAround(block, EnumSet.of(Material.WATER, Material.LAVA)))
            return;

        if (lacPlayer.movementViolations < 5) {
            Violations.movementViolation(player, checkType, lacPlayer);
        } else {
            if (!player.getNearbyEntities(1.0D, 10.0D, 1.0D).isEmpty())
                return;
            Violations.movementViolation(player, checkType, lacPlayer);
            if (lacPlayer.movementViolations < 20 || !Config.cancelHacking)
                return;
            player.teleport(location);
        }
    }

    public static void speedViolation(Player player, Location fromLocation, Location toLocation, CheckTypes checkType, LACPlayer lacPlayer, Block block, boolean isNoBlockAbove) {
        if (Math.abs(fromLocation.getX() - toLocation.getX()) > 0.03D && Math.abs(fromLocation.getZ() - toLocation.getZ()) > 0.03D)
            if (isNoBlockAbove || inaccurateViolation(lacPlayer))
                if (cancelFirstViolations(lacPlayer, (byte) 1))
                    cancelMovement(player, fromLocation, checkType, lacPlayer, block);
    }

    // Cancel first flags
    public static boolean cancelFirstViolations(LACPlayer lacPlayer, byte add) {
        if (lacPlayer.forCancelFirstViolationMethod >= 6)
            return true;
        if (!lacPlayer.isGeyser)
            lacPlayer.forCancelFirstViolationMethod += add;
        return false;
    }

    // Cancel 7 of 8 flags
    private static boolean inaccurateViolation(LACPlayer lacPlayer) {
        if (lacPlayer.forInaccurateViolationMethod < 8) {
            lacPlayer.forInaccurateViolationMethod++;
            return false;
        } else {
            lacPlayer.forInaccurateViolationMethod = 0;
            return true;
        }
    }

    private static boolean isBlockAround(Block block, EnumSet<Material> materials) {
        for (BlockFace blockFace : Movement.BLOCK_FACES) {
            if (materials.contains(block.getRelative(blockFace).getType()))
                return true;
        }
        return false;
    }

    private static boolean isFluidwalkAllowed(Block block) {
        for (BlockFace blockFace : Movement.FLUIDWALK_BLOCK_FACES) {
            if (!block.getRelative(blockFace).isLiquid())
                return false;
        }
        return true;
    }

    private static boolean isBlockY(double y) {
        return Math.round(y % 1.0D * 100000.0D % 25.0D) == 0;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {

        Player player = event.getPlayer();
        LACPlayer lacPlayer = LACPlayer.get(player);
        long time = System.currentTimeMillis();

        if (lacPlayer == null || lacPlayer.isBypass || time - lacPlayer.joinTime <= Config.disablerTimeOnJoin ||
                Config.punishmentCommand == null || lacPlayer.isApiBypass ||
                time - lacPlayer.lastTeleportTime <= 1000)
            return;

        if (Config.morePackets)
            Violations.morepacketsViolation(player, lacPlayer);

        if (player.isFlying()) {
            lacPlayer.lastGroundTime = time;
            lacPlayer.lastNonGroundTime = time;
            return;
        }

        Location toLocation = event.getTo();
        if (toLocation == null || !toLocation.getChunk().isLoaded())
            return;

        PlayerInventory playerInventory = player.getInventory();
        boolean isRiptiding = player.isRiptiding() &&
                (playerInventory.getItemInMainHand().getType() == Material.TRIDENT ||
                        playerInventory.getItemInOffHand().getType() == Material.TRIDENT);
        Block block1 = toLocation.getBlock();
        double y = toLocation.getY();
        if (y % 1.0D > 0.85D)
            block1 = block1.getRelative(BlockFace.UP);
        Block block = block1.getRelative(BlockFace.DOWN);

        if (time - lacPlayer.lastPreventTime <= Config.disablerTimeOnLegalFlight) {
            return;
        }
        if (isRiptiding) {
            lacPlayer.lastPreventTime = time;
            return;
        }

        Location fromLocation = event.getFrom();
        if (fromLocation == null)
            return;
        Block fromBlock = fromLocation.getBlock().getRelative(BlockFace.DOWN);
        double fromY = fromLocation.getY();
        double speed = fromLocation.distance(toLocation);
        Entity vehicle = player.getVehicle();

        // BoatFly
        if (vehicle != null) {
            if (vehicle.getType() == EntityType.BOAT)
                if (Config.boatFly && !vehicle.isOnGround() && vehicle.getFallDistance() < 35.0F && speed > 0.2D &&
                        !block1.isLiquid() && !fromBlock.getRelative(BlockFace.UP).isLiquid())
                    Violations.temporarilyViolation(player, CheckTypes.BOAT_FLY_A_0, lacPlayer, fromY - y < 0.0D);
            return;
        }

        boolean elytra = playerInventory.getChestplate() != null && playerInventory.getChestplate().getType() == Material.ELYTRA;
        boolean isGliding = player.isGliding() && elytra;

        // ElytraFly
        if (Config.elytraFly && isGliding && speed > 0.3D) {

            // ElytraFlyA (the player speed doesn't change)
            float roundSpeed = (float) (Math.round(speed * 10000.0D) / 10000.0D);
            if (playerInventory.getItemInMainHand().getType() == Material.FIREWORK_ROCKET ||
                    playerInventory.getItemInOffHand().getType() == Material.FIREWORK_ROCKET)
                lacPlayer.lastFireworkTime = time;
            if (Config.elytraFlyA) {
                if (roundSpeed == lacPlayer.lastElytraSpeed && time - lacPlayer.lastFireworkTime > 1000)
                    Violations.temporarilyViolation(player, CheckTypes.ELYTRA_FLY_A_0, lacPlayer,
                            Math.abs(fromLocation.getPitch() - toLocation.getPitch()) > 2.0F);
            }

            // ElytraFlyB (the acceleration is not possible)
            Block targetBlock2 = player.getTargetBlockExact(2);
            Block block2 = toLocation.subtract(0, 2, 0).getBlock();
            toLocation.add(0, 2, 0);
            if (block2.isEmpty() && block2.getRelative(BlockFace.DOWN).isEmpty() &&
                    (targetBlock2 == null || targetBlock2.isEmpty()) && time - lacPlayer.lastGroundTime > 2000) {
                double verticalSpeedExactly = fromY - y;
                if (Config.elytraFlyB && time - lacPlayer.lastFireworkTime > 5000) {
                    if (verticalSpeedExactly < -0.05D && roundSpeed > lacPlayer.lastElytraSpeed)
                        Movement.cancelFlightMovement(player, toLocation, CheckTypes.ELYTRA_FLY_B_0, lacPlayer, block1);
                }
            }

            lacPlayer.lastElytraSpeed = roundSpeed;
            lacPlayer.lastGlidingTime = time;
        }
        if (isGliding || time - lacPlayer.lastGlidingTime <= Config.disablerTimeOnLegalFlight) {
            return;
        }

        boolean isOnGround = ((Entity) player).isOnGround();
        Vector velocities = player.getVelocity();
        double velocity = velocities.getY();
        double verticalSpeedExactly = fromY - y;
        double verticalSpeed = Math.abs(verticalSpeedExactly);
        float fallDistance = player.getFallDistance();
        boolean isOnBlockY = isBlockY(y);
        boolean isOnBlockFromY = isBlockY(fromY);
        boolean isGround = !block.isPassable() || block.isLiquid() || isOnGround && fallDistance == 0.0F && verticalSpeed == 0.0D && Math.abs(velocity) < 0.1D && isOnBlockY && isOnBlockFromY;
        long noGroundTimeInRow = time - lacPlayer.lastGroundTime;
        PotionEffect speedEffect = player.getPotionEffect(PotionEffectType.SPEED);
        PotionEffect jumpEffect = player.getPotionEffect(PotionEffectType.JUMP);
        boolean noFlightEffect = player.getPotionEffect(PotionEffectType.LEVITATION) == null && player.getPotionEffect(PotionEffectType.SLOW_FALLING) == null;
        String block2 = block1.getType().name();
        String block3 = block1.getRelative(BlockFace.UP).getType().name();
        boolean isGroundCheck = y == lacPlayer.lastY && y == lacPlayer.lastLastY ||
                verticalSpeed < 0.05D && (isBlockY(lacPlayer.lastY) || isBlockY(lacPlayer.lastLastY)) ||
                (isOnGround || isOnBlockY || isOnBlockFromY) && (y == lacPlayer.lastY || y == lacPlayer.lastLastY) ||
                isOnGround && isOnBlockY && isOnBlockFromY && verticalSpeed < 0.05D;

        // Speed
        boolean isNoBlockAbove = block1.getRelative(0, 2, 0).isPassable();
        if (Config.speed && isGroundCheck && (speedEffect == null || speedEffect.getAmplifier() <= 1) && speed * 1.2D > 0.280616D) {
            double complexSpeed = player.isSneaking() ? speed * 1.2D : speed;
            complexSpeed *= 0.85D;
            if (complexSpeed > 0.280616) {
                boolean notJump = time - lacPlayer.lastJumpTime > 1200;
                if (notJump && isNoBlockAbove) {
                    if (!isBlockAround(block, EnumSet.of(Material.SOUL_SAND, Material.SOUL_SOIL))) {
                        if (speedEffect == null)
                            speedViolation(player, fromLocation, toLocation, CheckTypes.SPEED_A_1, lacPlayer, block1, true);
                        else if (complexSpeed > 0.459405D)
                            speedViolation(player, fromLocation, toLocation, CheckTypes.SPEED_A_1, lacPlayer, block1, true);
                    } else {
                        if (speedEffect == null && complexSpeed > 0.453196)
                            speedViolation(player, fromLocation, toLocation, CheckTypes.SPEED_A_1, lacPlayer, block1, true);
                    }
                } else {
                    if (!isBlockAround(block, EnumSet.of(Material.SOUL_SAND, Material.SOUL_SOIL))) {
                        if (speedEffect == null && complexSpeed > 0.386727)
                            speedViolation(player, fromLocation, toLocation, CheckTypes.SPEED_A_2, lacPlayer, block1, isNoBlockAbove);
                        else if (complexSpeed > 0.459405D)
                            speedViolation(player, fromLocation, toLocation, CheckTypes.SPEED_A_2, lacPlayer, block1, isNoBlockAbove);
                    } else {
                        if (speedEffect == null && complexSpeed > 0.498356)
                            speedViolation(player, fromLocation, toLocation, CheckTypes.SPEED_A_2, lacPlayer, block1, isNoBlockAbove);
                    }
                }
            }
        }

        // FastClimb, NoWeb
        if (!block2.equals("AIR") && !block2.contains("GRASS") && !block2.contains("_BUTTON") && !block2.equals("SNOW") ||
                !block3.equals("AIR") && !block3.contains("GRASS") && !block3.contains("_BUTTON") && !block3.equals("SNOW")) {

            // FastClimb
            Block fromBlockUp = fromBlock.getRelative(BlockFace.UP);
            if (Config.fastClimb && verticalSpeed > 0.16D && block1.getType() == Material.LADDER && fromBlockUp.getType() == Material.LADDER && block.getRelative(0, 2, 0).getType() == Material.LADDER && block
                    .getType() == Material.LADDER && !player.isSprinting() && velocity < -0.15D && fallDistance == 0.0F && !lacPlayer.isGeyser)
                Movement.cancelMovement(player, fromLocation, CheckTypes.FAST_CLIMB_A_0, lacPlayer, block1);

            // NoWeb
            if (Config.noWeb && noFlightEffect && speedEffect == null && jumpEffect == null) {
                boolean isCobweb = block1.getType() == Material.COBWEB && fromBlockUp.getType() == Material.COBWEB;
                boolean isSweetBerryBush = block.getType() == Material.SWEET_BERRY_BUSH && fromBlockUp.getType() == Material.SWEET_BERRY_BUSH;
                if (Config.noWebA && (isCobweb && verticalSpeedExactly < -0.3D || isSweetBerryBush && verticalSpeedExactly < -0.4D))
                    Violations.movementViolation(player, CheckTypes.NO_WEB_A_0, lacPlayer);
                if (isGroundCheck && isOnBlockFromY && isOnBlockY) {
                    if (Config.noWebB && isCobweb && speed > 0.1D)
                        Violations.movementViolation(player, CheckTypes.NO_WEB_B_0, lacPlayer);
                    if (Config.noWebB && isSweetBerryBush && speed > 0.17D)
                        Violations.movementViolation(player, CheckTypes.NO_WEB_B_0, lacPlayer);
                }
            }

            return;
        }

        if (!noFlightEffect || (speedEffect != null && speedEffect.getAmplifier() >= 2) || (jumpEffect != null && jumpEffect.getAmplifier() >= 2))
            return;

        // FluidWalk
        if (Config.fluidWalk && block.isLiquid() && fromBlock.isLiquid()) {
            // FluidWalkA
            if (Config.fluidWalkA) {
                if (isOnGround && (verticalSpeed < 0.04D || fromY % 1.0D == 0.0D || y % 1.0D == 0.0D) && velocity < -0.5D && player
                        .getFallDistance() == 0.0F && speed > 0.15D && speed - verticalSpeed > verticalSpeed)
                    if (cancelFirstViolations(lacPlayer, (byte) 2))
                        Movement.cancelMovement(player, fromLocation, CheckTypes.FLUID_WALK_A_1, lacPlayer, block1);
                if (isOnGround && verticalSpeed == 0.0F && !isOnBlockY && (player.isSprinting() || player.isSneaking() || speed > 0.2D))
                    if (cancelFirstViolations(lacPlayer, (byte) 2))
                        Movement.cancelMovement(player, fromLocation, CheckTypes.FLUID_WALK_A_2, lacPlayer, block1);
            }
            // FluidWalkB
            if (Config.fluidWalkB && (y % 1.0D < 0.06D || fromY % 1.0D < 0.06D) &&
                    (player.getGameMode() == GameMode.SURVIVAL || player.getGameMode() == GameMode.ADVENTURE) &&
                    isFluidwalkAllowed(block)) {
                if (verticalSpeedExactly == 0.0 || verticalSpeedExactly > 0.3000 && verticalSpeedExactly < 0.30001 || verticalSpeedExactly == 0.5)
                    if (speed > 0.1D)
                        if (cancelFirstViolations(lacPlayer, (byte) 1))
                            Movement.cancelMovement(player, fromLocation, CheckTypes.FLUID_WALK_B_0, lacPlayer, block1);
            }
        }

        boolean glidingIsPossible = elytra && (!block.getRelative(BlockFace.DOWN).isPassable() || !block.getRelative(0, -2, 0).isPassable()) && speed - verticalSpeed > verticalSpeed;
        int ping = lacPlayer.ping;
        boolean isFall = false;

        // Non ground checks
        if (!isGround && !glidingIsPossible) {
            Block targetBlock1 = player.getTargetBlockExact(1);
            boolean lookAtAir = targetBlock1 == null || targetBlock1.isEmpty();
            if ((noGroundTimeInRow > 1250 || Config.highSpeedMode && noGroundTimeInRow > 500) && speedEffect == null && jumpEffect == null ||
                    (noGroundTimeInRow > 1875 || Config.highSpeedMode && noGroundTimeInRow > 700)) {
                if (Config.flight && (lookAtAir || fallDistance != 0.0F)) {
                    // FlightA
                    if (Config.flightA && (fallDistance < 25.0F && velocity < -2.2D && speed > 0.2D || fallDistance > 25.0F && fallDistance < 50.0F && velocity < -2.7D && speed > 0.25D))
                        Movement.cancelFlightMovement(player, toLocation, CheckTypes.FLIGHT_A_1, lacPlayer, block1);
                    // FlightB
                    if (Config.flightB && (velocity < -1.0D || fallDistance > 10.0F || speed > 1.0D) && speed - verticalSpeed > verticalSpeed)
                        Movement.cancelFlightMovement(player, toLocation, CheckTypes.FLIGHT_B_0, lacPlayer, block1);
                    // FlightA
                    if (Config.flightA && velocity < -0.1284000015258789D && verticalSpeedExactly < 0.05D && (noGroundTimeInRow > 4000 || Config.highSpeedMode && noGroundTimeInRow > 1500L))
                        Movement.cancelFlightMovement(player, toLocation, CheckTypes.FLIGHT_A_2, lacPlayer, block1);
                }
                // IrregularMovementA
                if (Config.irregularMovementA && (lookAtAir || fallDistance != 0.0F) &&
                        fallDistance < 1.0F && (verticalSpeedExactly > 1.0D || velocity < -1.5D))
                    Movement.cancelMovement(player, toLocation, CheckTypes.IRREGULAR_MOVEMENT_A_1, lacPlayer, block1);
                // GroundSpoofA
                if (Config.groundSpoof && isOnGround && (noGroundTimeInRow > 2500 || Config.highSpeedMode && noGroundTimeInRow > 1000) && (player
                        .isSprinting() || player.isSneaking() || speed > 0.4D) && (lookAtAir || fallDistance != 0.0F) && fromY % 0.5D != 0.0D && y % 0.5D != 0.0D)
                    Movement.cancelFlightMovement(player, toLocation, CheckTypes.GROUND_SPOOF_A_0, lacPlayer, block1);
            } else {
                // FlightC
                if (Config.flightC && velocity < -0.2784000015258789D && verticalSpeedExactly < -0.5D && lookAtAir)
                    Movement.cancelFlightMovement(player, toLocation, CheckTypes.FLIGHT_C_0, lacPlayer, block1);
                // IrregularMovementA
                if (Config.irregularMovementA && fallDistance < 0.3F && (verticalSpeedExactly > 2.0D || velocity < -2.5D) && lookAtAir)
                    Movement.cancelFlightMovement(player, toLocation, CheckTypes.IRREGULAR_MOVEMENT_A_2, lacPlayer, block1);
            }

            // FlightD, IrregularMovementC
            if (noGroundTimeInRow > 250 && velocities.getX() == 0.0 && velocities.getZ() == 0.0) {
                if (Config.flightD) {
                    double antiVerticalSpeedExactly = y - fromY;
                    double vectorSpeed = event.getTo().toVector().distance(event.getFrom().toVector());
                    if ((!player.isSprinting() || antiVerticalSpeedExactly <= 0) && fallDistance == 0.0F && vectorSpeed > 0.8)
                        if (cancelFirstViolations(lacPlayer, (byte) 1))
                            Movement.cancelFlightMovement(player, toLocation, CheckTypes.FLIGHT_D_0, lacPlayer, block1);
                }
                if (fromY % 0.5D != 0.0D && y % 0.5D != 0.0D && fromBlock.isPassable()) {
                    // IrregularMovementC
                    if (Config.irregularMovementC && verticalSpeedExactly > 0.05 && lookAtAir) {
                        if (fallDistance == lacPlayer.lastFallDistance) {
                            if (lacPlayer.sameFallDistance <= 3) {
                                lacPlayer.sameFallDistance++;
                            } else {
                                if (inaccurateViolation(lacPlayer))
                                    if (cancelFirstViolations(lacPlayer, (byte) 2))
                                        if (!isBlockAround(block1, EnumSet.of(Material.HONEY_BLOCK)))
                                            Movement.cancelMovement(player, toLocation, CheckTypes.IRREGULAR_MOVEMENT_C_1, lacPlayer, block1);
                                        else
                                            lacPlayer.forInaccurateViolationMethod = 0;
                            }
                        } else {
                            lacPlayer.sameFallDistance = 0;
                        }
                    }
                    // IrregularMovementC
                    if (Config.irregularMovementC && verticalSpeedExactly > 0.1 && fallDistance > lacPlayer.lastFallDistance) {
                        isFall = true;
                        if (lacPlayer.fallDuration < 120)
                            lacPlayer.fallDuration++;
                        byte fallInRow = lacPlayer.fallDuration;
                        if (fallInRow > 5 && fallDistance < 2.7 * 0.6 ||
                                fallInRow > 10 && fallDistance < 6.6 * 0.6 ||
                                fallInRow > 20 && fallDistance < 18.8 * 0.6 ||
                                fallInRow > 30 && fallDistance < 35.9 * 0.6 ||
                                fallInRow > 40 && fallDistance < 57.5 * 0.6)
                            if (cancelFirstViolations(lacPlayer, (byte) 2)) {
                                Block block4 = block.getRelative(BlockFace.DOWN);
                                if (block.getType() != Material.SLIME_BLOCK && block4.getType() != Material.SLIME_BLOCK &&
                                        block4.getRelative(BlockFace.DOWN).getType() != Material.SLIME_BLOCK)
                                    Movement.cancelMovement(player, toLocation, CheckTypes.IRREGULAR_MOVEMENT_C_2, lacPlayer, block1);
                                else
                                    lacPlayer.forInaccurateViolationMethod = 0;
                            }
                    }
                    // IrregularMovementC
                    if (Config.irregularMovementC) {
                        if (velocity >= lacPlayer.lastVerticalVelocity) {
                            if (lacPlayer.sameVerticalVelocity <= 5) {
                                lacPlayer.sameVerticalVelocity++;
                            } else {
                                if (inaccurateViolation(lacPlayer))
                                    if (cancelFirstViolations(lacPlayer, (byte) 2))
                                        if (!isBlockAround(block1, EnumSet.of(Material.HONEY_BLOCK)))
                                            Movement.cancelMovement(player, toLocation, CheckTypes.IRREGULAR_MOVEMENT_C_3, lacPlayer, block1);
                                        else
                                            lacPlayer.forInaccurateViolationMethod = 0;
                            }
                        } else {
                            lacPlayer.sameVerticalVelocity = 0;
                        }
                    }
                }
            }
            // IrregularMovementB
            if (Config.irregularMovementB && ping < 250 &&
                    verticalSpeedExactly < -2.0D && noGroundTimeInRow < 1000 && velocity > 0.05D && velocity < 0.5D && block.getType() != Material.SLIME_BLOCK)
                Violations.interactViolation(player, CheckTypes.IRREGULAR_PLACEMENT_B_0, lacPlayer);
        }
        lacPlayer.lastFallDistance = fallDistance;
        lacPlayer.lastVerticalVelocity = velocity;


        if (isGround)
            lacPlayer.lastGroundTime = time;
        if (!isFall)
            lacPlayer.fallDuration = 0;
        if (!isOnGround && (!isOnBlockFromY && !isOnBlockY || !isNoBlockAbove))
            lacPlayer.lastJumpTime = time;
        lacPlayer.lastLastY = lacPlayer.lastY;
        lacPlayer.lastY = (float) y;

    }

}
