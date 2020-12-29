public class VerusCombatDisabler extends Module {
    public VerusCombatDisabler() {
        super("VerusCombatDisabler", "Disables Verus Combat Checks!", Catagory.MISC);
    }
    var count = 0;
    public void onWorldChange() {
        count = 0;
    }
    public void onPacket(EventPacket event) {
        if(!this.getState()) return;
        Packet packet = event.getPacket();
        if (packet instanceof C0FPacketConfirmTransaction) {
				if(mc.thePlayer.isDead) {
					confirmtranscounter = 0;
				}
				if(confirmtranscounter != 0) event.setCanceled(true);
				confirmtranscounter++;
			} else if(packet instanceof C0BPacketEntityAction) {
				event.setCanceled(true);
			}
    }
}
