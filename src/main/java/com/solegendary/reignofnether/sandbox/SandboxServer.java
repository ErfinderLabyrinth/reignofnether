package com.solegendary.reignofnether.sandbox;

import com.solegendary.reignofnether.player.PlayerServerEvents;
import com.solegendary.reignofnether.player.RTSPlayer;
import com.solegendary.reignofnether.registrars.EntityRegistrar;
import com.solegendary.reignofnether.unit.UnitServerEvents;
import com.solegendary.reignofnether.unit.interfaces.Unit;
import com.solegendary.reignofnether.unit.units.monsters.*;
import com.solegendary.reignofnether.unit.units.neutral.EndermanProd;
import com.solegendary.reignofnether.unit.units.piglins.*;
import com.solegendary.reignofnether.unit.units.villagers.*;
import com.solegendary.reignofnether.util.Faction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;

import java.util.List;

import static com.solegendary.reignofnether.player.PlayerServerEvents.serverLevel;

public class SandboxServer {

    public static boolean isSandboxPlayer(String playerName) {
        for (RTSPlayer rtsPlayer : PlayerServerEvents.rtsPlayers)
            if (rtsPlayer.faction == Faction.NONE)
                return true;
        return false;
    }

    public static void spawnUnit(SandboxAction sandboxAction, String playerName, String unitName, BlockPos blockPos) {
        if (serverLevel == null)
            return;

        EntityType<? extends Mob> entityType = switch(unitName) {
            case CreeperProd.itemName -> EntityRegistrar.CREEPER_UNIT.get();
            case SkeletonProd.itemName -> EntityRegistrar.SKELETON_UNIT.get();
            case ZombieProd.itemName -> EntityRegistrar.ZOMBIE_UNIT.get();
            case StrayProd.itemName -> EntityRegistrar.STRAY_UNIT.get();
            case HuskProd.itemName -> EntityRegistrar.HUSK_UNIT.get();
            case DrownedProd.itemName -> EntityRegistrar.DROWNED_UNIT.get();
            case SpiderProd.itemName -> EntityRegistrar.SPIDER_UNIT.get();
            case PoisonSpiderProd.itemName -> EntityRegistrar.POISON_SPIDER_UNIT.get();
            case VillagerProd.itemName -> EntityRegistrar.VILLAGER_UNIT.get();
            case ZombieVillagerProd.itemName -> EntityRegistrar.ZOMBIE_VILLAGER_UNIT.get();
            case VindicatorProd.itemName -> EntityRegistrar.VINDICATOR_UNIT.get();
            case PillagerProd.itemName -> EntityRegistrar.PILLAGER_UNIT.get();
            case IronGolemProd.itemName -> EntityRegistrar.IRON_GOLEM_UNIT.get();
            case WitchProd.itemName -> EntityRegistrar.WITCH_UNIT.get();
            case EvokerProd.itemName -> EntityRegistrar.EVOKER_UNIT.get();
            case SlimeProd.itemName -> EntityRegistrar.SLIME_UNIT.get();
            case WardenProd.itemName -> EntityRegistrar.WARDEN_UNIT.get();
            case RavagerProd.itemName -> EntityRegistrar.RAVAGER_UNIT.get();
            case GruntProd.itemName -> EntityRegistrar.GRUNT_UNIT.get();
            case BruteProd.itemName -> EntityRegistrar.BRUTE_UNIT.get();
            case HeadhunterProd.itemName -> EntityRegistrar.HEADHUNTER_UNIT.get();
            case HoglinProd.itemName -> EntityRegistrar.HOGLIN_UNIT.get();
            case BlazeProd.itemName -> EntityRegistrar.BLAZE_UNIT.get();
            case WitherSkeletonProd.itemName -> EntityRegistrar.WITHER_SKELETON_UNIT.get();
            case MagmaCubeProd.itemName -> EntityRegistrar.MAGMA_CUBE_UNIT.get();
            case GhastProd.itemName -> EntityRegistrar.GHAST_UNIT.get();
            case NecromancerProd.itemName -> EntityRegistrar.NECROMANCER_UNIT.get();
            case PiglinMerchantProd.itemName -> EntityRegistrar.PIGLIN_MERCHANT_UNIT.get();
            case RoyalGuardProd.itemName -> EntityRegistrar.ROYAL_GUARD_UNIT.get();
            case EndermanProd.itemName -> EntityRegistrar.ENDERMAN_UNIT.get();
            case ZombiePiglinProd.itemName -> EntityRegistrar.ZOMBIE_PIGLIN_UNIT.get();
            case ZoglinProd.itemName -> EntityRegistrar.ZOGLIN_UNIT.get();
            default -> null;
        };

        if (entityType != null)
            UnitServerEvents.spawnMob(entityType, serverLevel, blockPos, playerName);
    }
}
