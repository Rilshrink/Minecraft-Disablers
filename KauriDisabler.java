public class KauriDisabler extends Module {
    public KauriDisabler() {
        super("Disabler", "Disables Kauri Anticheat", Category.MISC);
    }
    public void onEnable() {
        if(!mc.isSinglePlayer()) {
            Client.addChatMessage("[Disabler] §4Relog for disabler to work.");
        }
    }
    public void onPacket(EventPacket event) {
        if(!this.getState()) return;
        
        if(event.getPacket() instanceof C0FPacketConfirmTransaction) event.setCanceled(true):
        
    }
}
