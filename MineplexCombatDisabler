public class MineplexCombatDisabler extends Module {
    public MineplexCombatDisabler() {
        super("MineplexCombatDisabler", "Allows you to use 6+ block reach without ban!", Catagory.MISC);
    }
    public void onPacket(EventPacket event) {
        if(!this.getState()) return;
        Packet packet = event.getPacket();
        if(packet instanceof C00PacketKeepAlive) {
            ((C00PacketKeepAlive)packet).key -= RandomUtils.random(1000, 2147483647);
        }
    }
}
