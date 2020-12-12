public class OnlyMCDisabler extends Module {
    public OnlyMCDisabler() {
        super("OnlyMCDisabler", "Disables OnlyMC's Anticheat", Catagory.MISC); // They use a old version of verus
    }
    ArrayList<Packet> transactions = new ArrayList<Packet>();
    int currentTransaction = 0;
    
    public void onPacket(EventPacket event) {
        if(!this.getState()) return;
        Packet packet = event.getPacket();
        
        if(packet instanceof C0FPacketConfirmTransaction) {
            transactions.add(packet);
            event.setCanceled(true);
        }
        
        if(packet instanceof C00PacketKeepAlive) {
            ((C00PacketKeepAlive)packet).key -= RandomUtils.random(1, 2147483647);
        }
        
        if(packet instanceof C03PacketPlayer) {
            mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0CPacketInput());
        }
    }
    
    public void onEnable() {
       mc.thePlayer.ticksExisted = 0;
    }
    
    public void onDisable() {
       transactions.clear();
       currentTransaction = 0;
    }
    
    public void onWorldChange() {
        if(!this.getState()) return;
        transactions.clear();
        currentTransaction = 0;
    }
    
    public void onUpdate() {
        if(!this.getState()) return;
        if(mc.thePlayer.ticksExisted % 50 == 0) {
           double rand = RandomUtils.randDouble(1000, 2000);
           mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosLook(mc.thePlayer.posX, mc.thePlayer.posY - rand, mc.thePlayer.posZ, 
                                                                    mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, mc.thePlayer.onGround));
        }
        if(mc.thePlayer.ticksExisted % 120 == 0 && transactions.size() > currentTransaction) {
           mc.thePlayer.sendQueue.addToSendQueueNoEvent(transactions.get(++currentTransaction)));
        }
    }
}
