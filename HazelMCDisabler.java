public class HazelMCDisabler extends Module {
    public LunarGGDisabler() {
        super("HazelMCDisabler", "Disables HazelMC's Anticheat", Catagory.MISC);
    }
    ArrayList<Packet> transactions = new ArrayList<Packet>();
    ArrayList<Packet> KeepAlives = new ArrayList<Packet>();
    int currentTransaction = 0;
    
    public void onPacket(EventPacket event) {
        if(!this.getState()) return;
        Packet packet = event.getPacket();
        
        if(packet instanceof C0FPacketConfirmTransaction) {
            transactions.add(packet);
            event.setCanceled(true);
        }
        
        if(packet instanceof C00PacketKeepAlive) {
            KeepAlives.add(packet);
            event.setCanceled(true);
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
       KeepAlives.clear();
    }
    
    public void onWorldChange() {
        if(!this.getState()) return;
        transactions.clear();
        currentTransaction = 0;
        KeepAlives.clear();
    }
    
    public void onUpdate() {
        if(!this.getState()) return;
        mc.thePlayer.sendQueue.addToSendQueue(new C00PacketKeepAlive(0));
        if(transactions.size() > currentTransaction) {
           mc.thePlayer.sendQueue.addToSendQueueNoEvent(transactions.get(currentTransaction++)));
        }
        if(mc.thePlayer.ticksExisted % 100 == 0) {
            for(Packet p : KeepAlives) {
               if(p != null) mc.thePlayer.sendQueue.addToSendQueueNoEvent(p);
            }
            KeepAlives.clear();
        }
    }
}
