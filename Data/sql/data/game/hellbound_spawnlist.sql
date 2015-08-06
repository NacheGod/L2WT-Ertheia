DROP TABLE IF EXISTS `hellbound_spawnlist`;
CREATE TABLE `hellbound_spawnlist` (
  `npc_templateid` smallint(5) unsigned NOT NULL DEFAULT '0',
  `locx` mediumint(6) NOT NULL DEFAULT '0',
  `locy` mediumint(6) NOT NULL DEFAULT '0',
  `locz` mediumint(6) NOT NULL DEFAULT '0',
  `heading` mediumint(6) NOT NULL DEFAULT '0',
  `respawn_delay` mediumint(5) NOT NULL DEFAULT '0',
  `respawn_random` mediumint(5) NOT NULL DEFAULT '0',
  `min_hellbound_level` tinyint(2) unsigned NOT NULL DEFAULT '0',
  `max_hellbound_level` tinyint(2) unsigned NOT NULL DEFAULT '0'
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

INSERT INTO `hellbound_spawnlist` VALUES
-- NPC's
-- From 1 to the end
(32352,-19648,249424,-3232,16384,60,0,1,100), -- Desert Heatmaster
(32294,-11840,236224,-3272,16384,60,0,1,100), -- Budenka
(32296,-27504,252448,-2256,0,60,0,1,100), -- Seruzia
(32300,-21075,250295,-3295,16384,60,0,1,100), -- Bernarde
(32297,-19904,250016,-3248,12288,60,0,1,100), -- Falk
(32308,-19696,249280,-3234,16384,60,0,1,100), -- Ming
(32298,-4896,255840,-3135,49152,60,0,1,100), -- Hude 
(32356,-4096,255472,-3136,40960,60,0,1,100), -- Jude
(32313,10632,244721,-2008,-32732,60,0,1,100), -- Deltuva 
(32245,8800,251652,-2032,11668,60,0,1,100), -- Matras
(32346,13694,255389,-2019,32768,60,0,1,100), -- Kanaf
(32331,-23797,245142,-3104,0,60,0,6,6), -- Invisible Core
 -- Native Slave
(32357,8670,252518,-1896,0,60,0,1,100), 
(32357,8690,252678,-1928,0,60,0,1,100),
(32357,8756,251730,-2024,0,60,0,1,100),
(32357,8867,251710,-2016,0,60,0,1,100),
(32357,8829,250040,-1835,0,60,0,1,100),
(32357,8809,249960,-1984,0,60,0,1,100),
-- 1'st - 4'th levels
(32345,-11172,236565,-3272,28492,60,0,1,4), -- Buron
(32354,-20939,250329,-3276,16384,60,0,1,4), -- Kief
-- 1'st - 5'th levels
-- Quarry Slave
(32299,-9168,243264,-1856,-13676,180,0,1,5),
(32299,-9200,243328,-1852,31080,180,0,1,5),
(32299,-9168,245008,-1856,11596,180,0,1,5),
(32299,-9216,244912,-1857,32132,180,0,1,5),
(32299,-9344,244288,-1856,24968,180,0,1,5),
(32299,-9392,244144,-1862,-32700,180,0,1,5),
(32299,-8336,245850,-1856,9312,180,0,1,5),
(32299,-8496,245776,-1852,23720,180,0,1,5),
(32299,-7648,246576,-1840,27544,180,0,1,5),
(32299,-7488,246688,-1851,22328,180,0,1,5),
(32299,-2880,242832,-1901,0,180,0,1,5),
(32299,-2960,242736,-1908,-4720,180,0,1,5),
(32299,-6608,240976,-1842,-19164,180,0,1,5),
(32299,-6816,241040,-1840,-24360,180,0,1,5),
(32299,-4864,241136,-1893,-25632,180,0,1,5),
(32299,-4768,240928,-1888,-13272,180,0,1,5),
(32299,-4032,241696,-1846,13004,180,0,1,5),
(32299,-3712,245840,-1857,17468,180,0,1,5),
(32299,-2848,244176,-1865,7608,180,0,1,5),
(32299,-2768,243952,-1878,0,180,0,1,5),
(32299,-4400,245456,-2016,-21612,180,0,1,5),
(32299,-4544,245568,-2033,-19996,180,0,1,5),
(32299,-5472,245680,-2059,-26644,180,0,1,5),
(32299,-6256,245328,-2079,0,180,0,1,5),
(32299,-5824,244944,-2058,16900,180,0,1,5),
(32299,-6464,244608,-2043,-25360,180,0,1,5),
(32299,-7600,245024,-2048,-5852,180,0,1,5),
(32299,-7856,244832,-2048,-8240,180,0,1,5),
(32299,-7104,244336,-2048,-1380,180,0,1,5),
(32299,-7600,244048,-2048,-15076,180,0,1,5),
(32299,-7312,242784,-2032,-4188,180,0,1,5),
(32299,-6496,243296,-2086,10700,180,0,1,5),
(32299,-6432,242848,-2080,4632,180,0,1,5),
(32299,-6340,242010,-2038,14736,180,0,1,5),
(32299,-5968,241744,-2016,10080,180,0,1,5),
(32299,-5392,243584,-2048,-23024,180,0,1,5),
(32299,-5072,243216,-2032,-9772,180,0,1,5),
(32299,-4256,243696,-2040,16668,180,0,1,5),
(32299,-4688,244336,-2080,-5192,180,0,1,5),
(32299,-4976,244064,-2048,29696,180,0,1,5),
-- 3'rd level - Keltas, 2-4h respawn
(22341,-27136,250938,-3523,0,10800,3600,3,3),
-- 4'th level - Keltas, 8-16h respawn
(22341,-27136,250938,-3523,0,43200,14400,4,4),
-- From 5'th to the end
(32345,-28567,248994,-3482,16384,60,0,5,100), -- Buron
(32354,-28357,248993,-3478,16384,60,0,5,100), -- Kief
(32355,-28916,249381,-3478,0,60,0,5,100), -- Solmon
-- Hellbound Insurgent
(32363,-28592,249976,-3478,16384,60,0,5,100),
(32363,-28400,249977,-3478,16384,60,0,5,100),
(32363,-29123,250387,-3478,0,60,0,5,100),
(32363,-28310,250388,-3478,32768,60,0,5,100),
(32363,-25376,252368,-3257,0,60,0,5,100),
(32363,-25376,252208,-3257,0,60,0,5,100),
-- Hellbound Traitor
(32364,-27352,252387,-3521,5416,60,0,5,100),
-- From 9 to the end
(32347,16882,238952,9776,-14916,60,0,9,100); -- Shadai


INSERT INTO `hellbound_spawnlist` VALUES
-- Monsters
-- 1'st level only
-- Remnant Wraith
(22330,-27677,257103,-1930,0,80,0,1,1),
(22330,-26302,256444,-1930,0,80,0,1,1),
(22330,-26677,256849,-1930,0,80,0,1,1),
(22330,-27998,256601,-1930,0,80,0,1,1),
(22330,-27046,254695,-2080,0,80,0,1,1),
(22330,-27533,255424,-2025,0,80,0,1,1),
(22330,-27257,254795,-2080,0,80,0,1,1),
(22330,-27771,255453,-2130,0,80,0,1,1),
(22330,-28706,255333,-2150,0,80,0,1,1),
(22330,-28360,255302,-2170,0,80,0,1,1),
(22330,-28795,255440,-2155,0,80,0,1,1),
(22330,-28218,254820,-2160,0,80,0,1,1),
(22330,-26207,255524,-2085,0,80,0,1,1),
(22330,-26271,254598,-2145,0,80,0,1,1),
(22330,-26454,254604,-2145,0,80,0,1,1),
(22330,-25462,255618,-2135,0,80,0,1,1),
(22330,-25386,257270,-2140,0,80,0,1,1),
(22330,-25474,257360,-2140,0,80,0,1,1),
(22330,-25636,256915,-2115,0,80,0,1,1),
(22330,-25361,256832,-2130,0,80,0,1,1),
-- 1'st - 4'th levels
-- Junior Watchman
(22320,-16812,235255,-2820,0,80,0,1,4),
(22320,-17557,236195,-2800,0,80,0,1,4),
(22320,-17919,236180,-2795,0,80,0,1,4),
(22320,-21234,240285,-2807,0,80,0,1,4),
(22320,-20228,240758,-2860,0,80,0,1,4),
(22320,-19142,239223,-2899,0,80,0,1,4),
(22320,-20225,238019,-2962,0,80,0,1,4),
(22320,-19525,240239,-2860,0,80,0,1,4),
(22320,-10501,237750,-3120,0,80,0,1,4),
(22320,-10315,237442,-3850,0,80,0,1,4),
(22320,-17970,239239,-3345,0,80,0,1,4),
(22320,-17240,239110,-3290,0,80,0,1,4),
(22320,-17837,238631,-3320,0,80,0,1,4),
-- Junior Summoner
(22321,-17247,235256,-2835,0,60,0,1,4),
(22321,-17350,234873,-2730,0,60,0,1,4),
(22321,-15706,234478,-2816,0,60,0,1,4),
(22321,-20515,238473,-2690,0,80,0,1,4),
(22321,-20764,240794,-2848,0,80,0,1,4),
(22321,-20072,239928,-2860,0,80,0,1,4),
(22321,-19837,238139,-2775,0,80,0,1,4),
(22321,-20195,241023,-2864,0,80,0,1,4),
(22321,-10270,237659,-3140,0,80,0,1,4),
(22321,-11142,237759,-3090,0,80,0,1,4),
(22321,-17899,239334,-3350,0,80,0,1,4),
(22321,-17231,238281,-3325,0,80,0,1,4),
(22321,-17768,239231,-3350,0,80,0,1,4),
-- 1'st - 5'th levels
-- Quarry Supervisor
(22344,-6317,243792,-1990,0,80,0,1,5),
(22344,-6542,244016,-1990,0,80,0,1,5),
(22344,-6438,245061,-2100,0,80,0,1,5),
(22344,-5341,243183,-2080,0,80,0,1,5),
(22344,-6818,243456,-2100,0,80,0,1,5),
-- Quarry Bowman
(22345,-5664,244948,-2070,0,80,0,1,5),
(22345,-5544,243743,-2020,0,80,0,1,5),
(22345,-6250,244419,-1968,0,80,0,1,5),
(22345,-6214,244131,-1880,0,80,0,1,5),
(22345,-6432,243646,-2060,0,80,0,1,5),
(22345,-5592,244768,-2050,0,80,0,1,5),
-- Quarry Foreman
(22346,-5585,244854,-2076,27840,60,0,1,5),
(22346,-4788,241228,-1880,0,60,0,1,5),
(22346,-8628,244748,-1872,-25548,60,0,1,5),

-- From 1 to the end
-- Blind Huntsman
(22324,-16124,236779,-3350,0,60,0,1,100),
(22324,-12416,237243,-3190,0,60,0,1,100),
(22324,-11925,239110,-3085,0,60,0,1,100),
(22324,-11973,239219,-3045,0,60,0,1,100),
(22324,-12805,236324,-3300,0,60,0,1,100),
(22324,-13009,238683,-3260,0,60,0,1,100),
(22324,-15617,236067,-3330,0,60,0,1,100),
(22324,-15850,237166,-3380,0,60,0,1,100),
(22324,-14793,237024,-3336,0,60,0,1,100),
(22324,-14771,236029,-3325,0,60,0,1,100),
(22324,-14296,235499,-3264,0,60,0,1,100),
(22324,-17471,234470,-2600,0,60,0,1,100),
(22324,-16902,235279,-2835,0,60,0,1,100),
(22324,-18021,234272,-2508,0,60,0,1,100),
(22324,-17635,235182,-2775,0,60,0,1,100),
(22324,-17142,235414,-2860,0,60,0,1,100),
(22324,-17178,234853,-2735,0,60,0,1,100),
(22324,-17317,235426,-2845,0,60,0,1,100),
(22324,-19852,238967,-2840,0,60,0,1,100),
(22324,-20537,238973,-2770,0,60,0,1,100),
(22324,-20982,239315,-2780,0,60,0,1,100),
(22324,-19919,238857,-2830,0,60,0,1,100),
(22324,-20477,239293,-2816,0,60,0,1,100),
-- Blind Watchman
(22325,-16887,237687,-3340,0,60,0,1,100),
(22325,-14254,237034,-3320,0,60,0,1,100),
(22325,-14387,235830,-3290,0,60,0,1,100),
(22325,-16267,237019,-3340,0,60,0,1,100),
(22325,-14099,236247,-3290,0,60,0,1,100),
(22325,-17179,237829,-3350,0,60,0,1,100),
(22325,-15155,237961,-3365,0,60,0,1,100),
(22325,-15301,237199,-3370,0,60,0,1,100),
(22325,-13265,238265,-3270,0,60,0,1,100),
(22325,-12326,238749,-3270,0,60,0,1,100),
(22325,-12266,237500,-3195,0,60,0,1,100),
(22325,-14197,236118,-3295,0,60,0,1,100),
(22325,-14002,237494,-3295,0,60,0,1,100),
(22325,-16466,234945,-2804,0,60,0,1,100),
(22325,-17039,235749,-2907,0,60,0,1,100),
(22325,-17044,234324,-2600,0,60,0,1,100),
(22325,-16609,234774,-2785,0,60,0,1,100),
(22325,-16908,234812,-2747,0,60,0,1,100),
(22325,-16335,234192,-2710,0,60,0,1,100),
(22325,-18380,236758,-2878,0,60,0,1,100),
(22325,-20027,239605,-2860,0,60,0,1,100),
(22325,-20920,239615,-2800,0,60,0,1,100),
(22325,-20414,240218,-2863,0,60,0,1,100),
(22325,-19656,238448,-2825,0,60,0,1,100),
(22325,-20857,240751,-2843,0,60,0,1,100),
-- Arcane Scout
(22327,-22789,245257,-3140,0,80,0,1,100),
(22327,-23137,244759,-3136,0,80,0,1,100),
(22327,-23900,245928,-3140,0,80,0,1,100),
(22327,-22691,244791,-3130,0,80,0,1,100),
(22327,-22724,245294,-3135,0,80,0,1,100),
(22327,-23009,244320,-3140,0,80,0,1,100),
-- Arcane Guardian
(22328,-24813,245393,-3140,0,80,0,1,100),
(22328,-24888,245975,-3130,0,80,0,1,100),
(22328,-23444,245038,-3140,0,80,0,1,100),
(22328,-23052,244046,-3130,0,80,0,1,100),
(22328,-23881,245382,-3120,0,80,0,1,100),
(22328,-24690,245248,-3140,0,80,0,1,100),
-- Arcane Watchman
(22329,-23054,244620,-3140,0,80,0,1,100),
(22329,-23711,244836,-3140,0,80,0,1,100),
(22329,-25293,244956,-3057,0,80,0,1,100),
(22329,-24261,246159,-3140,0,80,0,1,100),
(22329,-22814,244140,-3130,0,80,0,1,100),
(22329,-23753,243847,-3140,0,80,0,1,100),
-- Sand Scorpion
(22334,-12307,256010,-3355,0,80,0,1,100),
(22334,-11958,253211,-3050,0,80,0,1,100),
(22334,-10009,256688,-3340,0,80,0,1,100),
(22334,-11653,254598,-3230,0,80,0,1,100),
(22334,-6331,254560,-3200,0,80,0,1,100),
(22334,-6659,252859,-3270,0,80,0,1,100),
(22334,-5325,252585,-3320,0,80,0,1,100),
(22334,-7134,254006,-3230,0,80,0,1,100),
(22334,-3768,252624,-3265,0,80,0,1,100),
-- Desert Scorpion
(22335,-16607,250566,-3000,0,80,0,1,100),
(22335,-15639,251281,-3040,0,80,0,1,100),
(22335,-17370,250430,-3040,0,80,0,1,100),
(22335,-15749,251192,-3000,0,80,0,1,100),
(22335,-16846,249702,-2840,0,80,0,1,100),
(22335,-16946,250137,-2930,0,80,0,1,100),
(22335,-18659,253030,-3435,0,80,0,1,100),
(22335,-17587,251226,-3225,0,80,0,1,100),
(22335,-18945,252893,-3425,0,80,0,1,100),
(22335,-16313,252427,-3445,0,80,0,1,100),
(22335,-16145,254324,-3165,0,80,0,1,100),
(22335,-17168,253355,-3440,0,80,0,1,100),
(22335,-21281,253287,-3280,0,80,0,1,100),
(22335,-22186,253414,-3320,0,80,0,1,100),
(22335,-20838,253826,-3220,0,80,0,1,100),
(22335,-21124,254897,-3240,0,80,0,1,100),
(22335,-22224,254445,-3280,0,80,0,1,100),
(22335,-20834,252234,-3345,0,80,0,1,100),
(22335,-19526,254777,-3115,0,80,0,1,100),
(22335,-16487,254403,-3215,0,80,0,1,100),
(22335,-20194,256229,-3190,0,80,0,1,100),
(22335,-19022,255662,-3150,0,80,0,1,100),
(22335,-19304,255336,-3150,0,80,0,1,100),
(22335,-18410,255793,-3150,0,80,0,1,100),
(22335,-19201,257620,-3150,0,80,0,1,100),
(22335,-19252,256894,-3145,0,80,0,1,100),
(22335,-20935,256375,-3170,0,80,0,1,100),
(22335,-19047,258367,-3090,0,80,0,1,100),
(22335,-20216,256577,-3180,0,80,0,1,100),
(22335,-18558,257720,-3090,0,80,0,1,100),
-- Sand Devil
(22336,-10782,255723,-3285,0,90,0,1,100),
(22336,-7566,251242,-2990,0,90,0,1,100),
(22336,-9595,256581,-3310,0,90,0,1,100),
(22336,-3487,252837,-3190,0,90,0,1,100),
(22336,-5049,250979,-3220,0,90,0,1,100),
(22336,-4952,254498,-3140,0,90,0,1,100),
-- Desiccator
(22337,-17040,249528,-2840,0,90,0,1,100),
(22337,-18081,249656,-3010,0,90,0,1,100),
(22337,-17834,249737,-2990,0,90,0,1,100),
(22337,-15889,250715,-2960,0,90,0,1,100),
(22337,-15458,253052,-3455,0,90,0,1,100),
(22337,-19197,253546,-3440,0,90,0,1,100),
(22337,-17195,250939,-3155,0,90,0,1,100),
(22337,-17839,250321,-3065,0,90,0,1,100),
(22337,-21799,252680,-3350,0,90,0,1,100),
(22337,-21483,254908,-3260,0,90,0,1,100),
(22337,-19640,254137,-3170,0,90,0,1,100),
(22337,-20375,252942,-3390,0,90,0,1,100),
(22337,-19831,255902,-3180,0,90,0,1,100),
(22337,-17926,256680,-3160,0,90,0,1,100),
(22337,-17429,255843,-3185,0,90,0,1,100),
(22337,-19081,256062,-3140,0,90,0,1,100),
(22337,-19747,256876,-3160,0,90,0,1,100),
(22337,-19244,258141,-3105,0,90,0,1,100),
(22337,-20187,257911,-3090,0,90,0,1,100),
(22337,-11886,252693,-2960,0,90,0,1,100),
(22337,-10820,253080,-3135,0,90,0,1,100),
(22337,-8352,256976,-3110,0,90,0,1,100),
(22337,-4580,250633,-3190,0,90,0,1,100),
(22337,-5258,249929,-3120,0,90,0,1,100),
(22337,-5005,253017,-3330,0,90,0,1,100),
-- Wandering Caravan
(22339,-16147,249832,-2750,0,60,0,1,100),
(22339,-18073,249640,-3000,0,60,0,1,100),
(22339,-18164,254104,-3430,0,60,0,1,100),
(22339,-14856,253668,-3460,0,60,0,1,100),
(22339,-20707,254460,-3170,0,60,0,1,100),
(22339,-22011,254509,-3270,0,60,0,1,100),
(22339,-16582,255600,-3190,0,60,0,1,100),
(22339,-18759,255055,-3130,0,60,0,1,100),
(22339,-19443,257922,-3120,0,60,0,1,100),
(22339,-20897,257439,-3060,0,60,0,1,100),
(22339,-20049,258366,-3035,0,60,0,1,100),
(22339,-19765,258270,-3075,0,60,0,1,100),
-- Sandstorm
(22340,-17818,249130,-2830,0,80,0,1,100),
(22340,-16688,249359,-2760,0,80,0,1,100),
(22340,-17904,253279,-3435,0,80,0,1,100),
(22340,-17261,251262,-3410,0,80,0,1,100),
(22340,-21118,252079,-3340,0,80,0,1,100),
(22340,-19992,254331,-3150,0,80,0,1,100),
(22340,-16503,254905,-3215,0,80,0,1,100),
(22340,-15948,254767,-3180,0,80,0,1,100),
(22340,-20596,257228,-3110,0,80,0,1,100),
(22340,-19340,258553,-3090,0,80,0,1,100),
(32350,-19278,253338,-3440,0,60,0,1,100),
(32350,-21467,255362,-3255,0,60,0,1,100),
(32350,-19351,251840,-3295,0,60,0,1,100),
(32350,-18875,252677,-3405,0,60,0,1,100),
(32350,-17991,253999,-3430,0,60,0,1,100),
(32350,-16420,252222,-3430,0,60,0,1,100),
(32350,-21289,255042,-3260,0,60,0,1,100),
(32350,-20286,255521,-3215,0,60,0,1,100),
(32350,-19403,256409,-3150,0,60,0,1,100),
(32350,-20526,253695,-3220,0,60,0,1,100),
(22340,-10865,254477,-3200,0,80,0,1,100),
(22340,-9365,252902,-3055,0,80,0,1,100),
(22340,-3779,251875,-3310,0,80,0,1,100),
(22340,-6494,254591,-3230,0,80,0,1,100),
(32350,-10421,253922,-3160,0,60,0,1,100),
(32350,-4689,252828,-3330,0,60,0,1,100),
(32350,-12001,253958,-3130,0,60,0,1,100),
(32350,-7702,253027,-3150,0,60,0,1,100),
(32350,-6975,252302,-3175,0,60,0,1,100),
(32350,-4989,250471,-3125,0,60,0,1,100),
(32350,-11408,254989,-3260,0,60,0,1,100),
(32350,-6392,254294,-3255,0,60,0,1,100),
(32350,-8549,251605,-2945,0,60,0,1,100),
(32350,-9611,251318,-2925,0,60,0,1,100),
-- 2'nd - 4'th levels
-- Remnant Diabolist
(18463,-27503,256893,-1930,0,80,0,2,4),
(18463,-27560,256513,-1925,0,80,0,2,4),
(18463,-27580,255450,-2050,0,80,0,2,4),
(18463,-26804,254905,-2135,0,80,0,2,4),
(18463,-28494,255497,-2147,0,80,0,2,4),
(18463,-28403,254959,-2165,0,80,0,2,4),
(18463,-25838,254795,-2150,0,80,0,2,4),
(18463,-26439,255124,-2134,0,80,0,2,4),
(18463,-25301,256711,-2130,0,80,0,2,4),
(18463,-25641,256890,-2116,0,80,0,2,4),
-- Remnant Diviner
(18464,-26817,256360,-1915,0,80,0,2,4),
(18464,-28272,256980,-1930,0,80,0,2,4),
(18464,-27645,254763,-2085,0,80,0,2,4),
(18464,-26826,254904,-2140,0,80,0,2,4),
(18464,-28866,255082,-2160,0,80,0,2,4),
(18464,-28229,255159,-2168,0,80,0,2,4),
(18464,-25713,255574,-2144,0,80,0,2,4),
(18464,-25536,254956,-2144,0,80,0,2,4),
(18464,-25497,256924,-2125,0,80,0,2,4),
(18464,-25744,256265,-2093,0,80,0,2,4),
-- Remnant Wraith
(22330,-28138,256568,-1935,0,60,0,2,4),
(22330,-28323,256642,-1930,0,60,0,2,4),
(22330,-28354,257228,-1930,0,60,0,2,4),
(22330,-27650,254992,-2070,0,60,0,2,4),
(22330,-26827,255078,-2100,0,60,0,2,4),
(22330,-27356,255190,-2040,0,60,0,2,4),
(22330,-28205,255668,-2078,0,60,0,2,4),
(22330,-28669,255627,-2160,0,60,0,2,4),
(22330,-28926,255460,-2165,0,60,0,2,4),
(22330,-26220,255504,-2090,0,60,0,2,4),
(22330,-25493,254515,-2138,0,60,0,2,4),
(22330,-26044,255340,-2136,0,60,0,2,4),
(22330,-25347,256412,-2150,0,60,0,2,4),
(22330,-25299,257741,-2130,0,60,0,2,4),
(22330,-25542,256197,-2155,0,60,0,2,4);

INSERT INTO `hellbound_spawnlist` VALUES
-- 3'rd - 4'th levels
-- Native's Corpse
(32306,-26768,251968,-3481,45056,60,0,3,4),
(32306,-27296,251872,-3521,53248,60,0,3,4),
(32306,-27968,251120,-3521,53248,60,0,3,4),
(32306,-26848,251360,-3521,36864,60,0,3,4),
(32306,-27232,250800,-3521,49152,60,0,3,4),
(32306,-27328,251328,-3521,8192,60,0,3,4),
(32306,-28736,250848,-3521,28672,60,0,3,4),
(32306,-28880,251008,-3521,0,60,0,3,4),
(32306,-28768,251216,-3521,0,60,0,3,4),
(32306,-28544,251008,-3521,32768,60,0,3,4),
(32306,-28640,251776,-3521,0,60,0,3,4),
(32306,-28832,252176,-3521,36864,60,0,3,4),
(32306,-28112,252064,-3521,20480,60,0,3,4),
(32306,-27984,251584,-3521,45056,60,0,3,4),
(32306,-26896,252208,-3521,61440,60,0,3,4),
(32306,-27248,252528,-3521,0,60,0,3,4),
(32306,-29072,252736,-3521,0,60,0,3,4),
(32306,-28640,252432,-3521,53248,60,0,3,4),


-- From 5'th level to the end
-- Junior Watchman
(22320,-16920,235757,-2890,0,60,0,5,100),
(22320,-17866,236108,-2800,0,60,0,5,100),
(22320,-16754,234598,-2700,0,60,0,5,100),
(22320,-16028,234191,-2745,0,60,0,5,100),
(22320,-15344,234153,-2875,0,60,0,5,100),
(22320,-17143,236092,-3045,0,60,0,5,100),
(22320,-20888,238719,-2630,0,60,0,5,100),
(22320,-20011,238017,-2733,0,60,0,5,100),
(22320,-21064,239874,-2800,0,60,0,5,100),
(22320,-20289,239383,-2840,0,60,0,5,100),
(22320,-20403,238425,-2709,0,60,0,5,100),
(22320,-20118,238379,-2750,0,60,0,5,100),
(22320,-19828,238816,-2835,0,60,0,5,100),
(22320,-20357,240546,-2860,0,60,0,5,100),
(22320,-20096,238677,-2800,0,60,0,5,100),
(22320,-19622,238589,-2835,0,60,0,5,100),
(22320,-10686,237614,-3125,0,60,0,5,100),
(22320,-10953,237634,-3100,0,60,0,5,100),
(22320,-10743,237553,-3120,0,60,0,5,100),
(22320,-10332,237630,-3150,0,60,0,5,100),
(22320,-17662,238847,-3334,0,60,0,5,100),
(22320,-17699,239775,-3306,0,60,0,5,100),
(22320,-17064,238966,-3277,0,60,0,5,100),
(22320,-17955,239631,-3360,0,60,0,5,100),
(22320,-18415,239743,-3327,0,60,0,5,100),
(22320,-17319,238629,-3330,0,60,0,5,100),
-- Junior Summoner
(22321,-17156,234340,-2600,0,60,0,5,100),
(22321,-17850,234647,-2615,0,60,0,5,100),
(22321,-17694,234978,-2724,0,60,0,5,100),
(22321,-18040,236508,-2850,0,60,0,5,100),
(22321,-17137,234705,-2700,0,60,0,5,100),
(22321,-17169,235011,-2770,0,60,0,5,100),
(22321,-20297,239051,-2805,0,60,0,5,100),
(22321,-19769,240624,-2860,0,60,0,5,100),
(22321,-20629,240148,-2835,0,60,0,5,100),
(22321,-19417,238807,-2860,0,60,0,5,100),
(22321,-20623,240344,-2850,0,60,0,5,100),
(22321,-20427,239900,-2852,0,60,0,5,100),
(22321,-20176,239302,-2845,0,60,0,5,100),
(22321,-19766,239306,-2862,0,60,0,5,100),
(22321,-19524,239598,-2862,0,60,0,5,100),
(22321,-20192,239892,-2860,0,60,0,5,100),
(22321,-10805,237616,-3105,0,60,0,5,100),
(22321,-10256,237334,-3150,0,60,0,5,100),
(22321,-10245,237540,-3150,0,60,0,5,100),
(22321,-10387,237221,-3145,0,60,0,5,100),
(22321,-18014,239263,-3336,0,60,0,5,100),
(22321,-17140,238909,-3310,0,60,0,5,100),
(22321,-18254,239650,-3330,0,60,0,5,100),
(22321,-17758,239635,-3342,0,60,0,5,100),
(22321,-17693,239017,-3338,0,60,0,5,100),
(22321,-16844,238250,-3345,0,60,0,5,100),
-- Remnant Wraith
(22330,-26764,257254,-1935,0,80,0,5,100),
(22330,-27220,256594,-1927,0,80,0,5,100),
(22330,-27305,257002,-1935,0,80,0,5,100),
(22330,-28400,256672,-1933,0,80,0,5,100),
(22330,-27005,254513,-2100,0,80,0,5,100),
(22330,-27794,255247,-2137,0,80,0,5,100),
(22330,-27270,255309,-2026,0,80,0,5,100),
(22330,-27485,254630,-2090,0,80,0,5,100),
(22330,-29142,255309,-2167,0,80,0,5,100),
(22330,-28425,255413,-2162,0,80,0,5,100),
(22330,-28739,254607,-2166,0,80,0,5,100),
(22330,-28494,255574,-2138,0,80,0,5,100),
(22330,-25963,255536,-2135,0,80,0,5,100),
(22330,-26284,255040,-2138,0,80,0,5,100),
(22330,-26176,254826,-2149,0,80,0,5,100),
(22330,-25498,254693,-2144,0,80,0,5,100),
(22330,-25514,256801,-2131,0,80,0,5,100),
(22330,-25613,256502,-2143,0,80,0,5,100),
(22330,-25308,257446,-2143,0,80,0,5,100),
(22330,-25484,257204,-2138,0,80,0,5,100),
-- Hellbound Native
(32362,-27867,251228,-3523,0,60,0,5,100),
(32362,-27816,251972,-3523,0,60,0,5,100),
(32362,-28752,251948,-3523,0,60,0,5,100),
(32362,-28538,252041,-3527,0,60,0,5,100),
(32362,-28835,248999,-3483,0,60,0,5,100),
(32362,-28913,249427,-3479,0,60,0,5,100),
(32362,-27256,250872,-3523,0,60,0,5,100),
(32362,-27139,251154,-3527,0,60,0,5,100),
(32362,-27156,251391,-3523,0,60,0,5,100),
(32362,-27066,251031,-3523,0,60,0,5,100),
(32362,-29489,252871,-3523,0,60,0,5,100),
(32362,-29477,252673,-3523,0,60,0,5,100),
(32362,-28476,251192,-3523,0,60,0,5,100),
(32362,-28443,250510,-3527,0,60,0,5,100),
-- 6'th level only
-- Naia Failan
(18484,-23428,245322,-3142,0,300,150,6,6),
(18484,-23627,244925,-3138,0,300,150,6,6),
(18484,-23199,245080,-3138,0,300,150,6,6),
(18484,-24270,245074,-3132,0,300,150,6,6),
(18484,-23798,245110,-3109,0,300,150,6,6),
(18484,-24409,244729,-3138,0,300,150,6,6),
-- From 6'th level to the end
-- Caravan Supporter
(32361,-27113,251066,-3527,0,180,150,6,100),
(32361,-27134,251132,-3524,0,180,150,6,100),
(32361,-27028,250962,-3523,0,180,150,6,100),
(32361,-28318,250888,-3527,0,180,150,6,100),
(32361,-28389,250810,-3523,0,180,150,6,100),
(32361,-28353,251012,-3523,0,180,150,6,100),
(32361,-29146,250789,-3523,0,180,150,6,100),
(32361,-29054,250966,-3523,0,180,150,6,100),
(32361,-29137,250985,-3523,0,180,150,6,100),
(32361,-29463,253136,-3523,0,180,150,6,100),
(32361,-29381,253150,-3523,0,180,150,6,100),
(32361,-29523,253124,-3523,0,180,150,6,100),
(32361,-27594,252049,-3523,0,180,150,6,100),
(32361,-27588,252081,-3523,0,180,150,6,100),
(32361,-28138,252055,-3523,0,180,150,6,100),
(32361,-28678,249023,-3483,0,60,150,6,100),
(32361,-28766,249135,-3483,0,60,150,6,100),
(32361,-28638,249404,-3483,0,60,150,6,100),
-- From 7'th level to 8'th level
(18466,4912,244032,-1930,36864,60,0,7,8), -- Outpost Captain
-- Enceinte Defender
(22357,5440,243488,-1595,36864,60,0,7,8),
(22357,4800,244800,-1595,36864,60,0,7,8),
-- Enceinte Defender
(22358,4867,244495,-1930,36863,60,0,7,8),
(22358,5282,243755,-1930,36863,60,0,7,8),
(22358,5529,243572,-1595,36863,60,0,7,8),
(22358,4896,244816,-1595,36864,60,0,7,8),
-- From 7'th level to 13'th level
-- Chimera of Earth
(22349,2415,233687,-3336,0,20,20,7,13),
(22349,4397,235715,-3263,0,20,20,7,13),
(22349,2766,238930,-3177,0,20,20,7,13),
(22349,1170,237034,-3248,0,20,20,7,13),
(22349,7374,240648,-2043,0,20,20,7,13),
(22349,8162,239229,-2019,0,20,20,7,13),
(22349,8840,234564,-1969,0,20,20,7,13),
(22349,10520,234416,-1955,0,20,20,7,13),
-- Chimera of Darkness
(22350,1620,235484,-3363,0,20,20,7,13),
(22350,3699,235713,-3353,0,20,20,7,13),
(22350,3314,237142,-3417,0,20,20,7,13),
(22350,2753,237424,-3410,0,20,20,7,13),
(22350,8874,240166,-2010,0,20,20,7,13),
(22350,6490,239586,-1956,0,20,20,7,13),
(22350,9396,235635,-1970,0,20,20,7,13),
(22350,7967,237212,-1993,0,20,20,7,13),
-- Chimera of Wind
(22351,2005,235154,-3365,0,20,20,7,13),
(22351,4079,234512,-3275,0,20,20,7,13),
(22351,4201,238841,-3206,0,20,20,7,13),
(22351,1573,238509,-3098,0,20,20,7,13),
(22351,7692,240562,-2063,0,20,20,7,13),
(22351,7196,241087,-2024,0,20,20,7,13),
(22351,11154,238599,-1958,0,20,20,7,13),
(22351,11589,238492,-1980,0,20,20,7,13),
-- Chimera of Fire
(22352,2832,234939,-3340,0,20,20,7,13),
(22352,2123,234102,-3363,0,20,20,7,13),
(22352,2012,237572,-3303,0,20,20,7,13),
(22352,2605,236762,-3424,0,20,20,7,13),
(22352,8663,239780,-2052,0,20,20,7,13),
(22352,7436,238617,-2005,0,20,20,7,13),
(22352,9683,238403,-2008,0,20,20,7,13),
(22352,10339,235271,-2000,0,20,20,7,13),
-- Celtus
(22353,3678,233418,-3319,0,5400,3600,7,8);

INSERT INTO `hellbound_spawnlist` VALUES
-- From 9'th level to 13'th level
-- Chimera of Earth
(22349,8367,235138,-1980,0,20,20,9,13),
(22349,11063,239047,-1965,0,20,20,9,13),
(22349,6791,241014,-2019,0,20,20,9,13),
(22349,16487,278417,-9694,0,20,20,9,13),
-- Chimera of Darkness
(22350,10539,238859,-2002,0,20,20,9,13),
(22350,7267,235331,-1978,0,20,20,9,13),
(22350,6823,240524,-1960,0,20,20,9,13),
(22350,15805,278901,-9705,0,20,20,9,13),
-- Chimera of Wind
(22351,9204,234025,-1843,0,20,20,9,13),
(22351,8858,235244,-1980,0,20,20,9,13),
(22351,7130,239026,-2022,0,20,20,9,13),
(22351,16272,278938,-9709,0,20,20,9,13),
-- Chimera of Fire
(22352,10257,237186,-1978,0,20,20,9,13),
(22352,8462,236126,-1948,0,20,20,9,13),
(22352,8304,240560,-2056,0,20,20,9,13),
(22352,15541,278057,-9705,0,20,20,9,13),
-- Celtus
(22353,10734,235230,-1985,0,5400,3600,9,13),
(22353,8130,239963,-2090,0,5400,3600,9,13),
(22353,17483,277611,-9705,0,5400,3600,9,13),
-- From 10'th level to the end
-- Foundry Laborer
(22396,26224,246656,-3200,30572,80,0,10,100),
(22396,26160,246544,-3200,25704,80,0,10,100),
(22396,26048,246464,-3200,22316,80,0,10,100),
(22396,25936,246416,-3200,17496,80,0,10,100),
(22396,27984,248752,-3184,29124,80,0,10,100),
(22396,27872,248672,-3200,25216,80,0,10,100),
(22396,27776,248592,-3200,21892,80,0,10,100),
(22396,27712,248512,-3216,15124,80,0,10,100),
(22396,27107,246019,-3664,19608,80,0,10,100),
(22396,27280,246144,-3664,26356,80,0,10,100),
(22396,27408,246416,-3680,-30072,80,0,10,100),
(22396,27376,246272,-3664,28056,80,0,10,100),
(22396,27892,246256,-3696,-10584,80,0,10,100),
(22396,27984,246288,-3696,-10636,80,0,10,100),
(22396,28656,246128,-3696,-29408,80,0,10,100),
(22396,28544,246288,-3696,-21140,80,0,10,100),
(22396,28592,246208,-3696,-25144,80,0,10,100),
(22396,28096,246288,-3696,-21640,80,0,10,100),
(22396,28656,245728,-3696,-25188,80,0,10,100),
(22396,28688,245552,-3696,27356,80,0,10,100),
(22396,28496,245536,-3696,8272,80,0,10,100),
(22396,28128,245552,-3696,22648,80,0,10,100),
(22396,27968,245600,-3696,8540,80,0,10,100),
(22396,27888,245744,-3696,-396,80,0,10,100),
(22396,28384,244064,-3696,11888,80,0,10,100),
(22396,28336,244192,-3696,512,80,0,10,100),
(22396,28368,244320,-3696,-10236,80,0,10,100),
(22396,29008,243936,-3696,26896,80,0,10,100),
(22396,28912,243872,-3696,21380,80,0,10,100),
(22396,28800,243856,-3696,14888,80,0,10,100),
(22396,29120,244496,-3696,-10140,80,0,10,100),
(22396,29184,244288,-3696,15864,80,0,10,100),
(22396,29312,244416,-3696,-30748,80,0,10,100),
(22396,28704,244736,-3696,-11756,80,0,10,100),
(22396,28832,244720,-3696,-15492,80,0,10,100),
(22396,28624,244688,-3696,-4692,80,0,10,100),
-- Foundry Foreman
(22397,26224,246480,-3216,26720,120,0,10,100),
(22397,27968,248656,-3200,25816,120,0,10,100),
(22397,27392,246176,-3664,29584,120,0,10,100),
(22397,28416,245600,-3696,428,120,0,10,100),
(22397,27920,245840,-3696,-9612,120,0,10,100),
(22397,28224,246224,-3696,25308,120,0,10,100),
(22397,28528,245984,-3696,0,120,0,10,100),
(22397,28945,244643,-3696,25876,120,0,10,100),
(22397,29184,244192,-3696,13188,120,0,10,100),
(22397,28672,244000,-3696,-7332,120,0,10,100),
(22397,28448,244384,-3696,-17792,120,0,10,100),
-- Foundry Foreman
(22403,26080,246368,-3200,17944,120,0,10,100),
(22403,27840,248560,-3200,21092,120,0,10,100),
(22403,27273,245990,-3664,23116,120,0,10,100),
(22403,28208,245616,-3696,31856,120,0,10,100),
(22403,27936,246016,-3696,14964,120,0,10,100),
(22403,28320,246208,-3696,11176,120,0,10,100),
(22403,28528,245840,-3696,0,120,0,10,100),
(22403,29056,244560,-3696,0,120,0,10,100),
(22403,29072,244048,-3696,-24132,120,0,10,100),
(22403,28528,244080,-3696,28480,120,0,10,100),
(22403,28592,244528,-3696,4964,120,0,10,100);

INSERT INTO `hellbound_spawnlist` VALUES
-- Raids
(22448,-27681,252621,-3524,0,51840,34560,5,100), -- Leodas
(22326,-23965,245552,-3138,0,0,0,6,6), -- Hellinark
(18465,-26578,257101,-1930,0,0,0,4,4); -- Derek