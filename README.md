# Minecraft-Disablers
A list of known Minecraft Anticheat Disablers, this is mostly so the Dev's of the Anticheats know how to patch them.

All of these disablers are patched and no longer work, and since I have no intention of updating these, I will be archiving the repo.
But will provide a short explanation on why I believe these worked.

Current Disablers: <br>
Kauri, worked because the anticheat required the client to sync at least one transaction packet, but by not sending one the anticheat would just disable all checks until it did. <br>
OnlyMC, worked because delaying transactions disabled a majority of the checks, and then the flight and speed checks were fully disabled by forcing a flag every 50 ticks, sending the C0CPacketInput also disabled some speed checks.  <br>
LunarGG, worked the same as OnlyMC disabler, but with a client spoofer built in and different delays. <br>
HazelMC, worked by delaying keep alives and transactions caused the anticheat to 'loosen' up on it's checks, sending an invalid keep alive packet every tick also disabled most of the checks, most likely the ping spoof checks as well.<br>
Mineplex Combat, since the combat checks relied on the keep alive packets, not sending them wasn't an option, but sending invalid ones was, and caused the server to disable auto-bans, but it still flagged,<br>
Verus Combat, worked by disabling the syncing of your sprint state to the server and by canceling transactions. You had to send at least one transaction packet otherwise it would just flag you for ping spoofing. This disabled a majority but not all of the combat checks. <br>
ACMC Disabler, worked by slowing down the game timer to not trigger any timer checks, force flagging every 15 ticks to disable flight checks, and delaying transactions and keepalives disabled a lot of checks on it's own, mostly the combat and movement checks.<br>
Cold Network, worked the same as the other Verus disablers, except they used a newer version where it was patched and would ban after 10 minutes of play, flagged ping spoof every 2-3 minutes. <br>
EarthPolMC disabler, worked by making matrix anticheat think you were connecting to the server through geyser by sending a a custom payload through their plugin channel, this was patched soon after it was discovered by making it only accept the custom payloads from the server or proxy.
<br>
Disabler.js was for Liquidbounce
