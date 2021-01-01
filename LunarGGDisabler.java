public class LunarGGDisabler extends Module {
    public LunarGGDisabler() {
        super("LunarGGDisabler", "Disables Lunar's Anticheat", Catagory.MISC);
    }
    ArrayList<Packet> transactions = new ArrayList<Packet>();
    int currentTransaction = 0;
    
    public void onPacket(EventPacket event) {
        if(!this.getState()) return;
        Packet packet = event.getPacket();
        
        if(packet instanceof C17PacketCustomPayload) {
            C17PacketCustomPayload pay = (C17PacketCustomPayload)packet;
            if(pay.getChannelName().equalsIgnoreCase("MC|Brand")) {
                ByteArrayOutputStream b = new ByteArrayOutputStream();
    			ByteBuf message = Unpooled.buffer();
    			message.writeBytes("Lunar-Client".getBytes());
    			mc.thePlayer.sendQueue.addToSendQueue(new C17PacketCustomPayload("REGISTER", new PacketBuffer(message)));
            }
        }
        
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
        if(mc.thePlayer.ticksExisted % 120 == 0 && transactions.size() > currentTransaction) {
           mc.thePlayer.sendQueue.addToSendQueueNoEvent(transactions.get(currentTransaction++)));
        }
        if(mc.thePlayer.ticksExisted % 25 == 0) {
           mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer.C06PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + RandomUtils.randFloat(100, 1000), 
                                                                                             mc.thePlayer.posZ, mc.thePlayer.rotationYaw, mc.thePlayer.rotationPitch, true));
        }
        if(mc.thePlayer.ticksExisted % 600 == 0) {
           transactions.clear();
           currentTransaction = 0;
        }
    }
}
