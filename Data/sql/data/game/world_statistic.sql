DROP TABLE IF EXISTS `world_statistic`;
CREATE TABLE `world_statistic` (
  `charId` INT(30) NOT NULL DEFAULT '0',
  `expAdded` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `adenaAdded` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `timePlayed` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `timeInBattle` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `timeInParty` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `timeInFullParty` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `weaponEnchantMaxD` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `weaponEnchantMaxC` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `weaponEnchantMaxB` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `weaponEnchantMaxA` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `weaponEnchantMaxS` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `weaponEnchantMaxS80` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `weaponEnchantMaxR` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `weaponEnchantMaxR95` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `weaponEnchantMaxR99` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `armorEnchantMaxD` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `armorEnchantMaxC` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `armorEnchantMaxB` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `armorEnchantMaxA` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `armorEnchantMaxS` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `armorEnchantMaxS80` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `armorEnchantMaxR` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `armorEnchantMaxR95` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `armorEnchantMaxR99` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `weaponEnchantTryD` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `weaponEnchantTryC` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `weaponEnchantTryB` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `weaponEnchantTryA` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `weaponEnchantTryS` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `weaponEnchantTryS80` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `weaponEnchantTryR` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `weaponEnchantTryR95` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `weaponEnchantTryR99` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `armorEnchantTryD` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `armorEnchantTryC` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `armorEnchantTryB` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `armorEnchantTryA` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `armorEnchantTryS` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `armorEnchantTryS80` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `armorEnchantTryR` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `armorEnchantTryR95` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `armorEnchantTryR99` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `ssConsumedD` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `ssConsumedC` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `ssConsumedB` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `ssConsumedA` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `ssConsumedS` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `ssConsumedR` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `spsConsumedD` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `spsConsumedC` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `spsConsumedB` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `spsConsumedA` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `spsConsumedS` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `spsConsumedR` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `privateSellCount` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `questsCompleted` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `resurrectedCharCount` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `resurrectedByOtherCount` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `dieCount` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `monstersKilled` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `expFromMonsters` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `maxDamageToMonster` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `allDamageToMonster` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `allDamageFromMonster` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `killedByMonsterCount` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `raidKilled_25774` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `raidKilled_25785` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `raidKilled_29195` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `raidKilled_25779` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `raidKilled_25866` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `raidKilled_29194` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `raidKilled_29218` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `raidKilled_29213` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `raidKilled_29196` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `raidKilled_25867` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `raidKilled_29212` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `raidKilled_29197` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `pkCount` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `pvpCount` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `killedByPkCount` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `killedInPvpCount` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `maxDamageToPc` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `allDamageToPc` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `allDamageFromPc` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `clanMembersCount` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `clanInvitesCount` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `clanLeavedCount` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `clanReputationCount` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `clanAdenaAddedInWh` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `clanPvpCount` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  `clanWarWinCount` BIGINT(20) UNSIGNED NOT NULL DEFAULT '0',
  PRIMARY KEY (`charId`)
) DEFAULT CHARSET=utf8;
