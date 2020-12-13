public class ACMCDisabler extends Module {
    public ACMCDisabler() {
        super("ACMCDisabler", "Disables ACMC.pl's Anticheat", Catagory.MISC); 
    }
    ArrayList<Packet> transactions = new ArrayList<Packet>();
    ArrayList<Packet> keepAlives = new ArrayList<Packet>();
    int currentTransaction = 0;
    
    public void onPacket(EventPacket event) {
        if(!this.getState()) return;
        Packet packet = event.getPacket();
        
        if(packet instanceof C0FPacketConfirmTransaction) {
            transactions.add(packet);
            event.setCanceled(true);
        }
        
        if(packet instanceof C00PacketKeepAlive) {
            keepAlives.add(packet);
            event.setCanceled(true);
        }
        
        if(packet instanceof C03PacketPlayer) {
            C03PacketPlayer c03 = (C03PacketPlayer)packet;
            mc.thePlayer.sendQueue.addToSendQueueNoEvent(new C0CPacketInput());
            if(mc.thePlayer.ticksExisted % 15 == 0) {
                c03.y += RandomUtils.randDouble(100, 1000);
            }
        }
    }
    
    public void onEnable() {
       mc.thePlayer.ticksExisted = 0;
    }
    
    public void onDisable() {
       transactions.clear();
       currentTransaction = 0;
       keepAlives.clear();
    }
    
    public void onWorldChange() {
        if(!this.getState()) return;
        transactions.clear();
        currentTransaction = 0;
        keepAlives.clear();
    }
    
    public void onUpdate() {
        if(!this.getState()) return;
        mc.timer.timerSpeed = 0.4F;
        if(mc.thePlayer.ticksExisted % 25 == 0 && (transactions.size()-1) > currentTransaction) {
           mc.thePlayer.sendQueue.addToSendQueueNoEvent(transactions.get(++currentTransaction)));
        }
        if(mc.thePlayer.ticksExisted % 10 == 0) {
           for(Packet p : keepAlives)
              if(p != null) mc.thePlayer.sendQueue.addToSendQueueNoEvent(p);
        }
    }
}
